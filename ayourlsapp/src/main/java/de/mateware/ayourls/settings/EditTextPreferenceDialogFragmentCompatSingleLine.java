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

    private static final String ARG_INPUTTYPE = "inputType";

    public static EditTextPreferenceDialogFragmentCompatSingleLine newInstance(String key, int inputTypeFlags) {
        final EditTextPreferenceDialogFragmentCompatSingleLine fragment = new EditTextPreferenceDialogFragmentCompatSingleLine();
        final Bundle b = new Bundle(2);
        b.putString(ARG_KEY, key);
        b.putInt(ARG_INPUTTYPE,inputTypeFlags);
        fragment.setArguments(b);
        return fragment;
    }

    public static EditTextPreferenceDialogFragmentCompatSingleLine newInstance(String key) {
        return EditTextPreferenceDialogFragmentCompatSingleLine.newInstance(key,0);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = (EditText) view.findViewById(android.R.id.edit);
        int inputTypeFlags = getArguments().getInt(ARG_INPUTTYPE,0);
        if (inputTypeFlags != 0){
            editText.setInputType(inputTypeFlags);
        }
        editText.setSingleLine(true);
    }

}
