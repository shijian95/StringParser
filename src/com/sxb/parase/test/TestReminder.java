package com.sxb.parase.test;

import java.util.Calendar;

import me.justin.parser.ReminderParser;
import me.justin.parser.ParseResult;
import me.justin.parser.Parser;

import com.sxb.parase.data.Alarm;
import com.sxb.parase.data.Alarm.DaysOfWeek;

public class TestReminder {
    final static String type_strs[] = { "", "提醒", "收入", "支出", "备忘" };
    final static int DEFAULTHOUR = 9;
    public static final int ALARM_REPEAT_TYPE_NONE = 0;
    public static final int ALARM_REPEAT_TYPE_DAY = 1;
    public static final int ALARM_REPEAT_TYPE_WEEK = 2;
    public static final int ALARM_REPEAT_TYPE_MONTH = 3;
    public static final int ALARM_REPEAT_TYPE_YEAR = 4;
    public static final int ALARM_REPEAT_TYPE_INTERVAL = 5;
    public static final int ALARM_REPEAT_TYPE_STOPWATCH = 6;

    final static String repeat_type_strs[] = { "none", "每天", "每周", "每月", "每年",
            "定时", "倒计时" };


    static void test_32() {
        Alarm expect;
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_MONTH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 10;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        assertReminder("14号到17号，每天上午8点提醒我", expect);
    }
    
    static void test_31() {
        Alarm expect;
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_MONTH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 10;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        assertReminder("每月十号，提醒我领工资", expect);
    }
    
    static void test_30() {
        Alarm expect;
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_MONTH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 1;
        expect.hour= 9;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        assertReminder("每逢1号，上午9点提醒我", expect);
    }
    
    static void test_29() {
        Alarm expect;
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_MONTH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 15;
        expect.hour= 9;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        assertReminder("每逢15号，上午9点提醒我", expect);
    }
    
    static void test_28() {
        Alarm expect;
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_INTERVAL;
        expect.interval = 60*60*1000;
        assertReminder("下星期二提醒我联系小王", expect);
    }
    
    static void test_27() {
        Alarm expect;
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_INTERVAL;
        expect.interval = 60*60*1000;
        assertReminder("每小时提醒我", expect);
    }
    static void test_26() {
        Alarm expect;
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_INTERVAL;
        expect.interval = 30*60*1000;
        assertReminder("每30分钟提醒我", expect);
    }

    static void test_25() {
        Alarm expect;
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);

