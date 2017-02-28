package application;

/**
 * Created by PC on 2/17/2017.
 */

public class Commerce {

    public String datasetid;
    public String recordid;
    public String fields;
    public String jour_fermeture;
    public String ville;
    public String code_postal;
    public String  tco_libelle;
    public String coord_geo;

    private boolean isFromDatabase;

    public void setDatasetid(String datasetid) {
        this.datasetid = datasetid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void setJour_fermeture(String jour_fermeture) {
        this.jour_fermeture = jour_fermeture;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public void setTco_libelle(String tco_libelle) {
        this.tco_libelle = tco_libelle;
    }

    public void setCoord_geo(String coord_geo) {
        this.coord_geo = coord_geo;
    }

    public String getDatasetid() {
        return datasetid;
    }

    public String getRecordid() {
        return recordid;
    }

    public String getFields() {
        return fields;
    }

    public String getJour_fermeture() {
        return jour_fermeture;
    }

    public String getVille() {
        return ville;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public String getTco_libelle() {
        return tco_libelle;
    }
    public String getCoord_geo() {
        return coord_geo;
    }

}
