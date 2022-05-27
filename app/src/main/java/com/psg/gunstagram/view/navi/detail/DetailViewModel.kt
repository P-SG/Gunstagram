package com.psg.gunstagram.view.navi.detail

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

class DetailViewModel: BaseViewModel() {
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val contentDTO: LiveData<List<ContentDTO>> get() = _contentDTO
    private val _contentDTO = MutableLiveData<List<ContentDTO>>()

    val uid: LiveData<String> get() = _uid
    private val _uid = MutableLiveData<String>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(true)

    val isRefresh: LiveData<Boolean> get() = _isRefresh
    private val _isRefresh = MutableLiveData(true)


     fun getContent() {
        val contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        val contentUidList: ArrayList<String> = arrayListOf()

        fireStore.collection("images")
            .orderBy("timeStamp").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                for (snapshot in querySnapshot!!.documents){
                    val item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                viewModelScope.launch {
                    withContext(Dispatchers.Main){
                        _uid.value = FirebaseAuth.getInstance().currentUser?.uid
                        _contentDTO.value = contentDTOs
                        _isLoading.value = false
                        _isRefresh.value = false
                    }
                }
            }
    }

    fun favoriteEvent(position: Int) {
        val contentUidList: ArrayList<String> = arrayListOf()
        val tsDoc = fireStore.collection("images").document(contentUidList[position])
        fireStore.runTransaction{ transaction ->
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

            if (contentDTO!!.favorites.containsKey(uid)){
                contentDTO.favoriteCount = contentDTO.favoriteCount -1
                contentDTO.favorites.remove(uid)
            } else {
                contentDTO.favoriteCount = contentDTO.favoriteCount + 1
                contentDTO.favorites[uid!!] = true
            }
            transaction.set(tsDoc,contentDTO)
        }
    }
}