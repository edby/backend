<%@ page import="com.blocain.bitms.tools.consts.BitmsConst" %>
<%@ page import="com.blocain.bitms.quotation.consts.InQuotationConsts" %>
<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%-- 此段必须要引入 t为小时级别的时间戳 --%>
<link type="text/css" href="//g.alicdn.com/sd/ncpc/nc.css?t=1502956831425" rel="stylesheet"/>
<script type="text/javascript" src="//g.alicdn.com/sd/ncpc/nc.js?t=1502956831425"></script>
<%-- 引入结束 --%>
<%-- 此段必须要引入 --%>
<div id="_umfp" style="display:inline;width:1px;height:1px;overflow:hidden"></div>
<%-- 引入结束 --%>
<%--<%@ include file="/global/lang.jsp" %>--%>
<body style="background:url('${imagesPath}/bitms/home.jpg')">
<div class="row my0">
	<div class="col-xs-12 login pt15 hidden-xs" style="position: fixed;z-index: 10;height: 60px;background-color: rgba(0,0,0,0.3);">
		<a class="login-logo hidden-xs" href="/" title="bitms"><img class="img-responsive" src="${imagesPath}/bitms/bitms.svg" alt="bitms" /></a>
		<a href="/register" class="text-success pull-right fs16 hidden-xs" <%--onclick="remind(remindType.normal, '<fmt:message key="register.noOpen" />');"--%> title="SIGN UP">SIGN UP</a>
		<hr class=" pull-right login-hr hidden-xs">
		<a class="text-success pull-right fs16 text-center loginBtn hidden-xs" href="/login">LOG IN</a>
	</div>
</div>
<%--<div style="position: absolute;z-index: -1;"><img src="${imagesPath}/bitms/home.png" /></div>--%>
<div class="text-center mt50 visible-xs"><a href="/login" title="BitMS"><img src="${imagesPath}/bitms/bitms.svg" alt="BitMS"></a></div>
<div class="text-center clearfix mt20 visible-xs">
	<a href="/register" class="text-success fs16" style="display: inline-block" title="SIGN UP">SIGN UP</a>
	<hr class="login-hr" style="display: inline-block">
	<a class="text-success fs16 text-center loginBtn" style="display: inline-block" href="/login">LOG IN</a>
</div>
<div class="bitms-con text-center login-txt">
	<div class="login-txt1 fs45 mb30" style="color:#efb321;">
		<span class="glyphicon glyphicon-warning-sign"></span>
		<span>RISK WARNING</span>
	</div>
	<div class="mb30 cf">LUXEMBOURG／HONG KONG／SINGAPORE／MILAN</div>
	<div class="cf">
		<p>Digital asset trading can be extremely risky. You should carefully consider and be totally clear about the risks and bear the resulting loss.</p>
		<p>デジタル資産の取引は非常に危険です。 リスクについて慎重に検討し、完全に明確にし、結果として生じる損失を負うべきです。</p>
		<p>디지털 자산 거래는 매우 위험 할 수 있습니다. 위험에 대해 신중히 고려하고 명확히해야하며 이로 인한 손실을 감수해야합니다.</p>
		<p>تداول الأصول الرقمية يمكن أن يكون محفوفة بالمخاطر للغاية. يجب أن تفكر بعناية وأن تكون واضحًا تمامًا بشأن المخاطر وتحمل الخسارة الناتجة.</p>
		<p>數字資產交易存在相当的风险。 你應該仔細考慮並且完全清楚该風險並能承擔可能由此造成的損失。</p>
		<%--<p>Торговля цифровыми активами может быть чрезвычайно рискованной. Вы должны тщательно рассмотреть и полностью понять риски и нести результирующую потерю.</p>--%>
	</div>
	<a href="/login" class="login-txt2 btn btn-primary cf my0 fs22 mt20">VIEW LIVE TRADING</a>
</div>
<div class="table-responsive tab-pane active text-center mt30 col-sm-10 col-sm-offset-1" style="border:none;display:none;">
	<ul class="nav nav-tabs bitms-tabs mb20" style="background-color:transparent;margin-right:0;">
		<li class="active">
			<a href="#" data-toggle="tab" style="color:rgba(255,255,255,0.8);" onclick="changeMarkert('<%=FundConsts.WALLET_BTC_TYPE%>')">BTC Markets</a>
		</li>
		<li>
			<a href="#" data-toggle="tab" style="color:rgba(255,255,255,0.8);" onclick="changeMarkert('<%=FundConsts.WALLET_ETH_TYPE%>')">ETH Markets</a>
		</li>
		<%--<li>
			<a href="#" data-toggle="tab" style="color:rgba(255,255,255,0.8);">USD Markets</a>
		</li>
		<li>
			<a href="#" data-toggle="tab" style="color:rgba(255,255,255,0.8);">EUR Markets</a>
		</li>--%>
	</ul>
	<table id="allRtQuotation" class="table mb10">
		<thead>
		<tr>
			<th style="width: 16%">Pair</th>
			<th style="width: 16%">Last Price</th>
			<th class="hidden-xs" style="width: 16%">24h High</th>
			<th class="hidden-xs" style="width: 16%">24h Low</th>
			<th class="hidden-xs" style="width: 16%">24h Volume</th>
			<th style="width: 16%">24h Change</th>
		</tr>
		</thead>
		<tbody id="list_emement">

		</tbody>
	</table>
	<div class="mt10 m010">
        <%-- 通用分页 --%>
        <div id="pagination" class="paginationContainer"></div>
    </div>
