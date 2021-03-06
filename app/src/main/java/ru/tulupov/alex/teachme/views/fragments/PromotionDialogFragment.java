package ru.tulupov.alex.teachme.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import ru.tulupov.alex.teachme.R;


public class PromotionDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private int selectedItem = 0;
    private SelectPromotion listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        listener = (SelectPromotion) getParentFragment();
        Resources res = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(res.getString(R.string.btn_select_promotion))
                .setSingleChoiceItems(res.getStringArray(R.array.promotion), selectedItem, this)
                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (listener != null) {
            listener.selectPromotion(which);
        }
        dismiss();
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public interface SelectPromotion {
        void selectPromotion(int item);
    }
}
