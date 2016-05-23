package es.agustruiz.tddm.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.agustruiz.tddm.model.Notification;

public class NotificationDAO {

    public static final String LOG_TAG = NotificationDAO.class.getName() + "[A]";

    //region [Variables]

    private static final String TABLE_NAME = "notification";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_MESSAGE = "message";
    private static final String COL_TIMESTAMP = "timestamp";

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DatabaseManager mDatabaseManager;

    //endregion

    //region [Public methods]

    public NotificationDAO(Context context) {
        mContext = context;
        mDatabase = null;
        mDatabaseManager = new DatabaseManager(mContext);
    }

    public void openWritable() {
        mDatabase = mDatabaseManager.getWritableDatabase();
    }

    public void openReadOnly() {
        mDatabase = mDatabaseManager.getReadableDatabase();
    }

    public void close() {
        mDatabase.close();
        mDatabase = null;
    }

    public List<Notification> getAll() {
        List<Notification> result = new ArrayList<>();
        String query = "select * from " + TABLE_NAME;
        Cursor cursor = mDatabase.rawQuery(query, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                result.add(buildNotificationFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public Notification get(long id) {
        Notification result = null;
        String query = "select * from " + TABLE_NAME + " where " + COL_ID + "=?";
        String[] params = new String[]{String.valueOf(id)};
        Cursor cursor = mDatabase.rawQuery(query, params);
        if (cursor.moveToFirst()) {
            result = buildNotificationFromCursor(cursor);
        }
        cursor.close();
        return result;
    }

    public boolean add(Notification notification) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, notification.getmTitle());
        values.put(COL_MESSAGE, notification.getMessage());
        values.put(COL_TIMESTAMP, notification.getTimestamp());
        return mDatabase.insert(TABLE_NAME, null, values) > 0;
    }

    public boolean delete(Notification notification) {
        return delete(notification.getId());
    }

    public boolean delete(long id) {
        String whereClause = COL_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        int result = mDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        return result > 0;
    }

    //endregion

    //region [Public static methods]

    public static String createTableQuery() {
        return "create table if not exists " + TABLE_NAME + " ( "
                + COL_ID + " integer not null primary key autoincrement, "
                + COL_TITLE + " varchar(255) not null, "
                + COL_MESSAGE + " varchar(255) not null, "
                + COL_TIMESTAMP + " integer null )";
    }

    public static String dropTableQuery() {
        return "drop table if exists " + TABLE_NAME;
    }

    //endregion

    //region [Private methods]

    protected Notification buildNotificationFromCursor(Cursor cursor) {
        long id = cursor.getLong(0);
        String title = cursor.getString(1);
        String message = cursor.getString(2);
        long timestamp = cursor.getLong(3);
        return new Notification(id, title, message, timestamp);

    }

    //endregion

}
