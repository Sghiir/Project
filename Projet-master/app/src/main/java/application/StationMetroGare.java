package application;

/**
 * Created by PC on 2/21/2017.
 */

public class StationMetroGare {

    public String  nom_station_gare;
    public String  Coord_geo;

    public StationMetroGare() {
    }

    public String getNom() {
        return nom_station_gare;
    }

    public String getCoord_geo() {
        return Coord_geo;
    }

    public void setNom(String nom) {
        this.nom_station_gare = nom;
    }

    public void setCoord_geo(String coord_geo) {
        Coord_geo = coord_geo;
    }
}
