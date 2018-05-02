<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.blocain.bitms.trade.fund.consts.FundConsts"%>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var accountCandyRecordDataGrid;
    $(function () {
    	accountCandyRecordDataGrid = $('#accountCandyRecordDataGrid').datagrid({
            url: '${ctx}/fund/accountCandyRecord/data',
            fit: true,
            queryParams:{
            },
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
			{
			    width: '150',
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
			},{
                    width: '150',
                    title: '发放时间',
                    field: 'currentDate',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value * 1, "yyyy-MM-dd hh:mm:ss");
                    }
                },{
                    width: '150',
                    title: '发放数量',
                    field: 'occurAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return new Number(value).toFixed(12);
                    }
                }
                            ]],
            columns: [[ {
                width: '120',
                title: '资产类型',
                field: 'accountAssetType',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '120',
                title: '资产ID',
                field: 'accountAssetId'
            }, {
                width: '200',
                title: '备注',
                field: 'remark',
                sortable: true
            }
                ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
            }
        });
    });


    function searchAccountFundCurrentFun() {
    	accountCandyRecordDataGrid.datagrid('load',
            $.serializeObject($('#searchAccountColletAddrCurrentForm')));
    }
    function cleanAccountFundCurrentFun() {
        $('#searchAccountColletAddrCurrentForm input').val('');
        accountCandyRecordDataGrid.datagrid('load', {});
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAccountColletAddrCurrentForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="accountName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <th>证券代码:</th>
                    <td>
                        <input  id="approve_stockCode" name="stockinfoId"  class="easyui-combobox" name="language" style="width: 142px;"
                                placeholder="请选择证券"  data-options="
									url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_ERC20_TOKEN%>', method: 'get', valueField:'id',
									textField:'stockCode', groupField:'group'"  >
                    </td>
                    <th>发放开始时间:</th>
                    <td><input name="timeStart" class="easyui-datebox"  placeholder="请输入交易开始时间"/></td>
                    <th>发放结束时间:</th>
                    <td><input name="timeEnd" class="easyui-datebox"   placeholder="请输入交易结束时间"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountFundCurrentFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountFundCurrentFun();">清空</a>
                    </td>
                     
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="accountCandyRecordDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
