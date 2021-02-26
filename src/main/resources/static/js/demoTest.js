$(document).ready(function(){
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

    var $panel = $(".popup_panel");
    var $panelContents = $(".popup_contents");
    var $loading = $("#loading");


    let obj = {
        filename: [],
        image: [],
        size_img: []
    };

    let idx = 0;

    const imgW = new Array();
    const imgH = new Array();

    function clearNodes(parentNode) {
        if(parentNode.childNodes.length > 0)
            while(parentNode.hasChildNodes()) parentNode.removeChild(parentNode.firstChild);
    }

    $("#btn_popup_close").on("click", popupClose);
    // 팝업 배경 클릭 이벤트 정의
    $panel.find(".popup_bg").on("click", popupClose);
    function popupClose(e) {
        $panel.fadeOut();
        // 이벤트 기본 동작 중단
        e.preventDefault();
    }
    
    $("#select").change(function(e){
        thumbnailBody.style.display = "block";
        thumbnailBody.style.overflowX = "hidden";
        clearNodes(thumbnailCon);

        const files = e.target.files;

        function readFile(index) {
            if(index >= files.length){
                return;
            }

            obj.no_img = files.length;
            const fr = new FileReader();

            fr.readAsDataURL(files[index]);
            fr.onload = function(e) {
                const result = e.target.result;
                const bg = document.createElement("div");

                bg.id = idx;
                bg.style.position = "relative";
                bg.style.background = "url(\'"+result+"\')";
                bg.style.backgroundRepeat = "no-repeat";
                bg.style.backgroundSize = "cover";
                bg.style.width = `${100}px`;
                bg.style.height = `${100}px`;
                bg.style.borderRadius = "5px";
                bg.style.marginTop = "10px";
                bg.style.marginLeft = "10px";
                bg.style.marginBottom = "10px";
                bg.addEventListener("mouseover", function() {
                    bg.setAttribute('class','hover');
                });
                bg.addEventListener("mouseout", function() {
                    bg.setAttribute('class', 'mouseout');
                })
                bg.addEventListener("click", function(e) {
                    $loading.show();
                    console.log("select image");
                    clearNodes(resultImg);
                    imgIdx = Number(e.target.attributes[0].nodeValue);
                    console.log(JSON.stringify(obj.filename[imgIdx]+obj.size_img[imgIdx]));
                    var jsonData = "{ " + "\"filename\": [ \"" + obj.filename[imgIdx] + "\"]";
                    jsonData += ",\"image\" : [\"" + obj.image[imgIdx] + "\"]";
                    jsonData += ",\"size_img\" : [" + obj.size_img[imgIdx] + "]";
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
                    popupImg.id = idx;
                    popupImg.style.position = "relative";
                    popupImg.style.background = "url(\'"+result+"\')";
                    popupImg.style.backgroundRepeat = "no-repeat";
                    popupImg.style.backgroundSize = "100% 100%";
                    popupImg.style.width = `${500}px`;
                    popupImg.style.height = `${312}px`;
                    popupImg.style.borderRadius = "5px";
                    popupImg.style.margin = "0px";
                    
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
                            console.log(json);
                            
                            const type = json.results[0].boxs;
                            
                            console.log(type);
                            sqRed.style.position = "absolute";
                            sqRed.style.backgroundColor = "red";
                            sqRed.style.width = "15px";
                            sqRed.style.height = "15px";
                            sqPlum.style.position = "absolute";
                            sqPlum.style.left = "60px";
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
                            typeRed.style.left = "20px";
                            typePlum.style.position = "absolute";
                            typePlum.style.left = "80px";
                            typePlum.style.color = "plum";
                            typePlum.innerHTML = "dent";
                            typeYg.style.position = "absolute";
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
                            // for(var c = 0; c < typeColor.length; c++)
                            //     typeDiv.append(typeColor[c]);
                            popupImg.appendChild(typeDiv);
                            for(var i = 0; i < type.length; i++){
                                let rect = document.createElement("span");
                                let box = json.results[0].boxs[i];
                                const confi = document.createElement("p");
                                confi.id = "confi_" + i;
                                console.log(box[5]);
                                
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
                                confi.style.top = `${((312/imgH[imgIdx])*box[1])-15}px`;
                                confi.style.left = `${(500/imgW[imgIdx])*box[0]}px`;
                                popupImg.appendChild(confi);
                                popupImg.appendChild(rect);
                                resultImg.appendChild(popupImg);

                            }  
                        }
                    });
                })

                idx ++;

                let img = new Image();
                
                img.src = result;
                img.onload = function() {

                    obj.filename.push(files[index].name);
                    obj.image.push(result.split(',')[1]);
                    obj.size_img.push(this.width * this.height);

                    imgW.push(this.width);
                    imgH.push(this.height);

                    readFile(index+1);
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