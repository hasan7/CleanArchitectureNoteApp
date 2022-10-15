package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.plcoding.cleanarchitecturenoteapp.feature_note.data.FakeRepositoryImpl
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


import org.junit.Before
import org.junit.Test

class GetNotesUseCaseTest {

    lateinit var GetNotesUseCase: GetNotesUseCase
    lateinit var repository: FakeRepositoryImpl

    @Before
    fun setUp() {
        repository = FakeRepositoryImpl()
        GetNotesUseCase = GetNotesUseCase(repository)
        repository.fillUpNotes()

    }

    @Test
    fun `Order notes by title ascending `() = runBlocking {

        val notes = GetNotesUseCase(NoteOrder.Title(OrderType.Ascending)).first()

        assertThat(notes[0].title).isLessThan( notes[1].title)
    }

    @Test
    fun `Order notes by title Descending `() = runBlocking {

        val notes = GetNotesUseCase(NoteOrder.Title(OrderType.Descending)).first()

        assertThat(notes[0].title).isGreaterThan( notes[1].title)
    }

    @Test
    fun `Order notes by date ascending `() = runBlocking {

        val notes = GetNotesUseCase(NoteOrder.Date(OrderType.Ascending)).first()

        assertThat(notes[0].timestamp).isLessThan( notes[1].timestamp)
    }

    @Test
    fun `Order notes by date decending `() = runBlocking {

        val notes = GetNotesUseCase(NoteOrder.Date(OrderType.Descending)).first()

        assertThat(notes[0].timestamp).isGreaterThan( notes[1].timestamp)
    }

    @Test
    fun `Order notes by color ascending `() = runBlocking {

        val notes = GetNotesUseCase(NoteOrder.Color(OrderType.Ascending)).first()

        assertThat(notes[0].color).isLessThan( notes[1].color)
    }

    @Test
    fun `Order notes by color descending `() = runBlocking {

        val notes = GetNotesUseCase(NoteOrder.Color(OrderType.Descending)).first()

        assertThat(notes[0].color).isGreaterThan( notes[1].color)
    }

}