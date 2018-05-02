<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var systemWalletDataGrid;
    var systemWalletDataGrid2;
    $(function () {
    	systemWalletDataGrid2 = $('#systemWalletDataGrid2').datagrid({
    		//url: '${ctx}/fund/systemwallet/addr/data',
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
				    width: '260',
				    title: '钱包ID',
				    field: 'walletId',
				    sortable: true
				}, {
				    width: '150',
				    title: '钱包名称',
				    field: 'walletName',
				    sortable: true
				},{
				    width: '260',
				    title: '钱包地址',
				    field: 'walletAddr',
				    sortable: true
				}		
                            ]],
            columns: [[{
                width: '130',
                title: '证券ID',
                field: 'stockinfoId',
                sortable: true
            }, {
                width: '100',
                title: '证券代码',
                field: 'stockCode',
                sortable: true
            }, {
                width: '150',
                title: '证券名称',
                field: 'stockName',
                sortable: true,
                hidden:true
            }, {
                width: '200',
                title: '关联账户',
                field: 'accountName',
                sortable: true
            },{
                width: '150',
                title: '总接收',
                field: 'received',
                sortable: true
            },{
                width: '150',
                title: '未确认总接收',
                field: 'unconfirmedReceived',
                sortable: true
            },{
                width: '150',
                title: '备注',
                field: 'remark',
                sortable: true
            },{
                width: '150',
                title: '创建时间',
                field: 'createDate',
                hidden:true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }<shiro:hasPermission name="trade:setting:walletaddr:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 140,
                    formatter: function (value, row, index) {
                        var str = '';
                       return str;
                    }
                }
                </shiro:hasPermission>
                ]],
            onLoadSuccess: function (data) {
                <shiro:hasPermission name="trade:setting:walletaddr:operator">

                </shiro:hasPermission>
            }
        });
    });

    systemWalletDataGrid = $('#systemWalletDataGrid').datagrid({
        url: '${ctx}/fund/systemwallet/data',
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
            width: '260',
            title: '钱包ID',
            field: 'walletId',
            sortable: true
        }, {
            width: '150',
            title: '钱包名称',
            field: 'walletName',
            sortable: true
        }
                        ]],
        columns: [[{
            width: '130',
            title: '证券ID',
            field: 'stockinfoId',
            sortable: true
        }, {
            width: '100',
            title: '证券代码',
            field: 'stockCode',
            sortable: true
        }, {
            width: '100',
            title: '证券名称',
            field: 'stockName',
            sortable: true,
            hidden:true
        }, {
            width: '100',
            title: '钱包用途类型',
            field: 'walletUsageType',
            formatter: function (value, row, index) {
                return getDictValueByCode(value);
            }
        }, {
            width: '100',
            title: '备注',
            field: 'remark'
        }, {
            width: '150',
            title: '创建时间',
            field: 'createDate',
            formatter: function (value, row, index) {
                return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            }
        }<shiro:hasPermission name="trade:setting:walletaddr:operator">
            , {
                field: 'action',
                title: '操作',
                width: 140,
                formatter: function (value, row, index) {
                    var str = '';
                    str += $.formatString('<a href="javascript:void(0)" class="icoproduct-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editSystemWalletFun(\'{0}\');" >编辑</a>', row.id);
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="icoproduct-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteSystemWalletFun(\'{0}\');" >删除</a>', row.id);
                    return str;
                }
            }
            </shiro:hasPermission>
            ]],
        onLoadSuccess: function (data) {
            //用户未登录时刷新页面
            var codeNum = JSON.stringify(data.code);
            if(codeNum==2003){
                window.location.reload();
            }
            <shiro:hasPermission name="trade:setting:walletaddr:operator">
            $('.icoproduct-easyui-linkbutton-edit').linkbutton({text: '编辑'});
            $('.icoproduct-easyui-linkbutton-del').linkbutton({text: '删除'});
            </shiro:hasPermission>
              var rows=$('#systemWalletDataGrid').datagrid("getRows");
              if(rows.length>0){
                  $("#walletId").val(rows[0].walletId);
                  $("#stockinfoId").val(rows[0].stockinfoId);
                  var opts = systemWalletDataGrid2.datagrid("options");
                  opts.url = '${ctx}/fund/systemwallet/addr/data';
                  systemWalletDataGrid2.datagrid('reload',{stockinfoId:rows[0].stockinfoId,walletId:rows[0].walletId});
                  $('#systemWalletDataGrid').datagrid('selectRow', 0);
              }
        },
        onClickRow:function(rowIndex, rowData){
            $("#walletId").val(rowData.walletId);
            $("#stockinfoId").val(rowData.stockinfoId);
            systemWalletDataGrid2.datagrid('reload',{stockinfoId:rowData.stockinfoId,walletId:rowData.walletId});
         },
        toolbar: '#systemWalletbar'
    });
        
    function isHaveProduct(){
    	if($("#walletId").val() == null || $("#walletId").val() == ''){
    		$.messager.alert('提示','请选择系统钱包后,再对系统钱包地址进行编辑!');
    		return false;
    	}else{
    		return true;
    	}
    }    

    function addSystemWalletFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 300,
            href: '${ctx}/fund/systemwallet/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = systemWalletDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editSystemWalletForm');
                    f.submit();
                }
            }]
        });
    }

    function editSystemWalletFun(id) {
        if (id == undefined) {
            var rows = systemWalletDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            systemWalletDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 300,
            href: '${ctx}/fund/systemwallet/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = systemWalletDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editSystemWalletForm');
                    f.submit();
                }
            }]
        });
    }
    function deleteSystemWalletFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = systemWalletDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            systemWalletDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前系统钱包吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/fund/systemwallet/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        systemWalletDataGrid.datagrid('reload');
                    }else{
                        parent.$.messager.alert('错误', result.message, 'error');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }
</script>
<input type="hidden" id="stockinfoId" />
<input type="hidden" id="walletId" />
<div class="easyui-layout" data-options="fit:true,border:false" style="width:100%;height:100%;">
    <div data-options="region:'north',split:true" style="width: 580px;height:200px;">
        <table id="systemWalletDataGrid" data-options="fit:true,border:false,title:'系统钱包'"></table>
    </div>
    <div data-options="region:'center'"  style="padding: 5px; background: #eee;">
          <table id="systemWalletDataGrid2" data-options="fit:true,border:false,title:'系统钱包地址'"></table>
    </div>
</div>
<div id="systemWalletbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:walletaddr:operator">
    <a onclick="addSystemWalletFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>

