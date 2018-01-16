package com.balaji.notehomelane.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by balaji on 16/01/18.
 */

public class NoteAbstractActivity extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    protected void showAlert(String positive, String negative, String msg, final
    AlertDialogResponse alertDialogResponse) {
        if(alertDialogBuilder == null) {
            alertDialogBuilder = new AlertDialog.Builder(NoteAbstractActivity.this);
        }
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(positive, new DialogInterface
                .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogResponse.onPositive(dialog);
                    }
                });
        alertDialogBuilder.setNegativeButton(negative, new DialogInterface
                .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogResponse.onNegative(dialog);
                    }
                });
        alertDialogBuilder.show();
    }

    protected interface AlertDialogResponse {
        void onPositive(DialogInterface dialog);
        void onNegative(DialogInterface dialog);
    }

    protected void showProgressDialog(String msg) {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setCancelable(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
