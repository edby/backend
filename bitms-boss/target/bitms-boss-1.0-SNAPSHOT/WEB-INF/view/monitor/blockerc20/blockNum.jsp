<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var monitorBlockNumDataGrid;
//    var bzj_timer_mp;
    $(function () {
        monitorBlockNumDataGrid = $('#monitorBlockNumDataGrid').datagrid({
            url: '${ctx}/monitor/blockNum/data',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'chkDate',
            sortName: 'chkDate',
            sortOrder: 'desc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            columns: [[
                {
                    width: '120',
                    title: '监控时间',
                    field: 'chkDate',
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                },
                {
                    width: '100',
                    title: '内部区块高度',
                    field: 'inBlockNum'
                },
                {
                    width: '100',
                    title: '外部区块高度',
                    field: 'outBlockNum'
                },
                {
                    width: '80',
                    title: '高度差',
                    field: 'differenceNum'
                },
                {
                    width: '80',
                    title: '监控结果',
                    field: 'chkResult',
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '正常';
                        } else {
                            return '<font style="color:red;">异常</font>';
                        }
                    }
                },
                {
                    width: '100',
                    title: '区块来源',
                    field: 'blockResource'
                },
                {
                    width: '500',
                    title: '描述',
                    field: 'monitordesc'
                }
            ]],
            onLoadSuccess: function (data) {

            }
        });

        //bzj_timer_mp = window.setInterval(searchMonitorPlatBalFun, 5 * 1000);

    });

//    $(document).ready(function () {
//        $("#bzjjk_timer_mp").combobox({
//            onChange: function (n, o) {
//                window.clearInterval(bzj_timer_mp);
//                if ($("#bzjjk_timer_mp").combobox('getValue') != 0) {
//                    bzj_timer_mp = null;
//                    bzj_timer_mp = window.setInterval(searchMonitorPlatBalFun, $("#bzjjk_timer_mp").combobox('getValue') * 1000);
//                }
//            }
//
//        });
//    })

    function searchMonitorBlockNumFun() {
        try {
            monitorBlockNumDataGrid.datagrid('load',
                $.serializeObject($('#searchMonitorBlockNumForm')));
        } catch (err) {
        }
    }

    function cleanMonitorBlockNumFun() {
        $('#searchMonitorBlockNumForm input').val('');
        monitorBlockNumDataGrid.datagrid('load', {});
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchMonitorBlockNumForm">
            <table>
                <tr>
                    <th>监控结果:</th>
                    <td>
                        <select id="chkResult" class="easyui-combobox" name="chkResult" style="width:80px;"
                                data-options="required:true">
                            <option value="">-请选择-</option>
                            <option value="1">正常</option>
                            <option value="-1">异常</option>
                        </select>
                    </td>
                    <%--<th>开始时间:</th>--%>
                    <%--<td><input name="timeStart" class="easyui-datetimebox" placeholder="请输入开始时间"/></td>--%>
                    <%--<th>结束时间:</th>--%>
                    <%--<td><input name="timeEnd" class="easyui-datetimebox" placeholder="请输入结束时间"/></td>--%>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true"
                           onclick="searchMonitorBlockNumFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMonitorBlockNumFun();">清空</a>
                    </td>
                    <%--<th>定时刷新时间:</th>--%>
                    <%--<td>--%>
                        <%--<select id="bzjjk_timer_mp" class="easyui-combobox" name="bzjjk_timer_mp" style="width:80px;">--%>
                            <%--<option selected value="0">不刷新</option>--%>
                            <%--<option value="5">5秒</option>--%>
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
        <table id="monitorBlockNumDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
