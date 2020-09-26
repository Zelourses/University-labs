(function () {
    "use strict";
    console.log("зочем вам стили?");

    window.GraphPicker = function(wrapper, width, height) {
        let canvas = document.createElement("canvas");
        canvas.setAttribute("width", width);
        canvas.setAttribute("height", height);

        let oldPoints = [];
        let oldPointEls = wrapper.querySelectorAll("*");
        for (let i = 0; i < oldPointEls.length; i++) {
            let x = oldPointEls[i].getAttribute("data-x");
            let y = oldPointEls[i].getAttribute("data-y");
            let hit = oldPointEls[i].getAttribute("data-hit");

            oldPoints.push({x, y, hit: hit === "true"});
        }
        wrapper.innerHTML = "";

        wrapper.append(canvas);

        let scale = null;
        let pointer = null;
        let ctx = canvas.getContext('2d');

        let listener = () => {};

        let mouseDownFlag = false;

        canvas.addEventListener("click", onClick);
        canvas.addEventListener("mousemove", onDrag);
        canvas.addEventListener("mousedown", () => {mouseDownFlag = true});
        canvas.addEventListener("mouseup", () => {mouseDownFlag = false});
        canvas.addEventListener("mouseout", () => {mouseDownFlag = false; render()});
        ctx.mv = function(x, y) { this.moveTo(x*width, y*height); };
        ctx.ln = function(x, y) { this.lineTo(x*width, y*height); };
        ctx.txt = function(text, x, y) { this.fillText(text, x*width, y*height); };
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
            ctx.clearRect(0, 0, width, height);

            ctx.fillStyle = "#349eeb";
            ctx.fillRect(0.1*width, 0.5*height, 0.4*width, 0.2*height);
            ctx.beginPath();
            ctx.mv(.5, .5);
            ctx.ln(.5, .3);
            ctx.ln(.9, .5);
            ctx.arc(0.5*width, 0.5*height, .4*(width+height)/2, Math.PI, 3*Math.PI/2);
            ctx.fill();

            ctx.lineWidth = 3;
            ctx.strokeStyle = "black";
            ctx.fillStyle = "black";

            ctx.beginPath();
            ctx.mv(0, .5);
            ctx.ln(1, .5);
            ctx.ln(.97, .48);
            ctx.mv(1, .5);
            ctx.ln(.97, .52);

            ctx.mv(.1, .49);
            ctx.ln(.1, .51);
            ctx.mv(.3, .49);
            ctx.ln(.3, .51);
            ctx.mv(.7, .49);
            ctx.ln(.7, .51);
            ctx.mv(.9, .49);
            ctx.ln(.9, .51);

            ctx.mv(.5, 1);
            ctx.ln(.5, 0);
            ctx.ln(.48, .03);
            ctx.mv(.5, 0);
            ctx.ln(.52, .03);

            ctx.mv(.49, .9);
            ctx.ln(.51, .9);
            ctx.mv(.49, .7);
            ctx.ln(.51, .7);
            ctx.mv(.49, .3);
            ctx.ln(.51, .3);
            ctx.mv(.49, .1);
            ctx.ln(.51, .1);

            ctx.font = "48px Arial";
            ctx.textAlign = "center";
            ctx.textBaseline = "top";
            ctx.txt(scale ? -scale : "-R", .1, .52);
            ctx.txt(scale ? -scale/2 : "-R/2", .3, .52);
            ctx.txt(scale ? scale/2 : "R/2", .7, .52);
            ctx.txt(scale ? scale : "R", .9, .52);
            ctx.txt("x", .97, .52);

            ctx.textAlign = "left";
            ctx.textBaseline = "middle";
            ctx.txt(scale ? -scale : "-R", .52, .9);
            ctx.txt(scale ? -scale/2 : "-R/2", .52, .7);
            ctx.txt(scale ? scale/2 : "R/2", .52, .3);
            ctx.txt(scale ? scale : "R", .52, .1);
            ctx.txt("y", .52, .03);
            ctx.stroke();

            if (scale) {
                for (let i = 0; i < oldPoints.length; i++) {
                    let current = oldPoints[i];
                    ctx.fillStyle = current.hit ? "green" : "red";
                    let {x, y} = graphToNormalizedCoords(current);
                    ctx.beginPath();
                    ctx.arc(x*width, y*height, .005*(width+height)/2, 0, 2*Math.PI);
                    ctx.fill();
                }
            }

            if (pointer) {
                ctx.fillStyle = "#aa0000";
                ctx.strokeStyle = "#aa0000";

                let {x, y} = graphToNormalizedCoords(pointer);

                ctx.beginPath();
                ctx.arc(x*width, y*height, .005*(width+height)/2, 0, 2*Math.PI);
                ctx.fill();
                ctx.beginPath();

                ctx.mv(x, y + .01);
                ctx.ln(x, y + .02);
                ctx.mv(x, y - .01);
                ctx.ln(x, y - .02);
                ctx.mv(x + .01, y);
                ctx.ln(x + .02, y);
                ctx.mv(x - .01, y);
                ctx.ln(x - .02, y);
                ctx.stroke();
                if (mouseDownFlag) {
                    ctx.fillStyle = "rgba(0, 0, 0, .75)";
                    ctx.font = "24px Arial";
                    ctx.textAlign = "left";
                    ctx.textBaseline = "bottom";

                    let text = `x: ${pointer.x.toFixed(2)} y: ${pointer.y.toFixed(2)}`;
                    ctx.fillRect(x*width + 10, y*height - 10, 35 + 9*text.length, -50);
                    ctx.fillStyle = "white";
                    ctx.fillText(text, x*width + 20, y*height - 20)
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
            if (x != null)
                pointer.x = x;
            else
                pointer = null;
            render();
        }
        function setY(y) {
            if (!pointer)
                pointer = {x: 0, y: 0};
            if (y != null)
                pointer.y = y;
            else
                pointer = null;
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