import { Component, OnInit } from '@angular/core';
import {Series} from "../models/series";
import {SeriesService} from "../_services/series.service";

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {

  series?:Series;

  constructor(private seriesService: SeriesService) { }

  ngOnInit(): void {
  }

  getSeries(title:string){
    this.seriesService.getSeriesByTitle(title).subscribe(
      data => {
        this.series = data;
      },
      err => {
        this.series = JSON.parse(err.error).message;
      }
    );
  }
}
