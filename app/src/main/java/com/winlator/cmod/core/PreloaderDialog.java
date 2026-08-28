package com.winlator.cmod.core;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.winlator.cmod.R;

public class PreloaderDialog {
    private final Activity activity;
    private Dialog dialog;

    public PreloaderDialog(Activity activity) {
        this.activity = activity;
    }

    private void create() {
        if (dialog != null) return;
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.preloader_dialog);

        boolean isDarkMode = androidx.preference.PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("dark_mode", true);
        if (isDarkMode) {
            dialog.findViewById(R.id.LLPreloaderBackground).setBackgroundResource(R.drawable.content_dialog_background_dark);
            ((TextView)dialog.findViewById(R.id.TextView)).setTextColor(androidx.core.content.ContextCompat.getColor(activity, R.color.colorAccent));
        }

        Window window = dialog.getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    public synchronized void show(int textResId) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed() || isShowing()) return;
        close();
        try {
            if (dialog == null) create();
            if (dialog != null) {
                TextView tv = dialog.findViewById(R.id.TextView);
                if (tv != null) tv.setText(textResId);
                dialog.show();
            }
        }
        catch (Throwable t) {}
    }

    public synchronized void show(String text) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed() || isShowing()) return;
        close();
        try {
            if (dialog == null) create();
            if (dialog != null) {
                TextView tv = dialog.findViewById(R.id.TextView);
                if (tv != null) tv.setText(text);
                dialog.show();
            }
        }
        catch (Throwable t) {}
    }

    public void showOnUiThread(final int textResId) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        activity.runOnUiThread(() -> show(textResId));
    }

    public void showOnUiThread(final String text) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        activity.runOnUiThread(() -> show(text));
    }

    public synchronized void close() {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
        catch (Exception e) {}
    }

    public void closeOnUiThread() {
        activity.runOnUiThread(this::close);
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}
