package com.rnbridgetest;

import android.app.AlertDialog;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import javax.annotation.Nonnull;

public class AlertModule extends ReactContextBaseJavaModule {
    @Nonnull
    @Override
    public String getName() {
        return "AlertModule";
    }

    public AlertModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void showAlert(String title, String message, String positiveBtnText, String negativeBtnText, Callback onPositiveCallback, Callback onNegativeCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getCurrentActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                positiveBtnText,
                (dialog, id) -> {
                    dialog.cancel();
                    onPositiveCallback.invoke();
                });

        builder.setNegativeButton(
                negativeBtnText,
                (dialog, id) -> {
                    dialog.cancel();
                    onNegativeCallback.invoke();
                });

        AlertDialog alert = builder.create();
        alert.show();

    }
}
