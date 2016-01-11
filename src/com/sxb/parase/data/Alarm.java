

package com.sxb.parase.data;

import java.text.DateFormatSymbols;
import java.util.Calendar;


public final class Alarm  {

    /**
     * repeat type coded as integer
     * <P>Type: INTEGER</P>
     * 0 not repeat
     * 1 week repeat
     * 2 month repeat
     * 3 year repeat
     */
  public static final int ALARM_REPEAT_TYPE_NONE = 0;
  public static final int ALARM_REPEAT_TYPE_DAY = 1;
  public static final int ALARM_REPEAT_TYPE_WEEK = 2;
  public static final int ALARM_REPEAT_TYPE_MONTH = 3;
  public static final int ALARM_REPEAT_TYPE_YEAR = 4;
  public static final int ALARM_REPEAT_TYPE_INTERVAL = 5;
  public static final int ALARM_REPEAT_TYPE_STOPWATCH = 6;
  //使用相对时间，重复一次
  public static final int ALARM_REPEAT_TYPE_ONCE = 7;
  
  public static final int ALARM_TYPE_RELATIVE = 1;
  public static final int ALARM_TYPE_ABSOLUTE = 2;
  public static final int ALARM_TYPE_FAILED= 3;
  private OnTimeChangedListener mOnTimeChangedListener;

    public String label;
  
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minutes;
    public int second;
    public DaysOfWeek daysOfWeek; // 周，其实就是一个int型
    public long time;
    public int repeatType; // 提醒类型
    public int repeatTimes; // 重复次数
    public long interval; // ？间隔时间，可能没用
    // the below filed is not in the database column
    public int type; 
    public int ampm; // 1 :am, 2: pm

    // 下面的字段用于计算，表盘针的角度
    public int mHourDegree = 0;
    public int mMinuteDegree = 0;
    public int mPreMinuteDegree = 0;
    public int mPreHourDegree = 0;

    private boolean mHourAbove12 = false;
    private boolean mMinuteAbove12 = false;

