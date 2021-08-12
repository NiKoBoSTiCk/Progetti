export class SeriesForm {
  title: string;
  episodes: number;
  plot: string;
  genres: string[];

  constructor(title: string, episodes: number, plot: string, genres: string[]) {
    this.title = title; this.episodes = episodes; this.plot = plot;
    this.genres = genres;
  }
}

