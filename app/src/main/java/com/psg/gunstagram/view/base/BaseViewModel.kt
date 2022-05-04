package com.psg.gunstagram.view.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel:ViewModel() {
    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    open fun toastEvent(text: String){
        event(Event.ShowToast(text))
    }

    open fun signEmailEvent(user: FirebaseUser?) {
        event(Event.SignEmail(user))
    }

    open fun signGoogleEvent(intent: Intent, code: Int) {
        event(Event.SignGoogle(intent, code))
    }


    private fun event(event:Event) {
        CoroutineScope(Dispatchers.IO).launch{
            _eventFlow.emit(event)
        }
    }

    sealed class Event {
        data class ShowToast(val text: String) : Event()
        data class SignEmail(val user: FirebaseUser?) : Event()
        data class SignGoogle(val intent: Intent, val code: Int) : Event()
    }
}