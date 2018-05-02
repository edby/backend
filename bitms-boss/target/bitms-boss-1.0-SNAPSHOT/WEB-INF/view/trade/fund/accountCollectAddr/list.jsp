<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.blocain.bitms.trade.fund.consts.FundConsts"%>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var accountCollectAddrDataGrid;
    $(function () {
    	accountCollectAddrDataGrid = $('#accountCollectAddrDataGrid').datagrid({
            url: '${ctx}/fund/accountCollectAddr/data',
            fit: true,
            queryParams:{
                status:'<%=FundConsts.PUBLIC_STATUS_NO%>'
            },
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                <shiro:hasPermission name="trade:setting:accountCollectAddr:operator">
                 {
                    field: 'action',
                    title: '操作',
                    width: 120,
                    formatter: function (value, row, index) {
                        var str = '';
                        if(row.status=="no"){
                        str += $.formatString('<a href="javascript:void(0)"  class="accountCollectAddr-easyui-linkbutton-dongjie1" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editAccountCollectAddrFun(\'{0}\');" >审批</a>', row.id);
                        }else{
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                           /* str += $.formatString('<a href="javascript:void(0)"  class="accountCollectAddr-easyui-linkbutton-dongjie1" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" aria-disabled="true" style="color:gray" >审批</a>', row.id);*/
                        }
                        return str;
                    }
                },
                </shiro:hasPermission>
			{
			    width: '150',
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
			    width: '300',
			    title: '收款地址',
			    field: 'collectAddr',
			    sortable: true,
			}		
                            ]],
            columns: [[ {
                width: '150',
                title: '创建时间',
                field: 'createDate',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            },{
                width: '70',
                title: '认证状态',
                field: 'certStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '70',
                title: '激活状态',
                field: 'isActivate',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            },{
                width: '70',
                title: '审核状态',
                field: 'status',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            }, {
                width: '600',
                title: '备注',
                field: 'remark',
                sortable: true
            }
                ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
                <shiro:hasPermission name="trade:setting:accountCollectAddr:operator">
                $('.accountCollectAddr-easyui-linkbutton-dongjie1').linkbutton({text: '审核'});
                </shiro:hasPermission>
            }
        });
    });

    function editAccountCollectAddrFun(id) {
        if (id == undefined) {
            var rows = accountCollectAddrDataGrid.datagrid('getSelections');
            id = rows[0].accountCollectAddrName;
        } else {
            accountCollectAddrDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:accountCollectAddr:operator">
        parent.$.modalDialog({
            title: '审核',
            width: 1000,
            height: 500,
            href: '${ctx}/fund/accountCollectAddr/approval?id='+id,
            buttons: [{
                text: '通过',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = accountCollectAddrDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#accountCollectAddrForm');
                    parent.$.modalDialog.handler.find('#accountCollectAddrForm #status').val('<%=FundConsts.ASSET_LOCK_STATUS_YES %>');
                    f.submit();
                }
            }
            /*,{
            text: '拒绝',
                handler: function () {
                parent.$.modalDialog.openner_dataGrid = accountCollectAddrDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                var f = parent.$.modalDialog.handler.find('#accountCollectAddrForm');
                    parent.$.modalDialog.handler.find('#accountCollectAddrForm #status').val('<%=FundConsts.ASSET_LOCK_STATUS_NO %>');
                f.submit();
                }
            }*/
        ]
        });
        </shiro:hasPermission>
    }
    function searchAccountFundCurrentFun() {
    	accountCollectAddrDataGrid.datagrid('load',
            $.serializeObject($('#searchAccountColletAddrCurrentForm')));
    }
    function cleanAccountFundCurrentFun() {
        $('#searchAccountColletAddrCurrentForm input').val('');
        accountCollectAddrDataGrid.datagrid('load', {});
    }
    $(function(){
        $("#collectAddrStatusTd").html(dictDropDownOptionsList('collectAddrStatus','status', 'yesOrNo','<%=FundConsts.PUBLIC_STATUS_NO %>', '',  'width:142px,'));
        $("#collectAddrStatus").combobox({
            valueField:'code',
            textField:'name'
        });
    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAccountColletAddrCurrentForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="accountName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <th>证券代码:</th>
                    <td>
                        <input  id="approve_stockCode" name="stockinfoId"  class="easyui-combobox" name="language" style="width: 142px;"
                                placeholder="请选择证券"  data-options="
									url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_DIGITALCOIN%>', method: 'get', valueField:'id',
									textField:'stockCode', groupField:'group'"  >
                    </td>
                    <th>是否已通过:</th>
                    <td id="collectAddrStatusTd">

                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountFundCurrentFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountFundCurrentFun();">清空</a>
                    </td>
                     
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="accountCollectAddrDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
