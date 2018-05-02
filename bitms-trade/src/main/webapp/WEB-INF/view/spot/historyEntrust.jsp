<%@ page import="com.blocain.bitms.trade.trade.enums.TradeEnums" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<body>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp" %>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <div class="col-sm-12 column">
            <ul class="nav nav-tabs bitms-tabs" style="background-color:transparent;margin-right:0px;margin-top:-15px;">
                <li class="active">
                    <a data-toggle="tab"><%--历史委托--%><fmt:message key="spot.query.historyEntrust" /></a>
                </li>
                <%--<li onclick="jumpUrl('${ctx}/fund/accountWealthAsset')">
                    <a href="#" data-toggle="tab">&lt;%&ndash;出借记录&ndash;%&gt;<fmt:message key="fund.tab.weathflow" /></a>
                </li>--%>
                <li onclick="jumpUrl('${ctx}/fund/accountDebitAsset')">
                    <a href="#" data-toggle="tab"><%--借贷流水--%><fmt:message key="fund.tab.debitflow" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/financialCurrents')">
                    <a href="#" data-toggle="tab"><%--财务流水--%><fmt:message key="fund.tab.financeflow" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/currents')">
                    <a href="#" data-toggle="tab"><%--交易流水--%><fmt:message key="fund.tab.Dealflow" /></a>
                </li>
                <%--<li onclick="jumpUrl('${ctx}/fund/candyRecord')">
                    <a href="#" data-toggle="tab">&lt;%&ndash;糖果记录&ndash;%&gt;<fmt:message key="fund.tab.candyRecords" /></a>
                </li>--%>
            </ul>
            <form class="clearfix" data-widget="validator" name="entrustForm" id="entrustForm">
                <div class="panel clearfix">
                    <div class="col-sm-12 px0 clearfix">
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--交易对--%><fmt:message key='spot.query.transPair' /></span>
                        </div>
                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <select id="exchangePairMoney" name="exchangePairMoney"  class="form-control">
                                <c:if test="${!empty stockinfos}">
                                    <c:forEach var="item" items="${stockinfos }"  varStatus="status" >
                                        <option value="${item.id}" ${item_index==0?"selected":""}>${item.stockName}</option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--委托方向--%><fmt:message key='spot.C2C.entrustDirection' /></span>
                        </div>
                        <div id="entrustDirectDiv" class="form-group col-lg-2 col-sm-3 col-xs-12">
                        </div>
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--委托类型--%><fmt:message key='spot.C2C.entrustType' /></span>
                        </div>
                        <div id="entrustTypeDiv" class="form-group col-lg-2 col-sm-3 col-xs-12">
                        </div>
                    </div>
                    <div class="col-sm-12 px0 clearfix">
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--状态--%><fmt:message key='state' /></span>
                        </div>
                        <div id="statusDiv" class="form-group col-lg-2 col-sm-3 col-xs-12">
                        </div>
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--起始时间--%><fmt:message key='fund.currents.startingTime' /></span>
                        </div>
                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <input name="timeStart" id="timeStart" class="form-control timeOpen" />
                        </div>
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--结束时间--%><fmt:message key='fund.currents.terminalTime' /></span>
                        </div>
                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <input name="timeEnd" id="timeEnd" class="form-control" />
                        </div>
                        <input name="exchangePairVCoin" id="exchangePairVCoin" type="hidden" value="<%=FundConsts.WALLET_BTC_TYPE%>" class="form-control">
                        <div class="form-group col-sm-3 col-xs-12">
                            <jsp:useBean id="now" class="java.util.Date" scope="page"/>
                            <%
                                Date nowDate = new Date();
                                nowDate.setDate(nowDate.getDate()-1);
                                request.setAttribute("nowDate", nowDate);
                            %>
                            <input name="isHis" id="isHis" type="hidden" value="no" />
                            <button type="button" class="btn btn-primary" id="searchBtn0" title="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />"><%--当前查询--%><fmt:message key="fund.curent.currentQuery" /></button>
                            <button type="button" class="btn btn-primary" id="searchBtn" title="2017-12-21~ <fmt:formatDate value="${nowDate}" pattern="yyyy-MM-dd" />" ><%--历史查询--%><fmt:message key="fund.curent.historyQuery" /></button>
                        </div>
                    </div>
                </div>
                <div class="panel clearfix">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th><%--委托时间--%><fmt:message key='spot.C2C.entrustedTime' /></th>
                                <th><%--交易对--%><fmt:message key='spot.query.transPair' /></th>
                                <th><%--交易类型--%><fmt:message key='spot.C2C.transTypes' /></th>
                                <th><%--委托方向--%><fmt:message key='spot.C2C.entrustDirection' /></th>
                                <th><%--委托类型--%><fmt:message key='spot.C2C.entrustType' /></th>
                                <th><%--委托价格--%><fmt:message key='spot.C2C.entrustedPrice' /></th>
                                <th><%--已成交--%><fmt:message key='spot.C2C.transactedNumber' />/<%--委托--%><fmt:message key='spot.C2C.entrustedNumber' /></th></th>
                                <th><%--成交均价--%><fmt:message key='spot.C2C.averageTransPrice' /></th>
                                <th><%--委托来源--%><fmt:message key='spot.C2C.entrustedOrigin' /></th>
                                <th><%--状态--%><fmt:message key='state' /></th>
                                <th><%--操作--%><fmt:message key='operation' /></th>
                            </tr>
                            </thead>
                            <tbody id="entrustx_list_emement">

                            </tbody>
                        </table>
                        <div class="mt10 m010">
                            <%-- 通用分页 --%>
                            <div id="entrustx_pagination" class="paginationContainer"></div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="modal fade" id="viewEntrustVCoinMoney" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title text-success"><%--成交记录--%><fmt:message key='spot.C2C.transactedRecord' /></h4>
            </div>
            <div class="modal-body" id="viewEntrustVCoinMoneyBody">
                <table id="rowPage2" class="table">
                    <thead>
                    <tr>
                        <th><%--成交时间--%><fmt:message key='spot.C2C.transactedTime' /></th>
                        <th><%--成交方向--%><fmt:message key='spot.C2C.transactedDerict' /></th>
                        <th><%--成交数量--%><fmt:message key='future.entrust.transactionNumber' /></th>
                        <th><%--成交价格--%><fmt:message key='spot.C2C.transactedPrice' /></th>
                        <th><%--成交金额--%><fmt:message key='spot.C2C.transactedAmount' /></th>
                        <th><%--手续费--%><fmt:message key='fee' /></th>
                    </tr>
                    </thead>
                    <tbody id="realdealx_list_emement">

                    </tbody>
                </table>
                <div class="mt10 m010">
                    <%-- 通用分页 --%>
                    <div id="realdealx_pagination" class="paginationContainer"></div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script id="entrust_done_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDateTime $value.entrustTime}}</td>
        <td>{{$value.stockName}}</td>
        <td>{{$statusFlag $value.tradeType}}</td>
        <td>{{$statusFlag $value.entrustDirect}}</td>
        <td>{{$statusFlag $value.entrustType}}</td>
        <td>{{$Fix8Flag $value.entrustPrice}}</td>
        <td>{{$Fix4Flag $value.dealAmt}}/{{$Fix4Flag $value.entrustAmt}}</td>
        <td>{{$avgFlag $value.entrustDirect,$value.dealAmt,$value.dealBalance,$value.dealFee}}</td>
        <td>{{$value.entrustSource}}</td>
        <td>{{$statusFlag $value.status}}</td>
        <td>{{$viewActionFlag $value.id,$value.tableName}}</td>
    </tr>
    {{/each}}
