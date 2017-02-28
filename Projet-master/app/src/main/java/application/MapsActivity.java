package application;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pc.maptest.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    CommerceDataBase Database = new CommerceDataBase(this);
    StationMetroDataBase Database2= new StationMetroDataBase(this);

    public static String geofilterdistance = null ;
    public static String Nom_Station=null;
    public static Double latstation=null;
    public static Double logstation=null;
    public static String url=null;
    public static String noms=null;


    private String TAG = MapsActivity.class.getSimpleName();
    private ListView lv;
    ArrayList<HashMap<String, String>> CommerceList;
    String Latitude;
    String Longitude;
    public static List<Commerce> Lescommerces=new ArrayList<Commerce>();
    public static String Coord_Station=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SQLiteDatabase db = Database.getWritableDatabase();
        CommerceList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        Intent intent =getIntent();
        geofilterdistance = intent.getStringExtra("Geofilter_distance");
        Nom_Station=intent.getStringExtra("Nom_Station");
        latstation=intent.getDoubleExtra("lotstation",0);
        logstation=intent.getDoubleExtra("longtation",0);
        noms=intent.getStringExtra("station");

        GPSTracker gps = new GPSTracker(this);
        Latitude=Double.toString(gps.getLatitude());
        Longitude=Double.toString(gps.getLongitude());

        new GetContacts().execute();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
         SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
         mapFragment.getMapAsync(this);
    }


    public class GetContacts extends AsyncTask<Void,Void,List<Commerce>> {
        String jsonStr1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MapsActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected List<Commerce> doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            if(latstation==0 && logstation==0)
            {
                url = "https://data.ratp.fr/api/records/1.0/search/?dataset=liste-des-commerces-de-proximite-agrees-ratp&sort=code_postal&facet=tco_libelle&facet=jour_fermeture&facet=code_postal&geofilter.distance=" + Latitude + "%2C" + Longitude + "%2C" + geofilterdistance;
            }else{
                url = "https://data.ratp.fr/api/records/1.0/search/?dataset=liste-des-commerces-de-proximite-agrees-ratp&sort=code_postal&facet=tco_libelle&facet=jour_fermeture&facet=code_postal&geofilter.distance=" + latstation + "%2C" + logstation + "%2C3000";

            }

            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray commerces = jsonObj.getJSONArray("records");
                    String jour_fermeture, ville, code_postal, tco_libelle, coord_geo;

                    for (int i = 0; i < commerces.length(); i++) {
                        JSONObject c = commerces.getJSONObject(i);
                        String datasetid = c.optString("datasetid");
                        String recordid = c.optString("recordid");

                        JSONObject fields = c.getJSONObject("fields");
                        jour_fermeture = fields.optString("jour_fermeture");
                        ville = fields.optString("ville");
                        code_postal = fields.optString("code_postal");
                        tco_libelle = fields.optString("tco_libelle");
                        coord_geo = fields.optString("coord_geo");

                        Database.insertData(jour_fermeture, tco_libelle, recordid, ville, code_postal, datasetid, coord_geo);

                        Commerce com = new Commerce();
                        com.setCoord_geo(coord_geo);
                        com.setJour_fermeture(jour_fermeture);
                        com.setTco_libelle(tco_libelle);
                        Lescommerces.add(com);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
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

                        Database2.insertData(coord_geo, nom_station_gare);
                        StationMetroGare station = new StationMetroGare();
                        station.setNom(nom_station_gare);
                        station.setCoord_geo(coord_geo);
                        if (station.nom_station_gare == Nom_Station) {
                            Coord_Station = station.getCoord_geo();
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            }
            return Lescommerces;
        }

        @Override
        protected void onPostExecute(List<Commerce> result) {
            super.onPostExecute(result);

            for (Commerce commerce : Lescommerces) {
                String position = commerce.getCoord_geo();
                String[] oxoy = position.split("[\\[\\],]");
                String x = oxoy[1];
                String y = oxoy[2];
                Double lastLat = Double.valueOf(x);
                Double lastLng = Double.valueOf(y);

                if (lastLat + lastLng != 0) {
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(lastLat, lastLng)).title("Nom: "+commerce.getTco_libelle()+" Fermé le: "+commerce.getJour_fermeture());
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mCurrLocationMarker = mMap.addMarker(marker);
              }
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lastLat, lastLng)).zoom(13).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        Latitude = Double.toString(location.getLatitude());
        Longitude = Double.toString(location.getLongitude());
        LatLng latLng=null;
        if(latstation==0 && logstation==0)
        {
            latLng= new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        }else
        {
            latLng= new LatLng(latstation,logstation);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(noms);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
