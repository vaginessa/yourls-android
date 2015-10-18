package de.mateware.ayourls.dialog;

import android.view.LayoutInflater;
import android.view.View;

import de.mateware.ayourls.R;

/**
 * Created by Mate on 18.10.2015.
 */
public class AddLinkDialog extends DialogCustomView {

    @Override
    public View getView(LayoutInflater inflater) {
        View view =inflater.inflate(R.layout.dialog_addlink,null);
        return view;
    }
}
