package ru.tulupov.alex.teachme.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.Subject;
import ru.tulupov.alex.teachme.models.TeacherRegistration;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;

public class RegTeacherFullNameFragment extends Fragment
        implements ShowCity, FragmentCityDialog.SelectCity, RegDataCorrect {
    private final int pickImageResult = 1;

    EditText etBirthday;
    EditText etFirstName;
    EditText etLastName;
    EditText etFatherName;
    EditText etOkrug;
    EditText etDistrict;
    TextView etCity;
    ImageView ivAvatar;
    private CitySubjectPresenter presenter;
    private List<City> listCities;

    private City selectedCity;
    private int indexSelectedCity = -1;
    private Bitmap bitmapPhoto;
    private boolean cityDialogIsDownloading = false;
    private DatePickerFragment dialog;

    //только для изменения профиля
    private Map map;

    View.OnClickListener selectAvatar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectAvatarPhoto();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch ( requestCode ) {
            case pickImageResult:
                if(resultCode == Activity.RESULT_OK){
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        int height = selectedImage.getHeight();
                        int width = selectedImage.getWidth();
                        Log.d(Constants.MY_TAG, width + " " + height);
                        float res;
                        float w = width;
                        float h = height;

                        if (w > h) {
                            res = h / w;
                            w = 200;
                            h =  w * res;
                        } else {
                            res = w / h;
                            h = 200;
                            w = h * res;
                        }

                        height = (int) h;
                        width = (int) w;

                        Log.d(Constants.MY_TAG, width + " | " + height);
                        Bitmap smallBitmap = Bitmap.createScaledBitmap(selectedImage, width, height, false);
                        selectedImage = null;
                        ivAvatar.setImageBitmap(smallBitmap);
                        bitmapPhoto = smallBitmap;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = new CitySubjectPresenter();
        presenter.onCreate(this, null, null);

        View view = inflater.inflate(R.layout.fragment_reg_teacher_fullname, container, false);
        etBirthday = (EditText) view.findViewById(R.id.register_teacher_birthday);
        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog == null || dialog.isCancelable()) {
                    dialog = new DatePickerFragment();
                    dialog.setEditTextDate(etBirthday);
                    dialog.show(getChildFragmentManager(), "DatePickerFragment");
                }
            }
        });
        etFirstName = (EditText) view.findViewById(R.id.register_teacher_first_name);
        etLastName = (EditText) view.findViewById(R.id.register_teacher_last_name);
        etFatherName = (EditText) view.findViewById(R.id.register_teacher_father_name);
        etOkrug = (EditText) view.findViewById(R.id.register_teacher_okrug);
        etDistrict = (EditText) view.findViewById(R.id.register_teacher_district);
        etCity = (TextView) view.findViewById(R.id.register_teacher_city);
        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cityDialogIsDownloading) {
                    cityDialogIsDownloading = true;
                    presenter.getListCities();
                }
            }
        });
        ivAvatar = (ImageView) view.findViewById(R.id.avatarImage);
        ivAvatar.setOnClickListener(selectAvatar);
        view.findViewById(R.id.avatarImageText).setOnClickListener(selectAvatar);

        initFields();
        initData();

        return view;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    //используется только при изменении профиля так как только тогда map != null
    protected void initData() {
        if (map != null) {
            String firstName = (String) map.get("firstName");
            String lastName = (String) map.get("lastName");
            String fatherName = (String) map.get("fatherName");
            Double cityId = (Double) map.get("cityId");
            String cityTitle = (String) map.get("cityTitle");
            Boolean cityHasSub = (Boolean) map.get("cityHasSub");
            String birthDate = (String) map.get("birthDate");
            String okrug = (String) map.get("okrug");
            String district = (String) map.get("district");
            String photo = (String) map.get("photo");

            etFirstName.setText(firstName);
            etLastName.setText(lastName);
            etFatherName.setText(fatherName);
            etBirthday.setText(birthDate);
            selectedCity = new City(cityId.intValue(), cityTitle, cityHasSub);
            etOkrug.setText(okrug);
            etDistrict.setText(district);
            etCity.setText(cityTitle);

            Picasso.with(getContext()).load(Constants.DOMAIN_IMAGE + photo).into(ivAvatar);
        }
    }

    public Map<String, String> getDataMap() {
        Map<String, String> map = new HashMap<>();

        map.put("firstName", etFirstName.getText().toString());
        map.put("lastName", etLastName.getText().toString());
        map.put("fatherName", etFatherName.getText().toString());
        map.put("birthDate", etBirthday.getText().toString());
        map.put("city", String.valueOf(selectedCity.getId()));
        map.put("okrug", etOkrug.getText().toString());
        map.put("district", etDistrict.getText().toString());

        return map;
    }

    @Override
    public void showCities(List<City> list) {
        listCities = list;
        FragmentCityDialog dialog = new FragmentCityDialog();
        dialog.setListCities(list);
        dialog.setSelectedItem(indexSelectedCity);
        FragmentManager manager = getChildFragmentManager();
        dialog.show(manager, "city");
        cityDialogIsDownloading = false;
    }


    @Override
    public void selectCity(int cityIndex) {
        indexSelectedCity = cityIndex;
        selectedCity = listCities.get(cityIndex);
        etCity.setText(selectedCity.getTitle());
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TeacherRegistration teacherRegistration = TeacherRegistration.getInstance();
        teacherRegistration.setBirthDate(etBirthday.getText().toString());
        teacherRegistration.setFirstName(etFirstName.getText().toString());
        teacherRegistration.setLastName(etLastName.getText().toString());
        teacherRegistration.setFatherName(etFatherName.getText().toString());
        teacherRegistration.setCity(selectedCity);
        teacherRegistration.setOkrug(etOkrug.getText().toString());
        teacherRegistration.setDistrict(etDistrict.getText().toString());
        teacherRegistration.setPhoto(bitmapPhoto);
    }

    @Override
    public boolean dataIsCorrect() {
        boolean isCorrect = true;

        String strFirstName = etFirstName.getText().toString().trim();
        if (strFirstName.length() < 2 || strFirstName.matches(".*\\d.*") ) {
            warningColorEditText(etFirstName);
            isCorrect = false;
        } else {
            correctColorEditText(etFirstName);
        }

        String strLastName = etLastName.getText().toString().trim();
        if (strLastName.length() < 2 || strLastName.matches(".*\\d.*") ) {
            warningColorEditText(etLastName);
            isCorrect = false;
        } else {
            correctColorEditText(etLastName);
        }

        String strFatherName = etFatherName.getText().toString().trim();
        if (strFatherName.length() < 2 || strFatherName.matches(".*\\d.*") ) {
            warningColorEditText(etFatherName);
            isCorrect = false;
        } else {
            correctColorEditText(etFatherName);
        }

        String strBirthday = etBirthday.getText().toString().trim();
        if (strBirthday.length() != 10) {
            warningColorEditText(etBirthday);
            isCorrect = false;
        } else {
            correctColorEditText(etBirthday);
        }

        String strOkrug = etOkrug.getText().toString().trim();
        if (strOkrug.matches(".*\\d.*") ) {
            warningColorEditText(etOkrug);
            isCorrect = false;
        } else {
            correctColorEditText(etOkrug);
        }

        String strDistrict = etDistrict.getText().toString().trim();
        if (strDistrict.matches(".*\\d.*") ) {
            warningColorEditText(etDistrict);
            isCorrect = false;
        } else {
            correctColorEditText(etDistrict);
        }

        if (selectedCity == null) {
            warningColorTextView(etCity);
            isCorrect = false;
        } else {
            correctColorTextView(etCity);
        }

        if (!isCorrect) {
            String msg = getResources().getString(R.string.reg_warning_message);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT ).show();
        }

        return isCorrect;

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


    protected void initFields() {
        TeacherRegistration teacher = TeacherRegistration.getInstance();
//        etBirthday.setText(teacher.getBirthDate());
//        etFirstName.setText(teacher.getFirstName());
//        etLastName.setText(teacher.getLastName());
//        etFatherName.setText(teacher.getFatherName());
//        etOkrug.setText(teacher.getOkrug());
//        etDistrict.setText(teacher.getDistrict());
        bitmapPhoto = teacher.getPhoto();
        if (bitmapPhoto != null) {
            ivAvatar.setImageBitmap(bitmapPhoto);
        }
        selectedCity = teacher.getCity();
        if (selectedCity != null) {
            etCity.setText(selectedCity.getTitle());
        }
    }

    protected void selectAvatarPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, pickImageResult);
    }

}
