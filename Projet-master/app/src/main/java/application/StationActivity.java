package application;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.pc.maptest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StationActivity extends FragmentActivity {

    CommerceDataBase Database = new CommerceDataBase(this);
    public static String geofilterdistance = null ;
    public static String Nom_Station=null;

    private String TAG = MapsActivity.class.getSimpleName();
    ArrayList<HashMap<String, String>> CommerceList;
    String Latitude;
    String Longitude;
    public static List<StationMetroGare> Lesstations=new ArrayList<StationMetroGare>();
    public static String Coord_Station=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //setContentView(R.layout.activity_main);
        SQLiteDatabase db = Database.getWritableDatabase();
        CommerceList = new ArrayList<>();
        //lv = (ListView) findViewById(R.id.list);


        Intent intent = getIntent();
        geofilterdistance = intent.getStringExtra("Geofilter_distance");
        Nom_Station = intent.getStringExtra("Nom_Station");
        Toast.makeText(StationActivity.this, Nom_Station, Toast.LENGTH_SHORT).show();

        GPSTracker gps = new GPSTracker(this);
        Latitude = Double.toString(gps.getLatitude());
        Longitude = Double.toString(gps.getLongitude());

        new StationActivity.GetContacts().execute();
    }

    public class GetContacts extends AsyncTask<Void,Void,List<StationMetroGare>> {
        String url1;
        String jsonStr1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(StationActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected List<StationMetroGare> doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            url1 = "https://data.ratp.fr/api/records/1.0/search/?dataset=accessibilite-des-gares-et-stations-metro-et-rer-ratp&facet=departement&facet=accessibilite_ufr&facet=annonce_sonore_prochain_passage&facet=annonce_visuelle_prochain_passage&facet=annonce_sonore_situations_perturbees&facet=annonce_visuelle_situations_perturbees";

            jsonStr1 = sh.makeServiceCall(url1);
            Log.e(TAG, "Response from url: " + jsonStr1);

            if (jsonStr1 != null) {
                try {
                    JSONObject jsonObj1 = new JSONObject(jsonStr1);
                    JSONArray stations = jsonObj1.getJSONArray("records");
                    String nom_station_gare, coord_geo;

                    for (int i = 0; i < stations.length(); i++) {
                        JSONObject s = stations.getJSONObject(i);
                        JSONObject fields = s.getJSONObject("fields");
                        nom_station_gare = fields.optString("nom_station_gare");
                        coord_geo = fields.optString("coord");

                        //Database2.insertData(coord_geo, nom_station_gare);
                        StationMetroGare station = new StationMetroGare();
                        station.setNom(nom_station_gare);
                        station.setCoord_geo(coord_geo);
                        Lesstations.add(station);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return Lesstations;
        }

        @Override
        protected void onPostExecute(List<StationMetroGare> result) {
            super.onPostExecute(result);
            for (StationMetroGare station : Lesstations) {
                if (station.getNom().equals(Nom_Station)) {

                    Log.d("condition", station.getNom());
                    Coord_Station = station.getCoord_geo();
                    String[] XY = Coord_Station.split("[\\[\\],]");
                    String X = XY[1];
                    String Y = XY[2];
                    Double lastLatStation = Double.valueOf(X);
                    Double lastLngStation = Double.valueOf(Y);

                    Intent intent = new Intent(StationActivity.this, MapsActivity.class);

                    intent.putExtra("lotstation", lastLatStation);
                    intent.putExtra("longtation", lastLngStation);
                    intent.putExtra("station", station.getNom());

                    startActivity(intent);
                }
            }
        }
    }
}
