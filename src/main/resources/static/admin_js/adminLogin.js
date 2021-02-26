$(document).ready(function(){
    let uri = window.location.href;
    if (uri.indexOf('error', 0) > -1) {
        alert("로그인이 실패 하였습니다.");
    }
});
$(function() {
    $("#submitBtn").on('click', (e) => {
        const userId = $("#id")[0].value;
        const userPw = $("#pw")[0].value;
        e.preventDefault();

        $.ajax({
            type: 'post',
            url: "/loginAction",
            data : JSON.stringify({'id': userId.replace(/ /g,""), 'pw': userPw.replace(/ /g,"")}),
            processData: false,
            contentType: "application/json;charset=UTF-8",
            error: function(xhr, status){
                alert("에러가 발생했습니다..");
            },
            success: function(src){
                if(src) window.location.href = "/" + src;
            }
        })
    });
})