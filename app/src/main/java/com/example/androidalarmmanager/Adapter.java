package com.example.androidalarmmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import static com.example.androidalarmmanager.DatabaseHelper.TABLE2;

public class Adapter extends SimpleCursorAdapter {
    Context ctx;
    SQLiteDatabase db;
    Cursor updatedCursor;
    DatabaseHelper databaseHelper;
    long id;
    public Adapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int i) {
        super(context, layout, cursor, from, to);
        ctx = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        id=getItemId(position);
        view.setTag(id);
        Button btmMenu=(Button) view.findViewById(R.id.button);
        btmMenu.setOnClickListener(ClickBtmMenu);
        btmMenu.setTag(id);
        return view;
    }
    View.OnClickListener ClickBtmMenu = new View.OnClickListener() {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            databaseHelper = new DatabaseHelper(ctx);
            databaseHelper.create_db();
            db = databaseHelper.open();
            db.delete(DatabaseHelper.TABLE2, "_id = ?", new String[]{String.valueOf(id)});
            AlarmManagerBroadcastReceiver ABR = new AlarmManagerBroadcastReceiver();
            ABR.names(ctx);
Changed();
        }
    };
public void Changed (){
    onContentChanged();
}
}