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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.first.myapplic.MainActivity
import com.first.myapplic.MyApplic


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesList(navController: NavController, viewModel: NotesViewModel) {
    val openDialog = remember { mutableStateOf(false) }
    val deleteText = remember { mutableStateOf("") }
    val notesQuery = remember { mutableStateOf("") }
    val notesToDelete = remember { mutableStateOf(listOf<Note>()) }
    val notes = viewModel.notes.observeAsState()
    val context = LocalContext.current

    val allNotes = remember { mutableStateListOf<Note>() }
    val filteredNotes = remember { mutableStateListOf<Note>() }

    fun setFilteredNotes(newFilteredNotes: List<Note>) {
        filteredNotes.clear()
        filteredNotes.addAll(newFilteredNotes)
    }

    MyApplicTheme {
        Surface(modifier = Modifier.fillMaxSize(),
            color = Color.DarkGray
            ) {
            Scaffold(
                topBar = {
                    com.first.myapplic.uiph.NotesList.GenericAppBar(
                        title = stringResource(R.string.photo_notes),
                        onIconClick = {
                            SettingsScreen()
                        },
                        icon = {

                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_settings_24),
                                contentDescription = stringResource(id = R.string.settings),
                                tint = Color.White
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
                    modifier = Modifier
                        .padding(contentPadding)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF121212),
                                    Color(0xFF2C2C2C)
                                )
                            )
                        )
                ) {
                    SearchBar(
                        query = notesQuery,
                        notes = allNotes,
                        setFilteredNotes = { filteredList ->
                            setFilteredNotes(filteredList)
                        }
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

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

@Composable
fun GenericAppBar(
    title: String,
    onIconClick: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    iconState: MutableState<Boolean>
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.DarkGray
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier
                .width(250.dp))
            IconButton(onClick = {

            }) {
                icon()
            }
        }
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
fun SearchBar(query: MutableState<String>, notes: List<Note>, setFilteredNotes: (List<Note>) -> Unit) {
    var selectedFilter by remember { mutableStateOf(FilterType.DATE) }

    Column(Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 0.dp)) {
        TextField(
            value = query.value,
            placeholder = { Text("Search..") },
            maxLines = 1,
            onValueChange = { query.value = it },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF1E88E5)
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

        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(12.dp))
        ) {
            FilterButton(label = "Date", onClick = {
                selectedFilter = FilterType.DATE
                applyFilter(selectedFilter, notes, query.value, setFilteredNotes)
            })
            FilterButton(label = "Alphabetical", onClick = {
                selectedFilter = FilterType.ALPHABETICAL
                applyFilter(selectedFilter, notes, query.value, setFilteredNotes)
            })
        }
    }
}

fun applyFilter(
    filterType: FilterType,
    notes: List<Note>,
    query: String,
    setFilteredNotes: (List<Note>) -> Unit
) {
    val filteredNotes = when (filterType) {
        FilterType.DATE -> notes.sortedByDescending { it.dateUpdated }
        FilterType.ALPHABETICAL -> notes.sortedBy { it.title }
    }.filter { it.title.contains(query, true) || it.note.contains(query, true) }
    setFilteredNotes(filteredNotes)
}

enum class FilterType {
    DATE,
    ALPHABETICAL
}



@Composable
fun FilterButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(end = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
    ) {
        Text(text = label, color = Color.White)
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
        modifier = Modifier.background(Color(0xFF1E1E1E)).fillMaxSize()
    ) {
        val queriedNotes = if (query.value.isEmpty()) {
            notes
        } else {
            notes.filter { it.note.contains(query.value, true) || it.title.contains(query.value, true) }
        }
        itemsIndexed(queriedNotes) { index, note ->
            if (note.getDay() != previousHeader.value) {
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = note.getDay(), color = Color.White)
                }
                Spacer(modifier = Modifier.height(6.dp))
                previousHeader.value = note.getDay()
            }
            NoteListItem(
                note,
                openDialog,
                deleteText = deleteText,
                navController,
                notesToDelete = notesToDelete,
                noteBackGround = if (index % 2 == 0) {
                    Color(0xFF2A2A2A)
                } else Color(0xFF3C3C3C)
            )
            Spacer(modifier = Modifier.height(8.dp))
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { note.id?.let { navController.navigate(Constants.noteDetailNavigation(it)) } }
            .combinedClickable(
                onClick = { note.id?.let { navController.navigate(Constants.noteDetailNavigation(it)) } },
                onLongClick = {
                    openDialog.value = true
                    deleteText.value = "Do you want to delete this note?"
                    notesToDelete.value = listOf(note)
                }
            ),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = noteBackGround)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter(note.imageUri),
                contentDescription = "Note image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = note.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.note,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 2,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = note.dateUpdated,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun NotesFab(contentDescription: String, icon: Int, action: () -> Unit) {
    FloatingActionButton(
        onClick = { action() },
        containerColor = Color(0xFF1E88E5),
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(6.dp)
    ) {
        Icon(
            ImageVector.vectorResource(id = icon),
            contentDescription = contentDescription,
            tint = Color.White
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
                    IconButton(onClick = {

                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)) {
            Text(text = "Settings coming soon...", fontSize = 20.sp)
        }
    }
}


