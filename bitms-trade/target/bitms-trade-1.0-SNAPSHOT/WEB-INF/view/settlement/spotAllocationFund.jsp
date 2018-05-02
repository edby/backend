<%@ page import="com.blocain.bitms.trade.trade.enums.TradeEnums" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp"%>
<html>
<body>
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp" %>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <div class="col-sm-12 column">
            <ul class="nav nav-tabs bitms-tabs" style="background-color:transparent;margin-right:0px;margin-top:-15px;">
                <li onclick="jumpUrl('${ctx}/spot/historyEntrust')">
                    <a data-toggle="tab"><%--历史委托--%><fmt:message key="spot.query.historyEntrust" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/settlement/explosionEntrust')">
                    <a href="#" data-toggle="tab"><%--爆仓委托--%><fmt:message key="spot.query.explosionEntrust" /></a>
                </li>
                <li  class="active" onclick="jumpUrl('${ctx}/settlement/spotAllocationFund')">
                    <a data-toggle="tab"><%--分摊基金--%><fmt:message key="spot.query.allpcationFund" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/settlement/spotSettlement')">
                    <a href="#" data-toggle="tab"><%--交割结算--%><fmt:message key="spot.query.settlement" /></a>
                </li>
            </ul>
            <form class="clearfix" data-widget="validator" name="entrustForm" id="entrustForm">
                <div class="panel clearfix">
                    <div class="col-sm-12 px0">
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--交易对--%><fmt:message key="spot.query.transPair" /></span>
                        </div>
                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <select id="relatedStockinfoId" name="relatedStockinfoId"  class="form-control">
                                <c:if test="${!empty stockinfos}">
                                    <c:forEach var="item" items="${stockinfos }"  varStatus="status" >
                                        <option value="${item.id}" ${item_index==0?"selected":""}>${item.stockName}</option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>
                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <button type="button" class="btn btn-primary" id="searchBtn"><%--查询--%><fmt:message key='search' /></button>
                        </div>
                    </div>
                </div>
                <div class="panel clearfix">
                    <div class="table-responsive">
                        <h4 class="text-success"><%--分摊基金--%><fmt:message key='spot.query.allpcationFund' /></h4>
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th><%--时间--%><fmt:message key='time' /></th>
                                <th><%--合约--%><fmt:message key='spot.query.contract' /></th>
                                <th><%--分摊基金原始数量--%><fmt:message key='spot.query.allpcationFund' /><fmt:message key='spot.query.originalQuantity' /></th>
                                <th><%--分摊基金分摊数量--%><fmt:message key='spot.query.allpcationFund' /><fmt:message key='spot.query.allpcationQuantity' /></th>
                                <th><%--分摊基金最新余额--%><fmt:message key='spot.query.allpcationFund' /><fmt:message key='spot.query.latestBalance' /></th>
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
                </div>
            </form>
        </div>
    </div>
</div>
<script id="list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDateTime $value.settlementTime}}</td>
        <td>{{$value.stockCode}}</td>
        <td>{{$Fix8Flag $value.reserveOrgAmt}}</td>
        <td>{{$Fix8Flag $value.reserveAllocatAmt}}</td>
        <td>{{$Fix8Flag $value.reserveLastAmt}}</td>
    </tr>
    {{/each}}
</script>

<script>

    var validator;
    var renderRecivePage;
    seajs.use(['pagination','validator','template','moment'], function (Pagination,Validator,Template,moment) {
        $('.pageLoader').hide();
        validator = new Validator();

        template.helper('$formatDateTime', function (millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss.SSS");
        });
        template.helper('$Fix2Flag', function(flag) {
            return  parseFloat((flag)).toFixed(2);
        });
        template.helper('$Fix4Flag', function(flag) {
            return  parseFloat((flag)).toFixed(4);
        });
        template.helper('$Fix8Flag', function(flag) {
            return  parseFloat((flag)).toFixed(8);
        });
        template.helper('$statusFlag', function(flag) {
            if(flag == 1 || flage== true)
            {
                return '<fmt:message key='spot.query.delivery' />';
            }
            else
            {
                return '<fmt:message key='spot.query.settle' />';
            }
        });

        renderRecivePage = new Pagination({
            beforeAjax:function(){
                $('#searchBtn').attr("disabled", true);
                $('.reg-pop').show();
            },
            url : "${ctx}/settlement/spotSettlement/data",
            elem : "#pagination",
            form : $("#entrustForm"),
            rows : 10,
            method : "post",
            handleData : function(json) {
                var html = Template.render("list_tpl", json);
                $("#list_emement").html(html);
                $('#searchBtn').attr("disabled", false);
                $('.reg-pop').hide();
            }
        });
    });

    $("#searchBtn").click(function() {
        renderRecivePage.render(true);
    });
</script>
</body>
</html>
