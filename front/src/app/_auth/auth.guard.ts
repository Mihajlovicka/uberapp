import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {UserAuthService} from "../service/user-auth.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private service:UserAuthService,
    private router:Router
  ) {
  }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if(this.service.getToken() != null){
      const role = route.data["role"] as string;
      if(role){
        const match = this.service.roleMatch(role);
        if(match) return true;
        else {
          this.router.navigate(['/forbidden']);
          return false;
        }
      }

    }

    //this.router.navigate(['/login']);
    return false;
  }

}
