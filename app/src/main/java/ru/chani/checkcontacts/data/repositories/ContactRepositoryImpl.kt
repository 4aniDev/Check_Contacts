package ru.chani.checkcontacts.data.repositories

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.chani.checkcontacts.data.sources.entities.ContactConverter
import ru.chani.checkcontacts.data.sources.local.LocalDataSource
import ru.chani.checkcontacts.data.sources.remote.RemoteDataSource
import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.domain.repositories.ContactRepository

class ContactRepositoryImpl(
    private val context: Context
) : ContactRepository {

    private val localDatasource = LocalDataSource(context)
    private val remoteDataSource = RemoteDataSource()

    override fun getPhoneContacts(): List<Contact> {
        val listOfContacts =
            localDatasource.getPhoneContactEntities().map { ContactConverter.toDomainContact(it) }
        return listOfContacts
    }

    override suspend fun getSavedContacts(): List<Contact> {
        val listOfSavedContacts =
            localDatasource.getSavedContactEntities().map { ContactConverter.toDomainContact(it) }
        return listOfSavedContacts
    }

    override fun getSalamClientContacts(): Flow<List<Contact>> {
        val flowOfSalamContacts =
            localDatasource.getSalamClientContacts().map { listOfContactEntities ->
                listOfContactEntities.map { contactEntity ->
                    ContactConverter.toDomainContact(contactEntity)
                }
            }
        return flowOfSalamContacts
    }

    override suspend fun saveListOfContacts(list: List<Contact>) {
        val listOfContactEntities = list.map { ContactConverter.toDataContactEntity(it) }
        localDatasource.saveListOfContactEntities(listOfContactEntities)
    }

    override fun isContactSalamClient(contact: Contact): Boolean {
        return remoteDataSource.isContactEntitySalamClient(
            ContactConverter.toDataContactEntity(
                contact
            )
        )
    }
}