<%@ page import="com.blocain.bitms.trade.trade.enums.TradeEnums" %>
<%@ page import="com.blocain.bitms.quotation.consts.InQuotationConsts" %>
<%@ page import="com.blocain.bitms.tools.consts.BitmsConst" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-xs-12" style="padding:0px;">
            <div class="col-sm-2 col-xs-12 hidden-xs">
                <table class="table table-condensed table-hover" style="font-size:13px;margin-bottom:10px;border-bottom:1px solid #ddd;">
                    <thead>
                        <tr>
                            <th class="text-center" style="background-color:#e8e8e8;width:25%">Pair</th>
                            <th class="text-center" style="background-color:#e8e8e8;width:50%">Price</th>
                            <th class="text-center" style="background-color:#e8e8e8;width:25%">Change</th>
                        </tr>
                    </thead>
                    <tbody id="transPair">
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                    </tbody>
                </table>
                <div class="spinner" id="spinnerQt">
                    <div class="rect1"></div>
                    <div class="rect2"></div>
                    <div class="rect3"></div>
                    <div class="rect4"></div>
                    <div class="rect5"></div>
                </div>
            </div>
            <div class="col-sm-7 col-xs-12 padding-xs-none">
                <div class="clearfix" style="position:relative;">
                    <c:choose>
                        <c:when test="${isActive == 'no' || isActive == ''}">
                            <div class="text-center" style="position:absolute;width:100%;height:98%;background:rgba(0,0,0,0.2);z-index:5;">
                                <span onclick="jumpUrl('/exchange/activeToken?contractAddr=${ERC20Contract}')" style="padding:2px;border-radius:2px;font-size:45px;color:#fff;cursor:pointer;margin-top:120px;display:inline-block;">
                                    <span>${symbol}<sup><c:if test="${ERC20Contract != null && ERC20Contract != ''}" >${ERC20Contract.substring(40,42)}</c:if></sup></span>
                                    <br>
                                    <span class="glyphicon glyphicon-lock"></span>
                                    <span>Unactivated</span>
                                </span>
                            </div>
                        </c:when>
                        <c:otherwise>
                        </c:otherwise>
                    </c:choose>
                    <div class="col-xs-6 deep-xs-hidden" style="padding:0;">
                        <div class="text-center" style="position:absolute;width:100%;height:100%;opacity:0.1;z-index:-1;">
                            <img style="width:85%;height:85%;margin-top:30px" src="${imagesPath}/tokenLogo/${ERC20Contract}.svg" onerror="this.src='${imagesPath}/tokenLogo/token.svg'">
                        </div>
                        <table class="table table-condensed" style="font-size:13px;margin-bottom:12px;position:absolute;margin-top:28px;opacity:0.7;z-index:-1;transform:scaleX(-1);">
                            <tbody id="entBalRatioBuy">

                            </tbody>
                        </table>
                        <table class="table table-condensed" style="font-size:13px;margin-bottom: 12px;border-bottom:1px solid #ddd;">
                            <thead>
                            <tr>
                                <th class="hidden-xs" style="background-color:#e8e8e8;width:33%">SUM(ETH)</th>
                                <th style="background-color:#e8e8e8;width:33%">${symbol}<sup><c:if test="${ERC20Contract != null && ERC20Contract != ''}" >${ERC20Contract.substring(40,42)}</c:if></sup></th>
                                <th style="background-color:#e8e8e8;width:33%">BID(ETH)</th>
                            </tr>
                            </thead>
                            <tbody id="deepBuy">
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            <tr>
                                <td class="hidden-xs">--</td>
                                <td>--</td>
                                <td>--</td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="spinner" id="spinnerBid">
                            <div class="rect1"></div>
                            <div class="rect2"></div>
                            <div class="rect3"></div>
                            <div class="rect4"></div>
                            <div class="rect5"></div>
                        </div>
                    </div>
                    <div class="col-xs-6 deep-xs-hidden" style="padding:0;">
                        <div class="text-center" style="position:absolute;width:100%;height:100%;opacity:0.1;z-index:-1;">
                            <img style="width:85%;height:85%;margin-top:30px" src="${imagesPath}/tokenLogo/eth.svg">
                        </div>
                        <table class="table table-condensed" style="font-size:13px;margin-bottom:12px;position:absolute;margin-top:28px;opacity:0.7;z-index:-1;">
                            <tbody id="entBalRatioSell">

                            </tbody>
                        </table>
                        <table class="table table-condensed" style="font-size:13px;margin-bottom:12px;border-bottom:1px solid #ddd;">
                            <thead>
                            <tr>
                                <th style="background-color:#e8e8e8;width:33%">ASK(ETH)</th>
                                <th style="background-color:#e8e8e8;width:33%">${symbol}<sup><c:if test="${ERC20Contract != null && ERC20Contract != ''}" >${ERC20Contract.substring(40,42)}</c:if></sup></th>
                                <th class="hidden-xs" style="background-color:#e8e8e8;width:33%">SUM(ETH)</th>
                            </tr>
                            </thead>
                            <tbody id="deepSell">
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            <tr>
                                <td>--</td>
                                <td>--</td>
                                <td class="hidden-xs">--</td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="spinner" id="spinnerAsk">
                            <div class="rect1"></div>
                            <div class="rect2"></div>
                            <div class="rect3"></div>
                            <div class="rect4"></div>
                            <div class="rect5"></div>
                        </div>
                    </div>
                    <div class="col-xs-12" style="padding:0;margin-bottom:10px;">
                        <div class="clearfix" style="background-color:#eee;padding:8px;border-radius:4px;">
                            <form:form id="tradeForm">
                                <input name="exchangePairMoney" type="hidden" value="${stockInfo.id}" class="form-control">
                                <input name="entrustType" type="hidden" value="<%=TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()%>" class="form-control">
                                <div class="col-sm-3 col-xs-6" style="padding-left:4px;padding-right:4px;float:left;margin-bottom:10px;">
                                    <div class="input-group">
                                        <input type="text" id="price" class="form-control" name="entrustPrice" placeholder="Price" aria-describedby="sizing-addon1">
                                        <span class="input-group-addon">ETH</span>
                                    </div>
                                </div>
                                <div class="col-sm-3 col-xs-6" style="padding-left:4px;padding-right:4px;float:left;margin-bottom:10px;">
                                    <div class="input-group">
                                        <input type="text" id="amount"  onkeyup="if(/[^\d]/.test(this.value)){alert('Only Accept Integer!');this.value='';}" class="form-control" name="entrustAmt" placeholder="Amount" aria-describedby="sizing-addon1">
                                        <span class="input-group-addon">${symbol}<sup><c:if test="${ERC20Contract != null && ERC20Contract != ''}" >${ERC20Contract.substring(40,42)}</c:if></sup></span>
                                    </div>
                                </div>
                                <div class="col-sm-3 col-xs-6 hidden-xs" style="padding-left:4px;padding-right:4px;float:left;margin-bottom:10px;">
                                    <div class="input-group">
                                        <input type="text" id="total" class="form-control" name="total" placeholder="Total" aria-describedby="sizing-addon1">
                                        <span class="input-group-addon">ETH</span>
                                    </div>
                                </div>
                                <div class="col-sm-1 col-xs-6" style="padding-left:4px;padding-right:4px;float:right;margin-bottom:10px;">
                                    <div id="sellButton" style="background-image:none;box-shadow:none" class="btn btn-danger btn-block" <c:if test="${longStatus == false}" >disabled</c:if>>SELL</div>
                                </div>
                                <div class="col-sm-1 col-xs-6" style="padding-left:4px;padding-right:4px;float:right;margin-bottom:10px;">
                                    <div id="buyButton" style="background-image:none;box-shadow:none" class="btn btn-success btn-block" <c:if test="${longStatus == false}" >disabled</c:if>>BUY</div>
                                </div>
                            </form:form>
                            <c:choose>
                                <c:when test="${longStatus == true}">
                                    <div class="col-sm-4 col-xs-6" style="padding:0;font-size:12px;">
                                    <span class="text-success" style="font-weight: bold">
                                        <span>ETH Available：</span>
                                        <span id="usdxAmtBalance"><fmt:formatNumber type="number" value="${ethEnable.enableAmount}" pattern="#,##0.0000" maxFractionDigits="4"/></span>
                                    </span>
                                    </div>
                                    <div class="col-sm-4 col-xs-6 text-center" style="padding:0;font-size:12px;">
                                    <span class="text-danger" style="font-weight: bold">
                                        <span>${symbol} Available：</span>
                                        <span id="btcAmtBalance">0.0000</span>
                                    </span>
                                    </div>
                                    <div class="col-sm-4 col-xs-12 text-right hidden-xs" style="padding:0;font-size:12px;">
                                        <a onMouseOver="this.style.textDecoration='none'" onclick="jumpUrl('${ctx}/exchange/billHistory?contractAddr=${ERC20Contract}')" style="cursor:pointer;padding-right:5px;border-right:1px solid;">Bill History</a>
                                        <a onMouseOver="this.style.textDecoration='none'" onclick="jumpUrl('${ctx}/exchange/orderHistory?contractAddr=${ERC20Contract}')" style="cursor:pointer;">Order History</a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-xs-12" style="padding:0;margin-top:-5px;font-size:12px;">
                                    <span style="line-height:21px;">
                                        <a href="/login" style="cursor:pointer;">Log in</a>
                                        <span>or</span>
                                        <a href="/register" style="cursor:pointer;">Sign up</a>
                                        <span>to trade.</span>
                                    </span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-3 col-xs-12 hidden-xs">
                <table class="table table-condensed" style="font-size:13px;margin-bottom:10px;border-bottom:1px solid #ddd;">
                    <thead>
                        <tr>
                            <th class="text-center" style="background-color:#e8e8e8;width:20%">DATE</th>
                            <th class="text-center" style="background-color:#e8e8e8;width:30%">PRICE(ETH)</th>
                            <th class="text-center" style="background-color:#e8e8e8;width:30%">AMOUNT(${symbol})</th>
                            <th class="text-center" style="background-color:#e8e8e8;width:20%">TYPE</th>
                        </tr>
                    </thead>
                    <tbody id="trading">
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                        <tr>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                            <td class="text-center">--</td>
                        </tr>
                    </tbody>
                </table>
                <div class="spinner" id="spinnerDeal">
                    <div class="rect1"></div>
                    <div class="rect2"></div>
                    <div class="rect3"></div>
                    <div class="rect4"></div>
                    <div class="rect5"></div>
                </div>
            </div>
        </div>
        <div class="col-xs-12 padding-xs-none">
            <div class="table-responsive">
                <%--<h4>Open Orders</h4>--%>
                <table class="table table-condensed table-bordered " style="font-size:13px;margin-bottom:5px;">
                    <thead>
                    <tr>
                        <th class="text-center hidden-xs" style="background-color:#e8e8e8;width:20%;">Date</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:10%;">Side</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%;">Price</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%;">Executed/Amount</th>
                        <th class="text-center hidden-xs" style="background-color:#e8e8e8;width:20%;">Average Price</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:10%;">Operation</th>
                    </tr>
                    </thead>
                    <tbody id="openOrder" class="text-center">

                    </tbody>
                </table>
                <div class="col-xs-12 text-center" id="listTip">
                    <span class="glyphicon glyphicon-list-alt"></span>
                    <span>You have no open order</span>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>

