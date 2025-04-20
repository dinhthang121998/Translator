package com.example.domain.base

abstract class UseCase<in P, R> {
    operator fun invoke(parameter: P) = execute(parameter)

    protected abstract fun execute(parameter: P): R
}
