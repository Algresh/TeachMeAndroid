package ru.tulupov.alex.teachme.views.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;


public class FragmentCityDialog  extends DialogFragment implements DialogInterface.OnClickListener {

    protected SelectCity listener;
    protected int selectedItem = 0;

    protected List<City> listCities;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Fragment parent = getParentFragment();
        if (parent != null) {
            listener = (SelectCity) parent;
        }
        Resources res = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(res.getString(R.string.title_dialog_city))
                .setSingleChoiceItems(fromListToArray(listCities), selectedItem, this)
                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();

    }

    public void setListCities(List<City> listCities) {
        this.listCities = listCities;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (listener != null) {
            listener.selectCity(which);
        }
        dismiss();
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public interface SelectCity {
        void selectCity(int location);
    }

    protected String[] fromListToArray(List<City> list) {
        List<String> listStr = new ArrayList<>(list.size());
        for (City city: list) {
            listStr.add(city.getTitle());
        }

        String[] stockArr = new String[listStr.size()];
        stockArr = listStr.toArray(stockArr);

        return stockArr;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
          listener = (SelectCity) activity;
        } catch (ClassCastException ignored) {}
    }
}
