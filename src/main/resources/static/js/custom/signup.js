//获取验证码
function getValidationCode(button) {

    const email = $("#email").val().trim();
    if (email === "") {
        alert("请填写正确的邮件地址！");
        return false;
    }
    alert("发送验证码到： " + email);
    $.ajax({
        url: "/auth/sendCode",
        type: "POST",
        contentType: "application/json;charset=utf-8",  //request提交数据类型
        // dataType: "json",    //返回值类型
        data: JSON.stringify({
            'email': email,
            'sendFor': '1'
        }),
        success: function (data) {
            $("#modal-body").text(data['message']);
            $("#modal").modal('show');
        },
        error: function (xhr) {
            alert("发送失败！");
        }
    });
    //倒计时
    $(button).addClass("disabled");
    $(button).attr("disabled", true);
    let time = 60;
    $(button).text(time + "s");
    setInterval(() => {
        if (time <= 0) {
            $(button).removeClass("disabled");
            $(button).attr("disabled", false);
            $(button).text("获取验证码");
            return false;
        }
        time--;
        $(button).text(time + 's');
    }, 1000);
}

//提交表单
function signup(form) {
    const formArray = $(form).serializeArray();
    let link = "/";
    $.ajax({
        url: "/auth/signup",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: getJsonString(formArray),
        success:function (data) {
            if (data['status'] === 'fail') {
                let msg = data['message'] + " \n ";
                let errors = data['errors'];
                $.each(errors, function (key) {
                    msg = msg + key + ": " + errors[key] + " \n ";
                });
                $("#modal-body").text(msg);
                $("#modal").modal('show');
            } else {
                window.location.href = link;
            }
        },
        error:function () {
            alert("服务端错误！请重试...");
        }
    });
    return false;
}

//将form表单转成json字符串
function getJsonString(formArray) {
    let jsonObject = {};
    $.each(formArray, function (i, item) {
        jsonObject[item.name] = item.value;
    });
    return JSON.stringify(jsonObject);
}

