package com.skytel.sdp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.skytel.sdp.R;

/**
 * Created by altanchimeg on 5/19/2016.
 */
public class ConfirmDialog extends DialogFragment {

    OnDialogConfirmListener mListener;

    public interface OnDialogConfirmListener {
        public void onDialogConfirm();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDialogConfirmListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDialogConfirmListener");
        }
    }

    public static ConfirmDialog newInstance(int title, int message) {
        ConfirmDialog frag = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("message",message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        int message = getArguments().getInt("message");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_info)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mListener.onDialogConfirm();
                            }
                        }
                )
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }
                )
                .create();
    }

}
