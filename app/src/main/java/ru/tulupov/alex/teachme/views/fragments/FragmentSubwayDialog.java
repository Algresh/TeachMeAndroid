package ru.tulupov.alex.teachme.views.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.Subway;


public class FragmentSubwayDialog extends DialogFragment implements DialogInterface.OnMultiChoiceClickListener {

    protected SelectSubway listener;
    protected boolean[] arrSelected;

    protected List<Subway> listSubways;
    protected List<Integer> listSelected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (listSelected == null) {
            listSelected = new ArrayList<>();
        }

        Fragment parent = getParentFragment();
        if (parent != null) {
            listener = (SelectSubway) parent;
        }
        Resources res = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(res.getString(R.string.title_dialog_subway))
                .setMultiChoiceItems(fromListToArray(listSubways), arrSelected, this)
                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton(res.getString(R.string.select), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.selectSubway(listSelected);
                        dismiss();
                    }
                });

        return builder.create();

    }

    public void setListSubways(List<Subway> listSubways) {
        this.listSubways = listSubways;
    }

    public void setArrSelected(boolean[] arrSelected) {
        this.arrSelected = arrSelected;
    }

    public void setListSelected(List<Integer> listSelected) {
        this.listSelected = listSelected;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
        if (b) {
            listSelected.add(i);
        } else {
            int index = listSelected.indexOf(i);
            if (index >= 0) {
                listSelected.remove(index);
            }
        }
    }

    public interface SelectSubway {
        void selectSubway(List<Integer> listSelected);
    }

    protected String[] fromListToArray(List<Subway> list) {
        List<String> listStr = new ArrayList<>(list.size());
        for (Subway subway: list) {
            listStr.add(subway.getTitle());
        }

        String[] stockArr = new String[listStr.size()];
        stockArr = listStr.toArray(stockArr);

        return stockArr;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (SelectSubway) activity;
        } catch (ClassCastException ignored) {}
    }
}
