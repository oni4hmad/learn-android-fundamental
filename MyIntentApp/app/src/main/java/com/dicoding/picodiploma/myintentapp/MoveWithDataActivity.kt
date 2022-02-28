package com.dicoding.picodiploma.myintentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

class MoveWithDataActivity : AppCompatActivity() {

    /* companion object -> static variable, bisa diakses tanpa bikin obj */
    companion object {
        /* variable di bawah merupakan key */
        const val EXTRA_AGE = "extra_age"
        const val EXTRA_NAME = "extra_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_with_data)

        val tvDataReceived: TextView = findViewById(R.id.tv_data_received)

        /* get data dari activity asal */
        val name = intent.getStringExtra(EXTRA_NAME)
        val age = intent.getIntExtra(EXTRA_AGE, 0)

        val text = "Name : $name, Your Age : $age"
        tvDataReceived.text = text
    }
}