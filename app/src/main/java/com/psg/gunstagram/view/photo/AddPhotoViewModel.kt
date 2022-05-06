package com.psg.gunstagram.view.photo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.psg.gunstagram.R
import com.psg.gunstagram.util.Event
import com.psg.gunstagram.view.base.BaseViewModel
import com.psg.gunstagram.view.login.LoginActivity
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoViewModel : BaseViewModel() {
//    val uploadSuccess: LiveData<Boolean> get() = _uploadSuccess
//    private var _uploadSuccess = MutableLiveData<Boolean>(false)

    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var photoUri: Uri? = null


    fun uploadSuccess() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMAGE_" + timeStamp + "_.png"

        val storageRef = storage.reference.child("images")?.child(imageFileName)

        storageRef.putFile(photoUri!!).addOnSuccessListener {
//            makeToast(getString(R.string.upload_success))
            event(Event.SuccessUpload(true))
        }
//        finish()
    }


}