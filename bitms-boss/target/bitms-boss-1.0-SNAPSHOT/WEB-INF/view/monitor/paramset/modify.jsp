<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    if("${monitorParam.id}" == ""){
        //如果是添加模式则取消参数代码的只读性
        $('input[name=paramCode]').removeAttr("readonly");

    }else{
        //如果是修改模式则给所有输入框赋初值
        var t = "${monitorParam.paramType}";
        if(t == "Message") {
            $("#paramType  option[value='Message'] ").attr("selected", true);
        }
//        if(t == "warn"){
//            $("#paramType  option[value='warn'] ").attr("selected", true);
//        }
    }
     $(function() {
    	 $('#editMonitorParamForm').form({
    		 url : '${ctx}/monitor/param/save',
    		 onSubmit : function(){
    			 progressLoad();
                 var isValid = $(this).form('validate');
                 if (!isValid) {
                     progressClose();
                 }
                 return isValid;
    		 },
    	     success : function(result) {
    	    	 progressClose();
                 var result = $.parseJSON(result);
                 if (result.code == ajax_result_success_code) {
                     parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                     parent.$.modalDialog.handler.dialog('close');
                 } else {
                     parent.$.messager.alert('错误', result.message, 'error');
                     $('#editMonitorParamForm').find('input[name="csrf"]').val(result.csrf);
                 }
    	     }
    	 });
     });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" style="overflow:scroll">
      <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
           <form:form id="editMonitorParamForm" method="post">
                 <table class="grid">
                      <tr>
	                    <td>参数类型</td>
	                    <td>
                        <select id="paramType" class="easyui-combobox" name="paramType" style="width:305px;" data-options="editable:false,required:true">
                            <option value="" selected></option>
                            <option value="Message">消息提醒</option>
                        </select>
                        <input name="id" type="hidden" value="${monitorParam.id}">
                      </td>
                      </tr>
                      <tr>
	                    <td>参数名称</td>
	                    <td>
                        <input id="paramName"  name="paramName" style="width:305px;" value="${monitorParam.paramName}"  data-options="required:true">
                        </input>
                      </td>
                      </tr>
                      <tr>
	                    <td>参数代码</td>
	                    <td>
                        <input id="paramCode" readonly name="paramCode" style="width:305px;" value="${monitorParam.paramCode}"  data-options="required:true">
                        </input>
                      </td>
                      </tr>
                      <tr>
	                    <td>参数值</td>
	                    <td>
	                        <textarea name="paramValue" style="width: 374px;height: 50px;">${monitorParam.paramValue}</textarea>
	                    </td>
                      </tr>
                     <tr>
                         <td>参数备注</td>
                         <td>
                             <textarea name="paramDesc" style="width: 374px;height: 50px;">${monitorParam.paramDesc}</textarea>
                         </td>
                     </tr>
                 </table>
           </form:form>
      </div>
</div>