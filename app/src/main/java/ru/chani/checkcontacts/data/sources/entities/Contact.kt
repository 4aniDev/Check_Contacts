package ru.chani.checkcontacts.data.sources.entities

import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.domain.models.SalamClientState


data class ContactEntity(
    val id: Int,
    val name: String,
    val numbers: List<String>,
    val isSalamClient: String = SalamClientState.Unchecked.toString()
)

object ContactConverter {

    fun toDataContactEntity(contact: Contact): ContactEntity {
        return ContactEntity(
            id = contact.id,
            name = contact.name,
            numbers = contact.numbers,
            isSalamClient = contact.isSalamClient.toString()
        )
    }

    fun toDomainContact(contact: ContactEntity): Contact {

        val salamClientState = when {
            contact.isSalamClient.contains(SalamClientState.Unchecked.toString()) -> {
                SalamClientState.Unchecked
            }

            contact.isSalamClient.contains(SalamClientState.Client.toString()) -> {
                SalamClientState.Client
            }

            contact.isSalamClient.contains(SalamClientState.ItIsNotClient.toString()) -> {
                SalamClientState.ItIsNotClient
            }

            else -> {
                throw IllegalArgumentException("Unknown SalamClientState: ${contact.isSalamClient}")
            }
        }

        return Contact(
            id = contact.id,
            name = contact.name,
            numbers = contact.numbers,
            isSalamClient = salamClientState
        )
    }
}
