package fr.arthur.musicplayer.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.arthur.musicplayer.room.MusicEntity

@Dao
interface MusicDAO {
    @Query("SELECT * FROM music")
    suspend fun getAll(): List<MusicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tracks: List<MusicEntity>)

    @Delete
    suspend fun delete(tracks: List<MusicEntity>)

    @Query("DELETE FROM music")
    suspend fun clear()
}
