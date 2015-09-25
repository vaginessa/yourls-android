package de.mateware.ayourls.settings;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.EditText;

/**
 * Created by mate on 25.09.2015.
 */
public class EditTextPreferenceDialogFragmentCompatSingleLine extends EditTextPreferenceDialogFragmentCompat {

    public static EditTextPreferenceDialogFragmentCompatSingleLine newInstance(String key) {
        final EditTextPreferenceDialogFragmentCompatSingleLine fragment = new EditTextPreferenceDialogFragmentCompatSingleLine();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    protected void onAddEditTextToDialogView(View dialogView, EditText editText) {
        editText.setSingleLine(true);
        super.onAddEditTextToDialogView(dialogView, editText);
    }
}
