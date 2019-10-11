$(document).ready(function() {

	//点击数据源控制按钮
	$("#control-btn").click(function() {
		let boxs = $(".box");
		for(let i = 0; i < boxs.length; i++) {
			$(boxs[i]).css('display', 'none');
		}
		$("#box1").css('display', 'block');
		show_datasource_table();
	});
	//确定删除按钮
	$("#delete_sure").click(function() {
		var id = chosenId;
		var data = {
			'id': id
		};
//		if(fileType = "excel") {
			warehouseurl = "/datasource/delTDataSourceById";
//		} else if(fileType = "csv") {
//			warehouseurl = "";
//		} else if(fileType = "mysql") {
//			warehouseurl = "";
//		} else if(fileType = "redis") {
//			warehouseurl = "";
//		}
		$.ajax({
			type: "GET",
			url: warehouseurl,
			data: data,
			dataType: "json",
			async: false,
			success: function(result) {
				if(result.code == -1) {
					alert("入库失败");
				} else if(result.code == 0) {
					show_datasource_table();
				}
			},
			error: function(result){
				alert("抱歉，您没有该操作的权限！");
			}
		});
		$("#deleteSource").modal('hide');
	});

	//确定上传按钮
	$("#warehouse_sure").click(function() {
		var id = chosenId;

		let url = "";
		let data = {};
		if(fileType == "excel") {
			url = "/excel/excelWarehouse";
			data = {
				id:id
			}
		} else if(fileType == "csv") {
			url = "/csv/csvSave";
			data = {
				csvsave_id:id,
				csvsave_interpret:" "
			}
		} else if(fileType == "mysql") {
			url = "/mysql/mysqlSave";
			let tablesel = $("input:checked");
			let chetables = []
			for(let i = 0; i < tablesel.length; i++){
				chetables.push($(tablesel[i]).val());
			}
			let tables = {
				ds_id:id,
				tables_name:chetables
			}
			data = {
				tables_name:JSON.stringify(tables)
			}
		} else if(fileType == "redis") {
			url = "/redis/redisInDb";
			let dbsel = $("input:checked");
			let dbs = []
			for(let i = 0; i < dbsel.length; i++){
				dbs.push($(dbsel[i]).val().substring(2));
			}
			data = {
				'db_nums':JSON.stringify(dbs)
			}
			
		}
		
		$.ajax({
			type: "GET",
			url: url,
			data: data,
			dataType: "json",
			async: false,
			success: function(result) {
				if(result.code == -1) {
					alert("上传失败");
				} else if(result.code == 0) {
					alert("上传成功")
					show_datasource_table();
				}
			},
			error: function(result){
				alert("抱歉，您没有该操作的权限！");
			}
		});
		$("#warehouseSource").modal('hide');
	});

	$('#show_table').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function(e, rows) {
		var datas = $.isArray(rows) ? rows : [rows]; // 点击时获取选中的行或取消选中的行
		examine(e.type, datas); // 保存到全局 Array() 里
	});

	$("input[name='btSelectAll']").click(function() {
		checkall();
	})

	//个人信息展示
	$("#self").click(function() {
		let boxs = $(".box");
		for(let i = 0; i < boxs.length; i++) {
			$(boxs[i]).css('display', 'none');
		}
		$("#box4").css('display', 'block');
		let self_info_html = "<div id='self_info' class='row'>";
		$.ajax({
			type: "post",
			url: "/user/getself",
			async: false,
			data: { log_id: sessionStorage.getItem('log_id') },
			dataType: "json",
			success: function(result, status, xhr) {
				if(result.code == 0) {
					let user = result.payload;
					self_info_html += "<div class='row'><span class='col-lg-2'>头像</span><img src='../../img/" + user.userimg + "'/></div>"
					self_info_html += "<div class='row'><span class='col-lg-2'>用户ID</span><span class='col-lg-10'>" + user.id + "</span></div>"
					self_info_html += "<div class='row'><span class='col-lg-2'>账号</span><span class='col-lg-10'>" + user.loginid + "</span></div>"
					self_info_html += "<div class='row'><span class='col-lg-2'>昵称</span><span class='col-lg-10'>" + user.nickname + "</span></div>"
					self_info_html += "<div class='row'><span class='col-lg-2'>邮箱</span><span class='col-lg-10'>" + user.email + "</span></div>"
					self_info_html += "<div class='row'><span class='col-lg-2'>手机号码</span><span class='col-lg-10'>" + user.tel + "</span></div>"
					self_info_html += "</div>"

					$("#box4").html($(self_info_html));
				} else {
					alert("获取个人信息失败");
				}
			}
		})
	})
	
	$("#time_conf").change(function(){
		let id = $(this).val();
		$.ajax({
			type: "post",
			url: "/dateOper/setForm",
			async: false,
			data: { id:id },
			dataType: "json",
			success: function(result, status, xhr) {
				if(result.code == 0) {
					alert("设置成功");
					init_dateForm();
					show_datasource_table();
				} else {
					alert("设置时间格式失败");
				}
			},
			error: function(result, status, xhr){
				alert("抱歉，您没有该权限");
				$("#time_conf").val(dateFormId);
			}
		})
	})
});

