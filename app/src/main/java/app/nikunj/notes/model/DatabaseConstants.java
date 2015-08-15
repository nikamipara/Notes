package app.nikunj.notes.model;

/** all database related constants.
 * Created by Nikunj on 8/15/2015.
 */
public class DatabaseConstants {
    public static final String DATABASE_CREATE =
            "create table notes (_id integer primary key autoincrement, "
                    + "title text not null, body text not null);";
    public static final String DATABASE_NAME = "nikunjnotes";
    public static final String DATABASE_TABLE = "notes";
    public static final int DATABASE_VERSION = 10001;

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_ID = "_id";

    public static final int COLUMN_TITLE_INDEX = 1;
    public static final int COLUMN_BODY_INDEX = 2;
    public static final int COLUMN_ID_INDEX = 0;

}
