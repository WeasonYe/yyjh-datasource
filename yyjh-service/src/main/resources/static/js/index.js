$(document).ready(function(){
	log_ins=$("#login").find("div").find(".modal-body").find("input");
    var codeStr = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    var insCode = "";
    // 用来生成随机整数
    function getRandom(n, m) { // param: (Number, Number)
        n = Number(n);
        m = Number(m);
        // 确保 m 始终大于 n
        if (n > m) {
            var temp = n;
            n = m;
            m = temp;
        }
        return Math.floor(Math.random()*(m - n) + n);
    }
    // 将随机生成的整数下标对应的字母放入div中
    function getCode() {
        var codeDiv = document.getElementById('code');
        var str = '';
        // 验证码有几位就循环几次
        for (var i = 0;i < 4;i ++) {
            var ran = getRandom(0, 62);
            str += codeStr.charAt(ran);
        }
        insCode = str;
        codeDiv.innerHTML = str;
    }
    getCode();
    $("#code").click(function () {
        getCode();
    })
	$("#log_sub").click(function(){
        if(log_ins.eq(2).val()===insCode){
            if(log_ins.eq(0).val()!=''){
                $.ajax({
                    type:"post",
                    url:"/index/login",
                    data:{
                        'log_param':log_ins.eq(0).val(),
                        'log_pwd':log_ins.eq(1).val()
                    },
                    datatype:"json",
                    success:function(data){
                        if (data.code==0){
                            sessionStorage.setItem("log_id",data.payload.log_id)
                            window.location.href="/router/datasource"
                        }else{
                            alert("登录失败");
                            getCode();
                        }
                    }
                })
            }
        }else{
            alert("验证码错误");
            getCode();
        }
	})
    $("#log_clear").click(function () {
        log_ins.eq(0).val('');
        log_ins.eq(1).val('');
        log_ins.eq(2).val('');
        getCode();
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
})
