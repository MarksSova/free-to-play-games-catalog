package com.github.markssova.gamescatalog.ui.details

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.markssova.gamescatalog.R
import com.github.markssova.gamescatalog.api.Game
import com.github.markssova.gamescatalog.dao.AppDatabase
import com.github.markssova.gamescatalog.dao.GameDao
import com.github.markssova.gamescatalog.dao.toGame
import kotlinx.coroutines.launch

class GameDetailActivity : AppCompatActivity() {

    private lateinit var gameDao: GameDao
    private lateinit var favoriteIcon: ImageView
    private var currentGame: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        gameDao = AppDatabase.getInstance(applicationContext).gameDao()

        favoriteIcon = findViewById(R.id.gameFavoriteIcon)

        val gameId = intent.getIntExtra("gameId", -1)

        gameDao.getGameById(gameId).observe(this) { entity ->
            entity?.let {
                updateGameInformation(it.toGame())
            }
        }

        favoriteIcon.setOnClickListener {
            currentGame?.let { game ->
                val newValue = !game.isFavorite
                lifecycleScope.launch {
                    gameDao.updateFavoriteStatus(game.id, newValue)
                }
                val newGame = game.copy(isFavorite = newValue)
                updateGameInformation(newGame)
            }
        }
    }

    private fun updateGameInformation(game: Game) {
        currentGame = game
        Glide.with(this).load(game.thumbnail).into(findViewById(R.id.gameThumbnailView))
        findViewById<TextView>(R.id.gameTitleView).text = game.title
        updateFavoriteIcon()
        findViewById<TextView>(R.id.gameDescriptionView).text = game.shortDescription
        findViewById<TextView>(R.id.gameGenreView).text = getString(R.string.genre_text, game.genre)
        findViewById<TextView>(R.id.gamePlatformView).text =
            getString(R.string.platform_text, game.platform)
        findViewById<TextView>(R.id.gamePublisherView).text =
            getString(R.string.publisher_text, game.publisher)
        findViewById<TextView>(R.id.gameDeveloperView).text =
            getString(R.string.developer_text, game.developer)
        findViewById<TextView>(R.id.gameReleaseDateView).text =
            getString(R.string.release_date_text, game.releaseDate)
        findViewById<TextView>(R.id.gameProfileUrlView).text =
            getString(R.string.profile_url_text, game.freeToGameProfileUrl)
    }

    private fun updateFavoriteIcon() {
        val res = if (currentGame?.isFavorite == true) {
            R.drawable.ic_star_24dp
        } else {
            R.drawable.ic_star_border_24dp
        }
        favoriteIcon.setImageResource(res)
    }
}
