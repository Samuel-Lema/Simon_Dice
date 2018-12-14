package com.gamemotion.simondice

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnRojo: Button
    private lateinit var btnVerde: Button
    private lateinit var btnAmarillo: Button
    private lateinit var btnAzul: Button
    private lateinit var btnStart: Button
    private lateinit var progreso: ProgressBar
    private lateinit var toast1: Toast

    private var secuencia: ArrayList<Int> = ArrayList()
    private var cliente: Int = 0;
    private var MaxNum: Int = 3;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Muestra la ronda inicial - Ronda 1

        this.setTitle("Simon Dice - Ronda " + (MaxNum -2))

        // Asigno los objetos en pantalla para trabajar con ellos

        this.progreso = findViewById<ProgressBar>(R.id.progressBar)
        this.btnRojo = findViewById<Button>(R.id.btnRed)
        this.btnVerde = findViewById<Button>(R.id.btnGreen)
        this.btnAzul = findViewById<Button>(R.id.btnBlue)
        this.btnAmarillo = findViewById<Button>(R.id.btnYellow)
        this.btnStart = findViewById<Button>(R.id.btnStart)

        // Defino valores iniciales de los objetos en pantalla

        btnRojo.setBackgroundColor(Color.RED)
        btnVerde.setBackgroundColor(Color.GREEN)
        btnAzul.setBackgroundColor(Color.BLUE)
        btnAmarillo.setBackgroundColor(Color.YELLOW)
        progreso.setProgress(0);

        // Primera ejecución del programa con el boton StartRonda

        btnStart.setOnClickListener() {

            // Esconde el boton de Iniciar y empieza la secuencia

            btnStart.visibility = View.INVISIBLE
            StartRonda()
        }
    }

    fun StartRonda(){

        // Muestro el titulo - Ronda, y reinicia el progreso a 0

        this.setTitle("Simon Dice - Ronda " + (MaxNum -2))
        progreso.setProgress(0);

        // Dice cuantos colores dira Simon en su ronda

        this.toast1 = Toast.makeText(applicationContext, "Simon dice $MaxNum colores", Toast.LENGTH_SHORT)
        this.toast1.show()

        // Gestiona los botones clickables a 'false' antes de empezar la secuencia a mostrar

        btnRojo.isClickable = false
        btnAmarillo.isClickable = false
        btnAzul.isClickable = false
        btnVerde.isClickable = false

        // Inicia la secuencia

        startSecuencia()
    }

    // Inicia la secuencia de colores para mostrar su orden

    fun startSecuencia() {

        // Asigno el delay inicial de los parpadeos de los botones

        var delayed: Long = 2000;

        // Genera tantas combinaciones como Simon diga

        for (i in 0..MaxNum -1 step 1) {

            val x = Random().nextInt(3)

            when (x) {
                0 -> lightButton(btnRojo, Color.argb(80, 255, 0, 0), Color.RED, delayed, i)
                1 -> lightButton(btnAmarillo, Color.argb(80, 255, 255, 0), Color.YELLOW, delayed, i)
                2 -> lightButton(btnAzul, Color.argb(80, 0, 191, 255), Color.BLUE, delayed, i)
                3 -> lightButton(btnVerde, Color.argb(80, 0, 255, 0), Color.GREEN, delayed, i)
            }

            // Añade el boton que parpedea a un arrayList llamado 'secuencia' para su posterior comprobación
            // contra la selección del cliente y añade más delay al siguiente parpadeo del boton

            secuencia.add(x)
            delayed += 2000
        }

        delayed -= 2000

        Handler().postDelayed({

            // Activa los Listener de los botones cuando Simon finaliza de mostrar las combinaciones de la Ronda

            btnRojo.setOnClickListener() {
                checkIt(0)
            }

            btnAmarillo.setOnClickListener() {
                checkIt(1)
            }

            btnAzul.setOnClickListener() {
                checkIt(2)
            }

            btnVerde.setOnClickListener() {
                checkIt(3)
            }

        }, delayed)
    }

    // Gestiona los "parpadeos" de color cuando Simon dice...

    fun lightButton(boton: Button, argb: Int, colorOriginal: Int, delayed: Long, i: Int){

        Handler().postDelayed({
            boton.setBackgroundColor(argb)

            // El boton vuelve a su color original tras pasar 1 segundo y muestra un texto de "Debug"

            Handler().postDelayed({
                boton.setBackgroundColor(colorOriginal)
            }, 1000)
        }, delayed)
    }

    // Checkea si la selección del cliente en incorrecta

    fun checkIt(nBoton: Int) {

        if (secuencia.get(cliente) == nBoton) {

            if (cliente < (MaxNum -1)) { // El cliente acierta la selección actual, pero aun le quedan más en la ronda

                cliente++
                progreso.progress = (((cliente.toFloat() * 10)/ (secuencia.size.toFloat() * 10)) * 100).toInt()
                toast1.cancel()

            } else if (cliente == (MaxNum -1)) { // El cliente acierta toda la ronda y se prepara para la siguiente

                cliente++
                progreso.progress = (((cliente.toFloat() * 10)/ (secuencia.size.toFloat() * 10)) * 100).toInt()
                toast1.setText("Ronda completada")
                MaxNum += 1
                cliente = 0
                secuencia.clear()

                StartRonda()
            }

        } else { // El cliente falla, se le avisa y vuelve a iniciar desde el nivel más básico

            MaxNum = 3
            progreso.progress = 0
            cliente = 0
            secuencia.clear()
            btnStart.visibility = View.VISIBLE
            toast1.setText("INCORRECTO, REINICIO COMPLETADO")
        }

        toast1.show()
    }
}
