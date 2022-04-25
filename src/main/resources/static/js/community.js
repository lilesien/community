function post(){
    var question_id = $("#question_id").val();
    var content = $("#comment_content").val();
    if(!content){
        alert("不能回复空内容~~~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": question_id,
            "content": content,
            "type": 1
        }),
        success: function (response){
            console.log(response);
            if(response.code == 200){
                window.location.reload();
            }else {
                if(response.code == 2003){
                    var con = confirm(response.message);
                    //可以获取当前页面的URL地址
                    var prevLink = window.location.href;
                    if(con){
                        sessionStorage.setItem("gotoUrl","https://github.com/login/oauth/authorize?client_id=Iv1.933474e281b2fa6b&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        sessionStorage.setItem("returnUrl",prevLink);
                        window.location.href = "http://localhost:8080/";
                    }
                }else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    })
}