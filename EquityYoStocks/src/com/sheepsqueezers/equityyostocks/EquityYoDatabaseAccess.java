package com.sheepsqueezers.equityyostocks;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class EquityYoDatabaseAccess extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "EQUITY_DATABASE";
	private static final int DATABASE_VERSION = 1;
	
	public EquityYoDatabaseAccess(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		//Force any new database to overwrite an existing database.
		setForcedUpgradeVersion(DATABASE_VERSION);
		
		//Close the database once it has been installed.
		//I will open the database again later.
		close();
	}

}