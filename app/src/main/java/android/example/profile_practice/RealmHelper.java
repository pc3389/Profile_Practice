package android.example.profile_practice;

import android.example.profile_practice.Model.Profile;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;
    RealmResults<Profile> profileRealmResults;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    public void selectFromDb() {
        profileRealmResults = realm.where(Profile.class).findAll();
    }

    //Insert
    public void insert(final Profile profile) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int newKey = 0;
                Number maxId = realm.where(Profile.class).max("id");
                if(profile.getId() < 1) {
                    if(maxId == null) {
                        newKey = 1;
                    } else {newKey = maxId.intValue()+1;}
                } else newKey = profile.getId();
                profile.setId(newKey);

                realm.insertOrUpdate(profile);
            }
        });
    }

    //Read
    public List<Profile> retireve() {
        List<Profile> profileList = new ArrayList<>();
        for(Profile p : profileRealmResults) {
            profileList.add(p);
        }
        return profileList;
    }

    //DeleteAll
    public void deleteAll() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Profile> results = realm.where(Profile.class).findAll();
                results.deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.context, "Successfully deleted the data", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.context, "Failed to delete the data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Delete
    public void delete(final int id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Profile> result = realm.where(Profile.class).equalTo("id", id).findAll();
                result.deleteAllFromRealm();
            }
        });
    }



}
