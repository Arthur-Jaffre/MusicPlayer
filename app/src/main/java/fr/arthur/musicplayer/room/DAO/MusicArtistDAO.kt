package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.arthur.musicplayer.room.entities.MusicArtistCrossRef

@Dao
interface MusicArtistDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(crossRefs: List<MusicArtistCrossRef>)

    @Query("DELETE FROM MusicArtistCrossRef")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM MusicArtistCrossRef WHERE artistId = :artistId")
    suspend fun countByArtistId(artistId: String): Int
}
