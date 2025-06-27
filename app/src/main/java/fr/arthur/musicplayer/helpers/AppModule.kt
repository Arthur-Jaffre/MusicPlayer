package fr.arthur.musicplayer.helpers

import androidx.room.Room
import fr.arthur.musicplayer.helpers.AppConstants.DB_NAME
import fr.arthur.musicplayer.repositories.AlbumRepository
import fr.arthur.musicplayer.repositories.ArtistRepository
import fr.arthur.musicplayer.repositories.MusicRepository
import fr.arthur.musicplayer.repositories.PlaylistRepository
import fr.arthur.musicplayer.repositories.ScannerRepository
import fr.arthur.musicplayer.repositories.interfaces.IAlbumRepository
import fr.arthur.musicplayer.repositories.interfaces.IArtistRepository
import fr.arthur.musicplayer.repositories.interfaces.IMusicRepository
import fr.arthur.musicplayer.repositories.interfaces.IPlaylistRepository
import fr.arthur.musicplayer.repositories.interfaces.IScannerRepository
import fr.arthur.musicplayer.room.AppDatabase
import fr.arthur.musicplayer.usecase.AlbumUseCase
import fr.arthur.musicplayer.usecase.ArtistUseCase
import fr.arthur.musicplayer.usecase.MusicUseCase
import fr.arthur.musicplayer.usecase.PlaylistUseCase
import fr.arthur.musicplayer.usecase.ScannerUseCase
import fr.arthur.musicplayer.viewModel.AlbumListViewModel
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.FavoritesViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import fr.arthur.musicplayer.viewModel.RecentlyAddedViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DB_NAME
        )
//            .fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<AppDatabase>().musicDao() }
    single { get<AppDatabase>().albumDao() }
    single { get<AppDatabase>().artistDao() }
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().MusicArtistDAO() }

    single { FolderUriStore(get()) }
    single { MusicScanner(get(), get()) }

    single { MusicRepository(get()) }
    single { AlbumRepository(get()) }
    single { ArtistRepository(get()) }
    single { PlaylistRepository(get()) }
    single { ScannerRepository(get(), get(), get(), get(), get()) }

    single<IMusicRepository> { get<MusicRepository>() }
    single<IAlbumRepository> { get<AlbumRepository>() }
    single<IArtistRepository> { get<ArtistRepository>() }
    single<IPlaylistRepository> { get<PlaylistRepository>() }
    single<IScannerRepository> { get<ScannerRepository>() }

    factory { MusicUseCase(get(), get(), get()) }
    factory { ArtistUseCase(get()) }
    factory { PlaylistUseCase(get()) }
    factory { AlbumUseCase(get()) }
    factory { ScannerUseCase(get()) }

    factory { MusicListViewModel(get(), get()) }
    factory { ArtistListViewModel(get()) }
    factory { PlayListListViewModel(get()) }
    factory { AlbumListViewModel(get()) }
    factory { FavoritesViewModel(get()) }
    factory { RecentlyAddedViewModel(get()) }

}