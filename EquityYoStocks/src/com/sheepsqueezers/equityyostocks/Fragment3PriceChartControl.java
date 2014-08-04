package com.sheepsqueezers.equityyostocks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.iguanaui.controls.DataChart;
import com.iguanaui.controls.Series;
import com.iguanaui.controls.axes.CategoryAxis;
import com.iguanaui.controls.axes.CategoryXAxis;
import com.iguanaui.controls.axes.NumericAxis;
import com.iguanaui.controls.axes.NumericYAxis;
import com.iguanaui.controls.valuecategory.AreaSeries;
import com.iguanaui.controls.valuecategory.ColumnSeries;
import com.iguanaui.controls.valuecategory.ValueCategorySeries;
import com.iguanaui.graphics.SolidColorBrush;
import com.sheepsqueezers.equityyostocks.R;

public class Fragment3PriceChartControl extends Fragment {

	String sSymbol = "A"; //By default, the symbol for sector zero is A.
	View oFragment3PriceChart;
	DataChart igPriceChart,igDivChart;
	cEquityYoIguanaPriceCharts PriceChart,DivChart;

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
		oFragment3PriceChart = inflater.inflate(R.layout.fragment3_price,container,false); 

		//Set up the price chart.
        igPriceChart = (DataChart) oFragment3PriceChart.findViewById(R.id.igPriceChart);
        PriceChart = new cEquityYoIguanaPriceCharts("P",igPriceChart);
        
		//Add the list of stocks to the spinner in this fragment 
		populatePriceChart(sSymbol);
		
