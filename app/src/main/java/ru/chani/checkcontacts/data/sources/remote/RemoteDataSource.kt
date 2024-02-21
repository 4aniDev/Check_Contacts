package ru.chani.checkcontacts.data.sources.remote

import kotlinx.coroutines.flow.Flow
import ru.chani.checkcontacts.data.sources.DataSource
import ru.chani.checkcontacts.data.sources.entities.ContactEntity

class RemoteDataSource: DataSource {

    fun isContactEntitySalamClient(contactEntity: ContactEntity): Boolean {
        return FakeAPI.isContactEntitySalamClient(contactEntity = contactEntity)
    }

    override fun getSalamClientContacts(): Flow<List<ContactEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveListOfContactEntities(list: List<ContactEntity>) {
        TODO("Not yet implemented")
    }

}