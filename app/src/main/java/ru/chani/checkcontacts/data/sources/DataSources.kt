package ru.chani.checkcontacts.data.sources

import kotlinx.coroutines.flow.Flow
import ru.chani.checkcontacts.data.sources.entities.ContactEntity

interface DataSource {

    fun getSalamClientContacts(): Flow<List<ContactEntity>>

    suspend fun saveListOfContactEntities(list: List<ContactEntity>)

}