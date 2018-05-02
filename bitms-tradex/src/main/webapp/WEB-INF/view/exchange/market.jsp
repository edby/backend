<%@ page import="com.blocain.bitms.quotation.consts.InQuotationConsts" %>
<%@ page import="com.blocain.bitms.tools.consts.BitmsConst" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-xs-12" style="padding:0;">
            <div class="table-responsive">
                <table class="table table-condensed table-hover" style="font-size:14px;border-bottom:1px solid #ddd;">
                    <thead>
                    <tr>
                        <th class="text-left width-xs-adapt" style="background-color:#e8e8e8;width:10%;padding-left:20px;border:none;">Token</th>
                        <th class="text-center width-xs-adapt" style="background-color:#e8e8e8;width:20%;border:none;">Price</th>
                        <th class="text-center width-xs-adapt" style="background-color:#e8e8e8;width:15%;border:none;">Change</th>
                        <th class="text-center hidden-xs" style="background-color:#e8e8e8;width:20%;border:none;">24h High</th>
                        <th class="text-center hidden-xs" style="background-color:#e8e8e8;width:20%;border:none;">24h Low</th>
                        <th class="text-center width-xs-adapt" style="background-color:#e8e8e8;width:15%;border:none;">Volume</th>
                    </tr>
                    </thead>
                    <tbody id="allRtQuotation">

                    </tbody>
                </table>
                <%--<div class="spinner" style="top:150px;">
                    <div class="rect1"></div>
                    <div class="rect2"></div>
                    <div class="rect3"></div>
                    <div class="rect4"></div>
                    <div class="rect5"></div>
                </div>
                <div class="col-xs-12 text-center" id="listTip" style="margin-top:100px;display:none;">
                    <span class="glyphicon glyphicon-list-alt" style="font-size:100px;color:#ddd;margin-bottom:20px;"></span>
                    <p>No market data</p>
                </div>--%>
            </div>
            <span style="display:block;font-size:14px;text-align:right;padding-right:5px;">A total of <span id="total" style="font-weight:bold;font-size:16px;color:red;"></span> ERC20 Token Contracts found</span>
            <div id="pagination" class="paginationContainer" style="text-align:right;padding-right:5px;height:41px;"></div>
            <div class="table-responsive">
                <table class="table table-condensed table-hover" style="font-size:14px;border-bottom:1px solid #ddd;">
                    <thead>
                    <tr>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%;border:none;padding:4px;">Symbol</th>
                        <th class="text-center hidden-xs" style="background-color:#e8e8e8;width:20%;border:none;padding:4px;">Name</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:60%;border:none;padding:4px;">Contract Address</th>
                    </tr>
                    </thead>
                    <tbody id="token">

                    </tbody>
                </table>
            </div>
            <div id="pagination2" class="paginationContainer" style="padding-right:5px;"></div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script id="allRtQuotation_tpl" type="text/html">
    {{each msgContent}}
    <tr onclick="jumpUrl('${ctx}/exchange?contractAddr={{$value.tokencontactaddr}}')" style="cursor:pointer;">
        <td style="padding-left:15px;font-weight:bold;" class="text-left"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/tokenLogo/{{$value.tokenContactAddr}}.svg" onerror="this.src='${imagesPath}/tokenLogo/token.svg'">{{$FixStrFlag $value.stockName}}<sup>{{$supFlag $value.tokencontactaddr}}</sup></td>
        <td class="text-center">{{$value.platPrice}}</td>
        <td class="text-center {{$value.upDown=='DOWN'?'text-danger':'text-success'}}">{{$fix2Flag $value.range}}%<span style="vertical-align:text-bottom;">{{$value.upDown=='DOWN'?'↓':'↑'}}</span></td>
        <td class="text-center hidden-xs">{{$value.highestPrice}}</td>
        <td class="text-center hidden-xs">{{$value.lowestPrice}}</td>
        <td class="text-center">{{$value.volume}}</td>
    </tr>
    {{/each}}
</script>
<script id="token_list_tpl" type="text/html">
    {{each rows}}
    <tr>
        <td class="text-center size-xs-market" style="font-weight:bold;">{{$value.symbol}}<sup>{{$supFlag $value.contractAddr}}</sup></td>
        <td class="text-center hidden-xs">{{$value.symbolName}}</td>
        <td class="text-center size-xs-market"><a style="cursor:pointer;" href="${ctx}/exchange?contractAddr={{$value.contractAddr}}">{{$value.contractAddr}}</a></td>
    </tr>
    {{/each}}
</script>

<script>
    var flag_allQuotation = false;

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
    seajs.use(['pagination', 'validator', 'template', 'moment', 'qrcode', 'sockjs', 'reconnection'], function (Pagination, Validator, Template, moment, qrcode) {

        template.helper('$FixStrFlag', function (flag) {
            return flag.substring(0,flag.indexOf('2'));
        });
        template.helper('$fix2Flag', function(flag) {
            if(flag==''||flag==null){
                return '0.00';
            }else{
                return comdify(parseFloat((flag)).toFixed(2));
            }

        });
        template.helper('$supFlag', function (flag) {
            if(flag != null && flag != ''){
                return flag.substr(40,2);
            }
        });

        <%--获取实时行情--%>
        <% if (BitmsConst.RUNNING_ENVIRONMONT.equals("production")){ %>
        if (window.location.protocol == 'http:') {
            url = 'ws://socket.biex.com/ws/allRtQuotation';
        } else {
            url = 'wss://socket.biex.com/ws/allRtQuotation';
        }
        <% }else{ %>
        if (window.location.protocol == 'http:') {
            url = 'ws://' + window.location.host + '/ws/allRtQuotation';
        } else {
            url = 'wss://' + window.location.host + '/ws/allRtQuotation';
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
                if (msgType == '<%=InQuotationConsts.MESSAGE_TYPE_ALLRTQUOTATION%>') {
                    <%--wsAllRtQuotation--%>
                    renderAllRtQuotation(message);
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
            message.msgContent = message.msgContent.sort(function(a, b){return parseFloat(a["range" ]) < parseFloat(b["range" ]) ? 1 : parseFloat(a[ "range"]) == parseFloat(b[ "range" ]) ? 0 : -1;});
            var html = Template.render("allRtQuotation_tpl", message);
            /*$(".spinner").hide();
            if(message.msgContent.length == 0){
                $("#listTip").show();
            }*/
            $("#allRtQuotation").html(html);
        }

        <%--获取Token列表--%>
        Asset = new Pagination({
            url: "${ctx}/exchange/tokens",
            elem : ".paginationContainer",
            rows: 100,
            method: "get",
            handleData: function (json) {
                var html = Template.render("token_list_tpl", json);
                $("#token").html(html);
                $("#total").text(json.total)
            }
        });

    });

</script>
</body>
</html>