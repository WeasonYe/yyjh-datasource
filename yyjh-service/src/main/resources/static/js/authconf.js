$(document).ready(function() {
	$("#add-auth-rp-btn").click(function() {
		$("#auth_rp_conf").modal("show");
	})

	$("#show_ur").click(function() {
		init_ur_table();
		show_ur_table();
		show_users();
	})

	$("#show_rp").click(function() {
		init_rp_table();
		show_rp_table();
		show_roles();
	})

	$("#auth_ur_submit").click(function() {
		let username = $("#ur_username").text();
		let checkedboxes = $("#ur_check").find("input:checked");
		let rolenames = [];

		if(checkedboxes != null && checkedboxes.length != 0) {
			for(let i = 0; i < checkedboxes.length; i++) {
				rolenames.push($(checkedboxes[i]).val());
			}
			let confdata = {
				username: username,
				rolenames: rolenames
			}

			$.ajax({
				data: { conf_info: JSON.stringify(confdata) },
				type: "post",
				url: "/authority/confUserRole",
				async: false,
				dataType: "json",
				success: function(result, status, xhr) {
					if(result.code == 0) {
						alert("提交配置成功")
						$("#auth_ur_conf").modal("hide");
						init_ur_table();
						show_ur_table();
					} else {
						alert("提交配置失败")
					}
				}
			});
		} else {
			alert("未选择角色");
			$("#auth_ur_conf").modal("hide");
		}

	})
	
	$("#auth_rp_submit").click(function() {
		let rolename = getOriRolename($("#rp_rolename").text());
		let checkedboxes = $("#rp_check").find("input:checked");
		let permissions = [];

		if(checkedboxes != null && checkedboxes.length != 0) {
			for(let i = 0; i < checkedboxes.length; i++) {
				permissions.push($(checkedboxes[i]).val());
			}
			let confdata = {
				rolename: rolename,
				permissions: permissions
			}

			$.ajax({
				data: { conf_info: JSON.stringify(confdata) },
				type: "post",
				url: "/authority/confRolePer",
				async: false,
				dataType: "json",
				success: function(result, status, xhr) {
					if(result.code == 0) {
						alert("提交配置成功")
						$("#auth_rp_conf").modal("hide");
						init_rp_table();
						show_rp_table();
					} else {
						alert("提交配置失败")
					}
				}
			});
		} else {
			alert("未选择角色");
			$("#auth_rp_conf").modal("hide");
		}
		
		function getOriRolename(rolename){
			switch(rolename){
				case "数据管理员":
					return "data_manager";
					break;
				case "权限管理员":
					return "auth_manager";
					break;
				default:
					return rolename;
			}
		}
	})
})

var roles = [];
var users = [];
var urdatas = [];
var rpdatas = [];

//获取表数据
function init_ur_table() {
	users = [];
	urdatas = [];
	$.ajax({
		type: "post",
		url: "/authority/getUserRoles",
		async: false,
		dataType: "json",
		success: function(result, status, xhr) {
			if(result.code == 0) {
				users = result.payload.users;
				let datas = result.payload.urs;
				for(let i = 0; i < datas.length; i++) {
					let ur = {
						id: datas[i].id,
						user_id: datas[i].user_id,
						loginid: datas[i].loginid,
						role_id: datas[i].role_id,
						rolename: datas[i].rolename
					};
					urdatas.push(ur);
				}
			} else {
				alert("初始化表失败")
			}
		}
	});
}

