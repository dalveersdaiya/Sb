package in.ajm.sb.helper;

import android.content.Context;
import android.util.Patterns;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class StringHelper {
    public static boolean validateEmail(String emailStr) {
        return !StringHelper.isEmpty(emailStr) && Patterns.EMAIL_ADDRESS.matcher(emailStr).matches();
    }

    public static String formatInt(int value) {
        return String.format(Locale.getDefault(), "%d", value);
    }

    public static String formatLong(long value) {
        return String.format(Locale.getDefault(), "%d", value);
    }

    public static String formatDouble(double value) {
        return String.format(Locale.getDefault(), "%.2f", value);
    }

    public static String getRandomString(int sizeOfRandomString) {
        String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static String uniqueId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static String trimString(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().equals("");
    }

    public static int convertToInt(String value) {
        if (value == null || value.trim().equals(""))
            return 0;

        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();

            return 0;
        }
    }

    public static Integer convertToInteger(String value) {
        if (value == null || value.trim().equals(""))
            return 0;

        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static float convertToFloat(String value) {
        if (value == null || value.trim().equals(""))
            return 0f;

        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }

    public static double convertToDouble(String value) {
        if (value == null || value.trim().equals(""))
            return 0.00;

        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.00;
        }
    }

    public static String getDeviceId(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String formatAddress(String address, String city, String state, String zip) {
        String addressStr = StringHelper.isEmpty(address) ? "" : address;
        String cityStr = StringHelper.isEmpty(city) ? "" : city;
        String stateStr = StringHelper.isEmpty(state) ? "" : state;
        String zipStr = StringHelper.isEmpty(zip) ? "" : zip;

        String formatAddress = addressStr;
        if (!StringHelper.isEmpty(cityStr))
            formatAddress += ", " + cityStr;

        if (!StringHelper.isEmpty(stateStr))
            formatAddress += ", " + stateStr;

        if (!StringHelper.isEmpty(zipStr))
            formatAddress += ", " + zipStr;

        return formatAddress;
    }

//	public static String formatAddressNewLine(String address, String city, String state, String zip)
//	{
//		String addressStr = StringHelper.isEmpty(address) ? "" : address;
//		String cityStr = StringHelper.isEmpty(city) ? "" : city;
//		String stateStr = StringHelper.isEmpty(state) ? "" : state;
//		String zipStr = StringHelper.isEmpty(zip) ? "" : zip;
//
//		String formatAddress = addressStr;
//		if (!StringHelper.isEmpty(cityStr))
//			formatAddress += "\n" + cityStr;
//
//		if (!StringHelper.isEmpty(stateStr))
//			formatAddress += ", " + stateStr;
//
//		if (!StringHelper.isEmpty(zipStr))
//			formatAddress += ", " + zipStr;
//
//		return formatAddress;
//	}

}
