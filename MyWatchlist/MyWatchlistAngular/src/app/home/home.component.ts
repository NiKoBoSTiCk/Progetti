import { Component, OnInit } from '@angular/core';
import { SeriesService } from '../_services/series.service';
import {Series} from "../models/series";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  series: Series[] = [];
  currentIndex = -1;
  title = '';
  pageNumber = 1;
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  sortBy = '';

  constructor(private seriesService: SeriesService) { }

  ngOnInit(): void {
    this.getAllSeries();
  }

  getAllSeries(){
    const params = this.getRequestParams(this.pageNumber, this.pageSize, this.sortBy);
    this.seriesService.getAllSeries(params.pageNumber, params.pageSize, 'title').subscribe(
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

  searchTitle() {
    this.seriesService.getSeriesByTitle(this.title).subscribe(
      data => {
        this.series[0] = data;
      },
      err => {
        this.series = JSON.parse(err.error).message;
      }
    );
  }

  handlePageChange(event: number): void {
    this.pageNumber = event;
    this.getAllSeries();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.pageNumber = 1;
    this.getAllSeries();
  }

  refreshList(): void {
    this.getAllSeries();
    this.currentIndex = -1;
  }
}
