package ru.chani.checkcontacts.domain.usecases

import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.domain.models.SalamClientState
import ru.chani.checkcontacts.domain.repositories.ContactRepository

class SetRightStateSalamUserForContactUseCase(private val repository: ContactRepository) {
    suspend operator fun invoke(contact: Contact): Contact {

        return if (repository.isContactSalamClient(contact)) {
            contact.copy(isSalamClient = SalamClientState.Client)
        } else {
            contact.copy(isSalamClient = SalamClientState.ItIsNotClient)
        }

    }
}