package it.niko.mywatchlistandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.niko.mywatchlistandroid.databinding.FragmentWatchlistBinding

class WatchlistFragment : Fragment() {
    private lateinit var binding: FragmentWatchlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

}