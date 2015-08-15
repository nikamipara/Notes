package app.nikunj.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import app.nikunj.notes.presenter.EditNoteManager;

public class EditNoteActivity extends ActionBarActivity {

    /*private final static String TAG = "EditNoteActivity";
    private NoteDatabaseHelper dbHelper;
    private int mPosition;
    private long mNoteId;
    private EditText mTitle;
    private EditText mBody;*/
    private EditNoteManager nm;

    private void setupdatabase() {
        /*dbHelper = new NoteDatabaseHelper(this);
        dbHelper.open();*/
    }

    /*private Note getData(long noteID) {
        if (dbHelper != null) {
            Cursor c = dbHelper.getNote(noteID);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                String title = c.getString(NoteDatabaseHelper.COLUMN_TITLE_INDEX);
                String body = c.getString(NoteDatabaseHelper.COLUMN_BODY_INDEX);
                return new Note(title, body, "", (int) noteID);
            } else {
                Toast.makeText(this, "invalid id :" + noteID, Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return null;
    }*/

    /*private boolean saveData(long noteID) {
        if (dbHelper == null) return false;
        String newTitle = mTitle.getText().toString();
        String newBody = mBody.getText().toString();

        if (noteID == -1) {
            if ((newTitle == null || newTitle.isEmpty()) && (newBody == null || newBody.isEmpty()))
                return false;
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
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        nm = new EditNoteManager(this);
        init();
        processIntent(getIntent());
    }

    private void processIntent(Intent i) {
        nm.processIntent(i);
    }

    private void init() {
        nm.init();
    }

   /* private void filldata(long noteId, int position) {
        if (noteId < 0) {
            //no need to do anything
        } else {
            Note n;
            if ((n = getData(noteId)) != null) {
                setTextTitle(n.title);
                setTextBody(n.body);
            }
        }
    }*/

  /*  private void setTextBody(String body) {
        if (mBody != null) mBody.setText(body);
    }*/

    /*private void setTextTitle(String title) {
        if (mTitle != null) mTitle.setText(title);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //
        /*if (saveData(mNoteId))
            setResultOK();
        else setResult(RESULT_CANCELED);*/
        nm.onBackPressed();
        finish();
    }

    /*private void setResultOK() {
        Intent data = new Intent();
        data.putExtra(Constants.IS_UPDATED, true);
        data.putExtra(Constants.POSITION, mPosition);
        data.putExtra(Constants.NOTE_ID, mNoteId);
        setResult(RESULT_OK, data);
    }*/

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
