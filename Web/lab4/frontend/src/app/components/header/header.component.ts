import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {Locker} from "../../services/utils/Locker";
import {authorizedBackendApi} from "../../services/utils/BackendApi";
import {Session} from "../../models/Session";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

  check(): boolean {
    if (this.router.url === '/main') {
      return true;
    }
    return localStorage.getItem('token') != null && localStorage.getItem('token') !== undefined;
  }
  logout(): void {
    Locker(async () => {
      const session: Session = JSON.parse(localStorage.getItem('token'));
          const result = await authorizedBackendApi('session/destroy', session, 'DELETE', {token: session.token});
          if (result.ok) {
            localStorage.removeItem('token');
            await this.router.navigate(['']); // нужно ли это здесь? не перенаправит ли меня отсюда гуард?(луень тестить)
          }
    });
  }

}
