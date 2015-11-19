package com.sxb.parase.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.justin.parser.ReminderParser_v1;

public class main {
    private static final String DB_NAME = "bk_asr_notice.db";

    final static String[][] key_map_reg = {
            { "每(星期|礼拜|周)([1\\-7])", "daysofWeek=group(2)" },
            { "每(星期|礼拜|周)([一二三四五六七])",
                    "daysofWeek=group(2);repeatType=ALARM_REPEAT_TYPE_WEEK" }, };

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TestAccount.testAccout();
        // TestReminder.testReminder();
        String result = "大家好，我来了。你是谁？,";
        String regex = "(?<=赢钱)\\d+\\.\\d{2}";
        // String recFullString = result.replaceAll("。|？|，|,", "");
        // System.out.println(recFullString);
        ReminderParser_v1 parser_v1 = new ReminderParser_v1();
        parser_v1.parser_time_unit("每星期2中午提醒我");
    }



}