var overAllIds = new Array(); //全局数组

var dateDomain;
var dateFormId;
function dateOper(dateDomain) {
	if(dateDomain != null && dateDomain != undefined){
		var dateParts = new Array();
        var ymd = new Array();
        var hms = new Array();
        dateParts = dateDomain.split(' ');
        ymd = dateParts[0].split('-');
        hms = dateParts[1].split(':');
        switch(dateFormId) {
            case 1:
                return ymd[0]+"年"+ymd[1]+"月"+ymd[2]+"日 "+hms[0]+"时"+hms[1]+"分"+hms[2]+"秒";
            case 2:
                return ymd[2]+"/"+ymd[1]+"/"+ymd[0];
            case 3:
                return ymd[0]+"年"+ymd[1]+"月"+ymd[2]+"日";
            case 4:
                return ymd[2]+"/"+ymd[1]+"/"+ymd[0]+" "+hms[0]+":"+hms[1]+":"+hms[2];
            case 5:
                return hms[0]+":"+hms[1]+":"+hms[2];
            default:
                return dateDomain;
        }
	}      
}

init_dateForm()
function init_dateForm(){
	$.ajax({
			type: "post",
			url: "/dateOper/getForm",
			async: true,
			dataType: "json",
			success: function(result, status, xhr) {
				if(result.code == 0) {
					dateFormId = result.payload.id
					$("#time_conf").val(dateFormId);
				} 
			}
		})
}


function examine(type, datas) {
	if(type.indexOf('uncheck') == -1) {
		$.each(datas, function(i, v) {
			// 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
			overAllIds.indexOf(v.id) == -1 ? overAllIds.push(v.id) : -1;
		});
	} else {
		$.each(datas, function(i, v) {
			overAllIds.splice(overAllIds.indexOf(v.id), 1); //删除取消选中行
		});
	}
	console.log(overAllIds);
}
//跨页全选方法
function checkall() {
	$.ajax({
		type: "POST",
		url: '/datasource/getAll',
		dataType: 'json',
		success: function(result) {
			console.log(result);
			var data = result.data;
			overAllIds.splice(0, overAllIds.length); //清空overAllIds数组
			for(var i = 0; i < data.length; i++) {
				overAllIds.push(parseInt(data[i].id));
			}
			console.log(overAllIds);
			show_datasource_table();
		},
			error: function(result){
				alert("抱歉，您没有该操作的权限！");
			}
	});
}

