import { Component, Input, OnInit } from '@angular/core';
import { Serie } from 'src/app/modello/serie';
import { SerieService } from 'src/app/servizi/serie.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-dettaglio-serie',
  templateUrl: './dettaglio-serie.component.html',
  styleUrls: ['./dettaglio-serie.component.css']
})
export class DettaglioSerieComponent implements OnInit {

  ser: Serie;

  constructor(private route: ActivatedRoute, private serieService: SerieService) { }

  ngOnInit(): void {
    const serid = this.route.params.subscribe(params =>
       this.serieService.getSerie(params['id']).subscribe(ser => this.ser = ser);
  }

}
