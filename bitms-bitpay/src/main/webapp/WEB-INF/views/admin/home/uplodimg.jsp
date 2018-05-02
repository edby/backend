<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<script type="text/javascript">
	$(function(){
		$('#fileupload').fileupload({  
	        autoUpload: true,  
	        url: "${ctx}/admin/withdraw/import.ajax", 
	        dataType: 'json', 
	        done: function (e, data) {
	        	if(data.result.code == GW.SUCCESS){
	        		alert("上传成功");
	        		golist();
	        	}else{
	        		alert(data.result.message);
	        	}
	        },
	        progressall: function (e, data) {
	        } 
	    });    
		$('#fileupload').bind('fileuploadsubmit', function (e, data) {
	    });
		$(".upload").on("click", function (ev) {
			$("#fileupload").trigger("click");
		});
	});
</script>