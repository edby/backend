<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body style="min-width: 1230px;">
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-xs-12">
            <table class="table table-striped table-condensed table-bordered" style="font-size: 12px;">
                <thead>
                <tr>
                    <th style="background-color:#e8e8e8;">Date</th>
                    <th style="background-color:#e8e8e8;">Coin</th>
                    <th style="background-color:#e8e8e8;">Address</th>
                    <th style="background-color:#e8e8e8;">Amount</th>
                    <th style="background-color:#e8e8e8;">Status</th>
                </tr>
                </thead>
                <tbody id="depositHistory">

                </tbody>
            </table>
            <div id="pagination" class="paginationContainer"></div>
            <div class="col-xs-12 text-center" id="listTip" style="margin-top: 100px;">
                <span class="glyphicon glyphicon-list-alt" style="font-size:100px;color:#ddd;margin-bottom:20px;"></span>
                <p>You have no deposit history</p>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script id="depositHistory_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDate $value.createDate}}</td>
        <td>{{$value.stockCode}}</td>
        <td>{{$formatFlag2 $value.walletAddr $value.transId}}</td>
        <td>{{$fix4Flag $value.amount}}</td>
        <td>{{$formatFlag $value.status}}</td>
    </tr>
    {{/each}}
</script>
<script>
    var depositHistory = null;
    seajs.use([ 'pagination', 'template', 'moment','my97DatePicker' ], function(Pagination, Template, moment) {

        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });
        template.helper('$fix4Flag', function(flag) {
            if(flag==''||flag==null){
                return '0';
            }else{
                return parseFloat((flag)).toFixed(4)
            }

        });
        template.helper('$formatFlag', function(flag) {
            var colorvale = "#fff";
            if (flag.indexOf("unconfirm") > -1) {
                colorvale = "#a94442";
            }
            else{
                colorvale = "#648241";
            }
            return "<font color='"+colorvale+"'>" + getDictValueByCode(flag) + "</font>";
        });
        template.helper('$formatFlag2', function(flag,transId) {
            if(transId == null){
                return flag;
            }
            else{
                return flag+"&nbsp;<a class='text-success' target='_blank' href='https://etherscan.io/tx/"+transId+"'><%--查看状态--%><fmt:message key="fund.charge.check" /></a>";
            }
        });

        if('${longStatus}' == 'true') {
            depositHistory = new Pagination({
                url: "${ctx}/exchange/charge/chargeERC20List",
                data: {
                    contractAddr: '${ERC20Contract}'
                },
                elem : "#pagination",
                rows: 10,
                method: "get",
                handleData: function (json) {
                    var html = Template.render("depositHistory_list_tpl", json);
                    $("#depositHistory").html(html);

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