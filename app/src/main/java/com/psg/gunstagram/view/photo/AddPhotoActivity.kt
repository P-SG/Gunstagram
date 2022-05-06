package com.psg.gunstagram.view.photo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.storage.FirebaseStorage
import com.psg.gunstagram.R
import com.psg.gunstagram.databinding.ActivityAddPhotoBinding
import com.psg.gunstagram.util.Event
import com.psg.gunstagram.view.base.BaseActivity
import com.psg.gunstagram.view.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : BaseActivity<ActivityAddPhotoBinding, AddPhotoViewModel>(
    R.layout.activity_add_photo
) {
    override val TAG: String = AddPhotoActivity::class.java.simpleName
    override val viewModel: AddPhotoViewModel by inject()
    private val PICK_IMAGE_FROM_ALBUM = 0
//    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initView() {
        super.initView()
        requestPhotoUpload()
        binding.vm = viewModel
//        binding.btnAddPhoto.setOnClickListener {
//            uploadSuccess()
//        }

    }

    override fun handleEvent(event: Event) = when (event) {
        is Event.ShowToast ->
            CoroutineScope(Dispatchers.Main).launch {
                makeToast(event.text)
            }
        is Event.SuccessUpload ->
            if (event.success){
                CoroutineScope(Dispatchers.Main).launch{
                    makeToast(getString(R.string.upload_success))
                }
                finish()
            } else {
                makeToast(getString(R.string.upload_fail))
                finish()
            }
        else -> {}
    }

    private fun requestPhotoUpload(){
        val photoPikerIntent = Intent(Intent.ACTION_PICK)
        photoPikerIntent.type = "image/*"
        startActivityForResult(photoPikerIntent,PICK_IMAGE_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM){
            if (resultCode == Activity.RESULT_OK){
                viewModel.photoUri = data?.data
                binding.ivAddPhoto.setImageURI(viewModel.photoUri)
            } else {
                finish()
            }
        }
    }



}