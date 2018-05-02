<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.blocain.bitms.trade.fund.consts.FundConsts"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editAdjustForm').form({
            url : '${ctx}/fund/adjust/save',
            onSubmit : function() {
                progressLoad();
                var isValid = $(this).form('validate');
                if($('#needLock').combobox("getValue")=='<%=FundConsts.PUBLIC_STATUS_YES %>'){
                	var v = $('#lockEndDay').datetimebox('getValue');
                	if(v == '' || v ==0){
                		alert('请选择锁定结束日期');
                		isValid=false;
                	}
   			 	}
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
                    $('#editAdjustForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
    });
    $(document).ready(function(){
    	$('#stockInfoId').combobox({
     	   onChange:function(newValue,oldValue){
     		 if($('#businessFlag').combobox("getValue")=='<%=FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD %>'){
     			 if(newValue!=oldValue){
     				$('#stockInfoId').combobox("setValue",'<%=FundConsts.WALLET_BMS_TYPE %>');
     			 }
     		 }
     	   }
     	  });
    	
    	$('#businessFlag').combobox({
    		onChange: function(newValue,oldValue){
    			if($('#businessFlag').combobox("getValue")=='<%=FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD %>'){
        			 if(newValue!=oldValue){
        				$('#stockInfoId').combobox("setValue",'<%=FundConsts.WALLET_BMS_TYPE %>');
        			 }
        			 $('#needLock').combobox("setValue",'<%=FundConsts.PUBLIC_STATUS_YES %>');
        			 
        		 }
    		}
    	});
    	$('#needLock').combobox({
    		onChange: function(newValue,oldValue){
    			if($('#businessFlag').combobox("getValue")=='<%=FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD %>'){
        			 if(newValue!=oldValue){
        				$('#stockInfoId').combobox("setValue",'<%=FundConsts.WALLET_BMS_TYPE %>');
        				$('#needLock').combobox("setValue",'<%=FundConsts.PUBLIC_STATUS_YES %>');
        			 }
        		 }
    		}
    	});

        $("#adjustTypeTd").html(dictDropDownOptions('adjustType','adjustType', 'adjustType','adjustAdd', 'required:true,', 'width:374px'));
        $("#rateType").combobox({
            valueField:'code',
            textField:'name'
        });
        $("#needLockTd").html(dictDropDownOptions('needLock','needLock', 'yesOrNo','no', 'required:true,', 'width:374px'));
        $("#needLock").combobox({
            valueField:'code',
            textField:'name'
        });
    	
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editAdjustForm" method="post">
            <table class="grid">
                 <tr>
                    <td>业务类型</td>
                    <td>
                    	<select id="businessFlag" name="businessFlag" class="easyui-combobox"  style="width:374px;" data-options="required:true"  >
                            <%--<option value="<%=FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD %>">BMS交易奖励</option>--%>
						    <option selected value="<%=FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD %>">余额数量平台调增</option>
						    <option  value="<%=FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_SUB %>">余额数量平台调减</option>
						    <option  value="<%=FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_ADD %>">冻结数量平台调增</option> 
						    <option  value="<%=FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_SUB %>">冻结数量平台调减</option>     
						</select>  
                    </td>
                </tr>
                <tr>
                    <td>证券代码</td>
                    <td>
                        <input name="id" type="hidden"  value="">
                        <input  name="stockinfoId"  class="easyui-combobox" name="language" style="width: 374px;"
                                placeholder="请选择证券"  value="<%=FundConsts.WALLET_BTC_TYPE %>" data-options="
									url: '${ctx}/stock/info/allCanAdjustCoin', method: 'get', valueField:'id',
									textField:'stockCode', groupField:'group',required:true"  >

                    </td>
                </tr>
                <tr>
                    <td>调整类型</td>
                    <td id="adjustTypeTd">
                    </td>
                </tr>
                <tr>
                    <td>账户名</td>
                    <td><input name="accountName" type="text" placeholder="请输入账户名" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               data-options="required:true,validType:['length[0,50]']" value=""></td>
                </tr>
                <tr>
                    <td>调整数量</td>
                    <td><input name="adjustAmt" type="text" placeholder="请输入数量" class="easyui-numberbox" style="width: 374px;"
                               data-options="required:true,min:0.000001,precision:6" value=""></td>
                </tr>
                <tr>
                    <td>是否需要锁定</td>
                    <td id="needLockTd">
                    </td>
                </tr>
                <tr>
                    <td>锁定结束日期</td>
                    <td>
                    	<input id="lockEndDay"  name="lockEndDay2"  type="text" placeholder="请输入发布结束日期" class="easyui-datetimebox"
                               data-options="width:140, height:29" value=""  style="width:374px;"></td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td>
                        <textarea name="remark" style="width: 374px;height: 50px;"></textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>