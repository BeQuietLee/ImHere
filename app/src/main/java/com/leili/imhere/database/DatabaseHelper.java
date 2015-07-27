package com.leili.imhere.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leili.imhere.entity.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lei.Li on 7/27/15 9:24 AM.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "Position";

    public DatabaseHelper (Context context) {
        super(context, "Position.db", null, 1);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (`_id` INT AUTO_INCREMENT, `tencent_id` VARCHAR(24), `latitude` VARCHAR(10), `longitude` VARCHAR(10), `title` VARCHAR(24), `address` VARCHAR(64), PRIMARY KEY (`_id`));");
        db.execSQL(sb.toString());
    }

    public void insertPosition(String tencentId, String title, String address, double latitude, double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tencent_id", tencentId);
        cv.put("latitude", Double.toString(latitude));
        cv.put("longitude", Double.toString(longitude));
        cv.put("title", title);
        cv.put("address", address);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public List<Position> loadLikedPositions() {
        String[] params = {"tencent_id", "title", "address", "latitude", "longitude"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, params, null, null, null, null, null);
        List<Position> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Position position = Position.from(cursor.getString(0), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)), Double.parseDouble(cursor.getString(4)));
                result.add(position);
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    public void deletePosition(Position position) {
        String[] params = new String[] {position.getTencentId()};
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "tencent_id = ?", params);
        db.close();
    }
}
