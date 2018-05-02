<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var archiveRunLogsDataGrid;
    $(function () {
        archiveRunLogsDataGrid = $('#archiveRunLogsDataGrid').datagrid({
            url: '${ctx}/monitor/archiveRunLogs/data',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            columns: [[
                {
                    width: '120',
                    title: '归档时间',
                    field: 'recTime',
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                },
                {

                    width: '150',
                    title: '存储过程名称',
                    field: 'procCode'
                },
                {
                    width: '1500',
                    title: '归档日志描述',
                    field: 'logDesc'
                }

            ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if (codeNum == 2003) {
                    window.location.reload();
                }
            }
        });

    });

    function searchArchiveRunLogsFun() {
        try {
            archiveRunLogsDataGrid.datagrid('load',
                $.serializeObject($('#searchArchiveRunLogsForm')));
        } catch (err) {
        }
    }

    function cleanArchiveRunLogsFun() {
        $('#searchArchiveRunLogsForm input').val('');
        archiveRunLogsDataGrid.datagrid('load', {});
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchArchiveRunLogsForm">
            <table>
                <tr>
                    <th>开始时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox" placeholder="请输入开始时间"/></td>
                    <th>结束时间:</th>
                    <td><input name="timeEnd" class="easyui-datetimebox" placeholder="请输入结束时间"/></td>
                    <td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true"
                           onclick="searchArchiveRunLogsFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanArchiveRunLogsFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="archiveRunLogsDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
