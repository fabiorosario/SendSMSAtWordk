package fabiorosario.ime.pgsc.sendsmstowork_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class EnviarSMSDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "EnviarSMS.db";
    private static EnviarSMSDbHelper dbHelper;

    public static EnviarSMSDbHelper getDbHelper(Context context){
        if (dbHelper == null)
            dbHelper = new EnviarSMSDbHelper(context);
        return dbHelper;
    }
    private EnviarSMSDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EnviarSMSContrato.EsquemaBD.TABLE_NAME + " (" +
                    EnviarSMSContrato.EsquemaBD._ID + " INTEGER PRIMARY KEY," +
                    EnviarSMSContrato.EsquemaBD.COLUMN_NAME_DATA + " REAL," +
                    EnviarSMSContrato.EsquemaBD.COLUMN_NAME_SSID + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EnviarSMSContrato.EsquemaBD.TABLE_NAME;

    public static Boolean atualizarSSID(SQLiteOpenHelper dbHelper, String idSSID, String ssid, Date data){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EnviarSMSContrato.EsquemaBD.COLUMN_NAME_SSID, ssid);
        values.put(EnviarSMSContrato.EsquemaBD.COLUMN_NAME_DATA, data.getTime());

        int quantidadeAtualizacoes = 0;
        long quantidadeInsercoes = 0;

        if (ssidConfigurada(dbHelper, ssid)){
            String selection = EnviarSMSContrato.EsquemaBD._ID + " = ?";
            String[] selectionArgs = { idSSID };
            quantidadeAtualizacoes = db.update(
                    EnviarSMSContrato.EsquemaBD.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
        else
            quantidadeInsercoes = db.insert(EnviarSMSContrato.EsquemaBD.TABLE_NAME, null, values);

        if (quantidadeAtualizacoes > 0 || quantidadeInsercoes > 0)
            return true;
        return false;
    }

    public static Boolean ssidConfigurada(SQLiteOpenHelper dbHelper, String ssid){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EnviarSMSContrato.EsquemaBD.COLUMN_NAME_SSID,
        };

        Cursor cursor = db.query(
                EnviarSMSContrato.EsquemaBD.TABLE_NAME, projection,null, null,
                null, null, null
        );

        Boolean encontrou = false;
        if (cursor != null && cursor.moveToFirst()){
            encontrou = true;
        }
        cursor.close();

        return encontrou;
    }
    public static Boolean atualizarDataEnvioSMS(SQLiteOpenHelper dbHelper, long data, String ssid){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EnviarSMSContrato.EsquemaBD.COLUMN_NAME_DATA, data);

        String selection = EnviarSMSContrato.EsquemaBD.COLUMN_NAME_SSID + " = ?";
        String[] selectionArgs = { ssid };

        int count = db.update(
                EnviarSMSContrato.EsquemaBD.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count == 0)
            return false;
        return true;
    }
    public static String[] informacoesEnvioSMS(SQLiteOpenHelper dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                EnviarSMSContrato.EsquemaBD._ID,
                EnviarSMSContrato.EsquemaBD.COLUMN_NAME_SSID,
                EnviarSMSContrato.EsquemaBD.COLUMN_NAME_DATA,
        };

        Cursor cursor = db.query(
                EnviarSMSContrato.EsquemaBD.TABLE_NAME, projection, null, null,
                null, null, null
        );

        Integer id = 0;
        String ssid = "";
        long data = 0;
        if (cursor != null && cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndexOrThrow(EnviarSMSContrato.EsquemaBD._ID));
            ssid = cursor.getString(cursor.getColumnIndexOrThrow(EnviarSMSContrato.EsquemaBD.COLUMN_NAME_SSID));
            data = cursor.getLong(cursor.getColumnIndexOrThrow(EnviarSMSContrato.EsquemaBD.COLUMN_NAME_DATA));
        }
        cursor.close();

        return new String[] {String.valueOf(id), ssid, String.valueOf(data)};
    }
}
