<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-xs-12 padding-xs-none" style="margin-top:10px;">
            <a href="${ctx}/exchange?contractAddr=${ERC20Contract}" style="cursor:pointer;position:absolute;right:8px;margin-top:-20px;">Go to Trade</a>
            <div class="table-responsive">
                <table class="table table-condensed table-bordered" style="font-size: 12px;">
                    <thead>
                    <tr>
                        <th class="text-center" style="background-color:#e8e8e8;width:12%">Date</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:8%">Coin</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Type</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Amount</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Fee</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Balance</th>
                    </tr>
                    </thead>
                    <tbody id="bills">

                    </tbody>
                </table>
                <div id="pagination" class="paginationContainer"></div>
                <div class="col-xs-12 text-center" id="listTip" style="margin-top:100px;display:none;">
                    <span class="glyphicon glyphicon-list-alt" style="font-size:100px;color:#ddd;margin-bottom:20px;"></span>
                    <p>You have no bill history</p>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script id="bills_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td class="text-center">{{$formatDate $value.currentDate}}</td>
        <td class="text-center">${symbol}<sup>${fn:substring(ERC20Contract,40,42)}</sup></td>
        <td class="text-center">{{$formatFlag $value.businessFlag}}</td>
        <td class="text-center">{{$plusMinusFlag2 $value.occurAmt,$value.occurDirect}}</td>
        <td class="text-center">{{$fix8Date $value.fee}}</td>
        <td class="text-center">{{$fix8Date $value.lastAmt}}</td>
    </tr>
    {{/each}}
</script>
<script>
    var bills = null;
    seajs.use([ 'pagination', 'template', 'moment','my97DatePicker' ], function(Pagination, Template, moment) {

        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });
        template.helper('$fix8Date', function (flag) {
            if(flag == '' || flag == null){
                return '0';
            }
            return comdify(parseFloat(flag).toFixed(8));

        });
        template.helper('$plusMinusFlag2', function(amt,direct) {
            if(amt == '' || amt == null){
                return '0';
            }
            var clr='#a94442';
            if (amt<0) {
                clr='#a94442';
            }else{
                clr='#3c763d';
            }
            return "<font color='"+clr+"' >"+parseFloat(amt).toFixed(8)+"</font>";
        });
        template.helper('$formatFlag', function(flag) {
            return getDictValueByCode(flag);
        });

        if('${longStatus}' == 'true') {
            bills = new Pagination({
            url: "${ctx}/exchange/currents/currentsList",
            data: {
                isHis: 'no',
                contractAddr: '${ERC20Contract}'
            },
            elem : "#pagination",
            rows: 15,
            method: "get",
            handleData: function (json) {
                var html = Template.render("bills_list_tpl", json);
                $("#bills").html(html);

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