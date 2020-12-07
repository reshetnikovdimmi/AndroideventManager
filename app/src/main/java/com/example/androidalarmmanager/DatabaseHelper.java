package com.example.androidalarmmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "mydb3";
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "AlarmManager"; // название таблицы в бд
    static final String TABLE2 = "AM2";
    // названия столбцов
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_YEAR = "date_1";

    static final String COLUMN_ID_2 = "_id";
   static final String COLUMN_NAME2 = "Name1";
    static final String COLUMN_YEAR2 = "Date1";

   /* private static final String DB_CREATE = "create table " + TABLE2 + "(" +
            COLUMN_ID_2 + " integer primary key autoincrement, " +

            COLUMN_NAME2 + " txt," +

            COLUMN_YEAR2 + " integer);";*/

    private Context myContext;
    //private DBHelper mDBHelper;
   // private SQLiteDatabase mDB;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext=context;
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;
    }
 /*   private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE2);

                ContentValues cv = new ContentValues();

                cv.put(COLUMN_YEAR2,2222);
                cv.put(COLUMN_NAME2,"kjbbjbjk");

                mDB.insert(TABLE2, null, cv);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
    }

    void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        }
        catch(IOException ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

  /*  public void open2() {
        mDBHelper = new DBHelper(myContext, DB_NAME, null, SCHEMA);
        mDB = mDBHelper.getWritableDatabase();
    }*/
}
