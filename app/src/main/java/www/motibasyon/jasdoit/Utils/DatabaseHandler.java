package www.motibasyon.jasdoit.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

import www.motibasyon.jasdoit.model.ToDoModel;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "Jasdoit_database";
    private static final String Jasdoit_Table = "Jasdoit";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TABLE = " CREATE TABLE " + Jasdoit_Table + "(" + ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
    + TASK + "TEXT, " + STATUS + "INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + Jasdoit_Table);
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(Jasdoit_Table, null, cv);
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskslist = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(Jasdoit_Table, null, null, null, null, null, null);
            if (cur != null){
                if (cur.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskslist.add(task);
                    } while (cur.moveToNext());
                }
            }
        }

        finally{
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskslist;
    }
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(Jasdoit_Table, cv, ID + "= ?", new String[] {String.valueOf(id)});

    }

    public void updateTask(int id, String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(Jasdoit_Table, cv, ID + "= ?", new String[] {String.valueOf(id)});

    }

    public void deleteTask(int id) {
        db.delete(Jasdoit_Table, ID + "= ?", new String[] {String.valueOf(id)});
    }

}




