import { Component, OnInit } from '@angular/core';
import { SeriesService } from '../_services/series.service';
import { Series } from "../models/series";
import {Genres} from "../models/genres";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  series: Series[] = [];
  sortType = ['title', 'rating', 'views', 'members'];
  genresList = Genres.list;
  selectedGenre = '';
  sortBy = 'title';
  title = '';
  byTitle = false;
  byGenre = false;
  pageNumber = 1;
  count = 0;
  pageSize = 5;
  pageSizes = [5, 10, 15];

  constructor(private seriesService: SeriesService) { }

  ngOnInit(): void {
    this.getAllSeries();
  }

  getAllSeries(){
    this.byTitle = false;
    this.byGenre = false;
    const params = this.getRequestParams(this.pageNumber, this.pageSize, this.sortBy);
    this.seriesService.getAllSeries(params.pageNumber, params.pageSize, params.sortBy).subscribe(
      response => {
        const { content, totalElements } = response;
        this.series = content;
        this.count = totalElements;
      },
      err => {
        this.series = JSON.parse(err.error).message;
      }
    );
  }

  searchTitle() {
    this.byTitle = true;
    this.byGenre = false;
    this.selectedGenre = '';
    const params = this.getRequestParams(this.pageNumber, this.pageSize, this.sortBy);
    this.seriesService.getSeriesByTitle(this.title, params.pageNumber, params.pageSize, params.sortBy).subscribe(
      response => {
        const { content, totalElements } = response;
        this.series = content;
        this.count = totalElements;
      },
      err => {
        this.series = JSON.parse(err.error).message;
      }
    );
  }

  searchGenre() {
    this.byGenre = true;
    this.byTitle = false;
    const params = this.getRequestParams(this.pageNumber, this.pageSize, this.sortBy);
    this.seriesService.getSeriesByGenre(this.selectedGenre, params.pageNumber, params.pageSize, params.sortBy).subscribe(
      response => {
        const { content, totalElements } = response;
        this.series = content;
        this.count = totalElements;
      },
      err => {
        this.series = JSON.parse(err.error).message;
      }
    );
  }

  getRequestParams(pageNumber: number, pageSize: number, sortBy: string): any {
    let params: any = {};
    if (pageNumber) { params[`pageNumber`] = pageNumber - 1; }
    if (pageSize) { params[`pageSize`] = pageSize; }
    if (sortBy) { params[`sortBy`] = sortBy; }
    return params;
  }

  handlePageChange(event: number): void {
    this.pageNumber = event;
    if(this.byTitle) this.searchTitle();
    else if(this.byGenre) this.searchGenre();
    else this.getAllSeries();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.pageNumber = 1;
    if(this.byTitle) this.searchTitle();
    else if(this.byGenre) this.searchGenre();
    else this.getAllSeries();
  }

  changeSortType(event: any){
    this.sortBy = event.target.value;
    if(this.byTitle) this.searchTitle();
    else if(this.byGenre) this.searchGenre();
    else this.getAllSeries();
  }
}
