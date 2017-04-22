package ru.tulupov.alex.teachme.views.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    protected String[] arrCitiesTitle;
    protected boolean withOptional = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Fragment parent = getParentFragment();
        if (parent != null) {
            listener = (SelectCity) parent;
        }

        if (withOptional) {
            String title = getResources().getString(R.string.any_city);
            if (listCities.get(0).getId() != -1) {
                listCities.add(0, new City(-1, title, false));
            }
        }

//        Resources res = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(res.getString(R.string.title_dialog_city))
//                .setSingleChoiceItems(fromListToArray(listCities), selectedItem, this)
//                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dismiss();
//                    }
//                });

        arrCitiesTitle = fromListToArray(listCities);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_cities, null, false);
        builder.setView(view);

        ListView listView = (ListView) view.findViewById(R.id.listViewCities);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.city_item, R.id.item_city, arrCitiesTitle);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.item_city);
                String strCity = tv.getText().toString();

                int pos = indexOfCity(strCity);
                if (listener != null) {
                    listener.selectCity(pos);
                }
                dismiss();
            }
        });
        listView.setAdapter(adapter);

        EditText edtSearch = (EditText) view.findViewById(R.id.edt_search_by_cities);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return builder.create();

    }

    public void setListCities(List<City> listCities) {
        this.listCities = listCities;
    }

    public void setListOptionalCity(List<City> listCities) {
        withOptional = true;
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

    protected int indexOfCity (String city) {
        for (int i = 0; i < arrCitiesTitle.length; i++) {
            if (arrCitiesTitle[i].equals(city)) {
                return i;
            }
        }

        return 1;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
          listener = (SelectCity) activity;
        } catch (ClassCastException ignored) {}
    }
}
