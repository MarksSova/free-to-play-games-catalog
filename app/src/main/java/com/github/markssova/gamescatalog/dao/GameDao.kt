package com.github.markssova.gamescatalog.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.markssova.gamescatalog.api.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): LiveData<List<GameEntity>>

    @Query("SELECT * FROM games WHERE isFavorite = 1")
    fun getFavoriteGames(): LiveData<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGameById(id: Int): LiveData<GameEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM games)")
    suspend fun hasAnyGames(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("UPDATE games SET isFavorite = :isFavorite WHERE id = :gameId")
    suspend fun updateFavoriteStatus(gameId: Int, isFavorite: Boolean)

    @Query("DELETE FROM games")
    suspend fun clearGames()
}

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val thumbnail: String,
    val shortDescription: String,
    val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    val releaseDate: String,
    val freeToGameProfileUrl: String,
    val isFavorite: Boolean = false
)

fun Game.toEntity(): GameEntity = GameEntity(
    id, title, thumbnail, shortDescription, gameUrl,
    genre, platform, publisher, developer,
    releaseDate,
    freeToGameProfileUrl,
    isFavorite
)

fun GameEntity.toGame() = Game(
    id, title, thumbnail, shortDescription, gameUrl,
    genre, platform, publisher, developer,
    releaseDate,
    freeToGameProfileUrl,
    isFavorite
)
