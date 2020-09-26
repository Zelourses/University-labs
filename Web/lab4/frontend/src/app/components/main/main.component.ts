import {
  Component,
  ElementRef,
  OnInit,
  Renderer2,
  ViewChild,
  AfterViewInit,
  DoCheck,
  OnChanges,
  SimpleChanges, AfterViewChecked, Query
} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Locker} from '../../services/utils/Locker';
import {Observable, pipe} from 'rxjs';
import {map} from 'rxjs/operators';
import {ZERO} from '@angular/cdk/keycodes';
import {log} from 'util';
import {Session} from '../../models/Session';
import {BackendApiToaster} from '../../services/utils/BackendApiToaster';
import {authorizedBackendApi} from '../../services/utils/BackendApi';
import {ToasterService} from '../../services/toaster.service';
import {ok} from 'assert';
import {History} from '../../models/History';
import {Resolve} from '@angular/router';
import {Mouse} from '../../models/Mouse';
import {style} from '@angular/animations';




const CANVAS_WIDTH = 400;
const CANVAS_HEIGHT = 400;
const CANVAS_STEP_X = CANVAS_WIDTH / 2 / 7;
const CANVAS_STEP_Y = CANVAS_HEIGHT / 2 / 7;
const CANVAS_COLOR_PRIMARY = '#090909';
const CANVAS_COLOR_BACKGROUND = '#F9F9F9';
const CANVAS_COLOR_POINT_OTHER = '#333333';
const CANVAS_COLOR_POINT_INCLUDES = '#00ff00';
const CANVAS_COLOR_POINT_NOT_INCLUDES = '#ff0000';
@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit, AfterViewInit {
  constructor( private formBuilder: FormBuilder,
              private toaster: ToasterService) {
    this.areaform = this.formBuilder.group({
      X: [0],
      Y: [''],
      R: [0]
    });
    this.Code = this.formBuilder.group({
      code: ['']
    });
  }

  public canvasScale = 1;
  canvasTranslate = { x: 0, y: 0 };


  @ViewChild('panel')
  panel: ElementRef<HTMLTextAreaElement>;
  @ViewChild('canvas')
  canvas: ElementRef<HTMLCanvasElement>;
  areaform: FormGroup;
  Code: FormGroup;
  private isRDisabled = true;
  history: History[] = [];
  private Value;
  private FormPoint: History = {x: null, y: null, r: null, result: false};
  private formPointResult: boolean;
  private isMouseHover = false;
  private mouse: Mouse = {x: null, y: null};
  private session: Session;
  private previousFormPointRequest = 0;
  private previousR = '0';
  private previousFormPoint: History = {x: null, y: null, r: null, result: false};
  display = false;
  public Answer:string = '';
  ngOnInit() {

    this.areaform.get('R').valueChanges.subscribe((v) => {
      this.isRDisabled = Number.parseInt(v, 10) === 0;
    });
    this.onChangesR();
    this.onChanges();
    this.componentMount();
    this.session = JSON.parse(localStorage.getItem('token'));
  }
  ngAfterViewInit(): void {
    this.render(0);
  }
  private gettext() {
    return this.Answer;
  }

  componentMount() {
    setInterval(async () => {
      const session: Session = JSON.parse(localStorage.getItem('token'));
      if (session == null) {
          return;
      }
      const response = await authorizedBackendApi('history/get', session);
      if (!response.ok) {
        if (response.status === 401) {
          BackendApiToaster(this.toaster, Promise.resolve(response));
        }

        return ;
      }
      this.history = await response.json();
      this.Value  = this.history.map(query => ({...query, result: query.result ? 'Есть пробитие' : 'Наводчик контужен'})).reverse();
    }, 1000);

    this.toaster.info('А вы знали?', 'мы запустили свой язык программирования Нажмите на вариант');
  }
  private show() {
    this.display = true;
  }
  private hide() {
    this.display = false;
  }
  private sendData() {
    Locker(async () => {
      const code = this.Code.controls['code'].value;
      const response = await BackendApiToaster(this.toaster, authorizedBackendApi('session/programming',
           this.session, 'POST', {code}));
      if (response.ok) {
        this.Answer = await response.text();
      }
    });
  }


  public mouseMove(event: MouseEvent): void {

    const canvas = this.canvas.nativeElement.getContext('2d');

      const offsetLeft = parseInt(getCurrentStyle(canvas.canvas, 'border-left-width'), 10);
      const  offsetTop = parseInt(getCurrentStyle(canvas.canvas, 'border-top-width'), 10);

      const rect = canvas.canvas.getBoundingClientRect();
      const x = Math.ceil(event.clientX - rect.left - offsetLeft) / this.canvasScale - this.canvasTranslate.x;
      const y = (event.clientY - rect.top - offsetTop) / this.canvasScale - this.canvasTranslate.y;

      if (x < 0 || x >= CANVAS_WIDTH || y < 0 || y >= CANVAS_HEIGHT) {
        return;
      }

      const centerX = CANVAS_WIDTH / 2;
      const centerY = CANVAS_HEIGHT / 2;

      const zoomX = CANVAS_WIDTH / 14;
      const zoomY = CANVAS_HEIGHT / 14;
      this.mouse.x = (x - centerX) / zoomX;
      this.mouse.y = (centerY - y) / zoomY;
      this.render(this.areaform.get('R').value);
  }
  private onChangesR() {
    this.areaform.get('R').valueChanges.subscribe(value => {
        this.render(value);
      // console.log(value);
    });
  }
  private onCheck() {
    this.submit(null, null);
  }

  private submit(x: string, y: string) {
    if (localStorage.getItem('token') != null) {
      if (x == null) {
         x = this.areaform.controls['X'].value;
      }
        if (y == null) {
          y = this.areaform.controls['Y'].value;
        }
        const r = this.areaform.controls['R'].value;

        const local = localStorage.getItem('token');
        const session: Session = JSON.parse(local);
        this.onsubmitQuery(x, y, r, session, result => {

          if (this.history.length > 0) {
            const lastQuery = this.history[this.history.length - 1];

            if (lastQuery.x === x && lastQuery.y === y && lastQuery.r === r) {
              return;
            }

          }
          // console.log(`x: ${x} y: ${y} r: ${r} result: ${result}`);
          this.history.push({x, y, r, result});
          this.Value  = this.history.map(query => ({...query, result: query.result ? 'Есть пробитие' : 'Наводчик контужен'})).reverse();
        } );

    }
  }
  private onsubmitQuery(x, y, r, session: Session, addpoint: (result: boolean) => void): void {
    Locker(async () => {
      const response = await BackendApiToaster(this.toaster,
          authorizedBackendApi('area/check', session, 'POST', {x, y, r}));

      if (response.ok) {
        return;
      } else if (response.status === 400) {
        this.toaster.error('Неправильное значение!', 'Одно из введённых значений не попадает в границы');
      }
      addpoint(await response.json());
    });
  }

  private onChanges(): void {
    this.areaform.valueChanges.subscribe(value => {
      this.FormPoint.x = value.x;
      this.FormPoint.y = value.y;
      this.FormPoint.r = value.r;
    });

  }
  componentUpdate() {
    if (Date.now() - this.previousFormPointRequest > 100 &&
        (this.previousR !== this.FormPoint.r || this.previousFormPoint !== this.FormPoint)) {
      this.previousR = this.FormPoint.r;
      this.previousFormPoint = this.FormPoint;
      this.previousFormPointRequest = Date.now();
      this.formPointResult = null;
    }
    this.checkFormPoint();
  }
  public mouseClick(): void {
    this.submit(`${this.mouse.x}`, `${this.mouse.y}`);
  }
  public mouseLeave(): void {
    this.isMouseHover = false;
    this.render(this.areaform.get('R').value);

  }
  public mouseDown(): void {

  }
  public mouseOver(): void {
    this.isMouseHover = true;
  }
  private render(scaling: number): void {
    const canvas =  this.canvas.nativeElement.getContext('2d');
    const scale = scaling;

    const actualCanvasSize = {
      width: parseInt(getCurrentStyle(canvas.canvas, 'width'), 10),
      height: parseInt(getCurrentStyle(canvas.canvas, 'height'), 10)
    };

    canvas.globalAlpha = 1;
    canvas.resetTransform();
    canvas.clearRect(0, 0, canvas.canvas.width, canvas.canvas.height);

    const canvasScale = this.canvasScale = Math.min(
        actualCanvasSize.width / CANVAS_WIDTH,
        actualCanvasSize.height / CANVAS_HEIGHT
    );
    canvas.scale(canvasScale, canvasScale);


     const canvasTranslate = this.canvasTranslate = {
      x: (actualCanvasSize.width / canvasScale - CANVAS_WIDTH) / 2,
      y: (actualCanvasSize.height / canvasScale - CANVAS_HEIGHT) / 2
    };
     canvas.translate(canvasTranslate.x, canvasTranslate.y);

    canvas.strokeStyle = CANVAS_COLOR_PRIMARY;
    canvas.fillStyle = CANVAS_COLOR_BACKGROUND;
    canvas.font = `bold ${CANVAS_STEP_X / 2}px 'Courier New', monospace`;
    canvas.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

    canvas.beginPath();
    canvas.rect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    canvas.clip();

    // фигурки рисуются здесь
    const RCanvas = +scale;
    const halfR = +scale / 2;
    canvas.fillStyle = '#349eeb';

    canvas.beginPath();
    canvas.moveTo(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 );
    canvas.lineTo(CANVAS_WIDTH / 2 , CANVAS_HEIGHT / 2 + CANVAS_STEP_Y * halfR);
    canvas.lineTo(CANVAS_WIDTH / 2 - CANVAS_STEP_X * RCanvas, CANVAS_HEIGHT / 2 + CANVAS_STEP_Y * halfR);
    canvas.lineTo(CANVAS_WIDTH / 2 - CANVAS_STEP_X * RCanvas, CANVAS_HEIGHT / 2);
    canvas.lineTo(CANVAS_WIDTH / 2 - CANVAS_STEP_X * halfR, CANVAS_HEIGHT / 2 );
    canvas.lineTo(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - CANVAS_STEP_Y * halfR);
    canvas.lineTo(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 );
    canvas.lineTo(CANVAS_WIDTH / 2 + CANVAS_STEP_X * RCanvas , CANVAS_HEIGHT / 2);
    canvas.arcTo(CANVAS_WIDTH / 2 + CANVAS_STEP_X * RCanvas,
        CANVAS_HEIGHT / 2 + CANVAS_STEP_Y * RCanvas,
        CANVAS_WIDTH / 2,
        CANVAS_HEIGHT / 2 + CANVAS_STEP_Y * RCanvas,
        Math.abs((CANVAS_STEP_X + CANVAS_STEP_Y) / 2 * RCanvas));
    canvas.fill();

    // рендер линий
    let  r, mr, pr, mpr;
    if (Number.parseInt(String(scale), 10) !== 0) {
      r = scale;
      mr = -scale;
      pr = scale / 2;
      mpr = -pr ;
    } else {
      r = 'R';
      mr = '-R';
      pr = 'R/2';
      mpr = '-R/2' ;
    }
    canvas.beginPath();
    canvas.moveTo(CANVAS_STEP_X / 2, CANVAS_HEIGHT / 2);
    canvas.lineTo(CANVAS_WIDTH - CANVAS_STEP_X / 2, CANVAS_HEIGHT / 2);
    canvas.moveTo(CANVAS_WIDTH - CANVAS_STEP_X, CANVAS_HEIGHT / 2 - CANVAS_STEP_Y / 4);
    canvas.lineTo(CANVAS_WIDTH - CANVAS_STEP_X / 2, CANVAS_HEIGHT / 2);
    canvas.lineTo(CANVAS_WIDTH - CANVAS_STEP_X, CANVAS_HEIGHT / 2 + CANVAS_STEP_Y / 4);

    canvas.moveTo(CANVAS_WIDTH / 2, CANVAS_HEIGHT - CANVAS_STEP_Y / 2);
    canvas.lineTo(CANVAS_WIDTH / 2, CANVAS_STEP_X / 2);
    canvas.moveTo(CANVAS_WIDTH / 2 - CANVAS_STEP_X / 4, CANVAS_STEP_Y);
    canvas.lineTo(CANVAS_WIDTH / 2, CANVAS_STEP_Y / 2);
    canvas.lineTo(CANVAS_WIDTH / 2 + CANVAS_STEP_X / 4, CANVAS_STEP_Y);

    // Чёрточки
    canvas.moveTo(CANVAS_WIDTH / 2 - CANVAS_STEP_X * 5, CANVAS_HEIGHT / 2 - 5);
    canvas.lineTo(CANVAS_WIDTH / 2 - CANVAS_STEP_X * 5, CANVAS_HEIGHT / 2 + 5);

    canvas.moveTo(CANVAS_WIDTH / 2 - CANVAS_STEP_X * 5 / 2, CANVAS_HEIGHT / 2 - 5);
    canvas.lineTo(CANVAS_WIDTH / 2 - CANVAS_STEP_X * 5 / 2, CANVAS_HEIGHT / 2 + 5);

    canvas.moveTo(CANVAS_WIDTH / 2 + CANVAS_STEP_X * 5 / 2, CANVAS_HEIGHT / 2 - 5);
    canvas.lineTo(CANVAS_WIDTH / 2 + CANVAS_STEP_X * 5 / 2, CANVAS_HEIGHT / 2 + 5);

    canvas.moveTo(CANVAS_WIDTH / 2 + CANVAS_STEP_X * 5, CANVAS_HEIGHT / 2 - 5);
    canvas.lineTo(CANVAS_WIDTH / 2 + CANVAS_STEP_X * 5, CANVAS_HEIGHT / 2 + 5);

    canvas.moveTo(CANVAS_WIDTH / 2 - 5, CANVAS_HEIGHT / 2 - CANVAS_STEP_Y * 5);
    canvas.lineTo(CANVAS_WIDTH / 2 + 5, CANVAS_HEIGHT / 2 - CANVAS_STEP_Y * 5);

    canvas.moveTo(CANVAS_WIDTH / 2 - 5, CANVAS_HEIGHT / 2 - CANVAS_STEP_Y * 5 / 2);
    canvas.lineTo(CANVAS_WIDTH / 2 + 5, CANVAS_HEIGHT / 2 - CANVAS_STEP_Y * 5 / 2);

    canvas.moveTo(CANVAS_WIDTH / 2 - 5, CANVAS_HEIGHT / 2 + CANVAS_STEP_Y * 5 / 2);
    canvas.lineTo(CANVAS_WIDTH / 2 + 5, CANVAS_HEIGHT / 2 + CANVAS_STEP_Y * 5 / 2);

    canvas.moveTo(CANVAS_WIDTH / 2 - 5, CANVAS_HEIGHT / 2 + CANVAS_STEP_Y * 5 );
    canvas.lineTo(CANVAS_WIDTH / 2 + 5, CANVAS_HEIGHT / 2 + CANVAS_STEP_Y * 5 );

    canvas.font = '20px Arial';
    canvas.textAlign = 'center';
    canvas.textBaseline = 'top';
    canvas.fillStyle = CANVAS_COLOR_PRIMARY;
    canvas.fillText(r.toString(), CANVAS_WIDTH / 2 + CANVAS_STEP_X * 5, CANVAS_HEIGHT / 2 - 25);
    canvas.fillText(pr.toString(), CANVAS_WIDTH / 2 + CANVAS_STEP_X * 5 / 2, CANVAS_HEIGHT / 2 - 25);
    canvas.fillText(mr.toString(), CANVAS_WIDTH / 2 - CANVAS_STEP_X * 5, CANVAS_HEIGHT / 2 - 25);
    canvas.fillText(mpr.toString(), CANVAS_WIDTH / 2 - CANVAS_STEP_X * 5 / 2, CANVAS_HEIGHT / 2 - 25);
    canvas.fillText(r.toString(), CANVAS_WIDTH / 2 + 10, CANVAS_HEIGHT / 2  - CANVAS_STEP_Y * 5);
    canvas.fillText(pr.toString(), CANVAS_WIDTH / 2 + 20, CANVAS_HEIGHT / 2 - CANVAS_STEP_Y * 5 / 2);
    canvas.fillText(mr.toString(), CANVAS_WIDTH / 2 + 10, CANVAS_HEIGHT / 2  + CANVAS_STEP_Y * 5);
    canvas.fillText(mpr.toString(), CANVAS_WIDTH / 2 + 20, CANVAS_HEIGHT / 2 + CANVAS_STEP_Y * 5 / 2);
    canvas.stroke();

    // рендер истории точек
    canvas.lineWidth = 0.5;

    const centerX = CANVAS_WIDTH / 2;
    const centerY = CANVAS_HEIGHT / 2;
    this.history.forEach((point: History) => {
      canvas.globalAlpha = 1 - Math.min(point.r !== this.FormPoint.r ? Math.abs(+this.FormPoint.r - +point.r) / 3 : 0.9);
      canvas.fillStyle = point.r !== this.FormPoint.r
          ? CANVAS_COLOR_POINT_OTHER
          : point.result ?
              CANVAS_COLOR_POINT_INCLUDES :
              CANVAS_COLOR_POINT_NOT_INCLUDES
      ;
      canvas.beginPath();
      canvas.arc(
          centerX + +point.x * CANVAS_STEP_X,
          centerY - +point.y * CANVAS_STEP_Y,
          3, 0, Math.PI * 2
      );
      canvas.fill();
      canvas.stroke();
    });

    canvas.globalAlpha = 1;
    canvas.fillStyle = CANVAS_COLOR_BACKGROUND;

      // рендер текующей формы точки
    canvas.beginPath();
    canvas.moveTo(CANVAS_WIDTH / 2 + +this.previousFormPoint.x * CANVAS_STEP_X, CANVAS_HEIGHT);
    canvas.lineTo(CANVAS_WIDTH / 2 + +this.previousFormPoint.x * CANVAS_STEP_X, 0);
    canvas.stroke();

    if (this.FormPoint.y != null) {
      canvas.beginPath();
      canvas.moveTo(0, CANVAS_HEIGHT / 2 - +this.previousFormPoint.y * CANVAS_STEP_Y);
      canvas.lineTo(CANVAS_WIDTH, CANVAS_HEIGHT / 2 - +this.previousFormPoint.y * CANVAS_STEP_Y);
      canvas.stroke();

      canvas.fillStyle = this.formPointResult == null ?
          CANVAS_COLOR_POINT_OTHER :
          this.formPointResult ?
              CANVAS_COLOR_POINT_INCLUDES :
              CANVAS_COLOR_POINT_NOT_INCLUDES
      ;
      canvas.beginPath();
      canvas.arc(
          CANVAS_WIDTH / 2 + +this.previousFormPoint.x * CANVAS_STEP_X,
          CANVAS_HEIGHT / 2 - +this.previousFormPoint.y * CANVAS_STEP_Y,
          3, 0, Math.PI * 2
      );
      canvas.fill();
      canvas.stroke();
    }
    canvas.fillStyle = CANVAS_COLOR_BACKGROUND;

      // и наконец, позиция мышки
    if (this.isMouseHover && this.mouse != null) {
      const mouseXLabelText = `X: ${+this.mouse.x.toFixed(5)}`;
      const mouseYLabelText = `Y: ${+this.mouse.y.toFixed(5)}`;

      const mouseLabelWidth = Math.max(
          canvas.measureText(mouseXLabelText).width,
          canvas.measureText(mouseYLabelText).width
      );
      const mouseRightSide = Math.max(
          Math.floor((CANVAS_STEP_X * 1.5 + mouseLabelWidth) / CANVAS_STEP_X * 2) * CANVAS_STEP_X * 2,
          CANVAS_STEP_X * 3.5
      );
      canvas.beginPath();
      canvas.moveTo(CANVAS_STEP_X / 2, CANVAS_STEP_Y * 0.75);
      canvas.lineTo(mouseRightSide, CANVAS_STEP_Y * 0.75);
      canvas.lineTo(mouseRightSide, CANVAS_STEP_Y * 2.25);
      canvas.lineTo(CANVAS_STEP_X / 2, CANVAS_STEP_Y * 2.25);
      canvas.lineTo(CANVAS_STEP_X / 2, CANVAS_STEP_Y * 0.75);
      canvas.fill();
      canvas.stroke();

      canvas.fillStyle = CANVAS_COLOR_PRIMARY;
      canvas.fillText(mouseXLabelText, CANVAS_STEP_X * 0.75, whereMeDrawText(canvas, CANVAS_STEP_Y * 0.75));
      canvas.fillText(mouseYLabelText, CANVAS_STEP_Y * 0.75, whereMeDrawText(canvas, CANVAS_STEP_Y * 1.25));
      canvas.fillStyle = CANVAS_COLOR_BACKGROUND;
    }
  }
  private async checkFormPoint() {

    if (this.FormPoint.y != null && this.FormPoint.y.trim().length > 0 && this.session != null) {
      const response = await BackendApiToaster(this.toaster,
          authorizedBackendApi(`area/check/r/${this.FormPoint.r}/x/${this.FormPoint.x}/y/${this.FormPoint.y}`, this.session)
      );

      if (response.ok && this.previousFormPoint === this.FormPoint) {
         this.formPointResult = await response.json();
      }
    }
  }

}
function getCurrentStyle(element: HTMLElement, style1: string) {
  try {
    return window.getComputedStyle(element, null).getPropertyValue(style1);
  } catch (e) {
    return (element as { currentStyle?: { [key: string]: string } }).currentStyle![style1];
  }
}
function whereMeDrawText(context: CanvasRenderingContext2D, topY: number, height: number = CANVAS_STEP_Y) {
  return (height + context.measureText('M').width) / 2 + topY;
}
