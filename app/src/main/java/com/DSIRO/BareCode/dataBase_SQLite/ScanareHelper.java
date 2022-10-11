package com.DSIRO.BareCode.dataBase_SQLite;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class ScanareHelper extends SQLiteOpenHelper {

    public static final String SCAN_TABLE = "TABEL_SCAN";
    public static final String ID = "ID";
    public static final String COD_STAMPILA = "USER";
    public static final String BON_PIKING = "COD_BON";
    public static final String ETICHETA_ORIGINE = "COD_REPER";
    public static final String UV = "UV";
    public static final String DATA_PROCESARE = "DATA_PROCESARE";
    public static final String ORA_PROCESARE = "ORA_PROCESARE";
    private static final String TAG = "CSV";
    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static final boolean READ_CONTACTS_GRANTED = false;
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE,
            Manifest.permission.RECORD_AUDIO,
    };
    private final int EXTERNAL_STORAGE_PERMISSION_CODE = 23;


    public ScanareHelper(@Nullable Context context) {
        super(context, "TabelScan.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String createTable = "CREATE TABLE " + SCAN_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COD_STAMPILA + " TEXT, " + BON_PIKING + " TEXT, " + ETICHETA_ORIGINE + " TEXT, " + UV + " TEXT," + DATA_PROCESARE + " TEXT," + ORA_PROCESARE + " TEXT)";
        sqLiteDatabase.execSQL(createTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public boolean adaugaScanare(ScanareModel scanareModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(COD_STAMPILA, scanareModel.getUser());
        cv.put(BON_PIKING, scanareModel.getCod_bon());
        cv.put(ETICHETA_ORIGINE, scanareModel.getCod_reper());
        cv.put(UV, scanareModel.getUv());
        cv.put(DATA_PROCESARE, scanareModel.getData_procesare());
        cv.put(ORA_PROCESARE, scanareModel.getOra_procesare());

        long insert = db.insert(SCAN_TABLE, null, cv);
        return insert != -1;
    }

    public List<ScanareModel> getScanari() {
        List<ScanareModel> listaScanari = new ArrayList<>();
        String querry = "SELECT * FROM " + SCAN_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querry, null);

        if (cursor.moveToFirst()) {
            do {
                int scanId = cursor.getInt(0);
                String codStampila = cursor.getString(1);
                String bonPiking = cursor.getString(2);
                String etichetaOrigine = cursor.getString(3);
                String uv = cursor.getString(4);
                String data_procesare = cursor.getString(5);
                String ora_procesare = cursor.getString(6);

                ScanareModel userModel = new ScanareModel(scanId, codStampila, bonPiking, etichetaOrigine, uv, data_procesare, ora_procesare);
                listaScanari.add(userModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaScanari;

    }

    public boolean deleteScanare(ScanareModel scanareModel) {
        SQLiteDatabase db = this.getReadableDatabase();
        String querry = "DELETE FROM " + SCAN_TABLE + " WHERE " + ID + " = " + scanareModel.getId();
        Cursor cursor = db.rawQuery(querry, null);

        return cursor.moveToFirst();
    }

    public boolean deleteAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        String querry = "DELETE FROM " + SCAN_TABLE;
        Cursor cursor = db.rawQuery(querry, null);

        return cursor.moveToFirst();
    }

    public void exportDB(Context context) {


        ScanareHelper dbhelper = new ScanareHelper(context);
        // File exportDir = new File(Environment.getExternalStorageDirectory()., "");


        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Dacia/BareCode");
        // File exportDir = new File(context.getFilesDir().getPath(), "");
        //File exportDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "");
        Log.d(TAG, "exportDB: " + context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        File file = new File(exportDir, "export-piking_" + today.monthDay + "_" + (today.month + 1) + "_" + today.year + "_" + today.format("%k:%M:%S") + ".csv");
        try {


            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + SCAN_TABLE, null);


            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                int scanId = curCSV.getInt(0);
                String codStampila = curCSV.getString(1);
                String bonPiking = curCSV.getString(2);
                String etichetaOrigine = curCSV.getString(3);
                String uv = curCSV.getString(4);
                String dataProcesare = curCSV.getString(5);
                String oraProcesare = curCSV.getString(6);
                String[] arrStr = {String.valueOf(scanId), codStampila, bonPiking, etichetaOrigine, uv, dataProcesare, oraProcesare};

                csvWrite.writeNext(arrStr);
                Toast.makeText(context, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), Toast.LENGTH_SHORT).show();

                Log.d(TAG, "exportDB: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("CSV", sqlEx.getMessage(), sqlEx);

        }


    }


}
