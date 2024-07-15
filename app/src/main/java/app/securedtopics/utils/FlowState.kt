package app.securedtopics.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

interface FlowState {
    fun <T> Flow<T>.stateInWhileSubscribed(
        scope: CoroutineScope,
        initialValue: T,
        timeout: Long = 5000
    ) = stateIn(scope, SharingStarted.WhileSubscribed(timeout), initialValue)

    fun <T> Flow<T>.stateInLazily(scope: CoroutineScope, initialValue: T) =
        stateIn(scope, SharingStarted.Lazily, initialValue)

    fun <T> Flow<T>.stateInEagerly(scope: CoroutineScope, initialValue: T) =
        stateIn(scope, SharingStarted.Eagerly, initialValue)

}

