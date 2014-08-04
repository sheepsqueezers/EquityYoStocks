package com.sheepsqueezers.equityyostocks;

import com.sheepsqueezers.equityyostocks.R;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.FloatMath;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Fragment5AlsoViewedImageControl extends Fragment {

	String sSymbol = "A"; //By default, the symbol for sector zero is A.
	View oFragment5AlsoViewedImage;
	private cAlsoViewed AlsoViewed;
	private ImageView igAVImageView;
	int iAlsoViewedMeasuredHeight=0;
	int iAlsoViewedMeasuredWidth=0;
	String sPV;

	//Pull the arguments from the setArguments Bundle, if it exists; otherwise, sector key is zero.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sSymbol = getArguments() != null ? getArguments().getString("SYMBOL") : "A";
        iAlsoViewedMeasuredHeight = getArguments() != null ? getArguments().getInt("HEIGHT") : 0;
        iAlsoViewedMeasuredWidth = getArguments() != null ? getArguments().getInt("WIDTH") : 0;
    }

	//Make the fragment_main.xml file appear in the <fragment> of activity_main.xml.
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		//Create a View object from fragment_main
		oFragment5AlsoViewedImage = inflater.inflate(R.layout.fragment5_alsoviewed,container,false); 

        igAVImageView = (ImageView) oFragment5AlsoViewedImage.findViewById(R.id.igAVImageView);
        AlsoViewed = new cAlsoViewed(igAVImageView);
		
        //Generate the image first time round.
        generateAlsoViewedImage(sSymbol,iAlsoViewedMeasuredWidth,iAlsoViewedMeasuredWidth);
        
		return oFragment5AlsoViewedImage;
	}
	
	//Generate the Also Viewed image.
	public void generateAlsoViewedImage(String sSymbol,int piHeight,int piWidth) {

		AlsoViewed.UpdateAlsoViewedImage(sSymbol, piHeight, piWidth);

	}
	
	//Class to handle generating the AlsoViewed bitmap image.
	public class cAlsoViewed {

		//String sSymbol = "";
		String sPV = "-";
		ImageView oAVIV = null;
		
	  	EquityYoDatabaseAccess equityYoDB = new EquityYoDatabaseAccess(getActivity().getApplicationContext());
		SQLiteDatabase dbEquityYoDB = equityYoDB.getReadableDatabase(); 
		String sSQLClause;
		
		public cAlsoViewed(ImageView poAVImageView) {
			
			oAVIV = poAVImageView;
			
		}

		public void UpdateAlsoViewedImage(String psSymbol,int piHeight,int piWidth) {

			sSymbol = psSymbol;
			iAlsoViewedMeasuredHeight = piHeight;
			iAlsoViewedMeasuredWidth = piWidth;

			//Pull the PV column from the database.
		    sSQLClause = "SELECT sPV FROM vwEquityYoSymbolMasterSTOCKS WHERE sPV IS NOT NULL AND SYMBOL='" + sSymbol + "'";  	

		    //Execute the query
	    	Cursor oCSR = dbEquityYoDB.rawQuery(sSQLClause, null);
	        oCSR.moveToFirst();
        	int iColumnIndex = oCSR.getColumnIndex("sPV");
	        if (oCSR.getCount() > 0) {
	        	sPV = (oCSR.isNull(iColumnIndex) ? "-" : oCSR.getString(iColumnIndex));
	        }
	        else {
	        	sPV="-";
	        }
	    	
			Rect oSymbolRect = new Rect();
			Rect oTextRect = new Rect();

			//Update the imageview's height.
			oAVIV.setMinimumHeight(iAlsoViewedMeasuredHeight);

			//Create a blank bitmap.
			Bitmap oBM = Bitmap.createBitmap(iAlsoViewedMeasuredHeight, iAlsoViewedMeasuredWidth, Bitmap.Config.ARGB_8888);
			
			//Create a Canvas associating the bitmap oBM with it,
			Canvas oCanvas = new Canvas(oBM);
			
			//Paint to canvas oCanvas.
			//oCanvas.drawARGB(255, 173, 188, 227); 
			oCanvas.drawARGB(255, 255, 255, 255); 
						
			//Create a Paint object for the line 
			Paint oPT_LINE = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
			oPT_LINE.setARGB(255, 255, 255, 255);
			oPT_LINE.setStrokeWidth(3);
			oPT_LINE.setColor(Color.BLACK);
			oPT_LINE.setStyle(Style.STROKE);
			
			//Create a Paint object for the dot
			Paint oPT_DOT = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
			oPT_DOT.setARGB(255, 255, 255, 255);
			oPT_DOT.setStrokeWidth(3);
			oPT_DOT.setColor(Color.BLACK);
			oPT_DOT.setStyle(Style.FILL_AND_STROKE);		

			//Create a Paint object for the text (black and bold)
			Paint oPT_TEXT = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
			oPT_TEXT.setARGB(255, 255, 255, 255);
			oPT_TEXT.setTypeface(Typeface.DEFAULT_BOLD);
			oPT_TEXT.setTextSize(24);
			oPT_TEXT.setColor(Color.BLACK);
			oPT_TEXT.setTextAlign(Paint.Align.CENTER);

			//Determine the center of the canvas
			int iCanvasHeight = oCanvas.getHeight();
			int iCanvasWidth = oCanvas.getWidth();
			float fCanvasHeight_CENTER = ( ((float) iCanvasHeight) / 2.0f);
			float fCanvasWidth_CENTER = ( ((float) iCanvasWidth) / 2.0f);

			if (sPV.contentEquals("-")) {
				
				//Determine the bounding box for the text sSymbol.
				oPT_TEXT.getTextBounds("NO DATA AVAILABLE", 0, "NO DATA AVAILABLE".length(), oSymbolRect);
		
				//Place the sSymbol on the canvas.
				oCanvas.drawText("NO DATA AVAILABLE", fCanvasWidth_CENTER, fCanvasHeight_CENTER + (oSymbolRect.height())/2, oPT_TEXT);

				//Place a line around the perimeter of the bitmap
				oCanvas.drawRoundRect(new RectF(0f,0f,(float) piWidth,(float) piHeight), 20.0f, 20.0f, oPT_LINE);

			} else {
				
				//Place a line around the perimeter of the bitmap
				oCanvas.drawRoundRect(new RectF(0f,0f,(float) piWidth,(float) piHeight), 20.0f, 20.0f, oPT_LINE);
				
				//Determine the bounding box for the text sSymbol.
				oPT_TEXT.getTextBounds(sSymbol, 0, sSymbol.length(), oSymbolRect);
		
				//Place the sSymbol on the canvas.
				oCanvas.drawText(sSymbol, fCanvasWidth_CENTER, fCanvasHeight_CENTER + (oSymbolRect.height())/2, oPT_TEXT);
		
				//Parse sPV in to an array.
				String[] aPV = sPV.split((";"));
				
				//Determine the number of entries in aPV
				int iNbrPV = aPV.length;
				
				//Determine the number of degrees we will place each entry in aPV on the SurfaceView.
				float fDegrees = 360f / (float)iNbrPV;
				
				//Determine the width of the text WWWWW.
				Rect oWWWWWRect = new Rect();
				oPT_TEXT.getTextBounds("WWWWW", 0, "WWWWW".length(), oWWWWWRect);
				float fWWWWWWidth = (float) (Math.abs(oWWWWWRect.width()));
				
				//Compute the x-coordinate from the right side based on WWWWW (assumes the translate() has occurred!)
				float fXCoord = fCanvasWidth_CENTER - fWWWWWWidth;
				
				//Draw each entry in sPV in turn on the surface starting at zero degrees and working clockwise fDegrees.
				oCanvas.save();
				oCanvas.translate(fCanvasWidth_CENTER, fCanvasHeight_CENTER);
				//Place a circle around the sSymbol
				oCanvas.drawCircle(0, 0, fWWWWWWidth/2f, oPT_LINE);
				float fThisX;
				float fThisY;
				for(int i=0;i<iNbrPV;i++) {
		
					//Re-initialize variables
					fThisX=0;
					fThisY=0;
					oTextRect.setEmpty();
					
					//Get the symbol for this iteration
					String sThisSymbol = aPV[i];
					
					//Based on i, determine the degrees to place it at.
					float fThisDegrees = fDegrees * ( (float) i );
					float fThisRadians = (float) Math.toRadians(fThisDegrees);
					
					//Rotate the canvas
					fThisX = (float) (fXCoord * FloatMath.cos(fThisRadians));
					fThisY = (float) (fXCoord * FloatMath.sin(fThisRadians));
		
					//Get the bounds of the this iteration's symbol 
					oPT_TEXT.getTextBounds(sThisSymbol, 0, sThisSymbol.length(), oTextRect);
					
					//Draw a line from the origin (0,0) to the center of this iteration's symbol.
					//float fThisXOrigin = (float) ((float) oTextRect.width() * FloatMath.cos(fThisRadians));
					//float fThisYOrigin = (float) ((float) oTextRect.height() * FloatMath.sin(fThisRadians));
					float fThisXOrigin = (float) (fWWWWWWidth/2f * FloatMath.cos(fThisRadians));
					float fThisYOrigin = (float) (fWWWWWWidth/2f * FloatMath.sin(fThisRadians));
					float fThisXEnd = (float) (0.90f * fXCoord * FloatMath.cos(fThisRadians));
					float fThisYEnd = (float) (0.90f * fXCoord * FloatMath.sin(fThisRadians));
					
					//Draw a line from the sSymbol to this iteration's symbol.
					oCanvas.drawLine(fThisXOrigin, fThisYOrigin, fThisXEnd, fThisYEnd, oPT_LINE);
					
					//Draw a small filled circle at the (fThisXOrigin,fThisYOrigin)
					oCanvas.drawCircle(fThisXOrigin, fThisYOrigin, 4f, oPT_DOT);
					
					//Place the sSymbol on the canvas.
					if (fThisDegrees == 0f) {
						oPT_TEXT.setTextAlign(Paint.Align.LEFT);
						oCanvas.drawText(sThisSymbol, fThisX, fThisY + (oTextRect.height()/2), oPT_TEXT);
					} else if (fThisDegrees == 180f ) {
						oPT_TEXT.setTextAlign(Paint.Align.RIGHT);
						oCanvas.drawText(sThisSymbol, fThisX, fThisY + (oTextRect.height()/2), oPT_TEXT);
					} else if (fThisDegrees > 0f && fThisDegrees < 180f ) {
						oPT_TEXT.setTextAlign(Paint.Align.CENTER);
						oCanvas.drawText(sThisSymbol, fThisX, fThisY + (oTextRect.height()/2), oPT_TEXT);
					} else if (fThisDegrees > 180f && fThisDegrees < 360f ) {
						oPT_TEXT.setTextAlign(Paint.Align.CENTER);
						oCanvas.drawText(sThisSymbol, fThisX, fThisY + (oTextRect.height()/2), oPT_TEXT);
					} else {
						oPT_TEXT.setTextAlign(Paint.Align.CENTER);
						oCanvas.drawText(sThisSymbol, fThisX, fThisY, oPT_TEXT);					
					}
					
				}
				oCanvas.restore();

			} //end else 
			
			//Set the bitmap, oBM, as the image of imageView1.
			oAVIV.setImageBitmap(oBM);

		}
	}

}
