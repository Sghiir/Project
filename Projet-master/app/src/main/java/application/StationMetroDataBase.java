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

/**
 * Created by PC on 2/21/2017.
 */

public class StationMetroDataBase extends SQLiteOpenHelper {

        private static final String TAG = CommerceDataBase.class.getSimpleName();

        public StationMetroDataBase(Context context) {

        super(context, StationConstants.StationDATABASE.DB_NAME2, null, StationConstants.StationDATABASE.DB_VERSION2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(StationConstants.StationDATABASE.CREATE_TABLE_QUERY2);
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(StationConstants.StationDATABASE.DROP_QUERY2);
        this.onCreate(db);
    }

    public boolean insertData(String nom_station_gare, String coord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom_station_gare", nom_station_gare);
        values.put("coord", coord);
        long newRowId = db.insert(StationConstants.StationDATABASE.TABLE_NAME2, null, values);
        if (newRowId == -1)
            return false;
        else
            return true;
    }

    public List<StationMetroGare> getAllStations() {
        List<StationMetroGare> stationList = new ArrayList<StationMetroGare>();
           SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(StationConstants.StationDATABASE.GET_Station_QUERY2, null);
        if (cursor.moveToFirst()) {
            do {
                StationMetroGare station= new StationMetroGare();
                station.setNom(cursor.getString(cursor.getColumnIndex(StationConstants.StationDATABASE.nom)));
                station.setCoord_geo(cursor.getString(cursor.getColumnIndex(StationConstants.StationDATABASE.coord_geo)));
                stationList.add(station);
            } while (cursor.moveToNext());
        }
        return stationList;
    }

    public StationMetroGare getStationCoord(String nom) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from records where nom_station_gare = '" + nom + "'", null);
        StationMetroGare station = new StationMetroGare();
        if (cursor.moveToFirst()) {
            do {
                station.setCoord_geo(cursor.getString(cursor.getColumnIndex(StationConstants.StationDATABASE.coord_geo)));
            } while (cursor.moveToNext());
        }
        return station;
    }
}