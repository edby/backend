<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp" %>
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
                    <th style="background-color:#e8e8e8;">ApproveStatus</th>
                    <th style="background-color:#e8e8e8;">TransferStatus</th>
                    <th style="background-color:#e8e8e8;">operation</th>
                </tr>
                </thead>
                <tbody id="withdrawHistory">

                </tbody>
            </table>
            <div id="pagination" class="paginationContainer"></div>
            <div class="col-xs-12 text-center" id="listTip" style="margin-top: 100px;">
                <span class="glyphicon glyphicon-list-alt" style="font-size:100px;color:#ddd;margin-bottom:20px;"></span>
                <p>You have no withdraw History</p>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script id="withdrawHistory_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDate $value.currentDate}}</td>
        <td>${symbol}</td>
        <td>{{$formatFlag3 $value.withdrawAddr $value.transId}}</td>
        <td>{{$subFlag $value.occurAmt,$value.fee}}</td>
        <td>{{$formatFlag $value.approveStatus}}</td>
        <td>{{$formatFlag $value.transferStatus}}</td>
        <td>{{$actionFlag $value.approveStatus,$value.id}}</td>
    </tr>
    {{/each}}
</script>
<script>
    var withdrawHistory = null;
    seajs.use([ 'pagination', 'template', 'moment','my97DatePicker' ], function(Pagination, Template, moment) {

        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });
        template.helper('$formatFlag', function(flag) {
            return getDictValueByCode(flag);
        });
        template.helper('$subFlag', function(occurAmt,netFee) {
            return ((parseFloat(occurAmt)*10000-parseFloat(netFee)*10000)/10000).toFixed(4);
        });
        template.helper('$formatFlag3', function(flag,transId) {
            if(transId == null){
                return flag;
            }
            else{
                return flag+"&nbsp;<a class='text-success' target='_blank' href='https://etherscan.io/tx/"+transId+"'><%--查看状态--%><fmt:message key="fund.charge.check" /></a>";
            }
        });
        template.helper('$actionFlag', function(flag,id) {
            if (flag == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING %>' || flag == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_WAITING_EMAIL_CONFIRM %>' ) {
                return '<div class="btn btn-primary btn-xs" onclick="cancel(\'id'+id+'\')">Cancel<div>';
            } else {
                return '';
            }

        });

        if('${longStatus}' == 'true') {
            withdrawHistory = new Pagination({
                url: "${ctx}/exchange/withdrawEth/withdrawEthList",
                data: {
                    contractAddr: '${ERC20Contract}'
                },
                elem : "#pagination",
                rows: 10,
                method: "get",
                handleData: function (json) {
                    var html = Template.render("withdrawHistory_list_tpl", json);
                    $("#withdrawHistory").html(html);

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

    <%--取消提币--%>
    function cancel(id){
        $.ajax({
            url : '${ctx}/exchange/withdrawEth/withdrawEthCancel',
            data : {
                "id" : id.substr(2),
                "stockinfoId" : ${stockinfoId}
            },
            type : "POST",
            cache : false,
            async : false,// false=同步调用，锁定其它JS操作
            dataType : "json",
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                alert('服务器连接失败，请稍候重试！');
            },
            success : function(data, textStatus, jqXHR) {
                $('#csrf-form').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                setCsrfToken("csrf-form");
                if (data.code == bitms.success) {
                    remind(remindType.success, data.message,500);
                    setTimeout("location.reload();",500);
                } else {
                    remind(remindType.error, data.message,1000);
                }
            }
        });
    }
</script>
</body>
</html>