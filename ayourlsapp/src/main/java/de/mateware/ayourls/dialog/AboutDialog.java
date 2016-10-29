package de.mateware.ayourls.dialog;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.mateware.ayourls.BuildConfig;
import de.mateware.ayourls.R;
import de.mateware.dialog.DialogScrollingCustomView;

/**
 * Created by mate on 06.11.2015.
 */
public class AboutDialog extends DialogScrollingCustomView {


    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.dialog_about, parent, false);
        TextView versionTextView = (TextView) view.findViewById(R.id.version);
        versionTextView.setText(getContext().getString(R.string.dialog_about_version, BuildConfig.VERSION_NAME));
        Button rateButton = (Button) view.findViewById(R.id.rateButton);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    getContext().startActivity(goToMarket);
                } catch (ActivityNotFoundException ignored) {
                }
            }
        });
        return view;
    }

    public static class Builder extends AbstractBuilder<Builder, AboutDialog> {
        public Builder() {
            super(AboutDialog.class);
        }
    }
}
