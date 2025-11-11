package edu.temple.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder

class MainActivity : AppCompatActivity() {
    private var timerBinder: TimerService.TimerBinder? = null
    private var bound = false

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            timerBinder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (bound){
                timerBinder?.start(100)
            }



        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (bound){
                timerBinder?.stop()
            }


        }
    }
    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, conn, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(conn)
            bound = false
        }
    }
}