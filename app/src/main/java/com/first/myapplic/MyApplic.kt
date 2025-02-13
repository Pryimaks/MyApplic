package com.first.myapplic

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.first.myapplic.persistence.NotesDatabase
import com.first.myapplic.persistence.NotesDao

class MyApplic : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = getDb()
    }

    private fun getDb(): NotesDatabase {
        return Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java, Constants.DATABASE_TITLE
        ).fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        private var instance: MyApplic? = null
        private var db: NotesDatabase? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }


        fun getDao(): NotesDao? {
            return db?.NotesDao()
        }
    }
}
