package com.first.myapplic.uiph.NotesList

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.first.myapplic.GenericAppBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesList(navController: NavController) {
    val openDialog = remember { mutableStateOf(false) }
    val deleteText = remember { mutableStateOf("") }
    val notes = TestConstants.notes
    MyApplicTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = stringResource(R.string.photo_notes),
                        onIconClick = {
                            openDialog.value = true
                            deleteText.value = "Are you sure you want to delete all notes? "
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.note_delete),
                                contentDescription = stringResource(id = R.string.delete_note),
                                tint = MaterialTheme.colorScheme.onPrimary
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
            ) { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues)) {
                    notes.forEach { note ->
                        NoteListItem(
                            note = note,
                            openDialog = openDialog,
                            text = deleteText,
                            navController = navController
                        )
                    }
                    DeleteDialog(openDialog = openDialog, text = deleteText) {}
                }
            }
        }
    }
}

@Composable
fun NoteListItem(
    note: Note,
    openDialog: MutableState<Boolean>,
    text: MutableState<String>,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Тема для темного/світлого режиму
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Constants.noteDetailNavigation(note.id)) }
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary // Тема
            )
            Text(
                text = note.note,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface // Текст буде видно в темному режимі
            )
            Text(
                text = note.dateUpdated,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NotesFab(contentDescription: String, icon: Int, action: () -> Unit) {
    FloatingActionButton(
        onClick = { action.invoke() },
        containerColor = MaterialTheme.colorScheme.primaryContainer // Адаптація для темної теми
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimaryContainer // Контрастний колір іконки
        )
    }
}

@Composable
fun DeleteDialog(
    openDialog: MutableState<Boolean>,
    text: MutableState<String>,
    action: () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Delete Note", color = MaterialTheme.colorScheme.primary) },
            text = { Text(text.value, color = MaterialTheme.colorScheme.onSurface) },
            confirmButton = {
                Button(
                    onClick = {
                        action.invoke()
                        openDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Yes", color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                Button(
                    onClick = { openDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("No", color = MaterialTheme.colorScheme.onSecondary)
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
                    IconButton(onClick = { /* Додайте навігацію назад */ }) {
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
