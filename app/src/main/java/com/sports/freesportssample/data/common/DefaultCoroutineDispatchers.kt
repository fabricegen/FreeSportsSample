package com.sports.freesportssample.data.common

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

data class DefaultCoroutineDispatchers @Inject constructor(
    @IoDispatcher override val io: CoroutineDispatcher,
    @MainDispatcher override val main: CoroutineDispatcher,
    @DefaultDispatcher override val default: CoroutineDispatcher
) : CoroutineDispatchers
