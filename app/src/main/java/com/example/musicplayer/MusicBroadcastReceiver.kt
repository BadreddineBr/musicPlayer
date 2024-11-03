package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MusicBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "ACTION_PLAY" -> {
                Toast.makeText(context, "Playing music", Toast.LENGTH_SHORT).show()
                context.startService(Intent(context, MusicService::class.java).apply {
                    putExtra("action", "play")
                })
            }
            "ACTION_PAUSE" -> {
                Toast.makeText(context, "Pausing music", Toast.LENGTH_SHORT).show()
                context.startService(Intent(context, MusicService::class.java).apply {
                    putExtra("action", "pause")
                })
            }
            "ACTION_NEXT" -> {
                Toast.makeText(context, "Next track", Toast.LENGTH_SHORT).show()
                context.startService(Intent(context, MusicService::class.java).apply {
                    putExtra("action", "next")
                })
            }
            "ACTION_PREVIOUS" -> {
                Toast.makeText(context, "Previous track", Toast.LENGTH_SHORT).show()
                context.startService(Intent(context, MusicService::class.java).apply {
                    putExtra("action", "previous")
                })
            }
        }
    }
}