</script>

<script id="realdealx_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDateTime $value.dealTime}}</td>
        <td>{{$status2Flag $value.entrustDirect}}</td>
        <td>{{$Fix4Flag $value.dealAmt}}</td>
        <td>{{$Fix8Flag $value.dealPrice}}</td>
        <td>{{$Fix4Flag $value.dealBalance}}</td>
        <td>{{$feeFlag $value.entrustDirect,$value.buyFee,$value.sellFee,$value.buyFeeType,$value.sellFeeType}}{{$value.stockCode}}</td>
    </tr>
    {{/each}}
</script>

<script>
var validator;
var renderRecivePage;

seajs.use(['pagination','validator','template','moment','my97DatePicker'], function (Pagination,Validator,Template,moment) {
    validator = new Validator();
    $("#entrustTypeDiv").append(dictDropDownOptions('entrustType', 'entrustType', '','', 'form-control' ));
    $("#entrustTypeDiv select").find("option:first").before('<option selected value="">-<fmt:message key="fund.currents.pleaseSelect" />-</option>');
    $("#entrustDirectDiv").append(dictDropDownOptions('entrustDirect', 'entrustDealDirect', '', '', 'form-control' ));
    $("#entrustDirectDiv select").find("option:first").before('<option selected value="">-<fmt:message key="fund.currents.pleaseSelect" />-</option>');
    $("#statusDiv").append(dictDropDownOptions('status', 'dealStatus', '', '', 'form-control' ));
    $("#statusDiv select").find("option:first").before('<option selected value="">-<fmt:message key="fund.currents.pleaseSelect" />-</option>');
    template.helper('$formatDateTime', function (millsec) {
        return moment(millsec).format("YYYY-MM-DD HH:mm:ss.SSS");
    });
    template.helper('$avgFlag', function(direact,dealAmt,dealBalance,fee) {
            if(direact == '<%=TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()%>')
            {
                if(dealAmt>0)
                {
                    <%--return  (parseFloat(dealBalance)/parseFloat(dealAmt-fee)).toFixed(2);--%>
                    return  (parseFloat(dealBalance)/parseFloat(dealAmt)).toFixed(8);
                }
                else
                {
                    return "-";
                }
            }
            else if(direact == '<%=TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()%>')
            {
                if(dealAmt>0)
                {
                    return  (parseFloat(dealBalance)/parseFloat(dealAmt)).toFixed(8);
                    <%--return  (parseFloat(dealBalance-fee)/parseFloat(dealAmt)).toFixed(2);--%>
                }
                else
                {
                    return "-";
                }
            }
    });
    template.helper('$formatDate', function (millsec) {
        return moment(millsec).format("HH:mm:ss");
    });
    template.helper('$Fix2Flag', function(flag) {
        return  parseFloat((flag)).toFixed(2);
    });
    template.helper('$Fix4Flag', function(flag) {
        return  parseFloat((flag)).toFixed(4);
    });
    template.helper('$Fix8Flag', function(flag) {
        return  parseFloat((flag)).toFixed(8);
    });
    template.helper('$statusFlag', function(flag) {
        return  getDictValueByCode(flag);
    });
    template.helper('$viewActionFlag', function(id,tableName) {
        return  "<a class='text-success' onclick=\"doView('"+id+"','"+tableName+"')\"><fmt:message key='spot.C2C.detail' /></a>";
    });

    var timestamp=new Date().getTime();
    <%--$('#timeStart').val(moment(timestamp).format('YYYY-MM-DD'));--%>
    <%--$('#timeEnd').val(moment(timestamp).format('YYYY-MM-DD'));--%>
    $('#timeStart').on('click', function(){
        WdatePicker(
            {
                minDate:"2017-01-01",
                maxDate:""+moment(timestamp).format("YYYY-MM-DD"),
                lang:'en'
            }
        );
    });
    $('#timeEnd').on('click', function(){
        WdatePicker(
            {
                minDate:"2017-01-01",
                maxDate:""+moment(timestamp).format("YYYY-MM-DD"),
                lang:'en'
            }
        );
    });

    renderRecivePage = new Pagination({
        beforeAjax:function(){
            $('#searchBtn').attr("disabled", true);
            $('.reg-pop').show();
        },
        url : "${ctx}/spot/entrustData",
        elem : "#entrustx_pagination",
        form : $("#entrustForm"),
        rows : 10,
        method : "post",
        handleData : function(json) {
            var html = Template.render("entrust_done_list_tpl", json);
            $("#entrustx_list_emement").html(html);
            $('#searchBtn').attr("disabled", false);
            $('.reg-pop').hide();
        }
    });
    $("#searchBtn0").on('click',function() {
        var timestamp=new Date().getTime();
        $('#timeStart').val('');
        $('#timeEnd').val('');
        $("#isHis").val('no');
        renderRecivePage.render(true);
    });
});

