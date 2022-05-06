package com.psg.gunstagram.util

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.psg.gunstagram.view.base.BaseViewModel
import com.psg.gunstagram.view.login.LoginViewModel

sealed class Event{
    // Base
    data class ShowToast(val text: String) : Event()
    // Login
    data class SignEmail(val user: FirebaseUser?) : Event()
    data class SignGoogle(val intent: Intent, val code: Int) : Event()
    // AddPhoto
    data class SuccessUpload(val success: Boolean): Event()
}
