package com.first.myapplic.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.first.myapplic.model.Note

@Database(entities = [Note::class], version = 2)
@TypeConverters(
    Converters::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun NotesDao(): NotesDao
}

