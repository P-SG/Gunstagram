package com.psg.gunstagram.view.photo

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.psg.gunstagram.R
import com.psg.gunstagram.databinding.ActivityAddPhotoBinding
import com.psg.gunstagram.util.Event
import com.psg.gunstagram.view.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AddPhotoActivity : BaseActivity<ActivityAddPhotoBinding, AddPhotoViewModel>(
    R.layout.activity_add_photo
) {
    override val TAG: String = AddPhotoActivity::class.java.simpleName
    override val viewModel: AddPhotoViewModel by inject()
    private val PICK_IMAGE_FROM_ALBUM = 0
    private lateinit var dialog: Dialog
//    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initView() {
        super.initView()
        requestPhotoUpload()
        binding.vm = viewModel

        initLoading()

        viewModel.isLoading.observe(this) {
            if (it) progressOn() else progressOff()
        }
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
                    setResult(Activity.RESULT_OK)
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


    private fun initLoading() {
        dialog = Dialog(this).apply {
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.dialog_loading)

        }
    }

    private fun progressOn() {
        dialog.show()
    }

    private fun progressOff(){
        dialog.dismiss()
    }



}