<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var walletCashTransferCurrentDataGrid;
    $(function () {
        walletCashTransferCurrentDataGrid = $('#walletCashTransferCurrentDataGrid').datagrid({
            url: '${ctx}/fund/walletCashTransferCurrent/data',
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
                    width: '100',
                    title: '证券ID',
                    hidden: true,
                    field: 'stockinfoId'
                }, {
                    width: '100',
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
                        return parseFloat(value).toFixed(8);
                    }
                }, {
                    width: '150',
                    title: '发生额',
                    field: 'occurAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(8);
                    }
                }, {
                    width: '150',
                    title: '最新余额',
                    field: 'lastAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(8);
                    }
                }, {
                    width: '150',
                    title: '手续费',
                    field: 'fee',
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(8);
                    }
                }, {
                    width: '200',
                    title: '交易ID',
                    field: 'transId'
                }, {
                    width: '120',
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
                <shiro:hasPermission name="trade:setting:walletCashTransferCurrent:operator">
                $('.tixian-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
                $("#walletCashTransferCurrentDataGrid").datagrid('unselectAll');
            },
            toolbar: '#walletTransferCurrentToolbar'
        });
    });

    function addWalletTransferCurrentData() {
        parent.$.modalDialog({
            title: '新增',
            width: 680,
            height: 360,
            href: '${ctx}/fund/walletCashTransferCurrent/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = walletCashTransferCurrentDataGrid;
                    var f = parent.$.modalDialog.handler.find('#walletCashTransferCurrentForm');
                    f.submit();
                }
            }]
        });
    }

    function searchWithdrawRecordFun() {
        walletCashTransferCurrentDataGrid.datagrid('load', $.serializeObject($('#searchWithdrawRecordForm')));
    }
    function cleanWithdrawRecordFun() {
        $('#searchWithdrawRecordForm input').val('');
        walletCashTransferCurrentDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchWithdrawRecordForm">
            <table>
                <tr>
                    <th>交易ID:</th>
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
        <table id="walletCashTransferCurrentDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="walletTransferCurrentToolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:walletCashTransferCurrent:operator">
        <a onclick="addWalletTransferCurrentData();" href="javascript:void(0);" class="easyui-linkbutton"
           data-options="plain:true,iconCls:'fi-plus icon-blue'">新增</a>
    </shiro:hasPermission>
</div>
