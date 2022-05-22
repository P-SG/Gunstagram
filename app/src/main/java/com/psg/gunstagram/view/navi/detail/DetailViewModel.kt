package com.psg.gunstagram.view.navi.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.psg.gunstagram.data.model.ContentDTO
import com.psg.gunstagram.view.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel: BaseViewModel() {
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val contentDTO: LiveData<List<ContentDTO>> get() = _contentDTO
    private val _contentDTO = MutableLiveData<List<ContentDTO>>()

    init {
        getContent()
    }

    private fun getContent() {
        val contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        val contentUidList: ArrayList<String> = arrayListOf()

        fireStore.collection("images")
            .orderBy("timestamp").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                for (snapshot in querySnapshot!!.documents){
                    val item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                viewModelScope.launch {
                    withContext(Dispatchers.Main){
                        _contentDTO.value = contentDTOs

                    }
                }
            }
    }
}