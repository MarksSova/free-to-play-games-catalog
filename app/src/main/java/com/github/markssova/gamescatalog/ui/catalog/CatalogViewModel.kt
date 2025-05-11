package com.github.markssova.gamescatalog.ui.catalog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.markssova.gamescatalog.api.Game
import com.github.markssova.gamescatalog.api.RetrofitClient
import com.github.markssova.gamescatalog.dao.AppDatabase
import com.github.markssova.gamescatalog.dao.GameEntity
import com.github.markssova.gamescatalog.dao.toEntity
import com.github.markssova.gamescatalog.dao.toGame
import kotlinx.coroutines.launch

class CatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val gameDao = AppDatabase.getInstance(application).gameDao()

    val games: LiveData<List<Game>> = gameDao.getAllGames().toGames()
    val favoriteGames: LiveData<List<Game>> = gameDao.getFavoriteGames().toGames()

    fun fetchGames(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!forceRefresh && gameDao.hasAnyGames()) return@launch

            val games = RetrofitClient.freeToGameApiInstance.getGames()
            gameDao.clearGames()
            gameDao.insertGames(games.map(Game::toEntity))
        }
    }

    private fun LiveData<List<GameEntity>>.toGames(): LiveData<List<Game>> =
        this.map { list -> list.map { entity -> entity.toGame() } }
}