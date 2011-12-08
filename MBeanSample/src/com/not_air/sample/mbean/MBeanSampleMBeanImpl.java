package com.not_air.sample.mbean;

public class MBeanSampleMBeanImpl implements MBeanSampleMBean {
    MBeanSample obj;

    public MBeanSampleMBeanImpl() {

    }

    public void attachObject(MBeanSample obj) {
	this.obj = obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.not_air.sample.mbean.MBeanSampleStandardMBean#getMessage()
     */
    @Override
    public String getMessage() {
	return obj.getMessage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.not_air.sample.mbean.MBeanSampleStandardMBean#updateMessage(java.
     * lang.String)
     */
    @Override
    public void updateMessage(String message) {
	obj.updateMessage(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.not_air.sample.mbean.MBeanSampleStandardMBean#shutdown()
     */
    @Override
    public void shutdown() {
	obj.shutdown();
    }

}
