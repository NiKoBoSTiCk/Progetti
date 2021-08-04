import { Component, OnInit } from '@angular/core';
import { SeriesService } from '../_services/series.service';
import {Series} from "../models/series";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  series?: Series[];

  constructor(private seriesService: SeriesService) { }

  ngOnInit(): void {
    this.getSeries('title')
  }

  getSeries(sortBy:string){
    this.seriesService.getAllSeries(sortBy).subscribe(
      data => {
        this.series = data;
      },
      err => {
        this.series = JSON.parse(err.error).message;
      }
    );
  }

}
