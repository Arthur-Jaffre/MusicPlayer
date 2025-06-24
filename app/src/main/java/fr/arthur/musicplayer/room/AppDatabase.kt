package fr.arthur.musicplayer.room

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.arthur.musicplayer.room.DAO.MusicDAO

@Database(entities = [MusicEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDAO
}