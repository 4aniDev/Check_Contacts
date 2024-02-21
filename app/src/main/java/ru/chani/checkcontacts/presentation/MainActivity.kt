package ru.chani.checkcontacts.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.chani.checkcontacts.presentation.screens.ContactsScreen
import ru.chani.checkcontacts.presentation.screens.PermissionAlert
import ru.chani.checkcontacts.presentation.screens.ReadContactsPermissionTextProvider
import ru.chani.checkcontacts.presentation.theme.CheckContatactsTheme

class MainActivity : ComponentActivity() {

    private val viewModelFactory by lazy { MainViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckContatactsTheme {
                val viewModel: MainViewModel = viewModel(factory = viewModelFactory)

                val dialogQueue = viewModel.visiblePermissionDialogQueue
                val contactsPermissionResultLauncher = initContactsPermissionLauncher(viewModel)
                val isPermissionGranted = viewModel.isPermissionGranted.collectAsState()

                // request permission at first time
                SideEffect {
                    contactsPermissionResultLauncher.launch(
                        Manifest.permission.READ_CONTACTS
                    )
                }

                // open main screen if permission granted
                // else request permissions
                if (isPermissionGranted.value) {
                    ContactsScreen(viewModel = viewModel)
                } else {
                    dialogQueue
                        .reversed()
                        .forEach { permission ->
                            PermissionAlert(
                                permissionTextProvider = when (permission) {
                                    Manifest.permission.READ_CONTACTS -> {
                                        ReadContactsPermissionTextProvider()
                                    }

                                    else -> return@forEach
                                },
                                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                    permission
                                ),
                                onDismiss = viewModel::dismissDialog,
                                onConfirmClick = { openAppSettings() }
                            )
                        }
                }
            }
        }
    }


    @Composable
    private fun initContactsPermissionLauncher(
        viewModel: MainViewModel
    ): ManagedActivityResultLauncher<String, Boolean> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                viewModel.onPermissionResult(
                    permission = Manifest.permission.READ_CONTACTS,
                    isGranted = isGranted
                )
            }
        )
    }

}


fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

