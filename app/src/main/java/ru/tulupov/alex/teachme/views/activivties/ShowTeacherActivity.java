package ru.tulupov.alex.teachme.views.activivties;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

    ImageButton ibSMS;
    ImageButton ibCall;
    ImageButton ibFavorite;

    private boolean isFavorite = false;

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
        initBottomPanel(teacher.getPhoneNumber());

    }

    protected void initBottomPanel(final String phoneNumber) {
        ibSMS = (ImageButton) findViewById(R.id.btn_teacher_profile_details_sms);
        ibSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                sendIntent.putExtra("sms_body", "");
                if (ActivityCompat.checkSelfPermission(ShowTeacherActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    String str = getResources().getString(R.string.noPermissionSMS);
                    Toast.makeText(ShowTeacherActivity.this, str , Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(sendIntent);
            }
        });

        ibCall = (ImageButton) findViewById(R.id.btn_teacher_profile_details_call);
        ibCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                if (ActivityCompat.checkSelfPermission(ShowTeacherActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    String str = getResources().getString(R.string.noPermissionCall);
                    Toast.makeText(ShowTeacherActivity.this, str , Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(callIntent);
            }
        });

        ibFavorite = (ImageButton) findViewById(R.id.btn_teacher_profile_details_favorite);
        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFavorite = !isFavorite;
                if (isFavorite) {
                    Toast.makeText(ShowTeacherActivity.this, getString(R.string.message_teacher_add_to_favorites), Toast.LENGTH_SHORT).show();
                }
                initFavoriteButton();
            }
        });
    }

    protected void initFavoriteButton() {
        if ( isFavorite ) {
            ibFavorite.setImageResource(R.drawable.ic_favorite_full);
        }
        else {
            ibFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
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

        int colorText = ContextCompat.getColor(this, R.color.colorCorrect);
        String[] arrExp = getResources().getStringArray(R.array.typeExperience);
        String priceListFormat = getResources().getString(R.string.priceListFormat);
        for (PriceList item: teacher.getPriceLists()) {
            TextView tvSbj = new TextView(this);

            tvSbj.setTextColor(colorText);

            int typeExp = Integer.parseInt(item.getExperience());
            String titleSbj = item.getSubject().getTitle();
            int price = item.getPrice();
            String str = String.format(priceListFormat, titleSbj, price, arrExp[typeExp].toLowerCase());
            tvSbj.setText(str);
            llContainer.addView(tvSbj);
        }

        if (teacher.getPhoto() != null) {
            Picasso.with(this).load(Constants.DOMAIN_IMAGE + teacher.getPhoto()).into(ivAvatar);
        }

    }



}
