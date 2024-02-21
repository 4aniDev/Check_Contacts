package ru.chani.checkcontacts.data.sources.local

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.chani.checkcontacts.data.sources.DataSource
import ru.chani.checkcontacts.data.sources.entities.ContactEntity
import ru.chani.checkcontacts.data.sources.local.contentproviders.ContactsContentProvider
import ru.chani.checkcontacts.data.sources.local.db.ContactDbEntity
import ru.chani.checkcontacts.data.sources.local.db.ContactsDatabase

class LocalDataSource(
    private val context: Context
) : DataSource {

    private val contactsDb = ContactsDatabase.getDatabase(context = context)

    private val contactsContentProvider = ContactsContentProvider(context)

    override fun getSalamClientContacts(): Flow<List<ContactEntity>> {
        val contactsDbFlow = contactsDb.contactDao().getClientContacts()
        val contactEntitiesFlow = contactsDbFlow.map { listOfContactEntities ->
            listOfContactEntities.map { contactDbEntity ->
                ContactEntity(
                    id = contactDbEntity.id,
                    name = contactDbEntity.name,
                    numbers = contactDbEntity.numbers,
                    isSalamClient = contactDbEntity.isSalamClient
                )
            }
        }


        return contactEntitiesFlow
    }

    override suspend fun saveListOfContactEntities(list: List<ContactEntity>) {
        list.forEach { contactEntity ->
            contactsDb.contactDao().insertContact(
                ContactDbEntity(
                    id = contactEntity.id,
                    name = contactEntity.name,
                    numbers = contactEntity.numbers,
                    isSalamClient = contactEntity.isSalamClient
                )
            )
        }
    }

    suspend fun getSavedContactEntities(): List<ContactEntity> {
        val listOfContactDbEntities = contactsDb.contactDao().getAllContacts()
        val listOfContactEntities = mutableListOf<ContactEntity>()

        listOfContactDbEntities.forEach { contactDbEntity ->
            listOfContactEntities.add(
                ContactEntity(
                    id = contactDbEntity.id,
                    name = contactDbEntity.name,
                    numbers = contactDbEntity.numbers,
                    isSalamClient = contactDbEntity.isSalamClient
                )
            )
        }

        return listOfContactEntities
    }

    fun getPhoneContactEntities(): List<ContactEntity> {
        return contactsContentProvider.getListOfContactsEntity()
    }

}
