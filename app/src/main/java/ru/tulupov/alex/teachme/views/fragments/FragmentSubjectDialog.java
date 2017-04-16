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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.Subject;


public class FragmentSubjectDialog extends DialogFragment implements DialogInterface.OnClickListener {

    protected SelectSubject listener;
    protected int selectedItem = -1;

    protected List<Subject> listSubject;
    protected int tag = 0;
    protected String[] arrSubjectsTitle;
    protected boolean withOptional = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Fragment parent = getParentFragment();
        if (parent != null) {
            listener = (SelectSubject) parent;
        }

        if (withOptional) {
            String title = getResources().getString(R.string.any_subject);
            listSubject.add(0, new Subject(-1, title));
        }
//        Resources res = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(res.getString(R.string.title_dialog_subject))
//                .setSingleChoiceItems(fromListToArray(listSubject), selectedItem, this)
//                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dismiss();
//                    }
//                });

        arrSubjectsTitle = fromListToArray(listSubject);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_subjects, null, false);
        builder.setView(view);

        ListView listView = (ListView) view.findViewById(R.id.listViewSubjects);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.subject_item, R.id.item_subject, arrSubjectsTitle);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.item_subject);
                String strSubject = tv.getText().toString();

                int pos = indexOfSubject(strSubject);
                if (listener != null) {
                    listener.selectSubject(pos, tag);
                }
                dismiss();
            }
        });
        listView.setAdapter(adapter);

        EditText edtSearch = (EditText) view.findViewById(R.id.edt_search_by_subjects);
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


    @Override
    public void onClick(DialogInterface dialog, int which) {
        listener.selectSubject(which, tag);
        dismiss();
    }

    public void setListSubject(List<Subject> listSubject) {
        this.listSubject = listSubject;
    }

    public void setListOptionalSubject(List<Subject> listSubject) {
        withOptional = true;
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

    protected int indexOfSubject (String subject) {
        for (int i = 0; i < arrSubjectsTitle.length; i++) {
            if (arrSubjectsTitle[i].equals(subject)) {
                return i;
            }
        }

        return 1;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (SelectSubject) activity;
        } catch (ClassCastException ignored) {}
    }
}
