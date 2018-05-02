//通用js
var GW = {
    SUCCESS: 100, // 成功
    VALIDERR: 101 // 失败
};

$.validator.setDefaults({
	errorElement:'span'
});

$.extend($.validator.messages, {
	required: "必填字段",
	remote: "请修正该字段",
	email: "请输入正确格式的电子邮件",
	url: "请输入合法的网址",
	date: "请输入合法的日期",
	dateISO: "请输入合法的日期 (ISO).",
	number: "请输入合法的数字",
	digits: "只能输入整数",
	creditcard: "请输入合法的信用卡号",
	equalTo: "请再次输入相同的值",
	accept: "请输入拥有合法后缀名的字符串",
	maxlength: jQuery.format("请输入一个长度最多是 {0} 的字符串"),
	minlength: jQuery.format("请输入一个长度最少是 {0} 的字符串"),
	rangelength: jQuery.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
	range: jQuery.format("请输入一个介于 {0} 和 {1} 之间的值"),
	max: jQuery.format("请输入一个最大为 {0} 的值"),
	min: jQuery.format("请输入一个最小为 {0} 的值")
});

//邮箱 
jQuery.validator.addMethod("mail", function (value, element) {
	var mail = /^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$/;
	return this.optional(element) || (mail.test(value));
}, "邮箱格式不对");

//电话验证规则
jQuery.validator.addMethod("phone", function (value, element) {
    var phone = /^0\d{2,3}-\d{7,8}$/;
    return this.optional(element) || (phone.test(value));
}, "电话格式如：0371-68787027");

//区号验证规则  
jQuery.validator.addMethod("ac", function (value, element) {
    var ac = /^0\d{2,3}$/;
    return this.optional(element) || (ac.test(value));
}, "区号如：010或0371");

//无区号电话验证规则  
jQuery.validator.addMethod("noactel", function (value, element) {
    var noactel = /^\d{7,8}$/;
    return this.optional(element) || (noactel.test(value));
}, "电话格式如：68787027");

//手机验证规则  
jQuery.validator.addMethod("mobile", function (value, element) {
    var mobile = /^1[3|4|5|7|8]\d{9}$/;
	return this.optional(element) || (mobile.test(value));
}, "手机格式不对");

//邮箱或手机验证规则  
jQuery.validator.addMethod("mm", function (value, element) {
    var mm = /^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/;
	return this.optional(element) || (mm.test(value));
}, "邮箱或手机格式不对");

//电话或手机验证规则  
jQuery.validator.addMethod("tm", function (value, element) {
    var tm=/(^1[3|4|5|7|8]\d{9}$)|(^\d{3,4}-\d{7,8}$)|(^\d{7,8}$)|(^\d{3,4}-\d{7,8}-\d{1,4}$)|(^\d{7,8}-\d{1,4}$)/;
    return this.optional(element) || (tm.test(value));
}, "格式不对");

//年龄
jQuery.validator.addMethod("age", function(value, element) {   
	var age = /^(?:[1-9][0-9]?|1[01][0-9]|120)$/;
	return this.optional(element) || (age.test(value));
}, "不能超过120岁"); 
///// 20-60   /^([2-5]\d)|60$/

//传真
jQuery.validator.addMethod("fax",function(value,element){
    var fax = /^(\d{3,4})?[-]?\d{7,8}$/;
    return this.optional(element) || (fax.test(value));
},"传真格式如：0371-68787027");

//验证当前值和目标val的值相等 相等返回为 false
jQuery.validator.addMethod("equalTo2",function(value, element){
    var returnVal = true;
    var id = $(element).attr("data-rule-equalto2");
    var targetVal = $(id).val();
    if(value === targetVal){
        returnVal = false;
    }
    return returnVal;
},"不能和原始密码相同");

//大于指定数
jQuery.validator.addMethod("gt",function(value, element){
    var returnVal = false;
    var gt = $(element).data("gt");
    if(value > gt && value != ""){
        returnVal = true;
    }
    return returnVal;
},"不能小于0 或空");

