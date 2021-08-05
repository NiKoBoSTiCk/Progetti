import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {WatchlistService} from "../_services/watchlist.service";
import {Watchlist} from "../models/watchlist";
import {TokenStorageService} from "../_services/token-storage.service";

@Component({
  selector: 'app-watchlist-detail',
  templateUrl: './watchlist-detail.component.html',
  styleUrls: ['./watchlist-detail.component.css']
})
export class WatchlistDetailComponent implements OnInit {
  currentUser: any;
  watchlist?: Watchlist;

  constructor(
    private route: ActivatedRoute,
    private watchlistService: WatchlistService,
    private location: Location,
    private token: TokenStorageService
  ) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.getWatchlist();
  }

  getWatchlist(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.watchlistService.getWatchlistById(id)
      .subscribe(watchlist => this.watchlist = watchlist);
  }

  deleteWatchlist(): void{
    if(this.watchlist){
      this.watchlistService.deleteWatchlist(this.watchlist.series.title, this.currentUser.username)
    }
  }

  goBack(): void {
    this.location.back();
  }

}
