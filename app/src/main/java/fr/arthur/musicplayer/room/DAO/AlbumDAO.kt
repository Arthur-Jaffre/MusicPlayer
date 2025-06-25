package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.arthur.musicplayer.room.entities.AlbumEntity
import fr.arthur.musicplayer.room.entities.MusicEntity

@Dao
interface AlbumDAO {
    @Query("SELECT * FROM album")
    suspend fun getAll(): List<AlbumEntity>

    @Query("SELECT * FROM album WHERE artistId = :artistId")
    suspend fun getFromArtist(artistId: String): List<AlbumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)

    @Delete
    suspend fun delete(albums: List<AlbumEntity>)
}