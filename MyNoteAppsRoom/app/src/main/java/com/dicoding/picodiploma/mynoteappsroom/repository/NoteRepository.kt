package com.dicoding.picodiploma.mynoteappsroom.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.picodiploma.mynoteappsroom.database.Note
import com.dicoding.picodiploma.mynoteappsroom.database.NoteDao
import com.dicoding.picodiploma.mynoteappsroom.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/*
    penghubung antara ViewModel dengan database atau resource data
*/
class NoteRepository(application: Application) {
    private val mNotesDao: NoteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = NoteRoomDatabase.getDatabase(application)
        mNotesDao = db.noteDao() /* mendapatkan akses ke tabel note lwt DAO */
    }

    /* tanpa executor krn LiveData yang bersifat asynchronous */
    fun getAllNotes(): LiveData<List<Note>> = mNotesDao.getAllNotes()

    /*
        insert, delete, update harus menggunakan ExecutorService -> jika tdk, maka force close
        karena mereka menggunakan thread yang berbeda yakni background thread.
    */
    fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }
}