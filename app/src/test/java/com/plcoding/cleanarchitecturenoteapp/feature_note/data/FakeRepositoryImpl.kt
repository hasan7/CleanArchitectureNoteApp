package com.plcoding.cleanarchitecturenoteapp.feature_note.data

import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepositoryImpl : NoteRepository {

    val notes = mutableListOf<Note>()

    override fun getNotes(): Flow<List<Note>> {
       return flow {
           emit(notes)
       }
    }

    override suspend fun getNoteById(id: Int): Note? {
       return notes.find { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
        notes.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
    }

    fun fillUpNotes() {

        ('a' .. 'z').forEachIndexed {index, c ->
            notes.add(
                Note(
                    title = c.toString(),
                    color = index,
                    id = index,
                    content = c.toString(),
                    timestamp = index.toLong()
                )
            )
        }
    notes.shuffle()
    }
}