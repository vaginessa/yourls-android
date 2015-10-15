package de.mateware.ayourls.settings;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.EditText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mate on 25.09.2015.
 */
public class EditTextPreferenceDialogFragmentCompatSingleLine extends EditTextPreferenceDialogFragmentCompat {

    private static final Logger log = LoggerFactory.getLogger(EditTextPreferenceDialogFragmentCompatSingleLine.class);

    public static EditTextPreferenceDialogFragmentCompatSingleLine newInstance(String key) {
        final EditTextPreferenceDialogFragmentCompatSingleLine fragment = new EditTextPreferenceDialogFragmentCompatSingleLine();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = (EditText) view.findViewById(android.R.id.edit);
        editText.setSingleLine(true);
    }

}
