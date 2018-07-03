package com.example.lenovo.todolistactivitybased;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    ListView listView;
    ArrayList<Task> tasks;
    ToDoAdapter adapter;

    public static final String NAME_KEY = "name";
    public static final String DESCRIPTION_KEY = "description";
    public static final String CLICKED_POSN_KEY = "position";

    public static final int DISPLAY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        tasks = new ArrayList<>();


            //DATABASE (Readable)
        ToDoOpenHelper openHelper = new ToDoOpenHelper(getApplicationContext());
//        ToDoOpenHelper openHelper = new ToDoOpenHelper(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();

        Cursor cursor = database.query("todo", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("table"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            long id  = cursor.getLong(cursor.getColumnIndex("id"));
            Task task = new Task(name, description);
            task.setId(id);
            tasks.add(task);
        }
        cursor.close();
        /////DATABASE ka onCreate vala kaam done


        adapter = new ToDoAdapter(this, tasks);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(this);

        listView.setOnItemClickListener(this);
    }


    ////////menu options////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add){

            //open dialog box containing editTexts
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Add a task");
            builder.setCancelable(false);

            ////// in the foll. 5 lines, we make our custom view (a part of alertDialog)
            ////// for typing a to-do
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.add_view,null);
            final EditText titleEditText = view.findViewById(R.id.titleEditText);
            final EditText descriptionEditText = view.findViewById(R.id.descriptionEditText);
            builder.setView(view);


            // now set buttons
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Added a new To-Do", Toast.LENGTH_LONG).show();
                    Task task = new Task(titleEditText.getText().toString(), descriptionEditText.getText().toString());

                    //DATABASE (Writable)
                    ToDoOpenHelper openHelper = new ToDoOpenHelper(MainActivity.this);
                    SQLiteDatabase db = openHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("table", task.getTitle());
                    contentValues.put("description", task.getDesciptn());

                    long id = db.insert("todo", null, contentValues);
                    {
                        task.setId(id);

                        tasks.add(task);
                        adapter.notifyDataSetChanged();
                    }
                }
            });


            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);

    }
    /////////menu options close///////////



    //handle deletions
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {

        Task task = tasks.get(i);

        final int position = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setCancelable(false); // ie, u cant click smewhre on the screen to remove this alert
        builder.setMessage("Are you sure you want to delete " + task.getTitle() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Deleted!", Toast.LENGTH_LONG).show();
                tasks.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Task task = tasks.get(i);

            Bundle bundle = new Bundle();
            bundle.putString(NAME_KEY, task.getTitle());
            bundle.putString(DESCRIPTION_KEY, task.getDesciptn());
            bundle.putInt(CLICKED_POSN_KEY, i);

            Intent intent = new Intent(this, DisplayActivity.class);
            intent.putExtras(bundle);

            startActivityForResult(intent, DISPLAY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle;

        if (requestCode == DISPLAY_REQUEST_CODE && resultCode == DisplayActivity.DISPLAY_RESULT_CODE){

            bundle = data.getExtras();

            String name = bundle.getString(MainActivity.NAME_KEY);
            String description = bundle.getString(MainActivity.DESCRIPTION_KEY);
            int position = bundle.getInt(CLICKED_POSN_KEY);


                //DATABASE (Writable)
//            ToDoOpenHelper openHelper = new ToDoOpenHelper(MainActivity.this, ToDoOpenHelper.DB_NAME, null, 1);
//            SQLiteDatabase db = openHelper.getWritableDatabase();
//
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("table", name);
//            contentValues.put("description", description);
//
//            long id = db.insert("todo", null, contentValues);


            Task task = new Task(name, description);

//            if (id > -1) {
                tasks.set(position, task); // position bundle se hi pass karvani hogi
                // thru all activities
                adapter.notifyDataSetChanged();
//            }

//                //DATABASE (Writable)
//            ToDoOpenHelper openHelper = new ToDoOpenHelper(MainActivity.this, ToDoOpenHelper.DB_NAME, null, 1);
//            SQLiteDatabase db = openHelper.getWritableDatabase();
//
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("table", task.getTitle());
//            contentValues.put("description", task.getDesciptn());
//
//            long id = db.insert("todo", null, contentValues);
//
//            if (id > -1){
//                tasks.set(position, task);
//                adapter.notifyDataSetChanged();
//            }




        }
    }
}