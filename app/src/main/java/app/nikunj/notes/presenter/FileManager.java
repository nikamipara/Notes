package app.nikunj.notes.presenter;

/**
 * Created by Nikunj.
 * implements logic for filemanager acitivity.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import app.nikunj.notes.EditNoteActivity;
import app.nikunj.notes.NotesAdapter;
import app.nikunj.notes.R;
import app.nikunj.notes.model.DatabaseConstants;
import app.nikunj.notes.model.Note;
import app.nikunj.notes.model.NoteDatabaseHelper;

public class FileManager {
    public static final String TAG = "FileManger";
    public NoteDatabaseHelper dataHelper;
    public RecyclerView recyclerViewNotes;
    public NotesAdapter adapter;
    public ArrayList<Note> notes;
    public Activity mActivity;
    public StaggeredGridLayoutManager gridLayoutManager;

    public FileManager(Activity a) {
        mActivity = a;
    }

    public void setRecyclerView(int id) {
        recyclerViewNotes = (RecyclerView) mActivity.findViewById(id);
        notes = new ArrayList<>();
        adapter = new NotesAdapter(mActivity, notes);
        recyclerViewNotes.setAdapter(adapter);
        gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewNotes.setLayoutManager(gridLayoutManager);

        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                editNoteAt(position, false);
            }
        });

        adapter.setonItemDelete(new NotesAdapter.OnItemDelete() {
            @Override
            public void onDelete(int position) {
                deletedata(position);
            }
        });

    }

    public void onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
        } else if (id == R.id.add) {
            editNoteAt(-1, true);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (requestCode == REQUEST_EDIT) {
        if (data != null) {
            Log.d("nikunj", "data not null");
            boolean isUpdated = data.getBooleanExtra(Constants.IS_UPDATED, true);
            int position = data.getIntExtra(Constants.POSITION, -1);
            long noteId = data.getLongExtra(Constants.NOTE_ID, -1);
            updateList(isUpdated, position, noteId);
        }
    }

    private void setupdatabase() {
        dataHelper = new NoteDatabaseHelper(mActivity);
        dataHelper.open();

    }

    public void startloadingdata() {

        setupdatabase();
        Cursor c = dataHelper.getAllNotes();
        int noofrows = c.getCount();
        if (noofrows == 0) {
            dataHelper.createNote("Dany Targaryen", "Valyria");
            dataHelper.createNote("Rob Stark", "Winterfell");
            dataHelper.createNote("Jon Snow", "Castle Black");
            dataHelper.createNote("Jon Snow", "Castle Black");
            dataHelper.createNote("Tyvin Lanister", "King's Landing");
            dataHelper.createNote("Agon Targaryen", "Valyria");
            dataHelper.createNote("Tyvin Lanister", "King's Landing");
            dataHelper.createNote("Arya Stark", "Winterfell");
            dataHelper.createNote("Imp", "King's Landing");
            dataHelper.createNote("Dany Targaryen", "Valyria");

        }
        c.close();
        c = dataHelper.getAllNotes();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            notes.add(new Note(c.getString(DatabaseConstants.COLUMN_TITLE_INDEX), c.getString(DatabaseConstants.COLUMN_BODY_INDEX), "", c.getInt(DatabaseConstants.COLUMN_ID_INDEX)));
            c.moveToNext();
        }
        c.close();
        adapter.notifyDataSetChanged();
    }

    public void editNoteAt(int position, boolean isnewNote) {
        if (isnewNote) {
            Intent i = new Intent(mActivity, EditNoteActivity.class);
            i.putExtra(Constants.NOTE_ID, -1);
            mActivity.startActivityForResult(i, Constants.REQUEST_NEW);
        } else {
            long id = notes.get(position).id;
            Intent i = new Intent(mActivity, EditNoteActivity.class);
            i.putExtra(Constants.NOTE_ID, id);
            i.putExtra(Constants.POSITION, position);
            mActivity.startActivityForResult(i, Constants.REQUEST_EDIT);
        }
    }

    private Note getNoteFromDatabase(long id) {
        Cursor c = dataHelper.getNote(id);
        if (c != null) {
            c.moveToFirst();
            String title = c.getString(DatabaseConstants.COLUMN_TITLE_INDEX);
            String body = c.getString(DatabaseConstants.COLUMN_BODY_INDEX);
            return new Note(title, body, "", (int) id);
        }
        return null;
    }

    private void updateList(boolean isUpdated, int position, long id) {
        if (id <= 0) return;
        Note newNote = getNoteFromDatabase(id);
        if (position == -1) {
            if (newNote != null) {
                adddNewNoteInList(newNote);
            }
        } else {
            if (newNote != null) {
                updateNoteInList(newNote, position);
            }
        }
    }

    private void updateNoteInList(Note newNote, int position) {
        gridLayoutManager.scrollToPosition(position);
        Note n = notes.get(position);
        n.title = newNote.title;
        n.body = newNote.body;
        adapter.notifyItemChanged(position);
    }

    private void adddNewNoteInList(Note note) {
        gridLayoutManager.scrollToPosition(0);
        notes.add(0, note);
        adapter.notifyItemInserted(0);

    }

    private void deletedata(int position) {
        try {
            gridLayoutManager.assertNotInLayoutOrScroll("delete not permitted.");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return;
        }
        if (position < 0 || position > notes.size() - 1/*|| id>notes.size()-1*/) {
            Log.e(TAG, "illigal position" + position);
            adapter.notifyDataSetChanged();
            return;
        }
        int id = notes.get(position).id;
        if (id < 0 /*|| id>notes.size()-1*/) {
            Log.e(TAG, "illigal id" + id);
            return;
        }
        if (dataHelper.deleteNote(id)) Log.i(TAG, "row delete at id:" + id);
        else {
            Log.e(TAG, "not able to delete ID:" + id);
            Log.d("nikunj", notes.toString());
            String s = "";
            for (Note n : notes) s += " " + n.id + " " + n.title;
            Log.d("nikunj", s);

            Cursor c = dataHelper.getAllNotes();
            String trace = "";
            c.moveToFirst();
            while (!c.isAfterLast()) {
                trace += "id:" + c.getLong(DatabaseConstants.COLUMN_ID_INDEX) + " title:" + c.getString(DatabaseConstants.COLUMN_TITLE_INDEX) + "\n";
                c.moveToNext();
            }
            c.close();
            Log.e(TAG, trace);
        }
        notes.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void onDestroy() {
        if (dataHelper != null) dataHelper.close();
    }
}