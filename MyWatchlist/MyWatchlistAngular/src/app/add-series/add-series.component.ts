import { Component, OnInit } from '@angular/core';
import { SeriesService } from "../_services/series.service";
import { SeriesForm } from "../models/series-form";
import { Genres } from "../models/genres";

@Component({
  selector: 'app-board-admin',
  templateUrl: './add-series.component.html',
  styleUrls: ['./add-series.component.css']
})
export class AddSeriesComponent implements OnInit {

  genresList = Genres.list;
  model = new SeriesForm('', 0, '', []);

  constructor(private seriesService: SeriesService) { }

  ngOnInit(): void { }

  onSubmit() {
    const {  title, episodes, plot, genres } = this.model;
    this.seriesService.addSeries(title, episodes, plot, genres).subscribe(
      _ => { this.reloadPage() }
    )
  }

  reloadPage(): void {
    window.location.reload();
  }
}

