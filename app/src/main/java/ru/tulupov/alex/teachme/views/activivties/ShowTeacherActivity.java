package ru.tulupov.alex.teachme.views.activivties;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.PriceList;
import ru.tulupov.alex.teachme.models.Teacher;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.ShowTeacherPresenter;

public class ShowTeacherActivity extends BaseActivity implements ShowTeacherView {

    private Teacher teacher;

    private TextView tvFullName;
    private TextView tvAge;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvCity;
    private TextView tvOkrug;
    private TextView tvDistrict;
    private TextView tvSubways;
    private TextView tvOnlyDistanceLearning;
    private TextView tvDistanceLearning;
    private TextView tvLeaveHome;
    private TextView tvDescription;
    private ImageView ivAvatar;
    private LinearLayout llContainer;

    private ImageButton ibSMS;
    private ImageButton ibCall;
    private ImageButton ibFavorite;

    private ShowTeacherPresenter presenter;

    private boolean isFavorite = false;
    private boolean onlyDistanceLearning = false;
    private boolean distanceLearning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_teacher);

        presenter = new ShowTeacherPresenter();
        presenter.onCreate(this);

        Intent intent = getIntent();
        teacher = intent.getParcelableExtra("teacher");
        isFavorite = teacher.isFavorite();
        onlyDistanceLearning = teacher.isOnlyDistanceLearning();
        distanceLearning = teacher.isDistanceLearning();

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
        tvDistanceLearning = (TextView) findViewById(R.id.distantLearningShowTeacher);
        tvOnlyDistanceLearning = (TextView) findViewById(R.id.onlyDistantLearningShowTeacher);
        tvDescription = (TextView) findViewById(R.id.descriptionShowTeacher);
        ivAvatar = (ImageView) findViewById(R.id.avatarImageShowTeacher);
        llContainer = (LinearLayout) findViewById(R.id.containerSubjectsShowTeacher);

//        String typeUser = MyApplications.getUser().getTypeUser(this);
//        if (!isFavorite && typeUser.equals(User.TYPE_USER_PUPIL)) {
//            if (!checkConnection()) {
//                isFavorite = presenter.isTeacherFavorite(this, teacher.getId());
//                initData();
//            } else {
//                String accessToken = MyApplications.getUser().getAccessToken(this);
//                presenter.isFavorite(accessToken, teacher.getId());
//            }
//        } else {
//            initData();
//        }
        isFavorite = presenter.isTeacherFavorite(this, teacher.getId());
        initData();

        initBottomPanel(teacher.getPhoneNumber());
//        if (typeUser.equals(User.TYPE_USER_PUPIL)) {
//            initBottomPanel(teacher.getPhoneNumber());
//        } else {
//            findViewById(R.id.bottomPanelTeacher).setVisibility(View.GONE);
//        }


    }

    protected void initBottomPanel(final String phoneNumber) {
        ibSMS = (ImageButton) findViewById(R.id.btn_teacher_profile_details_sms);
        ibSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                sendIntent.putExtra("sms_body", "");
                if (ActivityCompat.checkSelfPermission(ShowTeacherActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                    String str = getResources().getString(R.string.noPermissionSMS);
//                    Toast.makeText(ShowTeacherActivity.this, str , Toast.LENGTH_SHORT).show();

                    String[] arrPermissions= new String[]{Manifest.permission.SEND_SMS};
                    ActivityCompat.requestPermissions(ShowTeacherActivity.this, arrPermissions, 101);
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

                    String[] arrPermissions= new String[]{Manifest.permission.CALL_PHONE};
                    ActivityCompat.requestPermissions(ShowTeacherActivity.this, arrPermissions, 101);
                    return;
                }
                startActivity(callIntent);
            }
        });

        ibFavorite = (ImageButton) findViewById(R.id.btn_teacher_profile_details_favorite);
        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                User user = MyApplications.getUser();

//                String accessToken = user.getAccessToken(ShowTeacherActivity.this);
//                int id = teacher.getId();
//                presenter.setFavorite();
                isFavorite = !isFavorite;

                if (isFavorite) {
                    Toast.makeText(ShowTeacherActivity.this,
                            getString(R.string.message_teacher_add_to_favorites), Toast.LENGTH_SHORT)
                            .show();
                    presenter.saveFavorite(ShowTeacherActivity.this, teacher);
                    ibFavorite.setImageResource(R.drawable.ic_favorite_full);
                } else {
                    Toast.makeText(ShowTeacherActivity.this,
                            getString(R.string.message_teacher_delete_from_favorites), Toast.LENGTH_SHORT)
                            .show();
                    presenter.deleteFavorite(ShowTeacherActivity.this, teacher.getId());
                    ibFavorite.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        });
        initFavoriteButton();
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

        String strOkrug = getString(R.string.hint_region);
        String strDistrict = getString(R.string.hint_district);

        if (teacher.getOkrug() != null && !teacher.getOkrug().equals("")) {
            tvOkrug.setText(strOkrug + ": " + teacher.getOkrug());
        } else {
            tvOkrug.setVisibility(View.GONE);
        }

        if (teacher.getDistrict() != null && !teacher.getDistrict().equals("")) {
            tvDistrict.setText(strDistrict + ": " + teacher.getDistrict());
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

        String onlyDistantLearningText;
        if (onlyDistanceLearning) {
            onlyDistantLearningText = getResources().getString(R.string.onlyDistanceLearningYes);
        } else {
            onlyDistantLearningText = getResources().getString(R.string.onlyDistanceLearningNo);
        }
        tvOnlyDistanceLearning.setText(onlyDistantLearningText);

        if (!onlyDistanceLearning) {
            String distantLearningText;
            if (distanceLearning) {
                distantLearningText = getResources().getString(R.string.distanceLearningYes);
            } else {
                distantLearningText = getResources().getString(R.string.distanceLearningNo);
            }
            tvDistanceLearning.setText(distantLearningText);
            tvDistanceLearning.setVisibility(View.VISIBLE);
        }


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


    @Override
    public void setFavorite() {
        isFavorite = !isFavorite;

        if (isFavorite) {
            Toast.makeText(this, getString(R.string.message_teacher_add_to_favorites), Toast.LENGTH_SHORT).show();
        }

        initFavoriteButton();
        presenter.saveFavorite(this, teacher);
        Log.d(Constants.MY_TAG, "saveFavorite");
    }

    @Override
    public void isFavoriteSuccess(boolean isFavorite) {
        this.isFavorite = isFavorite;
        initFavoriteButton();
        initData();
    }

    @Override
    public void isFavoriteError() {

    }

    @Override
    public void deleteFavorite() {
        isFavorite = !isFavorite;
        if (!isFavorite) {
            Toast.makeText(this, getString(R.string.message_teacher_delete_from_favorites), Toast.LENGTH_SHORT).show();
        }

        initFavoriteButton();
        presenter.deleteFavorite(this, teacher.getId());
        Log.d(Constants.MY_TAG, "deleteFavorite");
    }

    @Override
    public void errorFavorite() {
        Toast.makeText(this, "error favorite", Toast.LENGTH_SHORT).show();
    }
}
