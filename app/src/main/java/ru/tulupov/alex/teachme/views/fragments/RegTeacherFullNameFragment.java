package ru.tulupov.alex.teachme.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.Subject;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;

public class RegTeacherFullNameFragment extends Fragment implements ShowCity, FragmentCityDialog.SelectCity {
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
    private List<Subject> listSubjects;

    private City selectedCity;
    private int indexSelectedCity = 0;



    void clickOnAddAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, pickImageResult);
    }

    void clickOnBirthday() {

    }

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
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ivAvatar.setImageBitmap(selectedImage);
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
        presenter.onCreate(this, null);

        View view = inflater.inflate(R.layout.fragment_reg_teacher_fullname, container, false);
        etBirthday = (EditText) view.findViewById(R.id.register_teacher_birthday);
        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.setEditTextDate(etBirthday);
                dialog.show(getChildFragmentManager(), "DatePickerFragment");
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
                presenter.getListCities();
            }
        });
        ivAvatar = (ImageView) view.findViewById(R.id.img_avatar);

        return view;
    }

    @Override
    public void showCities(List<City> list) {
        listCities = list;
        FragmentCityDialog dialog = new FragmentCityDialog();
        dialog.setListCities(list);
        dialog.setSelectedItem(indexSelectedCity);
        FragmentManager manager = getFragmentManager();
        dialog.show(manager, "city");
    }


    @Override
    public void selectCity(int cityIndex) {
        City city = listCities.get(cityIndex);
        etCity.setText(city.getTitle());
    }
}
