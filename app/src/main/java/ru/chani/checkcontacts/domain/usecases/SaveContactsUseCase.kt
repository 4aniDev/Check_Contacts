package ru.chani.checkcontacts.domain.usecases

import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.domain.repositories.ContactRepository

class SaveContactsUseCase(private val repository: ContactRepository) {

    private val setRightStateSalamUserForContactUseCase = SetRightStateSalamUserForContactUseCase(repository)

    suspend operator fun invoke(listOfContacts: List<Contact>) {
        val checkedList = mutableListOf<Contact>()

        listOfContacts.forEach { contact ->
          val checkedContact = setRightStateSalamUserForContactUseCase(contact)
          checkedList.add(checkedContact)
        }

        repository.saveListOfContacts(checkedList)
    }
}