    // Creates a default alarm at the current time.
    public Alarm() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        label = "";
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
        second = 0; //c.get(Calendar.SECOND);
        daysOfWeek = new DaysOfWeek(0);
        time = c.getTimeInMillis();
        repeatType = ALARM_REPEAT_TYPE_NONE;
        repeatTimes = -1;
        interval = 0;
		ampm = 0;
    }

    /*
     * Days of week code as a single int.
     * 0x00: no day
     * 0x01: Monday
     * 0x02: Tuesday
     * 0x04: Wednesday
     * 0x08: Thursday
     * 0x10: Friday
     * 0x20: Saturday
     * 0x40: Sunday
     */
    public static final class DaysOfWeek {

        private static int[] DAY_MAP = new int[] {
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY,
        };

        // Bitmask of all repeating days
        private int mDays;

        public DaysOfWeek(int days) {
            mDays = days;
        }

        public String toString( boolean showNever) {
            StringBuilder ret = new StringBuilder();

            // no days
            if (mDays == 0) {
                return showNever ?"没有选择周" : "";
            }

            // every day
            if (mDays == 0x7f) {
                return "每一天";
            }

            // count selected days
            int dayCount = 0, days = mDays;
            while (days > 0) {
                if ((days & 1) == 1) dayCount++;
                days >>= 1;
            }

            // short or long form?
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] dayList = (dayCount > 1) ?
                    dfs.getShortWeekdays() :
                    dfs.getWeekdays();

            // selected days
            for (int i = 0; i < 7; i++) {
                if ((mDays & (1 << i)) != 0) {
                    ret.append(dayList[DAY_MAP[i]]);
                    dayCount -= 1;
                    if (dayCount > 0) ret.append( "");
//                            context.getText(R.string.day_concat));
                }
            }
            return ret.toString();
        }

        /**
         * 设置按周循环， day的取值：周一为0， 周日为6
         */
        private boolean isSet(int day) {
            return ((mDays & (1 << day)) > 0);
        }
        /**
         * 设置按周循环， day的取值：周一为0， 周日为6
         */
        public void set(int day, boolean set) {
            if (set) {
                mDays |= (1 << day);
            } else {
                mDays &= ~(1 << day);
            }
        }

        public void set(DaysOfWeek dow) {
            mDays = dow.mDays;
        }

        public int getCoded() {
            return mDays;
        }

        // Returns days of week encoded in an array of booleans.
        public boolean[] getBooleanArray() {
            boolean[] ret = new boolean[7];
            for (int i = 0; i < 7; i++) {
                ret[i] = isSet(i);
            }
            return ret;
        }

        public boolean isRepeatSet() {
            return mDays != 0;
        }

        /**
         * returns number of days from today until next alarm
         * @param c must be set to today
         */
        public int getNextAlarm(Calendar c) {
            if (mDays == 0) {
                return -1;
            }

            int today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;

            int day = 0;
            int dayCount = 0;
            for (; dayCount < 7; dayCount++) {
                day = (today + dayCount) % 7;
                if (isSet(day)) {
                    break;
                }
            }
            return dayCount;
        }
    }

  public void calcTime(boolean bFlag){
    if (mMinuteDegree >= 360){
      mMinuteDegree %= 360;
    }

    if (mMinuteDegree < 0){
      mMinuteDegree += 360;
    }

    if(mMinuteDegree == mPreMinuteDegree){
      return;
    }
    minutes = (int) ((mMinuteDegree / 360.0) * 60);

    if (IsDeasil(mMinuteDegree, mPreMinuteDegree)){
      if (mMinuteDegree < mPreMinuteDegree){
        hour += 1;
        hour %= 24;
      }
    }else{
      if (mMinuteDegree > mPreMinuteDegree){
        hour -= 1;
        if (hour < 0){
          hour += 24;
        }
      }
    }

    mHourDegree = (hour % 12) * 30 + mMinuteDegree / 12;
    if (mPreMinuteDegree != mMinuteDegree)
      mOnTimeChangedListener.onTimeChanged();

    mPreMinuteDegree = mMinuteDegree;
  }

  public void calcHour(boolean bFlag){
    if (mHourDegree >= 360){
      mHourDegree %= 360;
    }

    if (mHourDegree < 0){
      mHourDegree += 360;
    }

    hour = (int) ((mHourDegree / 360.0) * 12);

    mMinuteDegree = mHourDegree % 30 * 12;
    minutes = mMinuteDegree / 6;

    if (IsDeasil(mHourDegree, mPreHourDegree)){
      if(mHourDegree < mPreHourDegree){
        mHourAbove12 = !mHourAbove12;
        mMinuteAbove12 = false;
      }
    }else{
      if (mHourDegree > mPreHourDegree){
        mMinuteAbove12 = !mMinuteAbove12;
        mHourAbove12 = false;
      }
    }

    if(mMinuteAbove12 || mHourAbove12){
      hour = 12 + hour % 12;

      hour %= 24;
    }

    if (bFlag){
      calcDegreeByTime();
    }

    if (mPreHourDegree != mHourDegree)
      mOnTimeChangedListener.onTimeChanged();


    mPreHourDegree = mHourDegree;
  }

  private void calcDegreeByTime(){

    mMinuteDegree = (int) ((minutes) * 6);
    mHourDegree = (hour % 12) * 30 + mMinuteDegree / 12;
  }

  private boolean IsDeasil(int degree, int preDegree){          //
    if (degree >= preDegree){
      if (degree - preDegree < 180){
        return true;
      }
      return false;
    }else{
      if (preDegree - degree > 180){
        return true;
      }
      return false;
    }
  }

  public void setOnTimeChangedListener(OnTimeChangedListener listener) {
    mOnTimeChangedListener = listener;
  }

  public interface OnTimeChangedListener {
    public void onTimeChanged();
  }

  public void log() {
      System.out
      .format("year:%d, month:%d, day:%d, " +
              "hour:%d, minutes:%d second:%d " +
              "dayofweek:%s",
              year, month, day, hour, minutes, second, 
              daysOfWeek.toString(true));
  }
}
