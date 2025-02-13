package com.first.myapplic.uiph.EditNote

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import com.first.myapplic.model.Note
import com.first.myapplic.test.TestConstants
import com.first.myapplic.ui.theme.MyApplicTheme
import com.first.myapplic.uiph.NotesList.NotesFab

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteEditScreen(noteId: Int, navController: NavController) {
    val note = TestConstants.notes.find { it.id == noteId }
        ?: Note(id = 0, title = "Cannot find note details", note = "Cannot find note details")

    val currentNote = remember { mutableStateOf(note.note) }
    val currentTitle = remember { mutableStateOf(note.title) }
    val saveButtonState = remember { mutableStateOf(false) }

    MyApplicTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Edit Note",
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
                        contentDescription = stringResource(R.string.create_note),
                        action = {},
                        icon = R.drawable.camera
                    )
                }
            ) { paddingValues -> // Додано paddingValues
                Column(
                    Modifier
                        .background(Color.White)
                        .padding(paddingValues)
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    TextField(
                        value = currentTitle.value,
                        onValueChange = { value ->
                            currentTitle.value = value
                            saveButtonState.value = currentTitle.value != note.title ||
                                    currentNote.value != note.note
                        },
                        label = { Text(text = "Title") }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(
                        value = currentNote.value,
                        onValueChange = { value ->
                            currentNote.value = value
                            saveButtonState.value = currentTitle.value != note.title ||
                                    currentNote.value != note.note
                        },
                        label = { Text(text = "Body") }
                    )
                }
            }
        }
    }
}
