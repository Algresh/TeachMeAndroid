package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.Teacher;

public class ShowTeacherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_teacher);


        Intent intent = getIntent();

        Teacher teacher = intent.getParcelableExtra("teacher");

        Toast.makeText(this, teacher.getStrPriceList(), Toast.LENGTH_SHORT).show();

        initToolbar(R.string.result_search_activity_title, R.id.toolbarListTeachers);
    }
}
