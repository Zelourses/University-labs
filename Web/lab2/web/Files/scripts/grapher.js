(function () {
    window.GraphPicker = function(canvas) {
        let width = canvas.width;
        let height = canvas.height;
        let scale = null;
        let pointer = null;
        let canvas2D = canvas.getContext('2d');

        let listener = () => {};

        let mouseDownFlag = false;

        canvas.addEventListener("click", onClick);
        canvas.addEventListener("mousemove", onDrag);
        canvas.addEventListener("mousedown", () => {mouseDownFlag = true});
        canvas.addEventListener("mouseup", () => {mouseDownFlag = false});
        canvas.addEventListener("mouseout", () => {mouseDownFlag = false; render()});
        canvas2D.mv = function(x, y) { this.moveTo(x*width, y*height); };
        canvas2D.ln = function(x, y) { this.lineTo(x*width, y*height); };
        canvas2D.txt = function(text, x, y) { this.fillText(text, x*width, y*height); };
        render();

        this.setListener = setListener;
        this.setScale = setScale;
        this.setX = setX;
        this.setY = setY;

        function onClick(event) {
            if (!scale)
                return listener(null, null);
            let {layerX: elemX, layerY: elemY} = event;
            let {scrollWidth: maxX, scrollHeight: maxY} = canvas;
            let normalized = {x: elemX/maxX, y: elemY/maxY};
            pointer = normalizedToGraphCoords(normalized);
            render();
            listener(Math.round(pointer.x*1000)/1000, Math.round(pointer.y*1000)/1000);
        }

        function onDrag(event) {
            if (!scale || !mouseDownFlag)
                return;
            let {layerX: elemX, layerY: elemY} = event;
            let {scrollWidth: maxX, scrollHeight: maxY} = canvas;
            let normalized = {x: elemX/maxX, y: elemY/maxY};
            pointer = normalizedToGraphCoords(normalized);
            render();
        }

        function render() {
            canvas2D.clearRect(0, 0, width, height);

            canvas2D.fillStyle = "#0036eb";
            canvas2D.fillRect(0.5*width, 0.5*height, 0.2*width, 0.4*height);
            canvas2D.beginPath();
            canvas2D.mv(.5, .5);
            canvas2D.ln(.5, .9);
            canvas2D.ln(.5, .5);

            canvas2D.arc(0.5*width, 0.5*height, .2*(width+height), -Math.PI, -Math.PI/2,false);
            canvas2D.mv(.1, .5);
            canvas2D.ln(.5, .7);
            canvas2D.ln(.5, .5);
            canvas2D.fill();

            canvas2D.lineWidth = 3;
            canvas2D.strokeStyle = "black";
            canvas2D.fillStyle = "black";

            canvas2D.beginPath();
            canvas2D.mv(0, .5);
            canvas2D.ln(1, .5);
            canvas2D.ln(.97, .48);
            canvas2D.mv(1, .5);
            canvas2D.ln(.97, .52);

            canvas2D.mv(.1, .49);
            canvas2D.ln(.1, .51);
            canvas2D.mv(.3, .49);
            canvas2D.ln(.3, .51);
            canvas2D.mv(.7, .49);
            canvas2D.ln(.7, .51);
            canvas2D.mv(.9, .49);
            canvas2D.ln(.9, .51);

            canvas2D.mv(.5, 1);
            canvas2D.ln(.5, 0);
            canvas2D.ln(.48, .03);
            canvas2D.mv(.5, 0);
            canvas2D.ln(.52, .03);

            canvas2D.mv(.49, .9);
            canvas2D.ln(.51, .9);
            canvas2D.mv(.49, .7);
            canvas2D.ln(.51, .7);
            canvas2D.mv(.49, .3);
            canvas2D.ln(.51, .3);
            canvas2D.mv(.49, .1);
            canvas2D.ln(.51, .1);

            canvas2D.font = "48px Arial";
            canvas2D.textAlign = "center";
            canvas2D.textBaseline = "top";
            canvas2D.txt(scale ? -scale : "-R", .1, .52);
            canvas2D.txt(scale ? -scale/2 : "-R/2", .3, .52);
            canvas2D.txt(scale ? scale/2 : "R/2", .7, .52);
            canvas2D.txt(scale ? scale : "R", .9, .52);
            canvas2D.txt("x", .97, .52);

            canvas2D.textAlign = "left";
            canvas2D.textBaseline = "middle";
            canvas2D.txt(scale ? -scale : "-R", .52, .9);
            canvas2D.txt(scale ? -scale/2 : "-R/2", .52, .7);
            canvas2D.txt(scale ? scale/2 : "R/2", .52, .3);
            canvas2D.txt(scale ? scale : "R", .52, .1);
            canvas2D.txt("y", .52, .03);
            canvas2D.stroke();

            if (pointer) {
                canvas2D.fillStyle = "#ff0000";
                canvas2D.strokeStyle = "#ff0000";

                let {x, y} = graphToNormalizedCoords(pointer);

                canvas2D.beginPath();
                canvas2D.arc(x*width, y*height, .005*(width+height)/2, 0, 2*Math.PI);
                canvas2D.fill();

                canvas2D.beginPath();
                canvas2D.mv(x, y + .01);
                canvas2D.ln(x, y + .02);
                canvas2D.mv(x, y - .01);
                canvas2D.ln(x, y - .02);
                canvas2D.mv(x + .01, y);
                canvas2D.ln(x + .02, y);
                canvas2D.mv(x - .01, y);
                canvas2D.ln(x - .02, y);
                canvas2D.stroke();

                if (mouseDownFlag) {
                    canvas2D.fillStyle = "rgba(228,0,255,0.75)";
                    canvas2D.font = "24px Arial";
                    canvas2D.textAlign = "left";
                    canvas2D.textBaseline = "bottom";

                    let text = `x: ${pointer.x.toFixed(2)} y: ${pointer.y.toFixed(2)}`;
                    canvas2D.fillRect(x*width + 10, y*height - 10, 35 + 9*text.length, -50);
                    canvas2D.fillStyle = "white";
                    canvas2D.fillText(text, x*width + 20, y*height - 20)
                }
            }
        }

        function normalizedToGraphCoords(point) {
            return {x: scale*(point.x-.5)/.4, y: -scale*(point.y-.5)/.4}
        }

        function graphToNormalizedCoords(point) {
            return {x: (point.x*0.4/scale+0.5), y: (-point.y*0.4/scale+0.5)}
        }

        function setX(x) {
            if (!pointer)
                pointer = {x: 0, y: 0};
            pointer.x = x;
            render();
        }

        function setY(y) {
            if (!pointer)
                pointer = {x: 0, y: 0};
            pointer.y = y;
            render();
        }

        function setScale(s) {
            if (!s)
                return;
            scale = s;
            render();
        }

        function setListener(l) {
            listener = l;
        }
    }
})();