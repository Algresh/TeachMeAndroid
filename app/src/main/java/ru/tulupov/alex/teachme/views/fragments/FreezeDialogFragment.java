package ru.tulupov.alex.teachme.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import ru.tulupov.alex.teachme.R;

public class FreezeDialogFragment extends DialogFragment {

    public interface FreezeListener {
        void freezeTeacher();
    }

    private FreezeListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Resources res = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(res.getString(R.string.title_dialog_freeze))
                .setPositiveButton(res.getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.freezeTeacher();
                    }
                })
                .setNegativeButton(res.getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        listener = (FreezeListener) context;
        super.onAttach(context);
    }
}
