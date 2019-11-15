package com.priamm.citymobile

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var carView: CarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carView = CarView(this)
        carView.addCar(Car(this))
        setContentView(carView)
    }

}
