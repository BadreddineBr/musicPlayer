package com.example.musicplayer.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Adapters.SongsAdapter
import com.example.musicplayer.R
import com.example.musicplayer.data.Song

class SongsListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SongsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val songs = listOf(
            Song("Houdini", "Eminem", R.drawable.houdini, R.raw.eminem_houdini),
            Song("Die For You", "The Week End", R.drawable.theweekend, R.raw.die_for_you),
            Song("Puppet", "Faouzia", R.drawable.women_faouzia, R.raw.faouzia_puppet),
            Song("Save Your Tears", "The Week End", R.drawable.saveyour, R.raw.save_your_tears),
            Song("ICE", "Faouzia", R.drawable.ice_faouzia, R.raw.faouzia_ice),
            Song("Call Out MY Name ", "The Week End", R.drawable.weekend4, R.raw.call_out_my_name),
            Song("Fur Elise", "Faouzia", R.drawable.fureelise_faouzia, R.raw.faouzia_furelise) ,
            Song("StarBoy ", "The Week End", R.drawable.weekend, R.raw.starboy_theweeknd),
            Song("Grenade ", "Bruno Mars ", R.drawable.grenade, R.raw.bruno_grenade),


        )

        adapter = SongsAdapter(songs) { song, position ->
            val intent = Intent(this, SongDetailsActivity::class.java).apply {
                putExtra("song_list", ArrayList(songs)) // Pass the entire song list
                putExtra("current_index", position) // Pass the index of the clicked song
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}
