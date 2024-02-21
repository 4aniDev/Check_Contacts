package ru.chani.checkcontacts.data.sources.local.contentproviders

import android.content.Context
import android.provider.ContactsContract
import ru.chani.checkcontacts.data.sources.entities.ContactEntity

class ContactsContentProvider(private val context: Context) {

    fun getListOfContactsEntity(): List<ContactEntity> {

        val listOfContactEntities = mutableListOf<ContactEntity>()

        val cursorForIdAndName = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null,
        )

        cursorForIdAndName?.let {
            while (cursorForIdAndName.moveToNext()) {
                // get contact's ID
                val contactId =
                    cursorForIdAndName.getInt(
                        cursorForIdAndName.getColumnIndexOrThrow(
                            ContactsContract.Contacts._ID
                        )
                    )
                // get contact' Name
                val contactName =
                    cursorForIdAndName.getString(
                        cursorForIdAndName.getColumnIndexOrThrow(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )

                // create cursor for getting PhoneNumber
                val phoneCursor = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "contact_id  = $contactId",
                    null,
                    null
                )

                phoneCursor?.let {
                    while (phoneCursor.moveToNext()) {
                        // get contact's Number by ID
                        val contactNumber: String =
                            phoneCursor.getString(
                                phoneCursor.getColumnIndexOrThrow(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )

                        //delete space between dec
                        val formattedNumber = contactNumber.filter { !it.isWhitespace() }

                        // checking
                        // (if contact contains more than 1 number add additional number
                        // else just add new contact with 1 number)
                        if (phoneCursor.position != 0 && listOfContactEntities.isNotEmpty()) {

                            // add additional number to existing contact
                            val oldContact = listOfContactEntities[listOfContactEntities.lastIndex].copy()
                            val newNumbers = oldContact.numbers.toMutableList()
                            newNumbers.add(formattedNumber)

                            listOfContactEntities[listOfContactEntities.lastIndex] =
                                oldContact.copy(numbers = newNumbers)

                        } else {
                            listOfContactEntities.add(
                                ContactEntity(
                                    id = contactId,
                                    name = contactName,
                                    numbers = mutableListOf(formattedNumber)
                                )
                            )
                        }

                    }

                    phoneCursor.close()
                }
            }

            cursorForIdAndName.close()
        }

        return listOfContactEntities
    }

}