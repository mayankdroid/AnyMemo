package org.liberty.android.fantastischmemo.downloader.dropbox;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.liberty.android.fantastischmemo.R;
import org.liberty.android.fantastischmemo.common.BaseActivity;
import org.liberty.android.fantastischmemo.ui.FileBrowserFragment;
import org.liberty.android.fantastischmemo.utils.AMGUIUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class UploadDropboxActivity extends BaseActivity {

    public static final String EXTRA_AUTH_TOKEN = "authToken";

    private String authToken;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.upload_dropbox_screen);
    }

    private FileBrowserFragment.OnFileClickListener fileClickListener = new FileBrowserFragment.OnFileClickListener() {
        @Override
        public void onClick(File file) {
            showUploadDialog(file);
        }
    };

    private void showUploadDialog(final File file) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.upload_text)
            .setMessage(String.format(getString(R.string.dropbox_upload_text), file.getName()))
            .setPositiveButton(R.string.ok_text,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    UploadTask task = new UploadTask();
                    task.execute(file);
                    setResult(Activity.RESULT_OK);
                }
             }).setNegativeButton(R.string.cancel_text, null).show();
    }


    private void uploadToDropbox(File file) throws IOException, JSONException {
    }

    private class UploadTask extends AsyncTask<File, Void, Exception> {
        private ProgressDialog progressDialog;

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UploadDropboxActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle(getString(R.string.loading_please_wait));
            progressDialog.setMessage(getString(R.string.upload_wait));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        public Exception doInBackground(File... files) {
            try {
                uploadToDropbox(files[0]);
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        public void onPostExecute(Exception e){
            if (e == null) {
                new AlertDialog.Builder(UploadDropboxActivity.this)
                .setTitle(R.string.successfully_uploaded_text)
                .setMessage(R.string.dropbox_successfully_uploaded_message)
                .setPositiveButton(R.string.ok_text, null)
                .show();
            } else {
                AMGUIUtility.displayException(UploadDropboxActivity.this, getString(R.string.error_text), getString(R.string.error_text), e);
            }

            progressDialog.dismiss();
        }
    }

}