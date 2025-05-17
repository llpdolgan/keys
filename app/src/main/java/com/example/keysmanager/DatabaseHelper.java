package com.example.keysmanager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PasswordManager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PASSWORDS = "passwords";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NOTES = "notes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_PASSWORDS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_NOTES + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);
        onCreate(db);
    }

    // Добавление нового пароля
    public long addPassword(PasswordEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, entry.getTitle());
        values.put(COLUMN_USERNAME, entry.getUsername());
        values.put(COLUMN_PASSWORD, entry.getEncryptedPassword());
        values.put(COLUMN_NOTES, entry.getNotes());

        long result = db.insert(TABLE_PASSWORDS, null, values);
        db.close();
        return result;
    }

    // Получение всех паролей
    public List<PasswordEntry> getAllPasswords() {
        List<PasswordEntry> entries = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PASSWORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PasswordEntry entry = new PasswordEntry();
                entry.setId(cursor.getInt(0));
                entry.setTitle(cursor.getString(1));
                entry.setUsername(cursor.getString(2));
                entry.setEncryptedPassword(cursor.getString(3));
                entry.setNotes(cursor.getString(4));
                entries.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entries;
    }

    // Получение пароля по ID
    public PasswordEntry getPasswordById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PASSWORDS,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_NOTES},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            PasswordEntry entry = new PasswordEntry();
            entry.setId(cursor.getInt(0));
            entry.setTitle(cursor.getString(1));
            entry.setUsername(cursor.getString(2));
            entry.setEncryptedPassword(cursor.getString(3));
            entry.setNotes(cursor.getString(4));
            cursor.close();
            return entry;
        }
        return null;
    }

    // Обновление пароля
    public int updatePassword(PasswordEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, entry.getTitle());
        values.put(COLUMN_USERNAME, entry.getUsername());
        values.put(COLUMN_PASSWORD, entry.getEncryptedPassword());
        values.put(COLUMN_NOTES, entry.getNotes());

        return db.update(TABLE_PASSWORDS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(entry.getId())});
    }

    // Удаление пароля
    public void deletePassword(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PASSWORDS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}

