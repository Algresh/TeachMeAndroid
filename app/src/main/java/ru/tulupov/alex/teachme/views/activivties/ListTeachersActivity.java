package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.Teacher;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.ListTeachersPresenter;
import ru.tulupov.alex.teachme.views.adapters.TeachersAdapter;

import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_CITY;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_DISTANCE_LEARNING;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_EXPERIENCE;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_LEAVE_HOUSE;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_PHOTO;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_PRICE;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_START_PRICE;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_SUBJECT;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_SUBWAY;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_FAVORITE;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_FULL;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_MY_CITY;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_QUICK;

public class ListTeachersActivity extends BaseActivity implements ListTeachersView {

    protected User user;
    protected ListTeachersPresenter presenter;
    protected RecyclerView recyclerView;
    protected LinearLayoutManager manager;

    protected TextView tvSearchNothing;
    protected int typeSearch;

    protected int pages = 0;
    protected boolean isLastPage = false;
    protected boolean teachersAreDownloading = false;
    protected int cityId = 1;
    protected int subjectId = 1;
    protected int expId;
    protected int price;
    protected int startPrice;
    protected String subwaysIds;
    protected boolean distanceLearning;
    protected boolean leaveHouse;
    protected boolean isPhoto;
    protected Map<String, String> mapFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_teachers);

        initToolbar(R.string.result_search_activity_title, R.id.toolbarListTeachers);
        user = MyApplications.getUser();
        Intent intent = getIntent();
        presenter = new ListTeachersPresenter();
        presenter.onCreate(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewTeachers);
        tvSearchNothing = (TextView) findViewById(R.id.searchNothing);

        int typeSearch = intent.getIntExtra(TYPE_SEARCH, -1);
        if (typeSearch == TYPE_SEARCH_MY_CITY) {
            cityId = user.getCityId(this);
            presenter.getTeachersByCity(cityId);
            teachersAreDownloading = true;
        }

        if (typeSearch == TYPE_SEARCH_QUICK) {
            cityId = intent.getIntExtra(SEARCH_FIELD_CITY, -1);
            subjectId = intent.getIntExtra(SEARCH_FIELD_SUBJECT, -1);
            leaveHouse = intent.getBooleanExtra(SEARCH_FIELD_LEAVE_HOUSE, false);
            presenter.getTeachersQuickSearch(cityId, leaveHouse, subjectId);
            teachersAreDownloading = true;
        }

        if (typeSearch == TYPE_SEARCH_FULL) {
            cityId = intent.getIntExtra(SEARCH_FIELD_CITY, -1);
            subjectId = intent.getIntExtra(SEARCH_FIELD_SUBJECT, -1);
            leaveHouse = intent.getBooleanExtra(SEARCH_FIELD_LEAVE_HOUSE, false);
            expId = intent.getIntExtra(SEARCH_FIELD_EXPERIENCE, -1);
            isPhoto = intent.getBooleanExtra(SEARCH_FIELD_PHOTO, false);
            price = intent.getIntExtra(SEARCH_FIELD_PRICE, -1);
            startPrice = intent.getIntExtra(SEARCH_FIELD_START_PRICE, -1);
            distanceLearning = intent.getBooleanExtra(SEARCH_FIELD_DISTANCE_LEARNING, false);
            subwaysIds = intent.getStringExtra(SEARCH_FIELD_SUBWAY);
            mapFields = wrapQuery();
            presenter.getTeachersFullSearch(mapFields);
            teachersAreDownloading = true;
        }

        if (typeSearch == TYPE_SEARCH_FAVORITE) {
            teachersAreDownloading = true;
            if (checkConnection()) {
                String accessToken = MyApplications.getUser().getAccessToken(this);
                presenter.getFavoriteTeachers(accessToken);
            } else {
                presenter.getListFavoriteTeachers(this);
            }
        }


        this.typeSearch = typeSearch;

        addNewItemsByScroll();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected Map<String, String> wrapQuery () {
        Map<String, String> map = new HashMap<>();

        int leave = 0;
        int photo = 0;
        if (leaveHouse) leave = 1;
        if (isPhoto) photo = 1;

        map.put("city", String.valueOf(cityId));
        map.put("subject", String.valueOf(subjectId));
        map.put("leaveHouse", String.valueOf(leave));
        map.put("photo", String.valueOf(photo));
        map.put("experience", String.valueOf(expId));
        map.put("price", String.valueOf(price));
        map.put("priceStart", String.valueOf(startPrice));
        map.put("distanceLearning", String.valueOf(distanceLearning));
        map.put("subways", subwaysIds);
        map.put("page", String.valueOf(pages));

        Log.d(Constants.MY_TAG, cityId + " " + subjectId + " " + leave + " " + photo + " " + expId + " " + price  + " | " + subwaysIds + " | " + " " + pages);

        return map;
    }

    @Override
    public void showListTeachers(List<Teacher> teacherList) {
        if (teacherList.size() > 0) {
            manager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);
            TeachersAdapter adapter = new TeachersAdapter(teacherList, this);
            recyclerView.setAdapter(adapter);
            teachersAreDownloading = false;
            pages++;
        } else {
            tvSearchNothing.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void errorListTeachers() {

    }

    @Override
    public void addToListTeachers(List<Teacher> teacherList) {
        if (teacherList.size() > 0) {
            TeachersAdapter teachersAdapter = (TeachersAdapter) recyclerView.getAdapter();
            teachersAdapter.addNewItems(teacherList);
            teachersAreDownloading = false;
        } else {
            isLastPage = true;
        }
    }

    @Override
    public void errorAddListTeachers() {

    }

    protected void addNewItemsByScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    int visibleItemCount = manager.getChildCount();
                    int totalItemCount   = manager.getItemCount();
                    int pastVisibleItems = manager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount
                            && !teachersAreDownloading && !isLastPage){
                        teachersAreDownloading = true;
                        addNewItems();
                        pages++;
                    }
                }
            }
        });
    }

    protected void addNewItems() {
        if (typeSearch == TYPE_SEARCH_MY_CITY) {
            presenter.addTeachersByCity(cityId, pages);
        } else if (typeSearch == TYPE_SEARCH_FULL){
            mapFields.put("page", String.valueOf(pages));
            presenter.addTeachersFullSearch(mapFields);
        } else if (typeSearch == TYPE_SEARCH_QUICK) {
            presenter.addTeachersQuickSearch(cityId, leaveHouse, subjectId, pages);
        }
    }
}
