import * as pub from './modules/PublicModule.js'

let i = 0;
let selectedOpt;
let input = null;

$(function() {
    localStorage.removeItem("value");
    pub.displayScroll($('#tbody')[0], $('#contentsTable'), '880px', 18);
    input = $('#input')[0].value;

    const moveToSelectedList = function(){
        $(".trClass").off().on("click", function(e) {
            $(".clickDiv").css({
                "display": "inline-block",
                "top": e.pageY,
                "left": e.pageX,
            });
    
            $(".a").css("display", "inline-block");
    
            if($(e.target)[0].className === "bui"){
                $("#toUser").css("display", "none");
                localStorage.setItem("value", $(e.target)[0].parentNode.children[1].innerText);
            }else{
                $("#toBusi").css("display", "none");
                console.log($(e.target)[0].parentNode.children)
                localStorage.setItem("value", $(e.target)[0].parentNode.children[3].innerText);
            } 
        });
    }

    $("#select").val("company").prop("selected", true);

    $("#select").on("change", function(e) {
        selectedOpt = pub.optSelector(e.target);
        $.ajax({
            type: "post",
            url: '/company/ClientsManagement',
            data: JSON.stringify({
                'selectedOpt': selectedOpt,
                'inputText': input.replace(/ /g,"") 
            }),
            contentType: 'application/json',
            success: function (res) {
                if(selectedOpt === "company"){
                    res.length === 0 || $('.trClass').remove();
                    for(i = 0; i < res.length; i++){
                        let $tr = $(`<tr class="trClass" style="text-align: center;"></tr>`);
                        $tr.append(`<td class="bui" style="display: none;">${res[i].idx}</td>`)
                        $tr.append(`<th class="bui">${res[i].company}</th>`)
                        $tr.append(`<td class="bui">${res[i].reg_date}</td>`)
                        $tr.append(`<td class="bui">${res[i].count}</td>`)
                        $tr.append(`<td class="bui">${res[i].address}</td>`)
                        $('#tbody').append($tr);
                    }
                }else{
                    res.length === 0 || $('.trClass').remove() && $('.thClass').remove();
                    let $tr = $(`<tr class="thClass" style="text-align: center;"></tr>`);
                    $tr.append(`<th>업체명</th>`)
                    $tr.append(`<th>사용자명</th>`)
                    $tr.append(`<th>사용자 등록일시</th>`)
                    $tr.append(`<th>주소</th>`)
                    $('#thead').append($tr);

                    for(i = 0; i < res.length; i++){
                        $tr = $(`<tr class="trClass" style="text-align: center;"></tr>`);
                        $tr.append(`<td class="user" style="display: none;">${res[i].idx}</td>`)
                        $tr.append(`<th class="user">${res[i].company}</th>`)
                        $tr.append(`<td class="user">${res[i].name}</td>`)
                        $tr.append(`<td class="user">${res[i].reg_date}</td>`)
                        $tr.append(`<td class="user">${res[i].address}</td>`)
                        $('#tbody').append($tr);
                    }
                }

                moveToSelectedList()
            }
        });
    })
        
    $('#search').on('click', function (e) {
        e.preventDefault();

        selectedOpt = pub.optSelector($('#select')[0]);
        input = $('#input')[0].value;

        $.ajax({
            type: "post",
            url: '/company/ClientsManagement',
            data: JSON.stringify({
                'selectedOpt': selectedOpt,
                'inputText': input.replace(/ /g,"") 
            }),
            contentType: 'application/json',
            success: function (res) {
                console.log(!res[0])
                if(!res[0]){
                    if(selectedOpt === "company") alert("존재하지 않는 업체명 입니다.")
                    else alert("존재하지 않는 사용자명 입니다.")
                }else{
                    let keys = Object.keys(res[0]);
                    let flag = 0;
                    for(i=0; i<keys.length; i++){
                        if(keys[i] === "count") flag = 1;
                    }
                    
                    if(flag){
                        res.length === 0 || $('.trClass').remove();
                        for(i = 0; i < res.length; i++){
                            let $tr = $(`<tr class="trClass" style="text-align: center;"></tr>`);
                            $tr.append(`<td class="bui" style="display: none;">${res[i].idx}</td>`)
                            $tr.append(`<th class="bui">${res[i].company}</th>`)
                            $tr.append(`<td class="bui">${res[i].reg_date}</td>`)
                            $tr.append(`<td class="bui">${res[i].count}</td>`)
                            $tr.append(`<td class="bui">${res[i].address}</td>`)
                            $('#tbody').append($tr);
                        }
                    }else{
                        res.length === 0 || $('.trClass').remove() && $('.thClass').remove();
                        let $tr = $(`<tr class="thClass" style="text-align: center;"></tr>`);
                        $tr.append(`<th>업체명</th>`)
                        $tr.append(`<th>사용자명</th>`)
                        $tr.append(`<th>사용자 등록일시</th>`)
                        $tr.append(`<th>주소</th>`)
                        $('#thead').append($tr);

                        for(i = 0; i < res.length; i++){
                            $tr = $(`<tr class="trClass" style="text-align: center;"></tr>`);
                            $tr.append(`<td class="user" style="display: none;">${res[i].idx}</td>`)
                            $tr.append(`<th class="user">${res[i].company}</th>`)
                            $tr.append(`<td class="user">${res[i].name}</td>`)
                            $tr.append(`<td class="user">${res[i].reg_date}</td>`)
                            $tr.append(`<td class="user">${res[i].address}</td>`)
                            $('#tbody').append($tr);
                        }
                    }

                    moveToSelectedList()
                }
            }
        });   
    });

    moveToSelectedList()

    $("#x").off().on("click", function(e) {
        $(".clickDiv").css({
            "display": "none",
        });
    });
})