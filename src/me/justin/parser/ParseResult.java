package me.justin.parser;

public class ParseResult {

    
    public final static int TYPE_REMIND = 1;
    public final static int TYPE_INCOME = 2;
    public final static int TYPE_EXPAND = 3;
    public final static int TYPE_UNKNOWN = 4;
    
    public final static int TYPE_MEMO = 5;
    public final static int TYPE_ACCOUNT = 6;
    
    private int mType;
    private Object mObject;
    
    public ParseResult (int type, Object obj) {
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
