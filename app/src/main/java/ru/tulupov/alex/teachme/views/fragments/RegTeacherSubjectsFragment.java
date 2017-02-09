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
        ShowSubject ,FragmentSubjectDialog.SelectSubject, RegDataCorrect,
        ExperienceDialogFragment.SelectExperience {

    protected View fragmentView;
    protected LinearLayout subjectsContainer;
    protected int numSubjects = 0;
    private CitySubjectPresenter presenter;
    private List<Subject> listSubjects;
    private List<View> viewList;
    private List<PriceList> priceLists;
    private List<Integer> selectedListSubjects;
    private List<Integer> selectedListExp;

    private List<Subject> selectedObjSubjects;
    private boolean dialogIsDownloading = false;

    private boolean isUpdating = false;


    private void addSubject() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View viewSubject = layoutInflater.inflate(R.layout.reg_teacher_subject, null);
        TextView tvSubjects = (TextView) viewSubject.findViewById(R.id.sp_select_subject);
        TextView tvExp = (TextView) viewSubject.findViewById(R.id.et_reg_teacher_experience);

        String strTvSubject = getContext().getResources().getString(R.string.hint_subject);
        String strTvExp = getContext().getResources().getString(R.string.hint_experience);
        tvSubjects.setTag(numSubjects);
        tvSubjects.setText(strTvSubject);
        tvSubjects.setOnClickListener(this);

        tvExp.setTag(numSubjects);
        tvExp.setText(strTvExp);
        tvExp.setOnClickListener(this);

        viewList.add(viewSubject);
        subjectsContainer.addView(viewSubject);
        numSubjects++;
    }

    private void addSubjects() {
        for(View view : viewList) {
            subjectsContainer.addView(view);
        }

    }

    private void addAndFillSubjects() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        for (PriceList item : priceLists) {
            View viewSubject = layoutInflater.inflate(R.layout.reg_teacher_subject, null);
            TextView tvSubjects = (TextView) viewSubject.findViewById(R.id.sp_select_subject);
            tvSubjects.setOnClickListener(this);
            tvSubjects.setTag(numSubjects);
            TextView tvExp = (TextView) viewSubject.findViewById(R.id.et_reg_teacher_experience);
            tvExp.setOnClickListener(this);
            EditText etPrice = (EditText) viewSubject.findViewById(R.id.et_reg_teacher_price);
            tvExp.setTag(numSubjects);

            tvSubjects.setText(item.getSubject().getTitle());

            int index = Integer.parseInt(item.getExperience());
            selectedListExp.add(index);
            String strExp = getResources().getStringArray(R.array.typeExperience)[index];
            tvExp.setText(strExp);

            etPrice.setText(String.valueOf(item.getPrice()));

            viewList.add(viewSubject);
            subjectsContainer.addView(viewSubject);
            selectedObjSubjects.add(item.getSubject());


            numSubjects++;
        }
    }

    /**
     *
     * @TODO проверить работает ли регистрация!!
     */


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (priceLists == null || isUpdating) {
            presenter = new CitySubjectPresenter();
            viewList = new ArrayList<>();
            selectedListSubjects = new ArrayList<>();
            selectedListExp = new ArrayList<>();
            selectedObjSubjects = new ArrayList<>();
        }
