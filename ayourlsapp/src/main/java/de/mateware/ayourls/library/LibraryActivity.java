package de.mateware.ayourls.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import de.mateware.ayourls.R;
import de.mateware.ayourls.dialog.AboutDialog;
import de.mateware.ayourls.dialog.DialogActivty;
import de.mateware.ayourls.imports.ImportActivity;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.settings.SettingsActivity;
import de.mateware.ayourls.utils.TintHelper;

public class LibraryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static Logger log = LoggerFactory.getLogger(LibraryActivity.class);

    private static final String DIALOG_ABOUT = "aboutDialog";

    private RecyclerView recyclerView;
    private LinkLibraryAdapter adapter;
    private FloatingActionButton fab;
    private LinearLayout nolinksLayout;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_link_library);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawableCompat.setTint(DrawableCompat.wrap(fab.getDrawable()), (ContextCompat.getColor(this, R.color.menu_item)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(LibraryActivity.this, DialogActivty.class);
                addIntent.putExtra(DialogActivty.EXTRA_DIALOG, DialogActivty.DIALOG_ADD);
                //addIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(addIntent);
            }
        });
        nolinksLayout = (LinearLayout) findViewById(R.id.nolinks_layout);
        nolinksLayout.setVisibility(View.GONE);
        TintHelper.tintImageView((ImageView) findViewById(R.id.nolinks_image), R.color.primary_dark);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LinkLibraryAdapter();
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean(getString(R.string.pref_key_server_check), false)) {
            fab.setEnabled(true);
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.fab_background_tint)));
        } else {
            fab.setEnabled(false);
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.darker_gray)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_link_lib, menu);
        MenuItem importItem = menu.findItem(R.id.action_import);
        importItem.setEnabled(preferences.getBoolean(getString(R.string.pref_key_server_check), false));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_import:
                Intent importIntent = new Intent(this, ImportActivity.class);
                startActivity(importIntent);
                return true;
            case R.id.action_about:
                new AboutDialog().withTitle(R.string.action_about)
                                 .withPositiveButton()
                                 .show(getSupportFragmentManager(), DIALOG_ABOUT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        log.debug("creating loader with id {} and args {}", id, args);
        return new CursorLoader(this, Link.getContentUri(), null, null, null, Link.Columns.DATE + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        log.debug("loader finished");


        if (data != null) {

            if (data.getCount() == 0) {
                nolinksLayout.setVisibility(View.VISIBLE);
                adapter.setItems(null);
                return;
            }

            if (data.moveToLast()) {
                nolinksLayout.setVisibility(View.GONE);
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
