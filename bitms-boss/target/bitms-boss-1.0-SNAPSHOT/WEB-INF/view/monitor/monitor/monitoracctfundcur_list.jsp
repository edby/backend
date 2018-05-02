<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="detail.jsp" %>
<script type="text/javascript">
    var monitorAcctFundCurDataGrid;
    var bzj_timer_ac;
    $(function () {
        monitorAcctFundCurDataGrid = $('#monitorAcctFundCurDataGrid').datagrid({
            url: '${ctx}/monitor/monitoracctfundcur/data',
            queryParams: {monitorSubType: '${monitorSubType}'},
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
            frozenColumns: [[
                {
                    width: '180',
                    title: '账户名称',
                    field: 'accountName'
                },
                {
                    width: '70',
                    title: '资产类型',
                    field: 'acctAssetType',
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '钱包资产';
                        } else if (value == 2) {
                            return '杠杆资产';
                        } else if (value == 3) {
                            return '理财资产';
                        }
//                        } else if (value == 4) {
//                            return '理财资产';
//                        } else if (value == 5) {
//                            return '理财负债';
//                        }
                    }
                },
                {
                    width: '70',
                    title:
                        '证券名称',
                    field:
                        'stockName'
                }
                ,
                {
                    width: '100',
                    title:
                        '证券ID',
                    field:
                        'stockinfoId'
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
                    title:
                        '监控结果',
                    field:
                        'chkResult',
                    formatter:

                        function (value, row, index) {
                            if (value == 1) {
                                return '正常';
                            } else {
                                return '<font style="color:red;">异常</font>';
                            }
                        }
                }
                ,
                {
                    field: 'action',
                    width:
                        40,
                    title:
                        '操作',
                    formatter:
                        function (value, row, index) {
                            var str = '';
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-monitorParamEdit" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="assetCheckFun(\'{0}\',\'{1}\',\'{2}\',\'{3}\',\'{4}\',\'{5}\');" >对账</a>', row.accountId, row.stockinfoId, row.bizCategoryId, row.chkDate, row.relatedStockinfoId, row.acctAssetType);
                            return str;
                        }
                }
            ]],

            columns: [[
                {
                    width: '150',
                    title:
                        '描述',
                    field:
                        'monitordesc'
                }
                ,
                {
                    width: '130',
                    title:
                        '资产余额',
                    field:
                        'assetBal',
                    sortable:
                        true,
                    formatter:

                        function (value, row, index) {
                            return value;
                        }

                    ,
                }
                ,
                {
                    width: '130',
                    title:
                        '冻结资产余额',
                    field:
                        'assetfrozenbal',
                    sortable:
                        true
                }
                ,
                {
                    width: '130',
                    title:
                        '负债余额',
                    field:
                        'debetBal',
                    sortable:
                        true
                }
                ,
                {
                    width: '130',
                    title:
                        '流水总额',
                    field:
                        'curAssetBal',
                    sortable:
                        true
                }
                ,
                {
                    width: '130',
                    title:
                        '流水冻结余额',
                    field:
                        'curfrozenbal',
                    sortable:
                        true
                }
                ,
                {
                    width: '130',
                    title:
                        '流水负债总额',
                    field:
                        'curDebetBal',
                    sortable:
                        true
                }
            ]],
            onLoadSuccess: function (data) {
            }
        })
        ;

        //bzj_timer_ac = window.setInterval(searchMonitorAcctFundCurFun, 5 * 1000);
    });

    $(document).ready(function () {
        $("#bzjjk_timer_ac").combobox({
            onChange: function (n, o) {
                window.clearInterval(bzj_timer_ac);
                if ($("#bzjjk_timer_ac").combobox('getValue') != 0) {
                    bzj_timer_ac = null;
                    bzj_timer_ac = window.setInterval(searchMonitorAcctFundCurFun, $("#bzjjk_timer_ac").combobox('getValue') * 1000);
                }
            }

        });
    })

    function searchMonitorAcctFundCurFun() {
        try {
            monitorAcctFundCurDataGrid.datagrid('load',
                $.serializeObject($('#searchMonitorAcctFundCurForm')));
        }catch(err){
//            console.log('定时不在界面');
        }
    }

    function cleanMonitorAcctFundCurFun() {
        $('#searchMonitorAcctFundCurForm input').val('');
        monitorAcctFundCurDataGrid.datagrid('load', {});
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchMonitorAcctFundCurForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="monitor_acctName" class="easyui-textbox" name="accountName"
                               placeholder="请输入账户名" style="width:80px;"/>
                    </td>
                    <th>资产类型:</th>
                    <td>
                        <select id="acctAssetType" class="easyui-combobox" name="acctAssetType" style="width:80px;"
                                data-options="required:true">
                            <option value="">-请选择-</option>
                            <option value="1">钱包资产</option>
                            <option value="2">杠杆资产</option>
                            <%--<option value="3">杠杆负债</option>--%>
                            <option value="3">理财资产</option>
                            <%--<option value="5">理财负债</option>--%>
                        </select>
                    </td>
                    <th>监控结果:</th>
                    <td>
                        <select id="chkResult" class="easyui-combobox" name="chkResult" style="width:50px;"
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
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true"
                           onclick="searchMonitorAcctFundCurFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true"
                           onclick="cleanMonitorAcctFundCurFun();">清空</a>
                    </td>
                    <th>定时刷新时间:</th>
                    <td>
                        <select id="bzjjk_timer_ac" class="easyui-combobox" name="bzjjk_timer_ac" style="width:80px;">
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
        <table id="monitorAcctFundCurDataGrid" data-option="fit:true,border:false"></table>
    </div>
</div>
