package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.invalideNoteException
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.InvalidClassException
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var currentNoteId : Int? = null

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter Title..."
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Type your Note"
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.intialColor.toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1){
                 viewModelScope.launch {
                     noteUseCases.getNote(noteId)?.also { note ->
                         currentNoteId = note.id
                         _noteTitle.value = noteTitle.value.copy(
                             text = note.title,
                             hintVisible = false
                         )
                         _noteContent.value = noteContent.value.copy(
                             text = note.content,
                             hintVisible = false
                         )
                         _noteColor.value = note.color
                     }
                 }
            } else{
                _noteColor.value = Note.noteColors.get(1).toArgb()
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.title
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.content
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    hintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    hintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {

                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color =  noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: invalideNoteException){
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

  sealed class UiEvent{
      data class ShowSnackbar(val message: String) : UiEvent()
      object SaveNote: UiEvent()
  }
}