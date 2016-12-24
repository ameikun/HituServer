package com.zju.iot.controller;

import com.zju.iot.common.Message;
import com.zju.iot.entity.RouteMatrixItem;
import com.zju.iot.service.DirectionService;
import com.zju.iot.service.RouteService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * Created by amei on 16-12-22.
 */
@Controller
@RequestMapping("/direction")
public class DirectionController {
    private static Logger logger = Logger.getLogger(DirectionController.class);
    @Inject
    DirectionService service;

    @RequestMapping("/getRecommendRoute")
    @ResponseBody
    public Message getRecommendRoute(String origin, String destination){
        return service.getRecommendRoute(origin,destination);
    }

    @RequestMapping("/getRecommendRouteDuration")
    @ResponseBody
    public Message getRecommendRouteDuration(String origin, String destination){
        return service.getRecommendRouteDuration(origin,destination);
    }

    @RequestMapping("/test")
    @ResponseBody
    public RouteMatrixItem test(){
        RouteService routeService = new RouteService();
        return routeService.calculateSingleDrivingRoute("40.45,116.34","40.54,116.35");
    }
}
