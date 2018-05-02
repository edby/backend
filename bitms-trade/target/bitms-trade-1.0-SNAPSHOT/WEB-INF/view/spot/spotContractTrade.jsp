<%@ page import="com.blocain.bitms.quotation.consts.InQuotationConsts" %>
<%@ page import="com.blocain.bitms.trade.trade.enums.TradeEnums" %>
<%@ page import="com.blocain.bitms.tools.consts.BitmsConst" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<body>
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp" %>
    <%--代码开始--%>
    <div class="row">
        <div class="col-sm-12 column">
            <div class="panel">
                <div class="row">
                    <div class="col-lg-12">
                        <div id="C2CKlineDiv" class="bitms-bg1 col-lg-12 clearfix p5 mb10" style="cursor: pointer;border-bottom: 1px solid #23333f">
                            <span class="pl10 glyphicon glyphicon-chevron-down">&nbsp;<%--行情图表--%><fmt:message key='spot.C2C.quotationChart' /></span>
                        </div>
                        <div id="C2CKline" class="clearfix col-lg-12 bitms-hide">
                            <ul id="kLineTopic" class="col-xs-12 nav nav-tabs bitms-tabs bitms-c2c-tabs">
                                <li tips="1分" data="${money.topicKline1m}" ><a data-toggle="tab">1m</a></li>
                                <li tips="5分" data="${money.topicKline5m}" class="active"><a data-toggle="tab">5m</a></li>
                                <li tips="15分" data="${money.topicKline15m}"><a data-toggle="tab">15m</a></li>
                                <li tips="30分" data="${money.topicKline30m}"><a data-toggle="tab">30m</a></li>
                                <li tips="60分" data="${money.topicKlineHour}"><a data-toggle="tab">1h</a></li>
                                <li tips="日线" data="${money.topicKlineDay}"><a data-toggle="tab">1d</a></li>
                                <%--<li tips="周线" data="${money.topicKlineWeek}"><a data-toggle="tab">周线</a></li>--%>
                                <%--<li tips="月线" data="${money.topicKlineMonth}"><a data-toggle="tab">月线</a></li>--%>
                            </ul>
                            <!--K线图-->
                            <div id="bit_market"></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-8 clearfix pr5 mb10">
                        <div class="bitms-bg1 clearfix pb10">
                            <div class="col-sm-6 C2Ctable mt10" id="C2Cscroll">
                                <div class="thead tr clearfix">
                                    <span class="col-xs-3 bitms-c2" style="text-align: center"><%--时间--%><fmt:message key='time' /></span>
                                    <span class="col-xs-3 bitms-c2"><%--价格--%>(${money.capitalAmtSymbol})<fmt:message key='price' /></span>
                                    <span class="col-xs-3 bitms-c2"><%--数量--%>(${money.tradeAmtSymbol})<fmt:message key='number' /></span>
                                    <span class="col-xs-3 bitms-c2"><%--方向--%><fmt:message key='direction' /></span>
                                </div>
                                <div id="trading">

                                </div>
                            </div>
                            <div class="col-sm-6 C2Ctable mt10" id="C2CbuySell" style="border-left: 1px solid #22333f;">
                                <div class="thead tr clearfix" style="margin-bottom: 5px;">
                                    <span class="col-xs-3 bitms-c2" style="text-align: center"><%--买卖--%><fmt:message key='spot.C2C.buySell' /></span>
                                    <span class="col-xs-3 bitms-c2"><%--价格--%>(${money.capitalAmtSymbol})<fmt:message key='price' /></span>
                                    <span class="col-xs-3 bitms-c2"><%--数量--%>(${money.tradeAmtSymbol})<fmt:message key='number' /></span>
                                    <span class="col-xs-3 bitms-c2"><%--累计--%>(${money.tradeAmtSymbol})<fmt:message key='spot.C2C.cumulative' /></span>
                                </div>
                                <div id="deep_sell">

                                </div>
                                <hr style="margin-top: 8px;margin-bottom: 8px;">
                                <div id="deep_buy">

                                </div>
                                <div class="clearfix mt10 mb10 ml20 dataTxt">
                                    <ul id="deepLevel" class="nav nav-tabs bitms-tabs bitms-depth-tabs" style="display: inline-block">
                                        <span class="bitms-c2 pull-left"><%--深度--%><fmt:message key='spot.C2C.depth' />:</span>
                                        <li data="0" class="active"><a data-toggle="tab">0</a></li>
                                        <li data="1"><a data-toggle="tab">1</a></li>
                                        <li data="2"><a data-toggle="tab">2</a></li>
                                        <li data="3"><a data-toggle="tab">3</a></li>
                                        <li data="4"><a data-toggle="tab">4</a></li>
                                        <li data="5"><a data-toggle="tab">5</a></li>
                                    </ul>
                                    <i style="line-height: 20px" class="ml5 iconfont text-success pull-right">&#xe620;</i>
                                    <span class="bitms-c2 pull-right">
                                        <span><%--距离结算时间--%><fmt:message key='spot.C2C.istSettlementTime' /></span>
                                        <span class="text-danger" id="Days">7</span>
                                        <span><%--天--%><fmt:message key='day' /></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 clearfix pl5 mb10">
                        <div class="bitms-bg1 mb10 p10 clearfix dataTxt">
                            <div class="col-sm-4 px0" style="line-height: 40px;border-right: 1px solid #22333f">
                                <a id="drop4" href="#" class="dropdown-toggle bitms-c2"  data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="true">
                                    <span>${money.stockName}</span>
                                    <span class="dataTxt" style="padding: 0px 2px;border-radius: 2px;color: #1e222d;background-color: #4ab1cf"><%--合约--%><fmt:message key='spot.C2C.contract' /></span>
                                    <span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu bitms-c2" aria-labelledby="drop4" style="border: 4px solid #23333f;padding: 4px;">
                                    <c:if test="${!empty stockInfoList}">
                                        <c:forEach items="${stockInfoList}" var="obj">
                                            <li style="padding: 5px;" >
                                                <a class="text-center" onclick="jumpUrl(this)" remark="${obj.remark}" id="${obj.id}">
                                                    <span style="text-align:left">${obj.stockName}</span>
                                                    <span class="dataTxt" style="padding: 0px 2px;border-radius: 2px;color: #1e222d;background-color: #4ab1cf"><%--合约--%><fmt:message key='spot.C2C.contract' /></span>
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </c:if>
                                </ul>
                            </div>
                            <form method="post" action="/spot/spotContractTrade?type=${money.remark}" id="jumpForm">
                                <input type="hidden" id="jumpId" name="exchangePairMoney" value="${money.id}" />
                            </form>
                            <div id="quotation" class="col-sm-8 clearfix px0 pr5 pl5 text-right">
                                <span class="col-xs-4 px0 dataTxt">
                                    <span class="bitms-c2">00000.00${money.tradeAmtUnit}</span>
                                    <br>
                                    <span><%--24小时成交量--%><fmt:message key='spot.C2C.24amt' />&nbsp;</span>
                                </span>
                                <span class="col-xs-4 px0 dataTxt">
                                    <span class="text-success">${money.capitalAmtSymbol}0000.00↑</span>
                                    <br>
                                    <span><%--最新成交价--%><fmt:message key='spot.C2C.platPrice' />&nbsp;</span>
                                </span>
                                <span class="col-xs-4 px0 dataTxt">
                                    <span class="text-success">${money.capitalAmtSymbol}0000.00↑</span>
                                    <br>
                                    <span><%--指数--%><fmt:message key='spot.C2C.idxPrice' />&nbsp;</span>
                                </span>
                            </div>
                        </div>
                        <div class="bitms-bg1 mb10 p10 pb5 clearfix">
                            <div class="col-sm-6 px0 pr5 pl5 dataTxt" id="datatxtDiv1">
                                <span class="mb5" style="display: block">
                                    <span><%--账户权益--%><fmt:message key='spot.C2C.netAsset' />:</span>
                                    <a class="pull-right text-success" style="border-bottom: 0.1px solid #648241;" href="${ctx}/fund/accountAsset">转账</a>
                                    <span class="pull-right">&nbsp;</span>
                                    <span class="pull-right bitms-c2">&nbsp;${money.assetAmtUnit}</span>
                                    <span class="pull-right bitms-c2" id="btcNetValue"></span>
                                </span>
                                <span class="mb5" style="display: block">
                                    <span><%--强平价格--%><fmt:message key='spot.C2C.explosionPrice' />:</span>
                                    <span class="pull-right bitms-c2" id="riskRate" style="display: inline-block;padding: 0px 2px;border-radius: 2px;color: #151922;"></span>
                                    <span class="pull-right">&nbsp;</span>
                                    <span class="pull-right bitms-c2" id="explosionPrice"></span>
                                    <span class="pull-right bitms-c2">${money.capitalAmtSymbol}&nbsp;</span>
                                </span>
                            </div>
                            <div class="col-sm-6 px0 pr5 pl5 dataTxt" id="datatxtDiv2">
                                <span class="mb5" style="display: block">
                                    <span><%--当周盈亏--%><fmt:message key='spot.C2C.profitAndLoss' />:</span>
                                    <span class="pull-right bitms-c2" id="profitAndLossTag" style="display: inline-block;padding: 0px 2px;border-radius: 2px;color: #151922;"></span>
                                    <span class="pull-right">&nbsp;</span>
                                    <span class="pull-right bitms-c2">&nbsp;${money.assetAmtUnit}</span>
                                    <span class="pull-right bitms-c2" id="profitAndLoss"></span>
                                </span>
                                <span class="mb5 clearfix" style="display: block">
                                    <span style="position: absolute"><%--当前持仓--%><fmt:message key='spot.C2C.positionValue' />:</span>
                                    <span class="pull-right bitms-c2">
                                        <span>${money.clearAmtSymbol}</span>
                                        <span id="usdxPositionValue"></span>
                                        <span>&nbsp;</span>
                                        <span id="direction" class="pull-right" style="display: inline-block;padding: 0px 2px;border-radius: 2px;color: #151922;"></span>
                                    </span>
                                </span>
                            </div>
                        </div>
                        <div class="bitms-bg1 p10 pb0 clearfix">
                            <form:form class="form-horizontal col-sm-6 px0 pr5 pl5 bitms-bg1" id="C2Cbuyform">
                                <input name="exchangePairMoney" id="exchangePairMoney" type="hidden" value="${exchangePairMoney}" class="form-control">
                                <input name="exchangePairVCoin" id="exchangePairVCoin" type="hidden" value="${exchangePairVCoin}" class="form-control">
                                <input name="entrustType" id="entrustType" type="hidden" value="<%=TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()%>" class="form-control">
                                <%--<div class="form-group mb5">
                                    <div class="col-xs-12 dataTxt">
                                        <span>可用&lt;%&ndash;&lt;%&ndash;余额&ndash;%&gt;<fmt:message key='spot.C2C.balance' />&ndash;%&gt;:</span>
                                        <span class="text-success pull-right">
                                            <span id="<c:choose><c:when test="${isVCoin}">usdxAmtBalance</c:when><c:otherwise>btcAmtBalance</c:otherwise></c:choose>"></span>
                                            <span>${money.capitalAmtUnit}</span>
                                        </span>
                                        <br>
                                        <span>&lt;%&ndash;冻结&ndash;%&gt;<fmt:message key='spot.C2C.freeze' />:</span>
                                        <span class="text-success pull-right">
                                            <span id="<c:choose><c:when test="${isVCoin}">usdxFrozen</c:when><c:otherwise>btcFrozen</c:otherwise></c:choose>"></span>
                                            <span>${money.capitalAmtUnit}</span>
                                        </span>
                                        <br>
                                        <span>负债:</span>
                                        <span class="text-success pull-right">
                                            <span id="<c:choose><c:when test="${isVCoin}">usdxBorrow</c:when><c:otherwise>btcBorrow</c:otherwise></c:choose>">0000.00</span>
                                            <span>${money.capitalAmtUnit}</span>
                                        </span>
                                    </div>
                                </div>--%>
                                <div class="form-group mb15">
                                    <div class="col-xs-12 ui-form-item">
                                        <input id="priceBuy" name="entrustPrice" class="form-control form-controlC2C" placeholder="<%--买入价格--%><fmt:message key='spot.C2C.buyingPrice' />" data-display="<%--买入价格--%><fmt:message key='spot.C2C.buyingPrice' />" onkeydown="buyKeyDown();">
                                    </div>
                                </div>
                                <div class="form-group mb15">
                                    <div class="col-xs-12 ui-form-item">
                                        <input name="entrustAmt" id="buyEntrustAmt" class="form-control form-controlC2C" placeholder="" data-display="<%--买入数量--%><fmt:message key='spot.C2C.buyingQuantity' />" onkeydown="buyKeyDown();">
                                    </div>
                                </div>
                                <div class="form-group mb5">
                                    <div class="col-xs-12">
                                        <div id="buyButton" class="btn btn-group-lg btn-block cf btn-primary form-controlC2C">开多/平空<%--&lt;%&ndash;买入&ndash;%&gt;<fmt:message key='spot.C2C.buy' />--%></div>
                                    </div>
                                </div>
                                <div class="form-group mb5 clearfix">
                                    <div class="col-xs-12 dataTxt">
                                        <div class="pull-left">
                                            <span><%--限价--%><fmt:message key='spot.C2C.highestPrice' />：</span>
                                            <span class="text-success" id="buyHighestLimitPrice">0000.00</span>
                                        </div>
                                        <div class="pull-right">
                                            <span><%--费率--%><fmt:message key='spot.C2C.buyBtcFeeRate' />：</span>
                                            <span class="text-success" id="buyBtcFeeRate">0000.00</span>
                                        </div>
                                    </div>
                                </div>
                            </form:form>
                            <form:form class="form-horizontal col-sm-6 px0 pr5 pl5 bitms-bg1" id="C2Csellform">
                                <input name="exchangePairMoney" id="exchangePairMoney" type="hidden" value="${exchangePairMoney}" class="form-control">
                                <input name="exchangePairVCoin" id="exchangePairVCoin" type="hidden" value="${exchangePairVCoin}" class="form-control">
                                <input name="entrustType" id="entrustType" type="hidden" value="<%=TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()%>" class="form-control">
                                <%--<div class="form-group mb5">
                                    <div class="col-xs-12 dataTxt">
                                        <span>可用&lt;%&ndash;&lt;%&ndash;余额&ndash;%&gt;<fmt:message key='spot.C2C.balance' />&ndash;%&gt;:</span>
                                        <span class="text-danger pull-right">
                                            <span id="<c:choose><c:when test="${isVCoin}">btcAmtBalance</c:when><c:otherwise>usdxAmtBalance</c:otherwise></c:choose>"></span>
                                            <span>${money.tradeAmtUnit}</span>
                                        </span>
                                        <br>
                                        <span>&lt;%&ndash;冻结&ndash;%&gt;<fmt:message key='spot.C2C.freeze' />:</span>
                                        <span class="text-danger pull-right" >
                                            <span id="<c:choose><c:when test="${isVCoin}">btcFrozen</c:when><c:otherwise>usdxFrozen</c:otherwise></c:choose>"></span>
                                            <span>${money.tradeAmtUnit}</span>
                                        </span>
                                        <br>
                                        <span>负债:</span>
                                        <span class="text-danger pull-right">
                                            <span id="<c:choose><c:when test="${isVCoin}">btcBorrow</c:when><c:otherwise>usdxBorrow</c:otherwise></c:choose>">0000.00</span>
                                            <span>${money.tradeAmtUnit}</span>
                                        </span>
                                    </div>
                                </div>--%>
                                <div class="form-group mb15">
                                    <div class="col-xs-12 ui-form-item">
                                        <input id="priceSell" name="entrustPrice" class="form-control form-controlC2C" placeholder="<%--卖出价格--%><fmt:message key='spot.C2C.sellingPrice' />" data-display="<%--卖出价格--%><fmt:message key='spot.C2C.sellingPrice' />" onkeydown="sellKeyDown();">
                                    </div>
                                </div>
                                <div class="form-group mb15">
                                    <div class="col-xs-12 ui-form-item">
                                        <input name="entrustAmt" id="sellEntrustAmt" class="form-control form-controlC2C" placeholder="" data-display="<%--卖出数量--%><fmt:message key='spot.C2C.sellingQuantity' />" onkeydown="sellKeyDown();">
                                    </div>
                                </div>
                                <div class="form-group mb5">
                                    <div class="col-xs-12">
                                        <div id="sellButton" class="btn btn-group-lg btn-block cf btn-danger form-controlC2C">开空/平多<%--&lt;%&ndash;卖出&ndash;%&gt;<fmt:message key='spot.C2C.sell' />--%></div>
                                    </div>
                                </div>
                                <div class="form-group mb5 clearfix">
                                    <div class="col-xs-12 dataTxt">
                                        <div class="pull-left">
                                            <span><%--限价--%><fmt:message key='spot.C2C.highestPrice' />：</span>
                                            <span class="text-danger" id="sellLowestLimitPrice">0000.00</span>
                                        </div>
                                        <div class="pull-right">
                                            <span><%--费率--%><fmt:message key='spot.C2C.buyBtcFeeRate' />：</span>
                                            <span class="text-danger" id="sellBtcFeeRate">0000.00</span>
                                        </div>
                                    </div>
                                </div>
                            </form:form>
                        </div>
                        <%--<div class="bitms-bg1 p10 pt0 clearfix">
                            <div class="col-sm-12 dataTxt pl5" style="border: 1px solid;border-radius: 2px;">
                                <span>&lt;%&ndash;根据当前账户权益及市场风控，您还可使用&ndash;%&gt;<fmt:message key='spot.C2C.quotaTxt1' /></span>
                                <span class="text-success" style="font-weight: bold">
                                    <span id="usdxQuota"></span>
                                    <span>${money.capitalAmtUnit}</span>
                                </span>
                                <span>&lt;%&ndash;或&ndash;%&gt;<fmt:message key='spot.C2C.and' /></span>
                                <span class="text-danger" style="font-weight: bold">
                                    <span id="btcQuota"></span>
                                    <span>${money.tradeAmtUnit}</span>
                                </span>
                            </div>
                        </div>--%>
                    </div>
                </div>
            </div>
            <div class="panel pt0">
                <div class="row">
                    <div class="col-lg-12">
                        <ul class="nav nav-tabs bitms-tabs clearfix">
                            <li class="active">
                                <a href="#panel-293475" id="index_1" data-toggle="tab"><%--当前未成交--%><fmt:message key='spot.C2C.noTransAtPresent' />（<span id="doingCnt">0</span>）</a>
                            </li>
                            <li>
                                <a href="#panel-293475" id="index_2" data-toggle="tab"><%--历史委托--%><fmt:message key='spot.C2C.historyTrans' /></a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="table-responsive tab-pane active text-center" id="panel-293475">
                                <table id="rowPage" class="table mb10">
                                    <thead>
                                    <tr>
                                        <th><%--委托时间--%><fmt:message key='spot.C2C.entrustedTime' /></th>
                                        <th><%--交易类型--%><fmt:message key='spot.C2C.transTypes' /></th>
                                        <th><%--委托方向--%><fmt:message key='spot.C2C.entrustDirection' /></th>
                                        <th><%--委托价格--%><fmt:message key='spot.C2C.entrustedPrice' />(${money.capitalAmtSymbol})</th>
                                        <th><%--已成交--%><fmt:message key='spot.C2C.transactedNumber' />/<%--委托--%><fmt:message key='spot.C2C.entrustedNumber' />(${money.tradeAmtSymbol})</th>
                                        <th><%--成交均价--%><fmt:message key='spot.C2C.averageTransPrice' />(${money.capitalAmtSymbol})</th>
                                        <th><%--委托来源--%><fmt:message key='spot.C2C.entrustedOrigin' /></th>
                                        <th><%--状态--%><fmt:message key='state' /></th>
                                        <th><%--操作--%><fmt:message key='operation' /></th>
                                    </tr>
                                    </thead>
                                    <tbody id="entrustx_list_emement">

                                    </tbody>
                                </table>
                                <a class="text-success" id="jumpHistory" style="display: none" href="${ctx}/spot/historyEntrust">查看更多...</a>
                                <%--<div class="mt10 m010">
                                    <%-- 通用分页 --%>
                                    <div id="entrustx_pagination" class="paginationContainer"></div>
                                </div>--%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
            <div class="modal-body table-responsive" id="viewEntrustVCoinMoneyBody">
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
<script src="${ctx}/scripts/src/spot/C2CKLine.js"></script>
<script id="trading_tpl" type="text/html">
    {{each msgContent}}
    <div class="tr clearfix">
        <span class="col-xs-3 bitms-c2 text-center" style="text-align: center">{{$formatDate $value.dealTime}}</span>
        <span class="col-xs-3 bitms-c2">{{$value.dealPrice}}</span>
        <span class="col-xs-3 bitms-c2">{{$value.dealAmt}}</span>
        <span class="col-xs-3 bitms-c2 text-center">
            <span class="{{$value.direct=='spotSell'?'text-danger':'text-success'}}">
                {{$value.direct=='spotSell'?'Sell':'Buy'}}
            </span>
        </span>
    </div>
    {{/each}}
