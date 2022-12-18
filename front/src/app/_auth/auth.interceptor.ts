import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {UserRegistrationService} from "../user-registration.service";
import {Router} from "@angular/router";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {
  constructor(private service: UserRegistrationService,
              private router:Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>>
  {

    if (req.headers.get('No-Auth') === 'True') {
      return next.handle(req.clone());
    }

    const token = this.service.getToken();

    req = this.addToken(req, token);
    return next.handle(req).pipe(
      catchError(
        (err:HttpErrorResponse) => {
          console.log(err.status);
          if(err.status === 401){
            this.router.navigate(['/login']);
          }else if(err.status === 403){
            this.router.navigate(['/forbidden']);
          }
          return throwError("Nesto je poslo po zlu. ");
        }
      )
    )

  }

  private addToken(req: HttpRequest<any>, token: string | null){
    return req.clone(
      {
        setHeaders:{
          Authorization:`Barer ${token}`
        }
      }
    );
  }

}