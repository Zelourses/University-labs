import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpService} from '../../services/http.service';
import {ToasterService} from '../../services/toaster.service';
import {AppComponent} from '../../app.component';

@Component({
  selector: 'app-login-register',
  templateUrl: './login-register.component.html',
  styleUrls: ['./login-register.component.css']
})
export class LoginRegisterComponent implements OnInit {

  registerForm: FormGroup;
  RegisterClicked = false;
  constructor(public connector: HttpService, private fb: FormBuilder, private toaster: ToasterService) { }

  ngOnInit() {
    this.registerForm = this.fb.group({
      'username': ['', [
          Validators.required,
      ]],
      'password': ['', [
          Validators.required,
      ]]
    });
  }
  //  TODO это говно надо бы исправить, чтобы оно выглядело нормально, но времени нет.
  OnSubmit(): void {
    if (this.RegisterClicked) {
      this.onRegister();
    } else {
      this.onLogin();
    }
  }

  private onRegister() {
   this.connector.createUser(this.toaster, this.registerForm.controls['username'].value, this.registerForm.controls['password'].value);
  }

  private onLogin() {
    this.connector.loginUser(this.toaster, this.registerForm.controls['username'].value, this.registerForm.controls['password'].value);
  }

  onRegisterSubmit() {
    this.RegisterClicked = true;
  }
  onLoginSubmit() {
    this.RegisterClicked = false;
  }

}
