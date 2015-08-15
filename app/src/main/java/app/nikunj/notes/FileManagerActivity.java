package app.nikunj.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import app.nikunj.notes.presenter.FileManager;

public class FileManagerActivity extends ActionBarActivity {
    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        fm = new FileManager(this);
        setRecyclerView(R.id.recycler_view_notes);
        loadData();
    }

    private void loadData() {
        fm.loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        fm.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fm.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fm.onDestroy();
        fm = null;

    }

    public void setRecyclerView(int recyclerViewId) {
        fm.setRecyclerView(recyclerViewId);
    }
}
