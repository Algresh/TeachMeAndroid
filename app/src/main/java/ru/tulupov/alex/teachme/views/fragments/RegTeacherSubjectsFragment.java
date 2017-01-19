package ru.tulupov.alex.teachme.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import ru.tulupov.alex.teachme.models.TeacherRegistration;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;


public class RegTeacherSubjectsFragment extends Fragment implements View.OnClickListener,
        ShowSubject ,FragmentSubjectDialog.SelectSubject, RegDataCorrect {

    protected View fragmentView;
    protected LinearLayout subjectsContainer;
    protected int numSubjects = 0;
    private CitySubjectPresenter presenter;
    private List<Subject> listSubjects;
    private List<View> viewList;
    private List<PriceList> priceLists;
    private List<Integer> selectedListSubjects;
    private boolean subjectDialogIsDownloading = false;


    private void addSubject() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View viewSubject = layoutInflater.inflate(R.layout.reg_teacher_subject, null);
        TextView tvSubjects = (TextView) viewSubject.findViewById(R.id.sp_select_subject);

        String strTvSubject = getContext().getResources().getString(R.string.hint_subject);
        tvSubjects.setTag(numSubjects);
        tvSubjects.setText(strTvSubject);
        tvSubjects.setOnClickListener(this);

        viewList.add(viewSubject);
        subjectsContainer.addView(viewSubject);
//        priceLists.add(new PriceList());
        numSubjects++;
    }

    private void addSubjects() {
//        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

//        for (int i = 0; i < priceLists.size(); i++) {
//            View viewSubject = layoutInflater.inflate(R.layout.reg_teacher_subject, null);
//            TextView tvSubjects = (TextView) viewSubject.findViewById(R.id.sp_select_subject);
//            EditText edtExp = (EditText) viewSubject.findViewById(R.id.et_reg_teacher_experience);
//            EditText edtPrice = (EditText) viewSubject.findViewById(R.id.et_reg_teacher_price);
//            tvSubjects.setTag(numSubjects);
//            tvSubjects.setOnClickListener(this);
//
//            PriceList priceList = priceLists.get(i);
//            tvSubjects.setText(priceList.getSubject().getTitle());
//            edtExp.setText(priceList.getExperience());
//            edtPrice.setText(String.valueOf(priceList.getPrice()));
//
//            viewList.add(viewSubject);
//            subjectsContainer.addView(viewSubject);
//            selectedListSubjects.add(priceList.getSubject().getId());
//            numSubjects++;
//        }

        for(View view : viewList) {
            subjectsContainer.addView(view);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (priceLists == null) {
            presenter = new CitySubjectPresenter();
            viewList = new ArrayList<>();
            selectedListSubjects = new ArrayList<>();
        }
//        numSubjects = 0;
        presenter.onCreate(null, this, null);
        fragmentView = inflater.inflate(R.layout.fragment_reg_teacher_subjects, container, false);
        subjectsContainer = (LinearLayout) fragmentView.findViewById(R.id.ll_subjects);
        fragmentView.findViewById(R.id.btn_reg_teacher_add_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedListSubjects.size() == viewList.size()) {
                    addSubject();
                }
            }
        });
        if(priceLists == null) {
            addSubject();
        } else {
            addSubjects();
        }
        return fragmentView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sp_select_subject && !subjectDialogIsDownloading) {
            subjectDialogIsDownloading = true;
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
        subjectDialogIsDownloading = false;
    }

    @Override
    public void selectSubject(int subjectIndex, int tag) {
        View view = viewList.get(tag);
        TextView tvSubject = (TextView) view.findViewById(R.id.sp_select_subject);
        Subject subject = listSubjects.get(subjectIndex);
        tvSubject.setText(subject.getTitle());
        selectedListSubjects.add(subjectIndex);
    }

    @Override
    public boolean dataIsCorrect() {
        boolean isCorrect = true;

//        String strAbout = editTextAbout.getText().toString();
//        if (strAbout.length() < 2) {
//            isCorrect = false;
//        }

        List<PriceList> priceLists = new ArrayList<>();

        int numEmptyBlock = 0;

        for(int i = 0; i < viewList.size(); i++) {
            int numEmptyField = 0;
            int numWarning = 0;

            EditText edtExp = (EditText) viewList.get(i).findViewById(R.id.et_reg_teacher_experience);
            EditText edtPrice = (EditText) viewList.get(i).findViewById(R.id.et_reg_teacher_price);
            TextView tvSubject = (TextView) viewList.get(i).findViewById(R.id.sp_select_subject);
            String strSbj = tvSubject.getText().toString();
            String strExp = edtExp.getText().toString().trim();
            String strPrice = edtPrice.getText().toString().trim();

            if (strExp.length() == 0 || !strExp.matches(".*\\d.*")) {
                warningColorEditText(edtExp);
                numWarning++;
            } else {
                correctColorEditText(edtExp);
            }


            if (strPrice.length() == 0 || !strPrice.matches(".*\\d.*")) {
                warningColorEditText(edtPrice);
                numWarning++;
            } else {
                correctColorEditText(edtPrice);
            }

            if (strExp.length() == 0) {
                numEmptyField++;
            }
            if (strPrice.length() == 0) {
                numEmptyField++;
            }


            String textSbj = getResources().getString(R.string.hint_subject);
            if(strSbj.equals(textSbj)) {
                warningColorTextView(tvSubject);
                numEmptyField++;
                numWarning++;
            } else {
                correctColorTextView(tvSubject);
            }

            if (numEmptyField == 3) {
                numEmptyBlock++;
                correctColorTextView(tvSubject);
                correctColorEditText(edtPrice);
                correctColorEditText(edtExp);
            } else {
                if (numWarning > 0) {
                    isCorrect = false;
                } else {
                    PriceList priceList = new PriceList();
                    priceList.setExperience(strExp);
                    priceList.setPrice(Integer.parseInt(strPrice));
                    Integer item = selectedListSubjects.get(i - numEmptyBlock);
                    priceList.setSubject(listSubjects.get(item));
                    priceLists.add(priceList);
                }
            }
        }

        if (numEmptyBlock == viewList.size()) {
            isCorrect = false;
        }

        if (isCorrect) {
            this.priceLists = priceLists;
        }

        return isCorrect;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TeacherRegistration teacherRegistration = TeacherRegistration.getInstance();
        teacherRegistration.setPriceLists(priceLists);
        subjectsContainer.removeAllViews();
    }

    protected void warningColorTextView(TextView textView) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorWarning);
        textView.setTextColor(colorWarning);
    }

    protected void warningColorEditText(EditText editText) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorWarning);
        editText.setTextColor(colorWarning);
        editText.setHintTextColor(colorWarning);
    }
    protected void correctColorTextView(TextView textView) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorCorrect);
        textView.setTextColor(colorWarning);
    }

    protected void correctColorEditText(EditText editText) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorCorrect);
        editText.setTextColor(colorWarning);
        editText.setHintTextColor(colorWarning);
    }
}
