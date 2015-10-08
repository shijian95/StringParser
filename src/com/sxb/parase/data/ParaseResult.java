package com.sxb.parase.data;

public class ParaseResult {
    public final static int TYPE_REMIND = 1;
    public final static int TYPE_ACCOUNT = 2;
    public final static int TYPE_MEMO = 4;
    
    public final static int TYPE_INCOME = 5;
    public final static int TYPE_EXPAND = 6;

    
    private int mType;
    private Object mObject;
    
    public ParaseResult (int type, Object obj) {
    	mType = type;
    	mObject = obj;
    }
    
	public int getType() {
		return mType;
	}
	public void setType(int type) {
		this.mType = type;
	}
	public Object getObject() {
		return mObject;
	}
	public void setObject(Object mObject) {
		this.mObject = mObject;
	}
    
    
}
