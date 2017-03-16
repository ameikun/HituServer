package com.zju.iot.common;

/**
 * Created by amei on 16-12-14.
 */
public enum  Status {
    //it's normal
    RETURN_OK(0,"OK"),
    // inner server error
    INNER_ERROR(1,"inner server error"),
    // illegal parameters
    ILLEGAL_PARAMS(2,"illegal parameters"),
    // did't pass authentication
    AUTH_FAILED(3,"did't pass authentication"),
    // forbidden service
    SERVICE_BANNED(4,"forbidden service"),
    //no such information
    NO_RESULT(5,"no such information"),
    HAVE_EXISTED(6,"the object is already existed"),
    //类型转化失败
    PARSE_FAILED(7,"convert type failed"),
    USER_NOT_EXISTED(8,"user not existed"),
    UPDATE_FAILED(9,"update the object failed"),
    DELETE_FAILED(10,"delete the object failed"),
    ALREADY_EXISTED(11,"the object has already existed"),
    NO_START_POINT(12,"no start point"),
    NO_SHORTEST_ROUTE_1(13,"did not find shortest route 1"),
    NO_SHORTEST_ROUTE_2(14,"did not find shortest route 2")
    ;
    private final  int code;
    private final  String message;
    private Status(int code,String message){
        this.code = code;
        this.message = message;
    }
    public int getCode(){
        return this.code;
    }
    public String getMessage(){return this.message;}
}
