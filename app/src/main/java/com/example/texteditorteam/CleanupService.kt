package com.example.texteditorteam

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class CleanupService : Service() {

    // Tag yang sama dengan yang Anda filter di Logcat
    private val TAG = "LifecycleLogger"

    override fun onBind(intent: Intent?): IBinder? {
        // Tidak perlu binding untuk service seperti ini, jadi return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "CleanupService started")
        // Biarkan service berjalan sampai dihentikan
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        
        // INI BAGIAN PENTINGNYA
        // Log ini akan muncul saat aplikasi di-swipe dari recent apps
        Log.d(TAG, "App task removed from recents!")

        // Hentikan service setelah tugasnya selesai
        stopSelf()
    }
}
