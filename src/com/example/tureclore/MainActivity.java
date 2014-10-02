package com.example.tureclore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends android.support.v4.app.FragmentActivity {
	private Button bIngresar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 bIngresar = (Button) findViewById(R.id.bIngresar);
		 
	     bIngresar.setOnClickListener(new OnClickListener(){

	            @Override
	            public void onClick(View v){
	            	Intent ingresarIntent = new Intent(MainActivity.this, PantallaInicial.class);
	               	startActivity(ingresarIntent);
	            }
	        });
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		

}
