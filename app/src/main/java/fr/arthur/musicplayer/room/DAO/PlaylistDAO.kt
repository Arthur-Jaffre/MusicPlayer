package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.arthur.musicplayer.room.entities.PlaylistEntity
import fr.arthur.musicplayer.room.entities.PlaylistMusicCrossRef
import fr.arthur.musicplayer.room.entities.PlaylistWithMusics

@Dao
interface PlaylistDAO {
    @Query("SELECT * FROM playlist")
    suspend fun getAll(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMusic(crossRef: PlaylistMusicCrossRef)

    @Transaction
    @Query("SELECT * FROM playlist WHERE name = :playlistName LIMIT 1")
    suspend fun getMusicsByPlaylistName(playlistName: String): PlaylistWithMusics

    @Delete
    suspend fun delete(playlist: PlaylistEntity)
}
