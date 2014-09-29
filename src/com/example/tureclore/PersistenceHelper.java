package com.example.tureclore;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;

public class PersistenceHelper {
	
	private SharedPreferences prefs;

	private Activity context;
	
	public PersistenceHelper(Activity context){
		this.context = context;
		prefs = context.getSharedPreferences("TURECORRIDO", 0);
	}
	
	public void save(String lineaColectivo, ArrayList<String> carteles){
		SharedPreferences.Editor editor = prefs.edit();
		
		StringBuilder builder = new StringBuilder();
		
		for (String string : carteles) {
			builder.append(string);
			builder.append(",");
		}
		
		editor.putString(lineaColectivo, builder.toString());
		editor.commit();
	}
	

	public ArrayList<String> retrieve(String lineaColectivo){
		String contents = prefs.getString(lineaColectivo, "null");
		ArrayList<String> carteles = new ArrayList<String>();
		if (!contents.equalsIgnoreCase("null")){
			String[] cart = contents.split(",");
			for (String string : cart) {
				carteles.add(string);
			}
		}
		return carteles;
	}
	
	
}
