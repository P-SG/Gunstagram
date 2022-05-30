package com.psg.gunstagram.view.photo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.psg.gunstagram.util.Event
import com.psg.gunstagram.view.base.BaseViewModel
import com.psg.gunstagram.data.model.ContentDTO
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoViewModel : BaseViewModel() {
//    val uploadSuccess: LiveData<Boolean> get() = _uploadSuccess
//    private var _uploadSuccess = MutableLiveData<Boolean>(false)

    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var photoUri: Uri? = null
    var auth = FirebaseAuth.getInstance()
    var fireStore = FirebaseFirestore.getInstance()

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData<Boolean>(false)


    fun uploadSuccess(explain: String) {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMAGE_" + timeStamp + "_.png"

        val storageRef = storage.reference.child("images").child(imageFileName)

        _isLoading.value = true
        //Promise method
        storageRef
            .putFile(photoUri!!)
            .continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }.addOnSuccessListener { task2 ->
            val contentDTO = ContentDTO(
                photoUri.toString(),
                auth.currentUser?.uid,
                auth.currentUser?.email,
                explain,
                System.currentTimeMillis()
            )

            fireStore.collection("images")
                .document()
                .set(contentDTO) // fireStore에 content정보 저장

            _isLoading.value = false
            event(Event.SuccessUpload(true))
        }

        //CallBack method
//        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
//            val contentDTO = ContentDTO()
//
//            contentDTO.imageUrl = photoUri.toString()
//            contentDTO.uid = auth.currentUser?.uid
//            contentDTO.userId = auth.currentUser?.email
//            contentDTO.explain = explain
//            contentDTO.timeStamp = System.currentTimeMillis()
//
//            fireStore?.collection("images")?.document()?.set(contentDTO)
//
//            event(Event.SuccessUpload(true))
//        }
//        finish()
    }


}