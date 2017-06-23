package com.alvaro.tfg.gymkanaturistica.db;

/**
 * Created by Alvaro on 18/05/2017.
 */

public class DatosRespuesta {
    String texto;
    Integer correcta;

    public DatosRespuesta(String texto, Integer correcta) {
        this.texto = texto;
        this.correcta = correcta;
    }

    public DatosRespuesta() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getCorrecta() {
        return correcta;
    }

    public void setCorrecta(Integer correcta) {
        this.correcta = correcta;
    }
}
