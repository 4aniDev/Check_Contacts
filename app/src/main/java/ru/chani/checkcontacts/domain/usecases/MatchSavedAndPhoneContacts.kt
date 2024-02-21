package ru.chani.checkcontacts.domain.usecases

import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.domain.repositories.ContactRepository

class MatchSavedAndPhoneContacts(private val repository: ContactRepository) {

    private val getPhoneContactsUseCase = GetPhoneContactsUseCase(repository)
    private val getSavedContactsUseCase = GetSavedContactsUseCase(repository)
    private val saveContactsUseCase = SaveContactsUseCase(repository)

    suspend operator fun invoke() {
        val savedContacts = getSavedContactsUseCase()
        val phoneContacts = getPhoneContactsUseCase()


        // if we run app at first time, we just save contacts from phone
        // another way we save only new contacts
        if (savedContacts.isNotEmpty()) {
            val newContacts = findNewContacts(
                savedContacts = savedContacts,
                phoneContact = phoneContacts
            )
            saveContactsUseCase(newContacts)
        } else {
            saveContactsUseCase(phoneContacts)
        }

    }

    private fun findNewContacts(
        savedContacts: List<Contact>,
        phoneContact: List<Contact>
    ): List<Contact> {

        val savedContactsSet = savedContacts.toHashSet()
        val phoneContactsSet = phoneContact.toHashSet()

        val newContacts = phoneContactsSet.subtract(savedContactsSet).toList()

        return newContacts
    }
}