//展示可以被配置角色的用户
function show_users() {
	$("#user_table").bootstrapTable({
		striped: true, //是否显示行间隔色
		cache: false, //是否使用缓存
		pageNumber: 1, //初始化加载第一页
		pagination: true, //是否分页
		sortable: false,
		sidePagination: 'client', //server:服务器端分页|client：前端分页
		pageSize: 10, //单页记录数
		pageList: [10, 20, 30, 40], //可选择单页记录数
		search: true, //刷新按钮
		strictSearch: false,
		showColumns: true, //是否显示所有的列
		showRefresh: true, //是否显示刷新按钮
		minimumCountColumns: 2, //最少允许的列数
		clickToSelect: true, //是否启用点击选中行
		showToggle: false, //是否显示详细试图和列表视图的切换按钮
		cardView: false, //是否显示详细视图
		detailView: false,
		columns: [{
				field: "id",
				title: "ID",
				visible: true
			},
			{
				field: "loginid",
				title: "用户名",
				visible: true
			},
			{
				field: "operate",
				title: "操作",
				visible: true,
				formatter: addurbtns
			}
		]
	});

	function addurbtns(value, row, index) {
		return "<button onclick='conf_ur(this)' class='btn btn-success add-btn'>配置角色</button>";
	}
	$("#user_table").bootstrapTable("load", users);
}

//展示已配置的用户-角色表
function show_ur_table() {
	$("#ur_table").bootstrapTable({
		striped: true, //是否显示行间隔色
		cache: false, //是否使用缓存
		pageNumber: 1, //初始化加载第一页
		pagination: true, //是否分页
		sortable: false,
		sidePagination: 'client', //server:服务器端分页|client：前端分页
		pageSize: 10, //单页记录数
		pageList: [10, 20, 30, 40], //可选择单页记录数
		search: true, //刷新按钮
		strictSearch: false,
		showColumns: true, //是否显示所有的列
		showRefresh: true, //是否显示刷新按钮
		minimumCountColumns: 2, //最少允许的列数
		clickToSelect: true, //是否启用点击选中行
		showToggle: false, //是否显示详细试图和列表视图的切换按钮
		cardView: false, //是否显示详细视图
		detailView: false,
		columns: [{
				field: "id",
				title: "ID",
				visible: true
			},
			{
				field: "user_id",
				title: "用户ID",
				visible: true
			},
			{
				field: "loginid",
				title: "用户名",
				visible: true
			},
			{
				field: "role_id",
				title: "角色ID",
				visible: true
			},
			{
				field: "rolename",
				title: "角色名",
				visible: true,
				formatter: rolenameFormat
			},
			{
				field: "operate",
				title: "操作",
				visible: true,
				formatter: addurbtns
			}
		]
	});

	function addurbtns(value, row, index) {
		return '<button onclick="show_del_ur(this)" class="btn btn-danger">删除</button>';
	}

	function rolenameFormat(value, row, index) {
		switch(value) {
			case "data_manager":
				return "数据管理员";
				break;
			case "auth_manager":
				return "权限管理员";
				break;
			default:
				return value;
		}
	}
	$("#ur_table").bootstrapTable("load", urdatas);
}
//为用户配置角色
function conf_ur(thisbtn) {
	let id = $(thisbtn).parent().parent().find('td').eq(0).text();
	let username = $(thisbtn).parent().parent().find('td').eq(1).text();
	$.ajax({
		data: { u_id: id },
		type: "post",
		url: "/authority/getOneUR",
		async: false,
		dataType: "json",
		success: function(result, status, xhr) {
			if(result.code == 0) {
				let rlist = result.payload;
				let checkhtml = "";
				for(let i = 0; i < rlist.length; i++) {
					checkhtml += "<input type='checkbox' value='" + rlist[i].rolename + "'>" + getCNname(rlist[i].rolename) + "</input>&nbsp;";
				}
				$("#auth_ur_conf").modal("show");
				$("#ur_username").text(username);
				$("#ur_check").html(checkhtml);
			} else {
				alert("此用户无可配角色了")
			}
		}
	});

	function getCNname(rolename) {
		switch(rolename) {
			case "data_manager":
				return "数据管理员";
				break;
			case "auth_manager":
				return "权限管理员";
				break;
			default:
				return rolename;
		}
	}
}

