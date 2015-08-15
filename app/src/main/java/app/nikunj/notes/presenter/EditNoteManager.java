package app.nikunj.notes.presenter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import app.nikunj.notes.R;
import app.nikunj.notes.model.DatabaseConstants;
import app.nikunj.notes.model.Note;
import app.nikunj.notes.model.NoteDatabaseHelper;

/** data handler for edit note activity
 * Created by Nikunj on 8/15/2015.
 */
public class EditNoteManager {
    private final static String TAG = "EditNoteManager";
    private int mPosition;
    private long mNoteId;
    private NoteDatabaseHelper dbHelper;

    private Activity mActivity;
    private EditText mTitle;
    private EditText mBody;

    public EditNoteManager(Activity a) {
        mActivity = a;
    }

    private void setupDataBase() {
        dbHelper = new NoteDatabaseHelper(mActivity);
        dbHelper.open();
    }

    private Note getData(long noteID) {
        if (dbHelper != null) {
            Cursor c = dbHelper.getNote(noteID);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                String title = c.getString(DatabaseConstants.COLUMN_TITLE_INDEX);
                String body = c.getString(DatabaseConstants.COLUMN_BODY_INDEX);
                return new Note(title, body, "", (int) noteID);
            } else {
                Toast.makeText(mActivity, "invalid id :" + noteID, Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return null;
    }

    private boolean saveData(long noteID) {
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
    }

    public void processIntent(Intent i) {
        //Intent i = getIntent();
        if (i != null) {
            if (i.hasExtra(Constants.NOTE_ID)) {
                mNoteId = i.getLongExtra(Constants.NOTE_ID, -1);
                mPosition = i.getIntExtra(Constants.POSITION, -1);
                Toast.makeText(mActivity, "data got id:" + mNoteId + "position=" + mPosition, Toast.LENGTH_LONG).show();
                filldata(mNoteId, mPosition);
            } else {
                mActivity.setResult(Activity.RESULT_CANCELED);
                mActivity.finish();
            }
        } else {
            mActivity.setResult(Activity.RESULT_CANCELED);
            mActivity.finish();
        }
    }

    public void init() {
        setupDataBase();
        mTitle = (EditText) mActivity.findViewById(R.id.editTextTitle);
        mBody = (EditText) mActivity.findViewById(R.id.editTextBody);
    }

    private void filldata(long noteId, int position) {
        if (noteId < 0) {
            //no need to do anything
        } else {
            Note n;
            if ((n = getData(noteId)) != null) {
                setTextTitle(n.title);
                setTextBody(n.body);
            }
        }
    }

    private void setTextBody(String body) {
        if (mBody != null) mBody.setText(body);
    }

    private void setTextTitle(String title) {
        if (mTitle != null) mTitle.setText(title);
    }

    private void setResultOK() {
        Intent data = new Intent();
        data.putExtra(Constants.IS_UPDATED, true);
        data.putExtra(Constants.POSITION, mPosition);
        data.putExtra(Constants.NOTE_ID, mNoteId);
        mActivity.setResult(Activity.RESULT_OK, data);
    }

    public void onBackPressed() {
        if (saveData(mNoteId))
            setResultOK();
        else mActivity.setResult(Activity.RESULT_CANCELED);
        mActivity.finish();
    }

    public void onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            onBackPressed();
        }
    }

}