<script id="openOrder_list_tpl" type="text/html">
    {{each rows}}
        <tr class="test">
            <td class="hidden-xs">{{$formatDateTime $value.entrustTime}}</td>
            <td>{{$dictionaryFlag $value.entrustDirect}}</td>
            <td>{{$Fix8Flag $value.entrustPrice}}</td>
            <td>{{$Fix4Flag $value.dealAmt}}/{{$Fix4Flag $value.entrustAmt}}</td>
            <td class="hidden-xs">{{$avgFlag $value.entrustDirect,$value.dealAmt,$value.dealBalance,$value.dealFee}}</td>
            <td>{{$cancalActionFlag $value.id}}</td>
        </tr>
    {{/each}}
</script>

<script id="trading_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.index < 13}}
        <tr>
            <td class="text-center">{{$formatDate $value.dealTime}}</td>
            <td class="text-center">{{$Fix8Flag $value.dealPrice}}</td>
            <td class="text-center">{{$Fix8Flag $value.dealAmt}}</td>
            <td class="text-center">{{$value.direct=='spotSell'?'Sell':'Buy'}}</td>
        </tr>
    {{/if}}
    {{/each}}
</script>
<script id="trading_none_tpl" type="text/html">
    <tr>
        <td class="text-center">--</td>
        <td class="text-center">--</td>
        <td class="text-center">--</td>
        <td class="text-center">--</td>
    </tr>
