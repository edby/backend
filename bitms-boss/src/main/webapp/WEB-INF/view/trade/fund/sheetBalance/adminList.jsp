<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var sheetBalance22DataGrid;
    $(function () {
        sheetBalance22DataGrid = $('#sheetBalance22DataGrid').datagrid({
            url: '${ctx}/fund/sheetBalanceAdmin/data',
            queryParams:{
               
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
                    width: '150',
                    title: '账户ID',
                    field: 'accountId'
                },{
                    width: '250',
                    title: '账户名',
                    field: 'accountName'
                }, {
                    width: '120',
                    title: '证券代码',
                    field: 'stockCode'
                }, {
                    width: '350',
                    title: '营收量',
                    field: 'balanceAmt',
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }
                ]],

            columns: [[
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
    
    function searchSheetBalance22Fun() {
        var isValid = $("#searchSheetBalance22Form").form('validate');
        if(isValid)
        {
            sheetBalance22DataGrid.datagrid('load',
                $.serializeObject($('#searchSheetBalance22Form')));
        }
    }
    function cleanSheetBalance22Fun() {
        $('#searchSheetBalance22Form input').val('');
        sheetBalance22DataGrid.datagrid('load',{});
    }
    function exportSheetBalanceData(){
        $("#searchSheetBalance22Form").attr("action", "${ctx}/fund/sheetBalanceAdmin/export");
        $("#searchSheetBalance22Form").submit();
        $("#searchSheetBalance22Form").attr("action", "");
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchSheetBalance22Form">
            <table>
                <tr>
                    <th>开始日期(YYYYMMDD):</th>
                    <td><input class="easyui-numberbox" id="startDate"  data-options="required:true,min:20180101,precision:0,max:20991231,minLength:8,maxLength:8"  name="startDate" placeholder="请输入资产负债日"/></td>
                    <th>结束日期(YYYYMMDD):</th>
                    <td><input class="easyui-numberbox" id="endDate"  data-options="required:true,min:20180101,precision:0,max:20991231,minLength:8,maxLength:8" name="endDate" placeholder="请输入资产负债日"/></td>
                    <td>证券代码</td>
                    <td>
                        <input  id="stockinfoId" name="stockinfoId"  class="easyui-combobox" name="language"
                                placeholder="请选择证券"  value="${stockRate.stockinfoId}" data-options="
								url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchSheetBalance22Fun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanSheetBalance22Fun();">清空</a>
                        <a onclick="exportSheetBalanceData();" href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="plain:true,iconCls:'fi-save icon-green'">导出</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="sheetBalance22DataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
