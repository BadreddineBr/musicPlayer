package com.example.musicplayer.Activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.MusicService
import com.example.musicplayer.R
import com.example.musicplayer.data.Song

class SongDetailsActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var artistTextView: TextView
    private lateinit var coverImageView: ImageView
    private lateinit var previousButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var currentDurationTextView: TextView
    private lateinit var songDurationTextView: TextView

    private var isPlaying = false
    private var totalDuration = 0
    private lateinit var songList: List<Song>
    private var currentIndex = 0 // Current song index

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val currentPosition = intent?.getIntExtra("current_position", 0) ?: 0
            updateSeekBar(currentPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_details)

        // Initialize views
        titleTextView = findViewById(R.id.songTitle)
        artistTextView = findViewById(R.id.artistName)
        coverImageView = findViewById(R.id.songCover)
        previousButton = findViewById(R.id.previous_button)
        playButton = findViewById(R.id.play_button)
        nextButton = findViewById(R.id.next_button)
        seekBar = findViewById(R.id.seek_bar)
        currentDurationTextView = findViewById(R.id.player_current_duration)
        songDurationTextView = findViewById(R.id.player_song_duration)

        // Get the list of songs and the current index from the intent
        songList = intent.getSerializableExtra("song_list") as List<Song>
        currentIndex = intent.getIntExtra("current_index", 0)

        loadSongDetails()

        // Set up button click listeners
        previousButton.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                loadSongDetails()
            }
        }

        playButton.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        nextButton.setOnClickListener {
            if (currentIndex < songList.size - 1) {
                currentIndex++
                loadSongDetails()
            } else {
                Toast.makeText(this, "This is the last song", Toast.LENGTH_SHORT).show()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    startService(Intent(this@SongDetailsActivity, MusicService::class.java).apply {
                        putExtra("action", "seek")
                        putExtra("seek_position", progress)
                    })
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun loadSongDetails() {
        val song = songList[currentIndex]
        titleTextView.text = song.title
        artistTextView.text = song.artist
        coverImageView.setImageResource(song.coverImage)

        playMusic()

        val mediaPlayer = MediaPlayer.create(this, song.audioResId)
        totalDuration = mediaPlayer.duration
        mediaPlayer.release()

        songDurationTextView.text = formatDuration(totalDuration)
        seekBar.max = totalDuration
    }

    private fun playMusic() {
        startService(Intent(this, MusicService::class.java).apply {
            putExtra("action", "play")
            putExtra("audio_res_id", songList[currentIndex].audioResId)
        })
        playButton.setImageResource(R.drawable.pause)
        isPlaying = true
    }

    private fun pauseMusic() {
        startService(Intent(this, MusicService::class.java).apply {
            putExtra("action", "pause")
        })
        playButton.setImageResource(R.drawable.play)
        isPlaying = false
    }

    private fun updateSeekBar(currentPosition: Int) {
        seekBar.progress = currentPosition
        currentDurationTextView.text = formatDuration(currentPosition)
    }

    private fun formatDuration(milliseconds: Int): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicService::class.java))
        unregisterReceiver(broadcastReceiver)
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter("UPDATE_SEEK_BAR"))
    }
}