</script>

<script id="quotation_tpl" type="text/html">
    {{each msgContent}}
    <span class="col-xs-4 px0 dataTxt">
        <span class="bitms-c2">{{$Fix4Flag $value.vcoinAmtSum24h}}${money.tradeAmtUnit}</span>
        <br>
        <span><%--24小时成交量--%><fmt:message key='spot.C2C.24amt' />&nbsp;</span>
    </span>
    <span class="col-xs-4 px0 dataTxt">
        <span class="{{$value.direct=='DOWN'?'text-danger':'text-success'}}">${money.capitalAmtSymbol}{{$value.direct=='spotSell'?''+$value.platPrice+'↓':''+$value.platPrice+'↑'}}</span>
        <br>
        <span><%--最新成交价--%><fmt:message key='spot.C2C.platPrice' />&nbsp;</span>
    </span>
    <span class="col-xs-4 px0 dataTxt">
        <span class="{{$value.upDownIdx=='DOWN'?'text-danger':'text-success'}}">${money.capitalAmtSymbol}{{$value.upDownIdx=='DOWN'?''+$value.idxPrice+'↓':''+$value.idxPrice+'↑'}}</span>
        <br>
        <span><%--指数--%><fmt:message key='spot.C2C.idxPrice' />&nbsp;</span>
    </span>
    {{/each}}
