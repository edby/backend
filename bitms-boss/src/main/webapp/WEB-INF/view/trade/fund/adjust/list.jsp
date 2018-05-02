<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var adjustDataGrid;
    $(function () {
        adjustDataGrid = $('#adjustDataGrid').datagrid({
            url: '${ctx}/fund/adjust/data',
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
							    width: '200',
							    title: '账户名',
							    field: 'accountName',
							    sortable: true
							},{
            				    width: '100',
            				    title: '证券代码',
            				    field: 'stockCode',
            				    sortable: true
            				},{
            	                width: '100',
            	                title: '业务类型',
            	                field: 'businessFlag',
            	                formatter: function (value, row, index) {
            	                	return getDictValueByCode(value);
            	                }
            	            },{
            	                width: '100',
            	                title: '调整类型',
            	                field: 'adjustType',
            	                formatter: function (value, row, index) {
            	                	return getDictValueByCode(value);
            	                }
            	            }, {
            	                width: '150',
            	                title: '调整数量',
            	                field: 'adjustAmt',
            	                sortable: true,
                                formatter: function (value, row, index) {
                                    return parseFloat(value).toFixed(12);
                                }
            	            }
                           ]],       
            columns: [[
            	{
	                width: '100',
	                title: '是否需要锁定',
	                field: 'needLock'
	            }, {
	                width: '70',
	                title: '锁定状态',
	                field: 'lockStatus'
	            },{
                    width: '140',
                    title: '锁定结束时间',
                    field: 'lockEndDay',
                    formatter: function (value, row, index) {
                    	if(value>0){
                            return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    	}else{
                    		return '';
                    	}
                	}
                },{
                width: '100',
                title: '备注',
                field: 'remark',
                sortable: true
            },{
                width: '100',
                title: '创建人',
                field: 'createByName'
            }, {
                width: '140',
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
            },
            toolbar: '#adjustToolbar'
        });
    });

    function addAdjustFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 400,
            href: '${ctx}/fund/adjust/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = adjustDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editAdjustForm');
                    f.submit();
                }
            }]
        });
    }
    function searchAdjustFun() {
    	adjustDataGrid.datagrid('load', $.serializeObject($('#searchAdjustForm')));
    }
    function cleanAdjustFun() {
        $('#searchAdjustForm input').val('');
        adjustDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAdjustForm">
            <table>
                <tr>
                    <th>证券代码:</th>
                    <td>
                    	<input  id="check_stockCode2" name="stockinfoId"  class="easyui-combobox" name="language" style="width: 142px;"  
	                        placeholder="请选择证券"  value="${stockRate.stockinfoId}" data-options="
									url: '${ctx}/stock/info/allCanAdjustCoin', method: 'get', valueField:'id',
									textField:'stockCode', groupField:'group'"  >
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAdjustFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAdjustFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="adjustDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="adjustToolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:accountfundadjust:operator">
    <a onclick="addAdjustFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>