</script>

<script id="deep_buy_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.direct == 'spotBuy' && showLevel == $value.deepLevel}}
        <tr>
            <td class="hidden-xs">{{$SumFlag $value.entrustAmtSum $value.entrustPrice}}</td>
            <td>{{$Fix4Flag $value.entrustAmt}}</td>
            <td>{{$Fix8Flag $value.entrustPrice}}</td>
        </tr>
    {{/if}}
    {{/each}}
</script>
<script id="entBalRatio_buy_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.direct == 'spotBuy' && showLevel == $value.deepLevel}}
        <tr>
            <td colspan="3" style="position:relative;">
                <div style="background-color:#5cb85c;width:{{$value.entBalRatio}}%;border-radius:2px;">&nbsp;</div>
            </td>
        </tr>
    {{/if}}
    {{/each}}
</script>
<script id="deep_sell_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.direct == 'spotSell' && showLevel == $value.deepLevel}}
        <tr>
            <td>{{$Fix8Flag $value.entrustPrice}}</td>
            <td>{{$Fix4Flag $value.entrustAmt}}</td>
            <td class="hidden-xs">{{$SumFlag $value.entrustAmtSum $value.entrustPrice}}</td>
        </tr>
    {{/if}}
    {{/each}}
</script>
<script id="entBalRatio_sell_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.direct == 'spotSell' && showLevel == $value.deepLevel}}
        <tr>
            <td colspan="3" style="position:relative;">
                <div style="background-color:#d9534f;width:{{$value.entBalRatio}}%;border-radius:2px;">&nbsp;</div>
            </td>
        </tr>
    {{/if}}
    {{/each}}
