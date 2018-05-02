<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var monitorPlatBalDataGrid;
    var bzj_timer_mp;
    $(function () {
        monitorPlatBalDataGrid = $('#monitorPlatBalDataGrid').datagrid({
            url: '${ctx}/monitor/platbal/data',
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
                    width: '70',
                    title: '证券名称',
                    field: 'stockName'
                },
                {
                    width: '100',
                    title: '证券ID',
                    field: 'stockinfoId'
                },
                {
                    width: '150',
                    title: '监控时间',
                    field: 'chkDate',
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                },
                {
                    width: '70',
                    title: '监控结果',
                    field: 'chkResult',
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '正常';
                        } else {
                            return '<font style="color:red;">异常</font>';
                        }
                    }
                }
            ]],
            columns: [[
                {
                    width: '150',
                    title: '内部资产余额',
                    field: 'internalBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '120',
                    title: '冷储备钱包余额',
                    field: 'externalEBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '热付款钱包余额',
                    field: 'hotPayBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '冷付款钱包余额',
                    field: 'coldPayBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '充值钱包余额',
                    field: 'externalRBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '180',
                    title: '内外部差额',
                    field: 'differenceBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '1500',
                    title: '描述',
                    field: 'monitorDesc'
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

        //bzj_timer_mp = window.setInterval(searchMonitorPlatBalFun, 5 * 1000);

    });

    $(document).ready(function () {
        $("#bzjjk_timer_mp").combobox({
            onChange: function (n, o) {
                window.clearInterval(bzj_timer_mp);
                if ($("#bzjjk_timer_mp").combobox('getValue') != 0) {
                    bzj_timer_mp = null;
                    bzj_timer_mp = window.setInterval(searchMonitorPlatBalFun, $("#bzjjk_timer_mp").combobox('getValue') * 1000);
                }
            }

        });
    })

    function searchMonitorPlatBalFun() {
        try {
            monitorPlatBalDataGrid.datagrid('load',
                $.serializeObject($('#searchMonitorPlatBalForm')));
        } catch (err) {
        }
    }

    function cleanMonitorPlatBalFun() {
        $('#searchMonitorPlatalForm input').val('');
        monitorPlatBalDataGrid.datagrid('load', {});
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchMonitorPlatBalForm">
            <table>
                <tr>
                    <th>证券名称:</th>
                    <td>
                        <input id="stockinfoId" name="stockinfoId" class="easyui-combobox" style="width: 100px;"
                               placeholder="请选择" value="" data-options="
									url: '${ctx}/monitor/findByCurType', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'">
                    </td>
                    <th>监控结果:</th>
                    <td>
                        <select id="chkResult" class="easyui-combobox" name="chkResult" style="width:80px;"
                                data-options="required:true">
                            <option value="">-请选择-</option>
                            <option value="1">正常</option>
                            <option value="0">异常</option>
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
                           onclick="searchMonitorPlatBalFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMonitorPlatBalFun();">清空</a>
                    </td>
                    <th>定时刷新时间:</th>
                    <td>
                        <select id="bzjjk_timer_mp" class="easyui-combobox" name="bzjjk_timer_mp" style="width:80px;">
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
        <table id="monitorPlatBalDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
