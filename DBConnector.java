package nozagleh.org.gluttony;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by arnarfreyr on 11.3.15.
 */
public class DBConnector extends SQLiteOpenHelper{

    //DB VERSION
    private static final int DATABASE_VERSION = 1;
    //DB NAME
    private static final String DATABASE_NAME = "gluttony_db";

    //DB TABLE NAME AND ELEMENTS
    private static final String TABLE_FOOD = "food";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TIME = "time";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_TYPE = "type";

    private static String KEY_DB_LOCATION;

    private static Context acontext;

    //Intilize database
    SQLiteDatabase db;

    //Constructor


    public DBConnector(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        KEY_DB_LOCATION = context.getDatabasePath(DATABASE_NAME).toString();
        acontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create tables in the database
        String CREATE_TABLE_FOOD = "CREATE TABLE " + TABLE_FOOD + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + KEY_COMMENT + " TEXT DEFAULT 'No comment added',"
                + KEY_TYPE + " INTEGER DEFAULT 0"
                + ");";

        db.execSQL(CREATE_TABLE_FOOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }

    public void addFood(Food food){
        //Establish DB connection
        db = this.getWritableDatabase();

        //Set the values of a food item
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, food.get_name());
        values.put(KEY_TIME, food.get_time());
        values.put(KEY_COMMENT, food.get_comment());
        values.put(KEY_TYPE, food.get_type());

        //insert into the database and close the connection
        db.insert(TABLE_FOOD, null, values);
        db.close();
    }

    public Food getFood(int id){
        //Establish DB connection
        db = this.getReadableDatabase();

        //Query the database
        Cursor cursor = db.query(TABLE_FOOD, new String[]{KEY_ID,KEY_NAME,KEY_TIME,KEY_COMMENT,KEY_TYPE}, KEY_ID + "=?", new String[] {String.valueOf(id)},null,null,null);

        //check if the cursor is null
        if (cursor != null){
            cursor.moveToFirst();
        }

        //add values to food
        Food food = new Food(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4));

        //return the food item
        return food;
    }

    public int getCount(){
        db = this.getWritableDatabase();

        final String countAll = "SELECT count(" + KEY_ID + ") FROM " + TABLE_FOOD;
        Cursor cursor = db.rawQuery(countAll,null);
        cursor.moveToFirst();
        int counter = cursor.getInt(0);

        return counter;
    }

    public ArrayList<String> getFoodName(String name){
        ArrayList<String> arrayFood = new ArrayList<String>();
        //Establish DB connection
        db = this.getWritableDatabase();

        if (!name.equals("")){
            //Query the database
            final String selectByName = "SELECT " + KEY_NAME + " FROM " + TABLE_FOOD + " WHERE " + KEY_NAME + " LIKE '" + name + "%' LIMIT 1";
            Cursor cursor = db.rawQuery(selectByName,null);
            //Cursor cursor = db.query(TABLE_FOOD, new String[]{KEY_ID,KEY_NAME,KEY_TIME,KEY_COMMENT}, KEY_NAME + "=?", new String[] {name},null,null,null);

            if (cursor.moveToFirst()){
                do {
                    Food food = new Food();
                    String temp = cursor.getString(0);

                    if (!temp.equals("")){
                        arrayFood.add(temp);
                    }


                }while (cursor.moveToNext());
            }
        }
        //return the food item
        return arrayFood;
    }

    public List<Food> getAllfood(){
        List<Food> foodList = new ArrayList<Food>();

        final String selectAllFood = "SELECT * FROM " + TABLE_FOOD + " ORDER BY " + KEY_TIME + " DESC";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAllFood,null);

        if (cursor.moveToFirst()){
            do {
                Food food = new Food();
                food.set_id(cursor.getInt(0));
                food.set_name(cursor.getString(1));
                food.set_time(cursor.getString(2));
                food.set_comment(cursor.getString(3));
                food.set_type(cursor.getInt(4));

                foodList.add(food);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return foodList;
    }

    public List<Food> getTodayfood(String date){
        List<Food> foodList = new ArrayList<Food>();
        final String selectAllFood = "SELECT * FROM " + TABLE_FOOD + " WHERE " + KEY_TIME + " LIKE '" + date + "%' ORDER BY " + KEY_TIME + " DESC";
        Log.d("Query",selectAllFood);
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAllFood,null);

        if (cursor.moveToFirst()){
            do {
                Food food = new Food();
                food.set_id(cursor.getInt(0));
                food.set_name(cursor.getString(1));
                food.set_time(cursor.getString(2));
                food.set_comment(cursor.getString(3));
                food.set_type(cursor.getInt(4));

                foodList.add(food);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return foodList;
    }

    public void updateFood(Food food){
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME,food.get_name());
        values.put(KEY_TIME,food.get_time());
        values.put(KEY_COMMENT,food.get_comment());
        values.put(KEY_TYPE,food.get_type());

        db.update(TABLE_FOOD, values, KEY_ID + " =?",new String[]{String.valueOf(food.get_id())});
    }

    public void deleteFood(Food food){
        db = this.getWritableDatabase();

        db.delete(TABLE_FOOD,KEY_ID + " = ?",new String[]{String.valueOf(food.get_id())});
    }

    public void deleteAll(){
        db = this.getWritableDatabase();

        db.delete(TABLE_FOOD,null,null);
    }

    public void dumpDatabase(){
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Toast.makeText(acontext, "External SD card not mounted", Toast.LENGTH_LONG).show();
        }else{
            try{
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();


                if (sd.canWrite()){
                    File currentDB = new File(KEY_DB_LOCATION);
                    File backupDB = new File(sd,DATABASE_NAME + "_backup");

                    if (currentDB.exists()){
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src,0,src.size());
                        src.close();
                        dst.close();
                    }
                }
            }catch (Exception e){

            }
        }
    }

    public void importDatabase(){
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Toast.makeText(acontext, "External SD card not mounted", Toast.LENGTH_LONG).show();
        }else{
            try{
                File sd = Environment.getExternalStorageDirectory();
                //File data = Environment.getDataDirectory();


                if (sd.canWrite()){
                    File currentDB = new File(KEY_DB_LOCATION);
                    File backupDB = new File(sd,DATABASE_NAME + "_backup");


                    if (backupDB.exists()){
                        FileChannel src = new FileInputStream(backupDB).getChannel();
                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                        dst.transferFrom(src,0,src.size());
                        src.close();
                        dst.close();
                    }
                }
            }catch (Exception e){
            }
        }
    }

}
