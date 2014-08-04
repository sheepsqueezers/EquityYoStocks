package com.sheepsqueezers.equityyostocks;

import com.sheepsqueezers.equityyostocks.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FragmentSearch extends ListFragment {

	View oFragmentSearch;
	String sSearchText="-";
	
	//Pull the arguments from the setArguments Bundle, if it exists; otherwise, sector key is zero.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        sSearchText = getArguments() != null ? getArguments().getString("SEARCHTEXT") : "-";

        if (sSearchText.length() == 0) {
        	sSearchText="-";
        }
  
    }

	//Make the fragment_main.xml file appear in the <fragment> of activity_main.xml.
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        
		//Create a View object from fragment_main
		oFragmentSearch = inflater.inflate(R.layout.fragment_search,container,false); 

		//Populate the ListView based on the search criteria.
		SimpleCursorAdapter adapter=populateListView(sSearchText);
		
		//Set the list adapter here.
		setListAdapter(adapter);
		
		return oFragmentSearch;
	}
	
	public SimpleCursorAdapter populateListView(String sSearchText) {
	
		EquityYoDatabaseAccess equityYoDB = new EquityYoDatabaseAccess(getActivity().getApplicationContext());
		SQLiteDatabase dbEquityYoDB = equityYoDB.getReadableDatabase(); 
		String sSQLClause="";
		SimpleCursorAdapter adapter;
		
		//Set up the SQL clause based on the iSectorKey
		if (sSearchText.equals("-") || sSearchText.trim().length() == 0) {

			//Display a DialogFragment indicating that no usable search criteria was entered.
		    DialogFragment oAlertNoDataFragment = FragmentAlertSearchCriteriaControl.newInstance();
		    oAlertNoDataFragment.show(getFragmentManager(), "dialog");
			
			//Set the adapter to null.
			adapter=null;
		}
		else {
			sSQLClause="SELECT _id,Symbol || '/' || sName AS SYMBOL_SNAME FROM vwEquityYoSymbolMasterSTOCKS WHERE sName LIKE '%" + sSearchText + "%' ORDER BY 1";

			//Obtain a cursor for the query.
			Cursor oCSR = dbEquityYoDB.rawQuery(sSQLClause, null);
			oCSR.moveToFirst();
			
			//Set up a cursor adapter for the ListView using oCSR
			adapter = new SimpleCursorAdapter(oFragmentSearch.getContext(),
	                android.R.layout.simple_list_item_1,
	                oCSR,
	                new String[] {"SYMBOL_SNAME"},
	                new int[] {android.R.id.text1},0);
	          
		}

		return adapter;
	}
	
	//Class to handle the no search results alert.
	public static class FragmentAlertSearchCriteriaControl extends DialogFragment {

	    public static FragmentAlertSearchCriteriaControl newInstance() {
	    	FragmentAlertSearchCriteriaControl oAlert = new FragmentAlertSearchCriteriaControl();
	        return oAlert;
	    }

	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {

	    	//Set up several string variables.
	    	String sTitle = "Enter in your search criteria!";
	    	String sSearchAgain = "Search Again"; 
	    	String sGoHome = "Go Home"; 
	    	
	    	//Create an Alert Dialog object via the Builder.
	    	AlertDialog.Builder oADB = new AlertDialog.Builder(getActivity());

	    	//Set the title of the AlertDialog
	    	oADB.setTitle(sTitle);
	    	
	    	//Set the icon to the app icon.
	    	oADB.setIcon(R.drawable.ic_launcher);
	    	
	    	//Create a listener associated with the Search Again click.
	    	DialogInterface.OnClickListener onClickSearchAgainListener = new DialogInterface.OnClickListener() {
				
				//Close the dialog box itself when user clicks Search Again.
	    		@Override
                public void onClick(DialogInterface dialog, int whichButton) {
                	dialog.cancel(); //Close this dialog box
                }
				
			}; 
	    	
			//Associate the setNegativeButton with the listener above.
			oADB.setNegativeButton(sSearchAgain, onClickSearchAgainListener);
			
	    	//Create a listener associated with the Go Home click.
	    	DialogInterface.OnClickListener onClickGoHomeListener = new DialogInterface.OnClickListener() {
				
				//Close the SearcActivity when user clicks Go Home.
	    		@Override
                public void onClick(DialogInterface dialog, int whichButton) {
                	((SearchActivity)getActivity()).CloseSearchActivity(); 
                }
				
			}; 
			
			//Associate the setNegativeButton with the listener above.
			oADB.setPositiveButton(sGoHome, onClickGoHomeListener);

			//Create the AlertDialog and return it.
			return oADB.create();
			
	    }

	}
	
}
