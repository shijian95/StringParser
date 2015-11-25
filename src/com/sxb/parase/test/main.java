package com.sxb.parase.test;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.justin.parser.AccountParserResult;
import me.justin.parser.AccountParser_v1;
import me.justin.parser.ReminderParser_v1;

public class main {
    private static final String DB_NAME = "bk_asr_notice.db";
    private static final boolean DEBUG = true;
    final static String[][] key_map_reg = {
            { "每(星期|礼拜|周)([1\\-7])", "daysofWeek=group(2)" },
            { "每(星期|礼拜|周)([一二三四五六七])",
                    "daysofWeek=group(2);repeatType=ALARM_REPEAT_TYPE_WEEK" }, };
    final static String KEY_MAP_REGEX_ACCOUNT[][] = {
        { "(卖\\D*)(\\d+\\.\\d{2})", "amount=group(2);type=income" },
        { "(买\\D*)(\\d+\\.\\d{2})", "amount=group(2);type=expand" },
        };
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TestAccount.testAccout();
        // TestReminder.testReminder();
        String string = "今天给了小六子300.00块钱的红包 ";
        String regex = "给([我\\D]*)(\\d{3}\\.\\d{2})\\D*红包";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
//            System.out.println(matcher.group(3));
        }
        
        // String recFullString = result.replaceAll("。|？|，|,", "");
        // System.out.println(recFullString);
//        ReminderParser_v1 parser_v1 = new ReminderParser_v1();
//        parser_v1.parser_time_unit("每星期2中午提醒我");
//        parse_amount("买浴袍一件78.00");
    }

    public static AccountParserResult parse_amount(String content) {
        System.out.println("Regular parser:" + content);
        AccountParserResult result = null;
        HashMap<String, String> value_map = new HashMap<String, String>();
        boolean found = false;
        for (int i = 0; i < KEY_MAP_REGEX_ACCOUNT.length; i++) {
            String regex1 = KEY_MAP_REGEX_ACCOUNT[i][0];
            System.out.println(regex1);
            Pattern pattern = Pattern.compile(regex1);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                if (DEBUG) {
                    System.out.println("group count:" + matcher.groupCount());
                    System.out.println(matcher.group());
                }
                String find_str = matcher.group();
                int index = content.indexOf(find_str);
                int last_index = index+find_str.length();
                if (DEBUG) {
//                System.out.println(index+ " : " + last_index);
//                String begin = content.substring(0, index);
//                String end = content.substring(last_index, content.length());
//                System.out.println(begin+ " : " +find_str+ " : " + end);

//                System.out.println(matcher.group(1));
//                System.out.println(matcher.group(2));
                }
                String value = KEY_MAP_REGEX_ACCOUNT[i][1];
                String[] values = value.split(";"); // daysofWeek=group(2)
                for (String s : values) {
                    String[] expressions = s.split("="); // daysofWeek=group(2)
                    Pattern pattern_v = null;
                    Matcher matcher_v = null;
                    pattern_v = Pattern.compile("group\\((\\d)\\)");
                    matcher_v = pattern_v.matcher(expressions[1]);
                    if (matcher_v.find()) {
//                        System.out.println("group[0] = " + matcher_v.group(0));
//                        System.out.println("group[1] = " + matcher_v.group(1));
//                        System.out.println("group[2] = " + matcher.group(2));
//                        System.out.println("groupCount = " + matcher_v.groupCount());
//                        System.out.println("" + matcher.group(group_num));
                        int group_num = Integer.parseInt(matcher_v.group(1));
                        value_map.put(expressions[0], matcher.group(group_num));
                    } else {
                        value_map.put(expressions[0], expressions[1]);
                    }
                }
                found = true;
                break;
            }
            else {
               // nothing to do
            }
        }
        if (!found) {
            System.out.println("Regular result：nothing");
        } else {
            result = new AccountParserResult();
            String type = value_map.get("type");
            String amount = value_map.get("amount");
            if (type.equalsIgnoreCase("expand")) {
                result.setType(AccountParser_v1.TYPE_EXPAND);
            } else {
                result.setType(AccountParser_v1.TYPE_INCOME);
            }
            result.setAmount(Double.parseDouble(amount));
            System.out.println("Regular result：" + type +":"+ amount);
       }
       return result;
    }

}