//数据源管理模态框展示数据
function show_datasource_table() {
	//初始化搜索栏
	$("#content_search").html("");
	$('#show_table').bootstrapTable('destroy');
	$('#show_table').bootstrapTable({
		url: "/datasource/getTDatasourceListPage", //请求后台的URL（*）
		method: 'get',
		dataType: "json",
		dataField: 'rows',
		striped: true, //设置为 true 会有隔行变色效果
		undefinedText: "", //当数据为 undefined 时显示的字符
		pagination: true, //设置为 true 会在表格底部显示分页条。
		showToggle: "false", //是否显示 切换试图（table/card）按钮
		showColumns: "true", //是否显示 内容列下拉框
		pageNumber: 1, //初始化加载第一页，默认第一页
		pageSize: 10, //每页的记录行数（*）
		pageList: [10, 20, 30, 40], //可供选择的每页的行数（*），当记录条数大于最小可选择条数时才会出现
		paginationPreText: '上一页',
		paginationNextText: '下一页',
		search: false, //是否显示表格搜索,bootstrap-table服务器分页不能使用搜索功能，可以自定义搜索框，操作方法已经给出
		striped: true, //隔行变色
		showColumns: false, //是否显示 内容列下拉框
		showToggle: false, //是否显示详细视图和列表视图的切换按钮
		clickToSelect: false, //是否启用点击选中行
		data_local: "zh-US", //表格汉化
		sidePagination: "server", //服务端处理分页
		queryParamsType: "limit", //设置为 ‘limit’ 则会发送符合 RESTFul 格式的参数.
		queryParams: function(params) { //自定义参数，这里的参数是传给后台的，我这是是分页用的
			//            请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
			//　　　　　　　queryParamsType = 'limit' ,返回参数必须包含limit, offset, search, sort, order
			//            queryParamsType = 'undefined', 返回参数必须包含: pageSize, pageNumber, searchText, sortName, sortOrder.
			//            返回false将会终止请求。
			return { //这里的params是table提供的
				offset: params.offset, //从数据库第几条记录开始
				limit: params.limit, //找多少条
				//这个就是搜索框中的内容，可以自动传到后台，搜索实现在xml中体现
				type: $("#type_search").val(),
				content: $("#content_search").val()
			};
		},
		responseHandler: function(res) {
			//如果后台返回的json格式不是{rows:[{...},{...}],total:100},可以在这块处理成这样的格式
			return res;
		},
		columns: [{
			checkbox: true,
			formatter: function(i, row) { // 每次加载 checkbox 时判断当前 row 的 id 是否已经存在全局 Set() 里
				if($.inArray(row.id, overAllIds) != -1) { // 因为 判断数组里有没有这个 id
					return {
						checked: true // 存在则选中
					}
				}
			}
		}, {
			field: 'id',
			title: '数据源id',
		}, {
			field: 'userId',
			title: '创建者ID',
		}, {
			field: 'type',
			title: '数据源类型'
		}, {
			field: 'createtime',
			title: '创建时间',
			formatter: function(value, row, index){
				console.log(value);
				return dateOper(value);
			}
		}, {
			field: 'updatetime',
			title: '修改时间',
			formatter: function(value, row, index){
				return dateOper(value);
			}
		}, {
			field: 'databaseName',
			title: '数据库名称',
		}, {
			field: 'alias',
			title: '数据源名称'
		}, {
			field: 'driver',
			title: '驱动名称',
		}, {
			field: 'url',
			title: '数据源URL',
			class: 'colStyle'
		}, {
			field: 'port',
			title: '端口号'
		}, {
			field: 'auth',
			title: '认证方式',
		}, {
			field: 'username',
			title: '用户名',
		}, {
			field: 'description',
			title: '备注',
		}, {
			field: 'encode',
			title: '数据库编码格式',
			class: 'colStyle',
		}, {
			field: 'option',
			title: '操作',
			formatter: function(value, row, index) {
				return "<button id='update' class='btn btn-success btn-xs update' onclick='change_data(this)'  data-toggle='modal' data-target='#changeSource'>修改</button>" +
					"<button id='delete' class='btn btn-danger btn-xs delete' onclick='delete_data(this)' data-toggle='modal' data-target='#deleteSource'>删除</button>" +
					"<button id='warehouse' class='btn btn-default btn-xs ' onclick='warehouse_data(this)' data-toggle='modal' data-target='#warehouseSource'>入库</button>"
			}
		}],
		onLoadError: function() {
			alert("数据加载失败！");
		}
	});
}
//单个删除键所用的所选id
var chosenId;

