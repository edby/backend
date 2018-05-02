<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.blocain.bitms.security.OnLineUserUtils" %>
<%@ include file="/global/header.jsp" %>
<html>
<body>
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp"%>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <div class="col-sm-12 column">
            <%--开始代码位置--%>
            <ul class="nav nav-tabs bitms-tabs" style="background-color:transparent;margin-right:0px;margin-top:-15px;">
                <li onclick="jumpUrl('${ctx}/fund/accountAsset')">
                    <a href="#" data-toggle="tab"><%--账户资产--%><fmt:message key="sidebar.accoutAsset" /></a>
                </li>
                <li class="active">
                    <a href="#" data-toggle="tab"><%--充值充币--%><fmt:message key="sidebar.charge" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/withdraw')">
                    <a href="#" data-toggle="tab"><%--提现提币--%><fmt:message key="sidebar.raise" /></a>
                </li>
                <%--<li onclick="jumpUrl('${ctx}/fund/chargeCash')">
                    <a href="#" data-toggle="tab">&lt;%&ndash;现金充值&ndash;%&gt;<fmt:message key="fund.chargeCash.title" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/withdrawCash')">
                    <a href="#" data-toggle="tab">&lt;%&ndash;现金提现&ndash;%&gt;<fmt:message key="fund.raiseCash.title" /></a>
                </li>--%>
            </ul>
            <div class="panel">
                <div class="tabbable" id="tabs-640621">
                    <div class="tab-content">
                        <div class="tab-pane clearfix active">
                            <hr style="margin-top:0;margin-bottom:10px;">
                            <ul class="list-group col-md-5 px0">
                                <li class="list-group-item" style="overflow: visible;width: 90%;">
                                    <div class="dropdown">
                                        <button class="btn btn-block dropdown-toggle text-left" type="button" id="dropdownMenuListERC" data-toggle="dropdown" style="color:#151922;border-bottom:1px solid #999;background-color:#eee;" aria-haspopup="true" aria-expanded="true">
                                            <span class="pull-left mr10"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/coinLogo/${stockInfo.stockCode}.svg"></span>
                                            <span class="pull-left mr10">${stockInfo.stockCode}</span>
                                            <span class="pull-left mr10">-</span>
                                            <span class="pull-left">${stockInfo.stockName}</span>
                                        </button>
                                        <span class="caret pull-right" style="border-width: 8px;margin-top:-20px;margin-right:5px;color: #151922;"></span>
                                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuListERC" style="width:100%;border:none;padding:0;margin:0;">
                                            <c:forEach items="${listCoin}" var="data">
                                                <button class="btn btn-block dropdown-toggle text-left" onclick="JumpURList('${data.stockType}','${data.remark}')" ${data.canRecharge eq 'no'?'disabled':''} type="button" style="background-color:#eee;border-radius:0;color:#151922;margin-top:0;border-bottom:1px solid #999;">
                                                    <span class="pull-left mr10"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/coinLogo/${data.stockCode}.svg"></span>
                                                    <span class="pull-left mr10 coinERC">${data.stockCode}</span>
                                                    <span class="pull-left mr10">-</span>
                                                    <span class="pull-left">${data.stockName}</span>
                                                    <c:choose>
                                                        <c:when test="${data.canRecharge == 'no'}">
                                                            <span class="pull-right mr20"><%--暂停--%><fmt:message key="stop" /></span>
                                                        </c:when>
                                                    </c:choose>
                                                </button>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </li>
                                <%--<li class="list-group-item">
                                    <h3 class="text-success">&lt;%&ndash;充值&ndash;%&gt;<fmt:message key="recharge" />${coin}</h3>
                                </li>--%>
                                <li class="list-group-item">
                                    <span style="display: inline-block;">
                                        <span class="bitms-bg3 cf p8 bitms-radius2 hand" id="copyAddr" style="display: inline-block;">
                                            <span id="coin">${stockInfo.stockCode}</span>
                                            <%--充值地址--%><fmt:message key="fund.charge.rechargeAddr" />
                                        </span>
                                        <span id="item_addr" class="p8 bitms-bg4" style="display: inline-block;"></span>
                                    </span>
                                </li>
                                <li class="list-group-item pt0">
                                    <small><%--重要提示：系统将在收到12个网络确认后会为您进行入账处理。--%><fmt:message key="fund.chargeEth.txtFirst" /><fmt:message key="fund.chargeEth.txtSecond" /></small>
                                    <br>
                                    <small class="text-danger">
                                        <span class="glyphicon glyphicon-info-sign"></span>
                                        <span><%--禁止向该地址充值除该币种之外的资产，否则将不可找回。--%><fmt:message key="fund.charge.txtSecond" /></span>
                                    </small>
                                    <br>
                                    <small class="text-danger">
                                        <span class="glyphicon glyphicon-info-sign"></span>
                                        <span><%--ERC20 Token仅支持Transfer和Transfer from方法进行充值。--%><fmt:message key="fund.chargeEth.txtThird" /></span>
                                    </small>
                                </li>
                            </ul>
                            <div class="col-md-7 pt10" >
                                <div id="qrcode_img" style="width: 170px;height: 170px;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <h4 class="text-success"><%--充值历史--%><fmt:message key="fund.charge.rechargeHistory" /></h4>
                <form data-widget="validator" name="chargeForm" id="chargeForm" method="post">
                    <input type="hidden" id="stockinfoId" name="stockinfoId" value="${stockInfo.id}" />
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th><%--充值时间--%><fmt:message key="fund.charge.rechargeTime" /></th>
                                    <th><%--币种--%><fmt:message key="fund.raise.coin" /></th>
                                    <th><%--充值地址--%><fmt:message key="fund.charge.rechargeAddr" /></th>
                                    <th><%--数量--%><fmt:message key="number" /></th>
                                    <th><%--状态--%><fmt:message key="state" /></th>
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
                </form>
            </div>
        </div>
    </div>
