import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaSerieComponent } from './lista-serie.component';

describe('ListaSerieComponent', () => {
  let component: ListaSerieComponent;
  let fixture: ComponentFixture<ListaSerieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListaSerieComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListaSerieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