function show_del_ur(thisbtn) {
	let id = $(thisbtn).parent().parent().find('td').eq(0).text();
	let user = $(thisbtn).parent().parent().find('td').eq(2).text();
	let rolename = $(thisbtn).parent().parent().find('td').eq(4).text();
	$("#del_ur_confirm").modal("show");
	$("#del_ur_id").text(id);
	$("#del_ur_username").text(user);
	$("#del_ur_rolename").text(rolename);
	
}

function del_ur(){
	let id = $("#del_ur_id").text();
	$.ajax({
		type: "post",
		url: "/authority/delUserRole",
		async: false,
		data: { del_ur_id: id },
		dataType: "json",
		success: function(result, status, xhr) {
			if(result.code == 0) {
				$("#del_ur_confirm").modal("hide");
				alert("删除成功")
				init_ur_table();
				show_ur_table();
			} else {
				$("#del_ur_confirm").modal("hide");
				alert("删除失败")
			}
		}
	});
}

function init_rp_table() {
	rpdatas = [];
	$.ajax({
		type: "post",
		url: "/authority/getRolePer",
		async: false,
		dataType: "json",
		success: function(result, status, xhr) {
			if(result.code == 0) {
				roles = result.payload.roles;
				let datas = result.payload.rps;
				for(let i = 0; i < datas.length; i++) {
					let ur = {
						id: datas[i].id,
						role_id: datas[i].role_id,
						rolename: datas[i].rolename,
						permission_id: datas[i].permission_id,
						permission: datas[i].permission
					};
					rpdatas.push(ur);
				}
			} else {
				alert("初始化表失败")
			}
		}
	});
}

function show_rp_table() {
	$("#rp_table").bootstrapTable({
		striped: true, //是否显示行间隔色
		cache: false, //是否使用缓存
		pageNumber: 1, //初始化加载第一页
		pagination: true, //是否分页
		sortable: false,
		sidePagination: 'client', //server:服务器端分页|client：前端分页
		pageSize: 10, //单页记录数
		pageList: [10, 20, 30, 40], //可选择单页记录数
		search: true, //刷新按钮
		strictSearch: false,
		showColumns: true, //是否显示所有的列
		showRefresh: true, //是否显示刷新按钮
		minimumCountColumns: 2, //最少允许的列数
		clickToSelect: true, //是否启用点击选中行
		showToggle: false, //是否显示详细试图和列表视图的切换按钮
		cardView: false, //是否显示详细视图
		detailView: false,
		columns: [
			{
				field: "id",
				title: "ID",
				visible: true
			},
			{
				field: "role_id",
				title: "角色ID",
				visible: true
			},
			{
				field: "rolename",
				title: "角色名",
				visible: true,
				formatter: rolenameFormat
			},
			{
				field: "permission_id",
				title: "操作权限ID",
				visible: true
			},
			{
				field: "permission",
				title: "操作名",
				visible: true,
				formatter: permissionFormat
			},
			{
				field: "operate",
				title: "操作",
				visible: true,
				formatter: addurbtns
			}
		]
	});

	function addurbtns(value, row, index) {
		return '<button onclick="show_del_rp(this)" class="btn btn-danger">删除</button>';
	}
	
	function rolenameFormat(value, row, index) {
		switch(value) {
			case "data_manager":
				return "数据管理员";
				break;
			case "auth_manager":
				return "权限管理员";
				break;
			default:
				return value;
		}
	}
	
	function permissionFormat(value, row, index) {
		switch(value) {
			case "add":
				return "添加数据源操作";
				break;
			case "delete":
				return "删除数据源操作";
				break;
			case "update":
				return "修改数据源操作";
				break;
			case "select":
				return "查看数据源操作";
				break;
			case "u_r_conf":
				return "用户-角色配置";
				break;
			case "r_p_conf":
				return "角色-操作配置";
				break;
			default:
				return value;
		}
	}
	
	$("#rp_table").bootstrapTable("load", rpdatas);
}

