function getEntityStatics(){
	// 调用统计
	$.ajax({
		url : '/exchange/queryCount',
		type : 'post',
		contentType : 'application/json',
		data : {
			'productDetailId' : $("#id").val()
		},
		success : function(data) {
			data = JSON.parse(data);
			if (data.code == 200) {
				$("#totalCnt").text(toThousands(data.object.CNT));
				$("#totalsubamt").val(data.object.TOTALSUBAMT);
				$("#percent").text(
						(100 * data.object.TOTALSUBAMT / $(
								"#raiseAmount").val()).toFixed(2)
								+ '%');
				if((100 * data.object.TOTALSUBAMT / $(
								"#raiseAmount").val())==100){
					$(".BntRengou").css("display","none");
					$(".BntRengou2").css("display","block");
					$("#subAmt2").attr("disabled","false"); 
				}else{
					$(".BntRengou2").css("display","none");
					$(".BntRengou").css("display","block");
					$("#subAmt2").removeAttr("disabled"); 
				}
				var d = new Date();
				var total = parseFloat($("#endDate").val())
						- d.getTime();
				if (total < 0)
					total = 0;
				var days = Math.ceil(total / (24 * 60 * 60 * 1000));
				$("#lastDays").text(days);
				$("#lastDays2").text(days);
				seajs.use([ 'moment' ], function(moment) {
					$("#lastDay").text(moment(parseFloat($("#endDate").val())).format("YYYY-MM-DD HH:mm:ss"));
					$("#preDay").text(moment(parseFloat($("#preDate").val())).format("YYYY-MM-DD HH:mm:ss"));
				});
				var a = (100 * data.object.TOTALSUBAMT / $(
						"#raiseAmount").val()).toFixed(2);
				$("#colorBar").css("width", a + "%");
			} else {
				console.log(data.message);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(textStatus);
		}
	});
}
$(function() {
			getEntityStatics();

			// push页面弹窗
			$("#subsciption").click(function() {
				$("#preSubscribeWin").show();
				$("#preOrderWin").hide();
				$('.Mask').show();
			});
			$("#preOrder").click(function() {
				$("#preOrderWin").show();
				$("#preSubscribeWin").hide();
				$('.Mask').show();
			});
			$("#preOrder2").click(function() {
				$("#preOrderWin").show();
				$("#preSubscribeWin").hide();
				$('.Mask').show();
			});
			$("#subAmt3").click(function() {
				$("#preSubscribeWin").show();
				$("#preOrderWin").hide();
				$('.Mask').show();
			});
			$(".closeBtn").each(function() {
				$(this).click(function() {
					$("#preOrderWin").hide();
					$("#orderWin").hide();
					$("#preSubscribeWin").hide();
					$('.Mask').hide();
				});
			});
			$("#subAmt").keyup(function() {
				var value = parseFloat($("#subAmt").val());
				var value2= $("#subAmt").val();
				try{
					if(value2.toString().split(".")[1].length>4){
						$("#subAmt").val(value.toFixed(4));
					}
				}catch(err){}
						$("#subSum").text(
								( $("#subAmt").val()/$("#subPrice").val()).toFixed(6)
										+ 'BMS');
						 $("#yugouSubAmt").val(($(this).val()/$("#subPrice").val()).toFixed(6));
					});
			$("#subPrice").keyup(
					function() {
						$("#subSum").text(
								($("#subPrice").val() * $("#subAmt").val()).toFixed(6)
										+ 'BMS');
					});
			$("#subAmt2").keyup(function(){
				var value = parseFloat($("#subAmt2").val());
				var value2= $("#subAmt2").val();
				try{
					if(value2.toString().split(".")[1].length>4){
						$("#subAmt2").val(value.toFixed(4));
					}
				}catch(err){}
			    $("#totolFee").text(($(this).val()/$("#subPrice2").val()).toFixed(6));
			    $("#rengouSubAmt").val(($(this).val()/$("#subPrice2").val()).toFixed(6));
			  });
		});
// 数字格式化
function toThousands(num) {
	return (num || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
}


