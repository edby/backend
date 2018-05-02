<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var stepLogDataGrid;
    $(function () {
            stepLogDataGrid = $('#stepLogDataGrid').datagrid({
            url: '${ctx}/settlement/clear/steplog/data',
            striped: true,
            rownumbers: true,
            pagination: false,
            singleSelect: false,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 200,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                {
                    width: '100',
                    title: '父流程',
                    field: 'processId',
                    sortable: true
                },{
                    width: '100',
                    title: '流程名',
                    field: 'processName',
                    sortable: true
                },{
                    width: '150',
                    title: '状态',
                    field: 'status',
                    sortable: true,
                    formatter: function (value, row, index) {
                        switch(value)
                        {
                            case -1:return '执行失败';break;
                            case 0:return '未执行';break;
                            case 1:return '执行成功';break;
                            default : break;
                        }
                    }
                },{
                    width: '140',
                    title: '更新时间',
                    field: 'createDate',
                    formatter: function (value, row, index) {
                        if(value!='' && value!=null  && value!=0)
                        {
                            return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");

                        }else
                        {
                            return '';
                        }
                    }
                }
            ]],
            columns: [[
                {
                    width: '685',
                    title: '备注',
                    field: 'remark',
                    sortable: true
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
    });

    function todo()
    {
        var exchangePairMoney=$("[name='vcoin_money']").val();
        var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
            $.messager.prompt('确认','您确认要关闭交易开关吗？如果您确定要进行交割平仓，请在下输入框中输入结算价：',function(r){
                if (r){
                    var val=r;
                    if(isNaN(val)){alert('结算价错误：不是一个数值！');return ;}
                    if(parseFloat(val)<=0){alert('结算价错误：不能小于0！');return ;}
                    $.ajax({
                        url: "${ctx}/settlement/clear/updateSettlementPrice",
                        data:{jiesuanjia:val,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                        type:'post',
                        async: false,
                        dataType: 'json',
                        success: function(result){
                            $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                            setCsrfToken("csrf-form");
                            if (result.code == ajax_result_success_code) {
                                $("#newjiesuanjia").text(val);
                                $('#jiesuanjia').numberbox('setValue', parseFloat(val));
                            }else
                            {
                                $.messager.alert('错误',result.message);
                                return;
                            }

                        }
                    });
                    $.ajax({
                        url: "${ctx}/settlement/clear/step/operator",
                        data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                        type:'post',
                        async: false,
                        dataType: 'json',
                        success: function(result){
                            $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                            setCsrfToken("csrf-form");
                            $('#stepLogDataGrid').datagrid('load', {});
                            if (result.code == ajax_result_success_code) {
                                $.messager.alert('成功',result.message);
                            }else
                            {
                                $.messager.alert('错误',result.message);
                            }
                        }
                    });
                }
            });
    }

    function getStockinfo()
    {
        var exchangePairMoney=$("[name='vcoin_money']").val();
        var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
        $.ajax({
            url: "${ctx}/settlement/clear/stockinfo/data",
            data:{exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
            type:'post',
            async: false,
            dataType: 'json',
            success: function(result){
                if (result.code == ajax_result_success_code) {
                    $("#newjiesuanjia").text(result.object.settlementPrice);
                    $("*[name='jiesuanjia2']").val(result.object.settlementPrice);
                }
            }
        });
    }

    $(document).ready(function(){
        getStockinfo();
        $("#money").combobox({
            onChange: function (n,o) {
                var exchangePairMoney=$("[name='vcoin_money']").val();
                var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
                $.ajax({
                    url: "${ctx}/settlement/clear/stockinfo/data",
                    data:{exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                    type:'post',
                    async: false,
                    dataType: 'json',
                    success: function(result){
                        if (result.code == ajax_result_success_code) {
                            $("#newjiesuanjia").text(result.object.settlementPrice);
                            $('#jiesuanjia').textbox("setValue",result.object.settlementPrice);
                        }
                    }
                });

            }
        });
        $(function(){
            $('#updateTradeMasterSwitch').switchbutton({
                checked: ${switchStatus=="yes"?"true":"false"},
                onChange: function(checked){
                    var status='no';
                    if(checked){status='yes';}
                    $.ajax({
                        url: "${ctx}/settlement/clear/updateTradeMasterSwitch",
                        data:{status:status},
                        type:'post',
                        async: false,
                        dataType: 'json',
                        success: function(result){
                            $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                            setCsrfToken("csrf-form");
                            if (result.code == ajax_result_success_code) {
                                $.messager.alert('成功',result.message);
                            }else
                            {
                                $.messager.alert('失败',result.message);
                            }
                        }
                    });
                }
            })
        });

        $("#jiesuan_save_btn").click(function (){
            var exchangePairMoney=$("[name='vcoin_money']").val();
            var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
            $.messager.prompt('确认','您确认要关闭交易开关吗？如果您确定要进行交割平仓，请在下输入框中输入结算价：',function(r){
                if (r){
                    var val=r;
                    if(isNaN(val)){alert('结算价错误：不是一个数值！');return ;}
                    if(parseFloat(val)<=0){alert('结算价错误：不能小于0！');return ;}
                    $.ajax({
                        url: "${ctx}/settlement/clear/updateSettlementPrice",
                        data:{jiesuanjia:val,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                        type:'post',
                        async: false,
                        dataType: 'json',
                        success: function(result){
                            $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                            setCsrfToken("csrf-form");
                            if (result.code == ajax_result_success_code) {
                                $("#newjiesuanjia").text(val);
                                $('#jiesuanjia').numberbox('setValue', parseFloat(val));
                            }else
                            {
                                $.messager.alert('错误',result.message);
                                return;
                            }
                        }
                    });
                    $.ajax({
                        url: "${ctx}/settlement/clear/step/operator",
                        data:{exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                        type:'post',
                        async: false,
                        dataType: 'json',
                        success: function(result){
                            $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                            setCsrfToken("csrf-form");
                            if (result.code == ajax_result_success_code) {
                                $.messager.alert('成功',result.message);
                            }else
                            {
                                $.messager.alert('错误',result.message);
                            }
                            $('#stepLogDataGrid').datagrid('load', {});
                        }
                    });
                }
            });
        });
        $("#jiesuanjia_save_btn").click(function (){
            var val=$("#jiesuanjia").val();
            var exchangePairMoney =  $("*[name='vcoin_money']").val();
            if(isNaN(val)){alert('结算价错误：不是一个数值！');return ;}
            if(parseFloat(val)<=0){alert('结算价错误：不能小于0！');return ;}
            $.ajax({
                url: "${ctx}/settlement/clear/updateSettlementPrice",
                data:{jiesuanjia:val,exchangePairMoney:exchangePairMoney},
                type:'post',
                async: false,
                dataType: 'json',
                success: function(result){
                    if (result.code == ajax_result_success_code) {
                        $.messager.alert('成功',result.message);
                        $("#newjiesuanjia").text(val);
                        var exchangePairMoney=$("[name='vcoin_money']").val();
                        var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
                        $.ajax({
                            url: "${ctx}/settlement/clear/stockinfo/data",
                            data:{exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                if (result.code == ajax_result_success_code) {
                                    $("#newjiesuanjia").text(result.object.settlementPrice);
                                }
                            }
                        });
                    }else
                    {
                        $.messager.alert('错误',result.message);
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                }
            });

        });

    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 260px; overflow: hidden;background-color: #fff">
        <form id="searchIcoMintForm">
            <table class="grid">
                <tr><td colspan="2">&nbsp;</td></tr>
                <tr>
                    <td align="right" width="120px">交易总控开关：</td>
                    <td align="left">
                        <input  id="updateTradeMasterSwitch" style="width: 180px;" class="easyui-switchbutton" data-options="onText:'已打开',offText:'已关闭'" >
                    </td>
                </tr>
                <tr>
                    <td align="right" width="120px">数字货币：</td>
                    <td>
                        <input  id="vcoin" name="vcoin_vcoin"  class="easyui-combobox" name="language" style="width: 180px;"
                                value="<%=FundConsts.WALLET_BIEX2BTC_TYPE%>" data-options="
									url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_PURESPOT%>', method: 'get', valueField:'id',
									textField:'stockCode', groupField:'group'"  />
                    </td>
                </tr>
                <tr>
                    <td align="right" width="120px">交易对：</td>
                    <td>
                        <input  id="money" name="vcoin_money"  class="easyui-combobox" name="language" style="width: 180px;"
                                value="<%=FundConsts.WALLET_BTC2USDX_TYPE%>" data-options="
									url: '${ctx}/stock/info/allInExchange', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'"  />
                    </td>
                </tr>
                <tr>
                    <td align="right" width="120px">结算价：</td>
                    <td>
                        <input name="jiesuanjia2" id="jiesuanjia" type="text" style="width: 180px;" placeholder="结算价" class="easyui-numberbox" data-options="required:true,min:0.01,precision:2" value="${jiesuanjia}" \>

                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <a id="jiesuanjia_save_btn"  class="easyui-linkbutton" data-options="iconCls:'icon-save'">&nbsp;保存结算价&nbsp;</a>
                        &nbsp;&nbsp;
                        <a id="jiesuan_save_btn"  class="easyui-linkbutton" data-options="iconCls:'fi-monitor'">&nbsp;进行结算&nbsp;</a>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <center>
                            <font color="red">当前结算价：<span id="newjiesuanjia">${jiesuanjia}</span>
                            </font>
                        </center>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:false">
        <table id="stepLogDataGrid" data-options="fit:false,border:false"></table>
    </div>
</div>


