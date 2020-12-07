package com.example.androidalarmmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.androidalarmmanager.DatabaseHelper.TABLE2;

public class AlarmManagerActivity extends Activity {

    int den;
    SharedPreferences sPref;
    SimpleCursorAdapter scAdapter1;
    ListView lvData2;
    private AlarmManagerBroadcastReceiver alarm;
    SQLiteDatabase db;
    Cursor updatedCursor;
    DatabaseHelper databaseHelper;
    boolean iteM;
    int dencal,mes,god;
    BroadcastReceiver updateUIReciver;


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarm = new AlarmManagerBroadcastReceiver();
        Context context = this.getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        // создаем базу данных
        databaseHelper.create_db();
        db = databaseHelper.open();
        lvData2 = (ListView) findViewById(R.id.lv);

        startRepeatingTimer();
calendar("2020.11.11");
        Log.d("cdData", String.valueOf(god +"." + mes +"."+ dencal));
        IntentFilter filter = new IntentFilter();
        filter.addAction("service.to.activity.transfer");
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                if (intent != null)

                updateListView(context);
            }
        };
        registerReceiver(updateUIReciver, filter);



        updatedCursor = db.query(TABLE2, null, null, null, null, null, null);

        String[] from1 = new String[] {DatabaseHelper.COLUMN_YEAR2, DatabaseHelper.COLUMN_NAME2};
        int[] to1 = new int[] { R.id.tvData,R.id.tvName};

        scAdapter1 = new Adapter(this,R.layout.item,updatedCursor,from1,to1,2);
        ListView lvData2 = (ListView) findViewById(R.id.lv);
        lvData2.setItemsCanFocus(true);
        final View item = getLayoutInflater().inflate(R.layout.item,null);
        Button yourButton = (Button) item.findViewById(R.id.button);


        lvData2.setAdapter(scAdapter1);
        // добавляем контекстное меню к списку
        registerForContextMenu(lvData2);
    }
    protected void updateListView(Context context){


       databaseHelper = new DatabaseHelper(context);
        // создаем базу данных
        databaseHelper.create_db();
        db = databaseHelper.open();

        updatedCursor = db.query(TABLE2, null, null, null, null, null, null);
        //updatedCursor.moveToFirst();
        Log.d("updatedCursor", String.valueOf(updatedCursor));
        scAdapter1.changeCursor(updatedCursor);



    }
    public String calendar (String date1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);

        dencal = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH) ;
        god = calendar.get(Calendar.YEAR);
        //time = calendar.getTimeInMillis();
       // String date1 = god + "." + mes + "." + dencal;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
        Date datenorm;
        String data = null;
        try {
            datenorm = dateFormat.parse(date1);
            data = dateFormat.format(datenorm.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("summD1", data);

        return data;
    }
   @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public void Dobavit (View view){
    AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog);
    final View custom_dialogalert = getLayoutInflater().inflate(R.layout.custom_dialogalert,null);
    final TextView cdData = (TextView) custom_dialogalert.findViewById(R.id.cdData);
    final TextView cdName = (TextView) custom_dialogalert.findViewById(R.id.cdName);
    builder.setView(custom_dialogalert);
    cdData.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AlarmManagerActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date1 = year + "." + (month + 1) + "." + dayOfMonth;

                    cdData.setText(calendar(date1));

                }

            }

                    , god, mes, dencal);
            datePickerDialog.show();
        }
    });
builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
dialog.cancel();
                }
            });
builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {




    @Override
    public void onClick(DialogInterface dialog, int which) {
       Cursor userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE2 + " where " +
                DatabaseHelper.COLUMN_YEAR2 + "=?", new String[]{cdData.getText().toString()});
        userCursor.moveToFirst();
        // Cursor cursor = db.query(TABLE2, null, null, null, null, null, null);
        // Log.d("updatedCursor", String.valueOf(cursor + "+1"));
        // AlarmManagerActivity AM = new AlarmManagerActivity();
        //AM.updateListView(context);

        if( userCursor != null && userCursor.moveToFirst() ) {
           String date = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR2));

            if (date.equals(cdData.getText().toString())) {
                Toast.makeText(getApplicationContext(), "На эту дату уже есть событие", Toast.LENGTH_SHORT).show();
            }
        }else {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COLUMN_NAME2, cdName.getText().toString());
            cv.put(DatabaseHelper.COLUMN_YEAR2, cdData.getText().toString());
            db.insert(TABLE2, null, cv);
            Cursor updatedCursor = db.query(TABLE2, null, null, null, null, null, null);
            // Update the ListAdapter.

            scAdapter1.changeCursor(updatedCursor);
            //updateListView(getBaseContext());
            startRepeatingTimer();
        }
    }
});


    final AlertDialog dialog = builder.create();

    dialog.show();
}

    @Override
    protected void onStart() {
        super.onStart();
       // Toast.makeText(this, "onStart" , Toast.LENGTH_SHORT).show();
        Log.d("probl", "onStart");
    }

    public void startRepeatingTimer() {
        Context context = this.getApplicationContext();


        if(alarm != null){
            alarm.SetAlarm(context);
alarm.names(context);

        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelRepeatingTimer(){
        Context context = this.getApplicationContext();
        if(alarm != null){
            alarm.CancelAlarm(context);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void onetimeTimer(View view){
        Context context = this.getApplicationContext();
        if(alarm != null){
            alarm.setOnetimeTimer(context);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_widget_alarm_manager, menu);

        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                AlertDialog.Builder alert = new AlertDialog.Builder(this,android.R.style.Theme_Holo_Panel);

                alert.setMessage("Настройки");

                final View custom_dialogalert = getLayoutInflater().inflate(R.layout.custom_menu,null);
                alert.setView(custom_dialogalert);

                  final CheckBox Swi = (CheckBox) custom_dialogalert.findViewById(R.id.ChBox);
Log.d("loadText", String.valueOf(loadText())+ "-1");
Swi.setChecked(loadText());
Swi.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(Swi.isChecked() && loadText()==false)
        {

            //logic is it is checked
            Swi.setChecked(true);
            //iteM=false;
            saveText(true);
            Log.d("loadText", String.valueOf(loadText())+ "-сохр - T");
        }
        else
        {
            //logic is it is not checked
            Swi.setChecked(false);
            //iteM=true;
            saveText(false);
            Log.d("loadText", String.valueOf(loadText())+ "-сохр - F");
        }
    }
});

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        startRepeatingTimer();
                    }
                });
                alert.show();
                break;

            case R.id.action_reference:
                AlertDialog.Builder alertreference = new AlertDialog.Builder(this,android.R.style.Theme_Holo_Panel);

                alertreference.setMessage("Справка");

                final View custom_reference = getLayoutInflater().inflate(R.layout.custom_reference,null);
                alertreference.setView(custom_reference);

                alertreference.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alertreference.show();
                break;
            case R.id.action_exit:

saveText(false);
                startRepeatingTimer();
                cancelRepeatingTimer();
                this.finish();
                break;


        }
        return false;
    }
    void saveText(boolean iteM) {
        sPref = PreferenceManager.getDefaultSharedPreferences(this);;
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean("firstrun", iteM);
        ed.apply();
        Toast.makeText(this, "Text saved метод" + "-"+iteM, Toast.LENGTH_SHORT).show();

    }

     boolean loadText() {
        sPref = PreferenceManager.getDefaultSharedPreferences(this);

        iteM = sPref.getBoolean("firstrun", false);
        Toast.makeText(this, "Text loaded мотод" +"-"+iteM, Toast.LENGTH_SHORT).show();
        return iteM;
    }


}

