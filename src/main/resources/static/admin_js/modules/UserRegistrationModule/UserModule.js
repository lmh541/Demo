import * as pub from '../PublicModule.js'

let i = 0;
let j = 0;

export const authorization = function ($authArr) {
    let auth = '';

    for(i = 0; i < $authArr.length; i++) {
        if($($authArr[i]).is(":checked")) auth += $authArr[i].id + ', ';
    }

    return auth.slice(0, -2);
};

export const dec2bin = function(dec){
    let num = (dec >>> 0).toString(2);

    while(num.length < 8) num = '0'+num;
    
    return num;
}

export const drawPerm = function($trClass){
    for(i = 0; i < $trClass.length; i++){
        $trClass[i].children[3].innerText = $trClass[i].children[3].innerText.slice(0, 19);
        
        switch ($trClass[i].children[8].innerText) {
            case '64':
                $trClass[i].children[8].innerText = '손님';
                break;
            case '128':
                $trClass[i].children[8].innerText = '사용자';
                break;
            case '0':
                $trClass[i].children[8].innerText = '관리자';
                break;
        }

        let funcArr = dec2bin(Number($trClass[i].children[9].innerText)).split('');
        const funcName = ['DD', 'CS', 'CPS', 'AN'];
        let func = '';
        
        for(j = 0; j < funcArr.length; j++) {
            if(funcArr[j] === "1") func += `${funcName[j]}, `
        }

        $trClass[i].children[9].innerText = func.slice(0, -2);
    }
}

export const userInfoSetter = function () {
    const obj = {
        'company': '',
        'ceo': '',
        'name': '',
        'id': '',
        'pw': '',
        'tel': '',
        'user_perm': '',
        'menu_func': '',
    };

    let perm = pub.optSelector($('#select')[0]);
    switch (perm) {
        case 'user':
            perm = Number((128 >>> 0).toString(2));
            break;
        case 'customer':
            perm = Number((64 >>> 0).toString(2));
            break;
        case 'manager':
            perm = 0;
            break;
    }

    obj.company = pub.optSelector($('#selectComp')[0]);
    obj.ceo = $('#ceo')[0].innerText.replace(/ /g,"");
    obj.name = $('#name')[0].value.replace(/ /g,"");
    obj.id = $('#userId')[0].value.replace(/ /g,"");
    obj.pw = $('#userPw')[0].value.replace(/ /g,"");
    obj.tel = $('#firstPhoneNum')[0].value.replace(/ /g,"");
    obj.user_perm = pub.optSelector($('#select')[0]);
    obj.menu_func = authorization($('.auth'));

    return obj;
};

export const tableInfoSetter = function () {
    const today = `${new Date().getFullYear()}. ${new Date().getMonth() + 1}. ${new Date().getDate()}. ${new Date().getHours()}:${new Date().getMinutes()}`;
    const obj = {
        'compName': '',
        'date': '',
        'name': '',
        'userId': '',
        'representative': '',
        'firstPhoneNum': '',
        'userCont': '',
        'auth': ''
    };

    obj.compName = $('#compName')[0].value;
    obj.date = today;
    obj.name = $('#name')[0].value;
    obj.userId = $('#userId')[0].value;
    obj.representative = $('#representative')[0].value;
    obj.firstPhoneNum = $('#firstPhoneNum')[0].value;
    obj.userCont = pub.optSelector($('#select')[0]);
    obj.auth = authorization($('.auth'));

    return obj;
};