$("#searchBtn").click(function() {
    $("#isHis").val('yes');
    renderRecivePage.render(true);
});

<%--查看成交情况--%>
function doView(id,tableName){
    var renderRecivePage;
    var exchangePairMoney = $("#exchangePairMoney").val();
    var exchangePairVCoin = $("#exchangePairVCoin").val();
    seajs.use(['pagination','template', 'moment'], function (Pagination,Template, moment) {
        renderRecivePage = new Pagination({
            url : "${ctx}/spot/realDealByEntrustId",
            data:{id:id,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin,tableName:tableName},
            elem : "#realdealx_pagination",
            rows : 10,
            method : "post",
            handleData : function(json) {
                var html = Template.render("realdealx_list_tpl", json);
                $("#realdealx_list_emement").html(html);
            }
        });

        template.helper('$formatDateTime', function (millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss.SSS");
        });
        template.helper('$formatDate', function (millsec) {
            return moment(millsec).format("HH:mm:ss");
        });
        template.helper('$Fix2Flag', function(flag) {
            return  parseFloat((flag)).toFixed(2);
        });
        template.helper('$Fix4Flag', function(flag) {
            return  parseFloat((flag)).toFixed(4);
        });
        template.helper('$status2Flag', function(flag) {
            return  getDictValueByCode(flag);
        });
        template.helper('$feeFlag', function(entrustDirect,buyFee,sellFee,buyFeeType,sellFeeType) {
            if(entrustDirect == '<%=TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()%>')
            {
                <%--成交方买入 委托方是卖出方--%>
                return parseFloat((buyFee)).toFixed(8);
            }
            if(entrustDirect == '<%=TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()%>')
            {
                <%--成交方卖出委托方是买入方--%>
                return parseFloat((sellFee)).toFixed(8);
            }
            return  '--';
        });
    });

    $('#viewEntrustVCoinMoney').modal('toggle');
}
</script>
</html>
