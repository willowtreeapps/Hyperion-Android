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
    @ColumnInfo
    private String firstName;
    @ColumnInfo
    private String lastName;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] profileImage;
    @ColumnInfo
    private Long joinTimestamp;

    public UsersEntity(@NonNull String userName) {
        this.userName = userName;
    }

    private UsersEntity(Builder builder) {
        id = builder.id;
        setUserName(builder.userName);
        setFirstName(builder.firstName);
        setLastName(builder.lastName);
        setProfileImage(builder.profileImage);
        setJoinTimestamp(builder.joinTimestamp);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(UsersEntity copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.userName = copy.getUserName();
        builder.firstName = copy.getFirstName();
        builder.lastName = copy.getLastName();
        builder.profileImage = copy.getProfileImage();
        builder.joinTimestamp = copy.getJoinTimestamp();

        return builder;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public Long getJoinTimestamp() {
        return joinTimestamp;
    }

    public void setJoinTimestamp(Long joinTimestamp) {
        this.joinTimestamp = joinTimestamp;
    }

    @Dao
    public interface UserDao {
        @Insert
        void insert(UsersEntity user);

        @Insert
        void insert(UsersEntity...users);
    }

    public static final class Builder {
        private Long id;
        private String userName;
        private String firstName;
        private String lastName;
        private byte[] profileImage;
        private Long joinTimestamp;

        private Builder() {
        }

        public Builder withId(Long val) {
            id = val;
            return this;
        }

        public Builder withUserName(String val) {
            userName = val;
            return this;
        }

        public Builder withFirstName(String val) {
            firstName = val;
            return this;
        }

        public Builder withLastName(String val) {
            lastName = val;
            return this;
        }

        public Builder withProfileImage(String val) {
            profileImage = val.getBytes();
            return this;
        }

        public Builder withJoinTimestamp(Long val) {
            joinTimestamp = val;
            return this;
        }

        public UsersEntity build() {
            return new UsersEntity(this);
        }
    }
}
