import { Component, OnInit } from '@angular/core';
import { Series } from "../models/series";
import { SeriesService } from "../_services/series.service";
import { ActivatedRoute } from "@angular/router";
import { Location } from '@angular/common';

@Component({
  selector: 'app-detail',
  templateUrl: './series.detail.component.html',
  styleUrls: ['./series.detail.component.css']
})
export class SeriesDetailComponent implements OnInit {

  serie?:Series;

  constructor(
    private route: ActivatedRoute,
    private seriesService: SeriesService,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getSeries();
  }

  getSeries(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.seriesService.getSeriesById(id)
      .subscribe(serie => this.serie = serie);
  }

  goBack(): void {
    this.location.back();
  }

}
