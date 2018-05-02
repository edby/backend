<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<style>
    #tbodyxxx input[readonly] {
        background-color: #efefef;
    }
</style>
<script type="text/javascript">
    $(function () {
        $('#editBankRechargeInputForm').form({
            url: '${ctx}/fund/bankRecharge/save',
            onSubmit: function () {
                progressLoad();
                var isValid = $(this).form('validate');
                if (!isValid) {
                    progressClose();
                }
                return isValid;
            },
            success: function (result) {
                progressClose();
                var result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editBankRechargeInputForm').find('input[name="csrf"]').val(result.csrf);

                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editBankRechargeInputForm" method="post">
            <table class="grid">
                    <tr>
                        <td>币种</td>
                        <td>
                            <input name="stockinfoId" style="width: 250px" class="easyui-combobox"
                                   placeholder="请选择证券"  data-options="required:true,
                                    url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_CASHCOIN%>', method: 'get', valueField:'id',
                                    textField:'stockName', groupField:'group'"  value="<%=FundConsts.WALLET_EUR_TYPE%>">
                        </td>
                    </tr>
                    <tr>
                        <td>UID</td>
                        <td><input name="unid" type="text" placeholder="请输入UID"  class="easyui-numberbox" style="width: 250px;"
                                   data-options="required:true,precision:0" value="">
                        </td>
                    </tr>
                    <tr>
                        <td>姓氏</td>
                        <td><input name="surname" type="text" placeholder="请输入姓氏lastName"   class="easyui-validatebox easyui-textbox" style="width: 250px;"
                                   data-options="required:true,validType:['length[0,50]']" value="">
                        </td>
                    </tr>
                    <tr>
                        <td>名字</td>
                        <td><input name="realname" type="text" placeholder="请输入名字firstName"   class="easyui-validatebox easyui-textbox" style="width: 250px;"
                                   data-options="required:true,validType:['length[0,50]']" value="">
                        </td>
                    </tr>
                    <tr>
                        <td>交易ID</td>
                        <td><input name="transId" type="text" placeholder="请输入交易UID"   class="easyui-validatebox easyui-textbox" style="width: 250px;"
                                   data-options="required:true,validType:['length[0,50]']" value="">
                        </td>
                    </tr>
                    <tr>
                        <td>充值金额</td>
                        <td><input name="amount" type="text" placeholder="请输入充值金额"  class="easyui-numberbox" style="width: 250px;"
                                   data-options="required:true,min:0.000001,precision:6" value="">
                        </td>
                    </tr>
                    <!--<tr>
                        <td>手续费</td>
                        <td><input name="fee" type="text" placeholder="请输入手续费"  class="easyui-numberbox" style="width: 250px;"
                                   data-options="required:true,min:0,precision:6" value="">
                        </td>
                    </tr>
                    -->
                    <tr>
                        <td>备注</td>
                        <td>
                            <input name="remark" type="text" placeholder="请输入备注"  class="easyui-validatebox easyui-textbox"  style="width: 250px;height: 50px;"
                                   data-options="required:true,multiline:true,validType:['length[0,120]']" value="">
                        </td>
                    </tr>
            </table>
        </form:form>
    </div>
</div>