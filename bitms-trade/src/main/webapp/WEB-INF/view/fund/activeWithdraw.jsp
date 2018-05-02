<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp"%>
<html>
<body>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp"%>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <div class="col-sm-12 column">
            <%--开始代码位置--%>
            <div class="panel clearfix">
                <div class="col-sm-offset-4 col-sm-4 bitms-bg1 mt30 mb30 bitms-b bitms-radius6 px0">
                    <c:if test="${status == 'yes' }">
                        <div class="text-center mt30">
                            <img src="${imagesPath}/common/activationYes.png">
                            <h3 class="mt30 bitms-c2"><fmt:message key="fund.raise.confirm.success"/></h3>
                        </div>
                        <div class="text-center bitms-bg2 mt30 py50 bitms-radius6">
                            <span class="bitms-c2"><fmt:message key="fund.raise.apply"/><fmt:message key="fund.raise.confirm.success"/></span>
                        </div>
                    </c:if>
                    <c:if test="${status == 'no' }">
                        <div class="text-center mt30">
                            <img src="${imagesPath}/common/activationNo.png">
                            <h3 class="mt30 bitms-c2"><fmt:message key="fund.raise.confirm.fail"/></h3>
                        </div>
                        <div class="text-center bitms-bg2 mt30 py50 bitms-radius6">
                            <span class="bitms-c2"><fmt:message key="fund.raise.apply"/><fmt:message key="fund.raise.confirm.fail"/></span>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function toUrl(){
        location.href="${ctx}/fund/withdraw";
    }
    $(document).ready(function() {
        window.setTimeout("toUrl()",10000);
    });
</script>
</body>
</html>
