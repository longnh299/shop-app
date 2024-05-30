import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginDTO } from '../../dtos/user/login.dto';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import {LoginResponse} from '../../responses/user/login.response';
import { TokenService } from 'src/app/services/token.service';
import { RoleService } from 'src/app/services/role.service';
import { Role } from 'src/app/models/role';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;

  phoneNumber : string;
  password: string;


  constructor( private router: Router, 
    private userService: UserService,
    private tokenService: TokenService){

    this.phoneNumber = "";
    this.password = "";

  }


  onPhoneNumberChange(){
    //console.log(`Phone typed: ${this.phone}`);
  }

  login(){

    const loginDTO:LoginDTO = {
      "phone_number": this.phoneNumber,
      "password": this.password
    }

    this.userService.login(loginDTO).subscribe(
      {
        next: (response: LoginResponse) => {
          debugger

          // get token in response from backend
          const {token} = response;

          // save token into localStorage
          this.tokenService.setToken(token);

          //this.router.navigate(['/login'])
        },
  
        complete: () => {
            debugger
        },
  
        error: (error: any) => {
          alert(`Dang nhap khong thanh cong: ${error.error}`)
        }
  
      }
    );

  }

}
