package fr.arthur.musicplayer.helpers

import fr.arthur.musicplayer.repositories.IMusicRepository
import fr.arthur.musicplayer.repositories.MusicRepository
import fr.arthur.musicplayer.usecase.GetAllArtistsUseCase
import fr.arthur.musicplayer.usecase.GetAllMusicsUseCase
import fr.arthur.musicplayer.usecase.GetAllPlaylistUseCase
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel
import org.koin.dsl.module

val appModule = module {
    // single<IMusicRepository> { FakeMusicRepository() }
    single { FolderUriStore(get()) }
    single { MusicScanner(get(), get()) }
    single<IMusicRepository> { get<MusicRepository>() }
    single { MusicRepository(get()) }

    factory { GetAllMusicsUseCase(get()) }
    factory { GetAllArtistsUseCase(get()) }
    factory { GetAllPlaylistUseCase(get()) }

    factory { MusicListViewModel(get()) }
    factory { ArtistListViewModel(get()) }
    factory { PlayListListViewModel(get()) }
}