package com.example.task81;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task8_1.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PLAYLIST = "playlist";

    // Common column names
    private static final String COLUMN_ID = "id";

    // Users table columns
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Playlist table columns
    private static final String COLUMN_VIDEO_URL = "video_url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "users" table
        String createUsersTableQuery = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULL_NAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTableQuery);

        // Create the "playlist" table
        String createPlaylistTableQuery = "CREATE TABLE " + TABLE_PLAYLIST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VIDEO_URL + " TEXT)";
        db.execSQL(createPlaylistTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // required
    }


    public void addUser(String fullName, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean checkUserExistence(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return userExists;
    }

    public boolean validateUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + " = ? AND " +
                COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean isValidCredentials = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValidCredentials;
    }


    public void addVideoToPlaylist(String videoUrl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VIDEO_URL, videoUrl);

        db.insert(TABLE_PLAYLIST, null, values);
        db.close();
    }

    public List<String> getPlaylist() {
        List<String> playlist = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_PLAYLIST;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String videoUrl = cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_URL));
                playlist.add(videoUrl);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return playlist;
    }

    public void removeVideoFromPlaylist(String videoUrl) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = COLUMN_VIDEO_URL + " = ?";
        String[] whereArgs = {videoUrl};

        db.delete(TABLE_PLAYLIST, whereClause, whereArgs);
        db.close();
    }

    public void open() {
    }
}
