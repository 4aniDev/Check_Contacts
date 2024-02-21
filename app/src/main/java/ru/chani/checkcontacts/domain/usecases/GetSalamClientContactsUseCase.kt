package ru.chani.checkcontacts.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.domain.repositories.ContactRepository

class GetSalamClientContactsUseCase(private val repository: ContactRepository) {
    operator fun invoke(): Flow<List<Contact>> {
        return repository.getSalamClientContacts()
    }
}