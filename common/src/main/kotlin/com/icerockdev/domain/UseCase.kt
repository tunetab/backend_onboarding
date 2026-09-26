package com.icerockdev.domain

interface UseCase<I, O> {
    operator fun invoke(input: I): O
}
