package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.arthur.musicplayer.room.entities.PlaylistEntity
import fr.arthur.musicplayer.room.entities.PlaylistMusicCrossRef

@Dao
interface PlaylistDAO {
    @Query("SELECT * FROM playlist")
    suspend fun getAll(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMusic(crossRef: PlaylistMusicCrossRef)

    @Delete
    suspend fun delete(playlist: PlaylistEntity)

    @Update
    suspend fun update(playlist: PlaylistEntity)
    
    @Query("DELETE FROM playlist_music_cross_ref WHERE playlistId = :playlistId")
    suspend fun deletePlaylistMusicCrossRef(playlistId: String)
}
