package www.motibasyon.jasdoit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.os.AsyncTask;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;
import java.util.List;

import www.motibasyon.jasdoit.Adapter.ToDoAdapter;
import www.motibasyon.jasdoit.Utils.DatabaseHandler;
import www.motibasyon.jasdoit.model.ToDoModel;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksAdapter = new ToDoAdapter(db,MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);

        tasksList = db.getAllTasks();
        Collections.reverse(tasksList);

        tasksAdapter.setTasks(tasksList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        tasksList = db.getAllTasks();
        Collections.reverse(tasksList);
        tasksAdapter.setTasks(tasksList);
        tasksAdapter.notifyDataSetChanged();
    }
}

