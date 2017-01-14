package ru.tulupov.alex.teachme.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.PriceList;
import ru.tulupov.alex.teachme.models.Subject;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;


public class RegTeacherSubjectsFragment extends Fragment implements View.OnClickListener,
        ShowSubject ,FragmentSubjectDialog.SelectSubject {

    protected View fragmentView;
    protected LinearLayout subjectsContainer;
    protected int numSubjects = 0;
    private CitySubjectPresenter presenter;
    private List<Subject> listSubjects;
    private List<View> viewList;
    private List<PriceList> priceLists;
    private List<Integer> selectedListSubjects;


    private void addSubject() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View viewSubject = layoutInflater.inflate(R.layout.reg_teacher_subject, null);
        TextView tvSubjects = (TextView) viewSubject.findViewById(R.id.sp_select_subject);
        tvSubjects.setTag(numSubjects);
        tvSubjects.setOnClickListener(this);

        viewList.add(viewSubject);
        subjectsContainer.addView(viewSubject);
        priceLists.add(new PriceList());
        numSubjects++;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter = new CitySubjectPresenter();
        viewList = new ArrayList<>();
        priceLists = new ArrayList<>();
        selectedListSubjects = new ArrayList<>();
        presenter.onCreate(null, this);
        fragmentView = inflater.inflate(R.layout.fragment_reg_teacher_subjects, container, false);
        subjectsContainer = (LinearLayout) fragmentView.findViewById(R.id.ll_subjects);
        fragmentView.findViewById(R.id.btn_reg_teacher_add_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubject();
            }
        });
        addSubject();
        return fragmentView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sp_select_subject) {
            Integer tag = (Integer) view.getTag();
            presenter.getListSubjects(tag);
        }
    }

    @Override
    public void showSubjects(List<Subject> list, int tag) {
        listSubjects = list;
        FragmentSubjectDialog dialog = new FragmentSubjectDialog();
        dialog.setListSubject(list);
        dialog.setTag(tag);
        Integer selectItem;
        Log.d(Constants.MY_TAG, "showSubjects " + tag);
        if(selectedListSubjects.size() > tag) {
            selectItem = selectedListSubjects.get(tag);
        } else {
            selectItem = 0;
        }

        dialog.setSelectedItem(selectItem);
        FragmentManager manager = getChildFragmentManager();
        dialog.show(manager, "subjects");
    }

    @Override
    public void selectSubject(int subjectIndex, int tag) {
        View view = viewList.get(tag);
        TextView tvSubject = (TextView) view.findViewById(R.id.sp_select_subject);
        Subject subject = listSubjects.get(subjectIndex);
        tvSubject.setText(subject.getTitle());
        priceLists.get(tag).setSubject(subject);
        selectedListSubjects.add(tag, subjectIndex);
    }
}
