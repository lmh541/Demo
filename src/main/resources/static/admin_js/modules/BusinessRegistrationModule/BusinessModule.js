export const autoHyphenLicenseNum = function (str) {
    str = str.replace(/[^0-9]/g, '');
    var tmp = '';
    if(str.length < 4){
        return str;
    }else if(str.length < 7){
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3);
        return tmp;
    }else{         
        tmp += str.substr(0, 3); 
        tmp += '-';
        tmp += str.substr(3, 2);
        tmp += '-';
        tmp += str.substr(5);
        return tmp;
    }
    return str;
}

export const businessInfoSetter = function () {
    const obj = {
        'business_num': '',
        'company': '',
        'ceo': '',
        'tel_main': '',
        'tel_sub': '',
        'address': '',
    };
    
    obj.business_num = $('#licenseNum')[0].value.replace(/ /g,"");
    obj.company = $('#compName')[0].value.replace(/ /g,"");
    obj.ceo = $('#representative')[0].value.replace(/ /g,"");
    obj.tel_main = $('#firstPhoneNum')[0].value.replace(/ /g,"");
    obj.tel_sub = $('#secondPhoneNum')[0].value.replace(/ /g,"");
    obj.address = $('#address')[0].value.trim();

    return obj;
};

export const tableInfoSetter = function () {
    const today = `${new Date().getFullYear()}. ${new Date().getMonth() + 1}. ${new Date().getDate()}. ${new Date().getHours()}:${new Date().getMinutes()}`;
    const obj = {
        'compName': '',
        'date': '',
        'empCnt': '',
        'licenseNum': '',
        'representative': '',
        'inCharge': '',
        'firstPhoneNum': '',
    };

    obj.compName = $('#compName')[0].value;
    obj.date = today;
    obj.empCnt = $('#empCnt')[0].value;
    obj.licenseNum = $('#licenseNum')[0].value;
    obj.representative = $('#representative')[0].value;
    obj.inCharge = $('#inCharge')[0].value;
    obj.firstPhoneNum = $('#firstPhoneNum')[0].value;

    return obj;
};