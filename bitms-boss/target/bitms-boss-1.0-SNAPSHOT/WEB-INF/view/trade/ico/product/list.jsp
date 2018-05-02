<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var icoPruductDataGrid;
    var icoPruductDataGrid2;
    $(function () {
    	icoPruductDataGrid2 = $('#icoPruductDataGrid2').datagrid({
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
			    title: 'ICO阶段',
			    field: 'period',
			    sortable: true
			} 		
                            ]],
            columns: [[{
                width: '100',
                title: '募集数量(个)',
                field: 'raiseAmount',
                sortable: true
            }, {
                width: '120',
                title: '预购最低价格(个BTC)',
                field: 'preMinPrice',
                sortable: true
            }, {
                width: '120',
                title: '预购最高价格(个BTC)',
                field: 'preMaxPrice',
                sortable: true
            }, {
                width: '120',
                title: '正式发行价格(个BTC)',
                field: 'issuePrice',
                sortable: true
            }, {
                width: '120',
                title: '正式发行价格说明',
                field: 'issuePriceRemark',
                sortable: true
            }, {
                width: '80',
                title: '锁定天数',
                field: 'lockDays',
                sortable: true
            }, {
                width: '100',
                title: '认购数量起点',
                field: 'subAmtPoint',
                sortable: true
            }, {
                width: '150',
                title: '预购开始时间',
                field: 'preStartDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '150',
                title: '预购结束时间',
                field: 'preEndDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '150',
                title: '正式开始时间',
                field: 'officialStartDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '150',
                title: '正式结束时间',
                field: 'officialEndDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '80',
                title: '备注',
                field: 'remark',
                sortable: true
            }<shiro:hasPermission name="trade:setting:icoproduct:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 130,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editIcoProductDetailFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteIcoProductDetailFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission name="trade:setting:icoproduct:operator">
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#icoProductbar2'
        });
    });
    	
    	
   
        icoPruductDataGrid = $('#icoPruductDataGrid').datagrid({
        	url: '${ctx}/ico/product/data',
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
                			},{
                                width: '200',
                                title: '数字货币名称',
                                field: 'digitalCurrencyName',
                                sortable: true
                            }	
                                            ]],
            columns: [[  {
                width: '100',
                title: '募集数量(个)',
                field: 'raiseAmount',
                sortable: true
            }, {
                width: '100',
                title: '发行价格(个BTC)',
                field: 'issuePrice'
            }, {
                width: '150',
                title: '开始时间',
                field: 'startDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '150',
                title: '结束时间',
                field: 'endDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }, {
                width: '70',
                title: '启用状态',
                field: 'activeStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '200',
                title: '备注',
                field: 'remark'
            }<shiro:hasPermission name="trade:setting:icoproduct:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="icoproduct-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editIcoProductFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="icoproduct-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteIcoproductFun(\'{0}\');" >删除</a>', row.id);
                        return str;
                    }
                }
                </shiro:hasPermission>
                ]],
            onLoadSuccess: function (data) {
                <shiro:hasPermission name="trade:setting:icoproduct:operator">
                $('.icoproduct-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.icoproduct-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
              var rows=$('#icoPruductDataGrid').datagrid("getRows");
              if(rows.length>0){
	              $("#productId").val(rows[0].id);
	              var opts = icoPruductDataGrid2.datagrid("options");
	              opts.url = '${ctx}/ico/product/detail/data';
	              icoPruductDataGrid2.datagrid('reload',{productId:rows[0].id});
	              $('#icoPruductDataGrid').datagrid('selectRow', 0);
              }
            },
            onClickRow:function(rowIndex, rowData){
            	$("#productId").val(rowData.id);
                icoPruductDataGrid2.datagrid('reload',{productId:rowData.id});
             },
            toolbar: '#icoProductbar'
        });
        
    function isHaveProduct(){
    	if($("#productId").val() == null || $("#productId").val() == ''){
    		$.messager.alert('提示','请选择产品后,再对产品明细进行编辑!');
    		return false;
    	}else{
    		return true;
    	}
    }    

    function addIcoProductFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 330,
            href: '${ctx}/ico/product/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = icoPruductDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editIcoProductForm');
                    f.submit();
                }
            }]
        });
    }

    function editIcoProductFun(id) {
        if (id == undefined) {
            var rows = icoPruductDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            icoPruductDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 330,
            href: '${ctx}/ico/product/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = icoPruductDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editIcoProductForm');
                    f.submit();
                }
            }]
        });
    }

    function deleteIcoproductFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = icoPruductDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            icoPruductDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前产品吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/ico/product/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        icoPruductDataGrid.datagrid('reload');
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

    
    function addIcoProductDetailFun() {
    	if( !isHaveProduct() ){
    		return ;
    	}
        parent.$.modalDialog({
            title: '添加',
            width: 600,
            height: 320,
            href: '${ctx}/ico/product/detail/modify?productId='+$("#productId").val(),
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = icoPruductDataGrid2;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editIcoProductDetailForm');
                    f.submit();
                }
            }]
        });
    }

    function editIcoProductDetailFun(id) {
    	if( !isHaveProduct() ){
    		return ;
    	}
        if (id == undefined) {
            var rows = icoPruductDataGrid2.datagrid('getSelections');
            id = rows[0].id;
        } else {
            icoPruductDataGrid2.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 600,
            height: 320,
            href: '${ctx}/ico/product/detail/modify?id=' + id+'&productId='+$("#productId").val(),
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = icoPruductDataGrid2;
                    var f = parent.$.modalDialog.handler.find('#editIcoProductDetailForm');
                    f.submit();
                }
            }]
        });
    }

    function deleteIcoProductDetailFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = icoPruductDataGrid2.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            icoPruductDataGrid2.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前产品明细吗', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/ico/product/detail/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        icoPruductDataGrid2.datagrid('reload');
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
<input type="hidden" id="productId" />
<div class="easyui-layout" data-options="fit:true,border:false" style="width:100%;height:100%;">
    <div data-options="region:'north',split:true" style="width: 580px;height:200px;">
        <table id="icoPruductDataGrid" data-options="fit:true,border:false,title:'产品管理'"></table>
    </div>
    <div data-options="region:'center'"  style="padding: 5px; background: #eee;">
          <table id="icoPruductDataGrid2" data-options="fit:true,border:false,title:'产品明细管理'"></table>
    </div>
</div>
<div id="icoProductbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:icoproduct:operator">
    <a onclick="addIcoProductFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>
<div id="icoProductbar2" style="display: none;">
    <shiro:hasPermission name="trade:setting:icoproduct:operator">
    <a onclick="addIcoProductDetailFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>
<jsp:include page="/commons/setup_ajax.jsp"></jsp:include>	