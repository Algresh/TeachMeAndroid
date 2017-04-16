package ru.tulupov.alex.teachme.views.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import ru.tulupov.alex.teachme.R;


public class ExperienceDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private int selectedItem = 0;
    private SelectExperience listener;
    private int tagItem;

    protected boolean withOptional = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Fragment parent = getParentFragment();
        if (parent != null) {
            listener = (SelectExperience) parent;
        }
        Resources res = getActivity().getResources();

        String[] arrExp;
        if (withOptional) {
            arrExp = res.getStringArray(R.array.typeExperienceOptional);
        } else {
            arrExp = res.getStringArray(R.array.typeExperience);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(res.getString(R.string.btn_select_promotion))
                .setSingleChoiceItems(arrExp, selectedItem, this)
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
            listener.selectExperience(which, tagItem);
        }
        dismiss();
    }

    public void setWithOptional(boolean withOptional) {
        this.withOptional = withOptional;
    }

    public int getTagItem() {
        return tagItem;
    }

    public void setTagItem(int tagItem) {
        this.tagItem = tagItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public interface SelectExperience {
        void selectExperience(int item, int tagItem);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (SelectExperience) activity;
        } catch (ClassCastException ignored) {}
    }
}
