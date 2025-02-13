package com.first.myapplic.uiph.CreateNote

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.first.myapplic.GenericAppBar
import com.first.myapplic.R
import com.first.myapplic.ui.theme.MyApplicTheme
import com.first.myapplic.uiph.NotesList.NotesFab

@Composable
fun CreateNoteScreen(navController: NavController) {
    val currentNote = remember { mutableStateOf("") }
    val currentTitle = remember { mutableStateOf("") }
    val saveButtonState = remember { mutableStateOf(false) }

    fun updateSaveButtonState() {
        saveButtonState.value = currentTitle.value.isNotBlank() && currentNote.value.isNotBlank()
    }

    MyApplicTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Create Note",
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = stringResource(R.string.save_note),
                                tint = Color.White
                            )
                        },
                        onIconClick = { navController.popBackStack() },
                        iconState = saveButtonState
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = stringResource(R.string.add_photo),
                        action = {},
                        icon = R.drawable.camera
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(12.dp)
                ) {
                    TextField(
                        value = currentTitle.value,
                        onValueChange = { value ->
                            currentTitle.value = value
                            updateSaveButtonState()
                        },
                        label = { Text(text = "Title") }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(
                        value = currentNote.value,
                        onValueChange = { value ->
                            currentNote.value = value
                            updateSaveButtonState()
                        },
                        label = { Text(text = "Body") }
                    )
                }
            }
        }
    }
}
