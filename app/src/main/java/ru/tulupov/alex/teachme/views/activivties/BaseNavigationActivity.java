package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.user.TeacherUser;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.BasePresenter;
import ru.tulupov.alex.teachme.views.fragments.FreezeDialogFragment;

import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_FAVORITE;


public class BaseNavigationActivity extends BaseActivity implements FreezeDialogFragment.FreezeListener, BaseView {

    protected BasePresenter presenter;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new BasePresenter();
        presenter.onCreate(this);
    }

    protected void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        View viewHeader = null;

        User user = MyApplications.getUser();
        String typeUser = user.getTypeUser(this);

        if (typeUser.equals(User.TYPE_USER_PUPIL)) {
            viewHeader = getLayoutInflater().inflate(R.layout.navigation_header_pupil, null, false);
            navigationView.inflateMenu(R.menu.menu_navigation_pupil);
            Log.d(Constants.MY_TAG, "TYPE_USER_PUPIL");
        } else if (typeUser.equals(User.TYPE_USER_TEACHER)) {
            viewHeader = getLayoutInflater().inflate(R.layout.navigation_header_teacher, null, false);
            TeacherUser teacherUser = (TeacherUser) user;
            ImageView ivAvatar = (ImageView) viewHeader.findViewById(R.id.avatarImageNavHead);
            ivAvatar.setImageBitmap(teacherUser.getPhoto(this));

            Picasso.with(this).load(Constants.DOMAIN_IMAGE + teacherUser.getPhotoSrc(this)).into(ivAvatar);
            Log.d(Constants.MY_TAG, "TYPE_USER_Teacher");
            if (teacherUser.getEnable(this) == User.TYPE_ENABLE_ENABLE) {
                navigationView.inflateMenu(R.menu.menu_navigation_teacher);
            } else if(teacherUser.getEnable(this) == User.TYPE_ENABLE_LOCKED) {
                navigationView.inflateMenu(R.menu.menu_navigation_teacher_locked);
            }
        }
        if (viewHeader != null) {
            TextView tvEmail = (TextView) viewHeader.findViewById(R.id.emailNavHead);
            String email = user.getEmail(this);
            tvEmail.setText(email);
            navigationView.addHeaderView(viewHeader);
        }


        navigationView.setNavigationItemSelectedListener(selectMenuNavigationView());
    }

    private  NavigationView.OnNavigationItemSelectedListener selectMenuNavigationView() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                Intent intent = null;

                switch (item.getItemId()){
                    case R.id.nav_favorites:
                        intent = new Intent(BaseNavigationActivity.this, ListTeachersActivity.class);
                        intent.putExtra(TYPE_SEARCH, TYPE_SEARCH_FAVORITE);
                        break;
                    case R.id.nav_search_techer:
                        intent = new Intent(BaseNavigationActivity.this, SelectSearchActivity.class);
                        break;
                    case R.id.nav_logout:
                        logOut();
                        intent = new Intent(BaseNavigationActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        break;
                    case R.id.nav_edit_profile:
                        intent = new Intent(BaseNavigationActivity.this, SelectChangesActivity.class);
                        break;
                    case R.id.nav_freeze:
                        showFreeze();
                        break;
                    case R.id.nav_unlock_profile:
                        showFreeze();
                        break;
                    case R.id.nav_about:
                        intent = new Intent(BaseNavigationActivity.this, AboutActivity.class);
                        break;
                    case R.id.nav_feedback:
//                        intent = new Intent(BaseNavigationActivity.this, AboutActivity.class);
                        break;
                    case R.id.nav_search_profile:
                        intent = new Intent(BaseNavigationActivity.this, SelectSearchActivity.class);
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }

                return true;
            }
        };
    }

    @Override
    protected void initToolbar(int idTitle, int idToolbar) {
        toolbar = (Toolbar) findViewById(idToolbar);
        String title = getResources().getString(idTitle);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
        }
    }

    protected void showFreeze() {
        if (checkConnection()) {
            FreezeDialogFragment dialog = new FreezeDialogFragment();
            FragmentManager manager = getSupportFragmentManager();
            dialog.show(manager, "freeze");
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    protected void logOut() {
        MyApplications.getUser().clearAllData(this);
    }

    @Override
    public void freezeTeacher() {
        User user = MyApplications.getUser();
        String accessToken = user.getAccessToken(this);
        presenter.freezeTeacher(accessToken);
    }

    @Override
    public void freezeTeacherSuccess() {
        Toast.makeText(this, R.string.showTeacherAnketaStop, Toast.LENGTH_SHORT).show();
        MyApplications.getUser().setEnable(this, User.TYPE_ENABLE_LOCKED);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_navigation_teacher_locked);
    }

    @Override
    public void unfreezeTeacherSuccess() {
        Toast.makeText(this, R.string.showTeacherAnketaStart, Toast.LENGTH_SHORT).show();
        MyApplications.getUser().setEnable(this, User.TYPE_ENABLE_ENABLE);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_navigation_teacher);
    }

    @Override
    public void freezeTeacherError() {
        Toast.makeText(this, R.string.somethingBroken, Toast.LENGTH_SHORT).show();
    }
}
