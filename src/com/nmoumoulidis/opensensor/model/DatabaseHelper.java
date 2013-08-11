package com.nmoumoulidis.opensensor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.nmoumoulidis.opensensor.model.processing.DateManager;
import com.nmoumoulidis.opensensor.view.SensorStationActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite database management class (extends {@link SQLiteOpenHelper}), used for storing
 * persistently data on the phone, that were retrieved from a Wi-Fi connected OpenSensor Station.
 * @author Nikos Moumoulidis
 *
 */
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
    public static final String FUNC_AVG_VALUE = "avg("+KEY_DATA_VALUE+")";
    public static final String FUNC_MIN_VALUE = "min("+KEY_DATA_VALUE+")";
    public static final String FUNC_MAX_VALUE = "max("+KEY_DATA_VALUE+")";
    
    private SensorStationActivity conSensAct;
    private SQLiteDatabase privateDB;
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if(context.getClass() == SensorStationActivity.class) {;
        	conSensAct = (SensorStationActivity) context;
        }
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

    @Override
    public void close() {
    	super.close();
        if(privateDB != null) {
        	privateDB.close();
        	privateDB = null;
        }
    }
    
    public ArrayList<String> getAllStoredSensorTypes() {
    	ArrayList<String> sensorTypesList = new ArrayList<String>();
    	privateDB = this.getReadableDatabase();
    	Cursor cursor = privateDB.rawQuery("SELECT DISTINCT "+
    								KEY_SENSOR_TYPE+
    								" FROM "+
    								TABLE_SENSOR_DATA+";"
    								, null);
    	if (cursor.moveToFirst()) {
    		do {
    			sensorTypesList.add(cursor.getString(0));
    		} while (cursor.moveToNext());
    	}
    	conSensAct.addUsedCursor(cursor);
    	return sensorTypesList;
    }

    /** 
     * Main INSERT query.
     * @param data
     */
    public void insertBatchData(ArrayList<HashMap<String,String>> data) {
    	data = DateManager.transformDateBeforeInsert(data);
    	privateDB = this.getWritableDatabase();
    	Iterator it;
    	HashMap.Entry pairs;
    	for(int i=0 ; i<data.size() ; i++) {
    		// Insert all data elements to the database.
		    it = data.get(i).entrySet().iterator();
		    while (it.hasNext()) {
		    	pairs = (HashMap.Entry)it.next();
		    	if(!pairs.getKey().equals("datetime") && !pairs.getKey().equals("location")) {
		    		privateDB.execSQL("INSERT INTO "+ TABLE_SENSOR_DATA
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
    	privateDB.close(); // explicitly close it...
    }

    /**
     * Main SELECT query.
     * @param sensor
     * @param fromDate
     * @param toDate
     * @return
     */
    public Cursor getDetailedQueryCursor(String sensor, String fromDate, String toDate) {
    	privateDB = this.getReadableDatabase();
    	Cursor cursor = privateDB.rawQuery("SELECT "+
    								KEY_ID+", " +
    								KEY_DATE+", " +
    								KEY_LOCATION+", " +
    								FUNC_AVG_VALUE+", " +
    								FUNC_MIN_VALUE+", " +
    								FUNC_MAX_VALUE+", " +
    								KEY_SENSOR_TYPE+" " +
    								"FROM " +TABLE_SENSOR_DATA+" "+
    								"WHERE "+KEY_SENSOR_TYPE+" = '"+ sensor +"' "+
    								"AND ("+KEY_DATE+" " +
    										"BETWEEN Date('"+fromDate+ "') AND Date('"+toDate+"')) "+
    								"GROUP BY "+KEY_DATE
    								, null);
		if (cursor.moveToFirst()) {
			return cursor;
		}
		else {
			return null;
		}
    }

    public void deleteAllBatchData() {
    	privateDB = this.getReadableDatabase();
    	privateDB.delete(TABLE_SENSOR_DATA, null, null);
    }

    public long getDataCount() {
    	privateDB = this.getReadableDatabase();
    	return DatabaseUtils.queryNumEntries(privateDB, TABLE_SENSOR_DATA);
    }
}
