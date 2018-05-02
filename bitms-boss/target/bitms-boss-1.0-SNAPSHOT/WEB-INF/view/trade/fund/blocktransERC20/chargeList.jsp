<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var erc20BlocktransconfirmDataGrid;
    $(function () {
        erc20BlocktransconfirmDataGrid = $('#erc20BlocktransconfirmDataGrid').datagrid({
            url: '${ctx}/fund/blocktranserc20/chargeData',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns: [[
                {
                    width: '100',
                    title: '主键',
                    field: 'id',
                    hidden: true
                }, {
                    width: '100',
                    title: '账户名',
                    field: 'accountName',
                    sortable: true
                }, {
                    width: '100',
                    title: '证券ID',
                    field: 'stockinfoId',
                    sortable: true
                }, {
                    width: '80',
                    title: '证券代码',
                    field: 'stockCode',
                    sortable: true
                }, {
                    width: '80',
                    title: '证券名称',
                    field: 'stockName',
                    sortable: true,
                    hidden: true
                }, {
                    width: '80',
                    title: '证券类型',
                    field: 'stockType',
                    hidden: true,
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }, {
                    width: '120',
                    title: '钱包ID',
                    field: 'walletId',
                    sortable: true
                }, {
                    width: '120',
                    title: '钱包名称',
                    field: 'walletName',
                    sortable: true,
                    hidden: true

                }, {
                    width: '200',
                    title: '钱包地址',
                    field: 'walletAddr',
                    sortable: true
                }, {
                    width: '90',
                    title: '区块确认状态',
                    field: 'status',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }, {
                    width: '100',
                    title: '区块归集状态',
                    field: 'collectStatus'
//                formatter: function (value, row, index) {
//                    return getDictValueByCode(value);
//                }
                }
            ]],
            columns: [[{
                width: '100',
                title: '资产方向',
                field: 'direct',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            }, {
                width: '250',
                title: '交易ID',
                field: 'transId'
            }, {
                width: '150',
                title: '发生额',
                field: 'amount',
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '150',
                title: '手续费',
                field: 'fee',
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '100',
                title: '区块确认方',
                field: 'confirmSide'
            }, {
                width: '120',
                title: '区块哈希',
                field: 'blockHash'
            }, {
                width: '100',
                title: '区块高度',
                field: 'blockNumber',
                sortable: true
            }, {
                width: '150',
                title: '备注',
                field: 'remark',
                sortable: true
            }, {
                width: '120',
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
            }
        });
    });

    function searchErc20BlockTransConfirmFun() {
        erc20BlocktransconfirmDataGrid.datagrid('load', $.serializeObject($('#searchErc20BlockTransConfirmForm')));
    }

    function cleanErc20BlockTransConfirmFun() {
        $('#searchErc20BlockTransConfirmForm input').val('');
        erc20BlocktransconfirmDataGrid.datagrid('load', {});
    }

    //
    //    $(function () {
    //
    //        $("#statusChargeTd").html(dictDropDownOptionsList('statusCharge', 'status', 'confirmStatus', '', '', 'width:142px,'));
    //        $("#statusCharge").combobox({
    //            valueField: 'code',
    //            textField: 'name'
    //        });
    //
    //    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchErc20BlockTransConfirmForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入账户名"/></td>
                    <th>证券代码:</th>
                    <td>
                        <input id="allot_stockCode" name="stockCode" class="easyui-combobox" name="language"
                               style="width: 142px;"
                               placeholder="请选择证券" value="" data-options="
									url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_ERC20_TOKEN%>', method: 'get', valueField:'stockCode',
									textField:'stockCode', groupField:'group'">
                    </td>
                    <th>区块确认状态:</th>
                    <td>
                        <select id="status" class="easyui-combobox" name="status" style="width:80px;">
                            <option value="">-请选择-</option>
                            <option value="confirm">已确认</option>
                            <option value="unconfirm">未确认</option>
                        </select>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true"
                           onclick="searchErc20BlockTransConfirmFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanErc20BlockTransConfirmFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="erc20BlocktransconfirmDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
