<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var erctokenWithdrawAppGrid;
    $(function () {
        erctokenWithdrawAppGrid = $('#erctokenWithdrawAppGrid').datagrid({
            url: '${ctx}/fund/erctokenwithdraw/approvalData',
            queryParams: {
                accountAssetType: account_fund_current_accountAssetType,
                businessFlag: account_fund_current_businessFlag,
                approveStatus:'<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING%>'
            },
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            columns: [[
                {
                    width: '130',
                    title: 'ID',
                    field: 'id',
                    sortable: true
                }, {
                    width: '140',
                    title: '账户名',
                    field: 'accountName',
                    sortable: true
                }, {
                    width: '130',
                    title: '证券ID',
                    field: 'stockinfoId',
                    hidden: true,
                    sortable: true
                }, {
                    width: '40',
                    title: '证券代码',
                    field: 'stockCode',
                    sortable: true
                }, {
                    width: '100',
                    title: '证券名称',
                    field: 'stockName',
                    sortable: true,
                    hidden: true

                }, {
                    width: '120',
                    title: '提币申请时间',
                    field: 'currentDate',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                }, {
                    width: '70',
                    title: '业务类别',
                    field: 'businessFlag',
                    hidden: true,
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }, {
                    width: '150',
                    title: '原资产数量余额',
                    field: 'orgAmt',
                    hidden: true,
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '80',
                    title: '资产发生方向',
                    field: 'occurDirect',
                    hidden: true,
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }, {
                    width: '140',
                    title: '提现数量',
                    field: 'occurAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return (value - row.fee).toFixed(12);
                    }
                }, {
                    width: '100',
                    title: '手续费',
                    field: 'fee',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '100',
                    title: '网络手续费',
                    field: 'netFee',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '最新资产数量余额',
                    field: 'lastAmt',
                    hidden: true,
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '170',
                    title: '提币目标银行名称',
                    field: 'withdrawBankName',
                    hidden: true,
                    sortable: true
                }, {
                    width: '150',
                    title: '提币地址',
                    field: 'withdrawAddr',
                    sortable: true
                }, {
                    width: '280',
                    title: '交易ID',
                    field: 'transId'
                }, {
                    width: '160',
                    title: '备注',
                    field: 'remark',
                    hidden:true
                },{
                    width: '70',
                    title: '审批状态',
                    field: 'approveStatus',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }, {
                    width: '70',
                    title: '汇款状态',
                    field: 'transferStatus',
                    formatter: function (value, row, index) {
                        var color = "";
                        var values= getDictValueByCode(value);
                        if (value == 'transfer') {
                            color = 'green';
                            values='成功汇出';
                        }
                        else if (value == 'transferPending') {
                            color = 'orange';
                            values='等待汇出';
                        }
                        else if (value == 'transferRejected') {
                            color = 'red';
                            values='汇出失败';
                        }
                        return "<font color='" + color + "'>" + values + "</font>";
                    }
                },{
                    width: '70',
                    title: '确认状态',
                    field: 'confirmStatus',
                    formatter: function (value, row, index) {
                        var color = "";
                        var values= getDictValueByCode(value);
                        if (value == 'confirm') {
                            color = 'green';
                            values='已确认';
                        }
                        else if (value == 'unconfirm') {
                            color = 'red';
                            values='未确认';
                        }
                        return "<font color='" + color + "'>" + values + "</font>";
                    }
                },{
                    width: '120',
                    title: '审核时间',
                    field: 'auditDate',
                    formatter: function (value, row, index) {
                        if(null != value){
                            return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                        }
                    }
                }<shiro:hasPermission name="trade:setting:erctokenwithdrawapprove:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 70,
                    formatter: function (value, row, index) {
                        var str = '';
                        if (row.approveStatus == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING %>') {
                            str += $.formatString('<a href="javascript:void(0)"  class="accountfundcurrent-easyui-linkbutton-dongjie1" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editerctokenWithdrawAppFun(\'{0}\',\'<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH %>\',\'<%=FundConsts.WALLET_BTC2USD_TYPE%>\');" >审批</a>', row.id);
                        }
                        <%-- lse {
                            str += $.formatString('<a href="javascript:void(0)" class="accountfundcurrent-easyui-linkbutton-print" data-options="plain:true,iconCls:\'fi-page-export icon-red\'" onclick="window.open(\'${ctx}/fund/erctokenwithdraw/approvalPrint?id={0}&exchangePairMoney={1}\')" target="_blank">打印</a>', row.id, '<%=FundConsts.WALLET_BTC2USD_TYPE%>');
                        }--%>
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
                <shiro:hasPermission name="trade:setting:erctokenwithdrawapprove:operator">
                $('.accountfundcurrent-easyui-linkbutton-dongjie1').linkbutton({text: '审核'});
                $('.accountfundcurrent-easyui-linkbutton-print').linkbutton({text: '打印'});
                </shiro:hasPermission>
            }
        });
    });

    function editerctokenWithdrawAppFun(id, status, exchangePairMoney) {
        if (id == undefined) {
            var rows = erctokenWithdrawAppGrid.datagrid('getSelections');
            id = rows[0].accountFundCurrentName;
        } else {
            erctokenWithdrawAppGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:erctokenwithdrawapprove:operator">
        parent.$.modalDialog({
            title: '审核',
            width: 1000,
            height: 500,
            href: '${ctx}/fund/erctokenwithdraw/approval?id=' + id + "&exchangePairMoney=" + exchangePairMoney,
            buttons: [{
                text: '通过',
                handler: function () {
                    $.messager.confirm('确认','您确认要【通过】该笔交易吗？一旦确认将无法更改！',function(r){
                        if (r){
                            parent.$.modalDialog.openner_dataGrid = erctokenWithdrawAppGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                            var f = parent.$.modalDialog.handler.find('#editerctokenWithdrawAppForm');
                            parent.$.modalDialog.handler.find('#editerctokenWithdrawAppForm #approveStatus').val(status);
                            $("#editerctokenWithdrawAppForm").form("enableValidation");
                            f.submit();
                        }
                    });
                }
            }, {
                text: '拒绝',
                handler: function () {
                    $.messager.confirm('确认','您确认要【拒绝】该笔交易吗？一旦确认将无法更改！',function(r) {
                        if (r) {
                            parent.$.modalDialog.openner_dataGrid = erctokenWithdrawAppGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                            var f = parent.$.modalDialog.handler.find('#editerctokenWithdrawAppForm');
                            parent.$.modalDialog.handler.find('#editerctokenWithdrawAppForm #approveStatus').val('<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT%>');
                            $("#editerctokenWithdrawAppForm").form("enableValidation");
                            f.submit();
                        }
                    });
                }
            }]
        });
        </shiro:hasPermission>
    }
    
    function searchBankerctokenWithdrawAppFun() {
        erctokenWithdrawAppGrid.datagrid('load', $.serializeObject($('#searchBankerctokenWithdrawAppForm')));
    }
    function cleanBankerctokenWithdrawAppFun() {
        $('#searchBankerctokenWithdrawAppForm input').val('');
        erctokenWithdrawAppGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchBankerctokenWithdrawAppForm">
            <table>
                <tr>
                    <th>证券代码:</th>
                    <td>
                        <input name="stockinfoId" style="width: 75px" class="easyui-combobox"
                               placeholder="请选择证券" data-options="
                                    url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_ERC20_TOKEN%>', method: 'get', valueField:'id',
                                    textField:'stockName', groupField:'group'">
                    </td>
                    <th>用户名或UID:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入用户名或UID"/></td>
                    <th>审批状态</th>
                    <td><select class="easyui-combobox" name="approveStatus" style="width:80px;">
                        <option value="">-请选择-</option>
                        <option selected value="<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING%>">待审核</option>
                        <option value="<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH%>">已审核</option>
                    </select> </td>
                    <th>审核开始时间:</th>
                    <td><input name="timeStart" id="timeStartApp" style="width:120px;"  class="easyui-datebox"  placeholder="请输入交易开始时间"/></td>
                    <th>审核结束时间:</th>
                    <td><input name="timeEnd"  id="timeEndApp"  style="width:120px;" class="easyui-datebox"   placeholder="请输入交易结束时间"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchBankerctokenWithdrawAppFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanBankerctokenWithdrawAppFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="erctokenWithdrawAppGrid" data-options="fit:true,border:false"></table>
    </div>
</div>