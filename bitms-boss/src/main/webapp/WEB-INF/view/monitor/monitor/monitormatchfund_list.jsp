<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="detail.jsp" %>
<script type="text/javascript">
    var monitorMatchFundDataGrid;
    var bzj_timer_mf;
    $(function () {
        monitorMatchFundDataGrid = $('#monitorMatchFundDataGrid').datagrid({
            url: '${ctx}/monitor/monitormatchfund/data',
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
                },
                {
                    field: 'action',
                    width: 90,
                    title: '操作',
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString(
                            '<a href="javascript:void(0)" id="status_1" ' +
                            'class="user-easyui-linkbutton-detail" ' +
                            'data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" ' +
                            'onclick="findAssetFun(\'{0}\');" >查看账户明细</a>', row.stockinfoId);
                        return str;
                    }

                },
            ]],
            columns: [[
                {
                    width: '150',
                    title: '账户资产余额',
                    field: 'assetBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '冻结资产余额',
                    field: 'assetFrozenBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '负债余额',
                    field: 'debetBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '流水发生总额',
                    field: 'curAssetBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '流水冻结总额',
                    field: 'curFrozenBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '流水负债总额',
                    field: 'curDebetBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '数字货币流入总额',
                    field: 'inBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '数字货币流出总额',
                    field: 'outBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '普通用户负债总额',
                    field: 'borrowDebetBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '费用总额',
                    field: 'feeBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '强平负债转入余额',
                    field: 'closePositionDebetBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '强平资产转入余额',
                    field: 'closePositionAssetBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },
                {
                    width: '150',
                    title: '残渣退款余额',
                    field: 'closePositionReturnBal',
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

        //bzj_timer_mf = window.setInterval(searchMonitorMatchFundFun, 5 * 1000);
    });

    $(document).ready(function () {
        $("#bzjjk_timer_mf").combobox({
            onChange: function (n, o) {
                window.clearInterval(bzj_timer_mf);
                if ($("#bzjjk_timer_mf").combobox('getValue') != 0) {
                    bzj_timer_mf = null;
                    bzj_timer_mf = window.setInterval(searchMonitorMatchFundFun, $("#bzjjk_timer_mf").combobox('getValue') * 1000);
                }
            }
        });
    })

    function searchMonitorMatchFundFun() {
        try {
            monitorMatchFundDataGrid.datagrid('load',
                $.serializeObject($('#searchMonitorMatchFundForm')));
        }catch(err){

        }
    }

    function cleanMonitorMatchFundFun() {
        $('#searchMonitorMatchFundForm input').val('');
        monitorMatchFundDataGrid.datagrid('load', {});
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchMonitorMatchFundForm">
            <table>
                <tr>
                    <th>证券名称:</th>
                    <td>
                        <input id="monitor_matchfund_bizCategoryId" name="stockinfoId" class="easyui-combobox"
                               style="width: 100px;"
                               placeholder="请选择" value="" data-options="
									url: '${ctx}/monitor/findByCurType', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'">
                    </td>
                    <th>监控结果:</th>
                    <td>
                        <select id="chkResult" class="easyui-combobox" name="chkResult" style="width:100px;"
                                data-options="required:true">
                            <option value="">-请选择-</option>
                            <option value="1">正常</option>
                            <option value="-1">异常</option>
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
                           onclick="searchMonitorMatchFundFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMonitorMatchFundFun();">清空</a>
                    </td>
                    <th>定时刷新时间:</th>
                    <td>
                        <select id="bzjjk_timer_mf" class="easyui-combobox" name="bzjjk_timer_mf" style="width:80px;">
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
        <table id="monitorMatchFundDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
