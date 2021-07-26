import { Component, OnInit } from '@angular/core';
import { SerieService } from 'src/app/servizi/serie.service';
import { Router } from '@angular/router'

@Component({
  selector: 'app-lista-serie',
  templateUrl: './lista-serie.component.html',
  styleUrls: ['./lista-serie.component.css']
})
export class ListaSerieComponent implements OnInit {

  listaSerie: Serie[];
  selIndex = -1;

  constructor(private serieService: SerieService, private router: Router) { }

  ngOnInit(): void {
    this.serieService.getAllSerie().subscribe(ser => this.listaSerie = ser)
  }

  select(ser: Serie){
    this.router.navigate(['dettaglio', ser.id]);
    this.setIndex = this.listaSerie.indexOf(ser);
  }

}
