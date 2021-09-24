package com.photozig.videos.view.custom;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.photozig.videos.interfaces.IAlertDialogListener;

public class CustomActivity extends AppCompatActivity {

    private final CustomAlertDialog alertDialog = new CustomAlertDialog();
    private Toast toast;

    protected void setupActionBar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected void showAlertDialog(
            String message,
            String positiveButtonText,
            String negativeButtonText,
            IAlertDialogListener listener) {

        alertDialog.showDialog(
                this,
                message,
                positiveButtonText,
                negativeButtonText,
                listener);
    }

    public void showMessage(String message) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void openNewScreen(Intent intent) {
        startActivity(intent);
    }

    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void requestPermissions(int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }
}