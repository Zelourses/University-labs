import { Injectable } from '@angular/core';
import {MessageService} from 'primeng';
import {AppComponent} from '../app.component';

@Injectable({
  providedIn: 'root'
})
export class ToasterService {

  constructor(private toaster: MessageService) { }
  public app;
  private toast: ToasterService;
  public succes(summary: string, detail: string) {
    this.toaster.add({severity: 'success', summary: summary, detail: detail});
  }
  public error(summary: string, detail: string) {
    this.toaster.add({severity: 'error', summary: summary, detail: detail});
  }
  public info(summary: string, detail: string) {
    this.toaster.add({severity: 'info', summary: summary, detail: detail});
  }
  public setToast(toaster: ToasterService) {
    this.toast = toaster;
  }
  public getToast() {
    return this.toast;
  }
}
