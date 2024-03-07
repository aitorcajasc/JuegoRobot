package com.riberadeltajo.juegorobot;

import android.graphics.Bitmap;

public class Puerta {
    Bitmap puerta;

    public Puerta(Bitmap puerta) {
        this.puerta = puerta;
    }
    public Puerta() {}

    public Bitmap getPuerta() {
        return puerta;
    }

    public void setPuerta(Bitmap puerta) {
        this.puerta = puerta;
    }

    @Override
    public String toString() {
        return "Puerta{" +
                "puerta=" + puerta +
                '}';
    }
}
