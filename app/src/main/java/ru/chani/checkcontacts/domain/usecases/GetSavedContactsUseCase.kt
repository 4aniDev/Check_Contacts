package ru.chani.checkcontacts.domain.usecases

import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.domain.repositories.ContactRepository

class GetSavedContactsUseCase(private val repository: ContactRepository) {
    suspend operator fun invoke(): List<Contact> {
        return repository.getSavedContacts()
    }
}