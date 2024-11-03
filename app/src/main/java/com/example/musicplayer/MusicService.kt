package com.example.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra("action")) {
            "play" -> {
                val audioResId = intent.getIntExtra("audio_res_id", -1)
                if (audioResId != -1) {
                    playAudio(audioResId)
                }
            }
            "pause" -> pauseAudio()
            "stop" -> stopAudio()
            "seek" -> {
                val seekPosition = intent.getIntExtra("seek_position", 0)
                seekTo(seekPosition)
            }
        }
        return START_STICKY
    }

    private fun playAudio(audioResId: Int) {
        // Stop any existing playback and release the current MediaPlayer
        stopAudio()

        // Initialize a new MediaPlayer instance for the new song
        mediaPlayer = MediaPlayer.create(this, audioResId).apply {
            setOnCompletionListener {
                sendBroadcast(Intent("UPDATE_SEEK_BAR").putExtra("current_position", 0))
            }
            start() // Start playback
            isPrepared = true
        }

        // Start a new thread to update the seek bar position
        Thread {
            while (isPrepared) {
                try {
                    Thread.sleep(1000)
                    mediaPlayer?.let {
                        sendBroadcast(Intent("UPDATE_SEEK_BAR").putExtra("current_position", it.currentPosition))
                    }
                } catch (e: InterruptedException) {
                    break
                }
            }
        }.start()
    }

    private fun pauseAudio() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    private fun stopAudio() {
        mediaPlayer?.release() // Release the existing media player
        mediaPlayer = null // Clear the reference
        isPrepared = false
    }

    private fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAudio() // Ensure playback is stopped and resources are released
    }
}
