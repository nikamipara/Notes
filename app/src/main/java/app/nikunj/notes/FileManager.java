package app.nikunj.notes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import app.nikunj.notes.model.NoteDatabaseHelper;

public class FileManager extends ActionBarActivity {
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
                String name = notes.get(position).title;
                Toast.makeText(FileManager.this, name + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        startloadingdata();
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
            notes.add(new Note(c.getString(NoteDatabaseHelper.COLUMN_TITLE_INDEX), c.getString(NoteDatabaseHelper.COLUMN_BODY_INDEX),""));
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
            adddata();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(dbHelper!=null)dbHelper.close();
    }

    private ArrayList<Note> getThronesCharacters() {
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



    }
    private void adddata(){
        gridLayoutManager.scrollToPosition(0);
        notes.add(0,new Note("threetitle", "body", "  "));
// Notify the adapter
        adapter.notifyItemInserted(0);
    }
}
