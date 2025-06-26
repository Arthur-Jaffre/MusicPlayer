package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.arthur.musicplayer.room.entities.MusicEntity

@Dao
interface MusicDAO {
    @Query("SELECT * FROM music")
    suspend fun getAll(): List<MusicEntity>

    @Query("SELECT * FROM music WHERE albumId = :albumId")
    suspend fun getFromAlbum(albumId: String): List<MusicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(musics: List<MusicEntity>)

    @Delete
    suspend fun delete(musics: List<MusicEntity>)

    @Query("SELECT * FROM music WHERE isFavorite = 1")
    suspend fun getAllFavoritesMusics(): List<MusicEntity>

    @Query("UPDATE music SET isFavorite = :favorite WHERE id = :musicId")
    suspend fun updateFavorites(musicId: String, favorite: Int)
}
