<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var msgRecordLogDataGrid;
    $(function () {
        msgRecordLogDataGrid = $('#msgRecordLogDataGrid').datagrid({
            url: '${ctx}/common/msgRecord/data',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'accountName',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                {
                    width: '55',
                    title: '类型',
                    field: 'type',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                },{
                    width: '140',
                    title: '发送对象',
                    field: 'object',
                    sortable: true
                },{
                    width: '55',
                    title: '状态',
                    field: 'status',
                    formatter: function (value, row, index) {
                       if(value == 0 || value==true)
                       {
                           return '成功';
                       }
                       else{
                           return '失败';
                       }
                    }
                }
                    	]],
            columns: [[ {
                width: '1200',
                title: '内容',
                field: 'content',
                sortable: true
            }, {
                width: '150',
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
            }
        });
    });


    function searchMsgRecordLogFun() {
    	msgRecordLogDataGrid.datagrid('load', $.serializeObject($('#searchMsgRecordLogForm')));
    }
    function cleanMsgRecordLogFun() {
        $('#searchMsgRecordLogForm input').val('');
        msgRecordLogDataGrid.datagrid('load', {});
    }
    $(function(){
        $("#LogTypeTd").html(dictDropDownOptionsList('typeLog','type', 'msgTemplateType','', '',  'width:142px,'));
        $("#typeLog").combobox({
            valueField:'code',
            textField:'name'
        });
    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchMsgRecordLogForm">
            <table>
                <tr>
                    <th>模板类型:</th>
                    <td id="LogTypeTd"></td>
                    <th>发送对象:</th>
                    <td><input name="object" class="easyui-textbox" placeholder="发送对象"/></td>
                    <th>发送状态:</th>
                    <td>
                        <select id="statusString" class="easyui-combobox" name="statusString" style="width:100px;"  data-options="required:true">
                            <option value="">-请选择-</option>
                            <option value="0">成功</option>
                            <option value="1">失败</option>
                        </select>
                    </td>
                    <th>开始时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox"  placeholder="请输入开始时间"/></td>
                    <th>结束时间:</th>
                    <td><input name="timeEnd" class="easyui-datetimebox"   placeholder="请输入结束时间"/></td>
                    <td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchMsgRecordLogFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMsgRecordLogFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="msgRecordLogDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
