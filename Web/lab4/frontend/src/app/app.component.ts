import {Component, Injectable, NgModule, OnInit} from '@angular/core';
import {MessageService} from 'primeng/api';
import {AppModule} from './app.module';
import {ToasterService} from './services/toaster.service';
import {Session} from "./models/Session";


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    providers: []
})
@Injectable({ providedIn: 'root' })

export class AppComponent implements OnInit {
    public static toaster: ToasterService;
    public newsActive: boolean;
    constructor(private toaster: ToasterService) {}
    ngOnInit() {
        this.toaster.app = this;
        AppComponent.toaster = this.toaster;
    }
    private getTop() {
        return this.newsActive ? '150px' : '80px';
    }
}
