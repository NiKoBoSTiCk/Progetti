import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Location } from "@angular/common";
import { WatchlistService } from "../_services/watchlist.service";
import { Watchlist } from "../models/watchlist";
import { TokenStorageService } from "../_services/token-storage.service";
import { WatchForm } from "../models/watch-form";

@Component({
  selector: 'app-watchlist-detail',
  templateUrl: './watchlist-detail.component.html',
  styleUrls: ['./watchlist-detail.component.css']
})
export class WatchlistDetailComponent implements OnInit {

  states = ['watching', 'completed', 'dropped', 'on_hold', 'plan_to_watch'];
  model = new WatchForm(this.states[0], 0, 0, '');
  currentUser: any;
  watchlist?: Watchlist;
  update = false;

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
      this.watchlistService.deleteWatchlist(this.watchlist.series.title, this.currentUser.username).subscribe(
        _ => { this.goBack() }
      )
    }
  }

  onSubmit(): void {
    const { progress, status, score, comment } = this.model;
    if(this.watchlist) {
      this.watchlistService.updateWatchlist(
        this.watchlist.series.title, this.currentUser.username,
        status, progress, score, comment).subscribe(
        data => {this.reloadPage()}
      )
    }
  }

  updateButton(){
    this.update = !this.update;
  }

  goBack(): void {
    this.location.back();
  }

  reloadPage(): void {
    window.location.reload();
  }
}
