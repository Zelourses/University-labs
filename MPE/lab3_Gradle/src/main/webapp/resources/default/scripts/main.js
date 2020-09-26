(function () {
    const ACCEPTED_RS = [1, 1.5, 2, 2.5, 3];
    const MIN_Y = -5;
    const MAX_Y = 3;

    let canvasWrapper = document.getElementById('canvas-wrapper');

    let yInput = document.getElementById('form:y');
    let xHidden = document.getElementById('form:x-hidden');
    let submitButton = document.getElementById('form:submit-btn');

    let errMsgPanel = document.getElementById('err-msg');
    let graphPicker = new GraphPicker(canvasWrapper, 1000, 1000);

    let savedR = null;

    let selectedXs = [];

    graphPicker.setListener(onGraphClicked);

    function setErrorMsg(msg) {
        errMsgPanel.innerText = msg;
        if (msg != null)
            errMsgPanel.classList.remove('hidden');
        else
            errMsgPanel.classList.add('hidden');
        return false;
    }

    window.onSubmit = onSubmit;
    function onSubmit() {
        return checkX() && checkY() && checkR();
    }

    function onGraphClicked(x, y) {
        if (x == null || y == null)
            return setErrorMsg('Выберите R, чтобы выбрать точку на графике');

        xHidden.value = x;
        yInput.value = y;
        submitButton.click();
    }

    window.onYChanged = onYChanged;
    function onYChanged(value) {
        if (!isNaN(value)) {
            if (value <= MIN_Y || value >= MAX_Y)
                return setErrorMsg('Недопустимое значение Y');
            graphPicker.setY(value);
        }
    }

    this.selectedX = selectedX;
    function selectedX(value) {
        if (!isNaN(value)) {
            selectedXs.push(value);
        }
        if (selectedXs.length === 1)
            onXChanged(selectedXs[0]);
    }

    this.deselectedX = deselectedX;
    function deselectedX(value) {
        let index = selectedXs.indexOf(value);
        if (index !== -1) {
            selectedXs.splice(index, 1);
        }
        if (selectedXs.length === 1)
            onXChanged(selectedXs[0]);
    }

    function onXChanged(value) {
        if (!isNaN(value)) {
            graphPicker.setX(value);
            xHidden.value = value;
        } else {
            graphPicker.setX(null);
            xHidden.value = '';
        }
    }

    window.onRChanged = onRChanged;
    function onRChanged(r) {
        if (ACCEPTED_RS.indexOf(+r) === -1)
            return setErrorMsg('Недопустимое значение R');
        graphPicker.setScale(r);
        savedR = r;
        setErrorMsg(null);
    }

    function checkX() {
        let value = xHidden.value.trim();
        if (value.length === 0)
            return setErrorMsg('Следует выбрать X');
        if (selectedXs.length > 1)
            return setErrorMsg('Выберите один X');
        if (isNaN(value))
            return setErrorMsg('HACKING ATTEMPT');
        return true;
    }

    function checkY() {
        let value = yInput.value.trim();
        if (value.length === 0)
            return setErrorMsg('Следует ввести Y');
        if (isNaN(value))
            return setErrorMsg('Y должен быть числом');
        if (value.length > 16)
            return setErrorMsg('Y должен содержать не более 16 символов');
        let number = +value;
        if (number <= MIN_Y || number >= MAX_Y)
            return setErrorMsg(`Y должен быть больше ${MIN_Y} и меньше ${MAX_Y}`);
        return true;
    }

    function checkR() {
        if (savedR == null)
            return setErrorMsg('Выберите R');
        if (isNaN(savedR))
            return setErrorMsg('каво');
        if (ACCEPTED_RS.indexOf(+savedR) === -1)
            return setErrorMsg('Недопустимое значение R');
        return true;
    }
})();