</script>
<script id="deep_none_tpl" type="text/html">
    <tr>
        <td>--</td>
        <td class="hidden-xs">--</td>
        <td>--</td>
    </tr>
</script>

<script id="transPair_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.stockType == 'pureSpot' && $value.index < 13 }}
        <tr onclick="jumpUrl('${ctx}/exchange?contractAddr={{$value.tokencontactaddr}}')" style="cursor:pointer;">
            <td class="text-center">{{$FixStrFlag $value.stockName}}<sup>{{$supFlag $value.tokencontactaddr}}</sup></td>
            <td class="text-center">{{$Fix8Flag $value.platPrice}}</td>
            <td class="text-center {{$value.upDown=='DOWN'?'text-danger':'text-success'}}">{{$Fix2Flag $value.range}}%<span style="vertical-align:text-bottom;">{{$value.upDown=='DOWN'?'↓':'↑'}}</span></td>
        </tr>
    {{/if}}
    {{/each}}
</script>
<script id="transPair_none_tpl" type="text/html">
    <tr>
        <td class="text-center">--</td>
        <td class="text-center">--</td>
        <td class="text-center">--</td>
    </tr>
</script>

<script>
    var flag_deal = false;
    var flag_deep = false;
    var flag_allQuotation = false;

    var deepTopic = "${stockInfo.topicEntrustDeepprice}";
    var dealTopic = "${stockInfo.topicRealdealTransaction}";
    var allQuotationTopic = "allRtQuotation";

    var reader = {
        readAs: function (type, blob, cb) {
            var r = new FileReader();
            r.onloadend = function () {
                if (typeof(cb) === 'function') {
                    cb.call(r, r.result);
                }
            };
            try {
                r['readAs' + type](blob);
            } catch (e) {
            }
        }
    };

    var level = 0;
    var openOrder;
    seajs.use(['pagination', 'validator', 'template', 'moment', 'qrcode', 'sockjs', 'reconnection'], function (Pagination, Validator, Template, moment, qrcode) {

        validator = new Validator();

        template.helper('$formatDate', function (millsec) {
            return moment(millsec).format("HH:mm:ss");
        });

        template.helper('$formatDateTime', function (millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss.SSS");
        });
        template.helper('$dictionaryFlag', function (flag) {
            return getDictValueByCode(flag);
        });
        template.helper('$Fix2Flag', function (flag) {
            return comdify(parseFloat((flag)).toFixed(2));
        });
        template.helper('$Fix4Flag', function (flag) {
            return comdify(parseFloat((flag)).toFixed(4));
        });
        template.helper('$Fix8Flag', function (flag) {
            return comdify(parseFloat((flag)).toFixed(8));
        });
        template.helper('$avgFlag', function (direact, dealAmt, dealBalance, fee) {
            if (direact == '<%=TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()%>') {
                if (dealAmt > 0) {
                    return comdify((parseFloat(dealBalance) / parseFloat(dealAmt)).toFixed(2));
                }
                else {
                    return "-";
                }
            }
            else if (direact == '<%=TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()%>') {
                if (dealAmt > 0) {
                    return comdify((parseFloat(dealBalance) / parseFloat(dealAmt)).toFixed(2));
                }
                else {
                    return "-";
                }
            }

        });
        template.helper('$statusFlag', function (flag) {
            return getDictValueByCode(flag);
        });
        template.helper('$FixStrFlag', function (flag) {
            return flag.substring(0,flag.indexOf('2'));
        });
        template.helper('$cancalActionFlag', function (id) {
            return "<a class='text-success' style='cursor:pointer;' onclick=\"doCancel('" + id + "')\">Cancel</a>";
        });
        template.helper('$supFlag', function (flag) {
            if(flag != null && flag != ''){
                return flag.substr(40,2);
            }
        });
        template.helper('$SumFlag', function (flag,flag2) {
            return comdify(parseFloat((flag*flag2)).toFixed(4));
        });

        <%--买入--%>
        $("#buyButton").on("click", function () {
            <c:if test="${!empty stockInfo && longStatus == true}">
                var minbuyprice = (1 / (Math.pow(10, ${stockInfo.buyPricePrecision}))).toFixed(${stockInfo.buyPricePrecision}).toString();
                var minbuyamt = (1 / (Math.pow(10, ${stockInfo.buyAmountPrecision}))).toFixed(${stockInfo.buyAmountPrecision}).toString();
                validator.destroy();
                validator = new Validator({
                    element: '#tradeForm',
                    autoSubmit: false, <%--当验证通过后不自动提交--%>
                    onFormValidated: function (error, results, element) {
                        if (!error) {
                            <c:if test="${stockInfo.stockCode == 'BIEX2ETH'}">
                                confirmDialog("<h3 class='text-danger' style='margin-top:0;'><span class='glyphicon glyphicon-warning-sign'></span><span>Risk Warning<span></h3><p style='display:block;max-width:600px;text-align:left;'>BIEX is still in a BETA stage. Its purpose is to create a highly efficient and safe decentralized digital asset exchange environment, but currently it is still in the first stage and it remains a huge unknown whether its goal will be fulfilled. You should be well-aware of this.In the case of a failure of the whole project, it will completely lose its value. You should be well-aware of this fact. Having given the above warnings, we strongly discourage obtaining BIEX for the purpose of speculation and not just for the sake of activating of a trading pair.</p>",function () {
                            </c:if>
                                $.ajax({
                                    url: '${ctx}/spot/matchTrade/doMatchBuy',
                                    type: 'post',
                                    data: element.serialize(),
                                    beforeSend: function () {
                                        $("#sellButton").attr("disabled", true);
                                        $("#buyButton").attr("disabled", true);
                                    },
                                    success: function (data, textStatus, jqXHR) {
                                        $('#tradeForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                        data = JSON.parse(data);
                                        if (data.code == bitms.success) {
                                            remind(remindType.success, data.message, 1000);
                                            $("#tradeForm input").not(':button, :submit, :reset, :hidden').each(function () {
                                                $(this).val('');
                                            });
                                            <%--获取可用余额、委托单数据--%>
                                            timerFunc();
                                            window.setTimeout(function(){timerFunc();},2000);
                                        } else {
                                            if (data.object != null) {
                                                if (data.object.length == 1) {
                                                    remind(remindType.error, data.message.replace('{0}', parseFloat((data.object[0])).toFixed(${stockInfo.sellPricePrecision})), 2000);
                                                }
                                                else if (data.object.length == 2) {
                                                    var msg = data.message.replace('{0}', parseFloat((data.object[0])).toFixed(${stockInfo.sellPricePrecision}));
                                                    msg = msg.replace('{1}', parseFloat((data.object[1])).toFixed(${stockInfo.sellPricePrecision}));
                                                    remind(remindType.error, msg, 2000);
                                                }
                                                else {
                                                    remind(remindType.error, data.message, 2000);
                                                }
                                            } else {
                                                remind(remindType.error, data.message, 2000);
                                            }
                                            $("#tradeForm input").not(':button, :submit, :reset, :hidden').each(function () {
                                                $(this).val('');
                                            });
                                        }
                                    },
                                    complete: function () {
                                        $("#sellButton").attr("disabled", false);
                                        $("#buyButton").attr("disabled", false);
                                    }
                                });
                        <c:if test="${stockInfo.stockCode == 'BIEX2ETH'}">
                            });
                        </c:if>
                        }
                    }
                }).addItem({
                    element: '#tradeForm [name=entrustPrice]',
                    required: true,
                    rule: 'number min{min:' + minbuyprice + '} max{max:999999} numberOfDigits{maxLength:${stockInfo.buyPricePrecision}}'
                }).addItem({
                    element: '#tradeForm [name=entrustAmt]',
                    required: true,
                    rule: 'number min{min:' + minbuyamt + '} max{max:999999} numberOfDigits{maxLength:${stockInfo.buyAmountPrecision}}'
                }).addItem({
                    element: '#tradeForm [name=entrustType]',
                    required: true
                });
                $("#tradeForm").submit();
            </c:if>
        });

        <%--卖出--%>
        $("#sellButton").on("click", function () {
            <c:if test="${!empty stockInfo && longStatus == true}">
                var minsellprice = (1 / (Math.pow(10, ${stockInfo.sellPricePrecision}))).toFixed(${stockInfo.sellPricePrecision}).toString();
                var minsellamt = (1 / (Math.pow(10, ${stockInfo.sellAmountPrecision}))).toFixed(${stockInfo.sellAmountPrecision}).toString();
                validator.destroy();
                validator = new Validator({
                    element: '#tradeForm',
                    autoSubmit: false, <%--当验证通过后不自动提交--%>
                    onFormValidated: function (error, results, element) {
                        if (!error) {
                            <c:if test="${stockInfo.stockCode == 'BIEX2ETH'}">
                                confirmDialog("<h3 class='text-danger' style='margin-top:0;'><span class='glyphicon glyphicon-warning-sign'></span><span>Risk Warning<span></h3><p style='display:block;max-width:600px;text-align:left;'>BIEX is still in a BETA stage. Its purpose is to create a highly efficient and safe decentralized digital asset exchange environment, but currently it is still in the first stage and it remains a huge unknown whether its goal will be fulfilled. You should be well-aware of this.In the case of a failure of the whole project, it will completely lose its value. You should be well-aware of this fact. Having given the above warnings, we strongly discourage obtaining BIEX for the purpose of speculation and not just for the sake of activating of a trading pair.</p>",function () {
                            </c:if>
                                $.ajax({
                                    url: '${ctx}/spot/matchTrade/doMatchSell',
                                    type: 'post',
                                    data: element.serialize(),
                                    beforeSend: function () {
                                        $("#sellButton").attr("disabled", true);
                                        $("#buyButton").attr("disabled", true);
                                    },
                                    success: function (data, textStatus, jqXHR) {
                                        $('#tradeForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                        data = JSON.parse(data);
                                        if (data.code == bitms.success) {
                                            remind(remindType.success, data.message, 1000);
                                            $("#tradeForm input").not(':button, :submit, :reset, :hidden').each(function () {
                                                $(this).val('');
                                            });
                                            <%--获取可用余额、委托单数据--%>
                                            timerFunc();
                                            window.setTimeout(function(){timerFunc();},2000);
                                        } else {
                                            {
                                                if (data.object != null) {
                                                    if (data.object.length == 1) {
                                                        remind(remindType.error, data.message.replace('{0}', parseFloat((data.object[0])).toFixed(${stockInfo.sellPricePrecision})), 2000);
                                                    }
                                                    else if (data.object.length == 2) {
                                                        var msg = data.message.replace('{0}', parseFloat((data.object[0])).toFixed(${stockInfo.sellPricePrecision}));
                                                        msg = msg.replace('{1}', parseFloat((data.object[1])).toFixed(${stockInfo.sellPricePrecision}));
                                                        remind(remindType.error, msg, 2000);
                                                    }
                                                    else {
                                                        remind(remindType.error, data.message, 2000);
                                                    }
                                                } else {
                                                    remind(remindType.error, data.message, 2000);
                                                }
                                            }
                                            $("#tradeForm input").not(':button, :submit, :reset, :hidden').each(function () {
                                                $(this).val('');
                                            });
                                        }
                                    },
                                    complete: function () {
                                        $("#sellButton").attr("disabled", false);
                                        $("#buyButton").attr("disabled", false);
                                    }
                                });
                        <c:if test="${stockInfo.stockCode == 'BIEX2ETH'}">
                            });
                        </c:if>
                        }
                    }
                }).addItem({
                    element: '#tradeForm [name=entrustPrice]',
                    required: true,
                    rule: 'number min{min:' + minsellprice + '} max{max:999999} numberOfDigits{maxLength:${stockInfo.sellPricePrecision}}'
                }).addItem({
                    element: '#tradeForm [name=entrustAmt]',
                    required: true,
                    rule: 'number min{min:' + minsellamt + '} max{max:999999} numberOfDigits{maxLength:${stockInfo.sellAmountPrecision}}'
                }).addItem({
                    element: '#tradeForm [name=entrustType]',
                    required: true
                });
                $("#tradeForm").submit();
            </c:if>
        });

        <%--输入框--%>
        $('#price').on("keyup",function () {
            if($(this).val() == '' || $('#amount').val() == ''){
                $("#total").val("")
            }
            else{
                $("#total").val(($(this).val() * $('#amount').val()).toFixed(8));
            }
        });
        $('#amount').on("keyup",function () {
            if($(this).val() == '' || $('#price').val() == ''){
                $("#total").val("")
            }
            else{
                $("#total").val(($(this).val() * $('#price').val()).toFixed(8));
            }
        });
        $('#total').on("keyup",function () {
            if($(this).val() == '' || $('#price').val() == ''){
                $("#amount").val("")
            }
            else{
                $("#amount").val(($(this).val() / $('#price').val()).toFixed(0));
            }
        });

        <c:if test="${empty stockInfo}">
            $('.spinner').hide();
        </c:if>
        <c:if test="${!empty stockInfo}">

            <%--获取实时行情--%>
            <% if (BitmsConst.RUNNING_ENVIRONMONT.equals("production")){ %>
            if (window.location.protocol == 'http:') {
                url = 'ws://socket.biex.com' + "${stockInfo.webSocketUrl}";
            } else {
                url = 'wss://socket.biex.com' + "${stockInfo.webSocketUrl}";
            }
            <% }else{ %>
            if (window.location.protocol == 'http:') {
                url = 'ws://' + window.location.host + "${stockInfo.webSocketUrl}";
            } else {
                url = 'wss://' + window.location.host + "${stockInfo.webSocketUrl}";
            }
            <% } %>

            if ('WebSocket' in window) {
                ws = new ReconnectingWebSocket(url);
            } else {
                ws = new SockJS(url);
            }

            ws.onmessage = function (event) {
                var blob = event.data;
                reader.readAs('Text', blob.slice(0, blob.size, 'text/plain;charset=UTF-8'), function (result) {
                    var message = JSON.parse(result);
                    var msgType = message.msgType;
                    //console.log(message);
                    if (msgType == '<%=InQuotationConsts.MESSAGE_TYPE_REALDEAL%>') {
                        <%--deal--%>
                        renderDeal(message);
                        flag_deal = true;
                    }
                    else if (msgType == '<%=InQuotationConsts.MESSAGE_TYPE_DEEPPRICE%>') {
                        <%--deep--%>
                        renderDeep(message);
                        flag_deep = true;
                    }
                    else if (msgType == '<%=InQuotationConsts.MESSAGE_TYPE_ALLRTQUOTATION%>') {
                        <%--wsAllRtQuotation--%>
                        renderAllRtQuotation(message);
                        flag_allQuotation = true;
                    }
                });
            };

            //轮询行情是否为空
            window.setInterval(function () {
                if (! flag_deal) {
                    ws.send("{\"topic\":\"" + dealTopic + "\"}");
                }
                if (! flag_deep) {
                    ws.send("{\"topic\":\"" + deepTopic + "\"}");
                }
                if (! flag_allQuotation) {
                    ws.send("{\"topic\":\"" + allQuotationTopic + "\"}");
                }
            }, 3000);

        </c:if>

        function renderAllRtQuotation(message) {
            for(var n=0;n<message.msgContent.length;n++)
            {
                message.msgContent[n].index = n;
            }
            var html = Template.render("transPair_tpl", message);
            var AllRtQuotation = 0;
            for(var i=0;i<message.msgContent.length;i++)
            {
                AllRtQuotation++;
            }
            for(var j=0;j<13-AllRtQuotation;j++){
                html += Template.render("transPair_none_tpl");
            }
            $("#spinnerQt").hide();
            $("#transPair").html(html);
        }

        function renderDeal(message) {
            for(var n=0;n<message.msgContent.length;n++)
            {
                message.msgContent[n].index = n;
            }
            var html = Template.render("trading_tpl", message);
            var deal = 0;
            for(var i=0;i<message.msgContent.length;i++)
            {
                deal++;
            }
            for(var j=0;j<13-deal;j++){
                html += Template.render("trading_none_tpl");
            }
            $("#spinnerDeal").hide();
            $("#trading").html(html);

            <%--成交后刷新委托数据--%>
            timerFunc();
        }

        function renderDeep(message) {
            message.showLevel = level;
            deepData = message;
            var deep_buy = Template.render("deep_buy_tpl", deepData);
            var deep_sell = Template.render("deep_sell_tpl", deepData);
            var entBalRatio_buy = Template.render("entBalRatio_buy_tpl", deepData);
            var entBalRatio_sell = Template.render("entBalRatio_sell_tpl", deepData);
            var cntBuy = 0;
            var cntSell = 0;
            for(var i=0;i<deepData.msgContent.length;i++)
            {
                if(deepData.msgContent[i].direct == 'spotBuy')
                {
                    cntBuy++;
                }
                else{
                    cntSell++;
                }
            }
            for(var j=0;j<10-cntBuy;j++){
                deep_buy += Template.render("deep_none_tpl");
            }
            for(var j=0;j<10-cntSell;j++){
                deep_sell += Template.render("deep_none_tpl");
            }
            $("#spinnerBid").hide();
            $("#spinnerAsk").hide();
            $("#deepBuy").html(deep_buy);
            $("#deepSell").html(deep_sell);
            $("#entBalRatioBuy").html(entBalRatio_buy);
            $("#entBalRatioSell").html(entBalRatio_sell);
        }

        if(${longStatus == true} && ${!empty stockInfo}) {

            <%--获取可用余额、委托单数据--%>
            openOrder = new Pagination({
                url: "${ctx}/spot/matchTrade/entrustxOnDoing",
                data: {
                    exchangePairMoney: '${stockInfo.id}',
                    entrustType: '<%=TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()%>'
                },
                rows: 100,
                method: "get",
                handleData: function (json) {
                    var html = Template.render("openOrder_list_tpl", json);
                    $("#openOrder").html(html);

                    <%--判断图标是否显示--%>
                    if(json.rows.length >0){
                        $("#listTip").hide();
                    }else {
                        $("#listTip").show();
                    }
                }
            });
            getData();

        }
    });

    function timerFunc() {
        if(${longStatus == true} && ${!empty stockInfo})
        {
            openOrder.render();
            getData();
        }
    }

    <%--获取可用--%>
    function getData() {
        $("#total").val("");
        <c:if test="${!empty stockInfo.remark}">
        $.ajax({
            url: '${ctx}/spot/pureSpotTrade/getAccountFundAsset',
            data: {pair:"${stockInfo.remark}"},
            type: 'get',
            success: function (data, textStatus, jqXHR) {
                data = JSON.parse(data);
                $("#btcAmtBalance").text(comdify(parseFloat(data.object.btcAmount - data.object.btcFrozen).toFixed(4)));
                $("#usdxAmtBalance").text(comdify(parseFloat(data.object.usdxAmount - data.object.usdxFrozen).toFixed(4)));
            }
        });
        </c:if>
    }

    <%--中止我发起的委托操作--%>
    function doCancel(id) {
        $.ajax({
            cache: true,
            type: 'POST',
            url: "${ctx}/spot/matchTrade/doMatchCancel",
            data: {entrustId: id, exchangePairMoney: '${stockInfo.id}'},
            async: false,
            beforeSend: function () {
                $(this).attr("disabled", true);
            },
            success: function (data, textStatus, jqXHR) {
                $('#csrf-form').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                setCsrfToken("csrf-form");
                data = JSON.parse(data);
                if (data.code == bitms.success) {
                    remind(remindType.success, data.message, 1000);
                } else {
                    remind(remindType.error, data.message, 2000);
                }
                timerFunc();
                window.setTimeout(function(){timerFunc();},2000);
            },
            complete: function () {
                $(this).attr("disabled", false);
            }
        });
    }

</script>
</body>
</html>