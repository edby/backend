<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var recordListGrid;
    $(function () {
        recordListGrid = $('#recordListGrid').datagrid({
            url: '${ctx}/settlement/settlementRecord/list/data',
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
                    title: '本身证券信息id',
                    field: 'stockinfoId',
                    sortable: true
                }, {
                    width: '100',
                    title: '关联证券信息id',
                    field: 'relatedStockinfoId',
                    sortable: true
                },{
                    width: '100',
                    title: '交割结算类型',
                    field: 'settlementType',
                    formatter: function (value, row, index) {
                        if(value)
                        {
                            return '交割';
                        }
                        else
                        {
                            return '结算';
                        }
                    }
                }
            ]],
            columns: [[{
                width: '100',
                title: '证券代码',
                field: 'stockCode',
                sortable: true
            },{
                width: '150',
                title: '交割结算时间',
                field: 'settlementTime',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '100',
                title: '交割结算价格',
                field: 'settlementPrice',
                sortable: true
            },{
                width: '110',
                title: '准备金分摊数量',
                field: 'reserveAllocatAmt',
                sortable: true
            },{
                width: '110',
                title: '准备金原始数量',
                field: 'reserveOrgAmt',
                sortable: true
            },{
                width: '110',
                title: '准备金最新数量',
                field: 'reserveLastAmt',
                sortable: true
            }, {
                width: '140',
                title: '穿仓用户亏损分摊数量',
                field: 'wearingSharingLossesAmt',
                sortable: true
            },{
                width: '100',
                title: '分摊比例',
                field: 'assessmentRate',
                sortable: true
            },{
                width: '200',
                title: '备注',
                field: 'remark',
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
    function searchRecordAllFun() {
        recordListGrid.datagrid('load',
            $.serializeObject($('#earchRecordAllForm')));
    }
    function cleanRecordAllFun() {
        $('#earchRecordAllForm input').val('');
        recordListGrid.datagrid('load',{});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="earchRecordAllForm">
            <table>
                <tr>
                    <th>交割结算类型:</th>
                    <td>
                        <select  class="easyui-combobox" name="settlementTypeString" style="width:100px;"  data-options="required:true">
                            <option value="">-请选择-</option>
                            <option value="1">交割</option>
                            <option value="2">结算</option>
                        </select>
                    </td>
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchRecordAllFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanRecordAllFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="recordListGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
