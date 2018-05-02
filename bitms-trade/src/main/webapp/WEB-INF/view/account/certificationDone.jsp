<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <div class="panel">
                <div class="row">
                    <div class="view col-sm-12">
                        <div class="jumbotron" style="padding: 20px;">
                            <span class="text-success">
                                <div class="row">
                                    <div class="col-sm-5 col-xs-12">
                                        <h3 class="mb0"><span class="glyphicon glyphicon-ok-circle mr10"></span><%--你已经完成身份认证--%><fmt:message key="account.setting.CertificateDone"/>！</h3>
                                    </div>
                                </div>
                            </span>
                        </div>
                    </div>
                    <div class="col-md-8">
                        <ul class="list-group">
                            <h3 class="text-success"><%--认证信息--%><fmt:message key="account.setting.CertificateInfo"/></h3>
                            <hr />
                            <li class="list-group-item">
                                <span class="col-sm-2"><%--姓氏--%><fmt:message key="account.setting.surnames"/></span>
                                <span class="col-sm-10">${accountCertification.surname}</span>
                            </li>
                            <li class="list-group-item">
                                <span class="col-sm-2"><%--名字--%><fmt:message key="account.setting.name"/></span>
                                <span class="col-sm-10">${accountCertification.realname}</span>
                            </li>
                            <li class="list-group-item">
                                <span class="col-sm-2"><%--国家与地区--%><fmt:message key="account.setting.addr"/></span>
                                <span class="col-sm-10">${country}</span>
                            </li>
                            <li class="list-group-item">
                                <span class="col-sm-2"><%--护照ID--%><fmt:message key="account.setting.passport"/>&nbsp;ID</span>
                                <span class="text-success col-sm-10">
                                    <c:set var="passportLeng" value="${accountCertification.passportId.length()}"/>
                                    ${fn:substring(accountCertification.passportId,0,3)}************${fn:substring(accountCertification.passportId,phoneLeng - 4,phoneLeng)}
                                </span>
                            </li>
                        </ul>
                    </div>
                    <div class="col-md-4"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(function(){
        $('.pageLoader').hide();
    })
</script>
</body>
</html>
