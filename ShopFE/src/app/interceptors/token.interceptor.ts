import { Injectable } from "@angular/core";

import { HttpInterceptor } from "@angular/common/http";

import { HttpRequest } from "@angular/common/http";

import { HttpHandler } from "@angular/common/http";

import { HttpEvent } from "@angular/common/http";
import { Observable } from "rxjs";

import { TokenService } from "../services/token.service";

@Injectable({
    providedIn: 'root'
})

export class TokenInterceptor implements HttpInterceptor{

    constructor(private tokenService: TokenService){
    }

    intercept(
        req: HttpRequest<any>, 
        next: HttpHandler): Observable<HttpEvent<any>> {
        
            const token = this.tokenService.getToken(); // get toekn from localStorage

            if(token){
                req = req.clone({
                    setHeaders: {
                        Authorization: `Bearer ${token}`
                    },
                });
            }

            return next.handle(req);
    }
    
}