package de.mateware.ayourls.library;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import de.mateware.ayourls.R;
import de.mateware.ayourls.model.Link;

public class LinkLibraryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    final static Logger log = LoggerFactory.getLogger(LinkLibraryFragment.class);

    private RecyclerView recyclerView;
    private LinkLibraryAdapter adapter;

    public LinkLibraryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_link_lib, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LinkLibraryAdapter();
        recyclerView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_link_lib, menu);
        //        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        log.debug("creating loader with id {} and args {}", id, args);
        return new CursorLoader(getContext(), Link.getContentUri(), null, null, null, null);
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
