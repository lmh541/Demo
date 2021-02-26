import * as pub from './modules/PublicModule.js';
import * as business from './modules/BusinessRegistrationModule/BusinessModule.js';

$(function() {
    let businessInfo = {};
    let i = 0;
    let flag = 0;
    let idx = null;
    let req = null;

    const $span = $($('#licenseNum')[0].previousElementSibling);

    pub.displayScroll($('#tbody')[0], $('#infoTable'), '670px', 8);
    pub.overlapCheck($('#licenseNum'), $('.tdBusinessNum'));
        
    $('#licenseNum').keyup(function(e) {
        e = e || window.event;
        const _val = this.value.trim();
        this.value = business.autoHyphenLicenseNum(_val);
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

    $('#submitBtn').click(function(e) {
        e.preventDefault();

        if(!$('#licenseNum')[0].value || $('#licenseNum')[0].value.replace(/ /g,"") === ''){
            alert('사업자번호를 입력해 주십시오.');
        }else if(!$('#compName')[0].value || $('#compName')[0].value.replace(/ /g,"") === ''){
            alert('업체명을 입력해 주십시오.');
        }else if(!$('#representative')[0].value || $('#representative')[0].value.replace(/ /g,"") === ''){
            alert('대표자를 입력해 주십시오.');
        }else if(!$('#firstPhoneNum')[0].value || $('#firstPhoneNum')[0].value.replace(/ /g,"") === ''){
            alert('한 개 이상의 연락처를 입력해 주십시오.');
        }else if(!$('#address')[0].value || $('#firstPhoneNum')[0].value.replace(/ /g,"") === ''){
            alert('주소를 입력해 주십시오.');
        }else if($span[0].style.color === 'red'){
            alert('이미 존재하는 사업자 번호입니다.');
        }else{
            businessInfo = business.businessInfoSetter();
            pub.clearForm($('.businessInput'));

            req = {
                'business_num': businessInfo.business_num,
                'company': businessInfo.company,
                'ceo': businessInfo.ceo,
                'tel_main': businessInfo.tel_main,
                'tel_sub': businessInfo.tel_sub,
                'address': businessInfo.address,
            };

            $.ajax({
                type: 'post',
                url: '/company/BusinessRegistration',
                data: JSON.stringify(req),
                contentType : 'application/json',
                error: function(){
                    console.log('err');
                },
                success: function(e){
                    location.reload(true);
                }
            });
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
        } else if(!cnt) {
            alert('수정할 row를 선택해 주세요');
            $('#mask').css('display', 'none');
            pub.disabledTrue($('#update'));
        } else {
            $('#submitBtn').css('display', 'none');
            $('#updateBtnDiv').css('display', 'inline-block');

            $.ajax({
                type: 'post',
                url: '/company/updateSelectCompany',
                data: JSON.stringify({"selectedTr": Number(selectedTr)}),
                dataType : 'json',
                contentType : 'application/json',
                error: function(){
                    console.log('err');
                },
                success: function(data){
                    idx = data[0].idx;
                    $('#licenseNum')[0].value = data[0].business_num;
                    $('#compName')[0].value = data[0].company;
                    $('#representative')[0].value = data[0].ceo;
                    $('#firstPhoneNum')[0].value = data[0].tel_main;
                    $('#secondPhoneNum')[0].value = data[0].tel_sub;
                    $('#address')[0].value = data[0].address;
                }
            });
            pub.disabledDiv();
        }
    });

    $('#updateBtn').click(function(e) {
        e.preventDefault();

        if(!$('#licenseNum')[0].value || $('#licenseNum')[0].value.replace(/ /g,"") === ''){
            alert('사업자번호를 입력해 주십시오.');
        }else if(!$('#compName')[0].value || $('#compName')[0].value.replace(/ /g,"") === ''){
            alert('업체명을 입력해 주십시오.');
        }else if(!$('#representative')[0].value || $('#representative')[0].value.replace(/ /g,"") === ''){
            alert('대표자를 입력해 주십시오.');
        }else if(!$('#firstPhoneNum')[0].value || $('#firstPhoneNum')[0].value.replace(/ /g,"") === ''){
            alert('한 개 이상의 연락처를 입력해 주십시오.');
        }else if(!$('#address')[0].value || $('#firstPhoneNum')[0].value.replace(/ /g,"") === ''){
            alert('주소를 입력해 주십시오.');
        }else{
            businessInfo = business.businessInfoSetter();
            pub.clearForm($('.businessInput'));
    
            req = {
                'idx' : idx,
                'business_num': businessInfo.business_num,
                'company': businessInfo.company,
                'ceo': businessInfo.ceo,
                'tel_main': businessInfo.tel_main,
                'tel_sub': businessInfo.tel_sub,
                'address': businessInfo.address,
            }
    
            $.ajax({
                type: 'post',
                url: '/company/updateInfo',
                data: JSON.stringify(req),
                contentType : 'application/json',
                error: function(){
                    console.log('err');
                },
                success: function(e){
                    location.reload(true);
                }
            });
    
            pub.clearForm($('.businessInput'));
            $('#updateBtnDiv').css('display', 'none');
            $('#submitBtn').css('display', 'inline-block');
            $('#mask').remove();
            pub.disabledTrue($('#update'));
        }
    });

    $('#cancelBtn').click(function(e) {
        location.reload(true);
    });

    $('#delete').click(function(e) {
        e.preventDefault();
        pub.clearForm($('.businessInput'));
        pub.disabledTrue($('#update'));
        pub.disabledTrue($('#delete'));
        if(confirm("정말 삭제하시겠습니까 ?") == true){
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
                url: '/company/BusinessDelete',
                data: JSON.stringify(delTr),
                contentType : 'application/json',
                error: () => {
                    console.log("에러");
                },
                success: (e) => {
                    location.reload(true);
                }
            });
        } else {
            $('.trClass').css('backgroundColor', 'white');
        }
    });

    if(localStorage.getItem("value")){
        for(i=0; i<$(".trClass").length; i++){
            if($(".trClass")[i].children[3].innerText === localStorage.getItem("value")){
                $(".trClass")[i].click(this.children[0]);
            }
        }
        localStorage.removeItem("value");
    }
})