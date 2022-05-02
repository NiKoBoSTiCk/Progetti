package it.niko.mywatchlistandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import it.niko.mywatchlistandroid.R
import it.niko.mywatchlistandroid.SessionManager
import it.niko.mywatchlistandroid.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.apply {
            tvUsername.text = sessionManager.fetchUsername()
            tvToken.text = sessionManager.fetchAuthToken()

            btnUserToWatchlist.setOnClickListener {
                it.findNavController().navigate(R.id.action_userFragment_to_watchlistFragment)
            }

            btnUserToSeries.setOnClickListener {
                it.findNavController().navigate(R.id.action_userFragment_to_seriesFragment)
            }
        }
        return binding.root
    }

}