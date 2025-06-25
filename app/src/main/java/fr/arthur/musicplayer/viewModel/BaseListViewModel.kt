package fr.arthur.musicplayer.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseListViewModel {
    protected val job = Job()
    protected val scope = CoroutineScope(Dispatchers.Main + job)
    
}
