package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case.NoteUseCases
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val NoteUseCases: NoteUseCases
): ViewModel() {

    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> = _state

    private var DeletedNote: Note? = null

    private var getNoteJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent){

        when(event){

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    NoteUseCases.deleteNote(event.note)
                    DeletedNote = event.note
                }
            }


            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    NoteUseCases.addNote(DeletedNote ?: return@launch)
                    DeletedNote = null
                }
            }
            is NotesEvent.Order -> {

                if(state.value.noteOrder::class == event.noteOrder::class &&
                        state.value.noteOrder.orderType == event.noteOrder.orderType
                ){
                    return
                }

                getNotes(event.noteOrder)

            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder){
        getNoteJob?.cancel()
        getNoteJob = NoteUseCases.getNotes(noteOrder)
            .onEach {
                notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }.launchIn(viewModelScope)
    }

}