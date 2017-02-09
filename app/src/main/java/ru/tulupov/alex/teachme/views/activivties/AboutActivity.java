package ru.tulupov.alex.teachme.views.activivties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.tulupov.alex.teachme.R;

public class AboutActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initToolbar(getString(R.string.navi_about), R.id.toolbarAbout);
        initNavigationView();
    }
}
