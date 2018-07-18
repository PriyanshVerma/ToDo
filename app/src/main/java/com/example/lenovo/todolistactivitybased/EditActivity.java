package com.example.lenovo.todolistactivitybased;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    EditText nameEditText, descriptionEditText, timeEditText, dateEditText;
    Button saveBtn;
    Bundle bundle;
    String date, time;

    int countDateClick = 0, countTimeClick = 0;

    int dd, mm, yy, hr, min;

    public static int EDIT_RESULT_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        timeEditText = findViewById(R.id.timeEditText);
        dateEditText = findViewById(R.id.dateEditText);
        saveBtn = findViewById(R.id.saveBtn);

        Intent intent = getIntent();
        bundle = intent.getExtras();

        String name = bundle.getString(MainActivity.NAME_KEY);
        String description = bundle.getString(MainActivity.DESCRIPTION_KEY);
        date = bundle.getString(MainActivity.DATE_KEY);
        time = bundle.getString(MainActivity.TIME_KEY);

        nameEditText.setText(name);
        descriptionEditText.setText(description);
//        dateEditText.setText(date);
//        timeEditText.setText(time);
        timeEditText.setHint(time);
        dateEditText.setHint(date);


        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimeClick++;
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (minute < 10)   time = hourOfDay + ":0" + minute;
                                else   time = hourOfDay + ":" + minute;
                                timeEditText.setText(time);

                                hr = hourOfDay;
                                min = minute;

                            }
                        }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDateClick++;
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    date = day + "/" + (month+1) + "/" + year;
                                    dateEditText.setText(date);

                                    dd = day;
                                    mm = month;
                                    yy = year;

                                }
                            }, year, month, dayOfMonth);
                    datePickerDialog.show();
                }
            }
        });
    }

    public void saveEdit(View view){

        if (countDateClick == 0 || countTimeClick == 0){
            Toast.makeText(this, "Please set date and time of the task, again!", Toast.LENGTH_LONG).show();
        }

        else {
            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            bundle.putString(MainActivity.NAME_KEY, name);
            bundle.putString(MainActivity.DESCRIPTION_KEY, description);
            bundle.putString(MainActivity.DATE_KEY, date);
            bundle.putString(MainActivity.TIME_KEY, time);

            bundle.putInt("dd", dd);
            bundle.putInt("mm", mm);
            bundle.putInt("yy", yy);
            bundle.putInt("hr", hr);
            bundle.putInt("min", min);

            Intent intent = new Intent();
            intent.putExtras(bundle);

            setResult(EDIT_RESULT_CODE, intent);

            finish();
        }
    }
}