function show_roles() {
	$("#role_table").bootstrapTable({
		striped: true, //是否显示行间隔色
		cache: false, //是否使用缓存
		pageNumber: 1, //初始化加载第一页
		pagination: true, //是否分页
		sortable: false,
		sidePagination: 'client', //server:服务器端分页|client：前端分页
		pageSize: 10, //单页记录数
		pageList: [10, 20, 30, 40], //可选择单页记录数
		search: true, //刷新按钮
		strictSearch: false,
		showColumns: true, //是否显示所有的列
		showRefresh: true, //是否显示刷新按钮
		minimumCountColumns: 2, //最少允许的列数
		clickToSelect: true, //是否启用点击选中行
		showToggle: false, //是否显示详细试图和列表视图的切换按钮
		cardView: false, //是否显示详细视图
		detailView: false,
		columns: [{
				field: "id",
				title: "ID",
				visible: true
			},
			{
				field: "rolename",
				title: "角色名",
				visible: true,
				formatter: rolenameFormat
			},
			{
				field: "operate",
				title: "操作",
				visible: true,
				formatter: addurbtns
			}
		]
	});

	function addurbtns(value, row, index) {
		return "<button onclick='conf_rp(this)' class='btn btn-success add-btn'>配置操作权限</button>";
	}

	function rolenameFormat(value, row, index) {
		switch(value) {
			case "super_admin":
				return "超级管理员";
				break;
			case "data_manager":
				return "数据管理员";
				break;
			case "auth_manager":
				return "权限管理员";
				break;
			default:
				return value;
		}
	}
	$("#role_table").bootstrapTable("load", roles);
}

function conf_rp(thisbtn) {
	let id = $(thisbtn).parent().parent().find('td').eq(0).text();
	let rolename = $(thisbtn).parent().parent().find('td').eq(1).text();
	$.ajax({
		data: { r_id: id },
		type: "post",
		url: "/authority/getOneRP",
		async: false,
		dataType: "json",
		success: function(result, status, xhr) {
			if(result.code == 0) {
				let plist = result.payload;
				let checkhtml = "";
				for(let i = 0; i < plist.length; i++) {
					checkhtml += "<input type='checkbox' value='" + plist[i].permission + "'>" + getCNname(plist[i].permission) + "</input>&nbsp;";
				}
				$("#auth_rp_conf").modal("show");
				$("#rp_rolename").text(rolename);
				$("#rp_check").html(checkhtml);
			} else {
				alert("此角色无可配权限了")
			}
		}
	});

	function getCNname(permission) {
		switch(permission) {
			case "add":
				return "添加数据源操作";
				break;
			case "delete":
				return "删除数据源操作";
				break;
			case "update":
				return "修改数据源操作";
				break;
			case "select":
				return "查看数据源操作";
				break;
			case "u_r_conf":
				return "用户-角色配置";
				break;
			case "r_p_conf":
				return "角色-操作配置";
				break;
			default:
				return permission;
		}
	}
}

function show_del_rp(thisbtn) {
	let id = $(thisbtn).parent().parent().find('td').eq(0).text();
	let rolename = $(thisbtn).parent().parent().find('td').eq(2).text();
	let permission = $(thisbtn).parent().parent().find('td').eq(4).text();
	$("#del_rp_confirm").modal("show");
	$("#del_rp_id").text(id);
	$("#del_rp_rolename").text(rolename);
	$("#del_rp_permission").text(permission);
	
}

function del_rp(){
	let id = $("#del_rp_id").text();
	
	console.log(id);
	
	$.ajax({
		type: "post",
		url: "/authority/delRolePer",
		async: false,
		data: { del_rp_id: id },
		dataType: "json",
		success: function(result, status, xhr) {
			if(result.code == 0) {
				$("#del_rp_confirm").modal("hide");
				alert("删除成功");
				init_rp_table();
				show_rp_table();
			} else {
				$("#del_rp_confirm").modal("hide");
				alert("删除失败");
			}
		}
	});
}