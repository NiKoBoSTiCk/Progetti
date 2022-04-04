package it.niko.mywatchlistandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import it.niko.mywatchlistandroid.model.Series

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  setSupportActionBar(toolbar)

        val series = arrayListOf<Series>()

        for(i in 0..100){
            val genres = arrayListOf<String>()
            genres.add("DRAMA")
            series.add(Series("Breaking Bad","" , 80, "nice", genres))
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = SeriesAdapter()
        }
    }
}