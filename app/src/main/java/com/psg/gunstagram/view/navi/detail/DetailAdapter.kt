package com.psg.gunstagram.view.navi.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.psg.gunstagram.R
import com.psg.gunstagram.databinding.ItemDetailBinding
import com.psg.gunstagram.data.model.ContentDTO

class DetailAdapter: RecyclerView.Adapter<DetailAdapter.DetailViewHolder>(){
    var fireStore: FirebaseFirestore? = null
    var contentDTOs: List<ContentDTO> = arrayListOf()
    var contentUidList: List<String> = arrayListOf()
    var sUid: String? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder = DetailViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_detail,parent,false))

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(contentDTOs[position])

    }

    fun setData(items: List<ContentDTO>){
        contentDTOs = items
        notifyDataSetChanged()
    }

    fun setUid(uid: String){
        sUid = uid
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return contentDTOs.size
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: ContentDTO, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class DetailViewHolder(private val binding: ItemDetailBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ContentDTO){
            binding.item = item
            val pos = bindingAdapterPosition

            //UserId
            binding.tvDetailProfile.text = contentDTOs[pos].userId

            // Image
            Glide.with(itemView.context)
                .load(contentDTOs[pos].imageUrl)
                .into(binding.ivDetailContent)

            // Explain of content
            binding.tvDetailExplain.text = contentDTOs[pos].explain

            // Likes
            binding.tvDetailFavoriteCounter.text = "좋아요 " + contentDTOs[pos].favoriteCount

            // Profile Image
            Glide.with(itemView.context)
                .load(contentDTOs[pos].imageUrl)
                .into(binding.ivDetailProfile)


            if (contentDTOs[pos].favorites.containsKey(sUid)) {
                binding.ivDetailFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.ivDetailFavorite.setImageResource(R.drawable.ic_favorite_border)
            }

        }
    }

}