package com.first.myapplic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.first.myapplic.ui.theme.MyApplicTheme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import com.first.myapplic.model.Note
import com.first.myapplic.test.TestConstants


import androidx.compose.foundation.lazy.items

import androidx.compose.material3.*

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.first.myapplic.uiph.EditNote.NoteEditScreen
import com.first.myapplic.uiph.NoteDetail.NoteDetailScreen
import com.first.myapplic.uiph.NotesList.NotesList

import com.first.myapplic.ui.theme.MyApplicTheme
import com.first.myapplic.uiph.NoteDetail.NoteDetailScreen
import com.first.myapplic.uiph.NotesList.NotesList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            MyApplicTheme {
                NavHost(
                    navController = navController,
                    startDestination = Constants.NAVIGATION_NOTES_LIST
                ) {

                    composable(Constants.NAVIGATION_NOTES_LIST) {
                        NotesList(navController)
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
                            noteId = noteId, onBackClick = { navController.popBackStack() },
                            navController = navController
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
                            navController = navController
                        )
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