package com.nmoumoulidis.opensensor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper 
{	
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sensorReadingsManager";
    private static final String TABLE_SENSOR_DATA = "sensor_data";

    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_SENSOR_TYPE = "sensor_type_id";
    public static final String KEY_DATA_VALUE = "data_value";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SENSOR_DATA_TABLE = "CREATE TABLE " + TABLE_SENSOR_DATA
        		+ "(" 
        		+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
        		+ KEY_DATE + " TEXT NOT NULL,"
                + KEY_LOCATION + " TEXT NOT NULL,"
                + KEY_SENSOR_TYPE + " TEXT NOT NULL,"
                + KEY_DATA_VALUE + " REAL NOT NULL"
        		+ ");";
        db.execSQL(CREATE_SENSOR_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR_DATA);
        onCreate(db);
        System.out.println("SQLITE HELPER -> ON UPGRADE WAS RUN...");
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
  
    public ArrayList<String> getAllStoredSensorTypes() {
    	ArrayList<String> sensorTypesList = new ArrayList<String>();
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT DISTINCT "+
    								KEY_SENSOR_TYPE+
    								" FROM "+
    								TABLE_SENSOR_DATA+";"
    								, null);
    	if (cursor.moveToFirst()) {
    		do {
    			sensorTypesList.add(cursor.getString(0));
    		} while (cursor.moveToNext());
    	}
    	cursor.close();
    	return sensorTypesList;
    }

    public void insertBatchData(ArrayList<HashMap<String,String>> data) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	for(int i=0 ; i<data.size() ; i++) {
    		// Transform datetime into date.
    		String date = data.get(i).get("datetime").substring(0, 10);
    		data.get(i).put("datetime", date);
    		
    		// Insert all data elements to the database.
		    Iterator it = data.get(i).entrySet().iterator();
		    while (it.hasNext()) {
		    	HashMap.Entry pairs = (HashMap.Entry)it.next();
		    	if(!pairs.getKey().equals("datetime") && !pairs.getKey().equals("location")) {
		    		db.execSQL("INSERT INTO "+ TABLE_SENSOR_DATA
		    				+" ("
		    				+ KEY_DATE +", "
		    				+ KEY_LOCATION +", "
		    				+ KEY_SENSOR_TYPE +", " 
		    				+ KEY_DATA_VALUE
		    				+") VALUES ("
		    				+"'"+ data.get(i).get("datetime") +"', "
		    				+"'"+ data.get(i).get("location") +"', "
		    				+"'"+ pairs.getKey() +"', "
		    				+ pairs.getValue() 
		    				+");"
		    				);
		    	}
		    }
		}
    }

    public Cursor getDetailedQueryCursor(String sensor, String fromDate, String toDate) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	System.out.println("fromDate: "+fromDate+", doDate: "+toDate+", sensor: "+sensor);
    	Cursor cursor = db.query(TABLE_SENSOR_DATA
    			,new String[] {KEY_ID, KEY_DATE, KEY_LOCATION, KEY_SENSOR_TYPE, KEY_DATA_VALUE}
    			,"("+KEY_DATE+" BETWEEN ? AND ? )"+" AND "+KEY_SENSOR_TYPE+"= ?"
    			,new String[] { "24-07-2013" , "24-07-2013", sensor }
    			, null, null, null, null);
		if (cursor.moveToFirst()) {
			return cursor;
		}
		else {
			return null;
		}
    }

    public void printAllBatchData() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(TABLE_SENSOR_DATA, new String[] {KEY_DATE, KEY_LOCATION,
    													KEY_SENSOR_TYPE, KEY_DATA_VALUE}
			, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String dataLine = "";
				dataLine += cursor.getString(0);
				dataLine += cursor.getString(1);
				dataLine += cursor.getString(2);
				dataLine += cursor.getDouble(3);
				System.out.println(dataLine + "\n");
			} while (cursor.moveToNext());
		}
		else {
			System.out.println("Cursor is empty... No batch data...");
		}
		cursor.close();
    }

    public void getTodaysData() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(TABLE_SENSOR_DATA, new String[] {KEY_DATE, KEY_LOCATION,
    													KEY_SENSOR_TYPE, KEY_DATA_VALUE}
			, KEY_DATE+"='"+DateManager.getTodayString()+"'", null, null, null, null, null);
    	
		if (cursor.moveToFirst()) {
			do {
				String dataLine = "";
				dataLine += cursor.getString(0);
				dataLine += cursor.getString(1);
				dataLine += cursor.getString(2);
				dataLine += cursor.getDouble(3);
				System.out.println(dataLine + "\n");
			} while (cursor.moveToNext());
		}
		else {
			System.out.println("No data for today...");
		}
		cursor.close();
    }

    public void getLastWeeksData() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(TABLE_SENSOR_DATA, new String[] {KEY_DATE, KEY_LOCATION,
    													KEY_SENSOR_TYPE, KEY_DATA_VALUE}
			, KEY_DATE+">='"+DateManager.getSevenDaysBeforeString()+"'", null, null, null, null, null);
    	
		if (cursor.moveToFirst()) {
			do {
				String dataLine = "";
				dataLine += cursor.getString(0);
				dataLine += cursor.getString(1);
				dataLine += cursor.getString(2);
				dataLine += cursor.getDouble(3);
				System.out.println(dataLine + "\n");
			} while (cursor.moveToNext());
		}
		else {
			System.out.println("No data for this week...");
		}
		cursor.close();
    }

    public void getLastMonthsData() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(TABLE_SENSOR_DATA, new String[] {KEY_DATE, KEY_LOCATION,
    													KEY_SENSOR_TYPE, KEY_DATA_VALUE}
			, KEY_DATE+">='"+DateManager.getAMonthBeforeString()+"'", null, null, null, null, null);
    	
		if (cursor.moveToFirst()) {
			do {
				String dataLine = "";
				dataLine += cursor.getString(0);
				dataLine += cursor.getString(1);
				dataLine += cursor.getString(2);
				dataLine += cursor.getDouble(3);
				System.out.println(dataLine + "\n");
			} while (cursor.moveToNext());
		}
		else {
			System.out.println("No data for this week...");
		}
		cursor.close();
    }

    public void deleteAllBatchData() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.delete(TABLE_SENSOR_DATA, null, null);
    }
    
    public long getDataCount() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	return DatabaseUtils.queryNumEntries(db, TABLE_SENSOR_DATA);
    }
}
