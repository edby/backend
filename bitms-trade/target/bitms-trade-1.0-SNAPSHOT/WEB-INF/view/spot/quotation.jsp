<%@ page import="com.blocain.bitms.quotation.consts.InQuotationConsts" %>
<%@ page import="com.blocain.bitms.tools.consts.BitmsConst" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<body>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp" %>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <%--<div class="col-sm-2 column">
            <ul class="nav nav-tabs nav-stacked">
                <li class="active">
                    <a href="${ctx}/spot/C2CTrade">
                        <span class="glyphicon glyphicon-refresh"></span>
                        <span>BTC交易</span>
                    </a>
                </li>
                <li>
                    <a href="${ctx}/spot/entrust">
                        <span class="glyphicon glyphicon-floppy-open"></span>
                        <span>历史委托</span>
                    </a>
                </li>
            </ul>
        </div>--%>
        <div class="col-sm-12 column">
            <div class="panel clearfix">
                <div class="col-lg-12 clearfix pl5">
                    <div class="bitms-bg1 clearfix">
                        <div class="col-sm-6 C2Ctable mt10 mb10 pr30" style="border-right: 1px solid #22333f;">
                            <div class="thead tr clearfix">
                                <span class="col-xs-3 bitms-c2" style="text-align: center">买卖</span>
                                <span class="col-xs-3 bitms-c2">价格($)</span>
                                <span class="col-xs-3 bitms-c2">数量(฿)</span>
                                <span class="col-xs-3 bitms-c2">累计(฿)</span>
                            </div>
                            <div id="deep_sell">

                            </div>
                            <hr>
                            <div id="deep_buy">

                            </div>
                            <div id="quotation" class="clearfix mt10 mb10 ml20 dataTxt">
                                <div class="pull-left">
                                    <span class="bitms-c2">最新成交价:</span>
                                    <span class="text-success">$0000.00</span>
                                </div>
                                <div class="pull-right bitms-c2">
                                    <span>指数:</span>
                                    <span class="text-danger">0000.00</span>
                                </div>
                            </div>
                            <%--<div class="clearfix mt10 mb10 ml20 dataTxt">
                                <ul id="deepLevel" class="nav nav-tabs bitms-tabs bitms-depth-tabs" style="display: inline-block">
                                    <span class="bitms-c2 pull-left">深度:</span>
                                    <li data="0" class="active"><a data-toggle="tab">0</a></li>
                                    <li data="1"><a data-toggle="tab">1</a></li>
                                    &lt;%&ndash; <li data="2"><a data-toggle="tab">2</a></li>
                                    <li data="3"><a data-toggle="tab">3</a></li>
                                    <li data="4"><a data-toggle="tab">4</a></li>
                                    <li data="5"><a data-toggle="tab">5</a></li>&ndash;%&gt;
                                </ul>
                                <span class="bitms-c2 pull-right">
                                        <span>距离结算时间</span>
                                        <span class="text-danger" id="Days">7</span>
                                        <span>天</span>
                                    </span>
                            </div>--%>
                        </div>
                        <div class="col-sm-6 C2Ctable mt10 mb10 pre-scrollable" style="max-height: 900px;">
                            <div class="thead tr clearfix">
                                <span class="col-xs-3 bitms-c2" style="text-align: center">时间</span>
                                <span class="col-xs-3 bitms-c2">价格($)</span>
                                <span class="col-xs-3 bitms-c2">数量(฿)</span>
                                <span class="col-xs-3 bitms-c2">方向</span>
                            </div>
                            <div id="trading">

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script id="trading_tpl" type="text/html">
    {{each msgContent}}
    <div class="tr clearfix" style="margin-top: 2px">
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
    <div class="pull-left">
        <span class="bitms-c2">最新成交价:</span>
        <span class="{{$value.direct=='spotSell'?'text-danger':'text-success'}}">
            {{$value.direct=='spotSell'?'$'+$value.platPrice+'↓':'$'+$value.platPrice+'↑'}}
        </span>
    </div>
    <div class="pull-right">
        <span class="bitms-c2">指数:</span>
        <span class="{{$value.upDownIdx=='DOWN'?'text-danger':'text-success'}}">
            {{$value.upDownIdx=='DOWN'?'$'+$value.idxPrice+'↓':'$'+$value.idxPrice+'↑'}}
        </span>
    </div>
    {{/each}}
