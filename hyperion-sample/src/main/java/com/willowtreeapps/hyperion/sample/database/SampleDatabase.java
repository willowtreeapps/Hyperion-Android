package com.willowtreeapps.hyperion.sample.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

@Database(entities = {UsersEntity.class}, version = 2, exportSchema = false)
public abstract class SampleDatabase extends RoomDatabase {
    public static final String NAME = "sample_db";

    private static SampleDatabase INSTANCE;

    public synchronized static SampleDatabase getInstance(@NonNull final Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDb(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private static SampleDatabase buildDb(final Context context) {
        return Room.databaseBuilder(context,
                SampleDatabase.class, SampleDatabase.NAME)
                .fallbackToDestructiveMigration()
                //Pre-populating database with some sample data
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull final SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).getUserDao()
                                        .insert(createSampleRecords());
                            }
                        }) .start();
                    }
                })
                .build();
    }

    private static UsersEntity[] createSampleRecords() {

        int limit = 100;
        UsersEntity[] entities = new UsersEntity[limit];

        UsersEntity.Builder builder = UsersEntity.newBuilder();

        for (int i = 0; i < limit; i++) {
            entities[i] = builder.withUserName("User "  + i)
                    .withFirstName("User")
                    .withLastName(String.valueOf(i))
                    .withJoinTimestamp(System.currentTimeMillis())
                    .withProfileImage("https://www.profileimage.com/" + System.currentTimeMillis())
            .build();
        }

        return entities;
    }

    public abstract UsersEntity.UserDao getUserDao();
}
