<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.blocain.bitms.trade.ico.consts.SubscribeConst"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editIcoSubscribeForm').form({
            url : '${ctx}/ico/subscribe/save',
            onSubmit : function() {
                var isValid = $(this).form('validate');
                var newValue=$('#subStatus').combobox("getValue");
                if(newValue == '<%=SubscribeConst.SUBSCRIBE_STATUS_PRE_FAIL %>' || newValue == '<%=SubscribeConst.SUBSCRIBE_STATUS_SUB_FAIL %>')
				{
	    			$('#dealAmt').numberbox('setValue', 0);
	    			$('#dealAmt').numberbox('disable', true);
				}else{
					if($('#dealAmt').numberbox('getValue')<=0){
						 parent.$.messager.alert('提示', '成交数量要大于0', 'info');
						isValid = false;
					}
				}
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
                    $('#editIcoSubscribeForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
    });
    //数量变化金额自动变化
    $(document).ready(function(){
    	$('#dealAmt').numberbox({
     	   min:0,
     	   required:true,
     	   onChange:function(newValue,oldValue){
     	   	$('#dealBalance').numberbox('setValue', newValue*$("#dealPrice").val());
     	   }
     	  });
    	//状态变化，成交数量变化，金额也变化
    	$('#subStatus').combobox({
    		onChange: function(newValue,oldValue){
    			if(newValue == '<%=SubscribeConst.SUBSCRIBE_STATUS_PRE_FAIL %>' || newValue == '<%=SubscribeConst.SUBSCRIBE_STATUS_SUB_FAIL %>')
    				{
		    			$('#dealAmt').numberbox('setValue', 0);//认购失败 认购数量为0
		    			$('#dealAmt').numberbox('disable', true);//不能输入
    				}else{
    					$('#dealAmt').numberbox('enable', true);//认购成功状态 可填写认购数量
    				}
    		}
    	});
    	$('#subStatus').combobox('setValue', '${icoSubscribe.subStatus}');
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editIcoSubscribeForm" method="post">
            <table class="grid">
           		 <tr>
                    <td>认购状态</td>
                    <td>
                    <select id="subStatus" class="easyui-combobox" name="subStatus" style="width:300px;" data-options="width:140, height:29, editable:false, panelHeight:'auto', required:true ">   
					    <option value="<%=SubscribeConst.SUBSCRIBE_STATUS_PRE_FAIL %>">预购失败</option>   
					    <option value="<%=SubscribeConst.SUBSCRIBE_STATUS_PRE_SUCCESS %>">预购成功</option>    
					    <option value="<%=SubscribeConst.SUBSCRIBE_STATUS_SUB_FAIL %>">认购失败</option>   
					    <option value="<%=SubscribeConst.SUBSCRIBE_STATUS_SUB_SUCCESS %>">认购成功</option>     
					</select> 
					</td>
                </tr>
                <tr>
                    <td>成交数量</td>
                    <td>
                    	<input name="id" type="hidden" value="${icoSubscribe.id}">
                        <input id="dealAmt" name="dealAmt" type="text" style="width: 300px;" placeholder="请输入成交数量" class="easyui-numberbox"  disabled
                               data-options="required:true,min:0,precision:0,max:${icoSubscribe.subAmt}" value="${icoSubscribe.dealAmt}">
                    </td>
                </tr>
                <tr>
                    <td>成交价格</td>
                    <td><input id="dealPrice" name="dealPrice" type="text" style="width: 300px;" placeholder="请输入成交价格" class="easyui-numberbox" readonly
                               data-options="required:true,min:0.000001,precision:6" value="${icoSubscribe.subPrice}"></td>
                </tr>
                <tr>
                    <td>成交金额</td>
                    <td><input id="dealBalance" name="dealBalance" type="text" style="width: 300px;" placeholder="请输入成交金额" class="easyui-numberbox" readonly
                               data-options="required:true,min:0.0,precision:6" value="${icoSubscribe.dealBalance}"></td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3">
                        <textarea name="remark" style="width: 300px;height: 70px;">${icoSubscribe.remark}</textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>