</script>
<script id="deep_sell_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.direct == 'spotSell' && showLevel == $value.deepLevel}}
    <div class="tr clearfix" style="position: relative;margin-bottom: 3px;" onclick="autoWriteBuy({{$value.entrustPrice}},{{$value.entrustAmtSum}})">
        <div style="position: absolute;top: 0px; bottom: 0px; right: 0px; width:{{$percentFlag $value.entrustAmtSum}}%; border-radius: 2px; background-color: rgba(169,68,66,0.3);"></div>
        <span class="col-xs-3 text-danger" style="text-align: center">{{$value.desc}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}}>{{$value.entrustPrice}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}}>{{$value.entrustAmt}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}}>{{$value.entrustAmtSum}}</span>
    </div>
    {{/if}}
    {{/each}}
</script>
<script id="deep_buy_tpl" type="text/html">
    {{each msgContent}}
    {{if $value.direct == 'spotBuy' && showLevel == $value.deepLevel}}
    <div class="tr clearfix" style="position: relative;margin-bottom: 3px;" onclick="autoWriteSell({{$value.entrustPrice}},{{$value.entrustAmtSum}})">
        <div style="position: absolute; top: 0px; bottom: 0px; right: 0px; width:{{$percentFlag $value.entrustAmtSum}}%; border-radius: 2px; background-color: rgba(100,130,65,0.3);"></div>
        <span class="col-xs-3 text-success" style="text-align: center">{{$value.desc}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}}>{{$value.entrustPrice}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}}>{{$value.entrustAmt}}</span>
        <span class="col-xs-3 bitms-c2" {{$value.entrustAccountType==1?'style=color:yellow':''}}>{{$value.entrustAmtSum}}</span>
    </div>
    {{/if}}
    {{/each}}
</script>
<script>
    var ws = null; //websocket对象
    var level = 0;//深度
    var reader = {readAs: function(type,blob,cb){var r = new FileReader();r.onloadend = function(){if(typeof(cb) === 'function') {cb.call(r,r.result);}};try{r['readAs'+type](blob);}catch(e){}}};
    seajs.use(['pagination','validator','template', 'moment', 'sockjs','reconnection'], function (Pagination,Validator,Template, moment) {

        template.helper('$formatDate', function (millsec) {
            return moment(millsec).format("HH:mm:ss");
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
        <% if (BitmsConst.RUNNING_ENVIRONMONT.equals("production")){ %>
        if (window.location.protocol == 'http:') {
            url = 'ws://socket.bitms.com' + "/ws/btc2eur";
        } else {
            url = 'wss://socket.bitms.com' + "/ws/btc2eur";
        }
        <% }else{ %>
        if (window.location.protocol == 'http:') {
            url = 'ws://' + window.location.host + "/ws/btc2eur";
        } else {
            url = 'wss://' + window.location.host + "/ws/btc2eur";
        }
        <% } %>

        if ('WebSocket' in window) {
            ws = new ReconnectingWebSocket(url);
        } else {
            ws = new SockJS(url);
        }

        console.log("url:"+url);
        ws.onmessage = function (event) {
            var blob = event.data;
            reader.readAs('Text',blob.slice(0,blob.size,'text/plain;charset=UTF-8'),function(result){
                var message = JSON.parse(result);
                var msgType = message.msgType;
                console.log(message);
                if (msgType == '<%=InQuotationConsts.MESSAGE_TYPE_REALDEAL%>') {// deal
                    renderDeal(message);
                }
                else if (msgType == '<%=InQuotationConsts.MESSAGE_TYPE_DEEPPRICE%>') {// deep
                    renderDeep(message);
                }
                else if(msgType == '<%=InQuotationConsts.MESSAGE_TYPE_RTQUOTATION%>'){ //quotation
                    renderQuotation(message);
                }
                /*else {// kline
                    renderKline(message);
                }*/
            });
        };

        function renderDeal(message) {
            var html = Template.render("trading_tpl", message);
            $("#trading").html(html);
        }

        function renderQuotation(message) {
            var html = Template.render("quotation_tpl", message);
            $("#quotation").html(html);
        }

        function renderDeep(message) {
            message.showLevel = level;
            deepData = message;
            var deep_sell = Template.render("deep_sell_tpl", deepData);
            var deep_buy = Template.render("deep_buy_tpl", deepData);
            $("#deep_sell").html(deep_sell);
            $("#deep_buy").html(deep_buy);
        }
    });
</script>
</html>