<%@ page import="com.blocain.bitms.trade.trade.enums.TradeEnums" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-xs-12 padding-xs-none" style="margin-top:10px;">
            <a href="${ctx}/exchange?contractAddr=${ERC20Contract}" style="cursor:pointer;position:absolute;right:8px;margin-top:-20px;">Go to Trade</a>
            <div class="table-responsive">
                <table class="table table-striped table-condensed table-bordered" style="font-size: 12px;">
                    <thead>
                    <tr>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Date</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:8%">Coin</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Side</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Price</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Executed/Amount</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Average Price</th>
                    </tr>
                    </thead>
                    <tbody id="orderHistory">

                    </tbody>
                </table>
                <div id="pagination" class="paginationContainer"></div>
                <div class="col-xs-12 text-center" id="listTip" style="margin-top:100px;display:none;">
                    <span class="glyphicon glyphicon-list-alt" style="font-size:100px;color:#ddd;margin-bottom:20px;"></span>
                    <p>You have no order history</p>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>

<script id="orderHistory_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td class="text-center">{{$formatDateTime $value.entrustTime}}</td>
        <td class="text-center">{{$value.stockCode}}</td>
        <td class="text-center">{{$formatFlag $value.entrustDirect}}</td>
        <td class="text-center">{{$Fix8Flag $value.entrustPrice}}</td>
        <td class="text-center">{{$Fix4Flag $value.dealAmt}}/{{$Fix4Flag $value.entrustAmt}}</td>
        <td class="text-center">{{$avgFlag $value.entrustDirect,$value.dealAmt,$value.dealBalance,$value.dealFee}}</td>
    </tr>
    {{/each}}
</script>

<script>
    var orderHistory = null;
    seajs.use([ 'pagination', 'template', 'moment','my97DatePicker' ], function(Pagination, Template, moment) {

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
        template.helper('$Fix4Flag', function(flag) {
            return  comdify(parseFloat((flag)).toFixed(4));
        });
        template.helper('$Fix8Flag', function(flag) {
            return  comdify(parseFloat((flag)).toFixed(8));
        });
        template.helper('$formatFlag', function(flag) {
            return  getDictValueByCode(flag);
        });

        if('${longStatus}' == 'true') {
            orderHistory = new Pagination({
                url: "${ctx}/exchange/entrustData",
                data: {
                    isHis: 'no',
                    contractAddr: '${ERC20Contract}'
                },
                elem : "#pagination",
                rows: 15,
                method: "get",
                handleData: function (json) {
                    var html = Template.render("orderHistory_list_tpl", json);
                    $("#orderHistory").html(html);

                    <%--判断图标是否显示--%>
                    if(json.rows.length > 0){
                        $("#listTip").hide();
                    }else {
                        $("#listTip").show();
                    }
                }
            });
        }
    });
</script>
</body>
</html>