(function () {
    let form = document.getElementById("form");
    let error = document.getElementById("err-msg");
    let Xm = document.querySelectorAll("input[name='X[]']");
    let Ym = document.getElementById("Y");
    let Rm =document.getElementById("R");
    let error1= document.getElementById("text");

    const ALLXS = [-3,-2,-1,0,1,2,3,4,5];

    const MIN_Y = -3;
    const MAX_Y = 5;

    const MIN_R = 2;
    const MAX_R = 5;

    form.addEventListener("submit", onSubmit());
    document.addEventListener("load", keyChecker());
    form.addEventListener("submit",onSubmit);

    
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
    function onSubmit(event) {
        if (!(checkX() && checkY() && checkR())) {
            event.preventDefault();
        }
    }
    
    function setError(message) {
           error1.innerText=message;
           if (message!=null){
               error.classList.remove("hidden");
           }
           else {
               error.classList.add("hidden")
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
    function checkR() {
        Rm.value = Rm.value.trim();

        if (Rm.value.length === 0){
            setError("Задайте явно R");
            return false;
        }

        if (isNaN(Rm.value.replace(",","."))){
            setError("В поле R надо ввести число");
            return false;
        }
        let value = +Rm.value;
        if (value <=MIN_R || value >= MAX_R){
            setError("Значение R выходит за границы");
            return false;
        }
        return true;
    }
    function keyChecker() {
        let buffer = [];
        const rigtBuffer = ["ArrowUp", "ArrowUp", "ArrowDown", "ArrowDown", "ArrowLeft", "ArrowRight", "ArrowLeft", "ArrowRight", "b", "a"];

        document.addEventListener('keydown',event=>{
           const key=event.key;
            buffer.push(key);
           for (let i=0;i<buffer.length;i++){
             if (!((rigtBuffer[i] ===buffer[i]) && buffer.length <=rigtBuffer.length)){
                 buffer = [];
                 break;
             }
           }
           if (buffer.length === rigtBuffer.length){
               let audio = new Audio();
               audio.src = "./misc/misc.mp3";
               audio.autoplay = true;
               audio.volume = 0.2;
               setError("Start flexing  (╯°□°)╯ ┻━━┻");
           }
        });
    }
})();