</script>

<script id="deep_sell_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.direct == 'spotSell' && showLevel == $value.deepLevel}}
    <div class="tr clearfix" style="position: relative;margin-bottom: 2px;" onclick="autoWriteBuy({{$value.entrustPrice}},{{$value.entrustAmtSum}})">
        <div style="position: absolute;top: 0px; bottom: 0px; right: 0px; width:{{$percentFlag $value.entrustAmtSum}}%; border-radius: 2px; background-color: rgba(169,68,66,0.3);"></div>
        <span class="col-xs-3 text-danger" style="text-align: center"><fmt:message key='sell' />{{$value.desc}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}} {{$value.entrustAmt>500?'style=font-weight:bold':''}}>{{$value.entrustPrice}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}} {{$value.entrustAmt>500?'style=font-weight:bold':''}}>{{$value.entrustAmt}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}} {{$value.entrustAmt>500?'style=font-weight:bold':''}}>{{$value.entrustAmtSum}}</span>
    </div>
    {{/if}}
    {{/each}}
</script>
<script id="deep_buy_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.direct == 'spotBuy' && showLevel == $value.deepLevel}}
    <div class="tr clearfix" style="position: relative;margin-bottom: 2px;" onclick="autoWriteSell({{$value.entrustPrice}},{{$value.entrustAmtSum}})">
        <div style="position: absolute; top: 0px; bottom: 0px; right: 0px; width:{{$percentFlag $value.entrustAmtSum}}%; border-radius: 2px; background-color: rgba(100,130,65,0.3);"></div>
        <span class="col-xs-3 text-success" style="text-align: center"><fmt:message key='buy' />{{$value.desc}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}} {{$value.entrustAmt>500?'style=font-weight:bold':''}}>{{$value.entrustPrice}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}} {{$value.entrustAmt>500?'style=font-weight:bold':''}}>{{$value.entrustAmt}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}} {{$value.entrustAmt>500?'style=font-weight:bold':''}}>{{$value.entrustAmtSum}}</span>
    </div>
    {{/if}}
    {{/each}}
