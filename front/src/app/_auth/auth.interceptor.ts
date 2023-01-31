import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {UserAuthService} from "../service/user-auth.service";
import {Router} from "@angular/router";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {
  constructor(private service: UserAuthService,
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
        (err:any) => {
          console.log(err.status);
          console.log(err);
          if(err.error.message != undefined)
            if(err.error.message === "Email in use."){

            return throwError("Email in use.");

          }
          else if(err.error.message === "Account number does not exist.")
          {
            return throwError("Account number does not exist.");
          }
          else if(err.status === 401){
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
    return !req.headers.has('Authorization') ? req.clone(
      {
        setHeaders:{
          Authorization:`Bearer ${token}`
        }
      }
    ) : req;
  }

}
