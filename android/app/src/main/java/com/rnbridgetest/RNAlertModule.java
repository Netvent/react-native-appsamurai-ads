package com.rnbridgetest;

import android.app.AlertDialog;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import javax.annotation.Nonnull;

public class RNAlertModule extends ReactContextBaseJavaModule {
    @Nonnull
    @Override
    public String getName() {
        return "Alert";
    }

    public RNAlertModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void show(String title, String message, String positiveBtnText, String negativeBtnText, Callback onPositiveCallback, Callback onNegativeCallback) {
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
