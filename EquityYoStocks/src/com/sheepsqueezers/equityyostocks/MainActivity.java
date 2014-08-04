package com.sheepsqueezers.equityyostocks;

import com.sheepsqueezers.equityyostocks.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements Fragment1SpinnerControl.OnFragment1SpinnerChangedListener {

	ListView oSectorListView;
	Fragment1SpinnerControl oF1C_SP;
	Fragment2DetailsControl oF2C_DE;
	Fragment3PriceChartControl oF3C_PC;
	Fragment4DividendChartControl oF4C_DC;
	Fragment5AlsoViewedImageControl oF5C_AC;	
	FragmentManager oFM;
	FragmentTransaction oFT;
	DrawerLayout oSectorDrawerLayout;
	View viewSectorOLD;
	Boolean bFirstTimeThrough = true;
    ScrollView oScrollView;
    int iDeviceWidth,iDeviceHeight;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        //Access the DrawerLayout object.
        oSectorDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        //Access the ScrollView object.
        oScrollView = (ScrollView) findViewById(R.id.svFragments);

        //Access the sector ListView object.
        oSectorListView = (ListView) findViewById(R.id.sector_drawer);

        //Place the sector names in an array.
        String[] aSectorNames = getResources().getStringArray(R.array.sectors);
        
        //Rig up a custom ArrayAdapter to the list of sector names in the array.
        CustomArrayAdapter adapter = new CustomArrayAdapter(this,android.R.layout.simple_list_item_1,aSectorNames);

        //Associate the adapter with the sector ListView.
        oSectorListView.setAdapter(adapter);
        
    	//Access the FragmentManager.
    	oFM = (FragmentManager) getSupportFragmentManager();

    	//Get the width and height of the device
    	WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
		iDeviceWidth = display.getWidth();
		iDeviceHeight = display.getHeight();
		
        //Load the five fragments into the mainframe.
        initialLoadFragments();
            	
		//Open the drawer containing the sectors.
		oSectorDrawerLayout.openDrawer(oSectorListView);
		
    	//Set up a listener for the click of the NavigationDrawer.
		SectorDrawerItemClickListener sectorDrawerListener = new SectorDrawerItemClickListener();
		oSectorListView.setOnItemClickListener(sectorDrawerListener);
	
    }
    
    //Override the onRestart method.
    @Override
	protected void onRestart() {
		super.onRestart();
    	
		//Re-init the boolean variable bFirstTimeThrough to true.
		bFirstTimeThrough=true;
		
    	//Remove the fragments.
    	oFT = oFM.beginTransaction();
    	oFT.remove(oF5C_AC);
    	oFT.remove(oF4C_DC);
    	oFT.remove(oF3C_PC);
    	oFT.remove(oF2C_DE);
    	oFT.remove(oF1C_SP);
    	oFT.commitAllowingStateLoss();
    	oFM.executePendingTransactions();
    	
    	//Re-initialize the sector ListView
        oSectorListView = (ListView) findViewById(R.id.sector_drawer);
        String[] aSectorNames = getResources().getStringArray(R.array.sectors);
        CustomArrayAdapter adapter = new CustomArrayAdapter(this,android.R.layout.simple_list_item_1,aSectorNames);
        oSectorListView.setAdapter(adapter);
    	
    	//Re-initialize the fragments
    	initialLoadFragments();
    	
    	//Move the ScrollView up to the top.
    	oScrollView.scrollTo(0, 0);
    	
    }

	//Populate the menu on the ActionBar (will show three vertical dots)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        
        return true;
    }
    
    //Handle the menu item clicks.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId()) {

    	 case R.id.mUsersGuide:
			 Intent intent = new Intent(MainActivity.this,UsersGuideActivity.class);
			 startActivity(intent);
             return true;

    	 case R.id.mManagePorfolio:
    		 Toast.makeText(getApplicationContext(), "This feature is not implemented.", Toast.LENGTH_SHORT).show();    		 
    		 return true;
    	 
    	 case R.id.mSettings:
    		 Toast.makeText(getApplicationContext(), "This feature is not implemented.", Toast.LENGTH_SHORT).show();    		 
             return true;
             
    	 case R.id.mSearch:
			 Intent intentSearch = new Intent(MainActivity.this,SearchActivity.class);
			 startActivity(intentSearch);
             return true;

    	 default:
    		 return super.onOptionsItemSelected(item);
        }
    }

    //Method to load the fragments initially.
    private void initialLoadFragments() {

    	//Instantiate the five fragments.
    	oF1C_SP = new Fragment1SpinnerControl(); //Fragment #1: Spinner
    	oF2C_DE = new Fragment2DetailsControl(); //Fragment #2: Details
    	oF3C_PC = new Fragment3PriceChartControl(); //Fragment #3: Price Chart
    	oF4C_DC = new Fragment4DividendChartControl(); //Fragment #4: Dividend Chart
    	oF5C_AC = new Fragment5AlsoViewedImageControl(); //Fragment #5: Also Viewed Image

    	//Set up a Bundle for use with the five fragment objects.
    	Bundle oBundleArgs = new Bundle();

    	//Populate oBundleArgs for use with the fragments.
    	oBundleArgs.putString("SECTORKEY", "0");
    	oBundleArgs.putString("SYMBOL", "A");
    	oBundleArgs.putInt("WIDTH", iDeviceWidth);
    	oBundleArgs.putInt("HEIGHT", iDeviceHeight);
    	
    	//Fragment #1: Spinner
    	oF1C_SP.setArguments(oBundleArgs);
    	
    	//Fragment #2: Details
    	oF2C_DE.setArguments(oBundleArgs);

    	//Fragment #3: Price Chart
    	oF3C_PC.setArguments(oBundleArgs);
    	
    	//Fragment #4: Dividend Chart
    	oF4C_DC.setArguments(oBundleArgs);

    	//Fragment #5: Also Viewed Image
    	oF5C_AC.setArguments(oBundleArgs);

    	//Begin a fragment transaction
    	oFT = oFM.beginTransaction();
        
    	//Add in the fragments
    	oFT.add(R.id.mainframe,oF1C_SP);
    	oFT.add(R.id.mainframe,oF2C_DE);
    	oFT.add(R.id.mainframe,oF3C_PC);
    	oFT.add(R.id.mainframe,oF4C_DC);
    	oFT.add(R.id.mainframe,oF5C_AC);
    	
    	//Commit the change.
    	oFT.commitAllowingStateLoss();

    	//Execute Pending Transactions
    	oFM.executePendingTransactions(); 

    }
    
    //Create a class to handle clicks for the Sector Drawer.
    private class SectorDrawerItemClickListener implements ListView.OnItemClickListener {

    	@Override
    	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
    	    		
    		//Highlight the clicked sector
    		highlightSector(view,position);
    		
    		//Update the Spinner with the new sector key.
    		updateSpinner(position);
    		
    		//Update fragment #2 (Details)
    		updateDetails(position);
    		
    		//Update fragment #3 (Price Chart)
    		updatePriceChart(position);
    		
    		//Update fragment #4 (Dividend Chart)
    		updateDividendChart(position);

    		//Update fragment #5 (Also Viewed Image)
    		updateAlsoViewedImage(position);

    		//Move ScrollView to top
            oScrollView.scrollTo(0, 0);

    		//Close the drawer
    		oSectorDrawerLayout.closeDrawer(oSectorListView);

    	}

		//Highlight the appropriate sector when it is clicked
    	private void highlightSector(View view,int iPosition) {
    	   		
    		//Change the background color from green to white for the old position
    		if (viewSectorOLD != null) { 
    			viewSectorOLD.setBackgroundColor(Color.rgb(255, 255, 255));		
    		}
    	
    		//Change the background color from white to green for this clicked position.
    		view.setBackgroundColor(Color.rgb(163, 186, 66));
    		
    		//Swap views
    		viewSectorOLD = view;
    		
    	}
    	
    	//Update the spinner with the set of stocks for the newly selected sector.
    	private void updateSpinner(int iPosition) {

    		//Create an array from the sectorkeys array
    		String[] sArray = getResources().getStringArray(R.array.sectorkeys);

    		//String to hold this sectorkey
    		String sThisSectorKey = sArray[iPosition];

    		//Update the spinner in Fragement #1.
    		oF1C_SP.populateSpinner(sThisSectorKey);

    	}
    	
    	//Update the details fragment with the default stock for this sector.
    	private void updateDetails(int iPosition) {
        	
    		//Create an array from the sectordefaultsymbol array
    		String[] sArray = getResources().getStringArray(R.array.sectordefaultsymbol);

    		//String to hold this sectorkey's default stock
    		String sThisSectorKeyDefaultStock = sArray[iPosition];

    		//Update the spinner in Fragment #1.
    		oF2C_DE.populateDetails(sThisSectorKeyDefaultStock);

        }

    	//Update the Price Chart fragment with the default stock for this sector.
        private void updatePriceChart(int iPosition) {
        	
    		//Create an array from the sectordefaultsymbol array
    		String[] sArray = getResources().getStringArray(R.array.sectordefaultsymbol);

    		//String to hold this sectorkey's default stock
    		String sThisSectorKeyDefaultStock = sArray[iPosition];

    		//Update the price chart in Fragment #3.
    		oF3C_PC.populatePriceChart(sThisSectorKeyDefaultStock);
    		
        }

    	//Update the Price Chart fragment with the default stock for this sector.
        private void updateDividendChart(int iPosition) {
        	
    		//Create an array from the sectordefaultsymbol array
    		String[] sArray = getResources().getStringArray(R.array.sectordefaultsymbol);

    		//String to hold this sectorkey's default stock
    		String sThisSectorKeyDefaultStock = sArray[iPosition];

    		//Update the price chart in Fragment #3.
    		oF4C_DC.populateDividendChart(sThisSectorKeyDefaultStock);
    		
        }

    	//Update the Price Chart fragment with the default stock for this sector.
        private void updateAlsoViewedImage(int iPosition) {
        	
    		//Create an array from the sectordefaultsymbol array
    		String[] sArray = getResources().getStringArray(R.array.sectordefaultsymbol);

    		//String to hold this sectorkey's default stock
    		String sThisSectorKeyDefaultStock = sArray[iPosition];

    		//Update the price chart in Fragment #3.
    		oF5C_AC.generateAlsoViewedImage(sThisSectorKeyDefaultStock,iDeviceWidth,iDeviceWidth);
    		
        }

    }
    
    //Create a CustomArrayAdapter from ArrayAdapter in order to highlight the first ListView entry.
    private class CustomArrayAdapter extends ArrayAdapter<String> {
    	
    	 private String[] sArray;
    	 private Context context;
    	 
    	 public CustomArrayAdapter(Context context, int textViewResourceId,String[] objects) {
    	  super(context, textViewResourceId, objects);
    	  this.sArray = objects;
    	  this.context = context;
    	 }

    	 public int getCount() {
    	  return sArray.length;
    	 }
    	 
    	 @Override
    	 public String getItem(int position) {
    	  return sArray[position];
    	 }

    	 @Override
    	 public View getView(int position, View convertView, ViewGroup parent) {

    	  View view = convertView;
    	  
    	  //If convertView is null, inflate it with mytextview.
    	  if (view == null) {
    	   LayoutInflater inflater = (LayoutInflater) 
    	                     context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    	   //Inflate the layout.
    	   view = inflater.inflate(android.R.layout.simple_list_item_1, null); 
    	  }

    	  //Get the current item based on the position passed in
    	  String sItem = sArray[position];
    	  
    	  //Create a variable to point to our TextView tvSS.
    	  TextView oTVSS = (TextView) view.findViewById(android.R.id.text1);
    	  
    	  //Set the text to whatever you want in mytextview
    	  oTVSS.setText(sItem);
    	  
    	  //Set the background
    	  if (bFirstTimeThrough && position==0) {
    		  view.setBackgroundColor(Color.rgb(163, 186, 66));
    		  viewSectorOLD = view;
    	  }
    	  else {
    		  view.setBackgroundColor(Color.rgb(255, 255, 255));    		  
    	  }
    	  
    	  if (bFirstTimeThrough) {
    		  bFirstTimeThrough=false;
    	  }
    	  
    	  return view;
    	 }
    	
    }

    //Called when the user changes the Fragment #1 spinner.
	@Override
	public void OnFragment1SpinnerChanged(String sSymbol) {
		
		oF2C_DE.populateDetails(sSymbol);
		oF3C_PC.populatePriceChart(sSymbol);
		oF4C_DC.populateDividendChart(sSymbol);
		oF5C_AC.generateAlsoViewedImage(sSymbol,iDeviceWidth,iDeviceWidth);

	}
}
