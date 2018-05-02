<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var monitorRunLogsDataGrid;
//    var bzj_timer_ml;
    $(function () {
        monitorRunLogsDataGrid = $('#monitorRunLogsDataGrid').datagrid({
            url: '${ctx}/monitor/runlogs/data',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            columns: [[
                {
                    width: '150',
                    title: '监控日期',
                    field: 'createDate'
//                    formatter: function (value, row, index) {
//                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
//                    }
                },
                {
                    width: '100',
                    title: '监控服务名称',
                    field: 'monitorCode'
                },
                {
                    width: '5000',
                    title: '描述',
                    field: 'logDesc'
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

//        bzj_timer_ml = window.setInterval(searchMonitorRunLogsFun, 5 * 1000);

    });

//    $(document).ready(function () {
//        $("#bzjjk_timer_ml").combobox({
//            onChange: function (n, o) {
//                window.clearInterval(bzj_timer_ml);
//                if ($("#bzjjk_timer_ml").combobox('getValue') != 0) {
//                    bzj_timer_ml = null;
//                    bzj_timer_ml = window.setInterval(searchMonitorRunLogsFun, $("#bzjjk_timer_ml").combobox('getValue') * 1000);
//                }
//            }
//
//        });
//    })

    function searchMonitorRunLogsFun() {
        try {
            monitorRunLogsDataGrid.datagrid('load',
                $.serializeObject($('#searchMonitorRunLogsForm')));
        }catch(err){}
    }

    function cleanMonitorRunLogsFun() {
        $('#searchMonitorRunLogsForm input').val('');
        monitorRunLogsDataGrid.datagrid('load', {});
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchMonitorRunLogsForm">
            <table>
                <tr>
                    <th>监控服务名称</th>
                    <td>
                        <select id="monitorCode" class="easyui-combobox" name="monitorCode" style="width:100px;">
                            <option  value="">--请选择--</option>
                            <option  value="MARGIN">保证金监控</option>
                            <option  value="INTERNALPLATFUNDCUR">内部总账监控</option>
                            <option value="ACCTFUNDCUR">账户资产监控</option>
                            <option value="MONITORDIGITALCOIN">数字资产内外部对账</option>
                            <option value="MONITORCASHCOIN">现金资产内外部对账</option>
                        </select>
                    </td>
                    <th>开始时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox" placeholder="请输入开始时间"/></td>
                    <th>结束时间:</th>
                    <td><input name="timeEnd" class="easyui-datetimebox" placeholder="请输入结束时间"/></td>
                    <td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true"
                           onclick="searchMonitorRunLogsFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMonitorRunLogsFun();">清空</a>
                    </td>
                    <%--<th>定时刷新时间:</th>--%>
                    <%--<td>--%>
                        <%--<select id="bzjjk_timer_ml" class="easyui-combobox" name="bzjjk_timer_ml" style="width:80px;">--%>
                            <%--<option  value="0">不刷新</option>--%>
                            <%--<option selected value="5">5秒</option>--%>
                            <%--<option value="10">10秒</option>--%>
                            <%--<option value="15">15秒</option>--%>
                            <%--<option value="30">30秒</option>--%>
                            <%--<option value="60">1分钟</option>--%>
                        <%--</select>--%>
                    <%--</td>--%>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="monitorRunLogsDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
