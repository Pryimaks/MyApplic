package com.first.myapplic.uiph.NotesList

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.first.myapplic.Constants
import com.first.myapplic.model.Note
import com.first.myapplic.model.getDay
import com.first.myapplic.test.TestConstants
import com.first.myapplic.ui.theme.MyApplicTheme
import com.first.myapplic.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.first.myapplic.Constants.orPlaceHolderList
import com.first.myapplic.GenericAppBar
import com.first.myapplic.ui.theme.noteBGBlue
import com.first.myapplic.ui.theme.noteBGYellow
import com.first.myapplic.uiph.NotesViewModel
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesList(navController: NavController, viewModel: NotesViewModel) {
    val openDialog = remember { mutableStateOf(false) }
    val deleteText = remember { mutableStateOf("") }
    val notesQuery = remember { mutableStateOf("") }
    val notesToDelete = remember { mutableStateOf(listOf<Note>()) }
    val notes = viewModel.notes.observeAsState()
    val context = LocalContext.current

    MyApplicTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = stringResource(R.string.photo_notes),
                        onIconClick = {
                            if (!notes.value.isNullOrEmpty()) {
                                openDialog.value = true
                                deleteText.value = "Are you sure you want to delete all notes?"
                                notesToDelete.value = notes.value ?: emptyList()
                            } else {
                                Toast.makeText(context, "No Notes found.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.note_delete),
                                contentDescription = stringResource(id = R.string.delete_note),
                                tint = Color.Black
                            )
                        },
                        iconState = remember { mutableStateOf(true) }
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = stringResource(R.string.create_note),
                        action = { navController.navigate(Constants.NAVIGATION_NOTES_CREATE) },
                        icon = R.drawable.note_add_icon
                    )
                }
            ) { contentPadding ->
                Column(
                    modifier = Modifier.padding(contentPadding)
                ) {
                    SearchBar(notesQuery)
                    NotesList(
                        notes = notes.value.orPlaceHolderList(),
                        query = notesQuery,
                        openDialog = openDialog,
                        deleteText = deleteText,
                        navController = navController,
                        notesToDelete = notesToDelete
                    )
                }
                DeleteDialog(
                    openDialog = openDialog,
                    text = deleteText,
                    notesToDelete = notesToDelete,
                    action = {
                        notesToDelete.value.forEach {
                            viewModel.deleteNotes(it)
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: MutableState<String>) {
    Column(Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 0.dp)) {
        TextField(
            value = query.value,
            placeholder = { Text("Search..") },
            maxLines = 1,
            onValueChange = { query.value = it },
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Black,
            ),
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { query.value = "" }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.icon_cross),
                            contentDescription = stringResource(
                                R.string.clear_search
                            )
                        )
                    }
                }
            })
    }
}

@Composable
fun NotesList(
    notes: List<Note>,
    openDialog: MutableState<Boolean>,
    query: MutableState<String>,
    deleteText: MutableState<String>,
    navController: NavController,
    notesToDelete: MutableState<List<Note>>
) {
    val previousHeader = remember { mutableStateOf("") }
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
    ) {
        val queriedNotes = if (query.value.isEmpty()){
            notes
        } else {
            notes.filter { it.note.contains(query.value) || it.title.contains(query.value) }
        }
        itemsIndexed(queriedNotes) { index, note ->
            if (note.getDay() != previousHeader.value) {
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = note.getDay(), color = Color.Black)
                }
                Spacer(modifier = Modifier.height(6.dp))
                previousHeader.value = note.getDay()
            }
            NoteListItem(
                note,
                openDialog,
                deleteText = deleteText ,
                navController,
                notesToDelete = notesToDelete,
                noteBackGround = if (index % 2 == 0) {
                    noteBGYellow
                } else noteBGBlue
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    note: Note,
    openDialog: MutableState<Boolean>,
    deleteText: MutableState<String>,
    navController: NavController,
    noteBackGround: Color,
    notesToDelete: MutableState<List<Note>>
) {
    Box(modifier = Modifier.height(120.dp).clip(RoundedCornerShape(12.dp))) {
        Column(
            modifier = Modifier
                .background(noteBackGround)
                .fillMaxWidth()
                .height(120.dp)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = {
                        note.id?.let {
                            navController.navigate(Constants.noteDetailNavigation(it))
                        }
                    },
                    onLongClick = {
                        if (note.id != 0) {
                            openDialog.value = true
                            deleteText.value = "Are you sure you want to delete this note ?"
                            notesToDelete.value = listOf(note)
                        }
                    }
                )
        ) {

            Row() {
                if (note.imageUri != null && note.imageUri.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(data = Uri.parse(note.imageUri))
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.3f),
                        contentScale = ContentScale.Crop
                    )
                }
            }

                Column() {
                    Text(
                        text = note.title,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Text(
                        text = note.note,
                        color = Color.Black,
                        maxLines = 3,
                        modifier = Modifier.padding(12.dp)
                    )
                    Text(
                        text = note.dateUpdated,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
    }
}

@Composable
fun NotesFab(contentDescription: String, icon: Int, action: () -> Unit) {
    FloatingActionButton(
        onClick = { action() },
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Icon(
            ImageVector.vectorResource(id = icon),
            contentDescription = contentDescription,
            tint = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    openDialog: MutableState<Boolean>,
    text: MutableState<String>,
    action: () -> Unit,
    notesToDelete: MutableState<List<Note>>
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Delete Note", color = MaterialTheme.colorScheme.primary) },
            text = { Text(text.value, color = MaterialTheme.colorScheme.onSurface) },
            confirmButton = {
                Button(
                    onClick = {
                        action()
                        openDialog.value = false
                        notesToDelete.value = emptyList()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        notesToDelete.value = emptyList()
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text(text = "Settings coming soon...", fontSize = 20.sp)
        }
    }
}

