<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">

    var debitDataGrid2;
    var debitDataGrid;
    $(function () {
        debitDataGrid2 = $('#debitDataGrid2').datagrid({
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                {
                    width: '200',
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
                    width: '130',
                    title: '关联证券ID',
                    field: 'relatedStockinfoId',
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
                width: '150',
                title: '操作时间',
                field: 'currentDate',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            },{
                width: '150',
                title: '业务类别',
                field: 'businessFlag',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            },{
                width: '120',
                title: '原资产数量余额',
                field: 'orgAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '80',
                title: '资产发生方向',
                field: 'occurDirect',
                hidden:true,
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            },{
                width: '120',
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
                sortable: true,
                hidden:true
            }, {
                width: '300',
                title: '备注',
                field: 'remark',
                sortable: true
            }, {
                width: '70',
                title: '审批状态',
                field: 'approveStatus',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                },
                hidden:true
            }, {
                width: '70',
                title: '划拨状态',
                field: 'transferStatus',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                },
                hidden:true
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

            }
        });
    });

    debitDataGrid = $('#debitDataGrid').datagrid({
        url: '${ctx}/fund/debitAsset/data',
        striped: true,
        rownumbers: true,
        pagination: true,
        queryParams:{relatedStockinfoId:<%=FundConsts.WALLET_BTC2USD_TYPE%>},
        singleSelect: true,
        idField: 'id',
        sortName: 'id',
        sortOrder: 'asc',
        pageSize: 20,
        pageList: [10, 20, 30, 40, 50, 100],
        frozenColumns:[[
            {
                width: '120',
                title: '贷款人账户ID',
                field: 'lenderAccountId',
                sortable: true
            },{
                width: '200',
                title: '贷款人账户名',
                field: 'lenderAccountName',
                sortable: true
            },{
                width: '120',
                title: '借款人账户ID',
                field: 'borrowerAccountId',
                sortable: true
            },{
                width: '200',
                title: '借款人账户名',
                field: 'borrowerAccountName',
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
                width: '130',
                title: '关联证券ID',
                field: 'relatedStockinfoId',
                sortable: true
            }, {
                width: '80',
                title: '关联证券代码',
                field: 'relatedStockCode',
                sortable: true
            },{
                width: '150',
                title: '借贷数量或金额',
                field: 'debitAmt',
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }
        ]],
        columns: [[
            {
                width: '150',
                title: '备注',
                field: 'remark',
                sortable: true
            }, {
                width: '140',
                title: '修改时间',
                field: 'updateDate',
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
            var rows=$('#debitDataGrid').datagrid("getRows");
            if(rows.length>0){
                var opts = debitDataGrid2.datagrid("options");
                opts.url = '${ctx}/fund/account/data';
                debitDataGrid2.datagrid('reload',{originalBusinessId:rows[0].id,stockinfoId:rows[0].stockinfoId,accountId:rows[0].borrowerAccountId,relatedStockinfoId:rows[0].relatedStockinfoId});
                $('#debitDataGrid').datagrid('selectRow', 0);
            }
        },
        onClickRow:function(rowIndex, rowData){
            debitDataGrid2.datagrid('reload',{originalBusinessId:rowData.id,stockinfoId:rowData.stockinfoId,accountId:rowData.borrowerAccountId,relatedStockinfoId:rowData.relatedStockinfoId});
        }
    });

    function searchdebitRecordFun() {
        debitDataGrid.datagrid('load', $.serializeObject($('#searchdebitRecordForm')));
    }
    function cleandebitRecordFun() {
        $('#searchdebitRecordForm input').val('');
        debitDataGrid.datagrid('load', {});
    }

</script>
<input type="hidden" id="productId" />
<div class="easyui-layout" data-options="fit:true,border:false" style="width:100%;height:100%;">
    <div data-options="region:'north',split:true" style="width: 580px;height:40px;">
        <form id="searchdebitRecordForm">
            <table>
                <tr>
                    <th>贷款人账户名:</th>
                    <td><input name="lenderAccountName" class="easyui-textbox" placeholder="请输入贷款人账户名"/></td>
                    <th>借款人账户名:</th>
                    <td><input name="borrowerAccountName" class="easyui-textbox" placeholder="请输入借款人账户名"/></td>
                    <!--
                    <th>证券代码:</th>
                    <td>
                        <input  id="check_stockCode2" name="stockinfoId"  class="easyui-combobox" name="language" style="width: 142px;"
                                placeholder="请选择证券"  value="${stockRate.stockinfoId}" data-options="
									url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
									textField:'stockCode', groupField:'group'"  >
                    </td>
                    -->
                    <th>交易对:</th>
                    <td>
                        <input  id="check_stockCode3" name="relatedStockinfoId"  class="easyui-combobox" name="language" style="width: 142px;"
                                placeholder="请选择证券"  value="<%=FundConsts.WALLET_BTC2USD_TYPE %>" data-options="
									url: '${ctx}/stock/info/allInExchange', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group',required:true"  >
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchdebitRecordFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleandebitRecordFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center'"   style="width: 580px;height:400px;">
        <table id="debitDataGrid" data-options="fit:true,border:false,title:'借款管理'"></table>
    </div>
    <div data-options="region:'south'"   style="width: 580px;height:200px;">
        <table id="debitDataGrid2" data-options="fit:true,border:false,title:'合约资产借款明细'"></table>
    </div>
</div>
<jsp:include page="/commons/setup_ajax.jsp"></jsp:include>