$(document).ready(function(){
    const demoCon = document.getElementById("demoCon");
    const resultImg = document.getElementById("resultImg");
    const popupImg = document.createElement("div");
    const typeDiv = document.createElement("div");
    const typeRed = document.createElement("p");
    const typePlum = document.createElement("p");
    const typeYg = document.createElement("p");
    const sqRed = document.createElement("span");
    const sqPlum = document.createElement("span");
    const sqYg = document.createElement("span");
    const fBoxText = document.createElement("div");
    const thumbnailList = document.getElementById("thumbnailList");
    const selectedList = document.getElementById("selectedList");

    const imgW = new Array();
    const imgH = new Array();

    let idx = 0;

    let obj = {
        filename: [],
        image: [],
        size_img: [],
        no_img: 0
    };

    function clearNodes(parentNode) {
        if(parentNode.childNodes.length > 0)
            while(parentNode.hasChildNodes()) parentNode.removeChild(parentNode.firstChild);
    }
    
    var $panel = $(".popup_panel");
    var $panelContents = $(".popup_contents");
    var $loading = $("#loading");  

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

    $.ajax({
        url: "json/images.json",
        async: false,
        success: function(json){
            for(var i = 0; i < json.image_count-1; i++) {
                const thumbnailLi = document.createElement("li");
                const thumbnail = document.createElement("img");
                const imgName = document.createElement("p");

                imgName.innerHTML = json.image_name[i][0];
                imgName.style.position = "absolute";
                imgName.style.top = "90px";
                imgName.style.left = "115px";

                thumbnailLi.className = "text-left";
                thumbnailLi.style.display = "block";
                thumbnailLi.style.position = "relative";
                thumbnailLi.style.width = "300px";
                thumbnailLi.style.border = "1px solid gray";
                thumbnailLi.style.borderRadius = "5px";
                thumbnailLi.style.marginBottom = "10px";
                thumbnailLi.id = json.image_name[i][0];
                
                thumbnail.src = "divusImages/evaluation/thumbnail/"+json.image_name[i][0];
                thumbnail.width = 100;
                thumbnail.height = 100;
                thumbnail.style.objectFit = "cover";
                thumbnail.style.borderRadius = "5px";
                thumbnail.style.marginTop = "10px";
                thumbnail.style.marginLeft = "10px";
                thumbnail.style.marginBottom = "10px";

                thumbnailLi.addEventListener("mouseover", () => {
                    thumbnailLi.style.border = "3px solid #1acea6";
                    imgName.style.color = "#1acea6";
                });
                thumbnailLi.addEventListener("mouseout", () => {
                    thumbnailLi.style.border = "1px solid gray"
                    imgName.style.color = "gray";
                });
                thumbnailLi.addEventListener("mouseup", (e) => {
                    thumbnailLi.style.display = "none";
                    var fn = thumbnailLi.id;
                    console.log(fn);
                    var fSrc = "divusImages/evaluation/"+fn;

                    obj.no_img += 1;
                    var imgCnt = document.getElementById("imgCnt");
                    imgCnt.innerHTML = `(${obj.no_img})`;
                    const selectedLi = document.createElement("li");
                    const selectedImg = document.createElement("img");
                    const cancel = document.createElement("img");

                    if(idx > 11) {
                        selectedList.style.overflow = "scroll";
                        selectedList.style.overflowX = "hidden";
                    }

                    selectedLi.id = idx;
                    selectedLi.style.position = "relative";
                    selectedLi.style.float = "left";

                    selectedImg.src = fSrc;
                    selectedImg.width = 100;
                    selectedImg.height = 100;
                    selectedImg.style.objectFit = "cover";
                    selectedImg.style.borderRadius = "5px";
                    selectedImg.style.margin = "15px";
                    selectedImg.addEventListener("mouseover", () => selectedImg.style.border = "3px solid #1acea6");
                    selectedImg.addEventListener("mouseout", () => selectedImg.style.border = "none");
                    selectedImg.addEventListener("click", (e) => {
                        $loading.show();
                        clearNodes(popupImg);
                        console.log(selectedLi.id);
                        const imgIdx = Number(selectedLi.id);

                        $("#btn_popup_close").on("click", popupClose);
                        // 팝업 배경 클릭 이벤트 정의
                        $panel.find(".popup_bg").on("click", popupClose);
                        function popupClose(e) {
                            $panel.fadeOut();
                            // 이벤트 기본 동작 중단
                            e.preventDefault();
                        }
                        
                        popupImg.style.position = "relative";
                        popupImg.style.background = "url(\'"+fSrc+"\')";
                        popupImg.style.backgroundRepeat = "no-repeat";
                        popupImg.style.backgroundSize = "100% 100%";
                        popupImg.style.width = `${500}px`;
                        popupImg.style.height = `${312}px`;
                        popupImg.style.borderRadius = "5px";
                        popupImg.style.margin = "0px";

                        var jsonData = "{ " + "\"filename\": [ \"" + obj.filename[imgIdx] + "\"]";
                        jsonData += ",\"image\" : [\"" + obj.image[imgIdx] + "\"]";
                        jsonData += ",\"size_img\" : [[" + imgW[imgIdx] + "," + imgH[imgIdx] + "]]";
                        jsonData += ",\"no_img\" : 1";
                        jsonData += "}";

                        $.ajax({
                            type: 'post',
                            url: "/predict",
                            data : jsonData,
                            dataType: 'json',
                            processData: false,
                            contentType: false,
                            error: function(xhr, status, error){
                                $loading.hide();
                                console.log(error);
                                alert("에러가 발생했습니다..");
                            },
                            success: function(json){
                                $panel.fadeIn();
                                $loading.fadeOut("slow");
                                
                                const type = json.results[0].boxs;
                                
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
                                for(var i = 0; i < type.length; i++){
                                    let rect = document.createElement("span");
                                    let box = json.results[0].boxs[i];
                                    const confi = document.createElement("p");
                                    confi.id = "confi_" + i;
                                    
                                    rect.className = "rect"
                                    rect.id = "rect_" + i;
                                    rect.style.position = "absolute";
                                    rect.style.left = `${(500/imgW[imgIdx])*box[0]}px`;
                                    rect.style.top = `${(312/imgH[imgIdx])*box[1]}px`;
                                    rect.style.paddingRight = `${(box[2]-box[0])*(500/imgW[imgIdx])}px`;
                                    rect.style.paddingBottom = `${(box[3]-box[1])*(312/imgH[imgIdx])}px`;
                                    switch(type[i][4]) {
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
                                    rect.addEventListener("mouseover", function() {
                                        confi.setAttribute('class','hoverConfi');
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
                                    rect.addEventListener("mouseout", function() {
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
                                    var confidensce = parseFloat(box[5]*100).toFixed(2)
                                    confi.innerHTML = confidensce + "%";
                                    confi.style.position = "absolute";
                                    confi.style.fontSize = "10px";
                                    confi.style.top = `${((312/imgH[imgIdx])*box[1])-20}px`;
                                    confi.style.left = `${((500/imgW[imgIdx])*box[0])-10}px`;
                                    popupImg.appendChild(confi);
                                    popupImg.appendChild(rect);
                                    resultImg.appendChild(popupImg);
                                }  
                            }
                        });
                    });

                    cancel.src = "divusImages/cancel.png";
                    cancel.style.position = "absolute";
                    cancel.style.top = "5px";
                    cancel.style.left = "115px";
                    cancel.addEventListener("mouseover", () => {
                        cancel.style.border = "solid 1px red";
                        cancel.style.borderRadius = "8px";
                    });
                    cancel.addEventListener("mouseout", () => cancel.style.border = "none" );
                    cancel.addEventListener("click", () => {
                        --idx;
                        console.log(`cancel: ${idx}`)
                        // console.log(`before - \nobj.filename.splice: ${obj.filename}, \nsize_img.splice: [${obj.size_img}], \nno_img - 1: ${obj.no_img}`);
                        console.log(`before-------------------\nimgW: ${imgW}\nimgH: ${imgH}\n${selectedLi.id}`);
                        console.log(idx);
                        thumbnailLi.style.display = "block";
                        selectedList.removeChild(selectedLi);
                        imgW.splice(Number(selectedLi.id),1);
                        imgH.splice(Number(selectedLi.id),1);
                        obj.filename.splice(Number(selectedLi.id),1);
                        obj.image.splice(Number(selectedLi.id),1);
                        obj.size_img.splice(Number(selectedLi.id),1);
                        obj.no_img -= 1;
                        imgCnt.innerHTML = `(${obj.no_img})`;
                        console.log(`after-------------------\nimgW: ${imgW}\nimgH: ${imgH}\n${selectedLi.id}`);

                        var list = selectedList.childNodes;
                        for (i = 0; i < list.length; i ++) {
                            selectedLi.id = i;
                            list[i].id = i;
                            if(selectedLi.id != selectedImg.id) selectedImg.id = i;
                        }
                        // console.log(`after - \nobj.filename.splice: ${obj.filename}, \nsize_img.splice: [${obj.size_img}], \nno_img - 1: ${obj.no_img}`);
                    });
                    selectedLi.appendChild(selectedImg);
                    selectedImg.after(cancel);
                    selectedList.appendChild(selectedLi);
                    
                    toDataURL(fSrc, function(dataUrl) {
                        let temp = new Array();
                        let img = new Image();
                        
                        img.src = dataUrl;
                        img.onload = function() {
                            temp.push(this.width);
                            temp.push(this.height);
                            obj.filename.push(fn);
                            obj.image.push(dataUrl.split(',')[1]);
                            obj.size_img.push(temp);
                            temp = [];

                            imgW.push(this.width);
                            imgH.push(this.height);
                        }
                    })
                    idx++;
                });
                thumbnailLi.appendChild(thumbnail);
                thumbnail.after(imgName);
                thumbnailList.appendChild(thumbnailLi);
            }
        }
    });

    $("#btn_popup_close").on("click", popupClose);
    // 팝업 배경 클릭 이벤트 정의
    $panel.find(".popup_bg").on("click", popupClose);
    function popupClose(e) {
        $panel.fadeOut();
        // 이벤트 기본 동작 중단
        e.preventDefault();
    }
    
    $("#btnup").click(function(e){
        console.log(`obj.filename: ${obj.filename}, \nsize_img: [${obj.size_img}], \nno_img: ${obj.no_img}`);
        $loading.show();
        $.ajax({
            type: 'post',
            url: "/predict",
            data : JSON.stringify(obj),
            dataType: 'json',
            processData: false,
            contentType: false,
            error: function(xhr, status, error){
                $loading.hide();
                console.log(error);
                alert("에러가 발생했습니다..");
            },
            success: function(json){
                const fBox = json.f1_box;
                console.log(json);
                $loading.fadeOut("slow");
                demoCon.after(fBoxText);

                fBoxText.innerHTML = `<p style="font-size: 20px;">Box F1 Score: ${fBox.Box_F1_SCORE.toFixed(4)}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspClass F1 Score: ${fBox.Class_F1.toFixed(4)}</p>`;
            }
        });
    })
})

function toDataURL(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        var reader = new FileReader();
        reader.onloadend = function() {
            callback(reader.result);
        }

        reader.readAsDataURL(xhr.response);
    };
    xhr.open('GET', url);
    xhr.responseType = 'blob';
    xhr.send();
}

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