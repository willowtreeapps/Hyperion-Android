package com.willowtreeapps.hyperion.sample.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static com.willowtreeapps.hyperion.sample.database.UsersEntity.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class UsersEntity {

    public static final String TABLE_NAME = "USER_TABLE";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private Long id;
    @ColumnInfo(name = "username")
    private String userName;

    public UsersEntity(@NonNull String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Dao
    public interface UserDao {
        @Insert
        void insert(UsersEntity user);

        @Insert
        void insert(UsersEntity...users);
    }
}
