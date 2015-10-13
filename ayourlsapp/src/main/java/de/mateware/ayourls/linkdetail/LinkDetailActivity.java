package de.mateware.ayourls.linkdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.mateware.ayourls.R;

/**
 * Created by Mate on 12.10.2015.
 */
public class LinkDetailActivity extends AppCompatActivity {

    public static final String EXTRA_LINK_ID = "extraLinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_activity);
        if (savedInstanceState == null) {
            long id  = getIntent().getLongExtra(EXTRA_LINK_ID,-1);
            if (id == -1) {
                finish();
            }
            else {
                Bundle arguments = new Bundle();
                arguments.putLong(LinkDetailFragment.ARGUMENT_LINK_ID, id);
                LinkDetailFragment detailFragment = new LinkDetailFragment();
                detailFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                                           .replace(R.id.content_frame, detailFragment)
                                           .commit();
            }
        }
    }
}
