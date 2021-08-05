import { Status } from "./status";
import { Series } from "./series";

export class Watchlist {
  id: number;
  series: Series;
  status: Status;
  progress: number;
  comment: string;
  score: number;

  constructor(id:number, series: Series, status: Status, progress: number,
              comment: string, score: number) {
    this.id = id; this.series = series;
    this.status = status; this.progress = progress;
    this.comment = comment; this.score = score;
  }
}
