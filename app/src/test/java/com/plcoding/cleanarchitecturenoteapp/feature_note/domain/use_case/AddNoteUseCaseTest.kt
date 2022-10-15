package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.plcoding.cleanarchitecturenoteapp.feature_note.data.FakeRepositoryImpl
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.invalideNoteException
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddNoteUseCaseTest {
    lateinit var addNoteUseCase: AddNoteUseCase
    lateinit var repository: FakeRepositoryImpl

    @Before
    fun setUp() {
        repository = FakeRepositoryImpl()
        addNoteUseCase = AddNoteUseCase(repository)
    }

    @Test(expected = invalideNoteException::class)
    fun `get exception when blank title inserted`() = runBlocking {
        addNoteUseCase(
            Note(
            title = "",
            color = 1,
            id = 2,
            content = "content",
            timestamp = 1
            )
        )
    }

    @Test(expected = invalideNoteException::class)
    fun `get exception when blank content inserted`() = runBlocking {
        addNoteUseCase(
            Note(
                title = "title",
                color = 1,
                id = 2,
                content = "",
                timestamp = 1
            )
        )
    }

    @Test
    fun `note inserted`() = runBlocking {
        addNoteUseCase(
            Note(
                title = "Note",
                color = 1,
                id = 2,
                content = "the note is inserted",
                timestamp = 1
            )
        )
        val notes = repository.notes
        assertThat(notes[0].title).isNotEmpty() .also { assertThat(notes[0].content).isNotEmpty()  }
    }
}