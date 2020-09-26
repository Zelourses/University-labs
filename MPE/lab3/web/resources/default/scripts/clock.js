let timeNode = document.getElementById('clock');

function getCurrentTimeString() {
    return new Date().toTimeString().replace(/ .*/, '');
}

setInterval(
    () => timeNode.innerHTML = setCurrentDate()+getCurrentTimeString(),
    1000*10
);
function setCurrentDate(){
    let date = new Date();
    return date.getFullYear()+" год, "+date.getMonth()+"месяц, "+date.getDate()+" день\n"
}