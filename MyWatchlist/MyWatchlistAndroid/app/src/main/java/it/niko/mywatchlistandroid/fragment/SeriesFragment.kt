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
import it.niko.mywatchlistandroid.databinding.FragmentSeriesBinding
import it.niko.mywatchlistandroid.model.Series
import it.niko.mywatchlistandroid.model.SeriesAdapter
import it.niko.mywatchlistandroid.payload.MessageResponse
import it.niko.mywatchlistandroid.payload.SeriesResponse
import it.niko.mywatchlistandroid.payload.WatchlistRequest
import it.niko.mywatchlistandroid.services.SeriesService
import it.niko.mywatchlistandroid.services.WatchlistService
import retrofit2.Response

class SeriesFragment : Fragment() {
    private lateinit var binding: FragmentSeriesBinding
    private lateinit var seriesService: SeriesService
    private lateinit var watchlistService: WatchlistService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seriesService = RetrofitInstance.getRetrofitInstance().create(SeriesService::class.java)
        watchlistService = RetrofitInstance.getRetrofitInstance().create(WatchlistService::class.java)
        sessionManager = SessionManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSeriesBinding.inflate(inflater, container, false)
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            btnProfile.setOnClickListener {
                it.findNavController().navigate(R.id.action_seriesFragment_to_userFragment)
            }
        }
        getAllSeries()
        return binding.root
    }

    private fun getAllSeries() {
        val responseLiveData: LiveData<Response<SeriesResponse>> = liveData {
            val response = seriesService.getSeries()
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            val body = it.body()
            if (body != null) {
                binding.apply {
                    recyclerView.adapter = SeriesAdapter(body.seriesList) {
                            series: Series -> addSeriesToWatchlist(series)
                    }
                }
            }
        }
    }

    private fun addSeriesToWatchlist(series: Series) {
        val responseLiveData: LiveData<Response<MessageResponse>> = liveData {
            val username = sessionManager.fetchUsername()!!
            val response = watchlistService.addWatchlist(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                WatchlistRequest(
                    series.title,
                    username,
                    SessionManager.WATCHING,
                    0,
                    0,
                    ""
                )
            )
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.body()!!.message, Toast.LENGTH_SHORT).show()
        }
    }

}