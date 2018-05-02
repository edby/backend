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
                <li onclick="jumpUrl('${ctx}/fund/charge')">
                    <a href="#" data-toggle="tab"><%--充值充币--%><fmt:message key="sidebar.charge" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/withdraw')">
                    <a href="#" data-toggle="tab"><%--提现提币--%><fmt:message key="sidebar.raise" /></a>
                </li>
                <li class="active">
                    <a href="#" data-toggle="tab"><%--现金充值--%><fmt:message key="fund.chargeCash.title" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/withdrawCash')">
                    <a href="#" data-toggle="tab"><%--现金提现--%><fmt:message key="fund.raiseCash.title" /></a>
                </li>
            </ul>
            <div class="panel">
                <div class="tabbable" id="tabs-640621">
                    <ul id="depositTab" class="nav nav-tabs bitms-tabs bb0">
                        <li class="active">
                            <a href="#" data-toggle="tab">EUR</a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane clearfix active">
                            <hr style="margin-top:0;margin-bottom:10px;">
                            <ul class="list-group col-md-12 bitms-c4 pl0">
                                <li class="list-group-item"><h3 class="text-success mb0"><%--付款信息--%><fmt:message key="fund.chargeCash.information" /></h3></li>
                                <c:choose>
                                    <c:when test="${certStatus}">
                                        <li class="list-group-item"><%--银行名称--%><fmt:message key="fund.raiseCash.bank" />：Ubi banca - SEPA (Euro)</li>
                                        <li class="list-group-item"><%--开户人--%><fmt:message key='fund.raiseCash.accountHolder' />：JJ International srl</li>
                                        <li class="list-group-item"><%--卡号--%><fmt:message key='fund.raiseCash.IBAN' />：IT56O0690601008000000014392</li>
                                        <li class="list-group-item">SWIFT/BIC： BLOPIT22</li>
                                        <li class="list-group-item">RFB：<h4 style="display: inline-block"><span class="label label-danger"><%=OnLineUserUtils.getUnid()==null?"":OnLineUserUtils.getUnid() %></span></h4></li>
                                        <li class="list-group-item text-danger">
                                            <span class="glyphicon glyphicon-info-sign"></span>
                                            <span><%--在您汇款时，请务必将红色标签里的数字准确地填写到汇款附言（RFB）中，否则我们将无法及时为您入账。--%><fmt:message key="fund.chargeCash.tip" /></span>
                                        </li>
                                        <li class="list-group-item text-danger">
                                            <span class="glyphicon glyphicon-info-sign"></span>
                                            <span><%--请确保银行开户人信息跟护照认证的信息一致。--%><fmt:message key="fund.chargeCash.tip2" /></span>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="list-group-item text-danger">
                                            <div class="alert alert-danger mb0 p5" onclick="jumpUrl('${ctx}/account/certificationDoing')" role="alert" style="display: inline-block;">
                                                <strong>
                                                    <a class="text-danger"><%--请先登记证件信息--%><fmt:message key='fund.raiseCash.tip' /></a>
                                                </strong>
                                            </div>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <h4 class="text-success"><%--充值历史--%><fmt:message key="fund.charge.rechargeHistory" /></h4>
                <form data-widget="validator" name="chargeForm" id="chargeForm" method="post">
                    <input type="hidden" id="stockinfoId" name="stockinfoId" value="<%=FundConsts.WALLET_EUR_TYPE%>" />
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th><%--充值时间--%><fmt:message key="fund.charge.rechargeTime" /></th>
                                    <th><%--交易ID--%><fmt:message key="fund.chargeCash.trID" /></th>
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
        <td>{{$value.transId}}</td>
        <td>{{$fix4Flag $value.amount}}</td>
        <td>{{$formatFlag $value.status}}</td>
    </tr>
    {{/each}}
</script>
<script>
    seajs.use([ 'pagination', 'template', 'moment'], function(
        Pagination, Template, moment) {
        $('.pageLoader').hide();
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
            return getDictValueByCode(flag);
        });
        renderPage = new Pagination({
            url : "${ctx}/fund/charge/cashChargeList",
            elem : "#pagination",
            form : $("#chargeForm"),
            rows : 8,
            method : "post",
            handleData : function(json) {
                var html = Template.render("list_tpl", json);
                $("#list_emement").html(html);
            }
        });
    });
</script>
</body>
</html>
