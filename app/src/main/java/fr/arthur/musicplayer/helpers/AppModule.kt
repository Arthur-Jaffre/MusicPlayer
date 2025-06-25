package fr.arthur.musicplayer.helpers

import androidx.room.Room
import fr.arthur.musicplayer.helpers.AppConstants.DB_NAME
import fr.arthur.musicplayer.repositories.IMusicRepository
import fr.arthur.musicplayer.repositories.MusicRepository
import fr.arthur.musicplayer.room.AppDatabase
import fr.arthur.musicplayer.usecase.GetAllArtistsUseCase
import fr.arthur.musicplayer.usecase.GetAllMusicsUseCase
import fr.arthur.musicplayer.usecase.GetAllPlaylistUseCase
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DB_NAME
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<AppDatabase>().musicDao() }
    single { get<AppDatabase>().albumDao() }
    single { get<AppDatabase>().artistDao() }
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().playlistDao() }

    single { FolderUriStore(get()) }
    single { MusicScanner(get(), get()) }

    single { MusicRepository(get(), get(), get(), get(), get()) }
    single<IMusicRepository> { get<MusicRepository>() }

    factory { GetAllMusicsUseCase(get()) }
    factory { GetAllArtistsUseCase(get()) }
    factory { GetAllPlaylistUseCase(get()) }

    factory { MusicListViewModel(get()) }
    factory { ArtistListViewModel(get()) }
    factory { PlayListListViewModel(get()) }
}