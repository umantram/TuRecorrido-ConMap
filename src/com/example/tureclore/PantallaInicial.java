package com.example.tureclore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


public class PantallaInicial extends Activity {

	TabHost th;
	//variable de spinner
	private Spinner sp;

	private List<String>lista1;
	
	TextView showResults;
	long start, stop;
	private ListView lista;
	private BaseDeDatos BD; //no uso
	private Cursor c;//no uso
	private PersistenceHelper dataPersistence;//hizo migue

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.primer_main);
		DatosPorDefecto();
		/*
		 * dataPersistence = new PersistenceHelper(this);
		 * 
		 * ArrayList<String> data = new ArrayList<String>();
		 * data.add("PALERMO"); data.add("CHACO"); data.add("CHACO 2");
		 * dataPersistence.save("LINEA 5", data);
		 */

		// //
		

		new DownloadTask(this).execute("https://docs.google.com/document/d/19yOsxYvIZ-ygFpLXq46Hd7_j-UxoNtySwBOE6XC8-AA/export?format=txt");
		
		// ////////////////////////hasta
		// aqui///////////////////////////////////////
		th = (TabHost) findViewById(R.id.tabhost);

		th.setup();
		TabSpec specs = th.newTabSpec("tag1");
		specs.setContent(R.id.tab1);
		specs.setIndicator("Transito");
	    th.addTab(specs);
		
		specs = th.newTabSpec("tag2");
		specs.setContent(R.id.tab2);
		specs.setIndicator("Lineas");
		th.addTab(specs);
		
		specs = th.newTabSpec("tag3");
		specs.setContent(R.id.tab3);
		specs.setIndicator("Cuenta");
		th.addTab(specs);
		
		specs = th.newTabSpec("tag4");
		specs.setContent(R.id.tab4);
		specs.setIndicator("Info");
		th.addTab(specs);

	}
	private void DatosPorDefecto() {
		   sp = (Spinner) findViewById(R.id.spinnerLineas);
		   lista1 = new ArrayList<String>();
		   sp = (Spinner) this.findViewById(R.id.spinnerLineas);
		   lista1.add("Linea 2A");
		   lista1.add("Linea 2B");
		   lista1.add("Linea 3A");
		   lista1.add("Linea 3B");
		   lista1.add("Linea 5A");
		   lista1.add("Linea 5B");
		   lista1.add("Linea 5C");
		   lista1.add("Linea 8x16");
		   lista1.add("Linea 8x21");
		   lista1.add("Linea 8xEdison");
		   lista1.add("Linea 9A");
		   lista1.add("Linea 9B");
		   lista1.add("Linea 12xVillaMonona");
		   lista1.add("Linea 12xVillaDonBosco");
		   lista1.add("Linea 101");
		   lista1.add("Linea 104");
		   lista1.add("Linea 106A");
		   lista1.add("Linea 106B");
		   lista1.add("Linea 106C");
		   lista1.add("Linea 107xSanPablo");
		   lista1.add("Linea 107xVicentin");
		   lista1.add("Linea 110");
		   
		  
		   ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista1);
		   adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		   sp.setAdapter(adaptador);
		}

	

//hice yo
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
//hizo migue
	private class DownloadTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private PowerManager.WakeLock mWakeLock;

		public DownloadTask(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(String... sUrl) {
			InputStream input = null;
			StringBuilder contenido = new StringBuilder();
			BufferedReader in = null;
			try {

				input = new HTTPScraper().fecthHtmlGet(sUrl[0]);
				if (input == null) {
					throw new Exception("Unable to resolve host");
				}

				// download the file
				InputStream is = input;
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String str;
				boolean first = true;
				while ((str = in.readLine()) != null) {
					if (!first)
						contenido.append("\n");
					first = false;
					contenido.append(str);
				}

			} catch (Exception e) {
				return "error";
			} finally {
				try {
					if (input != null) {
						input.close();
					}
				} catch (IOException ignored) {
				}

				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException ignored) {
				}

			}
			return contenido.toString();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();

		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// if we get here, length is known, now set indeterminate to false
		}

		@Override
		protected void onPostExecute(String result) {
			mWakeLock.release();

			onFinishLoad(result);
			Toast.makeText(context, "Datos Actualizados!", Toast.LENGTH_SHORT)
					.show();
		}

		private void onFinishLoad(String result) {
			// ////////////////////////////////////////parte de
			// lista////////////////////////////
			ArrayList<ColetivoData> ls = Utils.getWebLocations(result);

			ArrayList<ListaEntrada> dataLista = new ArrayList<ListaEntrada>();
			
			for (ColetivoData data : ls) {
				dataLista.add(new ListaEntrada(R.drawable.lineas2jpg, data.getLineaColectivo(), data.getCartel()));
			}

			lista = (ListView) findViewById(R.id.listaColes);
			lista.setAdapter(new ListaAdaptador(PantallaInicial.this, R.layout.entrada, dataLista) {
				@Override
				public void onEntrada(Object entrada, View view) {
					if (entrada != null) {
						TextView texto_superior_entrada = (TextView) view
								.findViewById(R.id.textSuperior);
						if (texto_superior_entrada != null)
							texto_superior_entrada
									.setText(((ListaEntrada) entrada)
											.get_textoEncima());

						TextView texto_inferior_entrada = (TextView) view
								.findViewById(R.id.textInferior);
						if (texto_inferior_entrada != null)
							texto_inferior_entrada
									.setText(((ListaEntrada) entrada)
											.get_textoDebajo());

						ImageView imagen_entrada = (ImageView) view
								.findViewById(R.id.imagenCole);
						if (imagen_entrada != null)
							imagen_entrada
									.setImageResource(((ListaEntrada) entrada)
											.get_idImagen());
					}
				}
			});

			lista.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> pariente, View view,
						int posicion, long id) {
					ListaEntrada elegido = (ListaEntrada) pariente
							.getItemAtPosition(posicion);

					CharSequence texto = "Seleccionado: "
							+ elegido.get_textoDebajo();
					Toast toast = Toast.makeText(PantallaInicial.this, texto,
							Toast.LENGTH_LONG);
					toast.show();
				}
			});
		}
	}

}
