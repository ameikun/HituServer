package com.zju.iot.service;

import com.zju.iot.common.Message;
import com.zju.iot.common.Status;
import com.zju.iot.dao.PlanDAO;
import com.zju.iot.dao.UserDAO;
import com.zju.iot.entity.GeoMark;
import com.zju.iot.entity.Plan;
import com.zju.iot.map.baidu.Baidu;
import com.zju.iot.map.baidu.entity.BaiduRevGeoCode;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by amei on 16-12-27.
 */
@Component
public class PlanService {
    @Inject
    private PlanDAO planDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private CalculateService calculateService;
    @Inject
    private Baidu map;

    private Message message = new Message();

    /**
     * if successfully add the plan, it will return planID for further use;
     * @param plan
     * @return
     */
    public Message addPlan(Plan plan){
        message.clear();
        if (plan == null || plan.getUserID() == null || plan.getPlanlng() == null || plan.getPlanlat() == null)
            message.setMessage(Status.ILLEGAL_PARAMS);
        else {
            if (userDAO.isUserExistByID(plan.getUserID())) {
                plan.setPlanID(UUID.randomUUID().toString());
                plan.setCreateTime(System.currentTimeMillis());
                plan.setIsCommit(0);
                BaiduRevGeoCode ret = map.getRevGeoCode(new GeoMark("",plan.getPlanlng(),plan.getPlanlat()));
                if ( ret != null && ret.getAddressComponent() != null){
                    plan.setProvince(ret.getAddressComponent().getProvince());
                    plan.setCity(ret.getAddressComponent().getCity());
                }
                if (planDAO.addPlan(plan)){
                    message.setMessage(Status.RETURN_OK);
                    message.setResult(plan.getPlanID());
                }
                else{
                    message.setMessage(Status.INNER_ERROR);
                }
            }
            else {
                message.setMessage(Status.USER_NOT_EXISTED);
            }
        }
        return message;
    }

    /**
     * commit the plan，and programme the plan
     * @param userID
     * @param planID
     * @return the sequence of routes you will trival along with it
     */
    public Message commintPlan(String userID,String planID){
        message.clear();
        if ( userID != null && planID != null){
            Plan plan = planDAO.getPlanByPlanID(userID,planID);
            System.out.println("Plan:"+plan);
            if ( plan == null ){
                message.setMessage(Status.NO_RESULT);
            }
            else{

                Message msg = calculateService.programme(planID);
                System.out.println("is success: "+msg.isSuccess());
                if ( msg.isSuccess() ) {
                    plan.setIsCommit(1);
                    plan.setCommitTime(System.currentTimeMillis());
                    planDAO.updatePlan(plan);
                }
                return msg;
            }
        }
        else {
            message.setMessage(Status.ILLEGAL_PARAMS);
        }
        return  message;
    }

    public Message getCommitPlans(String userID){
        message.clear();
        if ( userID != null){
            ArrayList<Plan> plans = planDAO.getCommitPlans(userID);
            if ( plans == null ){
                message.setMessage(Status.NO_RESULT);
            }
            else{
                message.setMessage(Status.RETURN_OK);
                message.setResult(plans);
            }
        }
        else {
            message.setMessage(Status.ILLEGAL_PARAMS);
        }
        return message;
    }

    public Message getPlanByPlanID(String userID, String planID){
        message.clear();
        if ( userID != null && planID != null){
            Plan plan = planDAO.getPlanByPlanID(userID,planID);
            if ( plan == null ){
                message.setMessage(Status.NO_RESULT);
            }
            else{
                message.setMessage(Status.RETURN_OK);
                message.setResult(plan);
            }
        }
        else {
            message.setMessage(Status.ILLEGAL_PARAMS);
        }
        return  message;
    }

    public Message latestUncommitedPlan(String userID){
        message.clear();
        if ( userID != null ){
            Plan plan = planDAO.latestUncommitedPlan(userID);
            if ( plan != null ){
                message.setMessage(Status.RETURN_OK);
                message.setResult(plan);
            }
            else
                message.setMessage(Status.NO_RESULT);
        }
        else {
            message.setMessage(Status.ILLEGAL_PARAMS);
        }
        return message;
    }

    public Message latestCommitedPlan(String userID){
        message.clear();
        if ( userID != null ){
            Plan plan = planDAO.latestCommitedPlan(userID);
            if ( plan != null ){
                message.setMessage(Status.RETURN_OK);
                message.setResult(plan);
            }
            else
                message.setMessage(Status.NO_RESULT);
        }
        else {
            message.setMessage(Status.ILLEGAL_PARAMS);
        }
        return message;
    }

}