		return oFragment3PriceChart;
	}
	   	
	//Populate the chart with the data for the requested symbol.
	public void populatePriceChart(String sSymbol) {

		//Update teh price chart with the data for this symbol.
        PriceChart.UpdatePriceChart(sSymbol, igPriceChart);
        
        //igDivChart = (DataChart) findViewById(R.id.igDivChart);
        //DivChart = new cEquityYoIgunaCharts("D",igDivChart);
        //DivChart.UpdateDivChart(sSymbol, igDivChart);

	}
		
	//Class to handle the price charts.
	public class cEquityYoIguanaPriceCharts {

		//Set up a number format
		final NumberFormat numberFormat = NumberFormat.getInstance();
		private List<String> categories;
		private List<Float> column1;
		private ValueCategorySeries series;
		private CategoryXAxis categoryAxis;
		private NumericYAxis valueAxis;
		
	  	EquityYoDatabaseAccess equityYoDB = new EquityYoDatabaseAccess(getActivity().getApplicationContext());
		SQLiteDatabase dbEquityYoDB = equityYoDB.getReadableDatabase(); 
		String sSQLClause;
			
		//Constructor
		public cEquityYoIguanaPriceCharts(String pWhichData,DataChart igChart) {

			categoryAxis = new CategoryXAxis();
			valueAxis = new NumericYAxis();
			
			categoryAxis.setLabelBrush(new SolidColorBrush(Color.BLACK));
			valueAxis.setLabelBrush(new SolidColorBrush(Color.BLACK));
			
			categories = new ArrayList<String>();
			categories.add("1");
			categories.add("2");
			categories.add("3");
			categories.add("4");
			categories.add("5");
			categories.add("6");
			categories.add("7");
			categories.add("8");
			categories.add("9");
			categories.add("10");
			categories.add("11");
			categories.add("12");
			
			column1 = new ArrayList<Float>();
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
			column1.add(0.0f);
					
			//Create the X-Axis categories
			categoryAxis.setDataSource(categories);
			categoryAxis.setLabelFormatter(new CategoryAxis.LabelFormatter() {
				
				@Override
				public String format(CategoryAxis axis, Object item) {
					
					String sItem = item.toString();
					String sResult = "W";
					
					if (sItem.contentEquals("1")) sResult="J";
					if (sItem.contentEquals("2")) sResult="F";
					if (sItem.contentEquals("3")) sResult="M";
					if (sItem.contentEquals("4")) sResult="A";
					if (sItem.contentEquals("5")) sResult="M";
					if (sItem.contentEquals("6")) sResult="J";
					if (sItem.contentEquals("7")) sResult="J";
					if (sItem.contentEquals("8")) sResult="A";
					if (sItem.contentEquals("9")) sResult="S";
					if (sItem.contentEquals("10")) sResult="O";
					if (sItem.contentEquals("11")) sResult="N";
					if (sItem.contentEquals("12")) sResult="D";

					return sResult;
				}
				
			});
			igChart.scales().add(categoryAxis);
			
			//Create the Y-Axis
			valueAxis.setMinimumValue(valueAxis.getMinimumValue());
			valueAxis.setMaximumValue(valueAxis.getMaximumValue());
			valueAxis.setLabelFormatter(new NumericAxis.LabelFormatter() {
				
				@Override
				public String format(NumericAxis axis, float item, int precision) {
					
					if (precision != numberFormat.getMinimumFractionDigits()) {
						numberFormat.setMinimumFractionDigits(precision);
						numberFormat.setMaximumFractionDigits(precision);
					}
					
					return numberFormat.format(item);
				}
				
			});
			igChart.scales().add(valueAxis);
			
			if (pWhichData == "P") {

				series = new AreaSeries();
				
			}
			else if (pWhichData == "D") {

				series = new ColumnSeries();
				
			}

			series.setCategoryAxis(categoryAxis);
			series.setValueAxis(valueAxis);
			series.setValueMember("");
			series.setDataSource(column1);
			igChart.series().add(series);
			
		}
		
		//Update the chart
		public void UpdatePriceChart(String sSymbol,DataChart igChart) {

			int iMonth;
	    	float fDiv,fPrice;
	    	EquityYoDatabaseAccess equityYoDB = new EquityYoDatabaseAccess(getActivity().getApplicationContext());
	    	SQLiteDatabase dbEquityYoDB = equityYoDB.getReadableDatabase(); 
	    	String sSQLClause;
	    	String sWhichData = "P";
	    	String sType="S";
	    	
			if (sWhichData=="P") {		    	
				
		    	//Query the database table EquityYoPriceData.
			    sSQLClause = "SELECT Month,CAST(TOTAL(Price) AS REAL) AS Price FROM (SELECT Month,Price FROM EquityYoPriceData WHERE Type=? AND Symbol=? UNION ALL SELECT Month,0 AS Price FROM EquityYoDateData) GROUP BY Month ORDER BY Month";  	

			    //Execute the query
		    	Cursor oCsr = dbEquityYoDB.rawQuery(sSQLClause, new String[] {sType,sSymbol});
		        oCsr.moveToFirst();
		        
		    	//Pull in the indices for the month and price to be used below. 
		    	int iMonthIndex = oCsr.getColumnIndex("Month");
		    	int iPriceIndex = oCsr.getColumnIndex("Price");    	
		    	
		    	//Fill in categories and column1 based on the Month and Price data.
		    	if (oCsr.getCount()>0) { 
			    	while (oCsr.moveToNext()) {
			    		
			    		//Pull the month and format it to a character string.
			    		iMonth = oCsr.getInt(iMonthIndex);
			
			    		//Pull the price.
			    		fPrice = oCsr.getFloat(iPriceIndex);
			
			    		//Fill in the arrays
			    		categories.set(iMonth-1,Integer.toString(iMonth));
			    		column1.set(iMonth-1,fPrice);

			    	}
		    	}

		    	//Close the cursor.
		    	oCsr.close();
		    	
		    	valueAxis.setMinimumValue(0.90f * valueAxis.getMinimumValue());
				valueAxis.setMaximumValue(1.10f * valueAxis.getMaximumValue());
		    	
				series.setDataSource(null);
				series.setDataSource(column1);
				for (Series s: igChart.series()) {
					s.notifyDataReset();
				}
				series.notifyDataUpdate(0, 12);
				
			}
			else if (sWhichData=="D") {

		     	//Query the database table EquityYoPriceData.
			    sSQLClause = "SELECT Month,CAST(TOTAL(Dividend) AS REAL) AS Dividend FROM (SELECT Month,Dividend FROM EquityYoDividendData WHERE Type=? AND Symbol=? UNION ALL SELECT Month,0 AS Dividend FROM EquityYoDateData) GROUP BY Month ORDER BY Month";  	
			        	
		    	//Execute the query
		    	Cursor oCsr = dbEquityYoDB.rawQuery(sSQLClause, new String[] {sType,sSymbol});
		        oCsr.moveToFirst();
		        
		    	//Pull in the indices for the month and price to be used below. 
		    	int iMonthIndex = oCsr.getColumnIndex("Month");
		    	int iDivIndex = oCsr.getColumnIndex("Dividend");    	

		    	//Fill in categories and column1 based on the Month and Price data.
		    	if (oCsr.getCount()>0) { 
			    	while (oCsr.moveToNext()) {
			    		
			    		//Pull the month and format it to a character string.
			    		iMonth = oCsr.getInt(iMonthIndex);

			    		//Pull the price.
			    		fDiv = oCsr.getFloat(iDivIndex);
			
			    		//Fill in the arrays
			    		categories.set(iMonth-1,Integer.toString(iMonth));
			    		column1.set(iMonth-1,fDiv);
			    				
			    	}
		    	}
		    	else {
		    		for(int i=0;i<12;i++) {

		    			categories.set(i,Integer.toString(i+1));
			    		column1.set(i,0.0f);
		    			
		    		}
		    	}

		    	//Close the cursor.
		    	oCsr.close();
		    		
		    	//valueAxisDiv.setMinimumValue(0.90f * valueAxisDiv.getMinimumValue());
		    	valueAxis.setMinimumValue(0.00f);
				valueAxis.setMaximumValue(1.10f * valueAxis.getMaximumValue());
		    	
				series.setDataSource(null);
				series.setDataSource(column1);
				for (Series s: igChart.series()) {
					s.notifyDataReset();
				}
				series.notifyDataUpdate(0, 12);
				
			} 
		
		}
		
	}
	
}