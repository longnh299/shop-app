import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';

import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { RegisterDTO } from '../../dtos/user/register.dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  @ViewChild('registerForm') registerForm!: NgForm;

  //khai báo các trường dữ liệu trong form

  phoneNumber : string
  password: string
  retypePassword: string
  fullname: string
  address: string
  isAccepted: boolean
  dob: Date
 
  constructor( private router: Router, private userService: UserService){
    this.phoneNumber = "";
    this.password = "";
    this.retypePassword = "";
    this.fullname = "";
    this.address = "";
    this.isAccepted = false;
    this.dob = new Date();
    this.dob.setFullYear(this.dob.getFullYear() - 18);
  }

  onPhoneChange(){
    //console.log(`Phone typed: ${this.phone}`);
  }

  register(){

    const registerDTO:RegisterDTO = {
      "fullname": this.fullname,
      "phone_number": this.phoneNumber,
      "address": this.address,
      "password": this.password,
      "retype_password": this.retypePassword,
      "dob": this.dob,
      "facebook_account_id": 0,
      "google_account_id": 0,
      "role_id": 1
    }

    this.userService.register(registerDTO).subscribe(
      {
        next: (response: any) => {
          debugger
          this.router.navigate(['/login'])
        },
  
        complete: () => {
            debugger
        },
  
        error: (error: any) => {
          alert(`Dang ki khong thanh cong: ${error.error}`)
        }
  
      }
    );

  }

  checkPasswordMatch(){
    if(this.password != this.retypePassword){
      this.registerForm.form.controls['retypePassword'].setErrors({'passwordMismatch': true});
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }

}
