package ru.tulupov.alex.teachme.views.activivties;

import android.graphics.Point;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;


public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected Toolbar toolbar;

    protected void initToolbar(int idTitle, int idToolbar) {
        toolbar = (Toolbar) findViewById(idToolbar);
        String title = getResources().getString(idTitle);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected int getHeightScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.y;
    }

}
