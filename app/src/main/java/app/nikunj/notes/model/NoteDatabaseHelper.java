package app.nikunj.notes.model;

/** database for storing notes
 * Created by nikam on 09-08-2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NoteDatabaseHelper {

    /*public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_ID = "_id";

    public static final int COLUMN_TITLE_INDEX = 1;
    public static final int COLUMN_BODY_INDEX = 2;
    public static final int COLUMN_ID_INDEX = 0;
*/
    private static final String TAG = "NoteDataBaseHelper";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //Database creation
    /*private static final String DATABASE_CREATE =
            "create table notes (_id integer primary key autoincrement, "
                    + "title text not null, body text not null);";

    private static final String DATABASE_NAME = "nikunjnotes";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 10001;*/

    private final Context mContext;

    public NoteDatabaseHelper(Context ctx) {
        this.mContext = ctx;
    }


    public NoteDatabaseHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public long createNote(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseConstants.COLUMN_TITLE, title);
        initialValues.put(DatabaseConstants.COLUMN_BODY, body);

        return mDb.insert(DatabaseConstants.DATABASE_TABLE, null, initialValues);
    }


    public boolean deleteNote(long rowId) {

        return mDb.delete(DatabaseConstants.DATABASE_TABLE, DatabaseConstants.COLUMN_ID + "=" + rowId, null) > 0;
    }

    public Cursor getAllNotes() {

        return mDb.query(DatabaseConstants.DATABASE_TABLE, new String[]{DatabaseConstants.COLUMN_ID, DatabaseConstants.COLUMN_TITLE,
                DatabaseConstants.COLUMN_BODY}, null, null, null, null, DatabaseConstants.COLUMN_ID + " " + "DESC");
    }

    public Cursor getNote(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DatabaseConstants.DATABASE_TABLE, new String[]{DatabaseConstants.COLUMN_ID,
                                DatabaseConstants.COLUMN_TITLE, DatabaseConstants.COLUMN_BODY}, DatabaseConstants.COLUMN_ID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean updateNote(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(DatabaseConstants.COLUMN_TITLE, title);
        args.put(DatabaseConstants.COLUMN_BODY, body);

        return mDb.update(DatabaseConstants.DATABASE_TABLE, args, DatabaseConstants.COLUMN_ID + "=" + rowId, null) > 0;
    }
}

