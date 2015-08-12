package app.nikunj.notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import app.nikunj.notes.model.NoteDatabaseHelper;

public class FileManager extends ActionBarActivity {
    public static final String TAG = "FileManager";
    public NotesAdapter adapter;
    public RecyclerView rvUsers;
    public ArrayList<Note> notes;
    public StaggeredGridLayoutManager gridLayoutManager;
    public NoteDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);


        // Lookup the recyclerview in activity layout
         rvUsers = (RecyclerView) findViewById(R.id.rvUsers);
        // Create adapter passing in the sample user data
         notes = new ArrayList<>();
         adapter = new NotesAdapter(this,notes);
        // Attach the adapter to the recyclerview to populate items
        rvUsers.setAdapter(adapter);
        // Set layout manager to position the items
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
         gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

// Attach the layout manager to the recycler view
        rvUsers.setLayoutManager(gridLayoutManager);

        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                editNoteAt(position,false);
            }
        });
        adapter.setonItemDelete(new NotesAdapter.OnItemDelete() {
            @Override
            public void onDelete(int position) {
                deletedata(position);
            }
        });

        startloadingdata();
    }
    public static final int REQUEST_EDIT=5;
    public static final int REQUEST_NEW=6;
    public static final String NOTE_ID = "noteId";
    public static final String POSITION = "position";
    private void editNoteAt(int position,boolean isnewNote) {
        if(isnewNote){
            Intent i = new Intent(this,EditNoteActivity.class) ;
            i.putExtra(NOTE_ID,-1);
            startActivityForResult(i,REQUEST_NEW);
        }else{
            long id = notes.get(position).id;
            Intent i = new Intent(this,EditNoteActivity.class) ;
            i.putExtra(NOTE_ID,id);
            i.putExtra(POSITION,position);
            startActivityForResult(i, REQUEST_EDIT);
        }
        //String name = notes.get(position).title;
        //Toast.makeText(FileManager.this, name + " was clicked!", Toast.LENGTH_SHORT).show();
    }

    private void startloadingdata() {

        setupdatabase();
        Cursor c = dbHelper.getAllNotes();
        int noofrows = c.getCount();
        if(noofrows==0){
            dbHelper.createNote("Dany Targaryen", "Valyria");
            dbHelper.createNote("Rob Stark", "Winterfell");
            dbHelper.createNote("Jon Snow", "Castle Black");
            dbHelper.createNote("Jon Snow", "Castle Black");
            dbHelper.createNote("Tyvin Lanister", "King's Landing");
            dbHelper.createNote("Agon Targaryen", "Valyria");
            dbHelper.createNote("Tyvin Lanister", "King's Landing");
            dbHelper.createNote("Arya Stark", "Winterfell");
            dbHelper.createNote("Imp", "King's Landing");
            dbHelper.createNote("Dany Targaryen", "Valyria");

        }
        c.close();
        c= dbHelper.getAllNotes();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            notes.add(new Note(c.getString(NoteDatabaseHelper.COLUMN_TITLE_INDEX), c.getString(NoteDatabaseHelper.COLUMN_BODY_INDEX), "", c.getInt(NoteDatabaseHelper.COLUMN_ID_INDEX)));
            c.moveToNext();
        }
        c.close();
        adapter.notifyDataSetChanged();
        /*notes.add(new Note("Dany Targaryen", "Valyria", ""));
        notes.add(new Note("Rob Stark", "Winterfell",""));
        notes.add(new Note("Jon Snow", "Castle Black", ""));
        notes.add(new Note("Tyvin Lanister", "King's Landing",""));
        notes.add(new Note("Agon Targaryen", "Valyria",""));
        notes.add(new Note("Sansa Stark", "Winterfell", ""));
        notes.add(new Note("Arya Stark", "Winterfell", ""));
        notes.add(new Note("Imp", "King's Landing", ""));*/
    }

    private void setupdatabase() {
        dbHelper = new NoteDatabaseHelper(this);
        dbHelper.open();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.add){
            /*adddata();*/
            editNoteAt(-1,true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("nikunj","onactivity result");
       // if (requestCode == REQUEST_EDIT) {
            Log.d("nikunj","requestCode:REQUEST_EDIT");
            if(data!=null){
                Log.d("nikunj","data not null");
                boolean isUpdated = data.getBooleanExtra(EditNoteActivity.IS_UPDATED,true);
                int position = data.getIntExtra(POSITION, -1);
                long noteId = data.getLongExtra(NOTE_ID, -1);
                updateList(isUpdated, position, noteId);
            }
        //} else if (requestCode == REQUEST_NEW) {
            Log.d("nikunj","requestCode:REQUEST_NEw");
       // }
    }

    private void updateList(boolean isUpdated, int position,long id) {
        if(id<=0)return;
        Note newNote = getNoteFromDatabase(id);
        if(position==-1){
            if(newNote!=null){
                adddNewNoteInList(newNote);
            }
        }else{
            if(newNote!=null){
                updateNoteInList(newNote, position);
            }
        }
    }

    private void updateNoteInList(Note newNote, int position) {
        gridLayoutManager.scrollToPosition(position);
        //int id = (int) dbHelper.createNote("Whites Walkers", "Westrossss");
        // Log.i(TAG, "database entry with id:" + id);
        Note n = notes.get(position);
        n.title = newNote.title;
        n.body = newNote.body;
        //notes.get
// Notify the adapter
        adapter.notifyItemChanged(position);
       // adapter.notifyDataSetChanged();
    }

    private Note getNoteFromDatabase(long id) {
        Cursor c = dbHelper.getNote(id);
        if(c!=null){
            c.moveToFirst();
            String title =c.getString(NoteDatabaseHelper.COLUMN_TITLE_INDEX);
            String body =c.getString(NoteDatabaseHelper.COLUMN_BODY_INDEX);
            return new Note(title,body,"",(int)id);
        }return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(dbHelper!=null)dbHelper.close();
    }

    /*    private ArrayList<Note> getThronesCharacters() {
            ArrayList<Note> items = new ArrayList<>();
            items.add(new Note("Dany Targaryen", "Valyria",""));
            items.add(new Note("Rob Stark", "Winterfell",""));
            items.add(new Note("Jon Snow", "Castle Black",""));
            items.add(new Note("Tyvin Lanister", "King's Landing",""));
            items.add(new Note("Agon Targaryen", "Valyria",""));
            items.add(new Note("Sansa Stark", "Winterfell",""));
            items.add(new Note("Arya Stark", "Winterfell",""));
            items.add(new Note("Imp", "King's Landing",""));
            return items;



        }*/
    private void adddNewNoteInList(Note note) {
        gridLayoutManager.scrollToPosition(0);
        //int id = (int) dbHelper.createNote("Whites Walkers", "Westrossss");
       // Log.i(TAG, "database entry with id:" + id);
        notes.add(0, note);
       // Notify the adapter
        adapter.notifyItemInserted(0);
      //  adapter.notifyDataSetChanged();;

    }

    private void deletedata(int position) {
       try{ gridLayoutManager.assertNotInLayoutOrScroll("delete not permitted.");
       }
       catch (IllegalStateException e){
           e.printStackTrace();
           return;
       }
        if(position<0 || position>notes.size()-1/*|| id>notes.size()-1*/) {
            Log.e(TAG,"illigal position"+position);
            adapter.notifyDataSetChanged();
            return;
        }
        int id = notes.get(position).id;
        if(id<0 /*|| id>notes.size()-1*/) {
            Log.e(TAG,"illigal id"+id);
            return;
        }
        if (dbHelper.deleteNote(id)) Log.i(TAG, "row delete at id:" + id);
        else {
            Log.e(TAG, "not able to delete ID:" + id);
            Log.d("nikunj", notes.toString());
            String s = "";
            for (Note n : notes) s += " " + n.id + " " + n.title;
            Log.d("nikunj", s);

            Cursor c = dbHelper.getAllNotes();
            String trace = "";
            c.moveToFirst();
            while (!c.isAfterLast()) {
                trace += "id:" + c.getLong(NoteDatabaseHelper.COLUMN_ID_INDEX) + " title:" + c.getString(NoteDatabaseHelper.COLUMN_TITLE_INDEX) + "\n";
                c.moveToNext();
            }
            c.close();
            Log.e(TAG, trace);
        }
        notes.remove(position);
        adapter.notifyItemRemoved(position);
    }
}
