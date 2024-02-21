package ru.chani.checkcontacts.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.chani.checkcontacts.domain.models.Contact
import ru.chani.checkcontacts.presentation.MainViewModel
import ru.chani.checkcontacts.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    viewModel: MainViewModel
) {
    val contactsState = viewModel.contacts.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.contacts)) },
            )
        }
    ) { paddingValues ->

        ContactsList(
            paddingValues = paddingValues,
            contactsState = contactsState
        )

    }

}


@Composable
fun ContactsList(
    paddingValues: PaddingValues,
    contactsState: State<List<Contact>>
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
    ) {
        items(contactsState.value) { contact ->

            ContactCard(contact = contact)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun ContactCard(contact: Contact) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.person),
                contentDescription = "Contact's icon",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))
            Text(text = contact.name)
        }
    }
}