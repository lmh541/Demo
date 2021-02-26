import * as pub from '../modules/PublicModule.js';
import * as serv from '../modules/ServiceModules/ServiceModule.js'

$(function(){
    let i = 0;
    let j = 0;
    let k = 0;
    let dbData = null;
    let realData = null;
    
    let queryString = {
        startDate:``, 
        endDate:``,
        company:``
    };

    const drawRow = function(data, index){
        let difference = (data[index].ai_scratch+data[index].ai_dent+data[index].ai_crack)-(data[index].an_scratch+data[index].an_dent+data[index].an_crack);

        if(!data[index].an_photo){
            data[index].an_photo = "/divusImages/adminService/ano.png";
            data[index].an_scratch = "-";
            data[index].an_dent = "-";
            data[index].an_crack = "-";
            difference = "-";
        }
        
        $("#tbody").append(`
            <tr class="trClass">
                <td>
                    <div class="form-check">
                        <input class="form-check-input checkboxClass" type="checkbox">
                    </div>
                </td>
                <td class="tdClass">${data[index].reg_date}</td>
                <td class="tdClass">${data[index].company}</td>
                <td class="tdClass"><img class="ori thumbnail" loading="lazy" src="/photo/${data[index].src_photo}" alt="Original" onerror="this.src='/divusImages/adminService/ori.png'" style="width:100px; height:70px; object-fit: cover; border: 1px solid white; border-radius: 5px;"></td>
                <td class="tdClass"><img class="ai thumbnail" loading="lazy" src="/photo/${data[index].ai_photo}" alt="AI" onerror="this.src='/divusImages/adminService/ai.png'" style="width:100px; height:70px; object-fit: cover; border: 1px solid white; border-radius: 5px;"></td>
                <td class="tdClass"><img class="ano thumbnail" loading="lazy" src="/photo/${data[index].an_photo}" alt="Annotation" onerror="this.src='/divusImages/adminService/ano.png'" style="width:100px; height:70px; object-fit: cover; border: 1px solid white; border-radius: 5px;"></td>
                <td class="tdClass">
                    <div class="typeDiv">
                        <p>${data[index].ai_scratch}</p>
                        <p>${data[index].ai_dent}</p>
                        <p>${data[index].ai_crack}</p>
                    </div>
                </td>
                <td class="tdClass">
                    <div class="typeDiv">
                    <p>${data[index].an_scratch}</p>
                    <p>${data[index].an_dent}</p>
                    <p>${data[index].an_crack}</p>
                    </div>
                </td>
                <td class="tdClass">${difference}</td>
            </tr>
        `);
    };

    const drawTable = function(data){
        realData = [];
        $("#tbody").empty();

        for(i=0; i<data.length; i++){
            drawRow(data, i);
            realData.push(data[i]);            
        }

        $(".tdClass").off().on("click", function(){
            const oriSrc = $($(this)[0].parentNode.children[3].children[0])[0].src;
            const aiSrc = $($(this)[0].parentNode.children[4].children[0])[0].src;
            const anoSrc = $($(this)[0].parentNode.children[5].children[0])[0].src;
            const len = $("#tbody")[0].children.length;
            const $trColor = $(this)[0].parentNode.style.backgroundColor;
            if(!$trColor || $trColor === "white"){
                for(i=0; i<len; i++){
                    if($($("#tbody")[0].children[i])[0].style.backgroundColor === "lightgray"){
                        $($("#tbody")[0].children[i])[0].style.backgroundColor = "white"
                    }
                }
                $(this)[0].parentNode.style.backgroundColor = "lightgray";
                $("#tempWrap").css("display", "none");
                $("#tempQ").css("display", "none");
                $("#selectedWrap").append($(`
                    <div class="detectedWrap">
                        <div class="detectedDiv">
                            <img class="detectedImg" src="${oriSrc}" alt="Original Img" width="100%">
                        </div>
                        <div class="detectedDiv">
                            <img class="detectedImg" src="${aiSrc}" alt="AI Img" width="100%">
                        </div>
                        <div class="detectedDiv">
                            <img class="detectedImg" src="${anoSrc}" alt="Annotation Img" width="100%">
                        </div>
                    </div>
                `))
                $("#qDiv").append(`
                    <button class="imgQ" data="${oriSrc}" style="color: black;">●</button>
                `);
                
                let last = $("#selectedWrap")[0].children.length-1;
    
                for(i=1; i<last; i++){
                    $($(".detectedWrap")[i]).remove();
                }
    
                $(".detectedImg").off().on("click", function(e){
                    e.preventDefault();
                    let img = $(this)[0].src;
                    let idx = img.lastIndexOf('/') + 1;
                    let imgSrc = img.substr(idx)
                    const option = `status=no, menubar=no, resizable=no, scrollbars=no, location=no, toolbar=no`;
                    window.open($(this)[0].src, imgSrc, option);
                })
            }else{
                $(this)[0].parentNode.style.backgroundColor = "white";
                $("#selectedWrap")[0].children[1].remove();
                $("#tempWrap").css("display", "flex");
            }
        })

        pub.displayScroll($('#tbody')[0], $('#tbody'), '570px', 5);
    };

    $("#moveBtn").off().on("click", function(e){
        e.preventDefault();
        window.location.href = "annotation"
    });

    $("#search").on("click", (e) => {
        e.preventDefault();
    
        queryString = {
            startDate:`${$("#startDate")[0].value.replace(/ /g,"")}`, 
            endDate:`${$("#endDate")[0].value.replace(/ /g,"")}`,
            company:`${$("#company")[0].value.replace(/ /g,"")}`,
        };

        let key = Object.keys(queryString);
        let cnt = 0;
        for(i=0; i<key.length; i++){
            if(!queryString[key[i]]){
                cnt++;
            }
        }

        if(cnt === 3){
            alert("검색 조건을 입력해주십시오.");
        }else{
            $.ajax({
                type: "post",
                url: "/admin/doc",
                data: JSON.stringify(queryString),
                contentType : 'application/json',
                success: function(data){
                    const err = JSON.parse(JSON.stringify(data));
                    if(err === undefined || err[0] === undefined || err[0].aErr !== undefined){
                        alert(`"${queryString.company}"에 대한 정보가 없습니다.`)
                    }else{
                        $("#tbody").empty();
                        dbData = data;
                        drawTable(dbData);
                    }
                }
            })
        }
    });

    $("#exportBtn").off().on("click", function(e){
        e.preventDefault();
        let queryJson = [];
        for(i=0; i<$(".trClass").length; i++){
            let tempArr = [];
            if($($(".trClass")[i].children[0].children[0].children[0]).is(":checked")){
                for(j=1; j<$(".trClass")[i].children.length; j++){
                    if(j > 2 && j < 6){
                        let thumbnail = $($(".trClass")[i].children[j].children[0])[0].src;
                        let idx = thumbnail.lastIndexOf('/') + 1;
                        tempArr.push(thumbnail.substr(idx))
                    }else if(j === 6 || j === 7){
                        for(k=0; k<$(".trClass")[i].children[j].children[0].children.length; k++){
                            tempArr.push(Number($(".trClass")[i].children[j].children[0].children[k].innerText))
                        }
                    }else if(j === 8){
                        tempArr.push(Number($(".trClass")[i].children[j].innerText))
                    }else{
                        tempArr.push($(".trClass")[i].children[j].innerText)
                    }
                }
                let tempList = {
                    "reg_date": "",
                    "company": "",
                    "src_photo": "",
                    "ai_photo": "",
                    "an_photo": "",
                    "ai_scratch": 0,
                    "ai_dent": 0,
                    "ai_crack": 0,
                    "an_scratch": 0,
                    "an_dent": 0,
                    "an_crack": 0,
                    "difference": 0,
                };
                
                k = 0;
                for(var prop in tempList){
                    tempList[prop] = tempArr[k];
                    k++;
                }
                queryJson.push(tempList);
            }
        }
        
        $.ajax({
            type: "post",
            url: "/admin/excel",
            data: JSON.stringify(queryJson),
            contentType : 'application/json',
            success: function(data){
                let zip = new JSZip();
                let idx = data.lastIndexOf('/') + 1;
                let fileName = data.substr(idx);
                zip.file(fileName, serv.urlToPromise(data), {binary : true});
                zip.generateAsync({type: "blob"})
                .then(function(cont){
                    saveAs(cont, "excel.zip");
                });
                dbData = data;
            }
        })
    });
    
    $("#checkboxAll").on("click", () => {
        serv.checkAll($(".checkboxClass"));
    });
});