package it.niko.mywatchlistandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.niko.mywatchlistandroid.databinding.FragmentSeriesBinding

class SeriesFragment : Fragment() {
    private lateinit var binding: FragmentSeriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

}