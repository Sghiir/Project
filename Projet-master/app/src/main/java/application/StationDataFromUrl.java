package application;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2/21/2017.
 */

public  class StationDataFromUrl {

    private static String TAG = MapsActivity.class.getSimpleName();

    public static List<StationMetroGare> GetStationsFromUrl(String url) throws JSONException {
            List<StationMetroGare> lesstations =new ArrayList<StationMetroGare>();
        HttpHandler sh2 = new HttpHandler();
        String jsonStr = sh2.makeServiceCall(url);

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray stations = jsonObj.getJSONArray("records");
                String nom_station_gare, coord_geo;
                for (int i = 0; i < stations.length(); i++) {
                    JSONObject s = stations.getJSONObject(i);

                    JSONObject fields = s.getJSONObject("fields");
                    nom_station_gare = fields.optString("nom_station_gare");
                    coord_geo = fields.optString("coord");
                    StationMetroGare station=new StationMetroGare();
                    station.setCoord_geo(coord_geo);
                    station.setNom(nom_station_gare);
                    lesstations.add(station);
                    Log.d( "Json parsing error: ", station.getNom());
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }

    return lesstations;

    }

}