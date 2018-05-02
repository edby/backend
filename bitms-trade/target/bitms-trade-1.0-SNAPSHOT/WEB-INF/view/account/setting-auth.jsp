<%--实名认证--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="thumbnail bitms-b bitms-bg1 bitms-con pb15 mb15">
    <div class="caption">
        <div class="bitms-bg2 bitms-height1 px10"><fmt:message key="account.setting.identity"/></div>
        <div class="col-md-1 col-sm-2 col-xs-1 mt15">
            <i class="iconfont text-success fs30">&#xe61f;</i>
        </div>
        <div class="col-md-8 col-sm-10 col-xs-8 mt10">
            <c:choose>
                <c:when test="${accountCertification == null}">
                    <div class="certificationTxt1 bitms-c1"><fmt:message key="fund.raise.unauthenticated"/></div>
                    <div class="certificationTxt2"><fmt:message key="account.setting.identityTxt1"/>。</div>
                </c:when>
                <c:when test="${accountCertification != null && accountCertification.status == 0}">
                    <div class="certificationTxt1 text-success"><fmt:message key="account.setting.identityAudit"/></div>
                    <div class="certificationTxt2"><fmt:message key="account.setting.identityTxt1"/>。</div>
                </c:when>
                <c:when test="${accountCertification != null && accountCertification.status == 1}">
                    <div class="certificationTxt1 text-success"><fmt:message key="fund.raise.authenticated"/></div>
                    <div class="certificationTxt2"><fmt:message key="account.setting.identityTxt2"/>！</div>
                </c:when>
                <c:when test="${accountCertification != null && accountCertification.status == 2}">
                    <div class="certificationTxt1 bitms-c1"><fmt:message key="fund.raise.unauthenticated"/></div>
                    <div class="certificationTxt2"><fmt:message key="account.setting.identityTxt1"/>。</div>
                </c:when>
            </c:choose>
        </div>
        <c:choose>
            <c:when test="${accountCertification == null}">
                <button class="btn btn-primary col-md-2 col-sm-12 col-xs-2 mt10 certificationBtn1 pull-right" data-toggle="modal" data-target=".certification"><fmt:message key="account.setting.certification"/></button>
                <button class="btn btn-primary col-md-2 col-sm-12 col-xs-2 mt10 certificationBtn2 bitms-hide pull-right" data-toggle="modal" data-target=".certificationInfo"><fmt:message key="account.setting.information"/></button>
            </c:when>
            <c:when test="${accountCertification != null && accountCertification.status == 0}">
                <button class="btn btn-primary col-md-2 col-sm-12 col-xs-2 mt10 certificationBtn1 pull-right disabled" data-toggle="modal" ><fmt:message key="account.setting.certification"/></button>
                <button class="btn btn-primary col-md-2 col-sm-12 col-xs-2 mt10 certificationBtn2 bitms-hide pull-right" data-toggle="modal" data-target=".certificationInfo"><fmt:message key="account.setting.information"/></button>
            </c:when>
            <c:when test="${accountCertification != null && accountCertification.status == 1}">
                <button class="btn btn-primary col-md-2 col-sm-12 col-xs-2 mt10 certificationBtn1 pull-right bitms-hide" data-toggle="modal" data-target=".certification"><fmt:message key="account.setting.certification"/></button>
                <button class="btn btn-primary col-md-2 col-sm-12 col-xs-2 mt10 certificationBtn2  pull-right" data-toggle="modal" data-target=".certificationInfo"><fmt:message key="account.setting.information"/></button>
            </c:when>
            <c:when test="${accountCertification != null && accountCertification.status == 2}">
                <button class="btn btn-primary col-md-2 col-sm-12 col-xs-2 mt10 certificationBtn1 pull-right" data-toggle="modal" data-target=".certification"><fmt:message key="account.setting.certification"/></button>
                <button class="btn btn-primary col-md-2 col-sm-12 col-xs-2 mt10 certificationBtn2 bitms-hide pull-right" data-toggle="modal" data-target=".certificationInfo"><fmt:message key="account.setting.information"/></button>
            </c:when>
        </c:choose>
    </div>
</div>