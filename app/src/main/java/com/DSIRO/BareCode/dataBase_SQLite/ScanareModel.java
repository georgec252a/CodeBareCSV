package com.DSIRO.BareCode.dataBase_SQLite;

public class ScanareModel {
    private int id;
    private String user;
    private String cod_bon;
    private String cod_reper;
    private String uv;
    private String data_procesare;
    private String ora_procesare;

    public ScanareModel() {

    }

    public ScanareModel(int id, String user, String cod_bon, String cod_reper, String uv, String data_procesare, String ora_procesare) {

        this.id = id;
        this.user = user;
        this.cod_bon = cod_bon;
        this.cod_reper = cod_reper;
        this.uv = uv;
        this.data_procesare = data_procesare;
        this.ora_procesare = ora_procesare;
    }

    @Override
    public String toString() {
        return id +
                " User=" + user + '\'' +
                " Cod Reper=" + cod_bon + '\'' +
                " Cod Bon=" + cod_reper + '\'' +
                " Uv=" + uv + '\'' +
                " Data Procesare=" + data_procesare + '\'' +
                " Ora Procesare=" + ora_procesare;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCod_bon() {
        return cod_bon;
    }

    public void setCod_bon(String cod_bon) {
        this.cod_bon = cod_bon;
    }

    public String getCod_reper() {
        return cod_reper;
    }

    public void setCod_reper(String cod_reper) {
        this.cod_reper = cod_reper;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getData_procesare() {
        return data_procesare;
    }

    public void setData_procesare(String data_procesare) {
        this.data_procesare = data_procesare;
    }

    public String getOra_procesare() {
        return ora_procesare;
    }

    public void setOra_procesare(String ora_procesare) {
        this.ora_procesare = ora_procesare;
    }
}
