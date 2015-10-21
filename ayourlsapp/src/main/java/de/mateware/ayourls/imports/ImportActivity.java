package de.mateware.ayourls.imports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import de.mateware.ayourls.R;
import de.mateware.ayourls.dialog.Dialog;
import de.mateware.ayourls.dialog.DialogIndeterminateProgress;
import de.mateware.ayourls.yourslapi.YourlsError;

/**
 * Created by mate on 21.10.2015.
 */
public class ImportActivity extends AppCompatActivity implements ImportWorkerFragment.ImportWorkerCallback {

    ImportWorkerFragment workerFragment;

    private static final String TAG_DIALOG_WAIT = "dialogWait";
    private static final String TAG_DIALOG_ERROR = "dialogError";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        workerFragment = ImportWorkerFragment.findOrCreateFragment(getSupportFragmentManager(), this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState == null) {

        }

    }

    @Override
    public void showWaitDialog() {
        new DialogIndeterminateProgress().withMessage(R.string.dialog_check_server_message)
                                         .withCancelable(false)
                                         .show(getSupportFragmentManager(), TAG_DIALOG_WAIT);
    }

    @Override
    public void hideWaitDialog() {
        DialogIndeterminateProgress.dismissDialog(getSupportFragmentManager(),TAG_DIALOG_WAIT);
    }


    @Override
    public void onNetworkError(YourlsError error) {
        new Dialog().withTitle(R.string.dialog_error_title)
                    .withMessage(getString(R.string.dialog_error_message, error.getMessage()))
                    .withPositiveButton()
                    .show(getSupportFragmentManager(), TAG_DIALOG_ERROR);
    }
}
