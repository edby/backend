<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="../monitor/detail.jsp" %>
<script type="text/javascript" src="${ctx}/static/js/monitor.js" charset="utf-8"></script>

<script type="text/javascript">

    var monitorLogDataGrid;
    var bzj_timer_log;
    $(function () {
        monitorLogDataGrid = $('#monitorLogDataGrid').datagrid({
            url: '${ctx}/monitor/log/data',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'monitorDate',
            sortName: 'monitorDate',
            sortOrder: 'desc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns: [[
                {
                    width: '150',
                    title: '监控时间',
                    field: 'monitorDate',
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value * 1, "yyyy-MM-dd hh:mm:ss");
                    }
                },
                {
                    width: '140',
                    title: '监控类型',
                    field: 'monitorType',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return getMonitorTypeName(value);

                    }
                }
            ]],
            columns: [[
                {
                    width: '10000',
                    title: '描述',
                    field: 'monitorLogDesc'
                }
            ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
                $('.user-easyui-linkbutton-detail').linkbutton({text: '查看明细'});
            }
        });
//        bzj_timer_log = window.setInterval(searchMonitorLogFun, 5 * 1000);
    });

    $(document).ready(function () {
        $("#bzjjk_timer_log").combobox({
            onChange: function (n, o) {
                window.clearInterval(bzj_timer_log);
                if ($("#bzjjk_timer_log").combobox('getValue') != 0) {
                    bzj_timer_log = null;
                    bzj_timer_log = window.setInterval(searchMonitorLogFun, $("#bzjjk_timer_log").combobox('getValue') * 1000);
                }
            }

        });
    })

    function searchMonitorLogFun() {
        try {
            monitorLogDataGrid.datagrid('load',
                $.serializeObject($('#searchMonitorLogForm')));
        } catch (err) {

        }
    }

    function cleanMonitorLogFun() {
        $('#searchMonitorLogForm input').val('');
        monitorLogDataGrid.datagrid('load', {});
    }

$("#monitorTypeTd").html(printOptions(monitorType,'monitorType', 'monitorType', '', 'required:true,', ''));
$("#monitorType").combobox({
    valueField: 'code',
    textField: 'name'
});

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchMonitorLogForm">
            <table>
                <tr>
                    <th>监控类型:</th>
                    <td id="monitorTypeTd">
                    </td>
                    <th>开始时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox" placeholder="请输入开始时间"/></td>
                    <th>结束时间:</th>
                    <td><input name="timeEnd" class="easyui-datetimebox" placeholder="请输入结束时间"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true"
                           onclick="searchMonitorLogFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMonitorLogFun();">清空</a>
                    </td>
                    <th>定时刷新时间:</th>
                    <td>
                        <select id="bzjjk_timer_log" class="easyui-combobox" name="bzjjk_timer_log" style="width:80px;">
                            <option selected value="0">不刷新</option>
                            <option value="5">5秒</option>
                            <option value="10">10秒</option>
                            <option value="15">15秒</option>
                            <option value="30">30秒</option>
                            <option value="60">1分钟</option>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="monitorLogDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
