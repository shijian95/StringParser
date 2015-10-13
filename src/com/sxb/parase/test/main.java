package com.sxb.parase.test;
import java.sql.Connection;
import java.sql.DriverManager;


public class main {
    private static final String DB_NAME = "bk_asr_notice.db";
    /**
     * @param args
     */
    public static void main(String[] args) {
//        TestAccount.testAccout();
//        TestReminder.testReminder();
        String result = "大家好，我来了。你是谁？,";
        String recFullString = result.replaceAll("。|？|，|,", ""); 
        System.out.println(recFullString);
    }

}
