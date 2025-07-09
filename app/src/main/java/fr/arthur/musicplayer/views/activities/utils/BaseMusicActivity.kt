package fr.arthur.musicplayer.views.activities.utils

import androidx.appcompat.app.AppCompatActivity
import fr.arthur.musicplayer.adapters.MusicAdapter
import fr.arthur.musicplayer.helpers.MusicAdapterHandler
import fr.arthur.musicplayer.manager.PlayerManager
import fr.arthur.musicplayer.viewModel.ArtistListViewModel
import fr.arthur.musicplayer.viewModel.MusicListViewModel
import fr.arthur.musicplayer.viewModel.PlayListListViewModel

abstract class BaseMusicActivity : AppCompatActivity() {
    protected abstract val playlistViewModel: PlayListListViewModel
    protected abstract val musicViewModel: MusicListViewModel
    protected abstract val artistViewModel: ArtistListViewModel
    protected val musicAdapter: MusicAdapter by lazy {
        val handler = MusicAdapterHandler(
            playlistViewModel = playlistViewModel,
            toFavorites = { musicViewModel.updateFavorites(it) },
            onArtistClick = { artistId -> artistViewModel.getArtistById(artistId) },
            onMusicClick = { clickedMusic ->
                val list = musicAdapter.currentList
                PlayerManager.playQueue(
                    list.indexOfFirst {
                        it.id == clickedMusic.id
                    },
                    list,
                    this
                )
            }
        )

        MusicAdapter(
            onShowOptions = { context, music ->
                handler.showOptions(context, music)
            },
            onMusicClick = { music ->
                handler.onMusicClicked(music)
            }
        )

    }
}