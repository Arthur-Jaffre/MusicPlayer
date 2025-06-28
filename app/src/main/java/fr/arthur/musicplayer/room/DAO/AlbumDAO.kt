package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.arthur.musicplayer.room.entities.AlbumEntity

@Dao
interface AlbumDAO {
    @Query("SELECT * FROM album")
    suspend fun getAll(): List<AlbumEntity>

    @Query("SELECT * FROM album WHERE artistId = :artistId")
    suspend fun getFromArtist(artistId: String): List<AlbumEntity>

    @Query("SELECT * FROM album WHERE id = :id")
    suspend fun getById(id: String): AlbumEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)

    @Query("SELECT * FROM album WHERE name = :name")
    suspend fun getByName(name: String): List<AlbumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: AlbumEntity)

    @Delete
    suspend fun delete(albums: List<AlbumEntity>)

    @Query("DELETE FROM album WHERE id NOT IN (SELECT DISTINCT albumId FROM music)")
    suspend fun deleteOrphanAlbums()
    
    @Update
    suspend fun update(albums: AlbumEntity)
}