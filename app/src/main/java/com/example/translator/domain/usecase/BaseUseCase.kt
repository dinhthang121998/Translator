package com.example.translator.domain.usecase

import kotlinx.coroutines.flow.Flow

interface BaseUseCase<in I, out O> {
    operator fun invoke(param: I): Flow<O>
}
