import { Component, OnInit } from '@angular/core';
import { SeriesService } from "../_services/series.service";
import { SeriesForm } from "../models/series-form";

@Component({
  selector: 'app-board-admin',
  templateUrl: './add-series.component.html',
  styleUrls: ['./add-series.component.css']
})
export class AddSeriesComponent implements OnInit {

  genresList = ['drama', 'crime', 'sci_fi', 'comedy', 'fantasy', 'animation',
            'sitcom', 'reality', 'soap_opera', 'telenovela', 'documentary',
            'action', 'educational', 'sports', 'horror', 'romance',
            'satirical', 'supernatural', 'science', 'school', 'time_travel',
            'historical', 'adventure', 'thriller', 'war'];
  model = new SeriesForm('', 0, '', []);

  constructor(private seriesService: SeriesService) { }

  ngOnInit(): void {

  }

  onSubmit() {
    const {  title, episodes, plot, genres } = this.model;
    this.seriesService.addSeries(title, episodes, plot, genres).subscribe(
      data => {this.reloadPage()}
    )
  }

  reloadPage(): void {
    window.location.reload();
  }
}

