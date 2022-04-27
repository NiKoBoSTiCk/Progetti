package it.niko.mywatchlistandroid

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import it.niko.mywatchlistandroid.databinding.FragmentEditWatchlistBinding
import it.niko.mywatchlistandroid.model.Watchlist

class EditWatchlistFragment : Fragment() {
    private lateinit var binding: FragmentEditWatchlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditWatchlistBinding.inflate(inflater, container, false)
        val title = requireArguments().getString("title")
        val progress = requireArguments().getInt("old_progress")
        val comment = requireArguments().getString("old_comment")
        binding.apply {
            tvTitleE.text = title
            etProgress.setText(progress.toString())
            etComment.setText(comment)
            var selectedStatus = ""
            var selectedScore = 0

            val statusList = resources.getStringArray(R.array.status_list)
            spStatus.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusList)
            spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedStatus = statusList[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            val scoreList = resources.getStringArray(R.array.score_list)
            spScore.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, scoreList)
            spScore.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedScore = scoreList[position].toInt()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            btnSave.setOnClickListener {
                if (!TextUtils.isEmpty(etProgress.text)) {
                    setFragmentResult("edit_key", bundleOf(
                        "status" to selectedStatus,
                        "score" to selectedScore,
                        "progress" to etProgress.text.toString().toInt(),
                        "comment" to etComment.text.toString()
                    ))
                    view?.findNavController()?.navigate(R.id.action_editWatchlistFragment_to_watchlistFragment)
                }
            }
        }
        return binding.root
    }


}