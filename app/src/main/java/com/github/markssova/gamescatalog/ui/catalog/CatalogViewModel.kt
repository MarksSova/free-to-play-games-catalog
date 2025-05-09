package com.github.markssova.gamescatalog.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.markssova.gamescatalog.api.Game
import com.github.markssova.gamescatalog.api.RetrofitClient
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> = _games

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var lastFetchTime: Long = 0L
    private var cache: List<Game> = emptyList()

    private val cacheDurationMillis = 10 * 60 * 1000L // 10 minutes

    init {
        fetchGames()
    }

    fun fetchGames(forceRefresh: Boolean = false) {
        val currentTime = System.currentTimeMillis()
        val isCacheValid = cache.isNotEmpty() && (currentTime - lastFetchTime < cacheDurationMillis)

        if (!forceRefresh && isCacheValid) {
            _games.value = cache
            return
        }

        viewModelScope.launch {
            try {
                val result = RetrofitClient.freeToGameApiInstance.getGames().shuffled()
                cache = result
                lastFetchTime = currentTime
                _games.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load games: ${e.localizedMessage}"
            }
        }
    }
}