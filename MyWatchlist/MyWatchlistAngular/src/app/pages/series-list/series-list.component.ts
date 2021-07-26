import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { CreateSeriesDialogComponent } from 'src/app/components/create-series-dialog/create-series-dialog.component';
import { Series } from 'src/app/model/series';
import { SeriesService } from 'src/app/services/series.service';
import { TitleService } from 'src/app/services/title.service';

@Component({
  selector: 'app-series-list',
  templateUrl: './series-list.component.html',
  styleUrls: ['./series-list.component.css']
})
export class SeriesListComponent implements OnInit {

  seriesList: Series[] = [];

  constructor(
    private readonly seriesService: SeriesService,
    private readonly titleService: TitleService,
    private readonly dialog: MatDialog
    ) { }

  ngOnInit(): void {
    this.seriesService.getAll()
    .pipe(
      map((series: Series[]) => this.seriesList = this.seriesList)
    )
    .subscribe();
  }

  create(): void {
    this.dialog.open(CreateSeriesDialogComponent)
    .afterClosed()
    .pipe(
      switchMap((series: Series | undefined) => series ?
       this.seriesService.add(series) :
        new Observable(sub => sub.complete()))
    )
    .subscribe(
      (series: any) => console.log('Series created: ${series.id}')
    );
    this.titleService.title.next('SeriesList');
  }
}
