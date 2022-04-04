package it.niko.mywatchlistandroid.model

data class Series(
    val title: String,
    val photoUrl: String,
    val episodes: Int,
    val plot: String,
    val genres: ArrayList<String>
)