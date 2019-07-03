package com.rnbridgetest

import android.app.AlertDialog

import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class RNAlertModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "Alert"
    }

    @ReactMethod
    fun show(title: String, message: String, positiveBtnText: String, negativeBtnText: String, onPositiveCallback: Callback, onNegativeCallback: Callback) {
        val builder = AlertDialog.Builder(currentActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(true)

        builder.setPositiveButton(
                positiveBtnText
        ) { dialog, id ->
            dialog.cancel()
            onPositiveCallback.invoke()
        }

        builder.setNegativeButton(
                negativeBtnText
        ) { dialog, id ->
            dialog.cancel()
            onNegativeCallback.invoke()
        }

        val alert = builder.create()
        alert.show()
    }
}
