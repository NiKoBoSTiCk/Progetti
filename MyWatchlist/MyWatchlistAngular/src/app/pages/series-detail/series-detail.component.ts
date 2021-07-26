import { Component, OnInit } from '@angular/core';
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
    private readonly router: Router
  ) { }

  ngOnInit(): void {
    this.route.params
          .pipe(
            switchMap(params => this.seriesService.get(+params.id)),
            catchError(err => {
              this.router.navigate(['/']);
              throw err;
            }),
            map((series: Series) => {
              this.series = series;
              this.titleService.title.next(`Messaggio ${series.id}`);
            } )
          )
          .subscribe();
  }

  private getPlot(id: number): Series | undefined {
    return MOCK_SERIES.find(m => m.id === id);
  }

  delete(series: Series): void {
    this.seriesService.remove(series.id)
      .subscribe(
        () => {
          console.log(`${series.name} deleted!`);
          this.router.navigate(['/']);
        },
        err => console.error(err)
      );
  }
}
