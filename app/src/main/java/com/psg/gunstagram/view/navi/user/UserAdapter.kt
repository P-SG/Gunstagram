package com.psg.gunstagram.view.navi.user

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.psg.gunstagram.R
import com.psg.gunstagram.data.model.ContentDTO
import com.psg.gunstagram.databinding.ItemDetailBinding

class UserAdapter(private val width: Int): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var contentDTOs: List<ContentDTO> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)
        return UserViewHolder(imageView, DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_user,parent,false))
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return contentDTOs.size
    }

    fun setData(items: List<ContentDTO>){
        contentDTOs = items
        notifyDataSetChanged()
    }


    inner class UserViewHolder(
        private val imageView: ImageView,
        private val binding: ItemDetailBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(){
                val pos = bindingAdapterPosition

                Glide.with(binding.root.context)
                    .load(contentDTOs[pos].imageUrl)
                    .apply(RequestOptions().centerCrop())
                    .into(imageView)
            }
    }


}