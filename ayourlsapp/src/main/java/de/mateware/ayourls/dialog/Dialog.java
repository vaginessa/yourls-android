package de.mateware.ayourls.dialog;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Dialog extends DialogFragment {

    static final String ARG_INT_TITLE = "title_resid";
    static final String ARG_STRING_TITLE = "title_text";

    static final String ARG_INT_MESSAGE = "message_resid";
    static final String ARG_STRING_MESSAGE = "message";

    static final String ARG_INT_ICONID = "icon_id";
//    static final String ARG_INT_STYLEID = "style";

    static final String ARG_INT_BUTTONTEXTPOSITIVE = "positive_button_resid";
    static final String ARG_INT_BUTTONTEXTNEGATIVE = "negative_button_resid";
    static final String ARG_INT_BUTTONTEXTNEUTRAL = "neutral_button_resid";
    static final String ARG_STRING_BUTTONTEXTPOSITIVE = "positive_button_text";
    static final String ARG_STRING_BUTTONTEXTNEGATIVE = "negative_button_text";
    static final String ARG_STRING_BUTTONTEXTNEUTRAL = "neutral_button_text";

    public final static int BUTTON_POSITIVE = DialogInterface.BUTTON_POSITIVE;
    public final static int BUTTON_NEUTRAL = DialogInterface.BUTTON_NEUTRAL;
    public final static int BUTTON_NEGATIVE = DialogInterface.BUTTON_NEGATIVE;

    public Bundle args = new Bundle();
    DialogButtonListener buttonListener;
    DialogDismissListener dismissListener;
    DialogCancelListener cancelListener;

    public static Logger log = LoggerFactory.getLogger(Dialog.class);


    @Override
    public void show(@NonNull FragmentManager manager, String tag) {
        this.setArguments(args);
        super.show(manager, tag);
    }

    @Override
    public int show(@NonNull FragmentTransaction transaction, String tag) {
        this.setArguments(args);
        return super.show(transaction, tag);
    }

    public Dialog withTitle(String title) {
        args.putString(ARG_STRING_TITLE, title);
        return this;
    }

    public Dialog withTitle(int resId) {
        args.putInt(ARG_INT_TITLE, resId);
        return this;
    }

    public Dialog withMessage(String message) {
        args.putString(ARG_STRING_MESSAGE, message);
        return this;
    }

    public Dialog withMessage(int resId) {
        args.putInt(ARG_INT_MESSAGE, resId);
        return this;
    }

    public Dialog withPositiveButton(String text) {
        args.putString(ARG_STRING_BUTTONTEXTPOSITIVE, text);
        return this;
    }

    public Dialog withPositiveButton(int resId) {
        args.putInt(ARG_INT_BUTTONTEXTPOSITIVE, resId);
        return this;
    }

    public Dialog withPositiveButton() {
        return withPositiveButton(android.R.string.ok);
    }

    public Dialog withNeutralButton(String text) {
        args.putString(ARG_STRING_BUTTONTEXTNEUTRAL, text);
        return this;
    }

    public Dialog withNeutralButton(int resId) {
        args.putInt(ARG_INT_BUTTONTEXTNEUTRAL, resId);
        return this;
    }

    public Dialog withNeutralButton() {
        return withNeutralButton(android.R.string.untitled);
    }

    public Dialog withNegativeButton(String text) {
        args.putString(ARG_STRING_BUTTONTEXTNEGATIVE, text);
        return this;
    }

    public Dialog withNegativeButton(int resId) {
        args.putInt(ARG_INT_BUTTONTEXTNEGATIVE, resId);
        return this;
    }

    public Dialog withNegativeButton() {
        return withNegativeButton(android.R.string.cancel);
    }

    public Dialog withIcon(int resId) {
        args.putInt(ARG_INT_ICONID, resId);
        return this;
    }

    public Dialog withCancelable(boolean cancelable) {
        this.setCancelable(cancelable);
        return this;
    }

    public Dialog withBundle(Bundle bundle) {
        args.putAll(bundle);
        return this;
    }

    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            log.trace("Button", which);
            Bundle additionalArguments = new Bundle();
            args.putAll(additionalArgumentsOnClick(additionalArguments,which));
            if (buttonListener != null) buttonListener.onDialogClick(getTag(), Dialog.this.getArguments(), which);
            else log.info(DialogButtonListener.class.getSimpleName() + " not set in Activity " + getActivity().getClass()
                                                                                                              .getSimpleName());
        }
    };

    AlertDialog.Builder builder;



    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        log.trace(this.getTag());
        builder = new AlertDialog.Builder(getContext());

        if (hasIcon()) builder.setIcon(getIcon());

        if (hasTitle()) builder.setTitle(getTitle());

        setDialogContent();

        if (hasPositiveButton()) builder.setPositiveButton(getPositiveButton(), onClickListener);

        if (hasNeutralButton()) builder.setNeutralButton(getNeutralButton(), onClickListener);

        if (hasNegativeButton()) builder.setNegativeButton(getNegativeButton(), onClickListener);

        return createDialogToReturn();
    }

    void setDialogContent() {
        if (hasMessage()) builder.setMessage(getMessage());
    }

    AppCompatDialog createDialogToReturn() {
        return builder.create();
    }

    public Bundle additionalArgumentsOnClick(Bundle additionalArgs, int which) {
        return additionalArgs;
    }


    protected boolean hasTitle() {
        return (getArguments().containsKey(ARG_STRING_TITLE) || getArguments().containsKey(ARG_INT_TITLE));
    }

    protected String getTitle() {
        return getText(ARG_STRING_TITLE, ARG_INT_TITLE);
    }

    protected boolean hasMessage() {
        return (getArguments().containsKey(ARG_STRING_MESSAGE) || getArguments().containsKey(ARG_INT_MESSAGE));
    }

    protected String getMessage() {
        return getText(ARG_STRING_MESSAGE, ARG_INT_MESSAGE);
    }

    protected boolean hasIcon() {
        return (getArguments().containsKey(ARG_INT_ICONID));
    }

    protected int getIcon() {
        return getArguments().getInt(ARG_INT_ICONID);
    }

    protected boolean hasPositiveButton() {
        return (getArguments().containsKey(ARG_STRING_BUTTONTEXTPOSITIVE) || getArguments().containsKey(ARG_INT_BUTTONTEXTPOSITIVE));
    }

    protected String getPositiveButton() {
        return getText(ARG_STRING_BUTTONTEXTPOSITIVE, ARG_INT_BUTTONTEXTPOSITIVE);
    }

    protected boolean hasNegativeButton() {
        return (getArguments().containsKey(ARG_STRING_BUTTONTEXTNEGATIVE) || getArguments().containsKey(ARG_INT_BUTTONTEXTNEGATIVE));
    }

    protected String getNegativeButton() {
        return getText(ARG_STRING_BUTTONTEXTNEGATIVE, ARG_INT_BUTTONTEXTNEGATIVE);
    }

    protected boolean hasNeutralButton() {
        return (getArguments().containsKey(ARG_STRING_BUTTONTEXTNEUTRAL) || getArguments().containsKey(ARG_INT_BUTTONTEXTNEUTRAL));
    }

    protected String getNeutralButton() {
        return getText(ARG_STRING_BUTTONTEXTNEUTRAL, ARG_INT_BUTTONTEXTNEUTRAL);
    }

    protected String getText(String arg_string, String arg_int) {
        String result = null;
        if (getArguments().containsKey(arg_string)) result = getArguments().getString(arg_string);
        else if (getArguments().containsKey(arg_int)) result = getString(getArguments().getInt(arg_int));
        return result;
    }


    @Override
    public void onAttach(Activity activity) {
        log.trace(this.getTag());
        super.onAttach(activity);
        try {
            buttonListener = (DialogButtonListener) activity;
        } catch (ClassCastException e) {
            log.warn(e.getMessage());
        }
        try {
            dismissListener = (DialogDismissListener) activity;
        } catch (ClassCastException e) {
            log.warn(e.getMessage());
        }
        try {
            cancelListener = (DialogCancelListener) activity;
        } catch (ClassCastException e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getTag() != null) {
            log.trace(getTag());
            if (dismissListener != null) dismissListener.onDialogDismiss(getTag(), Dialog.this.getArguments());
            else log.info(DialogDismissListener.class.getSimpleName() + " not set in Activity " + getActivity().getClass()
                                                                                                               .getSimpleName());
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (getTag() != null) {
            log.trace(getTag());
            if (cancelListener != null) cancelListener.onDialogCancel(getTag(), Dialog.this.getArguments());
            else log.info(DialogCancelListener.class.getSimpleName() + " not set in Activity " + getActivity().getClass()
                                                                                                              .getSimpleName());
        }
        super.onCancel(dialog);
    }

    public interface DialogButtonListener {

        public void onDialogClick(String tag, Bundle arguments, int which);
    }

    public interface DialogDismissListener {
        public void onDialogDismiss(String tag, Bundle arguments);
    }

    public interface DialogCancelListener {
        public void onDialogCancel(String tag, Bundle arguments);
    }

    public static void dismissDialog(FragmentManager fm, String dialogTag) {
        log.trace(dialogTag);
        DialogFragment dialog = (DialogFragment) fm.findFragmentByTag(dialogTag);
        if (dialog != null) dialog.dismiss();
    }
}
