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
                        <div class="jumbotron">
                            <div class="row">
                                <div class="col-lg-1 col-sm-2 col-xs-12">
                                    <img src="${imagesPath}/bitms/unAuthorized.png"/>
                                </div>
                                <div class="col-sm-10 col-xs-12">
                                    <c:choose>
                                        <c:when test="${accountCertification != null && accountCertification.status == 0}">
                                            <h3 class="text-success"><%--请耐心等待，身份认证正在审核中--%><fmt:message key="account.setting.CertificateAuditTxt0"/>！</h3>
                                        </c:when>
                                        <c:when test="${accountCertification != null && accountCertification.status == 2}">
                                            <span class="text-success"><%--您的审核未通过，请重新认证--%><fmt:message key="account.setting.CertificateAuditTxt1"/>！</span>
                                            <br />
                                            <small><%--请您填写真实信息，实名认证一旦成功，不予修改和解除认证--%><fmt:message key="account.setting.CertificateAuditTxt2"/>。</small>
                                        </c:when>
                                        <c:when test="${accountCertification == null}">
                                            <span class="text-success"><%--您还没有进行身份认证，请立即认证--%><fmt:message key="account.setting.CertificateAuditTxt3"/>！ </span>
                                            <br />
                                            <small><%--请您填写真实信息，实名认证一旦成功，不予修改和解除认证--%><fmt:message key="account.setting.CertificateAuditTxt2"/>。</small>
                                        </c:when>
                                    </c:choose>
                                </div>
                                <div class="col-sm-2 col-xs-6">
                                    <c:choose>
                                        <c:when test="${accountCertification != null && accountCertification.status == 2}">
                                            <button onclick="jumpUrl('${ctx}/account/certificationDoing')" class="btn btn-primary btn-block"><%--认证--%><fmt:message key="account.setting.certification"/></button>
                                        </c:when>
                                        <c:when test="${accountCertification == null}">
                                            <button onclick="jumpUrl('${ctx}/account/certificationDoing')" class="btn btn-primary btn-block"><%--认证--%><fmt:message key="account.setting.certification"/></button>
                                        </c:when>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
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
