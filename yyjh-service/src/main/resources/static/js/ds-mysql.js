$(document).ready(function(){
	$("#mysql_conf_close").click(function(){
		$("#mysql_import").modal("show");
	});
	
	$("#mysql_submit").click(function(){
		$("#mysql_import").modal("hide");
		$.ajax({
			type:"post",
			url:"/mysql/mysqlUpload",
			async:false,
			data:{mysql_conf_str:JSON.stringify(mysql_datas)},
			dataType:"json",
			success:function(result,status,xhr){
				if(result.code == 0){
					alert("上传成功！");
				}else{
					alert("上传失败");
				}
			},
			error: function(result){
				alert("抱歉，您没有该操作的权限！");
			}
		});
	});
})

function addmysqlconf(){
	$("#mysql_import").modal("hide");
	$("#mysql_conf").modal("show");
	$("#m_c_reset").click();
}

var mysql_datas = [];

function init_mysql_datas(){
	mysql_datas = [];
	$("#mysql_datas").dataTable().fnDestroy();
	let table = $("#mysql_datas").DataTable({
		data: mysql_datas, //放入数据
		searching: false,
		autoFill: true,
		bAutoWidth: true,
		bLengthChange: false,
		"language": {
			oPaginate: {
				"sPrevious": "上一页",
				"sNext": "下一页",
			},
		},
		"info": false, //是否显示页脚信息
		pageLength: 7, //显示个数table
		//对应每列显示的数据
		columns: [{
				"data": "url",
				sortable: false
			},
			{
				"data": "database_name",
				sortable: false
			},
			{
				"data": "port",
				sortable: false
			},
			{
				"data": "username",
				sortable: false
			},
			{
				"data": "encode",
				sortable: false
			},
			{
				"data": "description",
				sortable:false
			}
		]
	})
}

function m_confirm(){
	let inputs = $("#mysql_conf_info").find("input");
	let url_name = $(inputs[0]).val();
	let database_name = $(inputs[1]).val();
	let port = $(inputs[2]).val();
	let username = $(inputs[3]).val();
	let pwd = $(inputs[4]).val();
	let encode = $(inputs[5]).val();
	let des = $(inputs[6]).val();
	
	let m_data = {
		"url": url_name,
		"database_name": database_name,
		"port": port,
		"username": username,
		"password": pwd,
		"encode": encode,
		"description": des
	};
	mysql_datas.push(m_data);
	
	$("#mysql_conf").modal("hide");
	$("#mysql_import").modal("show");
	
	$("#mysql_datas").dataTable().fnDestroy();
	let table = $("#mysql_datas").DataTable({
		data: mysql_datas, //放入数据
		searching: false,
		autoFill: true,
		bAutoWidth: true,
		bLengthChange: false,
		"language": {
			oPaginate: {
				"sPrevious": "上一页",
				"sNext": "下一页",
			},
		},
		"info": false, //是否显示页脚信息
		pageLength: 7, //显示个数table
		//对应每列显示的数据
		columns: [{
				"data": "url",
				sortable: false
			},
			{
				"data": "database_name",
				sortable: false
			},
			{
				"data": "port",
				sortable: false
			},
			{
				"data": "username",
				sortable: false
			},
			{
				"data": "encode",
				sortable: false
			},
			{
				"data": "description",
				sortable:false
			}
		]
	});
}
