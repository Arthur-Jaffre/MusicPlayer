package fr.arthur.musicplayer.observer

import android.os.Handler
import android.os.Looper

// presentation/observer/SimpleObservable.kt
class SimpleObservable<T> {
    private val observers = mutableListOf<(T) -> Unit>()
    internal var value: T? = null

    fun observe(observer: (T) -> Unit) {
        value?.let { safeCall(observer, it) }
        observers.add(observer)
    }

    fun post(newValue: T) {
        value = newValue
        observers.forEach { observer ->
            safeCall(observer, newValue)
        }
    }

    private fun safeCall(observer: (T) -> Unit, value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            observer(value)
        } else {
            Handler(Looper.getMainLooper()).post {
                observer(value)
            }
        }
    }
}
