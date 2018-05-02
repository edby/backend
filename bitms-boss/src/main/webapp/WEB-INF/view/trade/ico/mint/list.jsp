<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var icoMintDataGrid;
    $(function () {
        icoMintDataGrid = $('#icoMintDataGrid').datagrid({
            url: '${ctx}/ico/mint/data',
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
			    title: '主键',
			    field: 'id',
			    hidden: true
			}, {
			    width: '150',
			    title: '账户名',
			    field: 'accountName',
			    sortable: true
			}, {
			    width: '150',
			    title: '数字货币名称',
			    field: 'digitalCurrencyName',
			    sortable: true
			}  		
                            ]],
            columns: [[{
                width: '150',
                title: '锻造时间',
                field: 'mintTime',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '100',
                title: '原始锻造数量',
                field: 'orgMintAmt'
            }, {
                width: '100',
                title: '目标锻造数量',
                field: 'targetMinAmt'
            }, {
                width: '100',
                title: '实际锻造数量',
                field: 'realMinAmt'
            }, {
                width: '70',
                title: '锻造状态',
                field: 'mintStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '100',
                title: '备注',
                field: 'remark'
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
    function searchIcoMintFun() {
    	icoMintDataGrid.datagrid('load', $.serializeObject($('#searchIcoMintForm')));
    }
    function cleanIcoMintFun() {
        $('#searchIcoMintForm input').val('');
        icoMintDataGrid.datagrid('load', {});
    }
  
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchIcoMintForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input name="accountName" class="easyui-textbox" style="width:100px;" placeholder="请输入账户名"/></td>
                    <th>数字货币名称:</th>
                    <td><input name="digitalCurrencyName" class="easyui-textbox" style="width:100px;" placeholder="请输入数字货币名称"/></td>
                    <th>锻造开始时间:</th>
                    <td><input name="timeStart" style="width:150px;" class="easyui-datetimebox"  placeholder="请输入认购开始时间"/></td>
                    <th>锻造结束时间:</th>
                    <td><input name="timeEnd" style="width:150px;" class="easyui-datetimebox"  placeholder="请输入认购结束时间"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchIcoMintFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanIcoMintFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:false">
        <table id="icoMintDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
