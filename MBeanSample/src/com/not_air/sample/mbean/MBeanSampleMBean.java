package com.not_air.sample.mbean;

public interface MBeanSampleMBean {

    public abstract String getMessage();

    public abstract void updateMessage(String message);

    public abstract void shutdown();

}