        expect = new Alarm();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDayOfYear) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 16;
        expect.minutes = 6;
        if ((day == curDayOfYear)
                && (expect.hour < nowHour || expect.hour == nowHour
                        && expect.minutes <= nowMinute || expect.hour == nowHour
                        && expect.minutes == nowMinute
                        && expect.second <= nowSecond)) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.ampm = 2;
        assertReminder("下周一16:06提醒我", expect);
    }
    
    static void test_24() {
        Alarm expect;
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);

        expect = new Alarm();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDayOfYear) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 23;
        expect.minutes = 30;
        if ((day == curDayOfYear)
                && (expect.hour < nowHour || expect.hour == nowHour
                        && expect.minutes <= nowMinute || expect.hour == nowHour
                        && expect.minutes == nowMinute
                        && expect.second <= nowSecond)) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.ampm = 2;
        assertReminder("周一深夜11:30提醒我", expect);
    }
    
    static void test_23() {
        Alarm expect;
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 0;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        expect.minutes = 15;
        expect.second = 0;
        assertReminder("等一下提醒我", expect);
    }
    
    static void test_22() {
        Alarm expect;
        DaysOfWeek daysofWeek;
       Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        expect = new Alarm();
        assertReminder("提前提醒我", expect);
    }
    
    static void test_21() {
        Alarm expect;
        DaysOfWeek daysofWeek;
       Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDayOfYear) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        day =  c.get(Calendar.DAY_OF_YEAR);
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        if (day == curDayOfYear && isTimePassed(expect)) {
            ReminderParser.addOneWeek(expect);
        }
        assertReminder("周四点提醒我面试", expect);
    }

    static void test_20() {
        Alarm expect;
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.MONTH, 1);
        expect.year = c.get(Calendar.YEAR);
        expect.month = 2;
        expect.day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        assertReminder("提醒我2月末买车", expect);
    }

    static void test_week_1() {
        // 周六上午九点提醒我
        Alarm expect = new Alarm();
        com.sxb.parase.data.Alarm.DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);

        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDay) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 9;
        expect.minutes = 0;
        if ((day == curDay)
                && (expect.hour < nowHour || expect.hour == nowHour
                        && expect.minutes <= nowMinute || expect.hour == nowHour
                        && expect.minutes == nowMinute
                        && expect.second <= nowSecond)) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.ampm = 1;
        assertReminder("周六上午九点提醒我", expect);
    }

    static void test_week_2() {
        // 星期天下午五点半提醒我
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);

        Alarm expect = new Alarm();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDay) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 17;
        expect.minutes = 30;
        if ((day == curDay)
                && (expect.hour < nowHour || expect.hour == nowHour
                        && expect.minutes <= nowMinute || expect.hour == nowHour
                        && expect.minutes == nowMinute
                        && expect.second <= nowSecond)) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.ampm = 2;
        assertReminder("星期天下午五点半提醒我", expect);
    }

    static void test_week_3() {
        // 周六23：59分提醒我
        Alarm expect = new Alarm();
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);

        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDay) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 23;
        expect.minutes = 59;
        if ((day == curDay)
                && (expect.hour < nowHour || expect.hour == nowHour
                        && expect.minutes <= nowMinute || expect.hour == nowHour
                        && expect.minutes == nowMinute
                        && expect.second <= nowSecond)) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        assertReminder("周六23：59分提醒我", expect);
    }

    static void test_week_4() {
        // 周一下午3点25分16秒提醒我
        Alarm expect = new Alarm();
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDay) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 15;
        expect.minutes = 25;
        expect.second = 16;
        if ((day == curDay)
                && (expect.hour < nowHour || expect.hour == nowHour
                        && expect.minutes <= nowMinute || expect.hour == nowHour
                        && expect.minutes == nowMinute
                        && expect.second <= nowSecond)) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.ampm = 2;
        assertReminder("周一下午3点25分16秒提醒我", expect);
    }

    static void test_remider_1() {
        // 后天十点半提醒
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_YEAR, 2);
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 10;
        expect.minutes = 30;
        assertReminder("后天十点半提醒", expect);
    }

    static void test_remider_2() {
        // 大后天十点半提醒
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_YEAR, 3);
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 10;
        expect.minutes = 30;
        assertReminder("大后天十点半提醒", expect);
    }

    static void test_remider_4() {
        // 大后天12:35:55提醒
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_YEAR, 3);
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 12;
        expect.minutes = 35;
        expect.second = 55;
        assertReminder("大后天12:35:55提醒", expect);
    }

    // 一个半小时后提醒我
    static void test_remider_5() {
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 3;
        expect.minutes = 30;
        expect.second = 0;
        expect.ampm = 0;
        expect.second = 0;
        assertReminder("3个半小时后提醒我", expect);
    }

    // 今晚9:25提醒我开会
    static void test_remider_6() {
        Alarm expect = new Alarm();
        Calendar c = Calendar.getInstance();
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 21;
        expect.minutes = 25;
        expect.second = 0;
        expect.ampm = 2;
        expect.second = 0;
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("今晚9:25提醒我开会", expect);
    }

    // 今晚9:25提醒我开会
    static void test_remider_7() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 21;
        expect.minutes = 25;
        expect.second = 0;
        expect.ampm = 0;
        expect.second = 0;
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("21:25提醒我开会", expect);
    }

    // 五个小时后提醒我
    static void test_remider_8() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 5;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        expect.second = 0;
        assertReminder("五个小时后提醒我", expect);
    }

    // 晚上六点提醒我朋友请吃饭
    static void test_remider_9() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 18;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        expect.second = 0;
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("晚上六点提醒我朋友请吃饭", expect);
    }

    // 明天上午十点提醒我
    static void test_reminder_10() {
        Alarm expect = new Alarm();
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        // expect.month = 0;
        expect.day += 1;
        expect.hour = 10;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        assignDate(expect);
        assertReminder("明天上午十点提醒我", expect);
    }

    // 从现在起到晚上11点，每隔半小时提醒我
    static void test_interval_1() {
        Alarm expect = new Alarm();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        long now = c.getTimeInMillis();

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        long diff = c.getTimeInMillis() - now;

        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_INTERVAL;
        expect.interval = (30 * 60) * 1000;
        expect.repeatTimes = (int) (diff / expect.interval);
        assertReminder("从现在起到晚上11点，每隔半小时提醒我", expect);
    }

    static void test_reminder_11() {
        Alarm expect = new Alarm();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, maxMonthDay);

        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        if (isTimePassed(expect)) {
            addOneMonth(expect);
        }
        assertReminder("月末提醒我面试", expect);
    }

    // 周一五点提醒我
    static void test_reminder_12() {
        Alarm expect = new Alarm();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDay) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 5;
        expect.minutes = 0;
        expect.second = 0;
        if (day == curDay) {
            // assginWeek(expect, c, nowHour, nowMinute, nowSecond);
        }
        assertReminder("周一五点提醒我", expect);
    }

    static void test_1() {
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.month = 9;
        expect.day = 13;
        expect.hour = 8;
        expect.minutes = 0;
        if (isYearPassed(expect)) {
            expect.year += 1;
        }
        assertReminder("九月十三八点提醒我", expect);
    }

    static void test_2() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        Alarm expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.MONTH, 1);
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        assertReminder("下月末提醒我锻炼", expect);
    }

    static void test_3() {
        // 十分钟后提醒我.rd
        Alarm expect = new Alarm();
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 0;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        expect.minutes = 10;
        expect.second = 0;
        assertReminder("十分钟后提醒我.rd", expect);
    }

    static void test_4() {
        Alarm expect = new Alarm();
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 0;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        expect.minutes = 10;
        expect.second = 0;
        assertReminder("十分钟后提醒我", expect);
    }

    static void test_5() {
        Alarm expect = new Alarm();
        expect = new Alarm();
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.hour = 7;
        expect.minutes = 30;
        expect.second = 0;
        assertReminder("早饭后提醒我。", expect);
    }

    static void test_6() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        Alarm expect = new Alarm();
        expect = new Alarm();
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.hour = 14;
        expect.minutes = 30;
        expect.second = 0;
        expect.ampm = 2;
        if (isTimePassed(expect)) {
            addOneDay(expect);
        }
        assertReminder("下午三点开会，提前半小时提醒我", expect);
    }

    static void test_7() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        Alarm expect = new Alarm();
        expect = new Alarm();
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.hour = 14;
        expect.minutes = 45;
        expect.second = 0;
        expect.ampm = 2;
        if (isTimePassed(expect)) {
            addOneDay(expect);
        }
        assertReminder("下午三点开会，提前十五分钟提醒我", expect);
    }

    static void test_8() {
        Calendar c = Calendar.getInstance();
        Alarm expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_INTERVAL;
        expect.repeatTimes = -1;
        expect.interval = (60 * 60) * 1000;
        assertReminder("每隔一小时提醒我", expect);
    }

    static void test_9() {
        Calendar c = Calendar.getInstance();
        Alarm expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        int curDay = c.get(Calendar.DAY_OF_MONTH);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int curMonth = c.get(Calendar.MONTH) + 1;
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        int curYear = c.get(Calendar.YEAR);

        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.repeatTimes = -1;
        // expect.year = c.get(Calendar.YEAR);
        expect.month = 10;
        expect.day = 17;
        expect.hour = 8;
        expect.minutes = 0;
        if (expect.month < curMonth
                || (expect.day < curDay && expect.month == curMonth)
                || (expect.hour < nowHour && expect.day == curDay && expect.month == curMonth)) {
            expect.year = curYear + 1;
        } else {
            expect.year = curYear;
        }
        assertReminder("10月17号上午8点提醒我", expect);
    }

    static void test_10() {
        Calendar c = Calendar.getInstance();
        Alarm expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
        expect.repeatTimes = -1;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 8;
        expect.minutes = 0;
        expect.second = 0;
        expect.daysOfWeek.set(0, true);
        expect.daysOfWeek.set(1, true);
        expect.daysOfWeek.set(2, true);
        expect.daysOfWeek.set(3, true);
        expect.daysOfWeek.set(4, true);
        assertReminder("周一到周五每天上午8点提醒我", expect);
    }

    // 中午1点提醒我
    static void test_11() {
        Calendar c = Calendar.getInstance();
        Alarm expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.repeatTimes = -1;
        expect.hour = 13;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        if (isTimePassed(expect)) {
            addOneDay(expect);
        }
        assertReminder("中午1点提醒我", expect);
    }

    // 2016年11月8日早上9点提醒我
    static void test_12() {
        Calendar c = Calendar.getInstance();
        Alarm expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.repeatTimes = -1;
        expect.year = 2016;
        expect.month = 11;
        expect.day = 8;
        expect.hour = 9;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        assertReminder("2016年11月8日早上9点提醒我", expect);
    }

    /*
     * 每一个星期二中午提醒我 每个星期二中午提醒我 每到星期二中午提醒我 每星期二中午提醒我
     */
    static void test_13() {
        Calendar c = Calendar.getInstance();
        Alarm expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
        expect.repeatTimes = -1;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 12;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        expect.daysOfWeek.set(1, true);
        assertReminder("每星期二中午提醒我", expect);
    }

    static void test_14() {
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 17;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        DaysOfWeek daysofWeek = new DaysOfWeek(0);
        daysofWeek.set(1, true);
        daysofWeek.set(2, true);
        daysofWeek.set(3, true);
        daysofWeek.set(4, true);
        daysofWeek.set(5, true);
        daysofWeek.set(6, true);
        expect.daysOfWeek = daysofWeek;
        assertReminder("周二到周日的下午五点，提醒我接孩子", expect);
    }

    static void test_15() {
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 17;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        DaysOfWeek daysofWeek = new DaysOfWeek(0);
        daysofWeek.set(1, true);
        daysofWeek.set(2, true);
        daysofWeek.set(3, true);
        daysofWeek.set(4, true);
        daysofWeek.set(5, true);
        daysofWeek.set(6, true);
        expect.daysOfWeek = daysofWeek;
        assertReminder("星期二到星期日的下午五点，提醒我接孩子", expect);
    }

    static void test_16() {
        Alarm expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.month = 12;
        expect.day = 23;
        expect.hour = 12;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        // DaysOfWeek daysofWeek = new DaysOfWeek(0);
        // daysofWeek.set(1, true);
        // daysofWeek.set(2, true);
        // daysofWeek.set(3, true);
        // daysofWeek.set(4, true);
        // daysofWeek.set(5, true);
        // daysofWeek.set(6, true);
        // expect.daysOfWeek = daysofWeek;
        assertReminder("12月23日中午12点提醒我", expect);
    }

    static void test_17() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int curDay = c.get(Calendar.DAY_OF_MONTH);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int curMonth = c.get(Calendar.MONTH) + 1;
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        int curYear = c.get(Calendar.YEAR);
        if (curMonth > Calendar.MONDAY) {

        }
        int curDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        Alarm expect = new Alarm();
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.hour = 10;
        expect.minutes = 30;
        expect.second = 0;
        expect.ampm = 1;

        assertReminder("下星期一早上10点半提醒我", expect);
    }

    static void test_18() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int curDay = c.get(Calendar.DAY_OF_MONTH);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int curMonth = c.get(Calendar.MONTH) + 1;
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        int curYear = c.get(Calendar.YEAR);
        if (curMonth > Calendar.MONDAY) {

        }
        int curDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        Alarm expect = new Alarm();
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.minutes = 30;
        expect.hour = 10;
        expect.second = 0;
        expect.ampm = 1;
        // DaysOfWeek daysofWeek = new DaysOfWeek(0);
        // daysofWeek.set(1, true);
        // daysofWeek.set(2, true);
        // daysofWeek.set(3, true);
        // daysofWeek.set(4, true);
        // daysofWeek.set(5, true);
        // daysofWeek.set(6, true);
        // expect.daysOfWeek = daysofWeek;
        assertReminder("下周二早上10点半提醒我", expect);
    }

    static void test_19() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int curDay = c.get(Calendar.DAY_OF_MONTH);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int curMonth = c.get(Calendar.MONTH) + 1;
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        int curYear = c.get(Calendar.YEAR);
        if (curMonth > Calendar.MONDAY) {
        }
        int curDayOfYear = c.get(Calendar.DAY_OF_YEAR);
        c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        Alarm expect = new Alarm();
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.minutes = 00;
        expect.hour = 20;
        expect.second = 0;
        expect.ampm = 2;
        // DaysOfWeek daysofWeek = new DaysOfWeek(0);
        // daysofWeek.set(1, true);
        // daysofWeek.set(2, true);
        // daysofWeek.set(3, true);
        // daysofWeek.set(4, true);
        // daysofWeek.set(5, true);
        // daysofWeek.set(6, true);
        // expect.daysOfWeek = daysofWeek;
        assertReminder("下周三晚上8点提醒我", expect);
    }

    static void test_0() {

        Alarm expect;
        DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);

        expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_INTERVAL;
        expect.hour = 10;
        expect.repeatTimes = 26;
        expect.interval = (30 * 60) * 1000;
        assertReminder("从10点到晚上11点，每隔半小时提醒我", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 17;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        daysofWeek = new DaysOfWeek(0);
        daysofWeek.set(0, true);
        daysofWeek.set(1, true);
        daysofWeek.set(2, true);
        daysofWeek.set(3, true);
        daysofWeek.set(4, true);
        expect.daysOfWeek = daysofWeek;
        assertReminder("周一到周五的下午五点，提醒我接孩子", expect);

        {
            expect = new Alarm();
            c.setTimeInMillis(System.currentTimeMillis());
            expect.type = Alarm.ALARM_TYPE_RELATIVE;
            expect.repeatType = Alarm.ALARM_REPEAT_TYPE_INTERVAL;
            expect.repeatTimes = -1;
            expect.interval = (30 * 60) * 1000;
            assertReminder("每隔半小时提醒我", expect);
        }

        expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 15;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("今天下午15点提醒我", expect);

        expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 16;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("后晌提醒我。", expect);

        expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 0;
        expect.minutes = 0;
        expect.second = 0;
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("凌晨提醒我。", expect);

        expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.MONTH, 1);
        // expect.year = 0;
        // expect.month = 0;
        // expect.day = 0;
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 14;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        // 如果是2点12分提醒我，但是时间已经过去了。那么天数需要加1
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("午饭后2点提醒我。", expect);

        expect = new Alarm();
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.hour = 0;
        expect.minutes = 15;
        expect.second = 0;
        assertReminder("小李叫去打牌，提醒我回头马上去。", expect);

        expect = new Alarm();
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.hour = 0;
        expect.minutes = 10;
        expect.second = 0;
        assertReminder("小李叫去打牌，待一会提醒我去", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.day += 1;
        expect.hour = 9;
        expect.minutes = 30;
        expect.second = 0;
        expect.ampm = 1;
        assertReminder("明天上午九点半提醒我", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.month = 8;
        expect.day = 11;
        expect.hour = 2 + 12;
        expect.minutes = 30;
        expect.second = 0;
        expect.ampm = 2;
        assertReminder("八月十一号下午两点半提醒我", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 9;
        expect.minutes = 20;
        expect.second = 0;
        // 如果day == 0，默认为当前
        // 如果是2点12分提醒我，但是时间已经过去了。那么天数需要加1
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("9:20提醒我", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        daysofWeek = new DaysOfWeek(0);
        daysofWeek.set(2, true);
        expect.daysOfWeek = daysofWeek;
        assertReminder("每周三提醒我锻炼", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_DAY;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 17;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        assertReminder("每天提醒下午5点我锻炼", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 6;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        daysofWeek = new DaysOfWeek(0);
        daysofWeek.set(2, true);
        expect.daysOfWeek = daysofWeek;
        assertReminder("每周三早晨六点，提醒我锻炼", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_DAY;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 14;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        assertReminder("每天提醒下午我锻炼", expect);

        expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        expect.year = c.get(Calendar.YEAR);
        expect.month = 12;
        expect.day = 31;
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        assertReminder("提醒我年末买车", expect);

        expect = new Alarm();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDay) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        assertReminder("周天提醒我面试", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 0;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        expect.minutes = 2;
        expect.second = 50;
        assertReminder("两分钟五十秒提醒我", expect);
        // 一会去骑自行车，提醒我
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 0;
        expect.minutes = 5;
        assertReminder("一会儿去骑自行车，提醒我", expect);

        // 一会去骑自行车，提醒我
        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_DAY;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        assertReminder("每天提醒我吃早饭", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 2;
        expect.minutes = 15;
        expect.second = 0;
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("2点一刻提醒我", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.hour = 9;
        expect.minutes = 28;
        expect.second = 30;
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("九点二十八分三十秒提醒我", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        // expect.year = 0;
        // expect.month = 0;
        expect.day += 1;
        expect.hour = 8;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        assertReminder("明天早上十点面试提前两小时提醒我", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        expect.hour = 2;
        expect.minutes = 15;
        expect.second = 0;
        assginDay(expect, c, nowHour, nowMinute, nowSecond);
        assertReminder("2:15提醒我吃药", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 0;
        expect.minutes = 30;
        expect.second = 0;
        assertReminder("半小时提醒我吃药", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_YEAR;
        expect.year = 0;
        expect.month = 6;
        expect.day = 7;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        assertReminder("每年六月七号，提醒我给老婆过生日", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_YEAR;
        expect.year = 0;
        expect.month = 6;
        expect.day = 7;
        expect.hour = 8;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        assertReminder("每年六月七号上午八点，提醒我给老婆过生日", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_MONTH;
        expect.year = 0;
        expect.month = 0;
        expect.day = 10;
        expect.hour = DEFAULTHOUR;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 0;
        assertReminder("每月十号，提醒我领工资", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 6;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 1;
        daysofWeek = new DaysOfWeek(0);
        daysofWeek.set(6, true);
        expect.daysOfWeek = daysofWeek;
        assertReminder("每周天早晨六点，提醒我锻炼", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_DAY;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 7 + 12;
        expect.minutes = 0;
        expect.second = 0;
        expect.ampm = 2;
        assertReminder("每天晚上七点，提醒我吃药", expect);

        expect = new Alarm();
        expect.type = Alarm.ALARM_TYPE_RELATIVE;
        expect.year = 0;
        expect.month = 0;
        expect.day = 0;
        expect.hour = 0;
        expect.minutes = 3;
        expect.second = 50;
        expect.ampm = 0;
        expect.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        assertReminder("三分五十秒倒计时", expect);
    }

    private static void assginDay(Alarm expect, Calendar c, int nowHour,
            int nowMinute, int nowSecond) {
        if (expect.hour < nowHour || expect.hour == nowHour
                && expect.minutes <= nowMinute || expect.hour == nowHour
                && expect.minutes == nowMinute && expect.second <= nowSecond) {
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.DAY_OF_YEAR, 1);
            expect.year = c.get(Calendar.YEAR);
            expect.month = c.get(Calendar.MONTH) + 1;
            expect.day = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    private static void assginWeek(Alarm expect, Calendar c, int nowHour,
            int nowMinute, int nowSecond) {
        if (expect.hour < nowHour || expect.hour == nowHour
                && expect.minutes <= nowMinute || expect.hour == nowHour
                && expect.minutes == nowMinute && expect.second <= nowSecond) {
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.WEEK_OF_YEAR, 1);
            expect.year = c.get(Calendar.YEAR);
            expect.month = c.get(Calendar.MONTH) + 1;
            expect.day = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    static void assignDate(Alarm alarm) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.YEAR, alarm.year);
        c.set(Calendar.MONTH, alarm.month - 1);
        int maxday = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (alarm.day > maxday) {
            c.set(Calendar.YEAR, alarm.year);
            c.set(Calendar.MONTH, alarm.month - 1);
            c.set(Calendar.DAY_OF_MONTH, alarm.day);
            alarm.year = c.get(Calendar.YEAR);
            alarm.month = c.get(Calendar.MONTH) + 1;
            alarm.day = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    static void addOneDay(Alarm alarm) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_YEAR, 1);
        alarm.year = c.get(Calendar.YEAR);
        alarm.month = c.get(Calendar.MONTH) + 1;
        alarm.day = c.get(Calendar.DAY_OF_MONTH);
    }

    static boolean isTimePassed(Alarm alarm) {
        boolean ret = false;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        if (alarm.hour < nowHour || alarm.hour == nowHour
                && alarm.minutes <= nowMinute || alarm.hour == nowHour
                && alarm.minutes == nowMinute && alarm.second <= nowSecond) {
            ret = true;
        }
        return ret;
    }

    static boolean isYearPassed(Alarm alarm) {
        boolean ret = false;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int nowYear = c.get(Calendar.YEAR);
        int nowMonth = c.get(Calendar.MONTH);
        int nowDay = c.get(Calendar.DAY_OF_MONTH);

        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);

        if (alarm.month < nowMonth
                || (alarm.month == nowMonth && alarm.day < nowDay)
                || (alarm.month == nowMonth && alarm.day == nowDay && isTimePassed(alarm))) {
            ret = true;
        }

        return ret;
    }

    static void addOneMonth(Alarm alarm) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, alarm.year);
        c.set(Calendar.MONTH, alarm.month);
        c.set(Calendar.DAY_OF_MONTH, alarm.day);
        alarm.year = c.get(Calendar.YEAR);
        alarm.month = c.get(Calendar.MONTH) + 1;
        alarm.day = c.get(Calendar.DAY_OF_MONTH);
    }

    static boolean equals(Alarm a, Alarm b) {
        boolean ret = false;
        int a_days = a.daysOfWeek.getCoded();
        int b_days = b.daysOfWeek.getCoded();
        ret = (a.type == b.type && a.year == b.year && a.month == b.month
                && a.day == b.day && a.hour == b.hour && a.minutes == b.minutes
                && a.second == b.second
                // && a.ampm == b.ampm
                && a_days == b_days && a.repeatType == b.repeatType
                && a.interval == b.interval && a.repeatTimes == b.repeatTimes);
        return ret;
    }

    public static void assertReminder(String input, Alarm expect) {
        ParseResult item = Parser.RemindParser(input);
        Alarm alarm;
        if (item.getType() == ParseResult.TYPE_REMIND) {
            alarm = (Alarm) item.getObject();
        } else {
            System.err.print("Wrong Type:" + item.getType() + input);
            return;
        }
        long interval_hours = alarm.interval / (1000 * 60 * 60);
        long interval_minutes = alarm.interval / (1000 * 60) % 60;
        if (equals(alarm, expect)) {
            System.out
                    .format("type:%d repattype:%s,"
                            + "year:%d, month:%d, day:%d, hour:%d, minutes:%d second:%d "
                            + "ampm:%d, dayofweek:%s "
                            + "interval:%d小时%d分 次数:%d\n", alarm.type,
                            repeat_type_strs[alarm.repeatType], alarm.year,
                            alarm.month, alarm.day, alarm.hour, alarm.minutes,
                            alarm.second, alarm.ampm,
                            alarm.daysOfWeek.toString(true), interval_hours,
                            interval_minutes, alarm.repeatTimes);
        } else {
            System.err.println(input);
            System.err
                    .format("current:type:%d repattype:%s,"
                            + " year:%d, month:%d, day:%d, hour:%d, minutes:%d second:%d "
                            + "ampm:%d, dayofweek:%s "
                            + "interval:%d小时%d分 次数:%d\n", alarm.type,
                            repeat_type_strs[alarm.repeatType], alarm.year,
                            alarm.month, alarm.day, alarm.hour, alarm.minutes,
                            alarm.second, alarm.ampm,
                            alarm.daysOfWeek.toString(true), interval_hours,
                            interval_minutes, alarm.repeatTimes);
            long expect_interval_hours = expect.interval / (1000 * 60 * 60);
            long expect_interval_minutes = expect.interval / (1000 * 60) % 60;
            System.err
                    .format("expect :type:%d repattype:%s,"
                            + " year:%d, month:%d, day:%d, hour:%d, minutes:%d second:%d "
                            + "ampm:%d, dayofweek:%s "
                            + "interval:%d小时%d分 次数:%d\n", expect.type,
                            repeat_type_strs[expect.repeatType], expect.year,
                            expect.month, expect.day, expect.hour,
                            expect.minutes, expect.second, expect.ampm,
                            expect.daysOfWeek.toString(true),
                            expect_interval_hours, expect_interval_minutes,
                            expect.repeatTimes);
            // assert(false);
        }
    }

    public static void testReminder() {
        test_20();
        // "下周三晚上8点提醒我"
        test_19();
        // 下周一早上10点半提醒我
        test_18();
        // 下星期一早上10点半提醒我
        test_17();
        test_16();
        test_15();
        test_14();
        test_13();
        test_12();
        test_11();
        test_10();
        test_9();
        // 每隔一小时提醒我
        test_8();

        test_7();

        test_6();
        test_5();
        // 十分钟后提醒我
        test_4();

        // 十分钟后提醒我.rd
        test_3();
        // 下月末提醒我锻炼
        test_2();
        // 周一五点提醒我
        test_reminder_12();

        // 月末提醒我面试
        test_reminder_11();
        // 明天上午十点提醒我
        test_reminder_10();
        // 晚上六点提醒我朋友请吃饭
        test_remider_9();
        // 五个小时后提醒我
        test_remider_8();
        // 今晚9:25提醒我开会
        test_remider_7();
        // 今晚9:25提醒我开会
        test_remider_6();
        // 一个半小时后提醒我
        test_remider_5();
        // 大后天12:35:55提醒
        test_remider_4();
        // 周一下午3点25分16秒提醒我
        test_week_4();
        // 周六23：59分提醒我
        test_week_3();
        // 大后天十点半提醒
        test_remider_2();
        // 从现在起到晚上11点，每隔半小时提醒我
        test_interval_1();
        // 后天十点半提醒
        test_remider_1();
        // 星期天下午五点半提醒我
        test_week_2();
        // 周六上午九点提醒我
        test_week_1();
        // 九月十三八点提醒我
        test_1();

        test_0();

        test_20();
        test_21();
        
        test_22();
        test_23();
        test_24();
        test_25();
        test_26();
        test_27();
        test_28();
        test_29();
        test_30();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
//        test_28();
        test_32();
//        testReminder();
    }

}
