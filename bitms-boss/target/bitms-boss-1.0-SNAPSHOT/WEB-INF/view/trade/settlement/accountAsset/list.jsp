<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var accountAssetListGrid;
    $(function () {
        accountAssetListGrid = $('#accountAssetListGrid').datagrid({
            url: '${ctx}/settlement/settlementAccountAsset/list/data',
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
                    width: '150',
                    title: '账户名',
                    field: 'accountName',
                    sortable: true
                }, {
                    width: '100',
                    title: '本身证券信息id',
                    field: 'stockinfoId',
                    sortable: true
                },{
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
                width: '150',
                title: '交割结算价格',
                field: 'settlementPrice',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '期间期初数量',
                field: 'periodInitAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '期间流入数量',
                field: 'periodInflowAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '期间流出数量',
                field: 'periodOutflowAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '150',
                title: '期间最后数量',
                field: 'periodLastAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '期间分摊数量',
                field: 'periodAssessmentAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '100',
                title: '期间分摊比例',
                field: 'periodAssessmentRate',
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
    function searchAccountAssetAllFun() {
        accountAssetListGrid.datagrid('load',
            $.serializeObject($('#earchAccountAssetAllForm')));
    }
    function cleanAccountAssetAllFun() {
        $('#earchAccountAssetAllForm input').val('');
        accountAssetListGrid.datagrid('load',{});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="earchAccountAssetAllForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input  name="accountName" class="easyui-textbox" placeholder="请输入账户名"/></td>
                    <th>交割结算类型:</th>
                    <td>
                    <select  class="easyui-combobox" name="settlementAccountTypeString" style="width:100px;"  data-options="required:true">
                        <option value="">-请选择-</option>
                        <option value="1">交割</option>
                        <option value="2">结算</option>
                    </select>
                    </td>
                    <th>交割结算时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox"  placeholder="请输入交割结算开始时间"/></td>
                    <th>至</th>
                    <td><input name="timeEnd" class="easyui-datetimebox"   placeholder="请输入交割结算结束时间"/></td>
                    <td>
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountAssetAllFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountAssetAllFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="accountAssetListGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
