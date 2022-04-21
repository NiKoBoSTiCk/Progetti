package it.niko.mywatchlistandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import it.niko.mywatchlistandroid.databinding.FragmentSeriesBinding
import it.niko.mywatchlistandroid.model.Series
import it.niko.mywatchlistandroid.model.SeriesAdapter
import it.niko.mywatchlistandroid.model.SeriesList
import it.niko.mywatchlistandroid.model.SeriesResponse
import retrofit2.Response

class SeriesFragment : Fragment() {
    private lateinit var binding: FragmentSeriesBinding
    private lateinit var seriesService: SeriesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seriesService = RetrofitInstance.getRetrofitInstance().create(SeriesService::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSeriesBinding.inflate(inflater, container, false)
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
                    recyclerView.adapter = SeriesAdapter(body.seriesList)
                }
            }
        }
    }

}