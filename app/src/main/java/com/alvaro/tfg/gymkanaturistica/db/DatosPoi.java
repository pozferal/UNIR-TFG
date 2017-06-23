package com.alvaro.tfg.gymkanaturistica.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alvaro on 04/05/2017.
 */

public class DatosPoi {


    Integer idPoi;
    Integer orden;
    String nombre;
    String descripcion;
    Double longitud;
    Double latitud;
    String img;
    String masinfo;
    String categoria;
    Integer puntos;
    Integer id_ruta;
    Integer ordenSiguientePoi;
    String pregunta;
    List<DatosRespuesta> respuestas=new ArrayList<DatosRespuesta>();


    public DatosPoi(Integer orden, String nombre, String descripcion, Double longitud, Double latitud, String img, String masinfo, String categoria, Integer puntos, Integer ordenSiguientePoi, Integer id_ruta, String pregunta, List<DatosRespuesta> respuestas) {
        this.idPoi = idPoi;
        this.orden = orden;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.img = img;
        this.masinfo = masinfo;
        this.categoria = categoria;
        this.puntos = puntos;
        this.id_ruta = id_ruta;
        this.ordenSiguientePoi = ordenSiguientePoi;
        this.pregunta = pregunta;
        this.respuestas = respuestas;
    }

    public DatosPoi(Integer orden, String nombre, String descripcion, Double longitud, Double latitud, String img, String masinfo, String categoria, Integer puntos, Integer ordenSiguientePoi, Integer id_ruta, String pregunta) {
        this.orden = orden;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.img = img;
        this.masinfo = masinfo;
        this.categoria = categoria;
        this.puntos = puntos;
        this.id_ruta = id_ruta;
        this.ordenSiguientePoi = ordenSiguientePoi;
        this.pregunta = pregunta;
    }


    public DatosPoi(Integer orden, String nombre, String descripcion, Double longitud, Double latitud, String img, String masinfo, String categoria, Integer puntos, Integer ordenSiguientePoi, Integer id_ruta) {
        this.orden = orden;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.img = img;
        this.masinfo = masinfo;
        this.categoria = categoria;
        this.puntos = puntos;
        this.ordenSiguientePoi = ordenSiguientePoi;
        this.id_ruta=id_ruta;
    }

    public DatosPoi() {
    }


    public Integer getIdPoi() {
        return idPoi;
    }

    public void setIdPoi(Integer idPoi) {
        this.idPoi = idPoi;
    }




    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }


    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMasinfo() {
        return masinfo;
    }

    public void setMasinfo(String masinfo) {
        this.masinfo = masinfo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Integer getOrdenSiguientePoi() {
        return ordenSiguientePoi;
    }

    public void setOrdenSiguientePoi(Integer ordenSiguientePoi) {
        this.ordenSiguientePoi = ordenSiguientePoi;
    }



    public Integer getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(Integer id_ruta) {
        this.id_ruta = id_ruta;
    }

    public List<DatosRespuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<DatosRespuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