</script>

<script id="entrust_doing_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDateTime $value.entrustTime}}</td>
        <td>{{$statusFlag $value.tradeType}}</td>
        <td>{{$statusFlag $value.entrustDirect}}</td>
        <%--td>{{$statusFlag $value.entrustType}}</td>--%>
        <td>{{$Fix2Flag $value.entrustPrice}}</td>
        <td>{{$Fix4Flag $value.dealAmt}}/{{$Fix4Flag $value.entrustAmt}}</td>
        <td>{{$avgFlag $value.entrustDirect,$value.dealAmt,$value.dealBalance,$value.dealFee}}</td>
        <td>{{$value.entrustSource}}</td>
        <td>{{$statusFlag $value.status}}</td>
        <td>{{$cancalActionFlag $value.id}}</td>
    </tr>
    {{/each}}
</script>

<script id="entrust_done_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDateTime $value.entrustTime}}</td>
        <td>{{$statusFlag $value.tradeType}}</td>
        <td>{{$statusFlag $value.entrustDirect}}</td>
        <%--<td>{{$statusFlag $value.entrustType}}</td>--%>
        <td>{{$Fix2Flag $value.entrustPrice}}</td>
        <td>{{$Fix4Flag $value.dealAmt}}/{{$Fix4Flag $value.entrustAmt}}</td>
        <td>{{$avgFlag $value.entrustDirect,$value.dealAmt,$value.dealBalance,$value.dealFee}}</td>
        <td>{{$value.entrustSource}}</td>
        <td>{{$statusFlag $value.status}}</td>
        <td>{{$viewActionFlag $value.id}}</td>
    </tr>
    {{/each}}
