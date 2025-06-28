package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.arthur.musicplayer.room.entities.ArtistEntity
import fr.arthur.musicplayer.room.entities.MusicArtistCrossRef

@Dao
interface ArtistDAO {
    @Query("SELECT * FROM artist")
    suspend fun getAll(): List<ArtistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<ArtistEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(artist: ArtistEntity)

    @Delete
    suspend fun delete(artists: List<ArtistEntity>)

    @Query("DELETE FROM artist WHERE id NOT IN (SELECT artistId FROM MusicArtistCrossRef)")
    suspend fun deleteOrphanArtists()

    @Query("SELECT * FROM artist WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<ArtistEntity>

    @Query("SELECT * FROM artist WHERE id = :id")
    suspend fun getById(id: String): ArtistEntity

    @Query("DELETE FROM artist WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT id FROM artist")
    suspend fun getAllArtistIds(): List<String>

    @Query("SELECT * FROM MusicArtistCrossRef WHERE artistId = :artistId")
    suspend fun getArtistRefs(artistId: String): List<MusicArtistCrossRef>

    @Update
    suspend fun update(artist: ArtistEntity)
}