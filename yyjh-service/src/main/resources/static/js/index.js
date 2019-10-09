$(document).ready(function(){
	log_ins=$("#login").find("div").find(".modal-body").find("input");

	$("#log_sub").click(function(){

        if(log_ins.eq(0).val()!=''){
            if(findCookie(log_ins.eq(0).val())) alert("already")
            else {
                $.ajax({
                    type:"post",
                    url:"/index/login",
                    data:{
                        'log_id':log_ins.eq(0).val(),
                        'log_email':log_ins.eq(1).val(),
                        'log_tel':log_ins.eq(2).val(),
                        'log_pwd':log_ins.eq(3).val()
                    },
                    datatype:"json",
                    success:function(data){
                        if (data.code==0){
                            setCookie(log_ins.eq(0).val())
                            window.location.href="/router/datasource"
                        }else alert("登录失败")
                    }
                })
			}
        }
	})
	
	$("#sign_sub").click(function(){
		let signup_datas = {
			"username" : $("#sign_id").val(),
			"pwd" : $("#sign_pwd").val(),
			"tel" : $("#sign_tel").val(),
			"email" : $("#sign_email").val(),
			"nick" : $("#sign_nick").val(),
		}
		$.ajax({
                    type:"post",
                    url:"/index/signup",
                    data:{
                        "signup_datas": JSON.stringify(signup_datas)
                    },
                    datatype:"json",
                    success:function(data){
                        if (data.code==0){
                        	$("#sign_clear").click();
                            $("#signup").modal("hide");
                        }else alert("登录失败")
                    }
                })
	})

    function setCookie(value){
        var nameupdat
        if (document.cookie.length === 0) {
            // 第一次操作
            nameupdate = value
        }else {
            // 取出cookie中各个用户名的值，与当前值比较，如果当前值已存在，不改变cookie，否则写入新的cookie
            var data = document.cookie.split('=')[1].split('&')
            for (var i = 0;i < data.length;i++) {
                if (data[i] === value) {
                    return
                }
            }
            nameupdate = document.cookie.split('=')[1] + '&' + value
        }
        // 字符串拼接cookie，toGMTString() 方法根据格林威治时间 (GMT) 把 Date 对象转换为字符串
        document.cookie = 'user' + '=' + nameupdate
    }

    function findCookie(value){
        if (document.cookie.length > 0) {
            // 得到cookie中value的值，一个拼接的字符串
            var data1 = document.cookie.split('=')[1]
            // 得到各个value的值
            var data2 = data1.split('&')
            for (var i = 0; i < data2.length; i++) {
                if (data2[i] === value) {
                    return true
                }
            }
        }
        return false
    }
    function delCookie(value){
        var data = document.cookie.split('=')[1].split('&')
        var data2 = ""
        for(var i = 0;i < data.length;i++){
            if(value!=data[i]&&''!=data[i]&&'&'!=data[i]){
                data2+=data[i]
                if(i!=data.length-1) data2+='&'
            }
        }
        document.cookie = "user" + '=' + data2
    }
})
