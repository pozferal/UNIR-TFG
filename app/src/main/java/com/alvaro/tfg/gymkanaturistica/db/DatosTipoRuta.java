package com.alvaro.tfg.gymkanaturistica.db;

/**
 * Created by Alvaro on 27/04/2017.
 */

public class DatosTipoRuta {

    private Integer tipoRuta_id;
    private String descripcion;


    public DatosTipoRuta(Integer tipoRuta_id, String descripcion) {
        this.tipoRuta_id = tipoRuta_id;
        this.descripcion = descripcion;
    }

    public Integer getTipoRuta_id() {
        return tipoRuta_id;
    }

    public void setTipoRuta_id(Integer tipoRuta_id) {
        this.tipoRuta_id = tipoRuta_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
