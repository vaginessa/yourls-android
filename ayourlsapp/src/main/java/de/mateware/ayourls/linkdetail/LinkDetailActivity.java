package de.mateware.ayourls.linkdetail;


import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.databinding.ActivityLinkdetailBinding;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.viewmodel.LinkViewModel;

/**
 * Created by Mate on 12.10.2015.
 */
public class LinkDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static Logger log = LoggerFactory.getLogger(LinkDetailActivity.class);

    public static final String EXTRA_LINK_ID = "extraLinkId";

    private ActivityLinkdetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_linkdetail);


        Bundle loaderBundle = null;
        if (savedInstanceState == null) {
            loaderBundle = new Bundle();
            loaderBundle.putLong(EXTRA_LINK_ID, getIntent().getLongExtra(EXTRA_LINK_ID, -1));
        }
        getSupportLoaderManager().initLoader(0, loaderBundle, this);
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        //outState.putLong(EXTRA_LINK_ID, getIntent().getLongExtra(EXTRA_LINK_ID, -1));
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        log.debug("creating loader with id {} and args {}", id, args);
        return new CursorLoader(this, Link.getContentUri(args.getLong(EXTRA_LINK_ID)), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        log.debug("loaded {} {}", loader, data);
        if (data.isBeforeFirst()) data.moveToNext();
            Link link = new Link();
            link.load(data);
            log.debug("loaded {}", link);
            binding.setViewModel(new LinkViewModel(link));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        log.debug("reset {}", loader);
    }
}
