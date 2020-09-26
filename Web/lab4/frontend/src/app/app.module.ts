import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import {ToastModule} from 'primeng/toast';
import {Routes, RouterModule} from '@angular/router';

import { AppComponent } from './app.component';
import {
    AccordionModule, CardModule,
    InputTextareaModule,
    KeyFilterModule,
    PaginatorModule,
    PasswordModule,
    RadioButtonModule, ScrollPanelModule,
    SidebarModule
} from 'primeng';
import { HeaderComponent } from './components/header/header.component';
import { ContainerComponent } from './components/container/container.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import {GuardGuard} from './guard.guard';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import {MessageService} from 'primeng/api';
import { MainComponent } from './components/main/main.component';
import {Guard2Guard} from "./guard2.guard";

const appRoutes: Routes = [
    {path: '', component: LoginRegisterComponent, pathMatch: 'full', canActivate: [Guard2Guard]},
    {path: 'main', component: MainComponent, canActivate: [GuardGuard]},
    {path: '**', component: NotFoundComponent},
];

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        ContainerComponent,
        NotFoundComponent,
        LoginRegisterComponent,
        MainComponent,
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        TableModule,
        HttpClientModule,
        InputTextModule,
        DialogModule,
        ButtonModule,
        PasswordModule,
        ReactiveFormsModule,
        RouterModule.forRoot(appRoutes),
        ToastModule,
        RadioButtonModule,
        KeyFilterModule,
        PaginatorModule,
        SidebarModule,
        InputTextareaModule,
        AccordionModule,
        ScrollPanelModule,
        CardModule
    ],
    providers: [MessageService],
    bootstrap: [AppComponent]
})
export class AppModule { }
