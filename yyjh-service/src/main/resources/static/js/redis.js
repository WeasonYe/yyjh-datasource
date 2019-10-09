$(document).ready(function(){
	r_ins=$("#redis_conf").find(".modal-body").find("input");

	$("#r_sub").click(function(){
        if(r_ins.eq(0).val()!=''){
            $.ajax({
                type:"post",
                url:"/redis/redisConfig",
                data:{
                    'r_ip':r_ins.eq(0).val(),
                    'r_port':r_ins.eq(1).val(),
                    'r_pwd':r_ins.eq(2).val()
                },
                datatype:"json",
                success:function(data){
                    if (data.code==0){
                        alert("配置成功");
                        $("#r_clear").click();
                    }else alert("配置失败");
                }
            })
        }
	})
    $("#r_test").click(function () {
        if(r_ins.eq(0).val()!=''){
            $.ajax({
                type:"post",
                url:"/redis/configTest",
                data:{
                    'r_ip':r_ins.eq(0).val(),
                    'r_port':r_ins.eq(1).val(),
                    'r_pwd':r_ins.eq(2).val()
                },
                datatype:"json",
                success:function(data){
                    if (data.code==0){
                        alert("连接成功");
                    }else alert("连接失败");
                }
            })
        }
    })
    $("#r_clear").click(function () {
        r_ins.eq(0).val('');
        r_ins.eq(1).val('');
        r_ins.eq(2).val('');
    })
})
