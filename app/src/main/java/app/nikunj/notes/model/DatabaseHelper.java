package app.nikunj.notes.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nikunj on 8/15/2015.
 */
class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG="DatabaseHelper";
    /*private static final String DATABASE_CREATE =
            "create table notes (_id integer primary key autoincrement, "
                    + "title text not null, body text not null);";

    private static final String DATABASE_NAME = "nikunjnotes";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 10001;*/
    DatabaseHelper(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null,DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DatabaseConstants.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }
}
