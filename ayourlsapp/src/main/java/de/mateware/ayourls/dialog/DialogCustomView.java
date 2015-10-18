package de.mateware.ayourls.dialog;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Mate on 18.10.2015.
 */
public abstract class DialogCustomView extends Dialog {
    @Override
    void setDialogContent() {
        //Override to do nothing
    }

    @Override
    AppCompatDialog createDialogToReturn() {
        AlertDialog result = builder.create();
        View view = getView(LayoutInflater.from(getActivity()));
        result.setView(view);
        return result;
    }

    public abstract View getView(LayoutInflater inflater);
}
