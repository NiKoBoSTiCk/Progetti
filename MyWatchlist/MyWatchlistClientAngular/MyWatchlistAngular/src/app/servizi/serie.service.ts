import { Injectable } from '@angular/core';
import { Serie } from '../modello/serie'

@Injectable({
  providedIn: 'root'
})
export class SerieService {

  series:Serie[];

  selezionato:Serie;

  constructor() {
    this.series = [
      {id: 0, name: 'test0', episodes: 12, rating: 8, views: 235},
      {id: 1, name: 'test1', episodes: 24, rating: 7, views: 2345},
      {id: 2, name: 'test2', episodes: 56, rating: 2, views: 35},
      {id: 3, name: 'test3', episodes: 4, rating: 5, views: 5},
      {id: 4, name: 'test4', episodes: 80, rating: 9, views: 28935}
    ]
  }

  getAll():Serie[] {
    return this.series;
  }

  add(ser: Serie):void {
    this.series.push(ser);
  }

  get(index:number):Serie {
    return this.series.find((ser,id,obj)=> id==index);
  }

  del(ser:Serie):void {
    this.series.splice(this.series.indexOf(ser), 1);
  }
}
