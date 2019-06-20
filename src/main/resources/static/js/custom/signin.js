function signin(form) {
    const formArray = $(form).serializeArray();
    let link = "/";
    $.ajax({
        url: "/auth/login",
        type: "post",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: getJsonString(formArray),
        success:function (data) {
            if (data['status'] === 'fail') {
                // alert(data['message']);
                // $("#tips").text(data['message']);
                $("#modal-body").text(data['message']);
                $("#modal").modal('show');
            } else {
                window.location.href = link;
            }
        },
        error:function (xhr) {
            alert("服务端错误！请重试...");
        }
    });
    return false;
}

function getJsonString(formArray) {
    let jsonObject = {};
    $.each(formArray, function (i, item) {
        jsonObject[item.name] = item.value;
    });
    return JSON.stringify(jsonObject);
}

function cors() {
    return matchdetail();
}

function login() {
    $.ajax({
        url:"http://competition.lzlbog.club/api/auth/login",
        type: 'post',
        contentType: 'application/json;charset=utf-8',
        data:JSON.stringify(
            {
                'username': 'li',
                'password': '123456'
            }
        ),
        success: function (data) {
            alert(data['message']);
        },
        error: function (xhr) {
            alert(xhr);
        }
    });
    return false;
}

function matchlist() {
    $.ajax({
        url: "http://competition.lzlbog.club/api/match/list/1",
        type: 'get',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            alert(data['message']);
        },
        error: function (xhr) {
            alert(xhr);
        }
    });
    return false;
}

function matchdetail() {
    $.ajax({
        url: "http://competition.lzlbog.club/api/match/detail/1",
        type: 'get',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            alert(data['message']);
            alert(data['match']['matchName']);
        },
        error: function (xhr) {
            alert(xhr);
        }
    });
    return false;
}