package com.first.myapplic.uiph.NoteDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.first.myapplic.R
import com.first.myapplic.model.Note
import com.first.myapplic.ui.theme.MyApplicTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.first.myapplic.Constants
import com.first.myapplic.Constants.noteDetailPlaceHolder
import com.first.myapplic.GenericAppBar
import com.first.myapplic.uiph.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteDetailScreen(
    noteId: Int,
    navController: NavController,
    viewModel: NotesViewModel,
    onBackClick: () -> Boolean
) {
    val scope = rememberCoroutineScope()
    val note = remember { mutableStateOf(noteDetailPlaceHolder) }
    val openImageDialog = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            note.value = viewModel.getNote(noteId) ?: noteDetailPlaceHolder
        }
    }

    MyApplicTheme {
        Scaffold(
            topBar = {
                GenericAppBar(
                    title = note.value.title,
                    onIconClick = {
                        navController.navigate(Constants.noteEditNavigation(note.value.id ?: 0))
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.edit_note),
                            contentDescription = stringResource(R.string.edit_note),
                            tint = Color.Black,
                        )
                    },
                    iconState = remember { mutableStateOf(true) }
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
                if (note.value.imageUri != null && note.value.imageUri!!.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(data = Uri.parse(note.value.imageUri))
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight(0.3f)
                            .fillMaxWidth()
                            .padding(6.dp)
                            .clickable { openImageDialog.value = true },
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                NoteCard(note = note)
            }
        }
    }

    if (openImageDialog.value) {
        Dialog(onDismissRequest = { openImageDialog.value = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(Uri.parse(note.value.imageUri))
                            .build()
                    ),
                    contentDescription = "Full Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable { openImageDialog.value = false },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}





@Composable
fun NoteCard(note: MutableState<Note>) {
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
                text = note.value.title,
                modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 24.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = note.value.dateUpdated, Modifier.padding(12.dp), color = Color.Gray)
            Text(text = note.value.note, Modifier.padding(12.dp))
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
