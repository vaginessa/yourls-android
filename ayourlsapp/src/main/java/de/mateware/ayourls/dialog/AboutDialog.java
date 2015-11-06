package de.mateware.ayourls.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.mateware.ayourls.BuildConfig;
import de.mateware.ayourls.R;

/**
 * Created by mate on 06.11.2015.
 */
public class AboutDialog extends DialogScrollingCustomView {
    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.dialog_about,parent,false);
        TextView versionTextView = (TextView) view.findViewById(R.id.version);
        versionTextView.setText(getString(R.string.dialog_about_version, BuildConfig.VERSION_NAME));
        return view;
    }
}
