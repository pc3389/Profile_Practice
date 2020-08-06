package android.example.profile_practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.example.profile_practice.Model.Profile;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import io.realm.Realm;

public class EditorActivity extends AppCompatActivity {
    private Realm realm;
    private Profile profile;
    private RealmHelper helper;

    private Spinner mGenderSpinner;

    private EditText mNameEditText;
    private EditText mAgeEditText;
    private Button saveButton;
    private Button deleteButton;

    private int mGender = 0;
    int id;
    private boolean mProfileHasChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        realm = Realm.getDefaultInstance();
        helper = new RealmHelper(realm);
        helper.selectFromDb();

        id = getIntent().getIntExtra("id", 0);

        profile = realm.where(Profile.class)
                .equalTo("id", id)
                .findFirst();

        mNameEditText = findViewById(R.id.edit_name);
        mAgeEditText = findViewById(R.id.edit_age);
        saveButton = findViewById(R.id.button_save);
        deleteButton = findViewById(R.id.button_delete);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nullCheck();
                saveData();
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.delete(id);
                finish();
            }
        });

        mGenderSpinner = findViewById(R.id.spinner_gender);

        setupSpinner();

        bringEditInfo();
    }

    private void nullCheck() {
        if (mNameEditText == null) {
            Toast.makeText(this, "Name cannot be null", Toast.LENGTH_SHORT);
        }
    }

    private void saveData() {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setName(mNameEditText.getText().toString());
        profile.setAge(Integer.parseInt(mAgeEditText.getText().toString()));
        profile.setGender(mGender);
        RealmHelper realmHelper = new RealmHelper(realm);
        realmHelper.insert(profile);
    }

    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = 1;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = 2;
                    } else {
                        mGender = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (realm != null) {
            realm.close();
        }
        super.onDestroy();
    }

    private void bringEditInfo() {
        if (id != 0) {
            mNameEditText.setText(profile.getName());
            mAgeEditText.setText(String.valueOf(profile.getAge()));
            mGenderSpinner.setSelection(profile.getGender());
        }
    }
}