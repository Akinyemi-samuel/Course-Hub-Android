package com.example.coursehub.environemnt;

public class Environment {

    private static final String STAGING_ENVIRONMENT = "http://10.0.2.2:8080/api/v1/";
    private static final String PRODUCTION_ENVIRONMENT = "https://app-1351da2c-0190-4dfd-98d4-eb3ca4159edc.cleverapps.io/api/v1/";

    static EnvironmentType environmentType = EnvironmentType.PRODUCTION;

    public static String getBaseUrl(){

        switch (environmentType){
            case PRODUCTION :
                return PRODUCTION_ENVIRONMENT;
            case STAGING :
                return STAGING_ENVIRONMENT;
            default:
                return "";
        }
    }


    private enum EnvironmentType{
        PRODUCTION,
        STAGING
    }
}
