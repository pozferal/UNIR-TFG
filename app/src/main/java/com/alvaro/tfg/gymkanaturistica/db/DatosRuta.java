package com.alvaro.tfg.gymkanaturistica.db;

/**
 * Created by Alvaro on 27/04/2017.
 */

public class DatosRuta {

    private Integer idRuta;
    private Integer tipo_Ruta_id;
    private String descripcion_ruta;
    private boolean activa;
    private boolean completada;
    private Integer puntuacion;
    private String modo;
    private String duracion;
    private String distancia;
    private Integer orden_sigiuente_poi;





    public DatosRuta(){

    }

    public DatosRuta(  String descripcion, boolean activa, boolean completada, Integer puntuacion, String modo, Integer tipo_Ruta_id, String duracion, String distancia, String identificacion, Integer orden_sigiuente_poi){
        this.tipo_Ruta_id=tipo_Ruta_id;
        this.descripcion_ruta=descripcion;
        this.activa=activa;
        this.completada=completada;
        this.puntuacion=puntuacion;
        this.modo=modo;
        this.duracion=duracion;
        this.distancia=distancia;
        this.identificacion=identificacion;
        this.orden_sigiuente_poi=orden_sigiuente_poi;
    }

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public Integer getTipo_Ruta_id() {
        return tipo_Ruta_id;
    }

    public void setTipo_Ruta_id(Integer tipo_Ruta_id) {
        this.tipo_Ruta_id = tipo_Ruta_id;
    }

    public String getDescripcion() {
        return descripcion_ruta;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion_ruta = descripcion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }




    public Integer getOrden_sigiuente_poi() {
        return orden_sigiuente_poi;
    }

    public void setOrden_sigiuente_poi(Integer orden_sigiuente_poi) {
        this.orden_sigiuente_poi = orden_sigiuente_poi;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    private String identificacion;


    public String getDescripcion_ruta() {
        return descripcion_ruta;
    }

    public void setDescripcion_ruta(String descripcion_ruta) {
        this.descripcion_ruta = descripcion_ruta;
    }



    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }


}
