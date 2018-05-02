<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var positionPremiumFeeDataGrid;
    $(function () {
        positionPremiumFeeDataGrid = $('#positionPremiumFeeDataGrid').datagrid({
            url: '${ctx}/fund/positionPremiumFee/data',
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
                    width: '140',
                    title: '账户ID',
                    field: 'accountId'
                }, {
                    width: '150',
                    title: '账户名称',
                    field: 'accountName',
                    sortable: true
                }, {
                    width: '100',
                    title: 'USD持仓',
                    field: 'positionAmt',
                    sortable: true
                }
            ]],
            columns: [[ {
                width: '100',
                title: '平台价格',
                field: 'paltformPrice'
            }, {
                width: '100',
                title: '风控基价',
                field: 'indexPrice'
            }, {
                width: '100',
                title: '溢价费',
                field: 'premiumFee'
            }, {
                width: '100',
                title: '溢价费率',
                field: 'premiumFeeRate',
                formatter: function (value, row, index) {
                    return ((value*100).toFixed(2)+"%");
                }
            }, {
                width: '100',
                title: '溢价率',
                field: 'premiumRate'
            }, {
                width: '150',
                title: '备注',
                field: 'remark'
            }, {
                width: '150',
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
            }
        });
    });
    function searchPositionPremiumFeeFun() {
        positionPremiumFeeDataGrid.datagrid('load', $.serializeObject($('#searchPositionPremiumFeeForm')));
    }
    function cleanPositionPremiumFeeFun() {
        $('#searchPositionPremiumFeeForm input').val('');
        positionPremiumFeeDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchPositionPremiumFeeForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入账户名"/></td>
                    <th>开始时间:</th>
                    <td><input name="timeStart" style="width:120px;"  class="easyui-datebox"  placeholder="请输入开始时间"/></td>
                    <th>结束时间:</th>
                    <td><input name="timeEnd"  style="width:120px;" class="easyui-datebox"   placeholder="请输入结束时间"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchPositionPremiumFeeFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanPositionPremiumFeeFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="positionPremiumFeeDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>

