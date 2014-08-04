package com.sheepsqueezers.equityyostocks;

import com.sheepsqueezers.equityyostocks.R;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment1SpinnerControl extends Fragment {

	String sSectorKey = "0"; //By default, the sector key is zero indicating pull all stocks from all sectors.
	View oFragment1Spinner;
	OnFragment1SpinnerChangedListener mCallback;
	
	//Interface used to alert the parent Activity of a change in the Fragment 1 spinner.
	public interface OnFragment1SpinnerChangedListener {
		public void OnFragment1SpinnerChanged(String sSymbol);
	}
	
	//Pull the arguments from the setArguments Bundle, if it exists; otherwise, sector key is zero.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sSectorKey = getArguments() != null ? getArguments().getString("SECTORKEY") : "0";
    }

	//Make the fragment_main.xml file appear in the <fragment> of activity_main.xml.
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		//Create a View object from fragment_main
		 oFragment1Spinner = inflater.inflate(R.layout.fragment1_spinner,container,false); 
		 
		//Add the list of stocks to the spinner in this fragment 
		populateSpinner(sSectorKey);
		
		return oFragment1Spinner;
	}

	//Hook up mCallback to the activity
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallback = (OnFragment1SpinnerChangedListener) activity;		
	}
    
    //populateSpinner
    //Populate the Fragment #1 (fragment1_spinner.xml) Spinner (idfragment1_spinner)
    //with the appropriate data based on the sector key number.
	public void populateSpinner(String sSectorKey) {

    	EquityYoDatabaseAccess equityYoDB = new EquityYoDatabaseAccess(getActivity().getApplicationContext());
    	SQLiteDatabase dbEquityYoDB = equityYoDB.getReadableDatabase(); 
    	String sSQLClause;
    	
    	//Set up the SQL clause based on the iSectorKey
    	if (sSectorKey.equals("0")) {
    	 sSQLClause="SELECT DISTINCT _id,Symbol || '/' || sName AS SYMBOL_SNAME FROM EquityYoSymbolMaster WHERE Type='S' AND SectorKey IS NOT NULL AND IndustryKey IS NOT NULL ORDER BY 2";	    	 
    	}
    	else {
       	 sSQLClause="SELECT DISTINCT _id,Symbol || '/' || sName AS SYMBOL_SNAME FROM EquityYoSymbolMaster WHERE Type='S' AND SectorKey IS NOT NULL AND IndustryKey IS NOT NULL AND SectorKey=" + sSectorKey + " ORDER BY 2";	    	 
    	}

    	//Obtain a cursor for the query.
        Cursor oCSR = dbEquityYoDB.rawQuery(sSQLClause, null);
        
        //Attach the cursor to the Fragment #1 Spinner.
        //Instantiate the Spinner as an object 
        Spinner oSP = (Spinner) oFragment1Spinner.findViewById(R.id.idfragment1_spinner);

        //Create a new instance of GenericSpinnerListener for the Spinner.
        oSP.setOnItemSelectedListener(new cGenericSpinnerListener());
        
        //Set up a cursor adapter for the ListView using oCSR
        SimpleCursorAdapter adapter = 
                        new SimpleCursorAdapter(oFragment1Spinner.getContext(),
                                                android.R.layout.simple_spinner_item,
                                                oCSR,
                                                new String[] {"SYMBOL_SNAME"},
                                                new int[] {android.R.id.text1},0);

        //Indicate which layout to use when the drop-down arrow is clicked.
        adapter.setDropDownViewResource(
                                      android.R.layout.simple_spinner_dropdown_item);

        //Assign adapter as the adapter to the spinner.
        oSP.setAdapter(adapter);
        
    	return;
    }

    //*-----------------------------------------------------------------------*
    //* Name: cGenericSpinnerListener                                         *
    //* Type: Class                                                           *
    //* Purpose: This class implements the OnItemSelectedListener in order to *
    //*          to create a generic listener for the spinners that appear in *
    //*          the DrawerLayout.                                            *
    //*-----------------------------------------------------------------------*
    private class cGenericSpinnerListener implements OnItemSelectedListener {
    
    	private boolean isFirstTimeThru = true;
    	
    	// The onItemSelected method is triggered when a user changes the 
    	// selected item in a spinner. 
    	@Override
    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    		if (isFirstTimeThru) {
    			
    			isFirstTimeThru = false;
    			
    		} else {
    			
    			//Based on the new symbol, tell the parent Activity to update fragments 2, 3, 4, and 5.
    			String sSpinnerText = (String) ((TextView) view).getText();
    			String sSymbol = sSpinnerText.substring(0, sSpinnerText.indexOf("/"));    			
    			mCallback.OnFragment1SpinnerChanged(sSymbol);
    			
    		}
    	      	 
    	}
    	
    	@Override
    	public void onNothingSelected(AdapterView<?> parent) {
    	 // NOP
    	}
    	
    } //end of private class GenericSpinnerListener
		
}
