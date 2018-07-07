/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ivantelisman.ttotm.db.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.ivantelisman.ttotm.db.AppDatabase;
import com.example.ivantelisman.ttotm.db.User;

public class DatabaseInitializer {

    // Simulate a blocking operation delaying each Loan insertion with a delay:
    private static final int DELAY_MILLIS = 500;

    public static void populateAsync(final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static User addUser(final AppDatabase db, final String id, final int differenceInDays,
                                final String estimatedStartDate, final int cycleDuration, final String date) {
        User user = new User();
        user.id = id;
        user.cycleDuration = cycleDuration;
        user.differenceInDays = differenceInDays;
        user.estimatedStartDate = estimatedStartDate;
        user.date = date;
        db.userModel().insertUser(user);
        return user;
    }

    private static void populateWithTestData(AppDatabase db) {
        db.userModel().deleteAll();
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //populateWithTestData(mDb);
            return null;
        }

    }
}
