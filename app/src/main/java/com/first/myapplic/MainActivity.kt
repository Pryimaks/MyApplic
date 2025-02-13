package com.first.myapplic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.first.myapplic.ui.theme.MyApplicTheme


import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.first.myapplic.uiph.EditNote.NoteEditScreen
import com.first.myapplic.uiph.NoteDetail.NoteDetailScreen
import com.first.myapplic.uiph.NotesList.NotesList

import com.first.myapplic.uiph.CreateNote.CreateNoteScreen
import com.first.myapplic.uiph.NotesViewModel
import com.first.myapplic.uiph.NotesViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notesViewModel =
            NotesViewModelFactory(PhotoNotesApp.getDao())
                .create(NotesViewModel::class.java)

        setContent {
            val navController = rememberNavController()

            MyApplicTheme {
                NavHost(
                    navController = navController,
                    startDestination = Constants.NAVIGATION_NOTES_LIST
                ) {
                    composable(Constants.NAVIGATION_NOTES_LIST) {
                        NotesList(navController, notesViewModel)
                    }

                    composable(
                        route = Constants.NAVIGATION_NOTE_DETAIL,
                        arguments = listOf(
                            navArgument(Constants.NAVIGATION_NOTE_DETAIL_Argument) {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTE_DETAIL_Argument) ?: 0
                        NoteDetailScreen(
                            noteId = noteId,
                            navController = navController,
                            viewModel = notesViewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = Constants.NAVIGATION_NOTE_EDIT,
                        arguments = listOf(
                            navArgument(Constants.NAVIGATION_NOTE_EDIT_Argument) {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTE_EDIT_Argument) ?: 0
                        NoteEditScreen(
                            noteId = noteId,
                            navController = navController,
                            viewModel = notesViewModel
                        )
                    }

                    composable(Constants.NAVIGATION_NOTES_CREATE) {
                        CreateNoteScreen(navController, notesViewModel)
                    }
                }
            }
        }
    }
}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoNotesAppBar(title: String, iconContentDescription: String) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(onClick = { Log.d("hello", "Delete button clicked") }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.note_delete),
                    contentDescription = iconContentDescription,
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

 */
/*
@Composable
fun NotesList(notes: List<Note>, modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = modifier
    ) {
        items(notes) { note ->
            NoteListItem(note)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
        }
    }
}


 */
/*
@Composable
fun NoteListItem(note: Note) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .fillMaxWidth()
            .clickable {
                Log.d("Note Clicked", "Clicked on note: ${note.title}")
            }
            .padding(12.dp)
    ) {
        Column {
            Text(text = note.title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp))
            Text(text = note.note, maxLines = 3, modifier = Modifier.padding(6.dp))
            Text(text = note.dateCreated, modifier = Modifier.padding(6.dp))
        }
    }
}

 */
/*
@Composable
fun NotesFab(contentDescription: String) {
    FloatingActionButton(
        onClick = { Log.d("FAB", "Create Note Clicked") },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.note_add_icon),
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

 */