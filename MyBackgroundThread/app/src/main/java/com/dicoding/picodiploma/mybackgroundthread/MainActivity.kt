package com.dicoding.picodiploma.mybackgroundthread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // memproses semua di ui thread
        /*val btnStart = findViewById<Button>(R.id.btn_start)
        val tvStatus = findViewById<TextView>(R.id.tv_status)
        btnStart.setOnClickListener {
            try {
                // simulate process compressing
                for (i in 0..10) {
                    Thread.sleep(500)
                    val percentage = i * 10
                    if (percentage == 100) {
                        tvStatus.setText(R.string.task_completed)
                    } else {
                        tvStatus.text = String.format(getString(R.string.compressing), percentage)
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }*/

        // membagi proses di thread baru (memisah ui thread dg background thread)
        /*val btnStart = findViewById<Button>(R.id.btn_start)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        /* myLooper :  jika Anda ingin Handler berjalan dengan thread yang sama dengan sebelumnya */

        btnStart.setOnClickListener {
            executor.execute {
                try {
                    //simulate process in background thread
                    for (i in 0..10) {
                        Thread.sleep(500)
                        val percentage = i * 10
                        handler.post {
                            //update ui in main thread
                            if (percentage == 100) {
                                tvStatus.setText(R.string.task_completed)
                            } else {
                                tvStatus.text = String.format(getString(R.string.compressing), percentage)
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }*/

        // menggunakan coroutine
        val btnStart = findViewById<Button>(R.id.btn_start)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        btnStart.setOnClickListener {
            /* lifecycleScope -> anti memory leak coroutine kehapus ketika activity onDestroy */
            lifecycleScope.launch(Dispatchers.Default) { /* scope utk process berat / di Background Thread yang tidak memerlukan proses read-write */
                //simulate process in background thread
                for (i in 0..10) {
                    delay(500)
                    val percentage = i * 10
                    withContext(Dispatchers.Main) { /* scope utk ui */
                        //update ui in main thread
                        if (percentage == 100) {
                            tvStatus.setText(R.string.task_completed)
                        } else {
                            tvStatus.text = String.format(getString(R.string.compressing), percentage)
                        }
                    }
                }
            }
        }
    }
}