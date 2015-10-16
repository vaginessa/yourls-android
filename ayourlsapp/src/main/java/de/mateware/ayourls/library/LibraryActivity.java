package de.mateware.ayourls.library;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import de.mateware.ayourls.R;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.settings.SettingsActivity;

public class LibraryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static Logger log = LoggerFactory.getLogger(LibraryActivity.class);

    private RecyclerView recyclerView;
    private LinkLibraryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_link_library);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawableCompat.setTint(DrawableCompat.wrap(fab.getDrawable()), (ContextCompat.getColor(this, R.color.menu_item)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.debug("meeep");
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LinkLibraryAdapter();
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_link_lib, menu);
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
            showSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        log.debug("creating loader with id {} and args {}", id, args);
        return new CursorLoader(this, Link.getContentUri(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        log.debug("loader finished");


        if (data != null) {

            if (data.moveToLast()) {
                List<Link> links = new ArrayList<>();
                do {
                    Link link = new Link();
                    link.load(data);
                    links.add(link);
                } while (data.moveToPrevious());
                adapter.setItems(links);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        log.debug("loader reset {}", loader);
        adapter.setItems(null);
    }
}
