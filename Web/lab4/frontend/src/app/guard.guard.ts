import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import {ToasterService} from './services/toaster.service';
import {Locker, locker2} from './services/utils/Locker';
import {Session} from './models/Session';
import {BackendApiToaster} from './services/utils/BackendApiToaster';
import {authorizedBackendApi, backendApi} from './services/utils/BackendApi';
@Injectable({
  providedIn: 'root'
})
export class GuardGuard implements CanActivate {
  constructor(private  toast: ToasterService,
              private router: Router) {}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
     const session = localStorage.getItem('token');
        console.log(session);
        if (session == null) {
            this.router.navigate(['']);
            return false;
        }
            return locker2(async () => {
                try {
                    const t: Session = JSON.parse(session);
                    // console.log(t.token + ' <- token');
                    // console.log(t.userId + '<- userID');
                    const response = await BackendApiToaster(this.toast, authorizedBackendApi('session/check', t));
                    if (response.ok) {
                        console.log('Всё ок, идём в main');
                        return true;
                    } else {
                        this.router.navigate(['']);
                        return false;
                    }
                } catch (e) {
                    this.router.navigate(['']);
                    return false;
                }
            });
  }
}
