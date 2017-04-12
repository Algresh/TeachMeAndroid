package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ru.tulupov.alex.teachme.R;

public class SelectSearchActivity extends BaseNavigationActivity {

    private Button btnSearch;
    private Button btnQuickSearch;
    private Button btnFullSearch;

    public static final int TYPE_SEARCH_MY_CITY = 0;
    public static final int TYPE_SEARCH_QUICK = 1;
    public static final int TYPE_SEARCH_FULL = 2;
    public static final int TYPE_SEARCH_FAVORITE = 3;

    public static final String SEARCH_FIELD_CITY = "city";
    public static final String SEARCH_FIELD_SUBJECT = "subject";
    public static final String SEARCH_FIELD_LEAVE_HOUSE = "leaveHouse";
    public static final String SEARCH_FIELD_DISTANCE_LEARNING = "distanceLearning";
    public static final String SEARCH_FIELD_EXPERIENCE = "exp";
    public static final String SEARCH_FIELD_PRICE = "price";
    public static final String SEARCH_FIELD_START_PRICE = "start_price";
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
                if (checkConnection()) {
                    Intent intent = new Intent(SelectSearchActivity.this, MainSearchActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SelectSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnQuickSearch = (Button) findViewById(R.id.btn_search_quick);
        btnQuickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkConnection()) {
                    Intent intent = new Intent(SelectSearchActivity.this, QuickSearchActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SelectSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnFullSearch = (Button) findViewById(R.id.btn_search_all_profiles);
        btnFullSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkConnection()) {
                    Intent intent = new Intent(SelectSearchActivity.this, ListTeachersActivity.class);
                    intent.putExtra(TYPE_SEARCH, TYPE_SEARCH_MY_CITY);
                    startActivity(intent);
                } else {
                    Toast.makeText(SelectSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                }
            }
        });

        initToolbar(R.string.select_search_activity_title, R.id.toolbarSelectSearch);
        initNavigationView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
