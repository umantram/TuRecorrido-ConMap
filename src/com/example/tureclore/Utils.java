package com.example.tureclore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

public class Utils {

	public static final String LAT1 = "lat1";
	public static final String LAT2 = "lat2";
	public static final String LONG1 = "long1";
	public static final String LONG2 = "long2";

	public static final int GPS_NOT_TURNED_FF = 3;
	public static final String TU_REOCRRIDO = "turecorrido.";
	public static final String FIRST_TIME = TU_REOCRRIDO + "first_time";
	public static final String LAST_UPDATE = "last_update";
	public static final String FILE_NAME = "systemTB";
	public static final String CONTENTS = "contents";

	public static String EMPRESA_SELECTED = "empresa_selected";
	public static final int GPS_NOT_TURNED_ON = 2;

	public static ArrayList<ColetivoData> getWebLocations(Context context) {
		ArrayList<ColetivoData> list = new ArrayList<ColetivoData>();

		try {

			// Find the directory for the SD Card using the API
			// *Don't* hardcode "/sdcard"
			File sdcard = Environment.getExternalStorageDirectory();
			File dir = new File(sdcard.getAbsolutePath() + "/turecorrido");
			// Get the text file
			File file = new File(dir, FILE_NAME);

			StringBuilder text = new StringBuilder();
			String contents;
			if (file.exists()) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;
					while ((line = br.readLine()) != null) {
						text.append(line);
						text.append('\n');
					}
				} catch (IOException e) {
					// You'll need to add proper error handling here
				}
				contents = text.toString();
			} else {
				SharedPreferences prefs = context.getSharedPreferences(
						Utils.TU_REOCRRIDO, 0);
				contents = prefs.getString(Utils.CONTENTS, "error");
			}

			if (contents.equalsIgnoreCase("error")) {
				return list;
			}

			String[] linesText = contents.split("\n");

			int size = linesText.length;
			for (int i = 0; i < size; i++) {
				String string = linesText[i];
				String[] lines = string.split(",");
				ColetivoData store = new ColetivoData(lines[0].trim(),
						lines[1].trim());
				list.add(store);
			}

		} catch (Exception e) {
			return new ArrayList<ColetivoData>();
		}

		return list;
	}

	public static long getDaysCountFromLastUpdate(String lastUpdate) {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		Date startDate;
		try {
			startDate = formatter.parse(lastUpdate);
		} catch (ParseException e) {
			return 1;
		}
		Date today = new Date();

		long startTime = startDate.getTime();
		long endTime = today.getTime();

		long diffTime = endTime - startTime;

		long diffDays = diffTime / (1000 * 60 * 60 * 24);

		return diffDays;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	public static String getDateAsString() {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		return formatter.format(new Date());
	}

	public static ArrayList<ColetivoData> getWebLocations(String result) {
		ArrayList<ColetivoData> list = new ArrayList<ColetivoData>();
		try {

			if (result.equalsIgnoreCase("error")) {
				return list;
			}

			String[] linesText = result.split("\n");

			int size = linesText.length;
			for (int i = 0; i < size; i++) {
				String string = linesText[i];
				String[] lines = string.split(",");
				ColetivoData store = new ColetivoData(lines[0].trim(),
						lines[1].trim());
				list.add(store);
			}
		} catch (Exception e) {
			return new ArrayList<ColetivoData>();
		}
		return list;
	}


	
}