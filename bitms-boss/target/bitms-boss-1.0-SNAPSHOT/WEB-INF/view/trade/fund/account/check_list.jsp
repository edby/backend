<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.blocain.bitms.trade.fund.consts.FundConsts"%>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var accountFundCurrentDataGrid2;
    $(function () {
        accountFundCurrentDataGrid2 = $('#accountFundCurrentDataGrid2').datagrid({
            url: '${ctx}/fund/account/data',
            fit: true,
            queryParams:{relatedStockinfoId:<%=FundConsts.WALLET_BTC_TYPE%>,approveStatus:'<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING %>',accountAssetType:account_fund_current_accountAssetType,businessFlag:account_fund_current_businessFlag},
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
			{
			    width: '100',
			    title: '账户名',
			    field: 'accountName',
			    sortable: true
			}, {
			    width: '130',
			    title: '证券ID',
			    field: 'stockinfoId',
			    sortable: true
			}, {
			    width: '80',
			    title: '证券代码',
			    field: 'stockCode',
			    sortable: true
			}, {
			    width: '100',
			    title: '证券名称',
			    field: 'stockName',
			    sortable: true,
			    hidden:true
			}		
                            ]],
            columns: [[ {
                width: '120',
                title: '提币申请时间',
                field: 'currentDate',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            },{
                width: '70',
                title: '业务类别',
                field: 'businessFlag',
                hidden:true,
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '80',
                title: '原资产数量余额',
                field: 'orgAmt',
                sortable: true
            },{
                width: '80',
                title: '资产发生方向',
                field: 'occurDirect',
                hidden:true,
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '150',
                title: '资产发生数量',
                field: 'occurAmt',
                sortable: true,
                formatter: function (value, row, index) {
                	return (value-row.netFee).toFixed(12);
                }
            },{
                width: '150',
                title: '网络手续费',
                field: 'netFee',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '最新资产数量余额',
                field: 'lastAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '250',
                title: '提币目标地址',
                field: 'withdrawAddr',
                sortable: true
            }, {
                width: '100',
                title: '备注',
                field: 'remark',
                sortable: true
            }, {
                width: '70',
                title: '审批状态',
                field: 'approveStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '70',
                title: '划拨状态',
                field: 'transferStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '90',
                title: '创建时间',
                field: 'createDate',
                hidden:true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }<shiro:hasPermission name="trade:setting:accountfundcurrent:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 170,
                    formatter: function (value, row, index) {
                        var str = '';
                        if( row.approveStatus == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING %>'){
                        	str += $.formatString('<a href="javascript:void(0)"  class="accountfundcurrent-easyui-linkbutton-dongjie2" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editAccountFundCurrent2Fun(\'{0}\',\'<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH %>\');" >审批通过</a>', row.id);
                        	str += '&nbsp;|&nbsp;';
                        	str += $.formatString('<a href="javascript:void(0)"  class="accountfundcurrent-easyui-linkbutton-jiedong2" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="editAccountFundCurrent2Fun(\'{0}\',\'<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKREJECT %>\');" >审批拒绝</a>', row.id);
                        }
						return str;
                    }
                }
                </shiro:hasPermission>
                ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
                <shiro:hasPermission name="trade:setting:accountfundcurrent:operator">
                $('.accountfundcurrent-easyui-linkbutton-dongjie2').linkbutton({text: '复核通过'});
                $('.accountfundcurrent-easyui-linkbutton-jiedong2').linkbutton({text: '复核拒绝'});
                </shiro:hasPermission>
            }
        });
    });

    

    function editAccountFundCurrent2Fun(id,status) {
        if (id == undefined) {
            var rows = accountFundCurrentDataGrid2.datagrid('getSelections');
            id = rows[0].accountFundCurrentName;
        } else {
            accountFundCurrentDataGrid2.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:accountfundcurrent:operator">
        var approveStatus=0;
        var url='${ctx}/fund/account/approval/approval';
        var msg="你确定要拒绝该用户的钱包提现业务吗?";
        if(status == 'checkThrough'){
        	msg="你确定要通过该用户的钱包提现业务吗?";
        }
        parent.$.messager.confirm('询问', msg, function (b) {
            if (b) {
                progressLoad();
                $.post(url, {
                	id: id,approveStatus:status
                }, function (result) {
                	 progressClose();
                    if (result.code == ajax_result_success_code) {
                    	accountFundCurrentDataGrid2.datagrid('load', 
                    			{approveStatus:$("#check_approveStatus").combobox('getValue'),accountAssetType:account_fund_current_accountAssetType,businessFlag:account_fund_current_businessFlag});
                        parent.$.messager.alert('提示', result.message, 'info');
                    }else{
                    	parent.$.messager.alert('提示', result.message, 'error');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                }, 'JSON');
            }
        });
        </shiro:hasPermission>
    }
    function searchAccountFundCurrent2Fun() {
    	accountFundCurrentDataGrid2.datagrid('load', 
    			{accountAssetType:account_fund_current_accountAssetType,businessFlag:account_fund_current_businessFlag
                    relatedStockinfoId:$("#check_relatedStockinfoId").combobox('getValue'),
    		,approveStatus:$("#check_approveStatus").combobox('getValue'),accountName:$("#check_accountName").val(),stockCode:$("#check_stockCode2").combobox('getValue')});
    }
    function cleanAccountFundCurrent2Fun() {
        $('#searchAccountFundCurrent2Form input').val('');
        $("#check_approveStatus").combobox('setValue','<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING %>');
        accountFundCurrentDataGrid2.datagrid('load', 
        		{relatedStockinfoId:<%=FundConsts.WALLET_BTC_TYPE%>,approveStatus:$("#check_approveStatus").combobox('getValue'),accountAssetType:account_fund_current_accountAssetType,businessFlag:account_fund_current_businessFlag});
    }
    $(function(){
    	$("#check_approveStatusTd").html(dictDropDownOptionsList('check_approveStatus','approveStatus', 'approveStatus','<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING %>', '',  'width:142px,'));
    	$("#check_approveStatus").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAccountFundCurrent2Form">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="check_accountName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <th>证券代码:</th>
                    <td>
                    	<input  id="check_stockCode2" name="stockCode"  class="easyui-combobox" name="language" style="width: 142px;"  
	                        placeholder="请选择证券"  value="${stockRate.stockinfoId}" data-options="
									url: '${ctx}/stock/info/all', method: 'get', valueField:'stockCode',
									textField:'stockName', groupField:'group'"  >
                    </td>
                    <th>关联证券代码:</th>
                    <td>
                        <input id="check_relatedStockinfoId" name="relatedStockinfoId" class="easyui-combobox" name="language" style="width: 102px;"
                               placeholder="请关联证券代码" value="<%=FundConsts.WALLET_BTC_TYPE%>" data-options="
									url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'"  >
                    </td>
                    <th>审批状态:</th>
                    <td id="check_approveStatusTd">	
					</td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountFundCurrent2Fun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountFundCurrent2Fun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="accountFundCurrentDataGrid2" data-options="fit:true,border:false"></table>
    </div>
</div>	