//每条数据的修改按钮
function change_data(this_element) {
	var id = $(this_element).closest("tr").find("td").eq(1).text();
	$("#id").val(id);
	var userId = $(this_element).closest("tr").find("td").eq(2).text();
	$("#userId").val(userId);
	var type = $(this_element).closest("tr").find("td").eq(3).text();
	$("#type").val(type);
	var databaseName = $(this_element).closest("tr").find("td").eq(6).text();
	$("#databaseName").val(databaseName);
	var alias = $(this_element).closest("tr").find("td").eq(7).text();
	$("#alias").val(alias);
	var driver = $(this_element).closest("tr").find("td").eq(8).text();
	$("#driver").val(driver);
	var url = $(this_element).closest("tr").find("td").eq(9).text();
	$("#url").val(url);
	var port = $(this_element).closest("tr").find("td").eq(10).text();
	$("#port").val(port);
	var auth = $(this_element).closest("tr").find("td").eq(11).text();
	$("#auth").val(auth);
	var username = $(this_element).closest("tr").find("td").eq(12).text();
	$("#username").val(username);
	var description = $(this_element).closest("tr").find("td").eq(13).text();
	$("#description").val(description);
};

//删除数据按钮
function delete_data(this_element) {
	var id = $(this_element).closest("tr").find("td").eq(1).text();
	chosenId = id;
}

//入库按钮
$("#show_table tbody").on("click", "#warehousing", function() {
	var row = $("table#show_table tr").index($(this).closest("tr"));
	// alert(row);
	var id = $("table#show_table").find("tr").eq(row).find("td").eq(0).text()
	chosenId = id;
	// alert(chosenId);
	//    console.log(chosenId);
});

function ds_create_inter_div() {
	$("#ds_index_string").find("div").eq(0).append($(".oper_string").prop("outerHTML"));
}

function update_sure() {

	var id = $("#id").val();
	var userId = $("#userId").val();
	var type = $("#type").val();
	var databaseName = $("#databaseName").val();
	var alias = $("#alias").val();
	var driver = $("#driver").val();
	var url = $("#url").val();
	var port = $("#port").val();
	var auth = $("#auth").val();
	var username = $("#username").val();
	var password = $("#password").val();
	var description = $("#description").val();
	let filename = alias;
	//初始化
	if(type == "excel") {
		excel_interpret.push({
			"filename": filename,
			"isfilter": false
		});

		let interpret_filter = {}
		let start_line = $("#ds_start_line").val();
		let end_line = $("#ds_end_line").val();
		let index_logic = $("#ds_index_string").find("input[name='index_logic']:checked").val();
		if(start_line != "")
			interpret_filter["start_line"] = start_line;
		if(end_line != "")
			interpret_filter["end_line"] = end_line;
		if(index_logic != undefined)
			interpret_filter["index_logic"] = index_logic;
		let index_string = [];
		let oper_string_div = $("#upd_screen_div");
		for(let i = 0; i < oper_string_div.length; i++) { //组装筛选json
			let oper = oper_string_div[i];
			let oper_string = {};
			let start_string = $(oper).find("input:eq(0)").val();
			let end_string = $(oper).find("input:eq(1)").val();
			let content_string = $(oper).find("input:eq(2)").val();
			if(start_string != "" || end_string != "" ||
				content_string != "") {
				oper_string["start_string"] = start_string;
				oper_string["end_string"] = end_string;
				oper_string["content_string"] = content_string;
				index_string.push(oper_string);
			}
		}
		if(index_string.length != 0) {
			console.log(index_string);
			interpret_filter["index_string"] = index_string;
		}
		if(JSON.stringify(interpret_filter) != "{}") {
			for(let i = 0; i < excel_interpret.length; i++) {
				if(excel_interpret[i]["filename"] == filename) {
					excel_interpret[i]["isfilter"] = true;
					excel_interpret[i]["interpret_filter"] = interpret_filter;
					break;
				}
			}
		}
		console.log(excel_interpret);
	}
	var jsonstr = {
		'id': id,
		'userId': userId,
		'type': type,
		'databaseName': databaseName,
		'alias': alias,
		'driver': driver,
		'url': url,
		'port': port,
		'auth': auth,
		'username': username,
		'password': password,
		'description': description,
	}
	var data = {
		"json": JSON.stringify(jsonstr),
		"excel_interpret": JSON.stringify(excel_interpret)
	}
	console.log(data);
	$.ajax({
		type: "POST",
		url: "/datasource/updTDataSourceById",
		data: data,
		dataType: "json",
		async: false,
		success: function(data) {
			console.log(data);
			if(data.code == -1) {
				alert("修改失败");
			} else if(data.code == 0) {
				show_datasource_table();
			}
		},
			error: function(result){
				alert("抱歉，您没有该操作的权限！");
			}
	});
	$("#changeSource").modal('hide');
}
//删除已选数据
function deleteSelected() {
	if(!confirm("是否确认删除？"))
		return;
	var data = {
		ids: JSON.stringify(overAllIds)
	};
	batchDelete(data);
}

