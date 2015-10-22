package de.mateware.ayourls.viewmodel;

import android.content.Context;
import android.view.View;

/**
 * Created by Mate on 22.10.2015.
 */
public class LinkImportViewModel extends LinkViewModel {

    public LinkImportViewModel(Context context) {
        super(context);
    }

    @Override
    public View.OnClickListener onClickDetails() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        };
    }
}
