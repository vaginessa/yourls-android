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

    private View customView;

    @Override
    AppCompatDialog createDialogToReturn() {
        AlertDialog result = builder.create();
        customView = getView(LayoutInflater.from(getActivity()));
        result.setView(customView);
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public abstract View getView(LayoutInflater inflater);

}
