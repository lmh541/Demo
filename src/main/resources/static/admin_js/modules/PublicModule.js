let i = 0;

export const displayScroll = function ($tbody, $target, height, cnt ) {
    if($tbody.childElementCount > cnt)
        $target.css({'height': height, 'overflow': 'scroll', 'overflow-x': 'hidden'});
}

export const optSelector = function ($selector) {
    const selectedOpt = $selector.options[$selector.selectedIndex].value
    return selectedOpt;
};

export const clearForm = function ($input) {
    for(i = 0; i < $input.length; i++) $input[i].value = null;
};

export const overlapCheck = function ($inputId, $tdId,) {
    $inputId.on('keyup', function(e) {
        e.target.value = e.target.value.replace(/ /g,"");
        
        const span = e.target.previousElementSibling;
        
        for(i=0; i<$tdId.length; i++){
            if($inputId[0].value.replace(/ /g,"") !== $tdId[i].innerText.replace(/ /g,"")){
                $(span).css('color', 'black');
            }else{
                $(span).css('color', 'red');
                break;
            }
        }
    });
}

export const autoHyphenPhoneNum = function (str) {
    str = str.replace(/[^0-9]/g, '');
    var tmp = '';
    if( str.length < 4){
        return str;
    }else if(str.length < 7){
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3);
        return tmp;
    }else if(str.length < 11){
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3, 3);
        tmp += '-';
        tmp += str.substr(6);
        return tmp;
    }else{              
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3, 4);
        tmp += '-';
        tmp += str.substr(7);
        return tmp;
    }
    return str;
};

export const clearSelectTag = function ($select) {
    $select.find("option:eq(0)").prop("selected",true);
};

export const clearCheckbox = function ($chexbox) {
    for(i = 0; i < $chexbox.length; i++){
        $($chexbox[i]).prop('checked', false)
    } 
};

export const drawTable = function (arr, index) {
    for(i = 0; i < arr.length; i++){
        let $tr = $(`<tr class="trClass"></tr>`);
        $tr.attr('id', 'tr'+index);
        $tr.css({'textAlign': 'center'});
        for(var key in arr[i][index]){
            $tr.append(`<td class="td${index}" style="height: 63px; padding-top: 23px;">${arr[i][key]}</td>`)
        }
        index++;
        $tr.click(e => {
            e.preventDefault();
            trColorSetter(e);
        });
        $('#tbody').append($('.trClass'));
    }
    

    
};

export const trColorSetter = function (e) {
    const $tr = $(e);
    let color = toggleBgc($tr, 'lightGray', 'white');
    $tr.css('backgroundColor', color);
};

export const toggleBgc = function ($target, color1, color2) {
    if($target[0].style.backgroundColor ===  'lightgray') return color2;
    return color1;
};

export const disabledTrue = function ($target) {
    $target.prop('disabled', true);
};

export const disabledFalse = function ($target) {
    $target.prop('disabled', false);
};

export const disabledDiv = function () {
    const $mask = $('<div id="mask"></div>');
    $mask.css({
        'display': 'block',
        'position': 'absolute',
        'top': -10, 
        'width': '100%',
        'height': '105%',
        'backgroundColor': 'rgba(0, 0, 0, 0.1)'})
    $('#maskBoard').append($mask);
};