$(function() {
    const auth = () => {
		if(localStorage.getItem('token') === null) {
			$('#loginBtn').css('display','inline-block');
			$('#logoutBtn').css('display','none');
		}else if(localStorage.getItem('token') !== null){
			$('#loginBtn').css('display','none');
			$('#logoutBtn').css('display','inline-block');
		}
	}
    $("#closedEye").on("click", () => {
        console.log("click");
        $("#closedEye").css("display", "none");
        $("#eye").css("display", "inline");
        $("#passwd").prop("type", "text");
    });

    $("#eye").on("click",() => {
        $("#eye").css("display", "none");
        $("#closedEye").css("display", "inline");
        $("#passwd").prop("type", "password");
    });

    $("#submitBtn").on('click', (e) => {
        const userId = $("#id")[0].value;
        const userPw = $("#pw")[0].value;
        e.preventDefault();

        $.ajax({
            type: 'post',
            url: "/doLogin",
            data : JSON.stringify({'id': userId.replace(/ /g,""), 'pw': userPw.replace(/ /g,"")}),
            processData: false,
            contentType: "application/json;charset=UTF-8",
            error: function(xhr, status){
                alert("에러가 발생했습니다..");
            },
            success: function(data, textStatus, request){
                localStorage.setItem("token",request.getResponseHeader('Authorization'));
                if(data) window.location.href = data;
            }
        })
    });


	$('#logoutBtn').on('click', (e) => {
        e.preventDefault();
        localStorage.removeItem('token');
        window.location.href = "/logout";
	})
	auth();
})