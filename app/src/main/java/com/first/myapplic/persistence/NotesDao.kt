package com.first.myapplic.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.*
import com.first.myapplic.model.Note

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes WHERE id=:id")
    suspend fun getNoteById(id: Int) : Note?

    @Query("SELECT * FROM Notes ORDER BY dateUpdated DESC")
    fun getNotes(): LiveData<List<Note>>

    @Delete
    fun deleteNote(note: Note): Int

    @Insert
    suspend fun createNote(note: Note)

    @Update
    fun updateNote(note: Note): Int



    @Insert
    fun insertNote(note: Note)
}
