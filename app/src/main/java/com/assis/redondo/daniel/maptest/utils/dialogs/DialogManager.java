package com.assis.redondo.daniel.maptest.utils.dialogs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;

import com.assis.redondo.daniel.maptest.R;


public class DialogManager {


    private Context context;
    private AlertDialog.Builder mLoginDialog;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder mApiErrorDialog;

    public DialogManager(Context context){
        this.context = context;
    }

    public void showProgressDialog(Context context){

       Context ctx = context;
        this.context = context;
        if(ctx!=null) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(ctx);

                ProgressBar progressBar = new ProgressBar(this.context);
                progressBar.getIndeterminateDrawable().setColorFilter(this.context.getResources().getColor(R.color.progress_bar_custom_color),
                        android.graphics.PorterDuff.Mode.SRC_ATOP);
                mProgressDialog.setIndeterminateDrawable(progressBar.getIndeterminateDrawable());
                mProgressDialog.setMessage("Carregando taxistas...");

                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        }
    }

    public void dismissProgressDialog(){
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showErrorFromApi() {

        final Context ctx = context;
        if(ctx!=null) {

            if (mApiErrorDialog == null) {
                mApiErrorDialog = new AlertDialog.Builder(ctx, R.style.MyAlertDialogStyle);
                mApiErrorDialog.setTitle("Atenção");
                mApiErrorDialog.setMessage("Ocorreu um erro ao tentar carregar as localizações dos taxistas, tente novamente.");


                mApiErrorDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mApiErrorDialog = null;
                    }
                });

                mApiErrorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mApiErrorDialog = null;
                    }
                });

                mApiErrorDialog.show();
            }
        }
    }
}
