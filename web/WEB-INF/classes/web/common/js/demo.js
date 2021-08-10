$(document).ready(function() {
	// 服务验证点击验证
	$("#verifyBtn").click(function() {
		$("#verify_ret_val").html("");
		$.post("../verifyServer.do", {
			domainId : $("#domainId").val(),
			timeStamp : $("#timeStamp").val(),
			servUrl : $("#servUrl").val(),
			sign : $("#sign").val()
		}, function(res) {
			$("#verify_ret_val").html(res);
		});
	});

	// 绑定停车场到域中
	$("#bindParkBtn").click(function() {
		$("#bindPark_ret_val").html("");
		$.post("../bindPark.do", {
			domainId : $("#domainId_bp").val(),
			timeStamp : $("#timeStamp_bp").val(),
			parks : $("#parks").val(),
			sign : $("#sign_bp").val()
		}, function(res) {
			$("#bindPark_ret_val").html(res);
		});
	});

	// 自定义数据
	$(".custom table").css("display", "none");
	$(".custom table:first").css("display", "");

	$("#method_select").change(function() {
		$(".custom table").css("display", "none");
		var clazzName = $("#method_select").val();
		$("." + clazzName).css("display", "");
	});
	// 自定义数据点击发送
	$("#customDataBtn").click(function() {
		var customData = {};
		var params = {};
		var method = $("#method_select").val();
		customData.method = method;
		if ("spaceInfo" == method) {
			params.parkCode = $("#space_parkCode").val();
			params.total = $("#space_total").val();
			params.remain = $("#space_remain").val();
			params.bookCount = $("#bookCount").val();
			params.bookRemain = $("#bookRemain").val();
			params.chargeSpaceCount = $("#chargeSpaceCount").val();
			params.chargeSpaceRemain = $("#chargeSpaceRemain").val();
		}
		if ("driveIn" == method) {
			params.parkCode = $("#driveIn_parkCode").val();
			params.orderNo = $("#driveIn_orderNo").val();
			params.plateNo = $("#plateNo").val();
			params.timeIn = $("#timeIn").val();
			params.spaceCode = $("#spaceCode").val();
			params.imageCount = $("#driveIn_imageCount").val();
			params.imgSuffix = $("#driveIn_imgType").val();
			var array = $("#driveIn_images").val().split(",");
			params.images = [];
			for (var i = 0; i < array.length; i++) {
				params.images.push(array[i]);
			}
		}
		if ("payDetail" == method) {
			params.parkCode = $("#payDetail_parkCode").val();
			params.orderNo = $("#payDetail_orderNo").val();
			params.payFee = $("#payFee").val();
			params.payType = $("#payType").val();
			params.payTime = $("#payTime").val();
		}
		if ("driveOut" == method) {
			params.parkCode = $("#driveOut_parkCode").val();
			params.orderNo = $("#driveOut_orderNo").val();
			params.totalCost = $("#totalCost").val();
			params.realCost = $("#realCost").val();
			params.timeOut = $("#timeOut").val();
			params.parkDuration = $("#parkDuration").val();
			params.leaveState = $("#leaveState").val();
			params.imageCount = $("#driveOut_imageCount").val();
			params.imgSuffix = $("#driveOut_imgType").val();
			var array = $("#driveOut_images").val().split(",");
			params.images = [];
			for (var i = 0; i < array.length; i++) {
				params.images.push(array[i]);
			}
		}
		customData.params = params;

		$("#customData_ret_val").html("");
		$.post("../push/customData.do", {
			domainId : $("#domainId_cd").val(),
			timeStamp : $("#timeStamp_cd").val(),
			sign : $("#sign_cd").val(),
			content : JSON.stringify(customData)
		}, function(res) {
			$("#customData_ret_val").html(res);
		});
	});
	
	//下行接口发送内容选择
	$(".request_body_class textarea").css("display", "none");
	$(".request_body_class textarea:first").css("display", "");
	$("#invoke_method").change(function(){
		$(".request_body_class textarea").css("display", "none");
		var contentType = $("#invoke_method").val();
		$("."+contentType).css("display","");
	});
	
	$("#serviceInvokeBtn").click(function(){
		var contentType = $("#invoke_method").val();
		var requestContent = "";
		if(contentType == "queryOrderFee"){
			requestContent = $("#queryOrderFee").val();
		}else if(contentType == "payDetail"){
			requestContent = $("#payDetail").val();
		}
		$.ajax({
			url:"../parkServiceInvoke.do",
			type:"POST",
			data:JSON.stringify(JSON.parse(requestContent)),
			contentType:"text/plain",
			dataType:"text",
			success:function(res){
				$("#Invoke_ret_val").html(res);
			}
		});
	});
	
	
	//自动获取签名，便于测试
	$("#sign_btn_verifyServer").click(function(){
		$.post("../getSign.do",{
			value:$("#domainId").val()+";"+$("#timeStamp").val()+";"+$("#servUrl").val(),
			key:$("#domain_key").val()
		},function(res){
			$("#sign").val(res);
		});
	});
	$("#sign_btn_bindPark").click(function(){
		$.post("../getSign.do",{
			value:$("#domainId_bp").val()+";"+$("#timeStamp_bp").val()+";"+$("#parks").val(),
			key:$("#domain_key").val()
		},function(res){
			$("#sign_bp").val(res);
		});
	});
	$("#sign_btn_customData").click(function(){
		$.post("../getSign.do",{
			value:$("#domainId_cd").val()+";"+$("#timeStamp_cd").val(),
			key:$("#domain_key").val()
		},function(res){
			$("#sign_cd").val(res);
		});
	});
	
	$("#checkBtn").click(function(res){
		$.post("../parkServiceCheck.do",{
			parkCode:$("#check_parkcode").val(),
		},function(res){
			$("#check_ret_val").html(res);
		});
	});
	
	
	
});




