<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var accountMsgListGrid;
    $(function () {
        accountMsgListGrid = $('#accountMsgListGrid').datagrid({
            url: '${ctx}/account/msgList/data',
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
                },{
                    width: '120',
                    title: '语言类型',
                    field: 'langType',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                },{
                    width: '120',
                    title: '标题',
                    field: 'title',
                    sortable: true
                },{
                    width: '200',
                    title: '内容',
                    field: 'content',
                    sortable: true
                }
            ]],
            columns: [[{
                width: '100',
                title: '创建人',
                field: 'createBy'
            }, {
                width: '150',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            },{
                width: '100',
                title: '修改人',
                field: 'updateBy',
                sortable: true
            },{
                width: '150',
                title: '修改时间',
                field: 'updateDate',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            },{
                width: '100',
                title: '帐户ID',
                field: 'accountId',
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
    function searchAccountMsgAllFun() {
        accountMsgListGrid.datagrid('load',
            $.serializeObject($('#earchAccountMsgAllForm')));
    }
    function cleanAccountMsgAllFun() {
        $('#earchAccountMsgAllForm input').val('');
        accountMsgListGrid.datagrid('load',{});
    }
    $(function(){
        $("#langTypeMsgId").html(dictDropDownOptions('langTypeMsg','langType', 'langType', 'width:474px,'));
        $("#langTypeMsg").combobox({
            valueField:'code',
            textField:'name'
        });
    })
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="earchAccountMsgAllForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <th>语言类型</th>
                    <td id="langTypeMsgId">
                    </td>
                    <td><input  name="accountName" class="easyui-textbox" placeholder="请输入账户名"/></td>
                    <th>账户消息查询时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox"  placeholder="请输入账户消息查询开始时间"/></td>
                    <th>至</th>
                    <td><input name="timeEnd" class="easyui-datetimebox"   placeholder="请输入账户消息查询结束时间"/></td>
                    <td>
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountMsgAllFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountMsgAllFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="accountMsgListGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
