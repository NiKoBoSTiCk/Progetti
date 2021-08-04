import {Genre} from "./genre";

export class Series {
  id: number;
  title: string;
  episodes: number;
  rating: number;
  plot: string;
  views: number;
  members: number;
  genres: Genre[];

  constructor(id:number, title:string, episodes:number,
              rating:number, plot:string, views:number,
              members:number, genres:Genre[]) {
    this.id = id;
    this.title = title;
    this.episodes = episodes;
    this.rating = rating;
    this.views = views;
    this.plot = plot;
    this.members = members;
    this.genres = genres;
  }
}
