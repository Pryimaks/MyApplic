package com.first.myapplic.uiph.NoteDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.first.myapplic.R
import com.first.myapplic.model.Note
import com.first.myapplic.test.TestConstants
import com.first.myapplic.ui.theme.MyApplicTheme
import com.first.myapplic.uiph.NotesList.NotesFab
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.first.myapplic.Constants
import com.first.myapplic.GenericAppBar
import com.first.myapplic.ui.theme.MyApplicTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteDetailScreen(noteId: Int,
                     onBackClick: () -> Unit,
                     navController: NavController) {
    val note = TestConstants.notes.find { note -> note.id == noteId }
        ?: Note(note = "Cannot find note details", id = 0, title = "Cannot find note details")

    MyApplicTheme {
        Scaffold(
            topBar = {
                GenericAppBar(
                    title = note.title,
                    onIconClick = {
                        navController.navigate(Constants.noteEditNavigation(note.id))
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.edit_note),
                            contentDescription = stringResource(R.string.edit_note),
                            tint = Color.White,
                        )
                    },
                    iconState = remember{mutableStateOf(true)}
                )
            },
            floatingActionButton = {
                NotesFab(
                    contentDescription = stringResource(R.string.create_note),
                    action = {},
                    icon = R.drawable.camera
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                NoteCard(note = note)
            }
        }
    }
}


@Composable
fun NoteCard(note: Note) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Оновлено: ${note.dateUpdated}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.note,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailAppBar(title: String, onBackClick: () -> Unit, navController: NavController, noteId: Int) {
    TopAppBar(
        title = { Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Constants.noteEditNavigation(noteId)) }) {
                Icon(Icons.Default.Edit, contentDescription = "Редагувати")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

 */
