package com.first.myapplic

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericAppBar(
    title: String,
    onIconClick: (() -> Unit)?,
    icon: @Composable (() -> Unit)?,
    iconState: MutableState<Boolean>
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            if (iconState.value) {
                IconButton(onClick = { onIconClick?.invoke() }) {
                    icon?.invoke()
                }
            }
        }
    )
}