function batchDelete(data) {
	$.ajax({
		url: "/datasource/batchDeleteById",
		data: data,
		type: "GET",
		dataType: "json",
		success: function(result) {
			console.log(result.code);
			if(result.code == 0) {
				show_datasource_table();
			} else if(result.code == -1) {
				alert("删除失败")
			}
		},
			error: function(result){
				alert("抱歉，您没有该操作的权限！");
			}
	});
}
//搜索按钮
function search() {
	show_datasource_table();
}

var fileType = "";
//入库按钮
function warehouse_data(this_element) {
	var id = $(this_element).closest("tr").find("td").eq(1).text();
	var type = $(this_element).closest("tr").find("td").eq(3).text();
	fileType = type;
	chosenId = id;
	
	console.log(fileType == "mysql");

	if(fileType == "csv" || fileType == "excel") {
		$("#common_ware").css('display', 'block');
		$("#mysql_ware").css('display', 'none');
		$("#redis_ware").css('display', 'none');
	}

	if(fileType == "mysql") {
		$("#common_ware").css('display', 'none');
		$("#mysql_ware").css('display', 'block');
		$("#redis_ware").css('display', 'none');
		$.ajax({
			url: "/mysql/mysqlTablesPreview",
			data: {mysql_id:chosenId},
			type: "GET",
			dataType: "json",
			success: function(result) {
				if(result.code == 0) {
					let tables = result.payload;
					let checkboxshtml = "";
					
					console.log(tables);
					
					for(let i = 0; i < tables.length; i++){
						checkboxshtml += "<input type='checkbox' value='" + tables[i] + "' />" + tables[i] + "&nbsp;"
					}
					$("#mysql_ware").html(checkboxshtml);
				} else if(result.code == -1) {
					alert("无可入库表!")
				}
			},
			error: function(result){
				alert("抱歉，您没有该操作的权限！");
			}
		})
	}

	if(fileType == "redis") {
		$("#common_ware").css('display', 'none');
		$("#mysql_ware").css('display', 'none');
		$("#redis_ware").css('display', 'block');
		$.ajax({
			url: "/redis/getRedisDb",
			type: "GET",
			dataType: "json",
			success: function(result) {
				if(result.code == 0) {
					let dbs = result.payload;
					let checkboxshtml = "";
					for(let i = 0; i < dbs.length; i++){
						checkboxshtml += "<input type='checkbox' value='" + dbs[i] + "' />" + dbs[i] + "&nbsp;"
					}
					$("#redis_ware").html(checkboxshtml);
				} else if(result.code == -1) {
					alert("无可入库表!")
				}
			},
			error: function(result){
				alert("抱歉，您没有该操作的权限！");
			}
		})
	}
}