package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.arthur.musicplayer.room.entities.ArtistEntity

@Dao
interface ArtistDAO {
    @Query("SELECT * FROM artist")
    suspend fun getAll(): List<ArtistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<ArtistEntity>)

    @Delete
    suspend fun delete(artists: List<ArtistEntity>)

    @Query("DELETE FROM artist WHERE id NOT IN (SELECT artistId FROM MusicArtistCrossRef)")
    suspend fun deleteOrphanArtists()
}