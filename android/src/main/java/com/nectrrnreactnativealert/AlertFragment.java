package com.nectrrnreactnativealert;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/** A fragment used to display the dialog. */
public class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {
  private static final int UI_FLAG_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    // Set the content to appear under the system bars so that the
    // content doesn't resize when the system bars hide and show.
    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    // Hide the nav bar and status bar
    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_FULLSCREEN;

  /* package */ static final String ARG_TITLE = "title";
  /* package */ static final String ARG_MESSAGE = "message";
  /* package */ static final String ARG_BUTTON_POSITIVE = "button_positive";
  /* package */ static final String ARG_BUTTON_NEGATIVE = "button_negative";
  /* package */ static final String ARG_BUTTON_NEUTRAL = "button_neutral";
  /* package */ static final String ARG_ITEMS = "items";

  private final @Nullable ReactNativeAlertModule.AlertFragmentListener mListener;

  public AlertFragment() {
    mListener = null;
  }

  @SuppressLint("ValidFragment")
  public AlertFragment(@Nullable ReactNativeAlertModule.AlertFragmentListener listener, Bundle arguments) {
    mListener = listener;
    setArguments(arguments);
  }

  public static Dialog createDialog(
    Context activityContext, Bundle arguments, DialogInterface.OnClickListener fragment) {
    AlertDialog.Builder builder =
      new AlertDialog.Builder(activityContext).setTitle(arguments.getString(ARG_TITLE));

    if (arguments.containsKey(ARG_BUTTON_POSITIVE)) {
      builder.setPositiveButton(arguments.getString(ARG_BUTTON_POSITIVE), fragment);
    }
    if (arguments.containsKey(ARG_BUTTON_NEGATIVE)) {
      builder.setNegativeButton(arguments.getString(ARG_BUTTON_NEGATIVE), fragment);
    }
    if (arguments.containsKey(ARG_BUTTON_NEUTRAL)) {
      builder.setNeutralButton(arguments.getString(ARG_BUTTON_NEUTRAL), fragment);
    }
    // if both message and items are set, Android will only show the message
    // and ignore the items argument entirely
    if (arguments.containsKey(ARG_MESSAGE)) {
      builder.setMessage(arguments.getString(ARG_MESSAGE));
    }
    if (arguments.containsKey(ARG_ITEMS)) {
      builder.setItems(arguments.getCharSequenceArray(ARG_ITEMS), fragment);
    }

    return builder.create();
  }

  @Override
  public void show(FragmentManager manager, String tag) {
    super.show(manager, tag);

    // https://stackoverflow.com/questions/22794049/how-do-i-maintain-the-immersive-mode-in-dialogs
    getFragmentManager().executePendingTransactions();

    getDialog().getWindow().getDecorView().setSystemUiVisibility(UI_FLAG_IMMERSIVE);

    // Make the dialogs window focusable again.
    getDialog().getWindow().clearFlags(
      WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    );
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog ad = createDialog(getActivity(), getArguments(), this);
    ad.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

    return ad;
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    if (mListener != null) {
      mListener.onClick(dialog, which);
    }
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if (mListener != null) {
      mListener.onDismiss(dialog);
    }
  }
}
