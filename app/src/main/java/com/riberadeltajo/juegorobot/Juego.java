package com.riberadeltajo.juegorobot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

public class Juego extends SurfaceView implements SurfaceHolder.Callback, SurfaceView.OnTouchListener {

    private SurfaceHolder holder;
    private BucleJuego bucle;
    static final int X = 0;
    static final int Y = 1;
    static final int IZQ = 0;
    static final int DER = 1;
    static final int ABAJ = 2;
    static final int ARRI = 3;

    Bitmap puerta;
    Bitmap robot;
    int estado;
    int filas;
    float posicionPuerta[] = new float[2];
    float posicionRobot[] = new float[2];
    final float SEGUNDOS_EN_RECORRER_PANTALLA_HORIZONTAL=4;
    float velocidad[] = new float[2];
    boolean hayToque=false;
    ArrayList<Toque> toques = new ArrayList<Toque>();
    Control controles[]=new Control[4];
    float maxX, maxY;
    float robot_w, robot_h;
    boolean victoria;
    int segundos;

    private static final String TAG = Juego.class.getSimpleName();

    public Juego(Activity context, Bitmap puerta) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        this.puerta = puerta;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // se crea la superficie, creamos el game loop

        // Para interceptar los eventos de la SurfaceView
        getHolder().addCallback(this);

        inicializar();

        // creamos el game loop
        bucle = new BucleJuego(getHolder(), this);
        setOnTouchListener(this);


        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        //comenzar el bucle
        bucle.start();

    }

    private void inicializar() {
        //Canvas
        Canvas c = getHolder().lockCanvas();
        maxY = c.getHeight();
        maxX = c.getWidth();

        //Posiciones random
        posicionPuerta[X] = (float) (Math.random() * maxX);
        posicionPuerta[Y] = (float) (Math.random() * maxY);

        //Robot
        robot = BitmapFactory.decodeResource(getResources(), R.drawable.robot);
        robot_w = robot.getWidth() / 4;
        robot_h = robot.getHeight() / 2;
        posicionRobot[X] = maxX/2;
        posicionRobot[Y] = maxY-robot_h;
        estado =0;
        filas =0;
        velocidad[X]=maxX/SEGUNDOS_EN_RECORRER_PANTALLA_HORIZONTAL/BucleJuego.MAX_FPS;
        velocidad[Y]=maxY/SEGUNDOS_EN_RECORRER_PANTALLA_HORIZONTAL/BucleJuego.MAX_FPS;

        victoria=false;

        //flecha_izda
        controles[IZQ] = new Control(getContext(), 10, maxY-300);
        controles[IZQ].cargar(R.drawable.flecha_izda);
        controles[IZQ].nombre = "IZQUIERDA";
        //flecha_derecha
        controles[DER] = new Control(getContext(),
                controles[IZQ].ancho() + controles[IZQ].coordenada_x + 5, controles[IZQ].coordenada_y);
        controles[DER].cargar(R.drawable.flecha_dcha);
        controles[DER].nombre = "DERECHA";
        //Abajo
        controles[ABAJ] = new Control(getContext(), maxX - controles[IZQ].ancho(), controles[IZQ].coordenada_y);
        controles[ABAJ].cargar(R.drawable.flecha_abajo);
        controles[ABAJ].nombre = "ABAJO";
        //Arriba
        controles[ARRI] = new Control(getContext(),
                maxX - (controles[IZQ].ancho() * 2), controles[IZQ].coordenada_y);
        controles[ARRI].cargar(R.drawable.flecha_arriba);
        controles[ARRI].nombre = "ARRIBA";

        getHolder().unlockCanvasAndPost(c);
    }

    /**
     * Este método actualiza el estado del juego. Contiene la lógica del videojuego
     * generando los nuevos estados y dejando listo el sistema para un repintado.
     */
    public void actualizar() {

        if(!victoria){
            if(controles[DER].pulsado){
                if(posicionRobot[X]<maxX-robot_w){
                    posicionRobot[X]+=velocidad[X];

                    estado++;
                    if(estado >3){
                        estado =0;
                    }
                }
            }
            if(controles[IZQ].pulsado){
                if(posicionRobot[X]>0){
                    posicionRobot[X]-=velocidad[X];

                    estado++;
                    if(estado >3){
                        estado =0;
                    }
                }
            }
            if(controles[ARRI].pulsado){
                if(posicionRobot[Y]>0){
                    filas=1;
                    posicionRobot[Y]-=velocidad[Y];

                    estado++;
                    if(estado >3){
                        estado =0;
                    }
                }
            }
            if(controles[ABAJ].pulsado){
                if(posicionRobot[Y]<maxY-robot_h){
                    filas=1;
                    posicionRobot[Y]+=velocidad[Y];

                    estado++;
                    if(estado >3){
                        estado =0;
                    }
                }
            }
            if(!hayToque){
                estado =0;
                filas =0;
            }
            if(Colision.hayColision(robot, (int) posicionRobot[X], (int) posicionRobot[Y], puerta, (int) posicionPuerta[X], (int) posicionPuerta[Y])){
                segundos= (int) (bucle.tiempoTotal/1000);
                victoria=true;
            }

        }else{
            Intent i=new Intent(getContext(), MainActivity.class);
            i.putExtra("segundos", segundos);
            getContext().startActivity(i);
            bucle.JuegoEnEjecucion=false;
        }
    }

    /**
     * Este método dibuja el siguiente paso de la animación correspondiente
     */
    public void renderizar(Canvas c) {
        if (c == null) {
            return;
        }
        if(!victoria){
            c.drawColor(Color.BLACK);

            //pintar mensajes que nos ayudan
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setColor(Color.RED);
            p.setTextSize(50);
            c.drawText("Frame " + bucle.iteraciones + ";" + "Tiempo " + bucle.tiempoTotal + " [" + bucle.maxX + "," + bucle.maxY + "]", 50, 150, p);

            //Puerta
            c.drawBitmap(puerta, posicionPuerta[X], posicionPuerta[Y], p);
            //Robot
            c.drawBitmap(robot,
                    new Rect((int) (estado *robot_w), (int) (filas *robot_h), (int) (int) ((estado +1)*robot_w), (int) ((filas +1)*robot_h)),
                    new Rect((int) posicionRobot[X], (int) posicionRobot[Y], (int) (posicionRobot[X]+robot_w), (int) (posicionRobot[Y]+robot_h)),
                    null);

            for(int i=0;i<controles.length;i++){
                controles[i].dibujar(c,p);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Juego destruido!");
        // cerrar el thread y esperar que acabe
        boolean retry = true;
        while (retry) {
            try {
                bucle.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int index;
        int x, y;

        // Obtener el pointer asociado con la acción
        index = event.getActionIndex();


        x = (int) event.getX(index);
        y = (int) event.getY(index);

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                hayToque=true;

                synchronized(this) {
                    toques.add(index, new Toque(index, x, y));
                }

                //se comprueba si se ha pulsado
                for(int i=0;i<controles.length;i++)
                    controles[i].compruebaPulsado(x,y);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                synchronized(this) {
                    toques.remove(index);
                }

                //se comprueba si se ha soltado el botón
                for(int i=0;i<controles.length;i++)
                    controles[i].compruebaSoltado(toques);
                break;

            case MotionEvent.ACTION_UP:
                synchronized(this) {
                    toques.clear();
                }
                hayToque=false;
                //se comprueba si se ha soltado el botón
                for(int i=0;i<controles.length;i++)
                    controles[i].compruebaSoltado(toques);
                break;
        }

        return true;
    }

    public void fin() {
    }
}
