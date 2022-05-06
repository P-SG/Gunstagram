package com.psg.gunstagram.view.login

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.psg.gunstagram.util.AppLogger
import com.psg.gunstagram.util.Event
import com.psg.gunstagram.view.base.BaseViewModel
import com.psg.gunstagram.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LoginViewModel : BaseViewModel() {
    var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null
    var GOOGLE_LOGIN_CODE = 9001
    var callbackManager: CallbackManager? = null

    init {
        auth = FirebaseAuth.getInstance()

    }

    /**
     * 회원가입 및 로그인 함수
     */
    fun signInAndSignUp(email: String, password: String) {
        if (password.length < 5) {
            toastEvent("비밀번호는 6자리 이상")
            return
        }
        auth?.createUserWithEmailAndPassword(
            email, password
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) moveMainPage(task.result!!.user) else signInEmail(
                email,
                password
            )
        }
    }

    private fun signInEmail(email: String, password: String) {
        auth?.signInWithEmailAndPassword(
            email, password
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                moveMainPage(task.result!!.user)
            } else {
                // 에러 처리
                when (task.exception?.message) {
                    "The email address is badly formatted." -> toastEvent("이메일 포맷에 맞춰주세요.")
                }
                task.exception?.message?.let { AppLogger.p("에러는? $it") }
            }
        }
    }

    fun signInGoogle(activity: AppCompatActivity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1090065002249-mmn0cduhnsqkvdt2mpmuorqdldpu4kht.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)

        googleSignInClient?.signInIntent?.let { signGoogleEvent(it, GOOGLE_LOGIN_CODE) }
    }

    fun signInFacebook(activity: AppCompatActivity) {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    firebaseAuthWithFacebook(result?.accessToken)
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                }

            })
    }

    fun firebaseAuthWithFacebook(token: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(task.result!!.user)
                } else {
                    // 에러 처리
                    when (task.exception?.message) {
                        "The email address is badly formatted." -> toastEvent("이메일 포맷에 맞춰주세요.")
                    }
                    task.exception?.message?.let { AppLogger.p("에러는? $it") }
                }
            }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(task.result!!.user)
                } else {
                    // 에러 처리
                    when (task.exception?.message) {
                        "The email address is badly formatted." -> toastEvent("이메일 포맷에 맞춰주세요.")
                    }
                    task.exception?.message?.let { AppLogger.p("에러는? $it") }
                }
            }
    }


    private fun moveMainPage(user: FirebaseUser?) {
        signEmailEvent(user)
    }


     fun signEmailEvent(user: FirebaseUser?) {
        event(Event.SignEmail(user))
    }

     fun signGoogleEvent(intent: Intent, code: Int) {
        event(Event.SignGoogle(intent, code))
    }

//    sealed class Event {
//        data class SignEmail(val user: FirebaseUser?) : Event()
//        data class SignGoogle(val intent: Intent, val code: Int) : Event()
//    }


}