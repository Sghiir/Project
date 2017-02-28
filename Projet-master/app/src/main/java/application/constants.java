package application;

import android.provider.BaseColumns;

/**
 * Created by PC on 2/10/2017.
        */

public class constants {

      public static final class DATABASE implements BaseColumns {

        public static final String DB_NAME = "Commerces";
        public static final int DB_VERSION = 2;
        public static final String TABLE_NAME = "records";

        public static final String DROP_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String GET_Commerce_QUERY = "SELECT * FROM " + TABLE_NAME ;

        public static final String datasetid = "datasetid";
        public static final String recordid = "recordid";
        public static final String jour_fermeture = "jour_fermeture";
        public static final String ville = "ville";
        public static final String code_postal = "code_postal";
        public static final String tco_libelle = "tco_libelle";
        public static final String coord_geo = "coord_geo";

       // public static final String GET_CommerceByCoord_QUERY = "SELECT * FROM "+ TABLE_NAME + " WHERE"  + Coord_geo ='[48.930906,2.209379]'";

        public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "("
                + recordid + " TEXT not null,"
                + datasetid + " TEXT not null,"
                + jour_fermeture + " TEXT not null,"
                + ville + " TEXT not null,"
                + code_postal + " TEXT not null,"
                + tco_libelle + " TEXT not null,"
                + coord_geo + " TEXT not null)";
    }
}