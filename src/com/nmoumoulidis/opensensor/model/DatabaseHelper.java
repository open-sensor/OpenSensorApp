package com.nmoumoulidis.opensensor.model;

import java.util.ArrayList;
import java.util.Date;
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
    private static final String TABLE_SENSOR_TYPES = "sensor_types";

    private static final String KEY_ID = "_id";
    
    private static final String KEY_SENSOR_TYPE = "sensor_type";
    
    private static final String KEY_DATE = "date";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_SENSOR_TYPE_ID = "sensor_type_id";
    private static final String KEY_DATA_VALUE = "data_value";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SENSOR_TYPES_TABLE = "CREATE TABLE " + TABLE_SENSOR_TYPES 
        		+ "(" 
        		+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
        		+ KEY_SENSOR_TYPE + " TEXT NOT NULL"
        		+ ");";
        db.execSQL(CREATE_SENSOR_TYPES_TABLE);

        String CREATE_SENSOR_DATA_TABLE = "CREATE TABLE " + TABLE_SENSOR_DATA
        		+ "(" 
        		+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
        		+ KEY_DATE + " TEXT NOT NULL,"
                + KEY_LOCATION + " TEXT NOT NULL,"
                + KEY_SENSOR_TYPE_ID + " TEXT NOT NULL REFERENCES "
                						+TABLE_SENSOR_TYPES+"(KEY_ID),"
                + KEY_DATA_VALUE + " REAL NOT NULL"
        		+ ");";
        db.execSQL(CREATE_SENSOR_DATA_TABLE);
        populateSensorTypesTable(db);
    }

    private void populateSensorTypesTable(SQLiteDatabase db) {
    	for(int i=0 ; i<SensorDictionary.validSensors.length ; i++) {
    		String INSERT_SENSOR_TYPES = "INSERT INTO "+ TABLE_SENSOR_TYPES
        			+" ("+KEY_SENSOR_TYPE+") VALUES" 
        			+" ('"+SensorDictionary.validSensors[i]+"');";
    		db.execSQL(INSERT_SENSOR_TYPES);
		}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR_TYPES);
        onCreate(db);
        System.out.println("SQLITE HELPER -> ON UPGRADE WAS RUN...");
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public ArrayList<String> getSensorTypesStored() {
    	ArrayList<String> sensorTypesList = new ArrayList<String>();
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(TABLE_SENSOR_TYPES, new String[] { KEY_SENSOR_TYPE }
 							, null, null, null, null, null, null);
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
		    				+ KEY_SENSOR_TYPE_ID +", " 
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

    public void printAllBatchData() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(TABLE_SENSOR_DATA, new String[] {KEY_DATE, KEY_LOCATION,
    													KEY_SENSOR_TYPE_ID, KEY_DATA_VALUE}
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
    													KEY_SENSOR_TYPE_ID, KEY_DATA_VALUE}
			, KEY_DATE+"='"+DateManager.getToday()+"'", null, null, null, null, null);
    	
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
    
    public void getLastWeeksData() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(TABLE_SENSOR_DATA, new String[] {KEY_DATE, KEY_LOCATION,
    													KEY_SENSOR_TYPE_ID, KEY_DATA_VALUE}
			, KEY_DATE+"='"+DateManager.getToday()+"'", null, null, null, null, null);
    	
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
    
    public void deleteAllBatchData() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.delete(TABLE_SENSOR_DATA, null, null);
    }
    
    public long getDataCount() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	return DatabaseUtils.queryNumEntries(db, TABLE_SENSOR_DATA);
    }

    /*
    // Adding new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
 
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }
     
    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());
 
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }
 
    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }
 
 
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 */
}
