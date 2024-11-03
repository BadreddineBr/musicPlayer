package com.example.musicplayer.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.data.Song

class SongsAdapter(private val songs: List<Song>, private val clickListener: (Song, Int) -> Unit) :
    RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.songTitle)
        val artistTextView: TextView = itemView.findViewById(R.id.artistName)
        val coverImageView: ImageView = itemView.findViewById(R.id.songCover)

        fun bind(song: Song, position: Int) {
            titleTextView.text = song.title
            artistTextView.text = song.artist
            coverImageView.setImageResource(song.coverImage)

            itemView.setOnClickListener {
                clickListener(song, position) // Pass the song and its position
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position], position) // Bind the position
    }

    override fun getItemCount(): Int = songs.size
}
