package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.arthur.musicplayer.room.entities.MusicEntity
import fr.arthur.musicplayer.room.entities.PlaylistEntity

@Dao
interface PlaylistDAO {
    @Query("SELECT * FROM playlist")
    suspend fun getAll(): List<PlaylistEntity>
}
