package com.photozig.videos.view.custom;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.photozig.videos.interfaces.IAlertDialogListener;

public class CustomAlertDialog {

    private AlertDialog alertDialog;

    public void showDialog(
            Context context,
            String message,
            String positiveButtonText,
            String negativeButtonText,
            IAlertDialogListener listener) {

        if (alertDialog != null) alertDialog.dismiss();

        alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, (dialog, id) -> {
                    dialog.cancel();
                    listener.onClickPositiveButton();
                })
                .setNegativeButton(negativeButtonText, (dialog, id) -> {
                    dialog.cancel();
                    listener.onClickNegativeButton();
                })
                .create();

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }


}
