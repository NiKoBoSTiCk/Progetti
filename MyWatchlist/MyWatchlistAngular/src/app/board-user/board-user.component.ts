import { Component, OnInit } from '@angular/core';
import { WatchlistService } from "../_services/watchlist.service";
import { TokenStorageService } from "../_services/token-storage.service";
import { Watchlist } from "../models/watchlist";


@Component({
  selector: 'app-board-user',
  templateUrl: './board-user.component.html',
  styleUrls: ['./board-user.component.css']
})
export class BoardUserComponent implements OnInit {

  currentUser: any;
  watchlist?: Watchlist[];

  constructor(private watchlistService: WatchlistService, private token: TokenStorageService) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.getWatchlist(this.currentUser.username)
  }

  getWatchlist(username:string){
    this.watchlistService.getWatchlist(username).subscribe(
      data => {
        this.watchlist = data;
      },
      err => {
        this.watchlist = JSON.parse(err.error).message;
      }
    );
  }

}
