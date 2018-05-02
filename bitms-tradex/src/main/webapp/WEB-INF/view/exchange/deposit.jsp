<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%
    request.setAttribute("eth", FundConsts.WALLET_ETH_TYPE);
%>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-sm-5 col-xs-12 padding-xs-none" style="margin-bottom:10px;">
            <div style="border:1px solid #ddd;">
                <h4 class="loginTit" style="display:block;background-color:#e8e8e8;padding:7px;font-size:12px;font-weight:bold;">
                    <span>Deposit</span>
                    <a class="pull-right" href="${ctx}/exchange?contractAddr=${ERC20Contract}" style="cursor:pointer">Go to Trade</a>
                </h4>
                <div class="text-center" style="padding-bottom:5px;margin:20px auto;max-width:600px;">
                    <form:form class="form-horizontal" data-widget="validator" id="withdrawForm">
                        <input name="stockinfoId" hidden value="${stockinfoId}"/>
                        <div class="form-group">
                            <div class="col-xs-10 col-xs-offset-1 input-group">
                                <p class="alert alert-danger" style="background-color:#f2dede;border-color:#ebccd1;color:#a94442;padding:10px;background-image:none;margin-bottom:0px;">
                                    <c:if test="${stockinfoId ne eth}">
                                        <span class="glyphicon glyphicon-exclamation-sign"></span>
                                        <span>Please check that you are depositing this token</span>
                                        <br>
                                    </c:if>
                                    <span class="text-success text-left" style="font-weight:bold;">
                                        <span>Name:${symbol}(Symbol:${symbolName})</span>
                                        <c:if test="${stockinfoId ne eth}">
                                            <br>
                                            <span>ERC20 Contract Address:${fn:substring(ERC20Contract,0,10)}...${fn:substring(ERC20Contract,34,42)}</span>
                                        </c:if>
                                    </span>
                                </p>
                            </div>
                        </div>
                        <div class="form-group" style="margin-bottom:0">
                            <div class="col-xs-10 col-xs-offset-1">
                            <c:choose>
                                <c:when test="${address == '' || address == null}">
                                    <h4 class="text-danger">Address error,please contact the customer service!</h4>
                                </c:when>
                                <c:otherwise>
                                    <div id="qrcode_img"></div>
                                    <h4 id="item_addr" style="padding:5px;border-radius:4px;border:1px solid #5bc0de;background-color:#F0FFFF;display:inline-block;"></h4>
                                    <p class="text-center"> The deposit amount must be more than <span>${minValue}</span>, or will not becredited.</p>
                                </c:otherwise>
                            </c:choose>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
        <div class="col-sm-7 col-xs-12 padding-xs-none">
            <div class="table-responsive">
                <table class="table table-striped table-condensed table-bordered text-center" style="font-size:12px;margin-bottom:0;">
                    <thead>
                    <tr style="border:1px solid #ddd;">
                        <th class="text-center" style="background-color:#e8e8e8;border-left:1px solid #ddd;width:20%">Date</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:45%">Deposit address</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:20%">Amount</th>
                        <th class="text-center" style="background-color:#e8e8e8;border-right:1px solid #ddd;width:15%">Status</th>
                    </tr>
                    </thead>
                    <tbody id="depositHistory">

                    </tbody>
                </table>
                <div id="pagination" class="paginationContainer"></div>
                <div class="text-center" id="listTip" style="margin:100px;">
                    <span class="glyphicon glyphicon-list-alt" style="font-size:100px;color:#ddd;margin-bottom:20px;"></span>
                    <p>You have no deposit history</p>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script id="depositHistory_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDate $value.createDate}}</td>
        <td>{{$formatFlag2 $value.walletAddr $value.transId}}</td>
        <td>{{$fix4Flag $value.amount}}</td>
        <td>{{$formatFlag $value.status}}</td>
    </tr>
    {{/each}}
</script>
<script>
    seajs.use(['qrcode', 'pagination', 'template', 'moment'], function (qrcode, Pagination, Template, moment) {

        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });
        template.helper('$fix4Flag', function(flag) {
            if(flag==''||flag==null){
                return '0';
            }else{
                return comdify(parseFloat((flag)).toFixed(4));
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

        if(${longStatus == true}) {

            <%--充值记录--%>
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

            <%--充值地址二维码--%>
            if ('${address}' != '' && '${address}' != null) {
                $("#qrcode_img").html("");
                $("#qrcode_img").css("border","10px solid #fff");
                $("#qrcode_img").css("background-color","white");
                $("#qrcode_img").append(new qrcode({
                    render : "canvas",
                    width : 150, <%--宽度--%>
                    height : 150, <%--高度--%>
                    text : '${address}'
                }));
                $("#item_addr").text('${address}');
            }
        }

    });
</script>
</body>
</html>