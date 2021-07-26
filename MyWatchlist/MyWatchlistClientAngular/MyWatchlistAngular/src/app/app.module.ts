import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SerieService } from './servizi/serie.service';
import { DettaglioSerieComponent } from './componenti/dettaglio-serie/dettaglio-serie.component';
import { ListaSerieComponent } from './componenti/dettaglio-serie/lista-serie/lista-serie.component';

@NgModule({
  declarations: [
    AppComponent,
    DettaglioSerieComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [SerieService],
  bootstrap: [AppComponent]
})
export class AppModule { }

