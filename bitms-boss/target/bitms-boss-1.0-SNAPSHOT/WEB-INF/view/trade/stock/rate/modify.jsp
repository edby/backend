<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editStockRateForm').form({
            url : '${ctx}/stock/rate/save',
            onSubmit : function() {
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
                    $('#editStockRateForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
        
        $("#rateTypeTd").html(dictDropDownOptions('rateType','rateType', 'rateType','${stockRate.rateType}', 'required:true,', 'width:374px'));
    	$("#rateType").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editStockRateForm" method="post">
            <table class="grid">
                <tr>
                    <td>证券代码</td>
                    <td>
                        <input name="id" type="hidden"  value="${stockRate.id}">
                        <input  id="stockInfoId" name="stockinfoId"  class="easyui-combobox" name="language" style="width: 374px;"  
                        placeholder="请选择证券"  value="${stockRate.stockinfoId}" data-options="
								url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group',required:true"  >
                    </td>
                </tr>
                <tr>
                    <td>费用类型</td>
                    <td id="rateTypeTd">
                    </td>
                </tr>
                <tr>
                    <td>值类型</td>
                    <td>
                        <input class="easyui-combobox" style="width:374px;" name="rateValueType" value="${stockRate.rateValueType}"
                               data-options="
                                valueField: 'label',
                                textField: 'value',
                                data: [{
                                    label: '1',
                                    value: '比例'
                                },{
                                    label: '2',
                                    value: '绝对值'
                                }]" />
                    </td>
                </tr>
                <tr>
                    <td>费率</td>
                    <td><input name="rate" type="text" placeholder="请输入费率" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               data-options="required:true,min:0.01,precision:2,max:100" value="${stockRate.rate}"></td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td>
                        <textarea name="remark" style="width: 374px;height: 100px;">${stockRate.remark}</textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>