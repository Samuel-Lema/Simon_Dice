package com.gamemotion.simondice

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.view.*
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

        // Primera ejecución del programa

        btnStart.setOnClickListener() {
            btnStart.visibility = View.INVISIBLE
            Start()
        }
    }

    fun Start(){

        this.setTitle("Simon Dice - Ronda " + (MaxNum -2))
        progreso.setProgress(0);

        this.toast1 = Toast.makeText(applicationContext, "Simon dice $MaxNum colores", Toast.LENGTH_SHORT)
        this.toast1.show()

        startSimon()

        // Gestiono los Listener de los botones despues de generar la partida

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
    }

    // Inicia la secuencia de colores para mostrar su orden

    fun startSimon() {

        var delayed: Long = 2000;

        for (i in 0..MaxNum -1 step 1) {

            val x = Random().nextInt(3)

            when (x) {
                0 -> lightButton(btnRojo, Color.argb(80, 255, 0, 0), Color.RED, delayed, i)
                1 -> lightButton(btnAmarillo, Color.argb(80, 255, 255, 0), Color.YELLOW, delayed, i)
                2 -> lightButton(btnAzul, Color.argb(80, 0, 191, 255), Color.BLUE, delayed, i)
                3 -> lightButton(btnVerde, Color.argb(80, 0, 255, 0), Color.GREEN, delayed, i)
            }

            secuencia.add(x)
            delayed += 2000
        }
    }

    // Gestiona los "parpadeos" de color cuando Simon dice...

    fun lightButton(boton: Button, argb: Int, colorOriginal: Int, delayed: Long, i: Int){

        Handler().postDelayed({
            boton.setBackgroundColor(argb)

            Handler().postDelayed({
                boton.setText("Número: " + (i + 1))
                boton.setBackgroundColor(colorOriginal)
            }, 1000)
        }, delayed)
    }

    // Checkea si ha fallado o no

    fun checkIt(nBoton: Int) {

        if (cliente < (MaxNum -1)) {

            if (secuencia.get(cliente) == nBoton) {

                cliente++
                progreso.progress = (((cliente.toFloat() * 10)/ (secuencia.size.toFloat() * 10)) * 100).toInt()
                toast1.cancel()
            } else {

                toast1.setText("INCORRECTO")
            }
        } else if (cliente == (MaxNum -1)) {

            if (secuencia.get(cliente) == nBoton) {

                cliente++
                progreso.progress = (((cliente.toFloat() * 10)/ (secuencia.size.toFloat() * 10)) * 100).toInt()
                toast1.setText("Ronda completada")
                MaxNum += 1
                cliente = 0
                secuencia.clear()
                btnStart.visibility = View.VISIBLE
            } else {

                toast1.setText("INCORRECTO")
            }
        }

        toast1.show()
    }
}
