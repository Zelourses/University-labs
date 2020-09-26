import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import {locker2} from "./services/utils/Locker";
import {Session} from "./models/Session";
import {BackendApiToaster} from "./services/utils/BackendApiToaster";
import {authorizedBackendApi} from "./services/utils/BackendApi";
import {ToasterService} from "./services/toaster.service";

@Injectable({
  providedIn: 'root'
})
export class Guard2Guard implements CanActivate {
  constructor(private  toast: ToasterService,
              private router: Router) {}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const session = localStorage.getItem('token');
    if (session == null){
      return true;
    }
    // console.log(session);
    return locker2(async () => {
      try {
        const t: Session = JSON.parse(session);
        // console.log(t.token + ' <- token');
        // console.log(t.userId + '<- userID');
        const response = await BackendApiToaster(this.toast, authorizedBackendApi('session/check', t));
        if (response.ok) {
          this.router.navigate(['main']);
          return false;
        } else {
          return true;
        }
      } catch (e) {
        return true;
      }
    });
  }
}
