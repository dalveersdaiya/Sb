package in.ajm.sb.helper;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeHelper {
    public static Long getCurrentDateTimeInUnixTimeStamp() {
        try {
            return System.currentTimeMillis() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static Long getCurrentDateTimeInUnixTimeStampMillis() {
        try {
            return System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static String formatCalendar(Calendar calendar, String dateTimeFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat, Locale.US);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getCurrentDateTime(String dateFormatStr, TimeZone timeZone) {
        try {
            Calendar caledarObj = Calendar.getInstance(timeZone);
            SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormatStr, Locale.US);
            dateFormatter.setTimeZone(timeZone);
            String dateStr = dateFormatter.format(caledarObj.getTime());
            caledarObj.clear();
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertUnixTimeStampIntoRelativeDate(long timeStamp) {
        if (timeStamp > 0) {
            long currentTimeStamp = getCurrentDateTimeInUnixTimeStampMillis();
            long diff = currentTimeStamp - timeStamp;

            if (diff < 60)
                return "Just Now";
            else {
                CharSequence relativeTimeSpan = DateUtils.getRelativeTimeSpanString(timeStamp, currentTimeStamp, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
                return relativeTimeSpan.toString();
            }
        } else {
            return "Just Now";
        }
    }

    public static String convertUnixTimeStampIntoDateStr(long timeStamp, String dateFormatStr, TimeZone timeZone) {
        try {
            Date date = new Date(timeStamp);
            SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormatStr, Locale.getDefault());
            dateFormatter.setTimeZone(timeZone);
            return dateFormatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long convertDateStrIntoUnixTimeStamp(String dateTimeStr, String dateFormatStr, TimeZone timeZone) {
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormatStr, Locale.US);
            dateFormatter.setTimeZone(timeZone);
            Date date = dateFormatter.parse(dateTimeStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Calendar convertDateStrIntoCalendar(String dateTimeStr, String dateFormatStr) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr, Locale.US);
            cal.setTime(sdf.parse(dateTimeStr));
            return cal;
        } catch (Exception e) {
            return null;
        }
    }
}
