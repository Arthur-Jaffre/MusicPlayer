package fr.arthur.musicplayer.helpers

class Event<T>(private val content: T) {

    private var handled = false

    fun getIfNotHandled(): T? {
        return if (handled) null else {
            handled = true
            content
        }
    }
}
