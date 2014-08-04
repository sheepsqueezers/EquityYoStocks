package com.sheepsqueezers.equityyostocks;

import com.sheepsqueezers.equityyostocks.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchActivity extends ActionBarActivity {

	FragmentManager oFM;
	FragmentTransaction oFT;
	FragmentSearch oFC_SE;
	FragmentSearch oFC_SE_OLD=null;
	
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_search);
	        
	    	//Access the FragmentManager.
	    	oFM = (FragmentManager) getSupportFragmentManager();
	    	
	  }
	  
	  //Handle the onClick event for the Go! button!
	  public void SearchDatabase(View view) {

	    	//Set up a Bundle for use with the search fragment.
	    	Bundle oBundleArgs = new Bundle();
	    	
	    	//Pull the text in the TextView tvSearchText
	    	EditText oET = (EditText) findViewById(R.id.etSearchText);
	    	String sSearchText = oET.getText().toString().toUpperCase();
	    	
	    	//Add the text to the Bundle
	    	oBundleArgs.putString("SEARCHTEXT", sSearchText);
	    	
		    //Begin a fragment transaction
	    	oFT = oFM.beginTransaction();
	        
	      	//Add a custom animation
	    	oFT.setCustomAnimations(R.anim.my_slide_in_from_right,R.anim.my_slide_out_to_right);

	    	//Instantiate the five fragments.
	    	oFC_SE = new FragmentSearch(); 

	    	//Add the Bundle to oFC_SE
	    	oFC_SE.setArguments(oBundleArgs);
	    	
	    	//Remove previous fragment, if it exists.
	    	if (oFC_SE_OLD != null) {
		    	oFT.remove(oFC_SE_OLD);
	    		oFC_SE_OLD=oFC_SE;
		    	oFT.addToBackStack(null);	    		
	    	}
	    	else if (oFC_SE_OLD == null) {
	    		oFC_SE_OLD=oFC_SE;	    		
	    	}

	    	//Add in the fragments
	    	oFT.add(R.id.searchframe,oFC_SE);	    		
	    	
	    	//Commit the change.
	    	oFT.commit();

	    	//Execute Pending Transactions
	    	oFM.executePendingTransactions(); 
	    	
	  }
	  
	 
	  //Close the SearchActivity.
	  public void CloseSearchActivity() {
		  this.finish();		  
	  }
	  
}
