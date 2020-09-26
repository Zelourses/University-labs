(function () {
    let elem;
    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }
    // elem = document.getElementById("my-button");
    let elems = document.getElementsByClassName("running");
    for (let i =0;i<elems.length;i++){
        elems[i].onmousemove = handler
    }
    function handler() {
        this.style.left =  getRandomInt(0,document.documentElement.clientWidth-this.offsetWidth)+'px';
        this.style.top = getRandomInt(0,document.documentElement.clientHeight-this.offsetHeight)+'px';
    }
})();