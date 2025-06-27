package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.arthur.musicplayer.helpers.AppConstants.MAX_RECENTLY_ADDED
import fr.arthur.musicplayer.room.entities.MusicArtistCrossRef
import fr.arthur.musicplayer.room.entities.MusicEntity
import fr.arthur.musicplayer.room.entities.MusicWithArtists

@Dao
interface MusicDAO {
    @Transaction
    @Query("SELECT * FROM music ORDER BY title")
    suspend fun getAll(): List<MusicWithArtists>
    
    @Transaction
    @Query("SELECT * FROM music WHERE albumId = :albumId")
    suspend fun getFromAlbum(albumId: String): List<MusicWithArtists>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(musics: List<MusicEntity>)

    @Delete
    suspend fun delete(musics: List<MusicEntity>)

    @Transaction
    @Query("SELECT * FROM music WHERE isFavorite = 1")
    suspend fun getAllFavoritesMusics(): List<MusicWithArtists>

    @Query("UPDATE music SET isFavorite = :favorite WHERE id = :musicId")
    suspend fun updateFavorites(musicId: String, favorite: Int)

    @Query("DELETE FROM MusicArtistCrossRef WHERE musicId = :musicId")
    suspend fun deleteArtistCrossRefs(musicId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtistCrossRefs(refs: List<MusicArtistCrossRef>)

    @Transaction
    @Query("SELECT * FROM music ORDER BY addedAt DESC LIMIT :maxMusics")
    suspend fun getRecentlyAdded(maxMusics: Int = MAX_RECENTLY_ADDED): List<MusicWithArtists>

    @Transaction
    @Query(" SELECT m.* FROM music AS m INNER JOIN MusicArtistCrossRef AS ma ON m.id = ma.musicId WHERE ma.artistId = :artistId")
    suspend fun getFromArtist(artistId: String): List<MusicWithArtists>


    @Transaction
    suspend fun updateMusicWithRelations(
        music: MusicEntity,
        newRefs: List<MusicArtistCrossRef>
    ) {
        deleteArtistCrossRefs(music.id)
        insertAll(listOf(music))
        insertArtistCrossRefs(newRefs)
    }
}
