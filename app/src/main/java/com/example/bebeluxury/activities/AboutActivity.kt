package com.example.bebeluxury.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bebeluxury.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AboutActivity : AppCompatActivity() {
    
    private lateinit var textViewLogs: TextView
    private val logMessages = mutableListOf<String>()
    
    companion object {
        private const val TAG = "AboutActivity_Lifecycle"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLog("📱 onCreate() - Activity está siendo creada")
        setContentView(R.layout.activity_about)
        
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        textViewLogs = findViewById(R.id.textViewLifecycleLogs)
        
        findViewById<MaterialButton>(R.id.buttonTestLifecycle).setOnClickListener {
            Snackbar.make(it, "✅ Rota el dispositivo para ver onPause → onStop → onDestroy → onCreate", Snackbar.LENGTH_LONG).show()
        }
        
        if (savedInstanceState != null) {
            // Activity fue recreada (ej: rotación)
            addLog("🔄 Activity RECREADA desde savedInstanceState")
        }
        
        addLog("✅ onCreate() - Configuración completada")
        updateLogView()
    }
    
    override fun onStart() {
        super.onStart()
        addLog("👀 onStart() - Activity está VISIBLE para el usuario")
        updateLogView()
    }
    
    override fun onResume() {
        super.onResume()
        addLog("▶️ onResume() - Activity en PRIMER PLANO (interactiva)")
        updateLogView()
    }
    
    override fun onPause() {
        super.onPause()
        addLog("⏸️ onPause() - Activity PERDIENDO foco")
        updateLogView()
    }
    
    override fun onStop() {
        super.onStop()
        addLog("⏹️ onStop() - Activity YA NO VISIBLE")
        updateLogView()
    }
    
    override fun onDestroy() {
        addLog("💀 onDestroy() - Activity siendo DESTRUIDA")
        updateLogView()
        super.onDestroy()
    }
    
    override fun onRestart() {
        super.onRestart()
        addLog("🔄 onRestart() - Volviendo desde estado DETENIDO")
        updateLogView()
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        addLog("💾 onSaveInstanceState() - Guardando estado")
        outState.putStringArrayList("logs", ArrayList(logMessages))
        updateLogView()
    }
    
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        addLog("📂 onRestoreInstanceState() - Restaurando estado")
        val savedLogs = savedInstanceState.getStringArrayList("logs")
        if (savedLogs != null) {
            logMessages.clear()
            logMessages.addAll(savedLogs)
        }
        updateLogView()
    }
    
    private fun addLog(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val logEntry = "[$timestamp] $message"
        logMessages.add(logEntry)
        Log.d(TAG, message)
        
        // Mantener solo los últimos 20 logs
        if (logMessages.size > 20) {
            logMessages.removeAt(0)
        }
    }
    
    private fun updateLogView() {
        if (::textViewLogs.isInitialized) {
            textViewLogs.text = logMessages.joinToString("\n\n")
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
