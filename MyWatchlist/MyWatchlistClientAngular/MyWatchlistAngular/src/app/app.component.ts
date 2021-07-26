import { Component } from '@angular/core';
import { Serie } from './modello/serie';
import { SerieService } from './servizi/serie.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'MyWatchlistAngular';

  sservice:SerieService;

  selezionato:Serie;

  constructor(service:SerieService){
    this.sservice = service;
  }

  seleziona(ser:Serie){
    this.selezionato=ser;
  }

  cancella(ser:Serie){
    this.sservice.del(ser);
    this.selezionato=null;
  }
}

