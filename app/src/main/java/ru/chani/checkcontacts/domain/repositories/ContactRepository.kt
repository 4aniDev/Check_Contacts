package ru.chani.checkcontacts.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.chani.checkcontacts.domain.models.Contact

interface ContactRepository {
    fun getPhoneContacts(): List<Contact>

    suspend fun getSavedContacts(): List<Contact>

    fun getSalamClientContacts(): Flow<List<Contact>>

    suspend fun saveListOfContacts(list: List<Contact>)

    fun isContactSalamClient(contact: Contact): Boolean

}