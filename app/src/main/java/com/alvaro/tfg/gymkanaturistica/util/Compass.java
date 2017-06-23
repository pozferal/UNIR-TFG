package com.alvaro.tfg.gymkanaturistica.util;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.alvaro.tfg.gymkanaturistica.MainActivity;

import java.text.DecimalFormat;

import layout.Rumbo;


public class Compass implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gsensor;
    private Sensor msensor;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currectAzimuth = 0;
    private float azimuthNorte = 0f;
    private float currectAzimuthNorte = 0;
    private boolean norte=false;
    public ImageView arrowView = null;
    public ImageView arrowViewNorte = null;
    private double miLatitudActual = 0;
    private double miLongitudActual = 0;
    private double LatitudDondeIr = 0;
    private double LoingitudDondeIr =0 ;


    public double getMiLatitudActual() {
        return miLatitudActual;
    }

    public void setMiLatitudActual(double miLatitudActual) {
        this.miLatitudActual = miLatitudActual;
    }

    public double getMiLongitudActual() {
        return miLongitudActual;
    }

    public void setMiLongitudActual(double miLongitudActual) {
        this.miLongitudActual = miLongitudActual;
    }



    public double getLatitudDondeIr() {
        return LatitudDondeIr;
    }

    public void setLatitudDondeIr(double latitudDondeIr) {
        LatitudDondeIr = latitudDondeIr;
    }

    public double getLoingitudDondeIr() {
        return LoingitudDondeIr;
    }

    public void setLoingitudDondeIr(double loingitudDondeIr) {
        LoingitudDondeIr = loingitudDondeIr;
    }



    public Compass(Context context) {
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    public void start() {
        sensorManager.registerListener(this, gsensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, msensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }



    private void adjustArrowNorte() {
        if (arrowViewNorte == null) {
            Log.i(MainActivity.TAG, "arrow view is not set");
            return;
        }

        Animation an = new RotateAnimation(-currectAzimuthNorte, -azimuthNorte, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        currectAzimuthNorte = azimuthNorte;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        if (!Rumbo.getDatosRuta().isCompletada()) {
            arrowViewNorte.startAnimation(an);
        }
    }

    private void adjustArrow() {
        if (arrowView == null) {
            return;
        }else {

            Animation an = new RotateAnimation(-currectAzimuth, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            currectAzimuth = azimuth;

            an.setDuration(500);
            an.setRepeatCount(0);
            an.setFillAfter(true);

            if (!Rumbo.getDatosRuta().isCompletada()) {
                arrowView.startAnimation(an);

            }
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];

            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];

            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation
                azimuth = (azimuth + 360) % 360;
                azimuthNorte= (azimuth + 360) % 360;
                adjustArrowNorte();


                double deriva = bearing(miLatitudActual, miLongitudActual, LatitudDondeIr, LoingitudDondeIr);

                azimuth -= bearing(miLatitudActual, miLongitudActual, LatitudDondeIr, LoingitudDondeIr);

                adjustArrow();
            }
        }
    }


    public String obtenerDistanciaAObjetivo(){

        String distancia="?";

        Location locationActual = new Location("Localizacion actual");
        locationActual.setLatitude(miLatitudActual);  //latitud
        locationActual.setLongitude(miLongitudActual); //longitud
        Location locationObjetivo = new Location("Localizacion objetivo");
        locationObjetivo.setLatitude(LatitudDondeIr);  //latitud
        locationObjetivo.setLongitude(LoingitudDondeIr); //longitud
        double distance = locationActual.distanceTo(locationObjetivo);

        if (distance > 2000) {
            distance = distance / 1000;

            distancia = new DecimalFormat("#.##").format(distance) + " km";
        }else{
            distancia = (int)distance + " metros";
        }

        return distancia;
    }


    protected double bearing(double startLat, double startLng, double endLat, double endLng){
        double longitude1 = startLng;
        double longitude2 = endLng;
        double latitude1 = Math.toRadians(startLat);
        double latitude2 = Math.toRadians(endLat);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
