package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.Teacher;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.ListTeachersPresenter;
import ru.tulupov.alex.teachme.views.adapters.TeachersAdapter;

import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_MY_CITY;

public class ListTeachersActivity extends BaseActivity implements ListTeachersView {

    User user;
    ListTeachersPresenter presenter;
    RecyclerView recyclerView;
    LinearLayoutManager manager;

    int pages = 0;
    boolean isLastPage = false;
    boolean teachersAreDownloading = false;
    int cityId = 1;

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

        int typeSearch = intent.getIntExtra(TYPE_SEARCH, -1);
        if (typeSearch == TYPE_SEARCH_MY_CITY) {
            cityId = user.getCityId(this);
            presenter.getTeachersByCity(cityId);
            teachersAreDownloading = true;
        }

        addNewItemsByScroll();

    }

    @Override
    public void showListTeachers(List<Teacher> teacherList) {
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        TeachersAdapter adapter = new TeachersAdapter(teacherList, this);
        recyclerView.setAdapter(adapter);
        teachersAreDownloading = false;
        pages++;
    }

    @Override
    public void errorListTeachers() {
        Toast.makeText(this, "dsasdasd", Toast.LENGTH_SHORT).show();
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
                        teachersAreDownloading= true;
                        presenter.addTeachersByCity(cityId, pages);
                        pages++;
                    }
                }
            }
        });
    }
}
