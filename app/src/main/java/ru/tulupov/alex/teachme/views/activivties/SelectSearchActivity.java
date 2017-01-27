package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.tulupov.alex.teachme.R;

public class SelectSearchActivity extends BaseNavigationActivity {

    Button btnSearch;
    Button btnQuickSearch;
    Button btnFullSearch;

    public static final int TYPE_SEARCH_MY_CITY = 0;
    public static final int TYPE_SEARCH_QUICK = 1;
    public static final int TYPE_SEARCH_FULL = 2;

    public static final String SEARCH_FIELD_CITY = "city";
    public static final String SEARCH_FIELD_SUBJECT = "subject";
    public static final String SEARCH_FIELD_LEAVE_HOUSE = "leaveHouse";
    public static final String SEARCH_FIELD_EXPERIENCE = "exp";
    public static final String SEARCH_FIELD_PRICE = "price";
    public static final String SEARCH_FIELD_SUBWAY = "subway";
    public static final String SEARCH_FIELD_PHOTO = "photo";

    public static final String TYPE_SEARCH = "typeSearch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_search);

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSearchActivity.this, MainSearchActivity.class);
                startActivity(intent);
            }
        });
        btnQuickSearch = (Button) findViewById(R.id.btn_search_quick);
        btnQuickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSearchActivity.this, QuickSearchActivity.class);
                startActivity(intent);
            }
        });
        btnFullSearch = (Button) findViewById(R.id.btn_search_all_profiles);
        btnFullSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSearchActivity.this, ListTeachersActivity.class);
                intent.putExtra(TYPE_SEARCH, TYPE_SEARCH_MY_CITY);
                startActivity(intent);
            }
        });

        initToolbar(R.string.select_search_activity_title, R.id.toolbarSelectSearch);
        initNavigationView();
    }
}
