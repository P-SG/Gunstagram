package com.psg.gunstagram.view.navi.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psg.gunstagram.R
import com.psg.gunstagram.data.model.ContentDTO
import com.psg.gunstagram.databinding.FragmentUserBinding
import com.psg.gunstagram.view.navi.detail.DetailAdapter
import org.koin.android.ext.android.inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    var fragmentView : View? = null
    private lateinit var binding: FragmentUserBinding
    private val viewModel: UserViewModel by inject()
    private lateinit var userAdapter: UserAdapter

    var uid: String? = null
    var auth: FirebaseAuth? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        initView()
        val uid: String? = arguments?.getString("destinationUid")
        if (uid != null) {
            viewModel.getUserInfo(uid)
        }

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user, container, false)
        val width = resources.displayMetrics.widthPixels / 3
        userAdapter = UserAdapter(width)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initView(){
        initRecycler()
        viewModel.contentDTO.observe(viewLifecycleOwner){
            if (it != null){
                userAdapter.setData(it)
            }
        }
    }

    private fun initRecycler(){
        binding.rvAccount.adapter = userAdapter
        val manager = GridLayoutManager(requireActivity(), 3)
        binding.rvAccount.layoutManager = manager

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}