package com.github.markssova.gamescatalog.ui.catalog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.markssova.gamescatalog.api.Game
import com.github.markssova.gamescatalog.api.RetrofitClient
import com.github.markssova.gamescatalog.dao.AppDatabase
import com.github.markssova.gamescatalog.dao.toEntity
import com.github.markssova.gamescatalog.dao.toGame
import kotlinx.coroutines.launch

class CatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val gameDao = AppDatabase.getInstance(application).gameDao()

    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> = _games

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchGames()
    }

    fun fetchGames(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                if (!forceRefresh) {
                    val cached = gameDao.getAllGames()
                    if (cached.isNotEmpty()) {
                        _games.value = cached.map { it.toGame() }
                        return@launch
                    }
                }

                val games = RetrofitClient.freeToGameApiInstance.getGames().shuffled()
                gameDao.clearGames()
                val gamesEntities = games.map { it.toEntity() }
                gameDao.insertGames(gamesEntities)
                _games.value = games

            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            }
        }
    }
}