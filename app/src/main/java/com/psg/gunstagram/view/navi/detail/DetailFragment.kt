package com.psg.gunstagram.view.navi.detail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.psg.gunstagram.R
import com.psg.gunstagram.data.model.ContentDTO
import com.psg.gunstagram.databinding.FragmentDetailBinding
import org.koin.android.ext.android.inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by inject()
    private val detailAdapter = DetailAdapter()
    private var firstFlag = true
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreateView")
        viewModel.getContent()
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (!it) {
                initView()
                progressOff()
            } else {
                progressOn()
            }
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        // Inflate the layout for this fragment

        binding.rvDetail.adapter = detailAdapter
        val manager = LinearLayoutManager(activity)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        binding.rvDetail.layoutManager = manager
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (!firstFlag){
            binding.rvDetail.smoothScrollToPosition(detailAdapter.itemCount)
        }
        firstFlag = false
    }


    private fun initView() {

        viewModel.contentDTO.observe(viewLifecycleOwner) {
            if (it != null) {
                detailAdapter.setData(it)
            }
        }
        viewModel.uid.observe(viewLifecycleOwner){
            if (it != null) {
                detailAdapter.setUid(it)
            }
        }
        detailAdapter.setOnItemClickListener(object : DetailAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: ContentDTO, pos: Int) {
                when(v.id){
                    R.id.iv_detail_favorite -> { // 좋아요 버튼 클릭 이벤트
                        viewModel.favoriteEvent(pos)
                    }
                }
            }
        })
        binding.srlDetail.setOnRefreshListener {
            refresh()
        }
    }

    private fun progressOn() {
        dialog = Dialog(requireContext()).apply {
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.dialog_loading)
            show()
        }


    }

    private fun progressOff(){
        dialog.dismiss()
    }

    private fun refresh(){
        viewModel.getContent()
        viewModel.isRefresh.observe(viewLifecycleOwner) {
            binding.srlDetail.isRefreshing = it
        }
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}