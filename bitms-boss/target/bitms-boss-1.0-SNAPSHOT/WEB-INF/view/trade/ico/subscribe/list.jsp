<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var icoSubscribeDataGrid;
    $(function () {
        icoSubscribeDataGrid = $('#icoSubscribeDataGrid').datagrid({
            url: '${ctx}/ico/subscribe/data',
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
                width: '70',
                title: '认购数量',
                field: 'subAmt'
            }, {
                width: '70',
                title: '认购价格',
                field: 'subPrice'
            }, {
                width: '70',
                title: '认购金额',
                field: 'subBalance'
            }, {
                width: '140',
                title: '认购时间',
                field: 'subTime',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '70',
                title: '成交数量',
                field: 'dealAmt'
            }, {
                width: '70',
                title: '成交价格',
                field: 'dealPrice'
            }, {
                width: '70',
                title: '成交金额',
                field: 'dealBalance'
            }, {
                width: '70',
                title: '认购状态',
                field: 'subStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '70',
                title: '备注',
                field: 'remark'
            }, {
                width: '70',
                title: '锁定状态',
                field: 'lockStatus'
            }, {
                width: '140',
                title: '锁定结束时间',
                field: 'lockEndDay',
                formatter: function (value, row, index) {
                	if(value == '' || value == null){
                		return '';
                	}else{
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                	}
                    
                }
            }
                ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
            },
            toolbar: '#icoSubscribeToolbar'
        });
    });
    function searchIcoSubscribeFun() {
    	icoSubscribeDataGrid.datagrid('load', $.serializeObject($('#searchIcoSubscribeForm')));
    }
    function cleanIcoSubscribeFun() {
        $('#searchIcoSubscribeForm input').val('');
        icoSubscribeDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchIcoSubscribeForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input name="accountName" class="easyui-textbox"  style="width:100px;" placeholder="请输入账户名"/></td>
                    <th>数字货币名称:</th>
                    <td><input name="digitalCurrencyName"  class="easyui-textbox" style="width:100px;" placeholder="请输入数字货币名称"/></td>
                    <th>认购开始时间:</th>
                    <td><input name="timeStart"  style="width:150px;" class="easyui-datetimebox"  placeholder="请输入认购开始时间"/></td>
                    <th>认购结束时间:</th>
                    <td><input name="timeEnd"  style="width:150px;" class="easyui-datetimebox"  placeholder="请输入认购结束时间"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchIcoSubscribeFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanIcoSubscribeFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:false">
        <table id="icoSubscribeDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="icoSubscribeToolbar" style="display: none;">
</div>