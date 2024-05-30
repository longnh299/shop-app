import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root',
})

// use to store jwt token to localStorage
export class TokenService {

    private readonly TOKEN_KEY = 'access-token';

    constructor(){

    }

    getToken(): string | null{
        return localStorage.getItem(this.TOKEN_KEY);
    }

    setToken(token: string): void {

        localStorage.setItem(this.TOKEN_KEY, token);

    }

    removeToken(){
        localStorage.removeItem(this.TOKEN_KEY);
    }
}

