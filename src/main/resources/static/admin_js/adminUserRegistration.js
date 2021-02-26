import * as pub from './modules/PublicModule.js';
import * as user from './modules/UserRegistrationModule/UserModule.js';

$(function(){
    let userInfo = {};
    let companyList = [];

    let i = 0;
    let flag = 0;
    let idx = null;
    let compIdx = null;
    let req = null;

    const $span = $($('#userId')[0].previousElementSibling);

    user.drawPerm($('.trClass'));
    pub.displayScroll($('#tbody')[0], $('#infoTable'), '670px', 8);
    pub.overlapCheck($('#userId'), $('.tdId'));

    $.ajax({
        type: 'POST',
        url: '/admin/CompanyCeo',
        success: function(data) {
            companyList = data;
            for(i = 0; i < companyList.length; i++){
                if(!companyList[i].is_delete){
                    $('#selectComp')
                    .append(`
                        <option
                            id=${companyList[i].idx} 
                            value=${companyList[i].company}
                            comp=${i}
                        >${companyList[i].company}
                        </option>
                    `);
                }
            } 
            $('.a').prop('selected', true);
        }
    });

    $('#selectComp').on('change', function() {
        const selectedComp = pub.optSelector($('#selectComp')[0]);
        for(i = 0; i < companyList.length; i++){
            if(selectedComp === companyList[i].company){
                compIdx = companyList[i].idx;
                $('#ceo').html(companyList[i].ceo);
            }
        }
    });

    $('#select').on('change',() => {
        pub.clearCheckbox($('.auth'))
        if($('#select')[0].selectedIndex !== 0){
            if($('#select')[0].selectedIndex === 1) {
                $('.auth').prop("checked", true)
            }
            $('#selectList').css('visibility', 'visible');
        }else $('#selectList').css('visibility', 'hidden');
    });

    $('#firstPhoneNum').keyup(function(e) {
        e = e || window.event;
        const _val = this.value.trim();
        this.value = pub.autoHyphenPhoneNum(_val);
    });

    $('#secondPhoneNum').keyup(function(e) {
        e = e || window.event;
        const _val = this.value.trim();
        this.value = pub.autoHyphenPhoneNum(_val);
    });

    $('#submitBtn').on('click', function(e) {
        e.preventDefault();

        if(pub.optSelector($('#selectComp')[0]) === 'alpha'){
            alert('업체명이 선택되지 않았습니다.');
        }else if(!$('#name')[0].value || $('#name')[0].value.replace(/ /g,"") === ''){
            alert('이름칸이 비어있습니다.');
        }else if(!$('#userId')[0].value || $('#userId')[0].value.replace(/ /g,"") === ''){
            alert('아이디를 입력해주십시오.');
        }else if(!$('#userPw')[0].value || $('#userPw')[0].value.replace(/ /g,"") === ''){
            alert('비밀번호를 입력해주십시오.');
        }else if($('#userPw')[0].value !== $('#confirmPw')[0].value){
            alert('비밀번호가 일치하지 않습니다.');
        }else if(!$('#firstPhoneNum')[0].value || $('#firstPhoneNum')[0].value.replace(/ /g,"") === ''){
            alert('한 개 이상의 연락처를 입력해 주십시오.');
        }else if(pub.optSelector($('#select')[0]) === 'alpha'){
            alert('권한이 설정되지 않았습니다.');
        }else if($span[0].style.color === 'red'){
            alert('이미 존재하는 아이디입니다.');
        }else{
            userInfo = user.userInfoSetter();
        
            pub.clearForm($('.userInput'));
            pub.clearSelectTag($('#selectComp'));
            pub.clearSelectTag($('#selectCeo'));
            pub.clearSelectTag($('#select'));
            pub.clearCheckbox($('.auth'));
            $('#ceo').html('');
            $('#selectList').css('visibility', 'hidden');
    
            req = {
                'company_info_idx': compIdx,
                'name': userInfo.name,
                'id': userInfo.id,
                'pw': userInfo.pw,
                'tel': userInfo.tel,
                'user_perm': userInfo.user_perm,
                'menu_func': userInfo.menu_func,
            }
    
            $.ajax({
                type: 'post',
                url: '/admin/userInsert',
                data: JSON.stringify(req),
                contentType : 'application/json',
                error: function(){
                    console.log('err');
                },
                success: function(e){
                    console.log('success');
                    location.reload(true);
                }
            })
        }
    });

    $(".trClass").off().on("click", function(e) {
        flag = 0;
        if(localStorage.getItem("value")) pub.trColorSetter(e.target);
        else pub.trColorSetter(e.target.parentNode);
        
        for(i=0; i<$(".trClass").length; i++){
            if($(".trClass")[i].style.backgroundColor === "lightgray") {
                flag = 1;
            }
        }

        if(flag) {
            $('#update').prop('disabled', false);
            $('#delete').prop('disabled', false);
        }else {
            $('#update').prop('disabled', true);
            $('#delete').prop('disabled', true);
        }
    });

    $('#update').click(function(e) {
        e.preventDefault();
        pub.clearForm($('.businessInput'));
        pub.disabledTrue($('#delete'));

        let cnt = 0;
        const $trArr = $('.trClass');
        let selectedTr = null;

        for(i = 0; i < $trArr.length; i++){
            if($trArr[i].style.backgroundColor === 'lightgray') {
                selectedTr = $trArr[i].children[0].innerText;
                cnt++;
            }
        }

        if(cnt > 1) {
            alert('수정하시려면 1개의 row만 선택해 주세요.');
            $('#mask').css('display', 'none');
            pub.disabledTrue($('#update'));
        } else if(cnt === 0) {
            alert('수정할 row를 선택해 주세요');
            $('#mask').css('display', 'none');
            pub.disabledTrue($('#update'));
        } else {
            $('#selectList').css('visibility', 'visible');
            $('#submitBtn').css('display', 'none');
            $('#updateBtnDiv').css('display', 'inline-block');

            $.ajax({
                type: 'post',
                url: '/admin/updateUserInfo',
                data: JSON.stringify({"selectedTr": Number(selectedTr)}),
                dataType : 'json',
                contentType : 'application/json',
                error: function(e){
                    console.log('err');
                },
                success: function(data){
                    switch (data[0].user_perm) {
                        case 127:
                            data[0].user_perm = "user";
                            break;

                        case 64:
                            data[0].user_perm = "customer";
                            break;
                    
                        default:
                            data[0].user_perm = "manager";
                            break;
                    }

                    for(i = 0; i < $('#selectComp')[0].options.length; i++){
                        if($('#selectComp')[0].options[i].value === data[0].company) {
                            $(`option[comp=${i-1}]`).prop('selected', true);
                        }
                    }

                    for(i = 0; i < $('#select')[0].options.length; i++){
                        if($('#select')[0].options[i].value === data[0].user_perm) {
                            $(`option[perm=${i}]`).prop('selected', true);
                        }
                    }

                    const funcArr = user.dec2bin(data[0].menu_func).split('');

                    for(i = 0; i < funcArr.length; i++) {
                        if(funcArr[i] === "1") $($('.auth')[i]).prop('checked', true)
                    }
                    
                    idx = data[0].idx;
                    compIdx = data[0].company_info_idx;
                    $('#ceo').html(data[0].ceo);
                    $('#name')[0].value = data[0].name;
                    $('#userId')[0].value = data[0].id;
                    $('#userPw')[0].value = '';
                    $('#confirmPw')[0].value = '';
                    $('#firstPhoneNum')[0].value = data[0].tel;
                }
            });
            pub.disabledDiv();
        }
    });

    $('#updateBtn').click(function(e) {
        e.preventDefault();
        if(pub.optSelector($('#selectComp')[0]) === 'alpha'){
            alert('업체명이 선택되지 않았습니다.');
        }else if(!$('#name')[0].value || $('#name')[0].value.replace(/ /g,"") === ''){
            alert('이름칸이 비어있습니다.');
        }else if(!$('#userId')[0].value || $('#userId')[0].value.replace(/ /g,"") === ''){
            alert('아이디를 입력해주십시오.');
        }else if(!$('#userPw')[0].value || $('#userPw')[0].value.replace(/ /g,"") === ''){
            alert('비밀번호를 입력해주십시오.');
        }else if($('#userPw')[0].value !== $('#confirmPw')[0].value){
            alert('비밀번호가 일치하지 않습니다.');
        }else if(!$('#firstPhoneNum')[0].value || $('#firstPhoneNum')[0].value.replace(/ /g,"") === ''){
            alert('한 개 이상의 연락처를 입력해 주십시오.');
        }else if(pub.optSelector($('#select')[0]) === 'alpha'){
            alert('권한이 설정되지 않았습니다.');
        }else{
            userInfo = user.userInfoSetter();
            pub.clearForm($('.userInput'));
            
            req = {
                'idx' : idx,
                'company_info_idx': compIdx,
                'company': userInfo.company,
                'ceo': userInfo.ceo,
                'name': userInfo.name,
                'id': userInfo.id,
                'pw': userInfo.pw,
                'tel': userInfo.tel,
                'user_perm': userInfo.user_perm,
                'menu_func': userInfo.menu_func,
            }
    
            $.ajax({
                type: 'post',
                url: '/admin/updateUser',
                data: JSON.stringify(req),
                contentType : 'application/json',
                error: function(){
                    console.log('err');
                },
                success: function(e){
                    location.reload(true);
                }
            })
    
            $('#selectList').css('visibility', 'hidden');
            $('#updateBtnDiv').css('display', 'none');
            $('#submitBtn').css('display', 'inline-block');
            $('#mask').remove();
            pub.clearSelectTag($('#select'));
            pub.clearCheckbox($('.auth'));
            pub.clearForm($('.userInput'));
            pub.disabledTrue($('#update'));
        }      
    });

    $('#cancelBtn').click(function(e) {
        location.reload(true);
    });

    $('#delete').click(function(e) {
        e.preventDefault();
        pub.clearForm($('.userInput'));
        pub.disabledTrue($('#update'));
        pub.disabledTrue($('#delete'));
        pub.clearSelectTag($('#select'));
        pub.clearCheckbox($('.auth'));
        $('#selectList').css('visibility', 'hidden');
        if(confirm("정말 삭제하시겠습니까?") == true){
            const $trArr = $('.trClass');
            const delTr = [];
            for(i = 0; i < $trArr.length; i++) {
                if($trArr[i].style.backgroundColor === 'lightgray') {
                    delTr.push({
                        'idx': Number($trArr[i].children[0].innerText),
                        'is_delete': 1
                    });
                }
            }

            $.ajax({
                type: 'post',
                url: '/admin/userDelete',
                data: JSON.stringify(delTr),
                contentType : 'application/json',
                error: () => {
                    console.log("에러");
                },
                success: (e) => {
                    location.reload(true);
                }
            })
        } else {
            $('.trClass').css('backgroundColor', 'white');
        }
    });

    if(localStorage.getItem("value")){
        for(i=0; i<$(".trClass").length; i++){
            if($(".trClass")[i].children[3].innerText === localStorage.getItem("value")){
                $(".trClass")[i].click();
            }
        }
        localStorage.removeItem("value");
    }
})