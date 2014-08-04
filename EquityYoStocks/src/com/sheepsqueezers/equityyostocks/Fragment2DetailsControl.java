package com.sheepsqueezers.equityyostocks;

import java.text.DecimalFormat;

import com.sheepsqueezers.equityyostocks.R;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment2DetailsControl extends Fragment {

	String sSymbol = "A"; //By default, the symbol for sector zero is A.
	View oFragment2Details;
	int[] aiTextViewIDs = new int[] {
			R.id.tvSTOCK_Symbol,
			R.id.tvSTOCK_MaxMonthPriceNAV,
			R.id.tvSTOCK_sName,
			R.id.tvSTOCK_sInfo,
			R.id.tvSTOCK_Sector,
			R.id.tvSTOCK_Industry,
			R.id.tvSTOCK_sMktCapNetAssets,
			R.id.tvSTOCK_sAverage3MonthVolume,
			R.id.tvSTOCK_sBeta,
			R.id.tvSTOCK_sEPS,
			R.id.tvSTOCK_sPERatio,
			R.id.tvSTOCK_sTargetEstimate,
			R.id.tvSTOCK_PriceGain,
			R.id.tvSTOCK_ComputedYield,
			R.id.tvSTOCK_ComputedDividend,
			R.id.tvSTOCK_sDisplayedYield,
			R.id.tvSTOCK_sDisplayedDividend,
			R.id.tvSTOCK_DividendFrequency,
			R.id.tvSTOCK_DividendConsistency,
			R.id.tvSTOCK_DivGain
			};

	String[] asDetailColumnNames = new String[] {
			"Symbol",
			"MaxMonthPriceNAV",
			"sName",
			"sInfo",
			"Sector",
			"Industry",
			"sMktCapNetAssets",
			"sAverage3MonthVolume",
			"sBeta",
			"sEPS",
			"sPERatio",
			"sTargetEstimate",
			"YearlyGrowth",
			"ComputedYield",
			"ComputedDividend",
			"sDisplayedYield",
			"sDisplayedDividend",
			"DividendFrequency",
			"DividendConsistency",
			"YearlyDivCapGain"
			};

	String[] asDetailColumnLabels = new String[] {
			"Symbol: ",
			"Last Price: $",
			"Company Name: ",
			"Description: ",
			"Sector: ",
			"Industry: ",
			"Market Capitalization: ",
			"Average 3-Month Volume: ",
			"Beta: ",
			"Earnings Per Share(EPS): ",
			"Price/Earnings (PE) Ratio: ",
			"12-Month Target Estimate: ",
			"$10,000 Growth (due to Price): $ ",
			"Computed Yield: ",
			"Computed Dividend: ",
			"Displayed Yield: ",
			"Displayed Dividend: ",
			"Dividend Frequency: ",
			"Dividend Consistency: ",
			"$10,000 Growth (due to Div/CapGain): $"
			};
	
	//Pull the arguments from the setArguments Bundle, if it exists; otherwise, sector key is zero.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sSymbol = getArguments() != null ? getArguments().getString("SYMBOL") : "A";
    }

	//Make the fragment_main.xml file appear in the <fragment> of activity_main.xml.
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		//Create a View object from fragment_main
		 oFragment2Details = inflater.inflate(R.layout.fragment2_details,container,false); 

		//Add the list of stocks to the spinner in this fragment 
		populateDetails(sSymbol);
		
		return oFragment2Details;
	}
	   
    //populateDetails
    //Populate the Fragment #1 (fragment1_spinner.xml) Spinner (idfragment1_spinner)
    //with the appropriate data based on the sector key number.
	public void populateDetails(String sSymbol) {

    	EquityYoDatabaseAccess equityYoDB = new EquityYoDatabaseAccess(getActivity().getApplicationContext());
    	SQLiteDatabase dbEquityYoDB = equityYoDB.getReadableDatabase(); 
    	String sSQLClause;
    	int iColumnIndex;
    	String sColumnValue="";
    	TextView aTextView;
    	String sColumnLabel="";
    	
    	//Set up the SQL clause based on the iSectorKey
       	sSQLClause="SELECT * FROM vwEquityYoSymbolMasterSTOCKS WHERE Symbol=" + '"' + sSymbol + '"';	    	 
    	Log.d("MYAPPTAG","sSQLClause=" + sSQLClause);

    	//Obtain a cursor for the query.
        Cursor oCSR = dbEquityYoDB.rawQuery(sSQLClause, null);
        oCSR.moveToFirst();
        
        //Only one line will come in from the database.  
        //Match up each textview with its corresponding data from the cursor.
    	//Loop around filling in the TextViews.
        for(int i=0;i<aiTextViewIDs.length;i++) {
        	        
        	//Get the index of the column based on its column name
        	iColumnIndex = oCSR.getColumnIndex(asDetailColumnNames[i]);

        	//Get the column label
        	sColumnLabel = asDetailColumnLabels[i];

        	//Set the column text to the corresponding cursor entry.
        	if (asDetailColumnNames[i] == "DividendFrequency") {
            	sColumnValue = oCSR.isNull(iColumnIndex) ? "-" : Integer.toString(oCSR.getInt(iColumnIndex));
        	}
        	else if (asDetailColumnNames[i] == "sDisplayedDividend" || asDetailColumnNames[i] == "sDisplayedYield") {
        		sColumnValue = oCSR.isNull(iColumnIndex) ? "-" : oCSR.getString(iColumnIndex);	    		
        	}
        	else if (asDetailColumnNames[i] == "ComputedDividend") {
	    		if (!oCSR.isNull(iColumnIndex)) {
		    		
	        		//Get the data from the cursor based on the column index
		    		float fComputedDividend = oCSR.getFloat(iColumnIndex);

		    		//Set up a DecimalFormat
		    		DecimalFormat oNF = new DecimalFormat("#,###.##");
		    		sColumnValue = oNF.format(fComputedDividend);
	    		
	    		} else {

	    			sColumnValue = "-";
	    			
	    		}
        	}
        	else if (asDetailColumnNames[i] == "ComputedYield") {

        		if (!oCSR.isNull(iColumnIndex)) {
		    		
	        		//Get the data from the cursor based on the column index
		    		float fComputedYield = oCSR.getFloat(iColumnIndex);

		    		//Set up a DecimalFormat
		    		DecimalFormat oNF = new DecimalFormat("#,###.##");
		    		sColumnValue = oNF.format(fComputedYield)  + "%";
	    		
	    		} else {

	    			sColumnValue = "-";
	    			
	    		}
        		
        	}
        	else if (asDetailColumnNames[i] == "MaxMonthPriceNAV") {

	    		if (!oCSR.isNull(iColumnIndex)) {
		    		
	        		//Get the data from the cursor based on the column index
		    		float fMaxMonthPriceNAV = oCSR.getFloat(iColumnIndex);

		    		//Set up a DecimalFormat
		    		DecimalFormat oNF = new DecimalFormat("#,###,###.00");
		    		sColumnValue = oNF.format(fMaxMonthPriceNAV) ;	    				
	    				
	    		
	    		} else {
	    			
		    		sColumnValue = "-";
	    			
	    		}

        	}
        	else if (asDetailColumnNames[i] == "YearlyDivCapGain") {
        		
	    		if (!oCSR.isNull(iColumnIndex)) {
		    		
	        		//Get the data from the cursor based on the column index
		    		float fYearlyDivCapGain = oCSR.getFloat(iColumnIndex);

		    		//Set up a DecimalFormat
		    		DecimalFormat oNF = new DecimalFormat("#,###,###.00");
		    		sColumnValue = oNF.format(fYearlyDivCapGain);
	    		
	    		} else {

	    			sColumnValue = "-";
	    			
	    		}
        		
        	}
        	else if (asDetailColumnNames[i] == "YearlyGrowth") {

	    		if (!oCSR.isNull(iColumnIndex)) {
		    		
	        		//Get the data from the cursor based on the column index
		    		float fYearlyGrowth = oCSR.getFloat(iColumnIndex);

		    		//Set up a DecimalFormat
		    		DecimalFormat oNF = new DecimalFormat("#,###,###.00");
		    		sColumnValue = oNF.format(fYearlyGrowth);
	    		
	    		} else {

	    			sColumnValue = "-";
	    			
	    		}

        	}
	    	else if (asDetailColumnNames[i] == "DividendConsistency" || asDetailColumnNames[i] == "Sector" || asDetailColumnNames[i] == "Industry") {

            	//Get the data from the cursor based on the column index
            	sColumnValue = oCSR.isNull(iColumnIndex) ? "-" : oCSR.getString(iColumnIndex);
	    		        			    		
	    	}    
	    	else if (asDetailColumnNames[i] == "Symbol" || asDetailColumnNames[i] == "sName") {

            	//Get the data from the cursor based on the column index
            	sColumnValue = oCSR.getString(iColumnIndex);
	    		        			    		
	    	}        	
	    	else if (asDetailColumnNames[i] == "sInfo") {

            	//Get the data from the cursor based on the column index
            	sColumnValue = oCSR.isNull(iColumnIndex) ? "-" : oCSR.getString(iColumnIndex);
            	
            	//Move the sInfo TextView back to the top.
            	TextView oTV = (TextView) oFragment2Details.findViewById(R.id.tvSTOCK_sInfo);
            	oTV.setMaxLines(10);
            	oTV.setMovementMethod(ScrollingMovementMethod.getInstance());
            	oTV.setOnTouchListener(new View.OnTouchListener() {

            		@Override
	    			public boolean onTouch(View v, MotionEvent event) 
	    			{
	    				v.getParent().requestDisallowInterceptTouchEvent(true);
	    				return false;
	    			}
	    			
	    		});
            	oTV.scrollTo(0, 0);
	    		        			    		
	    	}        	        	
	    	else if (asDetailColumnNames[i] == "sMktCapNetAssets") {

            	sColumnValue = oCSR.isNull(iColumnIndex) ? "-" : oCSR.getString(iColumnIndex);

	    	}        	        	
	    	else if (asDetailColumnNames[i] == "sAverage3MonthVolume") {

            	sColumnValue = oCSR.isNull(iColumnIndex) ? "-" : oCSR.getString(iColumnIndex);

	    	}        	        	
	    	else if (asDetailColumnNames[i] == "sBeta" || asDetailColumnNames[i] == "sEPS" || asDetailColumnNames[i] == "sPERatio" || asDetailColumnNames[i] == "sTargetEstimate") {

            	sColumnValue = oCSR.isNull(iColumnIndex) ? "-" : oCSR.getString(iColumnIndex);

	    	}        	        	
        	else {
        		//sColumnValue = oCSR.getString(iColumnIndex);
        		sColumnValue = oCSR.isNull(iColumnIndex) ? "-" : oCSR.getString(iColumnIndex);
        	}
        	
        	//Create pointer to object TextView for this iteration
	    	aTextView = (TextView) oFragment2Details.findViewById(aiTextViewIDs[i]);

	    	//Set the text
        	aTextView.setText(sColumnLabel + sColumnValue);
       	
        }
        

    	return;
    }
	
	
}
