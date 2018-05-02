<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editIcoProductForm').form({
            url : '${ctx}/ico/product/save',
            onSubmit : function() {
                var isValid = $(this).form('validate');
                if (isValid) {progressLoad();}
                return isValid;
            },
            success : function(result) {
                progressClose();
                result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editIcoProductForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
       /** var longtime=new Date().getTime()+'';
        var startDate=new Date(parseInt("${icoProduct.startDate}")).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
        if(startDate == ''){
        	startDate=longtime;
        }
       $("#startDate").val(getFormatDateByLong(startDate*1, "yyyy-MM-dd hh:mm:ss"));
       
       var endDate= new Date(parseInt("${icoProduct.endDate}")).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
        if(endDate == ''){
    	   endDate=longtime;
       }
      $("#endDate").val(getFormatDateByLong(endDate*1, "yyyy-MM-dd hh:mm:ss"));
*/
      
  	$("#activeStatusEditTd").html(dictDropDownOptions('activeStatusEdit','activeStatus', 'active_status', '${icoProduct.activeStatus}', 'required:true,', 'width:305px,'));
   	$("#activeStatusEdit").combobox({
   	    valueField:'code',
   	    textField:'name'
   	});
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editIcoProductForm" method="post">
            <table class="grid">
            	<tr>
                    <td>启用状态</td>
                    <td id="activeStatusEditTd">
                    </td>
                </tr>
                <tr>
                    <td>数字货币名称</td>
                    <td><input name="id" type="hidden" value="${icoProduct.id}">
                        <input name="digitalCurrencyName" type="text" style="width: 300px;" placeholder="请输入数字货币名称" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,32]']" value="${icoProduct.digitalCurrencyName}"></td>
                </tr>
                <tr>
                    <td>募集数量(个)</td>
                    <td><input name="raiseAmount" type="text" style="width: 300px;" placeholder="请输入募集数量" class="easyui-numberbox"
                               data-options="required:true,min:1" value="${icoProduct.raiseAmount}"></td>
                </tr>
                <tr>
                    <td>发行价格(个BTC)</td>
                    <td><input name="issuePrice" type="text" style="width: 300px;" placeholder="请输入发行价格" class="easyui-numberbox"
                               data-options="required:true,min:0.000001,precision:6" value="${icoProduct.issuePrice}"></td>
                </tr>
                <tr>
                    <td>开始日期</td>
                    <td><input id="startDate" name="startDate" type="text" style="width: 300px;" placeholder="请输入开始日期" class="easyui-datetimebox"
                               data-options="required:true" value="${icoProduct.startDate}"></td>
                </tr>
                <tr>
                    <td>结束日期</td>
                    <td><input id="endDate" name="endDate" type="text" style="width: 300px;" placeholder="请输入结束日期" class="easyui-datetimebox"
                               data-options="required:true" value="${icoProduct.endDate}"></td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td><input name="remark" type="text" style="width: 300px;" placeholder="请输入备注" class="easyui-validatebox easyui-textbox"
                          data-options="validType:['length[0,32]']"  value="${icoProduct.remark}"></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>