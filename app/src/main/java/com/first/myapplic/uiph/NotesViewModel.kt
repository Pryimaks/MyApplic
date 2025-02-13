package com.first.myapplic.uiph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.first.myapplic.model.Note
import com.first.myapplic.persistence.NotesDao
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch

class NotesViewModel(
    private val db: NotesDao,
) : ViewModel() {

    val notes: LiveData<List<Note>> = db.getNotes()

    fun deleteNotes(note: Note) {
        viewModelScope.launch(Dispatchers.IO){
            db.deleteNote(note)
        }
    }

    suspend fun getNote(noteId : Int) : Note? {
        return db.getNoteById(noteId)
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO){
            db.updateNote(note)
        }
    }

    fun createNote(title: String, note: String, image: String? = null) {
        viewModelScope.launch(Dispatchers.IO){
            db.insertNote(Note(title = title, note = note, imageUri = image))
        }
    }
}

class NotesViewModelFactory(
    private val db: NotesDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  NotesViewModel(
            db = db,
        ) as T
    }
}
