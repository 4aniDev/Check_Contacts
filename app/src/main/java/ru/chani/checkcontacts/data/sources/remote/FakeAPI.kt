package ru.chani.checkcontacts.data.sources.remote

import ru.chani.checkcontacts.data.sources.entities.ContactEntity

object FakeAPI {
    private val DB = listOf(
        SalamUser(
            id = 0,
            userName = "Bayram",
            number = "+99365702215"
        ),
        SalamUser(
            id = 1,
            userName = "Aman",
            number = "+99365093295"
        ),
        SalamUser(
            id = 3,
            userName = "Dan",
            number = "+99364051530"
        ),
        SalamUser(
            id = 4,
            userName = "Test Account - 1",
            number = "+99365111111"
        ),
        SalamUser(
            id = 5,
            userName = "Test Account - 2",
            number = "+99365222222"
        ),
        SalamUser(
            id = 6,
            userName = "Test Account - 3",
            number = "+99365333333"
        )
    )


    fun isContactEntitySalamClient(contactEntity: ContactEntity): Boolean {

        contactEntity.numbers.forEach { number ->
            DB.forEach { salamUser ->
                if (salamUser.number == number) return true
            }
        }

        return false
    }


}