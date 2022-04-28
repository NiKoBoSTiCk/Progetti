package it.niko.mywatchlistandroid.model

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.niko.mywatchlistandroid.databinding.SeriesListBinding
import java.util.*
import kotlin.collections.ArrayList

class SeriesAdapter(private val seriesList: ArrayList<Series>,
                    private val addListener: (Series) -> Unit
                    ): RecyclerView.Adapter<SeriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val binding = SeriesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bind(seriesList[position], addListener)
    }

    override fun getItemCount(): Int = seriesList.size
}

class SeriesViewHolder(private val binding: SeriesListBinding): RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(series: Series, addListener: (Series) -> Unit) {
        binding.apply {
            tvTitle.text = series.title
            tvPlot.text = "Plot: ${series.plot}"
            tvEp.text = "Episodes: ${series.episodes}"
            tvRating.text = "Rating: ${series.rating}"
            tvViews.text = "Views: ${series.views}"
            tvGenres.text = "Genres: "
            for (genre: Genre in series.genres) {
                tvGenres.append(genre.type.lowercase(Locale.getDefault()) + " ")
            }

            btnAdd.setOnClickListener {
                addListener(series)
            }
        }
    }
}