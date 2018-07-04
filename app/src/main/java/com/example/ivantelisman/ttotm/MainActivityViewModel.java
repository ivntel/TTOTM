package com.example.ivantelisman.ttotm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.ivantelisman.ttotm.db.AppDatabase;
import com.example.ivantelisman.ttotm.db.User;
import com.example.ivantelisman.ttotm.db.utils.DatabaseInitializer;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private LiveData<List<User>> mUsers;

    public AppDatabase mDb;

    public MainActivityViewModel(Application application) {
        super(application);
        createDb();
    }

    public void createDb() {
        mDb = AppDatabase.getInMemoryDatabase(this.getApplication());

        // Populate it with initial data
        DatabaseInitializer.populateAsync(mDb);

        // Receive changes
        subscribeToDbChanges();
    }

    public LiveData<List<User>> getUsers() {
        return mUsers;
    }

    private void subscribeToDbChanges() {
        // Users is a LiveData object so updates are observed.
        // TODO: replace this with a query that searches for users by age
        mUsers = mDb.userModel().loadAllUsers();
    }
}

