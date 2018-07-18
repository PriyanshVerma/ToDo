package com.example.lenovo.todolistactivitybased;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoAdapter extends ArrayAdapter {

    ArrayList<Task> tasks;
    LayoutInflater inflater;
    ToDoClickListener clickListener;

    public ToDoAdapter(Context context, ArrayList<Task> items, ToDoClickListener clickListener){
        super(context, 0, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.tasks = items;
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //STEP 1 - CREATING VIEW by inflating
        View output = inflater.inflate(R.layout.task_row_layout, parent, false);

        //STEP 2 - SETTING the DATA
        TextView titleTextView = output.findViewById(R.id.title);
        TextView descriptionTextView = output.findViewById(R.id.description);
        TextView dateTextView = output.findViewById(R.id.date);
        TextView timeTextView = output.findViewById(R.id.Time);
        Button deleteBtn = output.findViewById(R.id.deleteBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.rowBtnClicked(v, position);
            }
        });
        Task task = tasks.get(position);

        titleTextView.setText(task.getTitle());
        descriptionTextView.setText(task.getDescription());
        dateTextView.setText(task.getDate());
        timeTextView.setText(task.getTime());

        return output;
    }
}
