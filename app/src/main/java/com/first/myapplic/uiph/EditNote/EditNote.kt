package com.first.myapplic.uiph.EditNote

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import com.first.myapplic.Constants
import com.first.myapplic.GenericAppBar
import com.first.myapplic.R
import com.first.myapplic.model.Note
import com.first.myapplic.test.TestConstants
import com.first.myapplic.ui.theme.MyApplicTheme
import com.first.myapplic.uiph.NotesList.NotesFab
import com.first.myapplic.uiph.NotesViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.first.myapplic.PhotoNotesApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteEditScreen(noteId: Int, navController: NavController, viewModel: NotesViewModel) {
    val scope = rememberCoroutineScope()
    val note = remember { mutableStateOf(Constants.noteDetailPlaceHolder) }
    val currentNote = remember { mutableStateOf(note.value.note) }
    val currentTitle = remember { mutableStateOf(note.value.title) }
    val currentPhotos = remember { mutableStateOf(note.value.imageUri) }
    val saveButtonState = remember { mutableStateOf(false) }

    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            PhotoNotesApp.getUriPermission(uri)
            currentPhotos.value = uri.toString()
            if (currentPhotos.value != note.value.imageUri) {
                saveButtonState.value = true
            }
        }
    }

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            val fetchedNote = viewModel.getNote(noteId) ?: Constants.noteDetailPlaceHolder
            note.value = fetchedNote
            currentNote.value = fetchedNote.note
            currentTitle.value = fetchedNote.title
            currentPhotos.value = fetchedNote.imageUri
        }
    }

    MyApplicTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Edit Note",
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = stringResource(R.string.save_note),
                                tint = Color.Black
                            )
                        },
                        onIconClick = {
                            viewModel.updateNote(
                                Note(
                                    id = note.value.id,
                                    note = currentNote.value,
                                    title = currentTitle.value,
                                    imageUri = currentPhotos.value
                                )
                            )
                            navController.popBackStack()
                        },
                        iconState = saveButtonState
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = stringResource(R.string.add_photo),
                        action = {
                            getImageRequest.launch(arrayOf("image/*"))
                        },
                        icon = R.drawable.camera // Переніс значення в NotesFab
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    if (currentPhotos.value?.isNotEmpty() == true) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(data = Uri.parse(currentPhotos.value))
                                    .build()
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.3f)
                                .padding(6.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    TextField(
                        value = currentTitle.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        onValueChange = { value ->
                            currentTitle.value = value
                            saveButtonState.value = (currentTitle.value != note.value.title) || (currentNote.value != note.value.note)
                        },
                        label = { Text(text = "Title") }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = currentNote.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        onValueChange = { value ->
                            currentNote.value = value
                            saveButtonState.value = (currentTitle.value != note.value.title) || (currentNote.value != note.value.note)
                        },
                        label = { Text(text = "Body") }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