</script>

<script id="realdealx_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDateTime $value.dealTime}}</td>
        <td>{{$status2Flag $value.entrustDirect}}</td>
        <td>{{$Fix4Flag $value.dealAmt}}</td>
        <td>{{$Fix2Flag $value.dealPrice}}</td>
        <td>{{$Fix4Flag $value.dealBalance}}</td>
        <td>{{$feeFlag $value.entrustDirect,$value.buyFee,$value.sellFee,$value.buyFeeType,$value.sellFeeType}}{{$value.stockCode}}</td>
    </tr>
    {{/each}}
</script>

<script>
    var ws = null; //websocket对象
    var url = null; //访问地址
    var lineName = "5分";//K线提示名
    var level = 0;//深度
    var myChart = null;
    var deepData = null;
    var validator;
    var renderRecivePage;
    var tab_index=1;
    var reader = {readAs: function(type,blob,cb){var r = new FileReader();r.onloadend = function(){if(typeof(cb) === 'function') {cb.call(r,r.result);}};try{r['readAs'+type](blob);}catch(e){}}};
    seajs.use(['pagination','validator','template', 'moment', 'sockjs','reconnection','echarts'], function (Pagination,Validator,Template, moment) {
        $('.pageLoader').hide();
        myChart = echarts.init(document.getElementById("bit_market"));
        validator = new Validator();

        renderRecivePage = new Pagination({
            url : "${ctx}/spot/matchTrade/entrustxOnDoing",
            data:{exchangePairMoney:${exchangePairMoney},exchangePairVCoin:${exchangePairVCoin},entrustType:'<%=TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()%>'},
            rows : 100,
            method : "get",
            handleData : function(json) {
                $("#doingCnt").text(json.total);
                var html = Template.render("entrust_doing_list_tpl", json);
                $("#entrustx_list_emement").html(html);
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
        template.helper('$percentFlag', function(flag) {
            if(flag < 0){
                flag = 0;
            }
            if(flag > 100){
                flag = 100;
            }
            return  parseFloat(flag).toFixed(2);
        });
        template.helper('$Fix4Flag', function(flag) {
            return  parseFloat((flag)).toFixed(4);
        });
        template.helper('$avgFlag', function(direact,dealAmt,dealBalance,fee) {
            if(direact == '<%=TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()%>')
            {
                if(dealAmt>0)
                {
                    //return  (parseFloat(dealBalance)/parseFloat(dealAmt-fee)).toFixed(2);
                    return  (parseFloat(dealBalance)/parseFloat(dealAmt)).toFixed(2);
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
                    return  (parseFloat(dealBalance)/parseFloat(dealAmt)).toFixed(2);
                    //return  (parseFloat(dealBalance-fee)/parseFloat(dealAmt)).toFixed(2);
                }
                else
                {
                    return "-";
                }
            }

        });
        template.helper('$statusFlag', function(flag) {
            return  getDictValueByCode(flag);
        });
        template.helper('$cancalActionFlag', function(id) {
            return  "<a class='text-success' onclick=\"doCancel('"+id+"')\"><%--撤单--%><fmt:message key='spot.C2C.cancel' /></a>";
        });
        template.helper('$viewActionFlag', function(id) {
            return  "<a class='text-success' onclick=\"doView('"+id+"')\"><%--明细--%><fmt:message key='spot.C2C.view' /></a>";
        });
        <% if (BitmsConst.RUNNING_ENVIRONMONT.equals("production")){ %>
        if (window.location.protocol == 'http:') {
            url = 'ws://socket.bitms.com' + "${money.webSocketUrl}";
        } else {
            url = 'wss://socket.bitms.com' + "${money.webSocketUrl}";
        }
        <% }else{ %>
        if (window.location.protocol == 'http:') {
            url = 'ws://' + window.location.host + "${money.webSocketUrl}";
        } else {
            url = 'wss://' + window.location.host + "${money.webSocketUrl}";
        }
        <% } %>

        if ('WebSocket' in window) {
            ws = new ReconnectingWebSocket(url);
        } else {
            ws = new SockJS(url);
        }

        ws.onmessage = function (event) {
            var blob = event.data;
            reader.readAs('Text',blob.slice(0,blob.size,'text/plain;charset=UTF-8'),function(result){
                var message = JSON.parse(result);
                var msgType = message.msgType;
                if (msgType == '<%=InQuotationConsts.MESSAGE_TYPE_REALDEAL%>') {// deal
                    renderDeal(message);
                }
                else if (msgType == '<%=InQuotationConsts.MESSAGE_TYPE_DEEPPRICE%>') {// deep
                    renderDeep(message);
                }
                else if(msgType == '<%=InQuotationConsts.MESSAGE_TYPE_RTQUOTATION%>'){ //quotation
                    renderQuotation(message);
                }
                else {// kline
                    renderKline(message);
                }
            });
        };

        function renderDeal(message) {
            var html = Template.render("trading_tpl", message);
            $("#trading").html(html);
        }

        function renderQuotation(message) {
            var html = Template.render("quotation_tpl", message);
            $("#quotation").html(html);

            /*最高买价、最低卖价*/
            if(message.msgContent[0].buyHighestLimitPrice != null){
                $("#buyHighestLimitPrice").text(parseFloat(message.msgContent[0].buyHighestLimitPrice).toFixed(2));
            }
            if(message.msgContent[0].sellLowestLimitPrice != null){
                $("#sellLowestLimitPrice").text(parseFloat(message.msgContent[0].sellLowestLimitPrice).toFixed(2));
            }
        }

        function renderDeep(message) {
            message.showLevel = level;
            deepData = message;
            var deep_sell = Template.render("deep_sell_tpl", deepData);
            var deep_buy = Template.render("deep_buy_tpl", deepData);
            $("#deep_sell").html(deep_sell);
            $("#deep_buy").html(deep_buy);
        }

        //绑定K线时段
        $("#kLineTopic li").click(function () {
            if (ws != null) {
                lineName = $(this).attr("tips");
                var topic = $(this).attr("data");
                ws.send("{\"topic\":\"" + topic + "\"}");
            }
        });

        //绑定深度时段
        $("#deepLevel li").click(function () {
            level = $(this).attr("data");
            if(deepData != null){
                deepData.showLevel = level;
                var deep_sell = Template.render("deep_sell_tpl", deepData);
                var deep_buy = Template.render("deep_buy_tpl", deepData);
                $("#deep_sell").html(deep_sell);
                $("#deep_buy").html(deep_buy);
            }
        });

        myChart.setOption({
            tooltip: {trigger: 'axis', axisPointer: {animation: false, type: 'cross',
                lineStyle: {color: '#376df4', width: 2, opacity: 1}}},
            xAxis: {type: 'category', data: [], axisLine: {lineStyle: {color: '#8392A5'}}},
            yAxis: {scale: true, axisLine: {lineStyle: {color: '#8392A5'}}, splitLine: {show: false}},
            grid: {y: 15, y2: 30, x: 60, x2: 10},
            dataZoom: [{type: 'inside'}],
            animation: false
        });

        window.onresize = function () {
            myChart.resize();
        };

        //k线图收缩显示
        $('#C2CKlineDiv').on("click", function() {
            $('#C2CKline').fadeToggle();
            myChart.resize();
        });

        //买入
        $("#buyButton").on("click",function () {
            validator.destroy();
            validator = new Validator({
                element: '#C2Cbuyform',
                autoSubmit: false,<%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        if($("#buyButton.disabled").length>0)
                        {
                            return;
                        }
                        $("#buyButton").addClass('disabled');
                        $.ajax({
                            url: '${ctx}/spot/matchTrade/doMatchBuy',
                            type: 'post',
                            data: element.serialize(),
                             beforeSend:function(){
                                 $("#buyButton").addClass('disabled');
                             },
                            success: function (data,textStatus, jqXHR) {
                                $('#C2Cbuyform').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                data =  JSON.parse(data);
                                if (data.code == bitms.success) {
                                    remind(remindType.success, data.message,100);
                                    $("#C2Cbuyform input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                } else {
                                    if (data.object != null) {
                                        if(data.object.length == 1)
                                        {
                                            remind(remindType.error,data.message.replace('{0}',parseFloat((data.object[0])).toFixed(${money.sellPricePrecision})),1000);
                                        }
                                        else if(data.object.length == 2)
                                        {
                                            var msg = data.message.replace('{0}',parseFloat((data.object[0])).toFixed(${money.sellPricePrecision}));
                                            msg = msg.replace('{1}',parseFloat((data.object[1])).toFixed(${money.sellPricePrecision}));
                                            remind(remindType.error,msg,1000);}
                                        else {
                                            remind(remindType.error,data.message,1000);
                                        }
                                    } else {
                                        remind(remindType.error, data.message, 1000);
                                    }
                                    $("#C2Cbuyform input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                }
                                $('#index_'+tab_index).click();
                                timerFunc();
                            },
                             complete:function(){
                                 $("#buyButton").removeClass('disabled');
                             }
                        });
                    }
                }
            }).addItem({
                element: '#C2Cbuyform [name=entrustPrice]',
                required: true,
                rule:'number min{min:0.01} max{max:999999} numberOfDigits{maxLength:${money.buyPricePrecision}}'
            }).addItem({
                element: '#C2Cbuyform [name=entrustAmt]',
                required: true,
                rule:'number min{min:${money.buyMinAmount }} max{max:999999} numberOfDigits{maxLength:${money.buyAmountPrecision }}'
            }).addItem({
                element: '#C2Cbuyform [name=entrustType]',
                required: true
            });
            $("#C2Cbuyform").submit();
        });
        //卖出
        $("#sellButton").on("click",function () {
            validator.destroy();
            validator = new Validator({
                element: '#C2Csellform',
                autoSubmit: false,<%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        if($("#sellButton.disabled").length>0)
                        {
                            return ;
                        }
                        $("#sellButton").addClass('disabled');
                        $.ajax({
                            url: '${ctx}/spot/matchTrade/doMatchSell',
                            type: 'post',
                            data: element.serialize(),
                            beforeSend:function(){
                                $("#sellButton").addClass('disabled');
                            },
                            success: function (data,textStatus, jqXHR) {
                                $('#C2Csellform').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                data =  JSON.parse(data);
                                if (data.code == bitms.success) {
                                    remind(remindType.success, data.message,100);
                                   $("#C2Csellform input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                } else {
                                    {
                                        if(data.object!=null)
                                        {
                                            if(data.object.length == 1)
                                            {
                                                remind(remindType.error,data.message.replace('{0}',parseFloat((data.object[0])).toFixed(${money.sellPricePrecision})),1000);
                                            }
                                            else if(data.object.length == 2)
                                            {
                                                var msg = data.message.replace('{0}',parseFloat((data.object[0])).toFixed(${money.sellPricePrecision}));
                                                msg = msg.replace('{1}',parseFloat((data.object[1])).toFixed(${money.sellPricePrecision}));
                                                remind(remindType.error,msg,1000);}
                                            else {
                                                remind(remindType.error,data.message,1000);
                                            }
                                        }else {
                                            remind(remindType.error,data.message,1000);
                                        }
                                    }
                                    $("#C2Csellform input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                }
                                $('#index_'+tab_index).click();
                                timerFunc();
                                $("#sellButton").removeClass('disabled');
                            },
                            complete:function(){
                                $("#sellButton").removeClass('disabled');
                            }
                        });
                    }
                }
            }).addItem({
                element: '#C2Csellform [name=entrustPrice]',
                required: true,
                rule:'number min{min:0.01} max{max:999999} numberOfDigits{maxLength:${money.sellPricePrecision }}'
            }).addItem({
                element: '#C2Csellform [name=entrustAmt]',
                required: true,
                rule:'number min{min:${money.sellMinAmount}} max{max:999999} numberOfDigits{maxLength:${money.sellAmountPrecision }}'
            }).addItem({
                element: '#C2Csellform [name=entrustType]',
                required: true
            });
            $("#C2Csellform").submit();
        });
        $("#index_1").click(function (){
            tab_index=1;
            renderRecivePage = new Pagination({
                url : "${ctx}/spot/matchTrade/entrustxOnDoing",
                data:{exchangePairMoney:${exchangePairMoney},exchangePairVCoin:${exchangePairVCoin},entrustType:'<%=TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()%>'},
                rows : 100,
                method : "get",
                handleData : function(json) {
                    $("#doingCnt").text(json.total);
                    var html = Template.render("entrust_doing_list_tpl", json);
                    $("#entrustx_list_emement").html(html);
                    $("#jumpHistory").hide();
                }
            });
        });
        $("#index_2").click(function (){
            tab_index=2;
            renderRecivePage = new Pagination({
                url : "${ctx}/spot/matchTrade/entrustxOnHistory",
                data:{noPage:'yes',exchangePairMoney:${exchangePairMoney},exchangePairVCoin:${exchangePairVCoin},entrustType:'<%=TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()%>'},
                rows : 3,
                method : "get",
                handleData : function(json) {
                    var html = Template.render("entrust_done_list_tpl", json);
                    $("#entrustx_list_emement").html(html);
                    $("#jumpHistory").show();
                }
            });
        });
        getData();
    });

    //中止我发起的委托操作
    function doCancel(id){
        $.ajax({
            cache: true,
            type: 'POST',
            url: "${ctx}/spot/matchTrade/doMatchCancel",
            data:{entrustId:id,exchangePairMoney:${exchangePairMoney},exchangePairVCoin:${exchangePairVCoin}},
            async: false,
            beforeSend:function(){
                $(this).attr("disabled", true);
            },
            success: function (data,textStatus, jqXHR) {
                $('#csrf-form').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                setCsrfToken("csrf-form");
                data =  JSON.parse(data);
                if (data.code == bitms.success) {
                    remind(remindType.success,data.message,300);
                } else {
                    remind(remindType.error,data.message,300);
                }
                $("#index_1").click();
                timerFunc();
            },
            complete:function(){
                $(this).attr("disabled", false);
            }
        });
    }

    //查看成交情况
    function doView(id){
        var renderRecivePage;
        seajs.use(['pagination','template', 'moment'], function (Pagination,Template, moment) {
            renderRecivePage = new Pagination({
                url : "${ctx}/spot/matchTrade/realDealByEntrustVCoinMoneyId",
                elem : "#realdealx_pagination",
                data:{id:id,exchangePairMoney:${exchangePairMoney},exchangePairVCoin:${exchangePairVCoin}},
                rows : 10,
                method : "get",
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
                {//成交方买入 委托方是卖出方
                    return parseFloat((buyFee)).toFixed(8);
                }
                if(entrustDirect == '<%=TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()%>')
                {//成交方卖出委托方是买入方
                    return parseFloat((sellFee)).toFixed(8);
                }
                return  '--';
            });
        });

        $('#viewEntrustVCoinMoney').modal('toggle');
    }

    function timerFunc(){
        if(tab_index==1)
        {
            renderRecivePage.render();
        }
        $('.reg-pop').remove();
        getData();
    }
    function getData()
    {
        $.ajax({
            url: '${ctx}/spot/spotContractTrade/getAccountFundAsset',
            data:{exchangePairMoney:${exchangePairMoney},exchangePairVCoin:${exchangePairVCoin}},
            type: 'get',
            success: function (data,textStatus, jqXHR) {
                data =  JSON.parse(data);
                $("#btcAmtBalance").text(parseFloat(data.object.btcAmtBalance+data.object.btcBorrow).toFixed(4));
                $("#explosionPrice").text(parseFloat(data.object.explosionPrice).toFixed(2));
                $("#btcNetValue").text(parseFloat(data.object.btcNetValue).toFixed(4));
                $("#profitAndLoss").text(parseFloat(data.object.profitAndLoss).toFixed(4));

                $("#usdxAmtBalance").text(parseFloat(data.object.usdxAmtBalance+data.object.usdxBorrow).toFixed(2));
                $("#usdxAmtNetToBtc").text(parseFloat((data.object.usdxAmtBalance)/data.object.btcUsdxInnerPrice).toFixed(2));

                $("#sellEntrustAmt").attr("placeholder","<%--最大可卖--%><fmt:message key='spot.C2C.maximumAvailable' />"+parseFloat(data.object.btcSellMaxCnt).toFixed(4));
                $("#buyEntrustAmt").attr("placeholder","<%--最大可买--%><fmt:message key='spot.C2C.maximumPurchase' />"+parseFloat(data.object.btcBuyMaxCnt).toFixed(4));
                $("#btcQuota").text(parseFloat(data.object.btcSellMaxCnt).toFixed(4));
                $("#usdxQuota").text(parseFloat(data.object.btcBuyMaxCntBalance).toFixed(2));

                $("#btcFrozen").text(parseFloat(data.object.btcFrozen).toFixed(4));
                $("#usdxFrozen").text(parseFloat(data.object.usdxFrozen).toFixed(2));

                $("#btcBorrow").text(parseFloat(data.object.btcBorrow).toFixed(4));
                $("#usdxBorrow").text(parseFloat(data.object.usdxBorrow).toFixed(2));

                $("#sellBtcFeeRate").text(parseFloat(data.object.sellBtcFeeRate).toFixed(2)+'%');
                $("#buyBtcFeeRate").text(parseFloat(data.object.buyBtcFeeRate).toFixed(2)+'%');

                $("#usdxPosition").text(parseFloat(data.object.usdxPosition).toFixed(2));

                $("#usdxPositionValue").text(parseFloat(data.object.usdxAmtBalance).toFixed(2));


                $("#settlementTimeNo").text(data.object.settlementTimeNo);

                //方向
                if(parseFloat(data.object.profitAndLoss) < 0){
                    $("#profitAndLossTag").text("亏损");
                    $("#profitAndLossTag").css('background-color','#a94442');
                }
                else{
                    $("#profitAndLossTag").text("盈利");
                    $("#profitAndLossTag").css('background-color','#648241');
                }

                //方向
                if(data.object.direction == 'None'){
                    $("#direction").text("<%--无仓--%><fmt:message key='spot.C2C.none' />");
                    $("#direction").css('background-color','#777777');
                }
                else if(data.object.direction == 'Short'){
                    $("#direction").text("<%--空头--%><fmt:message key='spot.C2C.short' />");
                    $("#direction").css('background-color','#a94442');
                }
                else if(data.object.direction == 'Long'){
                    $("#direction").text("<%--多头--%><fmt:message key='spot.C2C.long' />");
                    $("#direction").css('background-color','#648241');
                }

                //风险率
                if(parseFloat(data.object.riskRate).toFixed(2) == 0){
                    $("#riskRate").text("<%--安全--%><fmt:message key='spot.C2C.security' />");
                    $("#riskRate").css('background-color','#777777');
                }
                else if(0 < parseFloat(data.object.riskRate).toFixed(2) && parseFloat(data.object.riskRate).toFixed(2) <= 0.02){
                    $("#riskRate").text("<%--低险--%><fmt:message key='spot.C2C.lowRisk' />");
                    $("#riskRate").css('background-color','#648241');
                }
                else if(0.02 < parseFloat(data.object.riskRate).toFixed(2) && parseFloat(data.object.riskRate).toFixed(2) <= 0.06){
                    $("#riskRate").text("<%--预警--%><fmt:message key='spot.C2C.warning' />");
                    $("#riskRate").css('background-color','#FFA500');
                }
                else{
                    $("#riskRate").text("<%--危险--%><fmt:message key='spot.C2C.danger' />");
                    $("#riskRate").css('background-color','#a94442');
                }
            }
        });
    }

    function getCntOfEntrusting()
    {
        $.ajax({
            url: '${ctx}/spot/matchTrade/entrustxOnDoing',
            data:{exchangePairMoney:${exchangePairMoney},exchangePairVCoin:${exchangePairVCoin}},
            type: 'get',
            success: function (data,textStatus, jqXHR) {
                data =  JSON.parse(data);
                $("#doingCnt").text(data.total);
            }
        });
    }
    $(function(){
        var t1 = window.setInterval(timerFunc,1000);
        /*window.clearTimeout(t1);  /!*去掉定时器*!/*/

        /*计算结算日期（每周五）*/
        var Days =  5 - new Date().getDay();
        if(Days < 0){
            $('#Days').text(7+Days);
        }
        else{
            $('#Days').text(Days);
        }

        /*净值弹出框*/
        $("[data-toggle='popover']").popover({
            html : true
        });

    });

    //回车键绑定登录按钮
    function buyKeyDown() {
        var theEvent = window.event || arguments.callee.caller.arguments[0];
        var code = theEvent.keyCode;
        if (code== 13) {
            theEvent.returnValue = false;
            theEvent.cancel = true;
            $('#buyButton').click();
        }
    }
    function sellKeyDown() {
        var theEvent = window.event || arguments.callee.caller.arguments[0];
        var code = theEvent.keyCode;
        if (code== 13) {
            theEvent.returnValue = false;
            theEvent.cancel = true;
            $('#sellButton').click();
        }
    }

    //自动填充
    function autoWriteBuy(price,amt){
        $("#priceBuy").val(price);
        $("#buyEntrustAmt").val(amt);
    }
    function autoWriteSell(price,amt){
        $("#priceSell").val(price);
        $("#sellEntrustAmt").val(amt);
    }

    function jumpUrl(obj){
        $("#jumpId").val(obj.id);
        document.getElementById("jumpForm").action="/spot/spotContractTrade?type="+$(obj).attr('remark');
        $("#jumpForm").submit();
    }
</script>
</html>