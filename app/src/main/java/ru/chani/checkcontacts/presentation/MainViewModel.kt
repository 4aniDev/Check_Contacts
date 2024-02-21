package ru.chani.checkcontacts.presentation

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.chani.checkcontacts.data.repositories.ContactRepositoryImpl
import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.domain.usecases.GetSalamClientContactsUseCase
import ru.chani.checkcontacts.domain.usecases.MatchSavedAndPhoneContacts

class MainViewModel(
    private val context: Context
) : ViewModel() {

    private val contactRepository = ContactRepositoryImpl(context)

    private val matchSavedAndPhoneContacts = MatchSavedAndPhoneContacts(contactRepository)
    private val getSalamClientContactsUseCase = GetSalamClientContactsUseCase(contactRepository)


    val contacts: Flow<List<Contact>> = getSalamClientContactsUseCase()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    private val _isPermissionGranted = MutableStateFlow(false)
    val isPermissionGranted: StateFlow<Boolean> = _isPermissionGranted


    init {
        viewModelScope.launch {
            isPermissionGranted.collect {
                if (it) {
                    matchContacts()
                }
            }
        }
    }


    private fun matchContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            matchSavedAndPhoneContacts()
        }
    }




    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        } else {
            _isPermissionGranted.value = isGranted
        }
    }


}
