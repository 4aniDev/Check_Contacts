package ru.chani.checkcontacts.domain.models

data class Contact(
    val id: Int,
    val name: String,
    val numbers: List<String>,
    val isSalamClient: SalamClientState = SalamClientState.Unchecked
)

sealed interface SalamClientState {

    data object Unchecked : SalamClientState

    data object Client : SalamClientState

    data object ItIsNotClient : SalamClientState
}