//汉字
jQuery.validator.addMethod("chinese", function (value, element) {
    var chinese = /^[\u4E00-\u9FFF]+$/;
    return this.optional(element) || (chinese.test(value));
}, "格式不对");

//指定数字的整数倍
jQuery.validator.addMethod("times", function (value, element) {
    var returnVal = true;
    var base=$(element).attr('data-rule-times');
    if(value%base!=0){
        returnVal=false;
    }
    return returnVal;
}, "必须是发布赏金的整数倍");

//身份证
jQuery.validator.addMethod("idCard", function (value, element) {
    var isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;//(15位)
    var isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;//(18位)

    return this.optional(element) || (isIDCard1.test(value)) || (isIDCard2.test(value));
}, "格式不对");

//字符输入过多
jQuery.validator.addMethod("maxlen", function (value, element) {
	if(""!=value){
	    var returnVal = false;
	    var len = $(element).data("maxlen");
	    if(""!=null && value.length <= len){
	        returnVal = true;
	    }
	    return returnVal;
	}else
		return true;
    
}, "输入的字符过多");

jQuery.validator.addMethod("pass", function(value, element) {   
	var age = /^(?![0-9]+$)(?![a-zA-Z]+$)(?![_]+$)[0-9A-Za-z_]{6,20}$/;
	return this.optional(element) || (age.test(value));
}, "密码由6-20数字、字母和下划线，两种或以上不同类型组成");

jQuery.validator.addMethod("user", function(value, element) {   
	var age = /^([0-9A-Za-z]{6,20})?$/;
	return this.optional(element) || (age.test(value));
}, "帐号由6-20位的数字或字母组成");

//邮政编码
jQuery.validator.addMethod("zipcode", function(value, element) {
	var tel = /^[0-9]{6}$/;
	var f = this.optional(element) || (tel.test(value))
	return f;
}, "邮编号码必须为6位数字");  

function menu(url){
	$.ajax({
		type: 'post',
		url: url,
		dataType: 'html',
		success: function(data){
			$('.content-panel').html(data);
		}
	});
};
function add(url){
	$.ajax({
		type: 'post',
		url: url,
		dataType: 'html',
		success: function(data){
			$('.panel-search').hide();
			$('.panel-content').html(data);
		}
	});
};
function update(url){
	$.ajax({
		type: 'post',
		url: url,
		dataType: 'html',
		success: function(data){
			$('.panel-search').hide();
			$('.panel-content').html(data);
		}
	});
};
function save(obj, url){ 
	showModal();
	$.ajax({
		type: 'post',
		url: url,
		data: obj.serialize(),
		dataType: 'json',
		success: function(data){
			if(data.code == GW.SUCCESS){
				alert("保存或修改成功！");
			}else{
				alert(data.message); 
			}
			hideModal();
		}
	});
};
function del(url){
	if(confirm("是否确认删除数据？")){
		$.ajax({
			type: 'post',
			url: url,
			dataType: 'json',
			success: function(data){
				if(data.code == GW.SUCCESS){
					alert("删除成功！");
					golist();
				}else{
					alert(data.message);
				}
			}
		});
	}
};  
function batchdel(obj, name, url){
	var num = $("input[name='"+name+"']:checked").length;
	if(num <= 0){
		alert("请选择一条记录！")
		return;
	}else{
		if(confirm("是否确认批量删除数据？")){
			$.ajax({
				type: 'post',
				url: url,
				data:obj.serialize(),
				dataType: 'json',
				success: function(data){
					if(data.code == GW.SUCCESS){
						alert("删除成功！");
						golist();
					}else{
						alert(data.message);
					}
				}
			});
		}
	}
}
function search(obj, url){
	$.ajax({
		type: 'post',
		url: url,
		data: obj.serialize(),
		dataType: 'html',
		success: function(data){
			$('.panel-content').html(data);
			$('.panel-search').show();
		}
	});
}; 
