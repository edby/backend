<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" import="com.blocain.bitms.trade.fund.consts.FundConsts"%>
<%--顶部well--%>
<%--
<div class="well well-sm clearfix">
    <c:set var="notice" value="${fns:geLatestNotice()}"/>
    <c:set var="list" value="${fns:getWalletAsset()}"/>
    <c:set var="btc" value="<%=FundConsts.WALLET_BTC_TYPE %>"/>
    <c:set var="ltc" value="<%=FundConsts.WALLET_LTC_TYPE %>"/>
    <c:set var="eth" value="<%=FundConsts.WALLET_ETH_TYPE %>"/>
    <c:set var="bms" value="<%=FundConsts.WALLET_BMS_TYPE %>"/>
    <!--公告-->
    <span class="pull-left">
        <span class="glyphicon glyphicon-volume-up text-warning"></span>
        <span>${null != notice? notice.title: ""}</span>
    </span>
    <!--账户余额-->
    &lt;%&ndash;<ul class="pull-right clearfix bitms-Wallet col-lg-4 col-sm-6 hidden-xs">
        <li>
            <ul class="clearfix">
                <span class="pull-left"><fmt:message key="availableBalance" />：</span>
                <c:if test="${!empty list}">
                    <c:forEach var="item" items="${list }"  varStatus="status" >
                        <c:choose>
                            <c:when test="${item.stockinfoId == btc }">
                                <li class="pull-lef col-xs-4">
                                    <img src="${imagesPath}/bitms/icon-logo-a.png"/>
                                    <fmt:formatNumber value="${item.amount-item.frozenAmt}" pattern="#0.0000" />
                                </li>
                            </c:when>
                            <c:when test="${item.stockinfoId == ltc }">
                                <li class="pull-left col-xs-4">
                                    <img src="${imagesPath}/bitms/icon-logo-b.png"/>
                                    <fmt:formatNumber value="${item.amount-item.frozenAmt}" pattern="#0.0000" />
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="pull-left col-xs-4">
                                        ${item.stockinfoId }:<fmt:formatNumber value="${item.amount-item.frozenAmt}" pattern="0.0000" />
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:if>
                <span class="text-success pull-right glyphicon glyphicon-chevron-down" data-toggle="collapse" href="#collapseWallet" aria-expanded="false" aria-controls="collapseWallet"></span>
            </ul>
        </li>
        <li class="collapse" id="collapseWallet">
            <ul class="clearfix">
                <span class="pull-left"><fmt:message key="freezeBalance" />：</span>
                <c:if test="${!empty list}">
                    <c:forEach var="item2" items="${list }"  varStatus="status" >
                        <c:choose>
                            <c:when test="${item2.stockinfoId == btc }">
                                <li class="pull-left col-xs-4">
                                    <img src="${imagesPath}/bitms/icon-logo-a.png"/>
                                    <fmt:formatNumber value="${item2.frozenAmt}" pattern="#0.0000" />
                                </li>
                            </c:when>
                            <c:when test="${item2.stockinfoId == ltc }">
                                <li class="pull-left col-xs-4">
                                    <img src="${imagesPath}/bitms/icon-logo-b.png"/>
                                    <fmt:formatNumber value="${item2.frozenAmt}" pattern="#0.0000" />
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="pull-left col-xs-3">
                                        ${item2.stockinfoId }:<fmt:formatNumber value="${item2.frozenAmt}" pattern="0.0000" />
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:if>
            </ul>
        </li>
    </ul>&ndash;%&gt;
</div>
--%>
