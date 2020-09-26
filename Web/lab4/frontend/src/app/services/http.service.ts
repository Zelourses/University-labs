import {forwardRef, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {backendHost} from '../../config';
import {FormGroup} from '@angular/forms';
import {backendApi} from './utils/BackendApi';
import {BackendApiToaster} from './utils/BackendApiToaster';
import {ToasterService} from './toaster.service';
import {Locker} from './utils/Locker';
import {dispatch} from 'rxjs/internal-compatibility';
import {AppComponent} from '../app.component';
import {Session} from '../models/Session';
import {MainComponent} from '../components/main/main.component';
import {Router, RouterLink} from '@angular/router';



const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/x-www-form-urlencoded',
  })
};
@Injectable({
  providedIn: 'root'
})
export class HttpService {
  constructor(private router: Router) {
  }
  public static Auth = false;
  createUser(toaster: ToasterService, username: string, password: string) {
    Locker(async () => {
    const response = await BackendApiToaster(toaster, backendApi('users/create', 'POST', {username, password}));
    if (response.ok) {
      toaster.succes('Успешно!', 'Вы успешно зарегистрировались. Теперь вы можете войти');
    } else if (response.status === 400) {
      toaster.error('Ошибка при регистрации', 'Такой пользователь уже существует');
    }
    });
  }
  loginUser(toaster: ToasterService, username: string, password: string) {
    Locker(async () => {
      const respone = await BackendApiToaster(toaster, backendApi('session/create', 'POST', {username, password}));
      if (respone.ok) {
        const json: Response = await respone.json();
        localStorage.setItem('token', JSON.stringify(json)); // тут вроде строка
        this.router.navigate(['main']);
      } else if (respone.status === 401) {
        AppComponent.toaster.error('Ошибка входа!', 'Неизвестный пользователь либо неверный пароль');
      }
    });
  }
}
