package it.niko.mywatchlistandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import it.niko.mywatchlistandroid.R
import it.niko.mywatchlistandroid.RetrofitInstance
import it.niko.mywatchlistandroid.SessionManager
import it.niko.mywatchlistandroid.databinding.FragmentWatchlistBinding
import it.niko.mywatchlistandroid.model.Watchlist
import it.niko.mywatchlistandroid.model.WatchlistAdapter
import it.niko.mywatchlistandroid.payload.MessageResponse
import it.niko.mywatchlistandroid.payload.WatchlistRequest
import it.niko.mywatchlistandroid.services.WatchlistService
import retrofit2.Response

class WatchlistFragment : Fragment() {
    private lateinit var binding: FragmentWatchlistBinding
    private lateinit var watchlistService: WatchlistService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        watchlistService = RetrofitInstance.getRetrofitInstance().create(WatchlistService::class.java)
        sessionManager = SessionManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        binding.apply {

            recyclerViewW.layoutManager = LinearLayoutManager(requireContext())

            btnProfileW.setOnClickListener {
                it.findNavController().navigate(R.id.action_watchlistFragment_to_userFragment)
            }

            btnSeriesW.setOnClickListener {
                it.findNavController().navigate(R.id.action_watchlistFragment_to_seriesFragment)
            }
        }
        getUserWatchlist()
        return binding.root
    }

    private fun getUserWatchlist() {
        val responseLiveData: LiveData<Response<ArrayList<Watchlist>>> = liveData {
            val response = watchlistService.getWatchlist(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                sessionManager.fetchUsername()!!
            )
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                recyclerViewW.adapter = WatchlistAdapter(
                    it.body()!!,
                    { watchlist: Watchlist -> updateUserWatchlist(watchlist) },
                    { watchlist: Watchlist -> deleteUserWatchlist(watchlist) })
                }
            }
    }

    private fun updateUserWatchlist(watchlist: Watchlist) {

    }

    private fun deleteUserWatchlist(watchlist: Watchlist) {
        val watchlistRequest = WatchlistRequest(
            watchlist.series.title,
            sessionManager.fetchUsername()!!,
            watchlist.status.type,
            watchlist.progress,
            watchlist.score,
            watchlist.comment
        )
        val responseLiveData: LiveData<Response<MessageResponse>> = liveData {
            val response = watchlistService.deleteWatchlist(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                watchlistRequest
            )
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.body()!!.message, Toast.LENGTH_SHORT).show()
            getUserWatchlist()
        }
    }
}

