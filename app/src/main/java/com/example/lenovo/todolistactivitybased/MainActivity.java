package com.example.lenovo.todolistactivitybased;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    ListView listView;
    ArrayList<Task> tasks;
    ToDoAdapter adapter;

    public static final String NAME_KEY = "name";
    public static final String DESCRIPTION_KEY = "description";
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";
    public static final String CLICKED_POSN_KEY = "position";
    public static final String DB_ENTRY_ID = "db_id";

    public static final int DISPLAY_REQUEST_CODE = 1;

    Calendar calendar;
    String date;
    String time;

    int min, hr, dd, mm, yy; //for setting alarm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adding a new ToDo", Snackbar.LENGTH_SHORT);
//                        .setAction("Action", null).show();
                addToDo(view);
            }
        });


        listView = findViewById(R.id.listView);
        tasks = new ArrayList<>();


            //DATABASE (Readable)
        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
//        ToDoOpenHelper openHelper = new ToDoOpenHelper(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();

        Cursor cursor = database.query("todo", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            long id  = cursor.getLong(cursor.getColumnIndex("id"));
            Task task = new Task(name, description, date, time);
            task.setId(id);
            tasks.add(task);
        }
        cursor.close();
        /////DATABASE ka onCreate vala kaam done


        adapter = new ToDoAdapter(getApplicationContext(), tasks, new ToDoClickListener() {
            @Override
            public void rowBtnClicked(View view, int i) {

                handleDeletionWhenButtonClicked(view, i);

            }
        });

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(this);

        listView.setOnItemClickListener(this);
    }

    private void handleDeletionWhenButtonClicked(View view, int i) {
          final Task task = tasks.get(i);

                final int position = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setCancelable(false); // ie, u cant click smewhre on the screen to remove this alert
                builder.setMessage("Are you sure you want to delete " + task.getTitle() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
                        SQLiteDatabase db = openHelper.getWritableDatabase();

                        long id = task.getId();

                        String[] selectionArgs = {id + ""};

                        db.delete("todo", "id = ?", selectionArgs);

                        tasks.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Deleted " + task.getTitle() + "!", Toast.LENGTH_LONG).show();

                        //DELETE previously set ALARM
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                        PendingIntent pIntent  = PendingIntent.getBroadcast(getApplicationContext(), (int)id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pIntent);
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
    }

    ////////menu options////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //earlier had the code of adding task, datetime picker, alarm setter
        return super.onOptionsItemSelected(item);
    }
    /////////menu options close///////////

    public void addToDo(View v){
        //open dialog box containing editTexts
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add a task");
        builder.setCancelable(false);

        ////// in the foll. lines, we make our custom view (a part of alertDialog)
        ////// for typing a to-do
        final LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_view,null);
        final EditText titleEditText = view.findViewById(R.id.titleEditText);
        final EditText descriptionEditText = view.findViewById(R.id.descriptionEditText);
        Button dateButton = view.findViewById(R.id.dateBtn);
        Button timeButton = view.findViewById(R.id.timeBtn);
        final TextView dateTextView = view.findViewById(R.id.dateTextView);
        final TextView timeTextView = view.findViewById(R.id.timeTextView);
        builder.setView(view);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    date = day + "/" + month + "/" + year;
                                    dateTextView.setText(date);

                                    dd = day;
                                    mm = month;
                                    yy = year;

                                }
                            }, year, month, dayOfMonth);
                    datePickerDialog.show();
                }
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (minute < 10)  time = hourOfDay + ":0" + minute;
                                else  time = hourOfDay + ":" + minute;

                                hr = hourOfDay;
                                min = minute;

                                timeTextView.setText(time);
                            }
                        }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });

        // now set buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Added a new To-Do", Toast.LENGTH_SHORT).show();
                Task task = new Task(titleEditText.getText().toString(), descriptionEditText.getText().toString(), date, time);

                //DATABASE (Writable)
                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = openHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put("title", task.getTitle());
                contentValues.put("description", task.getDescription());
                contentValues.put("date", task.getDate());
                contentValues.put("time", task.getTime());

                long id = db.insert("todo", null, contentValues);

                {
                    task.setId(id);

                    tasks.add(task);
                    adapter.notifyDataSetChanged();

                    ////SET ALARM NOW
                    setAlarm(task, id);
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


    public void setAlarm(Task task, long id){
        //ALARM!
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("todoTitle", task.getTitle());
        PendingIntent pIntent  = PendingIntent.getBroadcast(getApplicationContext(), (int)id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        calendar = Calendar.getInstance();
        calendar.set(yy, mm, dd, hr, min, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);

    }



    //handle deletions (now handled via button, using delegation)
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {

        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Task task = tasks.get(i);

            Bundle bundle = new Bundle();
            bundle.putString(NAME_KEY, task.getTitle());
            bundle.putString(DESCRIPTION_KEY, task.getDescription());
            bundle.putString(DATE_KEY, task.getDate());
            bundle.putString(TIME_KEY, task.getTime());
            bundle.putInt(CLICKED_POSN_KEY, i);
            bundle.putLong(DB_ENTRY_ID, task.getId());

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
            String date = bundle.getString(MainActivity.DATE_KEY);
            String time = bundle.getString(MainActivity.TIME_KEY);
            int position = bundle.getInt(CLICKED_POSN_KEY);
            long id = bundle.getLong(DB_ENTRY_ID);

            int dd1 = bundle.getInt("dd");
            int mm1 = bundle.getInt("mm");
            int yy1 = bundle.getInt("yy");
            int hr1 = bundle.getInt("hr");
            int min1 = bundle.getInt("min");

            Task task = new Task(name, description, date, time);

            tasks.set(position, task);
            adapter.notifyDataSetChanged();
            task.setId(id);

            //DATABASE (Writable)  for updation
            ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
            SQLiteDatabase db = openHelper.getWritableDatabase();


            ContentValues contentValues = new ContentValues();
            contentValues.put("title", task.getTitle());
            contentValues.put("description", task.getDescription());
            contentValues.put("date", task.getDate());
            contentValues.put("time", task.getTime());

            id = task.getId();

            String[] selectionArgs = {id + ""};

            db.update("todo", contentValues, "id = ?", selectionArgs);

            //UPDATE ALARM by deleting previous and setting new one
            //DELETE previously set ALARM
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
//            PendingIntent pIntent  = PendingIntent.getBroadcast(getApplicationContext(), (int)id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.cancel(pIntent);
//            pIntent.cancel();

            //set new alarm
            Intent intentNew = new Intent(getApplicationContext(), AlarmReceiver.class);
            intentNew.putExtra("todoTitle", task.getTitle());
            PendingIntent pIntentNew  = PendingIntent.getBroadcast(getApplicationContext(), (int)id, intentNew, PendingIntent.FLAG_UPDATE_CURRENT);

            calendar = Calendar.getInstance();
            calendar.set(yy1, mm1, dd1, hr1, min1, 0);

            long epochTime = calendar.getTimeInMillis();

            alarmManager.set(AlarmManager.RTC_WAKEUP, epochTime, pIntentNew);

        }
    }
}