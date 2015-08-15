package app.nikunj.notes.model;

/** container having data.
 * Created by Nikunj Amipara
 * on 08-08-2015.
 */
public class Note {
    public String title;
    public String body;
    public String lastEditDate;
    public int id;

    public Note(String t, String b, String date, int id) {
        title = t;
        body = b;
        date = lastEditDate;
        this.id = id;
    }
}
