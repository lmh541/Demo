import * as pub from '../modules/PublicModule.js';
import * as serv from '../modules/ServiceModules/ServiceModule.js'

$(function() {
    let i = 0;
    let j = 0;
    let dbData = null;
    let pages = null;
    let totalNum = null;
    
    let queryString = JSON.stringify({
        startDate:``, 
        endDate:``,
        company:``,
        option: "all",
        pageNo: 1
    });

    const getDataFromDB = function() {
        $.ajax({
            type: "post",
            url: "/admin/ano",
            data: queryString,
            dataType:"json",
            contentType : 'application/json',
            success: function(data){
                totalNum = data.total;
                dbData = data.list;
                pages = data.navigatepageNums;
                drawTable(dbData);
                paging(pages);
            }
        });
    };

    const jsonUpload = function(upload) {
        $("#select").click();
        $("#select").off().on("change", function() {
            const file = $("#select").prop("files")[0];
            const fr = new FileReader();
            fr.readAsText(file, "UTF-8");
            fr.onload = function(selectedJsonData){
                let flag = true;
                let matchCnt = 0;
                let anoImgName = "";

                let src = "";
                let idx = 0;
                let oriImgName = "";
                const jsonData = JSON.parse(selectedJsonData.target.result);
                const key = Object.keys(jsonData);

                if(!upload || key.length > 1){
                    if(upload){
                        flag = false;
                        alert("묶음이 아닌 파일을 업로드 해주십시오.");
                    }else{
                        for(i=0; i<dbData.length; i++){
                            for(j=0; j<key.length; j++){
                                if(dbData[i].src_photo === jsonData[key[j]].filename) matchCnt++;
                            }
                        }
                        if(matchCnt != key.length){
                            flag = false;
                            alert("이름이 일치하지 않는 데이터가 있습니다.");
                        }
                    }
                }else{
                    src = $($($($(upload)[0].parentNode)[0].parentNode)[0].children[3])[0].children[0].src;
                    idx = src.lastIndexOf('/') + 1;
                    oriImgName = src.substr(idx);
                    anoImgName = jsonData[key].filename;

                    if(oriImgName !== anoImgName){
                        flag = false;
                        alert("이름이 일치하지 않는 데이터가 있습니다.");
                    }
                }

                if(flag){
                    $.ajax({
                        type: "post",
                        url: "/admin/updateAnnotation",
                        data: selectedJsonData.target.result,
                        contentType: "application/json",
                        success: function(){
                            $("#tbody").empty();
                            closePopup();
                            getDataFromDB();
                        }
                    });
                }
            }
            $("#select")[0].value = "";
        })
    };
    
    const clickPopupImg = function(url, name) {
        const option = `status=no, menubar=no, resizable=no, scrollbars=no, location=no, toolbar=no`;
        window.open(url, name, option);
    };
    
    const closePopup = function(){
        $("#popup").css("display", "none");
        $("#popupOri").attr("src", "");
        $("#popupAi").attr("src", "");
        $("#popupAno").attr("src", "");
        $("#divAno1").css("display", "inline-block");
        $("#divAno2").css("display", "none");
    };
    
    const popup = function($anoTd, con) {
        let idx;
        const oriUrl = $($anoTd[0].parentNode)[0].children[3].children[0].src;
        idx = oriUrl.lastIndexOf('/') + 1;
        const oriImgName = oriUrl.substr(idx);
        const aiUrl = $($anoTd[0].parentNode)[0].children[4].children[0].src;
        idx = aiUrl.lastIndexOf('/') + 1;
        const aiImgName = aiUrl.substr(idx);

        let anoUrl = "";
        let anoImgName = "";
    
        $("#popup").css("display", "inline-block");
        $("#popupOri").attr("src", oriUrl);
        $("#nameOri").text(oriImgName);
        $("#popupAi").attr("src", aiUrl);
        $("#nameAi").text(aiImgName);
    
        if(con){
            anoUrl = $($anoTd[0].parentNode)[0].children[5].children[0].src;
            idx = anoUrl.lastIndexOf('/') + 1;
            anoImgName = anoUrl.substr(idx);
            $("#popupAno").attr("src", anoUrl);
            $("#nameAno").text(anoImgName);
        } else {
            $("#divAno1").css("display", "none");
            $("#divAno2").css("display", "flex");
        }
        
        $("#popupOri").on("click", function() {
            clickPopupImg(oriUrl, "ori")
        });
    
        $("#popupAi").on("click", function() {
            clickPopupImg(aiUrl, "ai")
        });
    
        $("#popupAno").on("click", function() {
            clickPopupImg(anoUrl, "ano")
        });
    
        $("#popupClose").on("click", function(){
            closePopup();
        });

        $("#fakeUpload").off().on("click", function(event){
            event.preventDefault();
            $("#rUpload").click();
        })
    
        $("#rUpload").off().on("click", function(eve){
            eve.preventDefault();

            let popupOri = $($($($(this)[0].parentNode)[0].parentNode)[0].parentNode)[0].children[0].children[1].innerText;
            let index = $($($($(this)[0].parentNode)[0].parentNode)[0].parentNode)[0].children[0].children[1].innerText.lastIndexOf(":");
            let popupOriname = popupOri.substr(index+2);
            let ori = "";
            let oriThumb = ""
            let oriName = "";

            i = 0;
            j = 0;
            while(popupOriname != oriName){
                ori = $(".ori")[i];
                oriThumb = $($(".ori")[i])[0].src;
                j = oriThumb.lastIndexOf('/') + 1;
                oriName = oriThumb.substr(j);
                i++
            }

            const aUpload = $($($($(ori)[0].parentNode)[0].parentNode)[0].children[5])[0].children[0];

            jsonUpload(aUpload);
        });
    };
    
    const clickThumbnail = function(e, $anoTd) {
        const con = ($anoTd[0].children[0].nodeName === "IMG") ? true : false;
        popup($anoTd, con);
    };
    
    const drawRow = function(data, index) {
        let ano = null;

        if(!data[index].an_photo) ano = `<td id=${data[index].idx}><button class="aUpload" type="button"><i class="fas fa-file-upload"></i></button></td>`;
        else ano = `<td><img class="ano thumbnail" loading="lazy" src="/photo/${data[index].an_photo}" alt="Annotation" onerror="this.src='/divusImages/adminService/ano.png'" style="width:100px; height:70px; object-fit: cover; border: 1px solid white; border-radius: 5px;"></td>`
    
        $("#tbody").append(`
            <tr class="trClass">
                <td style="width: 70px; padding-left: 20px;">
                    <div class="form-check">
                        <input id=${index} class="form-check-input checkboxClass" type="checkbox">
                    </div>
                </td>
                <td>${data[index].reg_date}</td>
                <td>${data[index].company}</td>
                <td><img class="ori thumbnail" loading="lazy" src="/photo/${data[index].src_photo}" alt="Original" onerror="this.src='/divusImages/adminService/ori.png'" style="width:100px; height:70px; object-fit: cover; border: 1px solid white; border-radius: 5px;"></td>
                <td><img class="ai thumbnail" loading="lazy" src="/photo/${data[index].ai_photo}" alt="AI" onerror="this.src='/divusImages/adminService/ai.png'" style="width:100px; height:70px; object-fit: cover; border: 1px solid white; border-radius: 5px;"></td>
                ${ano}
            </tr>
        `);
    };
    
    const drawTable = function(data) {
        $("#tbody").empty();
        let $anoTd = null;
    
        for(i=0; i<data.length; i++){
            drawRow(data, i);
        }
    
        $(".ori").on("click", function(){
            $anoTd = $($($(this)[0].parentNode)[0].parentNode.children[5]);
            clickThumbnail(this, $anoTd);
        });
    
        $(".ai").on("click", function(){
            $anoTd = $($($(this)[0].parentNode)[0].parentNode.children[5]);
            clickThumbnail(this, $anoTd);
        });
    
        $(".ano").on("click", function(){
            $anoTd = $($($(this)[0].parentNode)[0].parentNode.children[5]);
            clickThumbnail(this, $anoTd);
        });
    
        $(".aUpload").off().on("click", function(e){
            e.preventDefault();
            jsonUpload(this);
        });

        pub.displayScroll($('#tbody')[0], $('#tbody'), '800px', 8);
    };

    const paging = function(pageArr){
        $("#pageNum").empty();
        for(i=0; i<pageArr.length; i++){
            $("#pageNum").append(`<button class="pTxt">${pageArr[i]}</button>`)
        }
        $("#itemNum")[0].innerText = totalNum;

        $(".pTxt")[0].style.color = "gray"
        $(".pTxt").off().on("click", function(){
            const selectedPage = Number($(this)[0].innerText);
            const opt = pub.optSelector($("#selectOpt")[0])
            console.log(opt);
            queryString = JSON.stringify({
                startDate:`${$("#startDate")[0].value}`, 
                endDate:`${$("#endDate")[0].value}`,
                company:`${$("#company")[0].value}`,
                option: opt,
                pageNo: selectedPage
            });

            $(".pTxt").css("color", "black");
            $(this).css("color", "gray");
            
            $.ajax({
                url: "/admin/ano",
                method: "post",
                data: queryString,
                contentType: "application/json",
                success: function(page){
                    totalNum = page.total;
                    dbData = page.list;
                    pages = page.navigatepageNums;
                    drawTable(dbData);
                }
            })
        })

        $("#prev").off().on("click", function(){
            let $pageNumArr = $(".pTxt");
            for(i=0; i<$pageNumArr.length; i++){
                if($pageNumArr[i].style.color === "gray"){
                    if(i === 0) alert("첫번째 페이지입니다.");
                    else $pageNumArr[--i].click();
                }
            }
        })

        $("#next").off().on("click", function(){
            let $pageNumArr = $(".pTxt");
            for(i=0; i<$pageNumArr.length; i++){
                if($pageNumArr[i].style.color === "gray"){
                    if(i === $pageNumArr.length-1) alert("마지막 페이지입니다.");
                    else $pageNumArr[++i].click();
                }
            }
        })
    };
    
    $("#search").on("click", (e) => {
        e.preventDefault();
        const opt = pub.optSelector($("#selectOpt")[0])
    
        queryString = JSON.stringify({
            startDate:`${$("#startDate")[0].value}`, 
            endDate:`${$("#endDate")[0].value}`,
            company:`${$("#company")[0].value}`,
            option: opt,
            pageNo: 1
        });

        $.ajax({
            type: "post",
            url: "/admin/ano",
            data: queryString,
            contentType : 'application/json',
            success: function(data){
                const err = JSON.parse(JSON.stringify(data.list));
                if(err === undefined || err[0] === undefined || err[0].aErr !== undefined){
                    alert(`"${$("#company")[0].value}"에 대한 정보가 없습니다.`)
                }else{
                    $("#tbody").empty();
                    totalNum = data.total;
                    dbData = data.list;
                    pages = data.navigatepageNums;
                    drawTable(dbData);
                    paging(pages);
                }
            }
        })
    });

    $("#checkboxAll").on("click", (e) => {
        serv.checkAll($(".checkboxClass"));
    });
    
    $("#download").off().on("click", (e) => {
        e.preventDefault();
        let flag = false;
        let imgName;
        let checkboxArr = $(".checkboxClass");
        let zip = new JSZip();
        
        for(i=0; i<checkboxArr.length; i++){
            if($(checkboxArr[i]).is(":checked")) {
                flag = true;
                let url = $($($($($($($(checkboxArr[i])[0].parentNode)[0].parentNode)[0].parentNode)[0].children[3])[0].parentNode)[0].children[3])[0].children[0].src;
                imgName = url.substr(url.lastIndexOf('/')+1);
                zip.file(imgName, serv.urlToPromise(url), {binary : true});
            }
        }

        if(flag){
            zip.generateAsync({type: "blob"})
            .then(function(cont){
                saveAs(cont, "images.zip");
            });
        }else{
            alert("다운로드할 이미지를 체크해 주십시오.");
        }
        
    });
    
    $("#upload").off().on("click", (e) => {
        e.preventDefault();
        jsonUpload();
    });

    getDataFromDB();
});