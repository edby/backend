<%@ page import="com.blocain.bitms.security.OnLineUserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="certificationInfo" value="${fns:getCertificationInfo()}"/>
<div class="row clearfix">
    <div class="col-md-12 column">
        <nav class="navbar navbar-default" role="navigation">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button>
                <a class="navbar-brand" href="${ctx}/spot/leveragedSpotTrade?exchangePair=btc2usd">
                    <img src="${imagesPath}/bitms/bitms.svg" />
                </a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class="dropdown" id="lg_switch">
                        <a href="javascript:;" id="lg_curr" class="dropdown-toggle" data-toggle="dropdown"></a>
                        <ul class="dropdown-menu" aria-labelledby="dropdown">
                            <li data="zh_CN" class="lg-item">
                                <a href="javascript:;" onclick="changeLanguage('zh_CN')"><img src="${imagesPath}/bitms/icon-state2.png" alt="">简体中文</a>
                            </li>
                            <li class="divider">
                            </li>
                            <li data="zh_HK" class="lg-item">
                                <a href="javascript:;" onclick="changeLanguage('zh_HK')"><img src="${imagesPath}/bitms/icon-state2.png" alt="">繁体中文</a>
                            </li>
                            <li class="divider">
                            </li>
                            <li data="en_US" class="lg-item">
                                <a href="javascript:;" onclick="changeLanguage('en_US')"><img src="${imagesPath}/bitms/icon-state1.png" alt=""/>English</a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <%--<li>
                        <a href="${ctx}/spot/pureSpotTrade?exchangePair=biex2btc">
                            <span class="glyphicon glyphicon-refresh"></span>
                            <span>&lt;%&ndash;现货交易&ndash;%&gt;<fmt:message key="topNav.spot" /></span>
                        </a>
                    </li>--%>
                    <li <%--data-toggle="modal" data-target=".comm"--%>>
                        <a href="${ctx}/spot/leveragedSpotTrade?exchangePair=btc2usd">
                            <span class="glyphicon glyphicon-retweet"></span>
                            <span><%--杠杆交易--%><fmt:message key="topNav.margin" /></span>
                        </a>
                    </li>
                    <%--<li>
                        <a name="177777777702"  onclick="jumpBaseUrl(this,'jumpSpotContractTradeId','jumpSpotContractTradeForm')">
                            <span class="glyphicon glyphicon-refresh"></span>
                            <span>&lt;%&ndash;合约交易&ndash;%&gt;<fmt:message key="topNav.contractTrade" /></span>
                        </a>
                    </li>--%>
                    <li>
                        <a href="${ctx}/spot/historyEntrust">
                            <span class="glyphicon glyphicon-duplicate"></span>
                            <span><%--交易查询--%><fmt:message key="topNav.tradeQuery" /></span>
                        </a>
                    </li>
                    <li>
                        <a href="${ctx}/fund/accountAsset">
                            <span class="glyphicon glyphicon-briefcase"></span>
                            <span><%--资金管理--%><fmt:message key="topNav.fund" /></span>
                        </a>
                    </li>
                    <%--<li>
                        <a href="${ctx}/fund/biexCandy">
                            <span class="glyphicon glyphicon-gift"></span>
                            <span>&lt;%&ndash;BITMS糖果&ndash;%&gt;<fmt:message key="topNav.candy" /></span>
                        </a>
                    </li>--%>
                    <li>
                        <a href="${ctx}/common/notice">
                            <span class="glyphicon glyphicon-volume-up"></span>
                            <span><%--公告--%><fmt:message key="topNav.notice" /></span>
                        </a>
                    </li>
                    <%--<li>
                        <a href="${ctx}/common/message">
                            <span class="glyphicon glyphicon-envelope"></span>
                            <span>&lt;%&ndash;消息&ndash;%&gt;<fmt:message key="notice.message" /></span>
                        </a>
                    </li>--%>
                    <li>
                        <a href="${ctx}/account/helpCenter">
                            <span class="glyphicon glyphicon-question-sign"></span>
                            <span><%--帮助--%><fmt:message key="topNav.help" /></span>
                        </a>
                    </li>
                    <li class="dropdown phoneOpen">
                        <a href="javascript:;" class="dropdown-toggle mb5" data-toggle="dropdown"><%=OnLineUserUtils.getEmail()==null?"":OnLineUserUtils.getEmail() %><strong class="caret"></strong></a>
                        <ul class="dropdown-menu topnav-right" aria-labelledby="dropdown">
                            <li class="uid clearfix">
                                <i class="iconfont fs16 text-success">&#xe61f;</i>
                                <span class="text-success">UID&nbsp;<%=OnLineUserUtils.getUnid()==null?"":OnLineUserUtils.getUnid() %>
                                     <c:choose>
                                         <c:when test="${certificationInfo == null}">
                                              <a href="${ctx}/account/certificationDoing" class="text-danger pull-right">
                                                  <i class="glyphicon glyphicon-info-sign" aria-hidden="true"></i><%--未认证--%><fmt:message key="fund.raise.unauthenticated" />
                                              </a>
                                         </c:when>
                                         <c:when test="${certificationInfo != null && certificationInfo.status == 0}">
                                              <a href="${ctx}/account/certification" class="text-danger pull-right">
                                                  <i class="glyphicon glyphicon-info-sign" aria-hidden="true"></i><%--审核中--%><fmt:message key="account.setting.identityAudit" />
                                              </a>
                                         </c:when>
                                         <c:when test="${certificationInfo != null && certificationInfo.status == 1}">
                                             <a href="${ctx}/account/certification" class="text-success pull-right">
                                                  <i class="glyphicon glyphicon-ok-sign" aria-hidden="true"></i><%--已认证--%><fmt:message key="fund.raise.authenticated" />
                                              </a>
                                         </c:when>
                                         <c:when test="${certificationInfo != null && certificationInfo.status == 2}">
                                             <a href="${ctx}/account/certification" class="text-danger pull-right">
                                                  <i class="glyphicon glyphicon-info-sign" aria-hidden="true"></i><%--未通过--%><fmt:message key="account.setting.identityNotPass" />
                                              </a>
                                         </c:when>
                                     </c:choose>
                                </span>
                            </li>
                            <li class="clearfix">
                                <a href="/fund/charge" class="btn btn-primary nav-link bitms-left"><i class="glyphicon glyphicon-log-in" aria-hidden="true"></i><%--充币--%><fmt:message key="fund.accountAsset.charge" /></a>
                                <a href="/fund/withdraw" class="btn btn-primary nav-link bitms-right"><i class="glyphicon glyphicon-log-out" aria-hidden="true"></i><%--提币--%><fmt:message key="fund.accountAsset.raise" /></a>
                            </li>
                            <li class="clearfix">
                                <a href="${ctx}/account/setting"  class="btn btn-primary nav-link">
                                    <i class="iconfont fs16">&#xe623;</i><%--安全中心--%><fmt:message key="account.setting.safeCenter" />
                                </a>
                            </li>
                            <li class="clearfix">
                                <a id="loginOut" href="#"   class="btn btn-danger nav-link">
                                    <i class="glyphicon glyphicon-off" aria-hidden="true"></i><%--退出--%><fmt:message key="topNav.logout" />
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </nav>
    </div>
