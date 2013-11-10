package com.example.keyring;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;


public class MainView extends Activity {
	
	MyCustomAdapter myCustomAdapter = null;
	ArrayList<Secret> secrets;
	final String FILE = "filename"; 
	MenuItem delete, edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_view);	
	}
	
	protected void onResume(){
	 	super.onResume();
		displayListView();
	}
	
	private void displayListView(){
		String contentFromFile = readFromFile();
		String[] names = contentFromFile.split("%");
		secrets = new ArrayList<Secret>();
		
		for (int i=0; i<names.length; i++){
			secrets.add(new Secret (names[i], false));
			myCustomAdapter = new MyCustomAdapter(this, R.layout.acitivity_list, secrets);
		}
		
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(myCustomAdapter);
	
	}
	
	private void writeToFile (int modeFile, String info){
		FileOutputStream outputStream;

		try {
		  outputStream = openFileOutput(FILE, modeFile);
		  outputStream.write(info.getBytes());
		  outputStream.close();
		} 
		catch (Exception e) {
		  e.printStackTrace();
		  System.err.println("Exception: "+ e.getMessage());
		}
	}
	
	private String readFromFile() 
	{
	    String ret = "";

	    try 
	    {
	        InputStream inputStream = openFileInput(FILE);
	        
	        if ( inputStream != null ) 
	        {
	        	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();
	           
	            while ( (receiveString = bufferedReader.readLine()) != null ) 
	            {
	                stringBuilder.append(receiveString);  
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) 
	    {
	        Log.e("login activity", "File not found: " + e.toString());
	    } 
	    catch (IOException e) 
	    {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}

	private class MyCustomAdapter extends ArrayAdapter<Secret> {
		  
		  private ArrayList<Secret> secrets;
		  
		  public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Secret> secrets) {
			  super(context, textViewResourceId, secrets);
			  this.secrets = new ArrayList<Secret>();
			  this.secrets.addAll(secrets);
		  }	  
		  
		  private class ViewHolder {
			  
			   CheckBox name;
		  }
		  
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) 
		  {
		  
			  ViewHolder holder = null;
			  Log.v("ConvertView", String.valueOf(position));
			  

			  if (convertView == null) 
			  {
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.acitivity_list, null);
				  holder = new ViewHolder();
				  holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
				  convertView.setTag(holder);
				  
				  holder.name.setOnClickListener( new View.OnClickListener() 
				  { 
					  
					  public void onClick(View v) { 
					      CheckBox cb = (CheckBox) v ; 
					      Secret secret = (Secret) cb.getTag(); 
					      //Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
					      secret.setChecked(cb.isChecked());
					      invalidateOptionsMenu();
					      
					      
					     } 
					  
				  }); 
			  }  
			  
			  else 
			  {
				  holder = (ViewHolder) convertView.getTag();
			  }
			  
			  Secret secret = this.secrets.get(position);
			  holder.name.setText(secret.getTitle());
			  holder.name.setChecked(secret.isChecked());
			  holder.name.setTag(secret);
			  
		  return convertView;  
		  }	  
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_b, menu);

		int count = 0;

		for (int i=0; i<secrets.size(); i++){
			if(secrets.get(i).isChecked() == true){
				count++;  
			}
		}
		System.out.println("count: " + count);

		if (count == 0){

			menu.getItem(1).setEnabled(false);
			menu.getItem(2).setEnabled(false);
			System.out.println("Delete disabled");
		}
		else if (count>=2){
			menu.getItem(1).setEnabled(false);
			System.out.println("Edit disabled");
		}
		else {
			menu.getItem(1).setEnabled(true);
			menu.getItem(2).setEnabled(true);
		}

		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{	
		switch(item.getItemId())
		{
			case R.id.action_new:
			{
				Intent i = new Intent(this, SaveView.class);
				startActivity(i);
				break;
			}
			
			case R.id.action_edit:
			{
				//Intent i = new Intent()
			}
			
			case R.id.action_discard:
			{
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
				{
				    @Override
				    public void onClick(DialogInterface dialog, int which) 
				    {
				        switch (which)
				        {
				        	case DialogInterface.BUTTON_POSITIVE:
				        	{
				        		deleteFile(FILE);
				        		
				        		for (int i=0; i<secrets.size(); i++){
				        			if (secrets.get(i).isChecked()){
				        				secrets.remove(i);
				        				--i;
				        			}	
				        			else{
				        				writeToFile(MODE_APPEND, secrets.get(i).getTitle() + "%");
				        			}
				        		}
				        		
				        		for(int i=0; i<secrets.size(); i++){
				        			System.out.println("Secret at " + i + ": "+secrets.get(i).getTitle());
				        		}
				        		
				        		myCustomAdapter.notifyDataSetChanged();
				        		
//				        		for(int i = 0; i < secrets.size(); i++)
//				        		{
//				        			Secret secretIterator = secrets.get(i);
//				        			if(secretIterator.isChecked())
//				        			{						
//				        				secrets.remove(i);
//				        				--i;
//				        			}
//				        			else
//				        			{				        								        				
//				        				 writeToFile(Context.MODE_APPEND, secretIterator.getTitle() + "%");
//				        			}
//				        		}			
//				        		dataAdapter.dataArray = ((ArrayList<Data>)data.clone());
//				        		dataAdapter.refreshAdapter();


				        		break;
				        	}

				        	case DialogInterface.BUTTON_NEGATIVE:
				        		//No button clicked
				        		break;
				        }
				    }
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Are you sure you want to delete these itemes?").setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("Cancel", dialogClickListener).show();
		        break;
			}
		}
		return true;
	}
}
