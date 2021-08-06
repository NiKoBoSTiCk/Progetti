import { Component, OnInit } from '@angular/core';
import { Series } from "../models/series";
import { SeriesService } from "../_services/series.service";
import { ActivatedRoute } from "@angular/router";
import { Location } from '@angular/common';
import {TokenStorageService} from "../_services/token-storage.service";
import {WatchlistService} from "../_services/watchlist.service";

@Component({
  selector: 'app-detail',
  templateUrl: './series.detail.component.html',
  styleUrls: ['./series.detail.component.css']
})
export class SeriesDetailComponent implements OnInit {

  serie?:Series;
  currentUser: any;
  isLoggedIn = false;

  constructor(
    private route: ActivatedRoute,
    private seriesService: SeriesService,
    private watchlistService: WatchlistService,
    private location: Location,
    private token: TokenStorageService
  ) { }

  ngOnInit(): void {
    this.isLoggedIn = !!this.token.getToken();
    this.currentUser = this.token.getUser();
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

  addToWatchlist(): void {
    if(this.serie){
      this.watchlistService.addWatchlist(this.serie.title, this.currentUser.username).subscribe();
    }
  }
}
