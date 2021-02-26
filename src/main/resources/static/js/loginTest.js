$(document).ready(function() {
    $("#submitBtn").click(()=>{
        $("#loading").show();
        const userId = $("#userid")[0].value;
        const userPw = $("#passwd")[0].value;

        $.ajax({
            type: 'post',
            url: "http://divus.iptime.org:4200/peoplecar/login",
            data : JSON.stringify({"userid": userId, "password": userPw}),
            dataType: 'json',
            processData: false,
            contentType: false,
            error: function(xhr, status){
                $("#loading").fadeOut("slow");
                alert("에러가 발생했습니다..");
            },
            success: function(_token){
                $("#loading").fadeOut("slow");
                console.log(_token);
                let token = "Bearer "+_token.access_token;
                if(_token.result == "success") {
                    $("#form").append("<input/>", {type: 'submit', value: token});
                    $("#form").submit();
                } else {
                    alert("없는 계정입니다.");
                }
            }
        })
    });
})
