package ru.tulupov.alex.teachme.views.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.List;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.Subject;


public class FragmentSubjectDialog extends DialogFragment implements DialogInterface.OnClickListener {

    protected SelectSubject listener;
    protected int selectedItem = 0;

    protected List<Subject> listSubject;
    protected int tag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        listener = (SelectSubject) getParentFragment();
        Resources res = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(res.getString(R.string.title_dialog_subject))
                .setSingleChoiceItems(fromListToArray(listSubject), selectedItem, this)
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
        listener.selectSubject(which, tag);
        dismiss();
    }

    public void setListSubject(List<Subject> listSubject) {
        this.listSubject = listSubject;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public interface SelectSubject {
        void selectSubject(int subjectIndex, int tag);
    }

    protected String[] fromListToArray(List<Subject> list) {
        String[] arrStr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arrStr[i] = list.get(i).getTitle();
        }

        return arrStr;
    }
}
