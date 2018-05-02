<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var walletTransferCurrentDataGrid;
    $(function () {
        walletTransferCurrentDataGrid = $('#walletTransferCurrentDataGrid').datagrid({
            url: '${ctx}/bitpay/walletTransferCurrent/data',
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: false,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns: [[
                {
                    width: '150',
                    title: '流水时间',
                    field: 'currentDate',
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                }, {
                    width: '150',
                    title: '证券ID',
                    field: 'stockinfoId'
                }, {
                    width: '150',
                    title: '证券代码',
                    field: 'stockCode'
                }, {
                    width: '100',
                    title: '资产发生方向',
                    field: 'occurDirect',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }
            ]],
            columns: [[
                {
                    width: '150',
                    title: '原余额',
                    field: 'orgAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '发生额',
                    field: 'occurAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '最新余额',
                    field: 'lastAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '区块转账费用',
                    field: 'netFee'
                }, {
                    width: '150',
                    title: '手续费',
                    field: 'netFee',
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '300',
                    title: '提现地址',
                    field: 'withdrawAddr',
                    sortable: true
                }, {
                    width: '300',
                    title: '交易ID',
                    field: 'transId'
                }, {
                    width: '100',
                    title: '备注',
                    field: 'remark'
                }, {
                    width: '100',
                    title: '创建人',
                    field: 'accountName'
                }, {
                    width: '150',
                    title: '创建时间',
                    field: 'createDate',
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
                <shiro:hasPermission name="trade:setting:walletTransferCurrent:operator">
                $('.tixian-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
                $("#walletTransferCurrentDataGrid").datagrid('unselectAll');
            },
            toolbar: '#withdrawRecordToolbar'
        });
    });

    function addWalletTransferCurrentData() {
        parent.$.modalDialog({
            title: '新增',
            width: 680,
            height: 360,
            href: '${ctx}/bitpay/walletTransferCurrent/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = walletTransferCurrentDataGrid;
                    var f = parent.$.modalDialog.handler.find('#walletTransferCurrentForm');
                    f.submit();
                }
            }]
        });
    }

    function searchWithdrawRecordFun() {
        walletTransferCurrentDataGrid.datagrid('load', $.serializeObject($('#searchWithdrawRecordCurrentForm')));
    }
    function cleanWithdrawRecordFun() {
        $('#searchWithdrawRecordCurrentForm input').val('');
        walletTransferCurrentDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchWithdrawRecordCurrentForm">
            <table>
                <tr>
                    <td>证券信息</td>
                    <td colspan="3">
                        <input  name="stockinfoId"  class="easyui-combobox" name="language" style="width: 200px;"
                                placeholder="请选择证券"  data-options="
								url: '${ctx}/stock/info/allCanAdjustCoin', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    <th>提现地址:</th>
                    <td><input name="withdrawAddr" class="easyui-textbox" placeholder="请输入提现地址"/></td>
                    <th>区块交易ID:</th>
                    <td><input name="transId" class="easyui-textbox" placeholder="请输入区块交易ID"/></td>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchWithdrawRecordFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanWithdrawRecordFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="walletTransferCurrentDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="withdrawRecordToolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:walletTransferCurrent:operator">
        <a onclick="addWalletTransferCurrentData();" href="javascript:void(0);" class="easyui-linkbutton"
           data-options="plain:true,iconCls:'fi-plus icon-blue'">新增</a>
    </shiro:hasPermission>
</div>
