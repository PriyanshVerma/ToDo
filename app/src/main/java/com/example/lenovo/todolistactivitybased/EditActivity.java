package com.example.lenovo.todolistactivitybased;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText nameEditText, descriptionEditText;
    Button saveBtn;
    Bundle bundle;

    public static int EDIT_RESULT_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveBtn = findViewById(R.id.saveBtn);

        Intent intent = getIntent();
        bundle = intent.getExtras();

        String name = bundle.getString(MainActivity.NAME_KEY);
        String description = bundle.getString(MainActivity.DESCRIPTION_KEY);

        nameEditText.setText(name);
        descriptionEditText.setText(description);

    }

    public void saveEdit(View view){

        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        bundle.putString(MainActivity.NAME_KEY, name);
        bundle.putString(MainActivity.DESCRIPTION_KEY, description);

        Intent intent = new Intent();
        intent.putExtras(bundle);

        setResult(EDIT_RESULT_CODE, intent);

        finish();
    }
}
