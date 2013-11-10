package com.example.keyring;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveView extends Activity {
	
	final String FILE = "filename"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_view);
		
		Bundle b = new Bundle();
		b = getIntent().getExtras();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_b, menu);
		return true;
	}
	
	public void cancel(View v){
		finish();
	}
	
	private void writeToFile (int modeFile, String info){
		FileOutputStream outputStream;

		try {
		  outputStream = openFileOutput(FILE, modeFile);
		  outputStream.write(info.getBytes());
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		  System.err.println("Exception: "+ e.getMessage());
		}
	}
	
	public void saveInFile(View v){
		EditText edit = (EditText) findViewById(R.id.newNameEdit);
		String myNewSecret = edit.getText().toString();
		Log.d("newSecret", myNewSecret);
		
		if (!myNewSecret.isEmpty()){
			writeToFile(Context.MODE_APPEND, myNewSecret+"%");
			Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show();
			finish();
		}
		else {
			Toast.makeText(this, "The field is empty!", Toast.LENGTH_SHORT).show();
		}		
	}

}
