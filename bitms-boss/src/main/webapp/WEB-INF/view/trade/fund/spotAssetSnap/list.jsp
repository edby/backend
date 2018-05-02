<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 60px; overflow: hidden;background-color: #fff">
        <form id="spotSnapAccountAssetForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <td>资产分类</td>
                    <td>
                        <input  name="stockinfoId"  class="easyui-combobox" name="language"
                                placeholder="请选择资产分类" value="<%=FundConsts.WALLET_USD_TYPE%>"  data-options="
								url: '${ctx}/stock/info/allCoin', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    <td>专区</td>
                    <td>
                        <input name="relatedStockinfoId" class="easyui-combobox" name="language"
                               placeholder="请选择专区" value="<%=FundConsts.WALLET_USD_TYPE%>" data-options="
								url: '${ctx}/stock/info/allCoin', method: 'get',  valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    <td colspan="4">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchSpotAssetFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanSpotAssetFun();">清空</a>
                    </td>
                </tr>
                <tr>
                    <th width="80px">风控基价:</th>
                    <td width="80px" id="indexPrice">&nbsp;</td>
                    <th width="80px">最新成交价:</th>
                    <td width="80px" id="paltformPrice">&nbsp;</td>
                    <th width="80px">溢价:</th>
                    <td width="80px" id="premiumPrice">&nbsp;</td>
                    <th width="80px">溢价率:</th>
                    <td width="80px" id="premiumRate">&nbsp;</td>
                    <th width="80px">快照时间:</th>
                    <td width="140px" id="marketTimestamp">&nbsp;</td>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="spotSnapAccountDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<script type="text/javascript">
    var spotSnapAccountDataGrid;
    $(function () {
        spotSnapAccountDataGrid = $('#spotSnapAccountDataGrid').datagrid({
            url: '${ctx}/fund/spotAssetSnap/data',
            fit: true,
            striped: true,
            rownumbers: true,
            queryParams: {
                stockinfoId: <%=FundConsts.WALLET_USD_TYPE%>,
                relatedStockinfoId:<%=FundConsts.WALLET_USD_TYPE%>
            },
            pagination: true,
            singleSelect: true,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                {
                    width: '150',
                    title: '账户名',
                    field: 'accountName',
                    sortable: true
                }, {
                    width: '80',
                    title: '证券代码',
                    field: 'stockCode'
                }, {
                    width: '80',
                    title: '专区代码',
                    field: 'areaCode'
                },{
                    width: '100',
                    title: '方向',
                    field: 'direction',
                    hidden:true
                }, {
                    width: '150',
                    title: '价格',
                    field: 'price',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    },
                    hidden:true
                },{
                    width: '150',
                    title: '当前数量',
                    field: 'amount',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '冻结数量',
                    field: 'frozenAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '负债数量',
                    field: 'debitAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '净数量',
                    field: 'id2',
                    formatter: function (value, row, index) {
                        var debitAmt=0;
                        if(row.debitAmt !=null && row.debitAmt!='')
                        {
                            debitAmt=row.debitAmt;
                        }
                        return (row.amount-debitAmt).toFixed(12);
                    }
                },{
                    width: '300',
                    title: '备注',
                    field: 'remark',
                    hidden:true
                },{
                    width: '150',
                    title: '修改时间',
                    field: 'updateDate',
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
                else
                {
                    getMarketSnap();
                }
            }
        });
    });
    function getMarketSnap() {
        $.post('${ctx}/fund/marketSnap/data', {
        }, function (result) {
            if (result.code == 200) {
                $("#paltformPrice").html(result.object.paltformPrice.toFixed(8));
                $("#indexPrice").html(result.object.indexPrice.toFixed(8));
                $("#premiumPrice").html(result.object.premiumPrice.toFixed(8));
                $("#premiumRate").html(result.object.premiumRate.toFixed(8));
                $("#marketTimestamp").html(getFormatDateByLong(result.object.marketTimestamp*1, "yyyy-MM-dd hh:mm:ss"));
            }
        }, 'JSON');
    }
    function searchSpotAssetFun() {
        spotSnapAccountDataGrid.datagrid('load', $.serializeObject($('#spotSnapAccountAssetForm')));
    }
    function cleanSpotAssetFun() {
        $('#spotSnapAccountAssetForm input').val('');
        spotSnapAccountDataGrid.datagrid('load', {});
    }
</script>