</div>
<div class="modal fade comm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog ga-modal-dialog" style="top: 40%;" role="document">
        <div class="modal-content bingGa-con1" style="border-width: 2px">
            <div class="modal-body bitms-con text-center fs25 bitms-c2">
                <span class="glyphicon glyphicon-exclamation-sign"></span>
                <span>Comming soon！</span>
            </div>
        </div>
    </div>
</div>
<script>
    seajs.use(['cookie', 'i18n'], function (Cookie, I18n) {
        $('#lg_switch li.lg-item').each(function (i, item) {
            var lang = Cookie.get('locale');
            if (!lang || lang.length < 2) {
                lang = (navigator.languages && navigator.languages.length > 0) ? navigator.languages[0]
                    : (navigator.language || navigator.userLanguage);
                if ("zh" == lang) lang = "zh_CN";
                if ("en" == lang) lang = "en_US";
            }
            lang = lang.toLowerCase();
            lang = lang.replace(/-/, "_");
            if (lang.length > 3) {
                lang = lang.substring(0, 3) + lang.substring(3).toUpperCase();
            }
            window.locale = lang;

            /*显示当前语言*/
            if ($(item).attr('data') == (lang)) {
                var content = $(this).find('a').html();
                $('#lg_curr').html(content+'<strong class="caret"></strong>');
            }
        });
        $('#loginOut').on('click', function () {
            $.ajax({
                url: '/common/logout',
                type: 'post',
                dataType: 'json',
                success: function (json) {
                    if (json.code == bitms.success) {
                        location.href = "/login";
                    }
                }
            });
        });
        //手机端安全中心面板直接显示
        if($(window).width() < 769){
            $('.phoneOpen').addClass('open');
            $('.phoneOpen>a').removeAttr('data-toggle');
        }else{
            $('.phoneOpen>a').attr('data-toggle','dropdown');
            $('.phoneOpen').removeClass('open');
        }
        $(window).resize(function(){
            if($(window).width() < 769){
                $('.phoneOpen').addClass('open');
                $('.phoneOpen>a').removeAttr('data-toggle');
            }else{
                $('.phoneOpen>a').attr('data-toggle','dropdown');
                $('.phoneOpen').removeClass('open');
            }
        })
    });
</script>
