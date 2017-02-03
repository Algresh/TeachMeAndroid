package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.user.TeacherUser;
import ru.tulupov.alex.teachme.models.user.User;

import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_FAVORITE;


public class BaseNavigationActivity extends BaseActivity {

    protected void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
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
            } else {
                navigationView.inflateMenu(R.menu.menu_navigation_teacher_locked);
            }
        }
        if (viewHeader != null) {
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
                        break;
                    case R.id.nav_edit_profile:
                        intent = new Intent(BaseNavigationActivity.this, ChangeEmailActivity.class);
                        break;
                }

                startActivity(intent);

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

    protected void logOut() {
        MyApplications.getUser().clearAllData(this);
    }
}
