package com.example.lenovo.todolistactivitybased;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    TextView titleTextView, descriptionTextView;
    String title, description;
    Bundle bundle;

    public static final int EDIT_REQUEST_CODE = 2;
    public static final int DISPLAY_RESULT_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();

        bundle = intent.getExtras();

        title = bundle.getString(MainActivity.NAME_KEY);
        description = bundle.getString(MainActivity.DESCRIPTION_KEY);

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.edit){

            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtras(bundle);

            startActivityForResult(intent, EDIT_REQUEST_CODE);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REQUEST_CODE && resultCode == EditActivity.EDIT_RESULT_CODE){



                bundle = data.getExtras();

                String name = bundle.getString(MainActivity.NAME_KEY);
                String description = bundle.getString(MainActivity.DESCRIPTION_KEY);

                titleTextView.setText(name);
                descriptionTextView.setText(description);

                setResult(DISPLAY_RESULT_CODE, data);
        }
    }
}