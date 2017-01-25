package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.user.TeacherUser;
import ru.tulupov.alex.teachme.models.user.User;


public class BaseNavigationActivity extends BaseActivity {

    protected void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);

        User user = MyApplications.getUser();
        String typeUser = user.getTypeUser(this);

        if (typeUser.equals(User.TYPE_USER_PUPIL)) {
            navigationView.inflateMenu(R.menu.menu_navigation_pupil);
        } else if (typeUser.equals(User.TYPE_USER_TEACHER)) {
            TeacherUser teacherUser = (TeacherUser) user;
            if (teacherUser.getEnable(this) == User.TYPE_ENABLE_ENABLE) {
                navigationView.inflateMenu(R.menu.menu_navigation_teacher);
            } else {
                navigationView.inflateMenu(R.menu.menu_navigation_teacher_locked);
            }
        }

        navigationView.setNavigationItemSelectedListener(selectMenuNavigationView());
    }

    private  NavigationView.OnNavigationItemSelectedListener selectMenuNavigationView() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                Intent intent;


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
