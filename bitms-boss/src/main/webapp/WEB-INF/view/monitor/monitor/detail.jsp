<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    //对账
    function assetCheckFun(accountId, stockinfoId, bizCategoryId, chkDate, relatedStockinfoId,acctAssetType) {
        var json = {
            "relatedStockinfoId": relatedStockinfoId,
            "accountId": accountId,
            "stockinfoId":stockinfoId
        };
        $.ajax({
            url: "${ctx}/monitor/monitoracctfundcur/checkData",
            type: "post",
            data: JSON.stringify(json),
            dataType: 'json',
            contentType: 'application/json;charset=utf-8',
            success: function (data) {
                if (data.code == bitms.success) {

                    parent.$.modalDialog({
                        title: '对账结果',
                        width: 400,
                        height: 200,
                        modal: true,
                        resizable:true,
                        href:'${ctx}/monitor/monitoracctfundcur/assetchk',
                        onLoad:function(){
                            $('#checkResult').text(data.object.monitorDesc);
                            if(data.object.returnCode == 1 && data.object.chekResult == -1){
                                $('#detailBtn').show();
                                $('#detailBtn').click(function(){
                                    monitorCurDetailFun(accountId, stockinfoId, bizCategoryId, chkDate, relatedStockinfoId,acctAssetType);
                                });
                            }
                        }
                    });
                } else {
                    alert('对账操作失败');
                }
            }
        })
    }

    //查看明细
    function monitorCurDetailFun(accountId, stockinfoId, bizCategoryId, chkDate, relatedStockinfoId,acctAssetType) {
        $('#detailForm').window('open');
        $('#monitorDetail').datagrid({
            url: '${ctx}/monitor/monitordetail/data',
            queryParams: {
                accountId: accountId,
                stockinfoId: stockinfoId,
                bizCategoryId: bizCategoryId,
                monitorDateStr: chkDate,
                relatedStockinfoId: relatedStockinfoId,
                acctAssetType: acctAssetType
            },
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
                    title: '账户ID',
                    field: 'accountId'
                },
                {
                    width: '150',
                    title: '业务时间',
                    field: 'businessDate',
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                },
                {
                    width: '270',
                    title: '业务类别',
                    field: 'businessFlag'
                },

                {
                    width: '100',
                    title: '证券ID',
                    field: 'stockinfoId'
                },
                {
                    width: '100',
                    title: '资产类型',
                    field: 'acctAssetType',
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '钱包资产';
                        } else if(value == 2){
                            return '杠杆资产';
                        } else if(value == 3){
                            return '理财资产';
                        }
                    }
                },
                {
                    width: '130',
                    title: '原业务流水ID',
                    field: 'originalBusinessId'
                },
                {
                    width: '130',
                    title: '关联业务流水ID',
                    field: 'relatedBusinessId'
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
                    width: '5000',
                    title: '描述',
                    field: 'monitorDesc'
                }
            ]]
        });
    }

    //查看账户
    function findAssetFun(stockinfoId) {
        $('#assetForm').window('open');
        $('#assetData').datagrid({
            url: '${ctx}/monitor/monitoracctfundcur/relatedData',
            queryParams: {
                stockinfoId: stockinfoId
            },
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            columns: [[
                {
                    width: '180',
                    title: '账户名称',
                    field: 'accountName'
                },
                {
                    width: '150',
                    title: '监控时间',
                    field: 'chkDate',
                    formatter: function (value, row, index) {
//                        return new Date(parseInt(value)).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                },
                {
                    width: '100',
                    title: '资产类型',
                    field: 'acctAssetType',
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '钱包资产';
                        } else if(value == 2){
                            return '杠杆资产';
                        } else if(value == 3){
                            return '理财资产';
                        }
                    }
                },
                {
                    width: '100',
                    title:
                        '证券代码',
                    field:
                        'stockinfoId'
                }
                ,
                {
                    width: '80',
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
                        100,
                    title:
                        '操作',
                    formatter:
                        function (value, row, index) {
                            var str = '';
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-monitorParamEdit" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="assetCheckFun(\'{0}\',\'{1}\',\'{2}\',\'{3}\',\'{4}\',\'{5}\');" >对账</a>', row.accountId, row.stockinfoId, row.bizCategoryId, row.chkDate, row.relatedStockinfoId,row.acctAssetType);
                            return str;
                        }

                }
                ,
                {
                    width: '1000',
                    title:
                        '描述',
                    field:
                        'monitordesc'
                }
            ]]
        });
    }

</script>
<div id="dd"></div>
<div id="detailForm" class="easyui-dialog" style="width:800px; height:500px;resizable:true"
     data-options="closed:true,title:'账户明细'">
    <table id="monitorDetail" data-options="fit:true,border:false">
    </table>
</div>
<div id="assetForm" class="easyui-dialog" style="width:800px; height:500px;resizable:true"
     data-options="closed:true,title:'账户资产监控'">
    <table id="assetData" data-options="fit:true,border:false">
    </table>
</div>

<%--<div id="checkDialog" class="easyui-dialog" style="width:400px; height:200px;resizable:true;modal:true"--%>
     <%--data-options="closed:true,title:'对账结果'">--%>
    <%--<div id="checkResult"></div>--%>
        <%--<button id="detailBtn" style="display:none">查看资金流水明细</button>--%>
<%--</div>--%>