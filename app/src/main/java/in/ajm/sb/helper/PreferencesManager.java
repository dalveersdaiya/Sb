package in.ajm.sb.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesManager
{

	private static String PREFS_NAME = "Sb";

	public static boolean hasPreference(Context context, String key)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.contains(key);
	}

	public static String getPreferenceByKey(Context context, String key)
	{
		try
		{
			SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
			return settings.contains(key) ? settings.getString(key, "") : "";
		} catch (Exception e)
		{
			return "";
		}
	}

	public static void clearAllData(Context context)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor e = settings.edit();
		e.clear();
		e.apply();
	}

	public static void setPreferenceByKey(Context context, String key, String value)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor e = settings.edit();
		e.putString(key, value);
		e.apply();
	}

	public static boolean getPreferenceBooleanByKey(Context context, String key)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getBoolean(key, false);
	}

	public static void setPreferenceBooleanByKey(Context context, String key, boolean value)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor e = settings.edit();
		e.putBoolean(key, value);
		e.apply();
	}

	public static int getPreferenceIntByKey(Context context, String key)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getInt(key, Integer.MIN_VALUE);
	}

	public static void setPreferenceIntByKey(Context context, String key, int value)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor e = settings.edit();
		e.putInt(key, value);
		e.apply();
	}

	public static Long getPreferenceLongByKey(Context context, String key)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getLong(key, Long.MIN_VALUE);
	}

	public static void setPreferenceLongByKey(Context context, String key, Long value)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor e = settings.edit();
		e.putLong(key, value);
		e.apply();
	}
}
