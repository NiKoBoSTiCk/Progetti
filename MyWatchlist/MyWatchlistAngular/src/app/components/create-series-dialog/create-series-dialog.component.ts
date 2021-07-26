import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Series } from 'src/app/model/series';

@Component({
  selector: 'app-create-series-dialog',
  templateUrl: './create-series-dialog.component.html',
  styleUrls: ['./create-series-dialog.component.css']
})
export class CreateSeriesDialogComponent implements OnInit {

  series: Series;

  constructor(private readonly ref: MatDialogRef<CreateSeriesDialogComponent>) {
    this.series = {
      id: 0,
      name: '',
      episodes: 0,
      rating: 0,
      plot: ''};
  }

  ngOnInit(): void {
  }

  close(): void {
    this.ref.close(this.series);
  }
}
