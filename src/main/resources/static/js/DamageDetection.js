$(document).ready(function () {
    const thumbnailBody = document.getElementById("thumbnailBody");
    const thumbnailCon = document.getElementById("thumbnailCon");
    const resultImg = document.getElementById("resultImg");
    const popupImg = document.createElement("div");
    const typeDiv = document.createElement("div");
    const typeRed = document.createElement("p");
    const typePlum = document.createElement("p");
    const typeYg = document.createElement("p");
    const sqRed = document.createElement("span");
    const sqPlum = document.createElement("span");
    const sqYg = document.createElement("span");
    const jsonStr = document.getElementById("jsonStr");

    var $panel = $(".popup_panel");
    var $panelContents = $(".popup_contents");
    var $loading = $("#loading");

    let version = 'AI Version: ';

    let obj = {
        filename: [],
        image: [],
        size_img: [],
        no_img: 0
    };

    let bgJsonData = {
        "img_name": [],
        "no_result": 0,
        "results": [],
    }

    const imgW = new Array();
    const imgH = new Array();

    function clearNodes(parentNode) {
        if (parentNode.childNodes.length > 0)
            while (parentNode.hasChildNodes()) parentNode.removeChild(parentNode.firstChild);
    }

    $("#btn_popup_close").on("click", popupClose);
    // 팝업 배경 클릭 이벤트 정의
    $panel.find(".popup_bg").on("click", popupClose);
    function popupClose(e) {
        $panel.fadeOut();
        // 이벤트 기본 동작 중단
        e.preventDefault();
    }

    $("#select").change(function (e) {
        clearNodes(thumbnailCon);
        const files = e.target.files;
        let idx = 0;

        if (files.length > 8) {
            alert("업로드 가능한 사진 개수는 8개 까지 입니다.");
            return;
        }

        thumbnailBody.style.display = "flex";

        obj = {
            filename: [],
            image: [],
            size_img: [],
            no_img: 0
        };
        imgW.splice(0, files.length);
        imgH.splice(0, files.length);

        function readFile(index) {
            $loading.show();
            $("#subtitle").css("display", "block");
            if (index >= files.length) {
                $("#btnup").css("display", "none");
                $("#useage").css("display", "none");
                $("#version").css("display", "none");
                $("#refresh").css("display", "inline-block");
                $("#refresh").click(() => {
                    $("#select").prop("value", "");
                    clearNodes(thumbnailCon);
                    bgJsonData = {
                        "img_name": [],
                        "no_result": 0,
                        "results": [],
                    }
                    $("#subtitle").css("display", "none");
                    $("#thumbnailBody").css("display", "none");
                    $("#jsonStr").css("display", "none");
                    $("#refresh").css("display", "none");
                    $("#btnup").css("display", "inline-block");
                    $("#useage").css("display", "block");
                    $("#version").css("display", "block");

                    obj = {
                        filename: [],
                        image: [],
                        size_img: [],
                        no_img: 0
                    };
                    imgW.splice(0, files.length);
                    imgH.splice(0, files.length);
                });

                $.ajax({
                    type: 'post',
                    url: "http://divus.iptime.org:4201/peoplecar/predict-v2",
                    data: JSON.stringify(obj),
                    dataType: 'json',
                    error: function (xhr, status, error) {
                        $loading.hide();
                        console.log(xhr);
                        alert("에러가 발생했습니다.");
                        location.href = "/";

                    },
                    success: function (json) {
                        console.log(json);
                        $loading.fadeOut("slow");
                        $("#jsonStr").css("display", "block");
                        version += json.ai_version + '\n';
                        for (var i = 0; i < json.no_result; i++) {
                            bgJsonData.img_name.push(obj.filename[i]);
                            bgJsonData.no_result = json.no_result;
                            bgJsonData.results.push(json.results[i]);
                        }
                        jsonStr.innerHTML = version;
                        jsonStr.innerHTML += JSON.stringify(bgJsonData, null, '\t');
                    }
                })
                return;
            }

            const fr = new FileReader();
            fr.readAsDataURL(files[index]);
            fr.onload = function (e) {
                const result = e.target.result;
                const bg = document.createElement("div");

                bg.id = idx;
                bg.style.float = "left";
                bg.style.position = "relative";
                bg.style.background = "url(\'" + result + "\')";
                bg.style.backgroundRepeat = "no-repeat";
                bg.style.backgroundSize = "cover";
                bg.style.width = `${100}px`;
                bg.style.height = `${100}px`;
                bg.style.borderRadius = "5px";
                bg.style.marginTop = "10px";
                bg.style.marginLeft = "10px";
                bg.style.marginBottom = "10px";
                bg.addEventListener("mouseover", function () {
                    bg.setAttribute('class', 'hover');
                });
                bg.addEventListener("mouseout", function () {
                    bg.setAttribute('class', 'mouseout');
                })
                bg.addEventListener("click", function (e) {
                    let imgIdx = Number(bg.id);
                    if (bg.data == "null") {
                        alert("최소사이즈 미만 오류 (가로: 512px  X  세로: 512px)");
                        return;
                    }
                    //$loading.show();
                    clearNodes(resultImg);

                    var jsonData = "{ " + "\"filename\": [ \"" + obj.filename[imgIdx] + "\"]";
                    jsonData += ",\"image\" : [\"" + obj.image[imgIdx] + "\"]";
                    jsonData += ",\"size_img\" : [ [" + imgW[imgIdx] + "," + imgH[imgIdx] + "] ]";
                    jsonData += ",\"no_img\" : 1";
                    jsonData += "}";

                    // 팝업 가운데 설정(가로)
                    if ($panelContents.outerWidth() < $(document).width()) {
                        $panelContents.css("margin-left", "-" + $panelContents.outerWidth() / 2 + "px");
                    } else {
                        $panelContents.css("left", "0px");
                    }

                    // 팝업 가운데 설정(세로)
                    if ($panelContents.outerHeight() < $(document).height()) {
                        $panelContents.css("margin-top", "-" + $panelContents.outerHeight() / 2 + "px");
                    } else {
                        $panelContents.css("top", "0px");
                    }

                    clearNodes(popupImg);
                    popupImg.id = imgIdx;
                    popupImg.style.position = "relative";
                    popupImg.style.background = "url(\'" + result + "\')";
                    popupImg.style.backgroundRepeat = "no-repeat";
                    popupImg.style.backgroundSize = "100% 100%";
                    popupImg.style.width = `${500}px`;
                    popupImg.style.height = `${312}px`;
                    popupImg.style.borderRadius = "5px";
                    popupImg.style.margin = "0px";

                    sqRed.style.position = "absolute";
                    sqRed.style.backgroundColor = "red";
                    sqRed.style.left = "-50px";
                    sqRed.style.width = "15px";
                    sqRed.style.height = "15px";
                    sqPlum.style.position = "absolute";
                    sqPlum.style.left = "40px";
                    sqPlum.style.backgroundColor = "plum";
                    sqPlum.style.width = "15px";
                    sqPlum.style.height = "15px";
                    sqYg.style.position = "absolute";
                    sqYg.style.left = "110px";
                    sqYg.style.backgroundColor = "yellowGreen";
                    sqYg.style.width = "15px";
                    sqYg.style.height = "15px";

                    typeRed.style.position = "absolute";
                    typeRed.style.color = "red";
                    typeRed.innerHTML = "scratch";
                    typeRed.style.top = "-3px";
                    typeRed.style.left = "-30px";
                    typePlum.style.position = "absolute";
                    typePlum.style.top = "-3px";
                    typePlum.style.left = "60px";
                    typePlum.style.color = "plum";
                    typePlum.innerHTML = "dent";
                    typeYg.style.position = "absolute";
                    typeYg.style.top = "-3px";
                    typeYg.style.left = "130px";
                    typeYg.style.color = "yellowGreen";
                    typeYg.innerHTML = "crack";

                    typeDiv.style.position = "relative";
                    typeDiv.style.paddingTop = "3px";
                    typeDiv.style.top = "100%";
                    typeDiv.style.left = "315px";
                    typeDiv.append(sqRed);
                    typeDiv.append(sqPlum);
                    typeDiv.append(sqYg);
                    typeDiv.append(typeRed);
                    typeDiv.append(typePlum);
                    typeDiv.append(typeYg);

                    popupImg.appendChild(typeDiv);
                    console.log(bgJsonData.results[imgIdx]);
                    if (bgJsonData.results[imgIdx] !== undefined) {
                        const type = bgJsonData.results[imgIdx].boxs;
                        for (var i = 0; i < type.length; i++) {
                            let rect = document.createElement("span");
                            let box = bgJsonData.results[imgIdx].boxs[i];
                            const confi = document.createElement("p");
                            confi.id = "confi_" + i;
                            console.log(box[5]);

                            rect.className = "rect"
                            rect.id = "rect_" + i;
                            rect.style.position = "absolute";
                            rect.style.left = `${(500 / imgW[imgIdx]) * box[0]}px`;
                            rect.style.top = `${(312 / imgH[imgIdx]) * box[1]}px`;
                            rect.style.paddingRight = `${(box[2] - box[0]) * (500 / imgW[imgIdx])}px`;
                            rect.style.paddingBottom = `${(box[3] - box[1]) * (312 / imgH[imgIdx])}px`;
                            switch (type[i][4]) {
                                case "scratch":
                                    rect.style.border = "solid red 2px";
                                    break;
                                case "dent":
                                    rect.style.border = "solid plum 2px";
                                    break;
                                case "crack":
                                    rect.style.border = "solid yellowgreen 2px";
                                    break;
                            }
                            rect.addEventListener("mouseover", function () {
                                confi.setAttribute('class', 'hoverConfi');
                                rect.style.zIndex = 2;
                                confi.style.zIndex = 2;
                                for (var j = 0; j < type.length; j++) {
                                    var rect_id = "rect_" + j;
                                    var confi_id = "confi_" + j;
                                    if (rect_id == rect.id) {
                                        continue;
                                    }
                                    if (confi_id == confi.id) {
                                        continue;
                                    }
                                    $("#" + rect_id).hide();
                                    $("#" + confi_id).hide();
                                }
                            });
                            rect.addEventListener("mouseout", function () {
                                confi.setAttribute('class', 'mouseoutConfi');
                                rect.style.zIndex = 1;
                                confi.style.zIndex = 1;
                                for (var j = 0; j < type.length; j++) {
                                    var rect_id = "rect_" + j;
                                    var confi_id = "confi_" + j;
                                    if (rect_id == rect.id) {
                                        continue;
                                    }
                                    if (confi_id == confi.id) {
                                        continue;
                                    }
                                    $("#" + rect_id).show();
                                    $("#" + confi_id).show();
                                }
                            })
                            var confidensce = parseFloat(box[5] * 100).toFixed(2)
                            confi.innerHTML = confidensce + "%";
                            confi.style.position = "absolute";
                            confi.style.fontSize = "10px";
                            confi.style.top = `${((312 / imgH[imgIdx]) * box[1]) - 20}px`;
                            confi.style.left = `${((500 / imgW[imgIdx]) * box[0]) - 10}px`;

                            popupImg.appendChild(confi);
                            popupImg.appendChild(rect);
                        }
                    }
                    resultImg.appendChild(popupImg);
                    $panel.fadeIn();
                })

                let temp = new Array();
                let img = new Image();

                img.src = result;
                img.onload = function () {
                    if (this.width < 512 && this.height < 512) {
                        bg.style.opacity = 0.2;
                        bg.data = "null";
                    } else {
                        // let imgWidth = this.width;
                        // let imgHeight = this.height;
                        // let per = 0;
                        // if(this.width > 1024) {
                        //     per = 1024 / this.width;
                        //     imgWidth *= per;
                        //     imgHeight *= per;
                        // }
                        // temp.push(imgWidth);
                        // temp.push(imgHeight);
                        // obj.filename.push(files[index].name);
                        // obj.image.push(result.split(',')[1]);
                        // obj.size_img.push(temp);
                        // obj.no_img = obj.no_img + 1;
                        // temp = [];

                        // imgW.push(this.width);
                        // imgH.push(this.height);

                        // idx ++;

                        temp.push(this.width);
                        temp.push(this.height);
                        obj.filename.push(files[index].name);
                        obj.image.push(result.split(',')[1]);
                        obj.size_img.push(temp);
                        obj.no_img = obj.no_img + 1;
                        temp = [];

                        imgW.push(this.width);
                        imgH.push(this.height);

                        idx++;
                    }
                    readFile(index + 1);
                }
                thumbnailCon.appendChild(bg);
            }
        }
        readFile(0);
    })
})



/*
function otherRectControl(total, id, show) {

    console.log("total: " + total + " id: " + id + " show: "+ show);
    for (var j = 0; j < total; j++) {
        var rect_id = "rect_" + j;
        var confi_id = "confi_" + j;
        if (j == id) {
            continue;
        }
        if (j == id) {
            continue;
        }

        if (show === true) {
            $("#" + rect_id).show();
            $("#" + confi_id).show();
        } else {
            $("#" + rect_id).hide();
            $("#" + confi_id).hide();
        }
    }
}
*/