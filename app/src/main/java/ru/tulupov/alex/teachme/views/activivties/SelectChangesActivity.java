package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.tulupov.alex.teachme.R;

public class SelectChangesActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_changes);


        findViewById(R.id.btn_changes_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectChangesActivity.this, ChangeTeacherProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_changes_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectChangesActivity.this, ChangeEmailActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_changes_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectChangesActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        initToolbar(getString(R.string.select_changes_activity_title), R.id.toolbarSelectChanges);
        initNavigationView();
    }
}
