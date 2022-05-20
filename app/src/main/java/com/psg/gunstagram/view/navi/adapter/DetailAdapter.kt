package com.psg.gunstagram.view.navi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.psg.gunstagram.R
import com.psg.gunstagram.databinding.ItemDetailBinding
import com.psg.gunstagram.view.navi.model.ContentDTO

class DetailAdapter: RecyclerView.Adapter<DetailAdapter.DetailViewHolder>(){
    var fireStore: FirebaseFirestore? = null
    var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
    var contentUidList: ArrayList<String> = arrayListOf()
    init {
        fireStore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            contentDTOs.clear()
            contentUidList.clear()
            for (snapshot in querySnapshot!!.documents){
                var item = snapshot.toObject(ContentDTO::class.java)
                contentDTOs.add(item!!)
                contentUidList.add(snapshot.id)
            }
            notifyDataSetChanged()

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailAdapter.DetailViewHolder {
        fireStore = FirebaseFirestore.getInstance()
//        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
        return DetailViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_detail,parent,false))
    }


    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(contentDTOs[position])

    }

    override fun getItemCount(): Int {
        return contentDTOs.size
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
            binding.tvDetailFavoriteCounter.text = "Likes " + contentDTOs[pos].favoriteCount

            // Profile Image
            Glide.with(itemView.context)
                .load(contentDTOs[pos].imageUrl)
                .into(binding.ivDetailProfile)

        }
    }

}