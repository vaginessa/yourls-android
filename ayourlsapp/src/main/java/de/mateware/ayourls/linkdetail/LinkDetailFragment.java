package de.mateware.ayourls.linkdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.mateware.ayourls.R;

/**
 * Created by Mate on 12.10.2015.
 */
public class LinkDetailFragment extends Fragment {

    public static final String ARGUMENT_LINK_ID = "argumentLinkId";

    public LinkDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getArguments().getLong(ARGUMENT_LINK_ID);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.link_detail, container, false);
        return view;
    }
}
