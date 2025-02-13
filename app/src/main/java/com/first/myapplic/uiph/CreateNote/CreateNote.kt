package com.first.myapplic.uiph.CreateNote

import android.graphics.Insets.add
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.first.myapplic.GenericAppBar
import com.first.myapplic.PhotoNotesApp
import com.first.myapplic.R
import com.first.myapplic.model.Note
import com.first.myapplic.ui.theme.MyApplicTheme
import com.first.myapplic.uiph.NotesList.NotesFab
import com.first.myapplic.uiph.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {

    val currentNote = remember { mutableStateOf("") }
    val currentTitle = remember { mutableStateOf("") }
    val currentPhotos = remember { mutableStateOf("") }
    val saveButtonState = remember { mutableStateOf(false) }

    fun updateSaveButtonState() {
        saveButtonState.value = currentTitle.value.isNotBlank() && currentNote.value.isNotBlank()
    }

    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        if (it != null) {
            PhotoNotesApp.getUriPermission(it)
        }
        currentPhotos.value = it.toString()
    }

    MyApplicTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Create Note",
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = stringResource(R.string.save_note),
                                tint = Color.Black
                            )
                        },
                        onIconClick = {
                            viewModel.createNote(
                                currentTitle.value,
                                currentNote.value,
                                currentPhotos.value
                            )
                            navController.popBackStack()
                        },
                        iconState = saveButtonState
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = stringResource(R.string.add_image),
                        action = {
                            getImageRequest.launch(arrayOf("image/*"))
                        },
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

                    if (currentPhotos.value.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(data = Uri.parse(currentPhotos.value))
                                    .build()
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight(0.3f)
                                .padding(6.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    TextField(
                        value = currentTitle.value,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black,
                        ),
                        onValueChange = { value ->
                            currentTitle.value = value
                            saveButtonState.value =
                                currentTitle.value != "" && currentNote.value != ""
                        },
                        label = { Text(text = "Title") }
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    TextField(
                        value = currentNote.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black,
                        ),
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth(),
                        onValueChange = { value ->
                            currentNote.value = value
                            saveButtonState.value =
                                currentTitle.value != "" && currentNote.value != ""
                        },
                        label = { Text(text = "Body") }
                    )
                }
            }

        }
    }
}
