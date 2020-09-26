(function () {

    //Place for page elements

    //Error field that SetError will change
    let error = document.getElementById("err-msg");
    let ErrorText= document.getElementById("text");

    let form = document.getElementById("form");
    let Xm = document.querySelectorAll("input[name='X[]']");
    let Ym = document.getElementById("Y");
    let Rm =document.getElementById("R");


    let submitButton = document.getElementById('submit-button');
    let canvas = document.getElementById("graph-canvas");
    let graphPicker = new GraphPicker(canvas);

    let buffer = [];
    const rightBuffer = ["ArrowUp", "ArrowUp", "ArrowDown", "ArrowDown", "ArrowLeft", "ArrowRight", "ArrowLeft", "ArrowRight", "b", "a"];

    //Listeners
    form.addEventListener("click",onSubmit);
    graphPicker.setListener(onGraphClicked);
    Ym.addEventListener("input",onYChanged);
    for (let i=0;i < Xm.length;i++){
        Xm[i].addEventListener("change",onXChanged);
    }


    Rm.addEventListener("keyup",setR);
    Rm.addEventListener("keypress",setR);

    document.addEventListener('keydown',event=>{
        const key=event.key;
        buffer.push(key);
        for (let i=0;i<buffer.length;i++) {
            if (!((rightBuffer[i] === buffer[i]) && buffer.length <= rightBuffer.length)) {
                buffer = [];
                break;
            }
        }
        if (buffer.length === rightBuffer.length){
            let audio = new Audio();
            audio.src = "./Files/misc/misc.mp3";
            audio.autoplay = true;
            audio.volume = 0.2;
            setError("Start flexing  (╯°□°)╯ ┻━━┻");
        }
    });

    //Place for Consts to graph check
    const ALLXS = [-3,-2,-1,0,1,2,3,4,5];

    const MIN_Y = -5;
    const MAX_Y = 5;

    const MIN_R = 1;
    const MAX_R = 4;
    //-----------------

    function onSubmit(event) {
        if (!(checkX() && checkY() && checkR())) {
            event.preventDefault();
        }
    }
    function checkY() {
        Ym.value = Ym.value.trim();

        if (Ym.value.length === 0){
            setError("Задайте явно Y");
            return false;
        }

        if (isNaN(Ym.value.replace(",","."))){
            setError("В поле Y надо ввести число");
            return false;
        }
        let value = +Ym.value;
        if (value <= MIN_Y || value >= MAX_Y){
            setError("Значение Y выходит за границы");
            return false
        }
        return true;

    }
    function checkX() {
        for (let i = 0;i<Xm.length;i++){
            if (Xm[i].checked){
                if (ALLXS.indexOf(+Xm[i].value)=== -1){
                    setError("Пожалуйста, не ломайте сайт");
                    return false;
                }
                return true;
            }
        }
        setError("Задайте значение x");
        return false;

    }
    function onXChanged() {
        graphPicker.setX(this.value);
        Xm.value = this.value;
    }
    function onYChanged() {
        let text = Ym.value;
        if (!isNaN(text))
            graphPicker.setY(+text);
    }
    
    function onGraphClicked(x,y) {
        if (x == null || y == null)
             return setError("Задайте значение R");
        x =Math.round(x);
        let selected = document.getElementById(`x${x}`);
        selected.checked = true;
        //console.log(selected.checked);
        //selected.setAttribute("state-selected","");
        //console.log(i);
        //console.log(x);
        //Xm[i].setAttribute("state-selected","");
        Ym.value = y;
        submitButton.click();
    }

    function setR() {
        if (checkR()){
            graphPicker.setScale(+Rm.value.replace(",","."));
            setError(null);
        }else
            console.log("|"+Rm.value);

    }
    function checkR() {
        Rm.value = Rm.value.trim();
        if (isNaN(Rm.value.replace(",","."))){
                setError("В поле R должно быть число");
                return false;
        }
        let val = +Rm.value;
        if ((val <=MIN_R || val >= MAX_R) && Rm.value !=="" ){
            setError("значение R выходит за границы");
            return false;
        }
        setError(null);
        return true;
    }

    function setError(message) {
        ErrorText.innerText=message;
        if (message!=null){
            error.classList.remove("hidden");
        }
        else {
            error.classList.add("hidden")
        }
    }






})();