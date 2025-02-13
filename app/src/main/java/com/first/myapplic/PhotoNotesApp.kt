package com.first.myapplic

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.first.myapplic.persistence.NotesDao
import com.first.myapplic.persistence.NotesDatabase
import android.content.Intent
import android.net.Uri


class PhotoNotesApp : Application() {

    private var db: NotesDatabase? = null

    init {
        instance = this

    }

    private fun getDb(): NotesDatabase {
        if (db != null) {
            return db!!
        } else {
            db = Room.databaseBuilder(
                instance!!.applicationContext,
                NotesDatabase::class.java, Constants.DATABASE_NAME
            ).fallbackToDestructiveMigration()// remove in prod
                .build()
            return db!!
        }
    }


    companion object {
        private var instance: PhotoNotesApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

            fun getDao(): NotesDao {
                return instance!!.getDb().NotesDao()
            }

        fun getUriPermission(uri: Uri){
            instance!!.applicationContext.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        }
    }
