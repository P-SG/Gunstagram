package com.psg.gunstagram.view.navi.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psg.gunstagram.data.model.ContentDTO
import com.psg.gunstagram.view.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel: BaseViewModel() {
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()

    val contentDTO: LiveData<List<ContentDTO>> get() = _contentDTO
    private val _contentDTO = MutableLiveData<List<ContentDTO>>()

    private val contentDTOs: ArrayList<ContentDTO> = arrayListOf()

    fun getUserInfo(uid: String){
        fireStore.collection("images").whereEqualTo("uid",uid).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (querySnapshot == null) return@addSnapshotListener

            for (snapshot in querySnapshot.documents){
                contentDTOs.add(snapshot.toObject(ContentDTO::class.java)!!)
            }

            viewModelScope.launch {
                withContext(Dispatchers.Main){
                    _contentDTO.value = contentDTOs
                }
            }
        }
    }
}