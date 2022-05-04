package it.niko.mywatchlistandroid.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.navigation.findNavController
import it.niko.mywatchlistandroid.R
import it.niko.mywatchlistandroid.RetrofitInstance
import it.niko.mywatchlistandroid.SessionManager
import it.niko.mywatchlistandroid.databinding.FragmentEditWatchlistBinding
import it.niko.mywatchlistandroid.payload.MessageResponse
import it.niko.mywatchlistandroid.payload.WatchlistRequest
import it.niko.mywatchlistandroid.services.WatchlistService
import retrofit2.Response
import java.util.*

class EditWatchlistFragment : Fragment() {
    private lateinit var binding: FragmentEditWatchlistBinding
    private lateinit var watchlistService: WatchlistService
    private lateinit var sessionManager: SessionManager
    private var title = ""
    private var progress: Int = 0
    private var selectedScore: Int = 0
    private var selectedStatus = ""
    private var comment = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        watchlistService = RetrofitInstance.getRetrofitInstance().create(WatchlistService::class.java)
        sessionManager = SessionManager(requireContext())

        title = requireArguments().getString("title")!!
        progress = requireArguments().getInt("progress")
        selectedStatus = requireArguments().getString("status")!!
        selectedScore = requireArguments().getInt("score")
        comment = requireArguments().getString("comment")!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditWatchlistBinding.inflate(inflater, container, false)

        binding.apply {
            tvTitleE.text = title
            etProgress.setText(progress.toString())
            etComment.setText(comment)

            val statusList = resources.getStringArray(R.array.status_list)
            spStatus.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusList)
            spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedStatus = statusList[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            when(selectedStatus) {
                "WATCHING" -> spStatus.setSelection(0)
                "COMPLETED" -> spStatus.setSelection(1)
                "DROPPED" -> spStatus.setSelection(2)
                "ON HOLD" -> spStatus.setSelection(3)
                "PLAN TO WATCH" -> spStatus.setSelection(4)
            }

            val scoreList = resources.getStringArray(R.array.score_list)
            spScore.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, scoreList)
            spScore.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedScore = scoreList[position].toInt()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            spScore.setSelection(selectedScore - 1)

            btnSave.setOnClickListener {
                progress = etProgress.text.toString().toInt()
                comment = etComment.text.toString()
                if (!TextUtils.isEmpty(etProgress.text)) {
                    updateUserWatchlist()
                }
                else {
                    Toast.makeText(requireContext(), "invalid input", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }

    private fun updateUserWatchlist() {
        val responseLiveData: LiveData<Response<MessageResponse>> = liveData {
            val response = watchlistService.updateWatchlist(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                WatchlistRequest(
                    title,
                    sessionManager.fetchUsername()!!,
                    selectedStatus,
                    progress,
                    selectedScore,
                    comment
                )
            )
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.body()!!.message, Toast.LENGTH_SHORT).show()
            view?.findNavController()?.navigate(R.id.action_editWatchlistFragment_to_watchlistFragment)
        }
    }


}