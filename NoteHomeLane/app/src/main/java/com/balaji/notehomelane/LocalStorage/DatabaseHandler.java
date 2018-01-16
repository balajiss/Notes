package com.balaji.notehomelane.LocalStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.balaji.notehomelane.Constants;
import com.balaji.notehomelane.NoteApplication;
import com.balaji.notehomelane.Pojo.Note;
import com.balaji.notehomelane.Utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by balaji on 16/01/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes";

    private static final String TABLE_NOTES = "notes";

    private static final String KEY_ID = "id";
    private static final String KEY_NOTE_TITLE = "title";
    private static final String KEY_NOTE_TEXT = "note_text";
    private static final String KEY_IMAGE_URI = "image_uri";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_NOTE_CREATED_DATE = "created_on";
    private static final String KEY_NOTE_UPDATED_DATE = "updated_on";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY NOT NULL," + KEY_NOTE_TITLE + " TEXT,"
                + KEY_NOTE_TEXT + " TEXT," + KEY_IMAGE_URI + " TEXT," + KEY_ACTIVE + " INTEGER," +
                KEY_NOTE_CREATED_DATE + " TEXT," + KEY_NOTE_UPDATED_DATE + " TEXT" + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNote(com.balaji.notehomelane.Pojo.Note note) {
        SQLiteDatabase db = NoteApplication.getDatabaseHandler().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTE_TITLE, note.getNoteTitle());
        values.put(KEY_NOTE_TEXT, note.getNoteText());
        values.put(KEY_IMAGE_URI, note.getImageUri());
        values.put(KEY_ACTIVE, note.getActive());
        values.put(KEY_NOTE_CREATED_DATE, Utils.getFormattedDateTime(note.getDateCreated()));
        values.put(KEY_NOTE_UPDATED_DATE, Utils.getFormattedDateTime(note.getDateUpdated()));

        db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection
    }

    public Note getNote(int id) {
        SQLiteDatabase db = NoteApplication.getDatabaseHandler().getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[]{KEY_ID,
                        KEY_NOTE_TITLE, KEY_NOTE_TEXT, KEY_IMAGE_URI, KEY_ACTIVE,
                        KEY_NOTE_CREATED_DATE, KEY_NOTE_UPDATED_DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note();
        note.setId(Integer.parseInt(cursor.getString(0)));
        note.setNoteTitle(cursor.getString(1));
        note.setNoteText(cursor.getString(2));
        note.setImageUri(cursor.getString(3));
        note.setActive(Integer.parseInt(cursor.getString(4)));
        try {
            note.setDateCreated(Utils.getTimeInMillisFromString(cursor.getString(5)));
            note.setDateUpdated(Utils.getTimeInMillisFromString(cursor.getString(6)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<Note>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;

        SQLiteDatabase db = NoteApplication.getDatabaseHandler().getWritableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, KEY_ACTIVE + " = ?", new String[]{String
                .valueOf(1)}, null, null, KEY_NOTE_CREATED_DATE + " " +Constants.DESC);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNoteTitle(cursor.getString(1));
                note.setNoteText(cursor.getString(2));
                note.setImageUri(cursor.getString(3));
                note.setActive(Integer.parseInt(cursor.getString(4)));
                try {
                    note.setDateCreated(Utils.getTimeInMillisFromString(cursor.getString(5)));
                    note.setDateUpdated(Utils.getTimeInMillisFromString(cursor.getString(6)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        return noteList;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = NoteApplication.getDatabaseHandler().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, note.getId());
        values.put(KEY_NOTE_TITLE, note.getNoteTitle());
        values.put(KEY_NOTE_TEXT, note.getNoteText());
        values.put(KEY_IMAGE_URI, note.getImageUri());
        values.put(KEY_ACTIVE, note.getActive());
        values.put(KEY_NOTE_CREATED_DATE, Utils.getFormattedDateTime(note.getDateCreated()));
        values.put(KEY_NOTE_UPDATED_DATE, Utils.getFormattedDateTime(note.getDateUpdated()));

        int status = db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return status;
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = NoteApplication.getDatabaseHandler().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, note.getId());
        values.put(KEY_ACTIVE, 0);
        db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
