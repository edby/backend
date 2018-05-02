<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var marginDataGrid;
    var bzj_timer;
    $(function () {
        marginDataGrid = $('#marginDataGrid').datagrid({
            url: '${ctx}/monitor/margin/data',
            striped: true,
            method: 'post',
            rownumbers: true,
            pagination: true,
            singleSelect: false,
            idField: 'accountId',
            sortName: 'accountId',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns: [[{field: 'accountId', checkbox: true},
                {
                    width: '150',
                    title: '借款人账户名',
                    field: 'accountName',
                    sortable: true
                }, {
                    width: '80',
                    title: '账户属性',
                    field: 'acctattr',
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '多头';
                        } else if (value == -1) {
                            return '空头';
                        } else {
                            return '无';
                        }
                    }

                },{
                    width: '100',
                    title: '风险率',
                    field: 'riskRate'
                }
            ]],
            columns: [[
                {
                    width: '100',
                    title: '最新行情',
                    field: 'platPrice',
                    sortable: true
                }, {
                    width: '100',
                    title: '强平价',
                    field: 'explosionPrice',
                    sortable: true
                }, {
                    width: '100',
                    title: '临界爆仓比例',
                    field: 'criticalCPPercent',
                    sortable: true
                }, {
                    width: '100',
                    title: '计价证券ID',
                    field: 'capitalStockinfoId',
                    sortable: true
                }, {
                    width: '150',
                    title: '计价货币余额',
                    field: 'capitalBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '计价货币借款',
                    field: 'capitalDebtBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '100',
                    title: '法定货币证券ID',
                    field: 'targetStockinfoId'
                }, {
                    width: '150',
                    title: '法定货币余额',
                    field: 'targetBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '法定货币借款',
                    field: 'targetDebtBal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '更新时间',
                    field: 'chkDate',
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                }, {
                    width: '150',
                    title: '描述',
                    field: 'monitordesc',
                    sortable: true
                }<shiro:hasPermission name="monitor:setting:debitMargin:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        if (row.acctattr == 1)//多头
                        {
                            if (row.riskRate <= 105 && row.riskRate > 0) {
                                if (row.accountId !=<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID %>
                                    && row.accountId !=<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID %>
                                    && row.accountId !=<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID %>) {
                                    str += $.formatString('<a href="javascript:void(0)" class="pingcang-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="doPingCang(\'{0}\',\'{1}\',\'{2}\');" >强制平仓</a>', row.accountId, row.targetStockinfoId, row.capitalStockinfoId);
                                }
                            }
                        }
                        if (row.acctattr == -1)//空头
                        {
                            if (row.riskRate > 95) {
                                if (row.accountId !=<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID %> && row.accountId !=<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID %>) {
                                    str += $.formatString('<a href="javascript:void(0)" class="pingcang-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="doPingCang(\'{0}\',\'{1}\',\'{2}\');" >强制平仓</a>', row.accountId, row.targetStockinfoId, row.capitalStockinfoId);
                                }
                            }

                        }
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
                <shiro:hasPermission name="monitor:setting:debitMargin:operator">
                $('.pingcang-easyui-linkbutton-edit').linkbutton({text: '强制平仓'});
                </shiro:hasPermission>
            },
            rowStyler: function (index, row) {
                if (row.accountId ==<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID %>
                    || row.accountId ==<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID %>
                    || row.accountId ==<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID %>) {
                    return 'background-color:#AEEEEE;color:blue;font-weight:bold;';
                }
                if (row.acctattr == 1)//多头
                {
                    if (row.riskRate <= 105 && row.riskRate > 0) {
                        return 'background-color:#FF4500;color:blue;font-weight:bold;';
                    }
                    else if (row.riskRate > 105 && row.riskRate <= 110) {
                        return 'background-color:#EE9A00;color:blue;font-weight:bold;';
                    } else {
                        return 'background-color:#AEEEEE;color:blue;font-weight:bold;';
                    }
                }
                if (row.acctattr == -1)//空头
                {
                    if (row.riskRate > 95) {
                        return 'background-color:#FF4500;color:blue;font-weight:bold;';
                    }
                    else if (row.riskRate <= 95 && row.riskRate > 90) {
                        return 'background-color:#EE9A00;color:blue;font-weight:bold;';
                    }
                    else {
                        return 'background-color:#AEEEEE;color:blue;font-weight:bold;';
                    }
                }
            },
            toolbar: '#pingCangToolbar'

        });

        //bzj_timer = window.setInterval(searchdebitRecordFun, 5 * 1000);

    });

    $(document).ready(function () {
        $("#bzjjk_timer").combobox({
            onChange: function (n, o) {
                window.clearInterval(bzj_timer);
                if ($("#bzjjk_timer").combobox('getValue') != 0) {
                    bzj_timer = null;
                    bzj_timer = window.setInterval(searchdebitRecordFun, $("#bzjjk_timer").combobox('getValue') * 1000);
                }
            }

        });
    })


    function searchdebitRecordFun() {
        try {
            marginDataGrid.datagrid('load', $.serializeObject($('#searchdebitRecordForm')));
        } catch (err) {
        }
    }

    function cleandebitRecordFun() {
        $('#searchdebitRecordForm input').val('');
        marginDataGrid.datagrid('load', {});
    }

    function doPingCang(accountId, targetStockinfoIds, capitalStockinfoIds) {
        parent.$.modalDialog({
            title: '强制平仓确认',
            width: 900,
            height: 400,
            href: '${ctx}/monitor/margin/approval?ids=' + accountId + "&targetStockinfoIds=" + targetStockinfoIds + "&capitalStockinfoIds=" + capitalStockinfoIds,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = marginDataGrid;
                    var f = parent.$.modalDialog.handler.find('#marginForm');
                    f.submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = marginDataGrid;
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                }
            }]
        });
    }

    function doPiLiangPingCang() {
        var checkedItems = $('#marginDataGrid').datagrid('getSelections');
        var ids = [];
        var targetStockinfoIds = [];
        var capitalStockinfoIds = [];
        var isOk = true;
        $.each(checkedItems, function (index, item) {
            if (item.acctattr == 1)//多头
            {
                if (item.riskRate > 105) {
                    isOk = false;
                    parent.$.messager.alert('错误', '账户：' + (item.accountName) + '的记录不是爆仓数据或已处理，请选择已爆仓的数据进行平仓!', 'error');
                } else {
                    if (item.accountId ==<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID %>
                        || item.accountId ==<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID %>
                        || item.accountId ==<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID %>) {
                        isOk = false;
                        parent.$.messager.alert('错误', '账户：' + (item.accountName) + '的记录是超级用户的记录，不能对超级用户进行平仓!', 'error');
                    } else {
                        ids.push(item.accountId);
                        targetStockinfoIds.push(item.targetStockinfoId);
                        capitalStockinfoIds.push(item.capitalStockinfoId)
                    }

                }
            }
            if (item.acctattr == -1)//空头
            {
                if (item.riskRate < 95) {
                    isOk = false;
                    isOk = false;
                    parent.$.messager.alert('错误', '账户：' + (item.accountName) + '的记录不是爆仓数据或已处理，请选择已爆仓的数据进行平仓!', 'error');
                } else {
                    if (item.accountId ==<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID %>
                        || item.accountId ==<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID %>
                        || item.accountId ==<%=FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID %>) {
                        isOk = false;
                        parent.$.messager.alert('错误', '账户：' + (item.accountName) + '的记录是超级用户的记录，不能对超级用户进行平仓!', 'error');
                    } else {
                        ids.push(item.accountId);
                        targetStockinfoIds.push(item.targetStockinfoId);
                        capitalStockinfoIds.push(item.capitalStockinfoId)
                    }

                }

            }

        });
        if (!isOk) {
            ids = [];
            targetStockinfoIds = [];
            capitalStockinfoIds = [];
            return false;
        }
        if (ids.length == 0) {
            parent.$.messager.alert('错误', '请选择要平仓的记录!', 'error');
            return false;
        }
        doPingCang(ids, targetStockinfoIds, capitalStockinfoIds);
    }

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchdebitRecordForm">
            <table>
                <tr>
                    <th>借款人账户名:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入借款人账户名"/></td>
                    <th>证券代码:</th>
                    <td>
                        <input id="check_stockCode_margin" name="capitalStockinfoId" class="easyui-combobox"
                               name="language" style="width: 142px;"
                               placeholder="请选择证券" value="" data-options="
									url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
									textField:'stockCode', groupField:'group'">
                    </td>
                    <th>交易对:</th>
                    <td>
                        <input id="check_stockCode2_margin" name="targetStockinfoId" class="easyui-combobox"
                               name="language" style="width: 142px;"
                               placeholder="请选择交易对" data-options="
									url: '${ctx}/stock/info/allInExchange', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'" value="">
                    </td>
                    <th>账户属性:</th>
                    <td>
                        <select class="easyui-combobox" name="acctattr" style="width:142px;">
                            <option value="">-请选择-</option>
                            <option value="1">多头</option>
                            <option value="-1">空头</option>
                        </select>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true"
                           onclick="searchdebitRecordFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleandebitRecordFun();">清空</a>
                    </td>
                    <th>定时刷新时间:</th>
                    <td>
                        <select id="bzjjk_timer" class="easyui-combobox" name="bzjjk_timer" style="width:80px;">
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
        <table id="marginDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="pingCangToolbar" style="display: none;">
    <shiro:hasPermission name="monitor:setting:debitMargin:operator">
        <a onclick="doPiLiangPingCang();" href="javascript:void(0);" class="easyui-linkbutton"
           data-options="plain:true,iconCls:'fi-pencil icon-blue'">批量强制平仓</a>
    </shiro:hasPermission>
</div>
