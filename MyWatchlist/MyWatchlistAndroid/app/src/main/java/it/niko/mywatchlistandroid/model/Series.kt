package it.niko.mywatchlistandroid.model

data class Series (
    private val id: Int,
    private val title: String,
    private val rating: Double,
    private val views: Int,
    private val plot: String,
    private val members: Int,
    private val genres: List<String>
)
