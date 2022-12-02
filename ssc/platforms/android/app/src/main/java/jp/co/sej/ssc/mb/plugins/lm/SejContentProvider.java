package jp.co.sej.ssc.mb.plugins.lm;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * @author gaoxingqiang
 */
public class SejContentProvider extends ContentProvider {

    static final String PROVIDER_NAME = "jp.co.sej.ssc.mb.SejContent";
    public static final String URL = "content://" + PROVIDER_NAME + "/content";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String ID = "_id";
    public static final String NAME = "key1";
    public static final String VALUE = "value1";

    private static HashMap<String, String> SEJ_PROJECTION_MAP;

    static final int CONTENT = 1;
    static final int CONTENT_ID = 2;

    static final UriMatcher URIMATCHER;

    static {
        URIMATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URIMATCHER.addURI(PROVIDER_NAME, "content", CONTENT);
        URIMATCHER.addURI(PROVIDER_NAME, "content/#", CONTENT_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "SejContent";
    static final String SEJ_TABLE_NAME = "content";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + SEJ_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " key1 TEXT NOT NULL, " +
                    " value1 TEXT NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SEJ_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SEJ_TABLE_NAME);

        switch (URIMATCHER.match(uri)) {
            case CONTENT:
                qb.setProjectionMap(SEJ_PROJECTION_MAP);
                break;

            case CONTENT_ID:
                qb.appendWhere(ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || "".equals(sortOrder)) {
            sortOrder = NAME;
        }
        Cursor c = qb.query(db, strings, s, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URIMATCHER.match(uri)) {

            case CONTENT:
                return "vnd.android.cursor.dir/vnd.example.content";
            case CONTENT_ID:
                return "vnd.android.cursor.item/vnd.example.content";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long rowId = db.insert(SEJ_TABLE_NAME, "", contentValues);

        if (rowId > 0) {
            Uri myUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(myUri, null);
            return myUri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int count;

        switch (URIMATCHER.match(uri)) {
            case CONTENT:
                count = db.delete(SEJ_TABLE_NAME, s, strings);
                break;

            case CONTENT_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(SEJ_TABLE_NAME, ID + " = " + id +
                        (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""), strings);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int count;

        switch (URIMATCHER.match(uri)) {
            case CONTENT:
                count = db.update(SEJ_TABLE_NAME, contentValues, s, strings);
                break;

            case CONTENT_ID:
                count = db.update(SEJ_TABLE_NAME, contentValues, ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""), strings);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
