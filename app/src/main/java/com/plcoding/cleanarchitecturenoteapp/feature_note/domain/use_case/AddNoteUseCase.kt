package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.invalideNoteException
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(
    private val  repository: NoteRepository
) {

    @Throws(invalideNoteException::class)
    suspend operator fun invoke(note: Note){

        if (note.title.isBlank()){
            throw invalideNoteException("Title can't be empty!")
        }
        if(note.content.isBlank()){
            throw invalideNoteException("content can't be empty")
        }
        repository.insertNote(note)

    }
}