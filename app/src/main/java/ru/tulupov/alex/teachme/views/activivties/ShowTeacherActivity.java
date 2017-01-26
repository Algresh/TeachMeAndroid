package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.PriceList;
import ru.tulupov.alex.teachme.models.Teacher;

public class ShowTeacherActivity extends BaseActivity {

    Teacher teacher;

    TextView tvFullName;
    TextView tvAge;
    TextView tvEmail;
    TextView tvPhone;
    TextView tvCity;
    TextView tvOkrug;
    TextView tvDistrict;
    TextView tvSubways;
    TextView tvLeaveHome;
    TextView tvDescription;
    ImageView ivAvatar;
    LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_teacher);

        Intent intent = getIntent();
        teacher = intent.getParcelableExtra("teacher");
        initToolbar(teacher.getFullName(), R.id.toolbarShowTeachers);

        tvFullName = (TextView) findViewById(R.id.fullNameShowTeacher);
        tvAge = (TextView) findViewById(R.id.ageShowTeacher);
        tvEmail = (TextView) findViewById(R.id.emailShowTeacher);
        tvPhone = (TextView) findViewById(R.id.phoneShowTeacher);
        tvCity = (TextView) findViewById(R.id.cityShowTeacher);
        tvOkrug = (TextView) findViewById(R.id.okrugShowTeacher);
        tvDistrict = (TextView) findViewById(R.id.districtShowTeacher);
        tvSubways = (TextView) findViewById(R.id.subwaysShowTeacher);
        tvLeaveHome = (TextView) findViewById(R.id.leaveHomeShowTeacher);
        tvDescription = (TextView) findViewById(R.id.descriptionShowTeacher);
        ivAvatar = (ImageView) findViewById(R.id.avatarImageShowTeacher);
        llContainer = (LinearLayout) findViewById(R.id.containerSubjectsShowTeacher);


        initData();

    }

    protected void initData() {
        tvFullName.setText(teacher.getFullName());
        tvAge.setText(teacher.getAge(getResources()));
        tvEmail.setText(teacher.getEmail());
        tvPhone.setText(teacher.getPhoneNumber());
        tvCity.setText(teacher.getCity().getTitle());
        if (teacher.getOkrug() != null && !teacher.getOkrug().equals("")) {
            tvOkrug.setText(teacher.getOkrug());
        } else {
            tvOkrug.setVisibility(View.GONE);
        }

        if (teacher.getDistrict() != null && !teacher.getDistrict().equals("")) {
            tvDistrict.setText(teacher.getDistrict());
        } else {
            tvDistrict.setVisibility(View.GONE);
        }

        if (teacher.getCity().isHasSubway()) {
            tvSubways.setText(teacher.getSubways());
        } else {
            tvSubways.setVisibility(View.GONE);
        }

        String leaveHome;
        if (teacher.isLeaveHome()) {
            leaveHome = getResources().getString(R.string.leaveHomeYes);
        } else {
            leaveHome = getResources().getString(R.string.leaveHomeNo);
        }
        tvLeaveHome.setText(leaveHome);

        tvDescription.setText(teacher.getDescription());

        /**
         * @TODO добавить опыт (использовать формат стрингов)
         */
        int colorText = ContextCompat.getColor(this, R.color.colorCorrect);
        for (PriceList item: teacher.getPriceLists()) {
            TextView tvSbj = new TextView(this);

            tvSbj.setTextColor(colorText);

            String str = item.getSubject().getTitle() + " " + item.getPrice() + " руб";
            tvSbj.setText(str);
            llContainer.addView(tvSbj);
        }

        if (teacher.getPhoto() != null) {
            Picasso.with(this).load(Constants.DOMAIN_IMAGE + teacher.getPhoto()).into(ivAvatar);
        }

    }



}
