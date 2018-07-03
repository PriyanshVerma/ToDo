package com.example.lenovo.todolistactivitybased;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoAdapter extends ArrayAdapter {

    ArrayList<Task> tasks;
    LayoutInflater inflater;

    public ToDoAdapter(Context context, ArrayList<Task> items){
        super(context, 0, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.tasks = items;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //STEP 1 - CREATING VIEW by inflating
        View output = inflater.inflate(R.layout.task_row_layout, parent, false);

        //STEP 2 - SETTING the DATA
        TextView titleTextView = output.findViewById(R.id.title);
        TextView descriptionTextView = output.findViewById(R.id.description);

        Task task = tasks.get(position);

        titleTextView.setText(task.getTitle());
        descriptionTextView.setText(task.getDesciptn());

        return output;
    }
}
