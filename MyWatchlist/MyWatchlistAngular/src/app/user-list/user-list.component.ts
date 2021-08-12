import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { SeriesService } from "../_services/series.service";
import { AuthService } from "../_services/auth.service";
import { User } from "../models/user";
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-board-moderator',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements AfterViewInit {

  // @ts-ignore
  @ViewChild(MatPaginator) paginator: MatPaginator;

  users = new MatTableDataSource<User>();
  displayedColumns: string[] = ['username', 'email'];

  constructor(private seriesService: SeriesService, private auth: AuthService) { }

  ngAfterViewInit() {
    this.getAllUsers();
    this.users.paginator = this.paginator;
  }

  getAllUsers(){
    this.auth.getAllUsers().subscribe(
      data => {
        this.users = data;
      },
      err => {
        this.users = JSON.parse(err.error).message;
      }
    );
  }
}
