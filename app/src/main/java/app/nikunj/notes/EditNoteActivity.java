package app.nikunj.notes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import app.nikunj.notes.model.NoteDatabaseHelper;

public class EditNoteActivity extends ActionBarActivity {
    public static final String IS_UPDATED = "isnoteupdated";
    private final static String TAG = "EditNoteActivity";
    private NoteDatabaseHelper dbHelper;
    private int mPosition;
    private long mNoteId;
    private void setupdatabase() {
        dbHelper = new NoteDatabaseHelper(this);
        dbHelper.open();
    }
    private Note getdata(long noteID){
        if(dbHelper!=null){
            Cursor c =  dbHelper.getNote(noteID);
            if(c!=null && c.getCount()>0){
                c.moveToFirst();
                String title = c.getString(NoteDatabaseHelper.COLUMN_TITLE_INDEX);
                String body = c.getString(NoteDatabaseHelper.COLUMN_BODY_INDEX);
                return new Note(title,body,"",(int)noteID);
            }else{
                Toast.makeText(this, "invalid id :"+noteID, Toast.LENGTH_SHORT).show();
                return null;
            }
        }return null;
    }

    private boolean saveData(long noteID) {
        if (dbHelper == null) return false;
        String newTitle = mTitle.getText().toString();
        String newBody = mBody.getText().toString();

        if (noteID == -1) {
            //new data to be entered. save the id of new data and it will be returned to the view.
            long newNoteId = dbHelper.createNote(newTitle, newBody);
            mNoteId = newNoteId;
            Log.d(TAG, "new note created with id :" + newNoteId);
            return true;
        } else {
            if (dbHelper.updateNote(noteID, newTitle, newBody)) {
                Log.i(TAG, "note data saved in data base id:" + noteID);
                return true;
            } else {
                Log.e(TAG, "Error could not save data in data base id:" + noteID);
                return false;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        init();


        Intent i = getIntent();
        if(i!=null){
           if(i.hasExtra(FileManager.NOTE_ID)){
                mNoteId = i.getLongExtra(FileManager.NOTE_ID, -1);
                mPosition = i.getIntExtra(FileManager.POSITION,-1);
               Toast.makeText(this,"data got id:"+mNoteId+"position="+mPosition,Toast.LENGTH_LONG).show();
               filldata(mNoteId,mPosition);
           }else{
               //TODO add result failed code ore something
               setResult(RESULT_CANCELED);
               finish();
           }
        }
        else{
            //TODO add result failed code ore something.
            setResult(RESULT_CANCELED);
            finish();
        }
    }
    private EditText mTitle;
    private EditText mBody;
    private void init() {
        setupdatabase();
        mTitle = (EditText)findViewById(R.id.editTextTitle);
        mBody = (EditText)findViewById(R.id.editTextBody);
    }

    private void filldata(long noteId, int position) {
        if(noteId<0){
            //no need to do anything
        }else{
            Note n;
            if((n =getdata(noteId))!=null){
                initializeTitle(n.title);
                initializeBody(n.body);
            }
        }
    }

    private void initializeBody(String body) {
        if(mBody!=null) mBody.setText(body);
    }

    private void initializeTitle(String title) {
       if(mTitle!=null) mTitle.setText(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //
        saveData(mNoteId);
        //TODO setresult
        setResultOK();
        /*super.onBackPressed();*/
        finish();
    }

    private void setResultOK() {
        Intent data = new Intent();
        data.putExtra(IS_UPDATED,true);
        data.putExtra(FileManager.POSITION,mPosition);
        data.putExtra(FileManager.NOTE_ID,mNoteId);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //saveData(mNoteId);
        //setResultOK();
    }
}