</div>
<script id="list_tpl" type="text/html">
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
    var renderPage;
    seajs.use([ 'pagination', 'template', 'moment','qrcode','clipboard' ], function(
        Pagination, Template, moment, qrcode) {
        $('.pageLoader').hide();
        <c:choose>
        <c:when test="${address ne ''}">
        $("#qrcode_img").show();
        $("#qrcode_img").html("");
        $("#qrcode_img").css("border","10px solid #fff");
        $("#qrcode_img").css("background-color","white");
        $("#qrcode_img").append(new qrcode({
            render : "canvas",
            width : 150, <%--宽度--%>
            height : 150, <%--高度--%>
            text : '${address}'
        }));
        $("#item_addr").html('${address}');
        </c:when>
        <c:otherwise>
        $("#item_addr").html('<%--调用充值地址异常，请联系客服！--%><fmt:message key="fund.charge.creatAddr" />');
        </c:otherwise>
        </c:choose>

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
        renderPage = new Pagination({
            url : "${ctx}/fund/charge/chargeERC20List",
            elem : "#pagination",
            form : $("#chargeForm"),
            rows : 8,
            method : "post",
            handleData : function(json) {
                var html = Template.render("list_tpl", json);
                $("#list_emement").html(html);
            }
        });
        //复制地址
        function copyArticle(event) {
            const range = document.createRange();
            range.selectNode(document.getElementById('item_addr'));

            const selection = window.getSelection();
            if(selection.rangeCount > 0) selection.removeAllRanges();
            selection.addRange(range);
            document.execCommand('copy');
        }
        document.getElementById('copyAddr').addEventListener('click', copyArticle, false);
    });

    $(".ListERCStockCode").each(function () {
        $(this).text('${stockInfo.stockCode}');
    });
    $(".ListERCSmallDepositFeeValue").each(function () {
        $(this).text('${stockInfo.smallDepositFeeValue}');
    });
    $(".ListERCSmallDepositStandardValue").each(function () {
        $(this).text('${stockInfo.smallDepositStandardValue}');
    });
    
    function JumpURList(flagStockType,symbol) {
        if(flagStockType == "<%=FundConsts.STOCKTYPE_DIGITALCOIN %>")
        {
            jumpUrl('${ctx}/fund/charge?symbol='+symbol);
        }
        else if(flagStockType == "<%=FundConsts.STOCKTYPE_ERC20_TOKEN %>"){
            jumpUrl('${ctx}/fund/chargeEth?symbol='+symbol);
        }
    }
</script>
</body>
</html>
