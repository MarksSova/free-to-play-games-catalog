package com.github.markssova.gamescatalog.dao

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
    suspend fun getAllGames(): List<GameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

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
    val releaseDate: String, // Store as String (ISO format) or Long
    val freeToGameProfileUrl: String,
    val favorite: Boolean = false
)

fun Game.toEntity(): GameEntity = GameEntity(
    id, title, thumbnail, shortDescription, gameUrl,
    genre, platform, publisher, developer,
    releaseDate.toString(),
    freeToGameProfileUrl
)
