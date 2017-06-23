package com.alvaro.tfg.gymkanaturistica.util;

import android.util.Log;
import android.util.Xml;

import com.alvaro.tfg.gymkanaturistica.MainActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class GymkanaXmlParser {

    private static final String ns = null;

    public static class Ruta {
        public final String nombre;
        public final String tipo;
        public final String duracion;
        public final String distancia;
        public final String identificacion;
        public final List<Poi> pois;

        private Ruta(String nombre, String tipo, String duracion, String distancia, String identificacion, List<Poi> pois) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.duracion = duracion;
            this.distancia=distancia;
            this.identificacion=identificacion;
            this.pois=pois;
        }
    }



    public static class Poi{
        public final String orden;
        public final String nombre;
        public final String descripcion;
        public final String longitud;
        public final String latitud;
        public final String img;
        public final String masinfo;
        public final String categoria;
        public final String puntos;
        public final String ordenSiguientePoi;
        public final String pregunta;
        public final List<Respuesta> respuestas;

        private Poi(String orden, String nombre, String descripcion, String longitud, String latitud, String img, String masinfo, String categoria, String puntos, String ordenSiguientePoi, String pregunta, List<Respuesta> respuestas){
            this.orden=orden;
            this.nombre=nombre;
            this.descripcion=descripcion;
            this.longitud=longitud;
            this.latitud=latitud;
            this.img=img;
            this.masinfo=masinfo;
            this.categoria=categoria;
            this.puntos=puntos;
            this.ordenSiguientePoi=ordenSiguientePoi;
            this.pregunta=pregunta;
            this.respuestas=respuestas;
        }

    }


    public static class Respuesta{
        public final String texto;
        public final Integer correcta;

        private Respuesta(String texto, Integer correcta){
            this.texto=texto;
            this.correcta=correcta;
        }
    }






    public List<Ruta> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            Log.i(MainActivity.TAG, "Vamos a parsear");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readRutas(parser);


        } finally {
            in.close();
        }
    }


    private List<Ruta> readRutas(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Ruta> entries = new ArrayList<Ruta>();

        parser.require(XmlPullParser.START_TAG, ns, "Rutas");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Ruta")) {
                entries.add(readRuta(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }


    private Ruta readRuta(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Ruta");
        String nombre = null;
        String tipo = null;
        String duracion = null;
        String distancia= null;
        String identificacion=null;
        List<Poi> pois=new ArrayList<Poi>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("nombre")) {
                nombre = readNombreRuta(parser);
            } else if (name.equals("tipo")) {
                tipo = readTipoRuta(parser);
            } else if (name.equals("duracion")) {
                duracion = readDuracion(parser);
            } else if (name.equals("distancia")) {
                distancia = readDistancia(parser);
            } else if (name.equals("identificacion")) {
                identificacion = readIdentificacion(parser);
            } else if (name.equals("poi")) {
                pois.add(readPoi(parser));
            } else {
                skip(parser);
            }
        }
        return new Ruta(nombre, tipo, duracion, distancia, identificacion, pois);
    }



    private Poi readPoi(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "poi");
        String orden=null;
        String nombre=null;
        String descripcion=null;
        String longitud=null;
        String latitud=null;
        String img=null;
        String masinfo=null;
        String categoria=null;
        String puntos=null;
        String ordenSiguientePoi=null;
        String pregunta=null;
        List<Respuesta> respuestas=new ArrayList<Respuesta>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("orden")) {
                orden = readOrden(parser);
            } else if (name.equals("nombre")) {
                nombre = readNombre(parser);
            } else if (name.equals("descripcion")) {
                descripcion = readDescripcion(parser);
            } else if (name.equals("long")) {
                longitud = readLongitud(parser);
            } else if (name.equals("lat")) {
                latitud = readLatitud(parser);
            } else if (name.equals("img")) {
                img = readImg(parser);
            } else if (name.equals("masinfo")) {
                masinfo = readMasinfo(parser);
            } else if (name.equals("categoria")) {
                categoria = readCategoria(parser);
            } else if (name.equals("puntos")) {
                puntos = readPuntos(parser);
            } else if (name.equals("ordenSiguientePoi")) {
                ordenSiguientePoi = readOrdenSiguientePoi(parser);
            } else if (name.equals("pregunta")) {
                    pregunta = readPregunta(parser);
            } else if (name.equals("respuesta")) {
                respuestas.add(readRespuesta(parser));

            } else {
                skip(parser);
            }
        }
        return new Poi(orden,nombre,descripcion,longitud,latitud,img,masinfo,categoria,puntos,ordenSiguientePoi, pregunta, respuestas);

    }

    private Respuesta readRespuesta(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "respuesta");
        String texto=null;
        Integer correcta=null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("texto")) {
                texto = readTexto(parser);
            } else if (name.equals("correcta")) {
                correcta = readCorrecta(parser);
            } else {
                skip(parser);
            }
        }
        return new Respuesta(texto,correcta);
    }


        private String readNombreRuta(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "nombre");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "nombre");
        return nombre;
    }

    private String readDuracion(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "duracion");
        String duracion = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "duracion");
        return duracion;
    }
    private String readDistancia(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "distancia");
        String distancia = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "distancia");
        return distancia;
    }
    private String readIdentificacion(XmlPullParser parser) throws  IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "identificacion");
        String distancia = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "identificacion");
        return distancia;
    }



    private String readOrden(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "orden");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "orden");
        return nombre;
    }
    private String readNombre(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "nombre");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "nombre");
        return nombre;
    }
    private String readDescripcion(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "descripcion");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "descripcion");
        return nombre;
    }
    private String readLongitud(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "long");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "long");
        return nombre;
    }
    private String readLatitud(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "lat");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "lat");
        return nombre;
    }

    private String readImg(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "img");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "img");
        return nombre;
    }

    private String readMasinfo(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "masinfo");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "masinfo");
        return nombre;
    }

    private String readCategoria(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "categoria");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "categoria");
        return nombre;
    }

    private String readPuntos(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "puntos");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "puntos");
        return nombre;
    }
    private String readOrdenSiguientePoi(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "ordenSiguientePoi");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "ordenSiguientePoi");
        return nombre;
    }

    private String readPregunta(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pregunta");
        String pregunta = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pregunta");
        return pregunta;
    }

    private String readTexto(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "texto");
        String texto = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "texto");
        return texto;
    }

    private Integer readCorrecta(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "correcta");
        String correcta = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "correcta");
        return new Integer(correcta);
    }


    private String readTipoRuta(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "tipo");
        String nombre = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "tipo");
        return nombre;
    }



    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }






}