</div>
<footer class="navbar-fixed-bottom bitms-con text-center" style="position:absolute;width:100%;left:0;bottom:0;" <%--style="position:relative;padding-top:500px;"--%>>
	<div class="fs12 text-center bitms-footer" style="color:#acadaf;margin-bottom:0px;background-color:#0b212f;border:none;">© 2017 BITMS.com All Rights Reserved BITMS LIMITED</div>
</footer>
<%--<div class="load-pop"><div class="lp-inner"><img src="${imagesPath}/bitms/reg-pop.gif"/><div class="rpi-txt fs14 c1">loading</div></div></div>--%>
<script id="list_tpl" type="text/html">
	{{each}}
		<tr>
			<td><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/coinLogo/{{$formatFlagImg $value.stockName}}.svg">{{$value.stockName}}</td>
			<td>{{$value.platPrice}}</td>
			<td class="hidden-xs">{{$value.highestPrice}}</td>
			<td class="hidden-xs">{{$value.lowestPrice}}</td>
			<td class="hidden-xs">{{$value.volume}}</td>
			<td>{{$formatFlagChange $value.range}}</td>
		</tr>
	{{/each}}
</script>
<script>
    var flag_allQuotation = false;
    var allQuotationTopic = "allRtQuotation";
	var stockinfoId="<%=FundConsts.WALLET_BTC_TYPE%>";
    var ws = null; <%--websocket对象--%>
    var url = null; <%--访问地址--%>
    var reader = {readAs: function(type,blob,cb){var r = new FileReader();r.onloadend = function(){if(typeof(cb) === 'function') {cb.call(r,r.result);}};try{r['readAs'+type](blob);}catch(e){}}};
    seajs.use(['pagination','template', 'moment', 'sockjs','reconnection'], function (Pagination, Template) {

        template.helper('$formatFlagChange', function (flag) {
            if(flag >= 0){
				return '<span style="color:#8bc040">+'+flag+'%</span>';
            }
            else {
                return '<span style="color:#de5f66">'+flag+'%</span>';
            }
        });

        template.helper('$formatFlagImg', function (flag) {
			return (flag.split('/'))[0];
        });

        <% if (BitmsConst.RUNNING_ENVIRONMONT.equals("production")){ %>
        if (window.location.protocol == 'http:') {
            url = 'ws://socket.bitms.com' + "/ws/allRtQuotation";
        } else {
            url = 'wss://socket.bitms.com' + "/ws/allRtQuotation";
        }
        <% }else{ %>
        if (window.location.protocol == 'http:') {
            url = 'ws://' + window.location.host + "/ws/allRtQuotation";
        } else {
            url = 'wss://' + window.location.host + "/ws/allRtQuotation";
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
                if (msgType == '<%=InQuotationConsts.MESSAGE_TYPE_ALLRTQUOTATION%>') {// ALLRTQUOTATION
                    renderAllRtQuotation(message.msgContent);
                    flag_allQuotation = true;
                }
            });
        };

        //轮询行情是否为空
        window.setInterval(function () {

            if (! flag_allQuotation) {
                ws.send("{\"topic\":\"" + allQuotationTopic + "\"}");
            }

        }, 3000);

        function renderAllRtQuotation(message) {
            var useList = new Array();
            for(var i = 0;i < message.length; i ++)
            {
                if(message[i].market == stockinfoId)
                {
                    useList.push(message[i]);
                }
            }
            var html = Template.render("list_tpl",useList);
            $("#list_emement").html(html);
        }

    });
    function changeMarkert(id)
	{
	    stockinfoId = id;
        $("#list_emement").html('');
        flag_allQuotation=false;
        ws.send("{\"topic\":\"" + allQuotationTopic + "\"}");
	}

	$(function(){
		$('.load-pop').hide();
	    $('.login-txt1,.login-txt2').addClass('reveal-landing');
	});
</script>
</body>
</html>
