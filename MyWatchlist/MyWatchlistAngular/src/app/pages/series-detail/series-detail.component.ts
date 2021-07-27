import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, map, switchMap } from 'rxjs/operators';
import { MOCK_SERIES } from 'src/app/mock/mock-series';
import { Series } from 'src/app/model/series';
import { SeriesService } from 'src/app/services/series.service';
import { TitleService } from 'src/app/services/title.service';

@Component({
  selector: 'app-series-detail',
  templateUrl: './series-detail.component.html',
  styleUrls: ['./series-detail.component.css']
})
export class SeriesDetailComponent implements OnInit {

  series: Series | undefined;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly seriesService: SeriesService,
    private readonly titleService: TitleService,
    private readonly router: Router,
    private readonly snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.route.params
          .pipe(
            switchMap(params => this.seriesService.get(+params.id)),
            catchError(err => {
              this.router.navigate(['/']);
              this.snackBar.open(`Error: ${err}`);
              throw err;
            }),
            map((series: Series) => {
              this.series = series;
              this.titleService.title.next(`Series ${series.id}`);
            } )
          )
          .subscribe();
  }

  delete(series: Series): void {
    this.seriesService.remove(series.id)
      .subscribe(
        () => {
          console.log(`Series ${series.name} deleted!`);
          this.router.navigate(['/']);
        },
        err =>{
          console.error(err);
          this.snackBar.open(`Error: ${err}`);
        }
      );
  }
}
