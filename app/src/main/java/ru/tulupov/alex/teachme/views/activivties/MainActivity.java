package ru.tulupov.alex.teachme.views.activivties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import ru.tulupov.alex.teachme.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, )
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleViewTeacher);
    }
}
