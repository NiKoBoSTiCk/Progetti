package it.niko.mywatchlistandroid.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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

            btnAll.setOnClickListener {
                getAllSeries()
            }

            btnTopViews.setOnClickListener {
                getTopViewsSeries()
            }

            btnTopRated.setOnClickListener {
                getTopRatedSeries()
            }

            btnSearchByTitle.setOnClickListener {
                if (!TextUtils.isEmpty(etSearchByTitle.text)) {
                    val seriesTitle = etSearchByTitle.text.toString()
                    getSeriesByTitle(seriesTitle)
                } else {
                    Toast.makeText(requireContext(), "no title", Toast.LENGTH_SHORT).show()
                }
            }

            btnProfile.setOnClickListener {
                it.findNavController().navigate(R.id.action_seriesFragment_to_userFragment)
            }
        }
        return binding.root
    }

    private fun getAllSeries() {
        val responseLiveData: LiveData<Response<SeriesResponse>> = liveData {
            val response = seriesService.getSeries()
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                recyclerView.adapter = SeriesAdapter(it.body()!!.seriesList) {
                        series: Series -> addSeriesToWatchlist(series)
                }
            }
        }
    }

    private fun getTopViewsSeries() {
        val responseLiveData: LiveData<Response<ArrayList<Series>>> = liveData {
            val response = seriesService.getSeriesByViews()
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                recyclerView.adapter = SeriesAdapter(it.body()!!) {
                        series: Series -> addSeriesToWatchlist(series)
                }
            }
        }
    }

    private fun getTopRatedSeries() {
        val responseLiveData: LiveData<Response<ArrayList<Series>>> = liveData {
            val response = seriesService.getSeriesByRating()
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                if (it.body() != null) {
                    recyclerView.adapter = SeriesAdapter(it.body()!!) { series: Series ->
                        addSeriesToWatchlist(series)
                    }
                }
                else {
                    Toast.makeText(requireContext(), "no result", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getSeriesByTitle(title: String) {
        val responseLiveData: LiveData<Response<SeriesResponse>> = liveData {
            val response = seriesService.getSeriesByTitle(title)
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                recyclerView.adapter = SeriesAdapter(it.body()!!.seriesList) {
                        series: Series -> addSeriesToWatchlist(series)
                }
            }
        }
    }

    private fun addSeriesToWatchlist(series: Series) {
        val responseLiveData: LiveData<Response<MessageResponse>> = liveData {
            val response = watchlistService.addWatchlist(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                WatchlistRequest(
                    series.title,
                    sessionManager.fetchUsername()!!,
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