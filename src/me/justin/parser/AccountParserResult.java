package me.justin.parser;

public class AccountParserResult {
    private int type;
    private String typeDes;
    private double amount;
    
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getTypedescription() {
        String des = "无法解析";
        if (type == AccountParser_v1.TYPE_EXPAND) {
            des = "支出";
        } else if (type == AccountParser_v1.TYPE_INCOME) {
            des = "收入";
        } else  {
            des = "无法解析";
        }
        return des;
    }
    
    public boolean isAccount() {
        return type == AccountParser_v1.TYPE_EXPAND || type == AccountParser_v1.TYPE_INCOME;
    }
    
    public boolean isExpand(){
        return type == AccountParser_v1.TYPE_EXPAND; 
    }
    
    public boolean isIncome() {
        return type == AccountParser_v1.TYPE_INCOME;
    }
    
}
