package com.first.myapplic.uiph.CreateNote

import android.Manifest
import android.content.Intent
import android.graphics.Insets.add
import android.net.Uri
import android.widget.Toast
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
import androidx.core.content.FileProvider
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
import java.io.File
import android.os.Environment

import androidx.activity.compose.rememberLauncherForActivityResult



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {
    val context = LocalContext.current
    val currentNote = remember { mutableStateOf("") }
    val currentTitle = remember { mutableStateOf("") }
    val currentPhotos = remember { mutableStateOf("") }
    val saveButtonState = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val photoUri = remember { mutableStateOf<Uri?>(null) }


    val takePhotoRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri.value != null) {
            currentPhotos.value = photoUri.value.toString()
        }
    }

    val cameraPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                File(context.cacheDir, "photo.jpg")
            )
            photoUri.value = uri
            takePhotoRequest.launch(uri)
        } else {
            Toast.makeText(context, "Доступ до камери відхилено", Toast.LENGTH_SHORT).show()
        }
    }


    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            currentPhotos.value = it.toString()
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
                        action = { showDialog.value = true },
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
                                ImageRequest.Builder(LocalContext.current)
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
                                currentTitle.value.isNotEmpty() && currentNote.value.isNotEmpty()
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
                                currentTitle.value.isNotEmpty() && currentNote.value.isNotEmpty()
                        },
                        label = { Text(text = "Body") }
                    )
                }
            }


            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Додати фото") },
                    text = { Text("Оберіть джерело зображення") },
                    confirmButton = {
                        Button(onClick = {
                            showDialog.value = false
                            getImageRequest.launch(arrayOf("image/*"))
                        }) {
                            Text("Галерея")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            showDialog.value = false
                            cameraPermission.launch(Manifest.permission.CAMERA)
                        }) {
                            Text("Камера")
                        }
                    }
                )

            }
        }
    }
}