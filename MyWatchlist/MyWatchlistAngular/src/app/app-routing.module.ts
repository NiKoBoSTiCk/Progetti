import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { WatchlistComponent } from './watchlist/watchlist.component';
import { UserListComponent } from './user-list/user-list.component';
import { AddSeriesComponent } from './add-series/add-series.component';
import { SeriesDetailComponent } from "./series-detail/series.detail.component";
import { WatchlistDetailComponent } from "./watchlist-detail/watchlist-detail.component";

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'detail/:id', component: SeriesDetailComponent },
  { path: 'detail-watchlist/:id', component: WatchlistDetailComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'user', component: WatchlistComponent },
  { path: 'mod', component: UserListComponent },
  { path: 'admin', component: AddSeriesComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
