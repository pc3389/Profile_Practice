package android.example.profile_practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.example.profile_practice.Model.Profile;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class MainActivity extends AppCompatActivity {
    private Realm realm;
    public static Context context;

    private RecyclerView recyclerView;
    private List<Profile> profileList;
    private Button addButton;
    private Button deleteButton;

    private RealmHelper helper;
    private RealmChangeListener realmChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        realm = Realm.getDefaultInstance();

        recyclerView = findViewById(R.id.profile_rc);
        addButton = findViewById(R.id.button_add);
        deleteButton = findViewById(R.id.button_deleteAll);

        helper = new RealmHelper(realm);
        helper.selectFromDb();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ProfileAdapter profileAdapter = new ProfileAdapter(this, helper.retireve());
        recyclerView.setAdapter(profileAdapter);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditorActivity.class);
                context.startActivity(intent);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.deleteAll();
            }
        });
        refresh();
    }

    private void refresh() {
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                ProfileAdapter adapter = new ProfileAdapter(MainActivity.this, helper.retireve());
                recyclerView.setAdapter(adapter);
            }
        };
        realm.addChangeListener(realmChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.removeChangeListener(realmChangeListener);
        realm.close();
    }
}