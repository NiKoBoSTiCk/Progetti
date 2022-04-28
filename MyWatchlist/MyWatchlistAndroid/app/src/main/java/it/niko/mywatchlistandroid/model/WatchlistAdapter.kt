package it.niko.mywatchlistandroid.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.niko.mywatchlistandroid.databinding.WatchListBinding
import java.util.*
import kotlin.collections.ArrayList

class WatchlistAdapter(private val watchlists: ArrayList<Watchlist>,
                       private val updateListener: (Watchlist) -> Unit,
                       private val deleteListener: (Watchlist) -> Unit
                       ): RecyclerView.Adapter<WatchlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val binding = WatchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        holder.bind(watchlists[position], updateListener, deleteListener)
    }

    override fun getItemCount(): Int = watchlists.size
}

class WatchlistViewHolder(private val binding: WatchListBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(watchlist: Watchlist, updateListener: (Watchlist) -> Unit, deleteListener: (Watchlist) -> Unit) {
        binding.apply {
            tvTitleW.text = watchlist.series.title
            ("Score: " + watchlist.score).also { tvScore.text = it }
            ("Progress: " + watchlist.progress).also { tvProgressW.text = it }
            ("Status: " + watchlist.status.type).also { tvStatus.text = it.lowercase(Locale.getDefault()) }
            ("Comment: " + watchlist.comment).also { tvComment.text = it }

            btnEdit.setOnClickListener {
                updateListener(watchlist)
            }

            btnDelete.setOnClickListener {
                deleteListener(watchlist)
            }
        }
    }
}