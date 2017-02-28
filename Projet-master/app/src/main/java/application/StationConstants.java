package application;

import android.provider.BaseColumns;

/**
 * Created by PC on 2/21/2017.
 */

public class StationConstants {

    public static final class StationDATABASE implements BaseColumns {

        public static final String DB_NAME2 = "Stations";
        public static final int DB_VERSION2 = 2;
        public static final String TABLE_NAME2 = "stations";

        public static final String DROP_QUERY2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;

        public static final String GET_Station_QUERY2 = "SELECT * FROM " + TABLE_NAME2 ;

        public static final String nom = "nom";
        public static final String coord_geo = "coord";

                public static final String CREATE_TABLE_QUERY2 = "CREATE TABLE " + TABLE_NAME2 + "("
                + _ID + " TEXT not null,"
                + nom + " TEXT not null,"
                + coord_geo + " TEXT not null)";
    }
}
