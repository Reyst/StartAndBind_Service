package reyst.gsihome.research.services

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "onServiceConnected")
        }

        override fun onBindingDied(name: ComponentName?) {
            Log.i(TAG, "onBindingDied")
        }

        override fun onNullBinding(name: ComponentName?) {
            Log.i(TAG, "onNullBinding")
        }
    }

    private val btnBind1: Button by lazy {
        findViewById<Button>(R.id.btn_bind_1).apply {
            setOnClickListener { bindServiceWithParam(text.toString()) }
        }
    }

    private val btnBind2: Button by lazy {
        findViewById<Button>(R.id.btn_bind_2).apply {
            setOnClickListener { bindServiceWithParam(text.toString()) }
        }
    }

    private val btnUnbind: Button by lazy {
        findViewById<Button>(R.id.btn_unbind).apply {
            setOnClickListener {
                Log.i(TAG, "unbindService")
                unbindService(connection)
                it.isEnabled = false
            }
        }

    }

    private val btnStart: Button by lazy {
        findViewById<Button>(R.id.btn_start).apply {
            setOnClickListener {
                Log.i(TAG, "startService")
                startService(Intent(this@MainActivity, ForegroundService::class.java))
            }
        }
    }
    private val btnStop: Button by lazy {
        findViewById<Button>(R.id.btn_stop).apply {
            setOnClickListener {
                Log.i(TAG, "stopService")
                val result = stopService(Intent(this@MainActivity, ForegroundService::class.java))
                Log.i(TAG, "result: $result")
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnBind1.isEnabled = true
        btnBind2.isEnabled = true
        btnUnbind.isEnabled = false
        btnStart.isEnabled = true
        btnStop.isEnabled = true
    }

    private fun bindServiceWithParam(param: String) {

        Intent(this, ForegroundService::class.java)
            .apply { putExtra("name", param) }
            .let {
                Log.i(TAG, "bindService: ${it.extras}")
                bindService(it, connection, Context.BIND_AUTO_CREATE)
                btnUnbind.isEnabled = true
            }
    }

}
