<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var explosionEntrustDataGrid;
    $(function () {
        explosionEntrustDataGrid = $('#explosionEntrustDataGrid').datagrid({
            url:'${ctx}/entrust/closePostionEntrustList/data',
            queryParams:{
                entrustRelatedStockinfoId:'<%=FundConsts.WALLET_BTC2USD_TYPE%>',tableName:'table'
            },
            fit: true,
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
                    width: '100',
                    title: '交易类型',
                    field: 'tradeType',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                },{
                    width: '100',
                    title: '委托来源',
                    field: 'entrustSource'
                },{
                    width: '130',
                    title: '业务类别',
                    field: 'businessFlag',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }
            ]],
            columns: [[ {
                width: '150',
                title: '委托时间',
                field: 'entrustTime',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '80',
                title: '委托类型',
                field: 'entrustType',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            }, {
                width: '80',
                title: '委托方向',
                field: 'entrustDirect',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            },{
                width: '150',
                title: '委托数量',
                field: 'entrustAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '委托价格',
                field: 'entrustPrice',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '100',
                title: '委托备注',
                field: 'entrustRemark'
            }, {
                width: '100',
                title: '委托账户类型',
                field: 'entrustAccountType',
                formatter: function (value, row, index) {
                    if(value)
                    {
                        return '系统';
                    }
                    else
                    {
                        return '用户';
                    }
                }
            },{
                width: '100',
                title: '证券代码',
                field: 'stockCode',
                sortable: true
            },{
                width: '150',
                title: '手续费率',
                field: 'feeRate',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '成交数量',
                field: 'dealAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '成交金额(金额)',
                field: 'dealBalance',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '成交手续费',
                field: 'dealFee',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '100',
                title: '状态',
                field: 'status',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            },{
                width: '150',
                title: '最后更新时间',
                field: 'updateTime',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
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

    function searchExplosionEntrustFun() {
        explosionEntrustDataGrid.datagrid('load',
                $.serializeObject($('#searchExplosionEntrustForm'))
        );
    }
    function cleanExplosionEntrustFun() {
        ($('#searchExplosionEntrustForm input')).val('');
        explosionEntrustDataGrid.datagrid('load',{})
    }
    $(function(){
        $("#closeTradeTypeListTd").html(dictDropDownOptionsList('closeTradeTypeList','tradeType', 'tradeType','', '',  'width:142px,'));
        $("#closeTradeTypeList").combobox({
            valueField:'code',
            textField:'name'
        });
        $("#closeEntrustDirectListTd").html(dictDropDownOptionsList('closeEntrustDirectId','entrustDirect', 'entrustDealDirect', '', ''));
        $("#closeEntrustDirectId").combobox({
            valueField:'code',
            textField:'name'
        });
        $('#baocang_table_id').switchbutton({
            checked: false,
            onChange: function(checked){
                if(checked)
                {
                    $("#baocang_table_name").val("tableHis");
                }else
                {
                    $("#baocang_table_name").val("table");
                }
                explosionEntrustDataGrid.datagrid('load',
                    $.serializeObject($('#searchExplosionEntrustForm')) );
            }
        });
    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 60px; overflow: hidden;background-color: #fff">
        <Form id ="searchExplosionEntrustForm">
            <table>
                <tr>
                    <th>交易对</th>
                    <td>
                        <input  id="entrustRelatedStockinfoId" name="entrustRelatedStockinfoId"  class="easyui-combobox" name="language" style="width: 142px;"
                                placeholder="请选择交易对" value="<%=FundConsts.WALLET_BTC2USD_TYPE%>"   data-options="
									url: '${ctx}/stock/info/allInExchange', method: 'get', valueField:'id',
									required:true,textField:'stockName', groupField:'group'"  >
                    </td>
                    <th>账户名:</th>
                    <td><input  name="accountName" class="easyui-textbox" placeholder="请输入账户名"/></td>
                    <th>交易类型:</th>
                    <td id="closeTradeTypeListTd">
                    </td>
                    <th>委托方向:</th>
                    <td id="closeEntrustDirectListTd">
                    </td>
                    <th>状态:</th>
                    <td>
                        <select id ="status"  class="easyui-combobox" name="status" style="width:100px;"  data-options="required:true">
                            <option value="">-请选择-</option>
                            <option value="init">委托中</option>
                            <option value="noDeal">未成交</option>
                            <option value="partialDeal">部分成交</option>
                            <option value="allDeal">全部成交</option>
                            <option value="abnormal">异常委托</option>
                            <option value="withDrawed">已撤单</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>委托时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox"  placeholder="请输入委托开始时间"/></td>
                    <th>至</th>
                    <td><input name="timeEnd" class="easyui-datetimebox"   placeholder="请输入委托结束时间"/></td>
                    <td>
                    <td align="left">
                        <input id="baocang_table_name" name="tableName" value="table" type="hidden" />
                        <input id="baocang_table_id" style="width: 100px;" class="easyui-switchbutton" data-options="onText:'历史数据',offText:'当前数据'" >
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchExplosionEntrustFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanExplosionEntrustFun();">清空</a>
                    </td>
                </tr>
            </table>
        </Form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="explosionEntrustDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>