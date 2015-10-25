package de.mateware.ayourls.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import de.mateware.ayourls.R;
import de.mateware.ayourls.imports.ImportActivity;
import de.mateware.ayourls.model.Link;

/**
 * Created by Mate on 22.10.2015.
 */
public class LinkImportViewModel extends LinkViewModel {

    public LinkImportViewModel(Context context) {
        super(context);
    }
    boolean newLink = true;

    @Override
    public void setLink(Link link) {
        super.setLink(link);
        Cursor cursor = getContext().getContentResolver()
                                    .query(Link.getContentUri(), null, Link.Columns.SHORTURL + " LIKE '" + getShorturl() + "'", null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    newLink = false;
                }
            } finally {
                cursor.close();
            }
        }
    }

    public
    @ColorRes
    int getTitleTextColor() {
        if (newLink)
            return ContextCompat.getColor(getContext(),R.color.primary_dark);
        return ContextCompat.getColor(getContext(),android.R.color.darker_gray);
    }

    @Override
    public View.OnClickListener onClickDetails() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof ImportActivity) {
                    ImportActivity importActivity = (ImportActivity) v.getContext();
                    importActivity.onImportLink(getLink());
                }
            }
        };
    }
}
