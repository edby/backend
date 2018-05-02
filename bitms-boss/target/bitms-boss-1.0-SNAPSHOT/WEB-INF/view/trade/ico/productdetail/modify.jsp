<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editIcoProductDetailForm').form({
            url : '${ctx}/ico/product/detail/save',
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
                    $('#editIcoProductDetailForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editIcoProductDetailForm" method="post">
            <table class="grid">
                <tr>
                    <td>ICO阶段</td>
                    <td>
                    	<input name="id" type="hidden" value="${icoProductdetail.id}">
                    	<input name="productId" type="hidden" value="${icoProductdetail.productId}">
                        <input name="period" type="text" style="width: 142px;" placeholder="请输入ICO阶段" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,16]']" value="${icoProductdetail.period}">
                     </td>
                    <td>募集数量(个)</td>
                    <td>
                    	<input name="raiseAmount" type="text" style="width: 142px;" placeholder="请输入募集数量" class="easyui-numberbox"
                               data-options="required:true,min:1" value="${icoProductdetail.raiseAmount}">
                    </td>
                </tr>
                <tr>
                    <td>预购开始日期</td>
                    <td><input id="preStartDate" name="preStartDate" type="text" style="width: 142px;" placeholder="请输入开始日期" class="easyui-datetimebox"
                               data-options="required:true" value="${icoProductdetail.preStartDate}"></td>
                    <td>预购结束日期</td>
                    <td><input id="preEndDate" name="preEndDate" type="text" style="width: 142px;" placeholder="请输入结束日期" class="easyui-datetimebox"
                               data-options="required:true" value="${icoProductdetail.preEndDate}"></td>
                </tr>
                <tr>
                    <td>正式开始日期</td>
                    <td><input id="officialStartDate" name="officialStartDate" type="text" style="width: 142px;" placeholder="请输入开始日期" class="easyui-datetimebox"
                               data-options="required:true" value="${icoProductdetail.officialStartDate}"></td>
                    <td>正式结束日期</td>
                    <td><input id="officialEndDate" name="officialEndDate" type="text" style="width: 142px;" placeholder="请输入结束日期" class="easyui-datetimebox"
                               data-options="required:true" value="${icoProductdetail.officialEndDate }"></td>
                </tr>
                <tr>
                    <td>预购最低价格(个BTC)</td>
                    <td><input name="preMinPrice" type="text" style="width: 142px;" placeholder="请输入预购最低价格" class="easyui-numberbox"
                               data-options="required:true,min:0.000001,precision:6" value="${icoProductdetail.preMinPrice}">
                    </td>
                    <td>预购最高价格(个BTC)</td>
                    <td><input name="preMaxPrice" type="text" style="width: 142px;" placeholder="请输入预购最高价格" class="easyui-numberbox"
                               data-options="required:true,min:0.000001,precision:6" value="${icoProductdetail.preMaxPrice}">
                    </td>
                </tr>
                 <tr>
                    <td>正式发行价格(个BTC)</td>
                    <td><input name="issuePrice" type="text" style="width: 142px;" placeholder="请输入正式发行价格" class="easyui-numberbox"
                               data-options="required:true,min:0.000001,precision:6" value="${icoProductdetail.issuePrice}">
                    </td>
                    <td>正式发行价格说明</td>
                    <td><input name="issuePriceRemark" type="text" style="width: 142px;" placeholder="请输入正式发行价格说明" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,16]']" value="${icoProductdetail.issuePriceRemark}">
                    </td>
                </tr>
                <tr>
                    <td>认购数量起点</td>
                    <td><input name="subAmtPoint" type="text" style="width: 142px;" placeholder="请输入认购数量起点" class="easyui-numberbox"
                               data-options="required:true,min:1" value="${icoProductdetail.subAmtPoint}">
                     </td>
                     <td>锁定天数</td>
                    <td><input name="lockDays" type="text" style="width: 142px;" placeholder="请输入锁定天数" class="easyui-numberbox"
                               data-options="required:true,min:1" value="${icoProductdetail.lockDays}">
                     </td>
                </tr>
                
                <tr>
                    <td>备注</td>
                    <td colspan="3"><input name="remark" type="text" style="width: 420px;" placeholder="请输入备注" class="easyui-validatebox easyui-textbox"
                    data-options="validType:['length[0,16]']"  value="${icoProductdetail.remark}"></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>