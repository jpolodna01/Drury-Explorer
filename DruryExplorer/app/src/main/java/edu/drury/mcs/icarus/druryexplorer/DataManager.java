package edu.drury.mcs.icarus.druryexplorer;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.location.Location;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daniel Chick && Josef Polodna on 10/26/2014.
 *
 * This class is used to assist in the creation and version management of the database. Much of the original functionality
 * of creating a database was overrode so that we could simply use a pre-made database created outside of android studio.
 *
 */
public class DataManager extends SQLiteOpenHelper
{

    //////////////////////////////Variables///////////////////////////////

    private static final int DATABASE_VERSION = 1;
	
	// path to db
    //josef edited file path
	private static String DB_PATH = "/data/data/edu.drury.mcs.icarus.druryexplorer/databases";

    private static final String DATABASE_NAME = "hallManager.sqlite";

    private String path = DB_PATH + DATABASE_NAME;

    private final Context context;
    private SQLiteDatabase db;

    // pulling from the db, so not necessary
	private String TABLE_HALL = "Hall",
       KEY_ID ="id",
       KEY_NAME = "name",
       KEY_YEAR = "year",
       KEY_HISTORY = "history";

    private String TABLE_DEPT = "Department",
        KEY_ID2 = "Dept_ID",
        KEY_NAME2 = "Name",
        KEY_DESCRIPTION = "Description",
        KEY_LOCATION = "Location";

    /////////////////////////////Constructors//////////////////////////////////

    /**
     * Constructor for class with context variable
     *
     * @param context
     */
    public DataManager(Context context)
    {
        // Calls parent class
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

		// update: set the context for the db
		this.context=context;
    }

    /////////////////////////Methods/////////////////////////////

    /**
     * Create a database and override it with our created database
     *
     * @throws IOException
     */
	 public void create() throws IOException{

		boolean dbExist = checkDataBase();

		if(dbExist){
		  //do nothing - database already exist
		}
        else{

		  //By calling this method and empty database will be created into the default system path
		  //of your application so we are gonna be able to overwrite that database with our database.
			this.getWritableDatabase();

			try {

			copyDataBase();

		  } catch (IOException e) {

			throw new Error("Error copying database");

		  }
		}
	}
	
	//update: check database existance to prevent re-copying the data

    /**
     * Check to see if the database exists to prevent re-copying data into it
     *
     * @return = boolean, depends on existence of database
     */
	private boolean checkDataBase(){

        File dbFile = new File(DB_PATH + DATABASE_NAME);
        return dbFile.exists();
	  }

    /**
     * Copy the database assets to the new new system database
     *
     * @throws IOException
     */
	private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

	//update: Open the database

    /**
     * Open the database
     *
     * @return = boolean depending on if opened
     */
	public boolean open() {
		try {
		  //String myPath = DB_PATH + DATABASE_NAME; //using global var for now
		  db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
		  return true;
		} 
		catch(SQLException sqle) {
		  db = null;
		  return false;
		}
	}

    /**
     * Close the database
     */
	@Override
	public synchronized void close(){
		if(db != null) {
			db.close();
		}
		
		super.close();
	}
	
	/*
		Given that we are replacing the functionality of the standard database set-
		up, we have to override the base functions provided by the SQLiteHelper since we are
		merely drawing data from an already created database.
	*/

    /**
     * Appropriately override the onCreate function to do nothing on create
     *
     * @param db
     */
	@Override
	public void onCreate(SQLiteDatabase db){}

    /**
     * This method details what to do when upgrading the database, which is nothing as it was above
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}


    /**
     * Collects the hall names to be projected onto the screen
     *
     * @return
     */
    public List<Halls> getHallList(){
        List<Halls> hall = null;

        try{

            String query = "SELECT Name FROM " + TABLE_HALL + " ORDER BY Name ASC";

            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            hall = new ArrayList<Halls>();
            if(cursor.moveToFirst())
            {
                do {

                    Halls hallObject = new Halls();
                    hallObject.setName(cursor.getString(0));

                    hall.add(hallObject);
                }
                while (cursor.moveToNext());
            }
        }
        catch(Exception e){
            //sql error
        }

        return hall;
    }

    /*public ArrayList<HallListActivity> getHallList2(){
        ArrayList<HallListActivity> hall = null;

        try{

            String query = "SELECT Name FROM " + TABLE_HALL + " ORDER BY Name ASC";

            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            hall = new ArrayList<HallListActivity>();
            if(cursor.moveToFirst())
            {
                do {

                    HallListActivity hallObject = new HallListActivity();
                    hallObject.setName(cursor.getString(0));

                    hall.add(hallObject);
                }
                while (cursor.moveToNext());
            }
        }
        catch(Exception e){
            //sql error
        }

        return hall;
    }*/

    /**
     * Collects the hall names to be projected onto the screen
     *
     * @return
     */
    public List<Departments> getDeptList(){
        List<Departments> department = null;

        try{

            String query = "SELECT Name FROM " + TABLE_DEPT + " ORDER BY Name ASC";

            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            department = new LinkedList<Departments>();
            if(cursor.moveToFirst())
            {
                do {

                    Departments deptObject = new Departments();
                    deptObject.setName(cursor.getString(0));

                    department.add(deptObject);
                }
                while (cursor.moveToNext());
            }
        }
        catch(Exception e){
            //sql error
        }

        return department;
    }

}