$(document).ready(function() {
	// 查询会员信息
	$("#getLeaguerInfoBtn").click(function() {
		$("#getLeaguerInfo_ret_val").html("");
		$.post("../getLeaguerInfo.do", {
			domainId : $("#domainId").val(),
			timeStamp : $("#timeStamp").val(),
			parkCode : $("#parkCode").val(),
			plateNo : $("#plateNo").val(),
			plateType : $("#plateType").val(),
			sign : $("#sign").val()
		}, function(res) {
			$("#getLeaguerInfo_ret_val").html(res);
		});
	});
	
	//会员扣费信息
	$("#leaguerChargeBtn").click(function() {
		$("#charge_ret_val").html("");
		$.post("../parkLeaguerCharge.do", {
			domainId : $("#domainId_charge").val(),
			timeStamp : $("#timeStamp_charge").val(),
			plateNo : $("#plateNo_charge").val(),
			plateType : $("#plateType_charge").val(),
			chargeFee : $("#charge_fee").val(),
			sign : $("#sign_charge").val()
		}, function(res) {
			$("#charge_ret_val").html(JSON.stringify(res));
		});
	});
	
	
	//自动获取签名，便于测试
	$("#sign_btn_getLeaguerInfo").click(function(){
		$.post("../getSign.do",{
			value:$("#domainId").val()+";"+$("#timeStamp").val()+";"+$("#plateNo").val()+";"+$("#parkCode").val()+";"+$("#plateType").val(),
			key:$("#domain_key").val()
		},function(res){
			$("#sign").val(res);
		});
	});
	//自动获取签名，便于测试
	$("#sign_btn_charge").click(function(){
		$.post("../getSign.do",{
			value:$("#domainId_charge").val()+";"+$("#timeStamp_charge").val()+";"+$("#plateNo_charge").val()+";"+$("#charge_fee").val()+";"+$("#plateType_charge").val(),
			key:$("#domain_key").val()
		},function(res){
			$("#sign_charge").val(res);
		});
	});
	
	
	
});




