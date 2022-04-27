package it.niko.mywatchlistandroid.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.niko.mywatchlistandroid.databinding.SeriesListBinding

class SeriesAdapter(private val seriesList: ArrayList<Series>, private val clickListener: (Series) -> Unit): RecyclerView.Adapter<SeriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val binding = SeriesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) = holder.bind(seriesList[position], clickListener)

    override fun getItemCount(): Int = seriesList.size
}

class SeriesViewHolder(private val binding: SeriesListBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(series: Series, clickListener: (Series) -> Unit) {
        binding.apply {
            tvTitle.text = series.title
            ("Plot: " + series.plot).also { tvPlot.text = it }
            ("Episodes: " + series.episodes.toString()).also { tvEp.text = it }
            ("Rating: " + series.rating.toString()).also { tvRating.text = it }
            ("Views: " + series.views.toString()).also { tvViews.text = it }

            "Genres: ".also { tvGenres.text = it }
            for (genre: Genre in series.genres) {
                tvGenres.append(genre.type + " ")
            }

            btnAdd.setOnClickListener {
                clickListener(series)
            }
        }
    }
}