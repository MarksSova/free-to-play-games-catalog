package com.github.markssova.gamescatalog.ui.catalog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.markssova.gamescatalog.api.RetrofitClient
import com.github.markssova.gamescatalog.dao.AppDatabase
import com.github.markssova.gamescatalog.dao.GameEntity
import com.github.markssova.gamescatalog.dao.toEntity
import kotlinx.coroutines.launch

class CatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val gameDao = AppDatabase.getInstance(application).gameDao()

    private val _games = MutableLiveData<List<GameEntity>>()
    val games: LiveData<List<GameEntity>> = _games

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
                        _games.value = cached.map { it }
                        return@launch
                    }
                }

                val remote = RetrofitClient.freeToGameApiInstance.getGames().shuffled()
                gameDao.clearGames()
                val gamesEntities = remote.map { it.toEntity() }
                gameDao.insertGames(gamesEntities)
                _games.value = gamesEntities

            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            }
        }
    }
}