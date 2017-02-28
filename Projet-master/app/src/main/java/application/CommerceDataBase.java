package application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CommerceDataBase extends SQLiteOpenHelper {

    private static final String TAG = CommerceDataBase.class.getSimpleName();

    public CommerceDataBase(Context context) {
        super(context, constants.DATABASE.DB_NAME, null, constants.DATABASE.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(constants.DATABASE.CREATE_TABLE_QUERY);
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(constants.DATABASE.DROP_QUERY);
        this.onCreate(db);
    }

    public boolean insertData(String coord_geo, String tco_libelle, String jour_fermeture, String recordid, String ville, String code_postal, String datasetid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("datasetid", datasetid);
        values.put("recordid", recordid);
        values.put("jour_fermeture", jour_fermeture);
        values.put("ville", ville);
        values.put("code_postal", code_postal);
        values.put("tco_libelle", tco_libelle);
        values.put("coord_geo", coord_geo);
        long newRowId = db.insert(constants.DATABASE.TABLE_NAME, null, values);
        if (newRowId == -1)
            return false;
        else
            return true;
    }

    public List<Commerce> getAllCommerce(SQLiteDatabase db) {
        List<Commerce> comList = new ArrayList<Commerce>();
        // Select All Query
        //SQLiteDatabase database =this.getWritableDatabase();
       Cursor cursor = db.rawQuery("select * from " + constants.DATABASE.TABLE_NAME, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Commerce commerce = new Commerce();
                commerce.setDatasetid(cursor.getString(cursor.getColumnIndex(constants.DATABASE.datasetid)));
                commerce.setRecordid(cursor.getString(cursor.getColumnIndex(constants.DATABASE.recordid)));
                commerce.setJour_fermeture(cursor.getString(cursor.getColumnIndex(constants.DATABASE.jour_fermeture)));
                commerce.setVille(cursor.getString(cursor.getColumnIndex(constants.DATABASE.ville)));
                commerce.setCode_postal(cursor.getString(cursor.getColumnIndex(constants.DATABASE.code_postal)));
                commerce.setTco_libelle(cursor.getString(cursor.getColumnIndex(constants.DATABASE.tco_libelle)));
                commerce.setCoord_geo(cursor.getString(cursor.getColumnIndex(constants.DATABASE.coord_geo)));

                comList.add(commerce);
            } while (cursor.moveToNext());
        }

        return comList;
    }

    public Commerce getCommerceByCoord(String coord) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from records where coord_geo = '" + coord + "'", null);
        Commerce commerce = new Commerce();
        if (cursor.moveToFirst()) {
            do {
                commerce.setDatasetid(cursor.getString(cursor.getColumnIndex(constants.DATABASE.datasetid)));
                commerce.setRecordid(cursor.getString(cursor.getColumnIndex(constants.DATABASE.recordid)));
                commerce.setJour_fermeture(cursor.getString(cursor.getColumnIndex(constants.DATABASE.jour_fermeture)));
                commerce.setVille(cursor.getString(cursor.getColumnIndex(constants.DATABASE.ville)));
                commerce.setCode_postal(cursor.getString(cursor.getColumnIndex(constants.DATABASE.code_postal)));
                commerce.setTco_libelle(cursor.getString(cursor.getColumnIndex(constants.DATABASE.tco_libelle)));
                commerce.setCoord_geo(cursor.getString(cursor.getColumnIndex(constants.DATABASE.coord_geo)));

                // comList.add(commerce);
            } while (cursor.moveToNext());

        }
        //  return comList;
        //}
        return commerce;
    }
}