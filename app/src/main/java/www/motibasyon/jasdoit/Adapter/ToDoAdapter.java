package www.motibasyon.jasdoit.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import www.motibasyon.jasdoit.AddNewTask;
import www.motibasyon.jasdoit.MainActivity;
import www.motibasyon.jasdoit.R;
import www.motibasyon.jasdoit.Utils.DatabaseHandler;
import www.motibasyon.jasdoit.model.ToDoModel;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todolist;
    private DatabaseHandler db;
    private MainActivity activity;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasklayout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        ToDoModel item = todolist.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    db.updateStatus(item.getId(),1);
                }
                else{
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    private boolean toBoolean(int n){
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> todolist){
        this.todolist = todolist;
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        ToDoModel item = todolist.get(position);
        db.deleteTask(item.getId());
        todolist.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = todolist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
