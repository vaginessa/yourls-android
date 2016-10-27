package de.mateware.ayourls.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import de.mateware.ayourls.R;
import de.mateware.ayourls.service.ShortUrlService;
import de.mateware.dialog.DialogScrollingCustomView;

/**
 * Created by Mate on 18.10.2015.
 */
public class AddLinkDialog extends DialogScrollingCustomView {

    EditText titleEditText;
    EditText urlEditText;
    EditText keywordEditText;

    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.dialog_addlink, parent,false);
        titleEditText = (EditText) view.findViewById(R.id.title);
        urlEditText = (EditText) view.findViewById(R.id.url);
        keywordEditText = (EditText) view.findViewById(R.id.keyword);
        String url = getArguments().getString(ShortUrlService.EXTRA_URL);
        String title = getArguments().getString(ShortUrlService.EXTRA_TITLE);
        String keyword = getArguments().getString(ShortUrlService.EXTRA_KEYWORD);
        if (!TextUtils.isEmpty(url))
            urlEditText.setText(url);
        if (!TextUtils.isEmpty(title))
            titleEditText.setText(title);
        if (!TextUtils.isEmpty(keyword))
            keywordEditText.setText(keyword);
        return view;
    }

    @Override
    public AppCompatDialog createDialogToReturn() {
        AppCompatDialog dialog = super.createDialogToReturn();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }


    @Override
    public Bundle addArgumentsToDialogAfterButtonClick(Bundle dialogArguments, int which) {
        String url = urlEditText.getText().toString();
        String title = titleEditText.getText().toString();
        String keyword = keywordEditText.getText().toString();

        if (!TextUtils.isEmpty(url))
            dialogArguments.putString(ShortUrlService.EXTRA_URL,url);
        if (!TextUtils.isEmpty(title))
            dialogArguments.putString(ShortUrlService.EXTRA_TITLE,title);
        if (!TextUtils.isEmpty(keyword))
            dialogArguments.putString(ShortUrlService.EXTRA_KEYWORD,keyword);

        return dialogArguments;
    }



    public static class Builder extends AbstractBuilder<Builder,AddLinkDialog> {
        public Builder() {
            super(AddLinkDialog.class);
        }
    }
}