//        numSubjects = 0;
        presenter.onCreate(null, this, null);
        fragmentView = inflater.inflate(R.layout.fragment_reg_teacher_subjects, container, false);
        subjectsContainer = (LinearLayout) fragmentView.findViewById(R.id.ll_subjects);
        fragmentView.findViewById(R.id.btn_reg_teacher_add_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedObjSubjects.size() == viewList.size()) {
                    addSubject();
                }
            }
        });
        if(priceLists == null) {
            addSubject();
        } else {
            if (!isUpdating) {
                addSubjects();
            } else {
                addAndFillSubjects();
            }

        }
        return fragmentView;
    }

    public void isUpdating() {
        isUpdating = true;
    }

    public void setPriceLists(List<PriceList> priceLists) {
        this.priceLists = priceLists;
    }

    public List<PriceList> getPriceLists() {
        return priceLists;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sp_select_subject && !dialogIsDownloading) {
            dialogIsDownloading = true;
            Integer tag = (Integer) view.getTag();
            presenter.getListSubjects(tag);
        }

        if (view.getId() == R.id.et_reg_teacher_experience && !dialogIsDownloading) {
            dialogIsDownloading = true;
            Integer tag = (Integer) view.getTag();
            ExperienceDialogFragment fragment = new ExperienceDialogFragment();
            fragment.setTagItem(tag);
            Integer selectItem;
            if(selectedListExp.size() > tag) {
                selectItem = selectedListExp.get(tag);
            } else {
                selectItem = -1;
            }
            fragment.setSelectedItem(selectItem);
            FragmentManager manager = getChildFragmentManager();
            fragment.show(manager, "exp");
            dialogIsDownloading = false;
        }
    }

    @Override
    public void showSubjects(List<Subject> list, int tag) {
        if (list != null && list.size() > 0) {
            listSubjects = list;
            FragmentSubjectDialog dialog = new FragmentSubjectDialog();
            dialog.setListSubject(list);
            dialog.setTag(tag);
            Integer selectItem;
            Log.d(Constants.MY_TAG, "showSubjects " + tag);
            if(selectedListSubjects.size() > tag) {
                selectItem = selectedListSubjects.get(tag);
            } else {
                selectItem = -1;
            }

            dialog.setSelectedItem(selectItem);
            FragmentManager manager = getChildFragmentManager();
            dialog.show(manager, "subjects");
            dialogIsDownloading = false;
        }
    }

    @Override
    public void selectSubject(int subjectIndex, int tag) {
        View view = viewList.get(tag);
        TextView tvSubject = (TextView) view.findViewById(R.id.sp_select_subject);
        Subject subject = listSubjects.get(subjectIndex);
        selectedObjSubjects.add(subject);
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

            TextView edtExp = (TextView) viewList.get(i).findViewById(R.id.et_reg_teacher_experience);
            EditText edtPrice = (EditText) viewList.get(i).findViewById(R.id.et_reg_teacher_price);
            TextView tvSubject = (TextView) viewList.get(i).findViewById(R.id.sp_select_subject);
            String strSbj = tvSubject.getText().toString();
            String strExp = edtExp.getText().toString().trim();
            String strPrice = edtPrice.getText().toString().trim();

            String textExp = getResources().getString(R.string.hint_experience);
            if (strExp.equals(textExp)) {
                warningColorTextView(edtExp);
                numWarning++;
                numEmptyField++;
            } else {
                correctColorTextView(edtExp);
            }


            if (strPrice.length() == 0 || !strPrice.matches(".*\\d.*") || strPrice.contains(".")) {
                warningColorEditText(edtPrice);
                numWarning++;
            } else {
                correctColorEditText(edtPrice);
            }

//            if (strExp.length() == 0) {
//                numEmptyField++;
//            }
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
                correctColorTextView(edtExp);
            } else {
                if (numWarning > 0) {
                    isCorrect = false;
                } else {
                    PriceList priceList = new PriceList();
                    Integer itemExp = selectedListExp.get(i - numEmptyBlock);
                    priceList.setExperience(String.valueOf(itemExp));
                    priceList.setPrice(Integer.parseInt(strPrice));
//                    Integer itemSbj = selectedListSubjects.get(i - numEmptyBlock);
                    priceList.setSubject(selectedObjSubjects.get(i - numEmptyBlock));
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

    @Override
    public void selectExperience(int item, int tagItem) {
        View view = viewList.get(tagItem);
        TextView tvExp = (TextView) view.findViewById(R.id.et_reg_teacher_experience);
        String titleExp = getContext().getResources().getStringArray(R.array.typeExperience)[item];
        tvExp.setText(titleExp);
        selectedListExp.add(item);

    }
}
