import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NavigationComponent } from './navigation/navigation.component';
import { LoginComponent } from './pages/login/login.component';
import { SeriesDetailComponent } from './pages/series-detail/series-detail.component';
import { SeriesListComponent } from './pages/series-list/series-list.component';
import { AuthenticationGuard } from './services/guards/authentication.guard';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    component: NavigationComponent,
    canActivate: [AuthenticationGuard],
    children: [
      {
        path: '',
        component: SeriesListComponent
      },
      {
        path: 'series/:id',
        component: SeriesDetailComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
