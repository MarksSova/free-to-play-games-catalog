package com.github.markssova.gamescatalog.ui.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.markssova.gamescatalog.R
import com.github.markssova.gamescatalog.api.Game

class CatalogAdapter : ListAdapter<Game, CatalogAdapter.GameViewHolder>(GameDiffCallback()) {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageIcon: ImageView = view.findViewById(R.id.itemIcon)
        private val titleText: TextView = view.findViewById(R.id.itemNameTextView)
        private val genreText: TextView = view.findViewById(R.id.itemGenreTextView)
        private val platformText: TextView = view.findViewById(R.id.itemPlatformTextView)

        fun bind(game: Game) {
            titleText.text = game.title
            genreText.text = game.genre
            platformText.text = game.platform

            Glide.with(imageIcon.context)
                .load(game.thumbnail)
                .into(imageIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_catalog, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class GameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean =
        oldItem == newItem
}