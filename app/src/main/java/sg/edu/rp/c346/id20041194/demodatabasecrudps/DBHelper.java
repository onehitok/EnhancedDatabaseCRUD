package sg.edu.rp.c346.id20041194.demodatabasecrudps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "simplesongs.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_SONG = "song";
    private static final String SONG_ID = "_id";
    private static final String SONG_TITLE = "song_title";
    private static final String SINGER = "singer";
    private static final String YEAR = "year";
    private static final String RATING = "rating";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSongTableSql = "CREATE TABLE " + TABLE_SONG + "("
                + SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SONG_TITLE + " TEXT, "
                + SINGER + " TEXT, "
                + YEAR + " INTEGER, "
                + RATING + " INTEGER) ";
        db.execSQL(createSongTableSql);

        Log.i("info", "created tables");

        //Dummy records, to be inserted when the database is created

        for (int i = 0; i< 4; i++) {
            ContentValues values = new ContentValues();
            values.put(SONG_TITLE, "Data number " + i);
            db.insert(TABLE_SONG, null, values);
        }
        Log.i("info", "dummy records inserted");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + TABLE_SONG + " ADD COLUMN  module_name TEXT ");
    }

    public long insertNote(String noteContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SONG_TITLE, noteContent);
        long result = db.insert(TABLE_SONG, null, values);
        db.close();
        Log.d("SQL Insert","ID:"+ result); //id returned, shouldn’t be -1
        return result;
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> notes = new ArrayList<Song>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns= {SONG_ID, SONG_TITLE};
        Cursor cursor = db.query(TABLE_SONG, columns, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    public int updateNote(Note data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SONG_TITLE, data.getNoteContent());
        String condition = SONG_ID + "= ?";
        String[] args = {String.valueOf(data.getId())};
        int result = db.update(TABLE_SONG, values, condition, args);
        db.close();
        return result;
    }

    public int deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = SONG_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_SONG, condition, args);
        db.close();
        return result;
    }

    public ArrayList<Note> getAllNotes(String keyword) {
        ArrayList<Note> notes = new ArrayList<Note>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns= {SONG_ID, SONG_TITLE};
        String condition = SONG_TITLE + " Like ?";
        String[] args = { "%" +  keyword + "%"};
        Cursor cursor = db.query(TABLE_SONG, columns, condition, args,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

}


