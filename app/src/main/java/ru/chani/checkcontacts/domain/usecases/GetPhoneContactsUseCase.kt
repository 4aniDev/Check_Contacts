package ru.chani.checkcontacts.domain.usecases

import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.domain.repositories.ContactRepository

class GetPhoneContactsUseCase(private val repository: ContactRepository) {
    operator fun invoke(): List<Contact> {
        return repository.getPhoneContacts()
    }
}