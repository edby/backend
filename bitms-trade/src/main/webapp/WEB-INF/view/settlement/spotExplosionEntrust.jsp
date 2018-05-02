<%@ page import="com.blocain.bitms.trade.trade.enums.TradeEnums" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp"%>
<html>
<body>
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp" %>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <div class="col-sm-12 column">
            <ul class="nav nav-tabs bitms-tabs" style="background-color:transparent;margin-right:0px;margin-top:-15px;">
                <li onclick="jumpUrl('${ctx}/spot/historyEntrust')">
                    <a data-toggle="tab"><%--历史委托--%><fmt:message key="spot.query.historyEntrust" /></a>
                </li>
                <li class="active">
                    <a href="#" data-toggle="tab"><%--爆仓委托--%><fmt:message key="spot.query.explosionEntrust" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/settlement/spotAllocationFund')">
                    <a data-toggle="tab"><%--分摊基金--%><fmt:message key="spot.query.allpcationFund" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/settlement/spotSettlement')">
                    <a href="#" data-toggle="tab"><%--交割结算--%><fmt:message key="spot.query.settlement" /></a>
                </li>
            </ul>
            <form class="clearfix" data-widget="validator" name="entrustForm" id="entrustForm">
                <div class="panel clearfix">
                    <div class="col-sm-12 px0">
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--交易对--%><fmt:message key="spot.query.transPair" /></span>
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

                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <button type="button" class="btn btn-primary" id="searchBtn"><%--查询--%><fmt:message key='search' /></button>
                        </div>
                    </div>
                </div>
                <div class="panel clearfix">
                    <div class="table-responsive">
                        <h4 class="text-success"><%--爆仓委托--%><fmt:message key='spot.query.explosionEntrust' /></h4>
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th><%--交易对--%><fmt:message key='spot.query.transPair' /></th>
                                <th><%--委托时间--%><fmt:message key='spot.C2C.entrustedTime' /></th>
                                <%--<th>&lt;%&ndash;委托资产&ndash;%&gt;<fmt:message key='spot.C2C.entrustedAssets' /></th>--%>
                                <th><%--委托方向--%><fmt:message key='spot.C2C.entrustDirection' /></th>
                                <th><%--委托数量--%><fmt:message key='future.entrust.entrustedNumber' /></th>
                                <th><%--委托价格--%><fmt:message key='spot.C2C.entrustedPrice' /></th>
                                <th><%--状态--%><fmt:message key='state' /></th>
                            </tr>
                            </thead>
                            <tbody id="list_emement">

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

<script id="entrust_done_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$value.stockName}}</td>
        <td>{{$formatDateTime $value.entrustTime}}</td>
        <%--<td>{{$value.stockCode}}</td>--%>
        <td>{{$statusFlag $value.entrustDirect}}</td>
        <td>{{$Fix4Flag $value.entrustAmt}}</td>
        <td>{{$Fix8Flag $value.entrustPrice}}</td>
        <td>{{$status2Flag $value.entrustAmt $value.dealAmt}}</td>
    </tr>
    {{/each}}
</script>

<script>
var validator;
var renderRecivePage;
seajs.use(['pagination','validator','template','moment',"my97DatePicker"], function (Pagination,Validator,Template,moment) {
    $('.pageLoader').hide();
    validator = new Validator();

    template.helper('$formatDateTime', function (millsec) {
        return moment(millsec).format("YYYY-MM-DD HH:mm:ss.SSS");
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
    template.helper('$status2Flag', function(entrustAmt,dealAmt) {

        if(dealAmt == 0)
        {
            return  getDictValueByCode('<%=TradeEnums.DEAL_STATUS_NODEAL.getCode()%>');
        }else if(dealAmt>0 && entrustAmt>dealAmt){
            return  getDictValueByCode('<%=TradeEnums.DEAL_STATUS_PARTIALDEAL.getCode()%>');
        } else if(entrustAmt == dealAmt)
        {
            return  getDictValueByCode('<%=TradeEnums.DEAL_STATUS_ALLDEAL.getCode()%>');
        }
        else
        {
            return  '-';
        }
    });

    template.helper('$statusFlag', function(flag) {
        return  getDictValueByCode(flag);
    });

    var timestamp=new Date().getTime();
    $('#timeStart').val(moment(timestamp).format('YYYY-MM-DD'));
    $('#timeEnd').val(moment(timestamp).format('YYYY-MM-DD'));
    $('#timeStart').on('click', function(){
        WdatePicker(
            {
                minDate:"2017-01-01",
                maxDate:""+moment(timestamp).format("YYYY-MM-DD")
            }
        );
    });
    $('#timeEnd').on('click', function(){
        WdatePicker(
            {
                minDate:"2017-01-01",
                maxDate:""+moment(timestamp).format("YYYY-MM-DD")
            }
        );
    });

    renderRecivePage = new Pagination({
        beforeAjax:function(){
            $('#searchBtn').attr("disabled", true);
            $('.reg-pop').show();
        },
        url : "${ctx}/settlement/explosionEntrust/data",
        elem : "#entrustx_pagination",
        form : $("#entrustForm"),
        rows : 10,
        method : "post",
        handleData : function(json) {
            var html = Template.render("entrust_done_list_tpl", json);
            $("#list_emement").html(html);
            $('#searchBtn').attr("disabled", false);
            $('.reg-pop').hide();
        }
    });

});

$("#searchBtn").click(function() {
    renderRecivePage.render(true);
});
</script>
</body>
</html>