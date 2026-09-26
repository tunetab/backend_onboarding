package com.icerockdev.domain

interface SuspendUseCase<I, O> {
    suspend operator fun invoke(input: I): O
}
