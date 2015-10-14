package com.sxb.parase.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class main {
    private static final String DB_NAME = "bk_asr_notice.db";
    /**
     * @param args
     */
    public static void main(String[] args) {
//        TestAccount.testAccout();
//        TestReminder.testReminder();
        String result = "大家好，我来了。你是谁？,";
        String regex = "(?<=花)\\d+\\.\\d{2}";
//        String recFullString = result.replaceAll("。|？|，|,", ""); 
//        System.out.println(recFullString);
        
        Pattern pattern =Pattern.compile(regex);
        Matcher matcher = pattern.matcher("花12.43买车16.36的方式发到");
        if (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

}
