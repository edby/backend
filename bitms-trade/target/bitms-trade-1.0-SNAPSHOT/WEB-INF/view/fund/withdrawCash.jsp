<%@ page import="lombok.experimental.var" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
                <li onclick="jumpUrl('${ctx}/fund/chargeCash')">
                    <a href="#" data-toggle="tab"><%--现金充值--%><fmt:message key="fund.chargeCash.title" /></a>
                </li>
                <li class="active">
                    <a href="#" data-toggle="tab"><%--现金提现--%><fmt:message key="fund.raiseCash.title" /></a>
                </li>
            </ul>
            <div class="panel">
                <div class="tabbable" id="tabs-640621">
                    <ul class="nav nav-tabs bitms-tabs">
                        <li class="active">
                            <a href="#" data-toggle="tab">EUR</a>
                        </li>
                    </ul>
                    <div class="tab-content clearfix">
                        <div class="tab-pane clearfix active bitms-bg1 col-xs-12 pt10">
                            <form:form id="addRaise" class="form-horizontal" method="post" data-widget="validator" >
                                <input autocomplete="off" name="stockinfoId" hidden value="${stockinfoId}" />
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"><%--可用余额--%><fmt:message key='fund.raise.availableBalance' /></label>
                                    <div class="col-sm-4 mt5">
                                        <span id="currAmt"><fmt:formatNumber type="number" value="${enableAmount}" pattern="#,##0.000000" maxFractionDigits="4"/></span>${coin}
                                    </div>
                                </div>
                                <c:choose>
                                    <c:when test="${empty bank}">
                                        <c:choose>
                                            <c:when test="${!certStatus}">
                                                <div class="form-group">
                                                    <div class="col-sm-offset-2 col-sm-8 col-xs-12">
                                                        <div class="alert alert-danger mb0 p5" onclick="jumpUrl('${ctx}/account/certificationDoing')" role="alert" style="display: inline-block;">
                                                            <strong>
                                                                <a class="text-danger"><%--请先登记证件信息--%><fmt:message key='fund.raiseCash.tip' /></a>
                                                            </strong>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="form-group">
                                                    <div class="col-sm-offset-2 col-sm-8 col-xs-12">
                                                        <button type="button" data-toggle="modal" data-target="#addrMgePop" class="btn btn-primary"><%--添加--%><fmt:message key='account.setting.add' /></button>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="form-group">
                                            <label class="col-sm-2 col-xs-12 control-label"><%--提现信息--%><fmt:message key="fund.raiseCash.information" /></label>
                                            <div class="col-sm-8 col-xs-12">
                                                <div style="display: inline-block;">
                                                    <ul class="list-group bitms-c4 mb0">
                                                            <%--<li class="list-group-item pl0"><fmt:message key="fund.raiseCash.bank" />：${bank.bankName}</li>--%><%--银行名称--%>
                                                        <li class="list-group-item pl0"><%--开户人--%><fmt:message key="fund.raiseCash.accountHolder" />：${realName}
                                                            <c:choose>
                                                                <c:when test="${!certStatus}"><a class="text-danger" onclick="jumpUrl('${ctx}/account/certificationDoing')"><%--请先登记证件信息--%><fmt:message key='fund.raiseCash.tip' /></a></c:when>
                                                                <c:otherwise></c:otherwise>
                                                            </c:choose>
                                                        </li>
                                                        <li class="list-group-item pl0"><%--卡号--%><fmt:message key="fund.raiseCash.IBAN" />：${bank.cardNo}</li>
                                                        <li class="list-group-item pl0">SWIFT/BIC: ${bank.swiftBic}</li>
                                                        <li class="list-group-item pl0 text-danger">
                                                            <span class="glyphicon glyphicon-info-sign"></span>
                                                            <span><%--请确保银行开户人信息跟护照认证的信息一致。--%><fmt:message key="fund.chargeCash.tip2" /></span><br>
                                                        </li>
                                                        <li class="list-group-item pl0">
                                                            <button type="button" data-toggle="modal" data-target="#addrMgePop" class="btn btn-primary"><%--修改--%><fmt:message key='account.setting.modify' /></button>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="withdrawpanel">
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label"><%--提现金额--%><fmt:message key='fund.raiseCash.amount' /></label>
                                                <div class="col-sm-2 text-left ui-form-item">
                                                    <input autocomplete="off" type="text" class="form-control" name="occurAmt" id="occurAmt" data-display="Amount"/>
                                                </div>
                                                <small class="text-info">
                                                    <span><%--当日剩余提现额度：--%><fmt:message key="fund.raise.remainMoney" /></span>
                                                    <span id="quota">${canUsing}&nbsp;${coin}</span>
                                                </small>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label"><%--提现费率--%><fmt:message key='fund.raiseCash.rate' /></label>
                                                <div class="col-sm-2 text-left ui-form-item">
                                                    <fmt:formatNumber type="number" value="${feeRate*100}" pattern="#,##0.00" maxFractionDigits="2"/>%
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label"><%--资金密码--%><fmt:message key='fund.raise.fundPassword' /></label>
                                                <div class="col-sm-2 ui-form-item">
                                                    <input autocomplete="new-password" type="password" name="fundPwd" class="form-control" style="background-color: rgba(38,40,46,0.25);" data-display="Payment password"/>
                                                </div>
                                                <small class="text-danger col-sm-3 fundPwdTip" onclick="jumpUrl('${ctx}/account/setting')" style="display: none;cursor: pointer">Please set payment password first!</small>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label"><%--谷歌验证码--%><fmt:message key='fund.raise.googleCode' /></label>
                                                <div class="col-sm-2 ui-form-item">
                                                    <input autocomplete="off" type="text" class="form-control" name="ga" data-display="GA code">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-sm-offset-2 col-sm-10">
                                                    <button type="button" id="addraisebutton" class="btn btn-primary"><%--立即提现--%><fmt:message key='fund.raise.immediateWithdrawal' /></button>
                                                </div>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <form data-widget="validator" name="withdrawForm" id="withdrawForm" method="post">
                    <input autocomplete="off" name="stockinfoId" hidden value="${stockinfoId}" />
                    <h4 class="text-success"><%--提现记录--%><fmt:message key='fund.raise.withdrawalRecord' /></h4>
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th><%--提现时间--%><fmt:message key='fund.raise.withdrawalTime' /></th>
                                    <th><%--卡号--%><fmt:message key="fund.raiseCash.IBAN" /></th>
                                    <th><%--提现金额--%><fmt:message key="fund.raiseCash.amount" /></th>
                                    <th><%--提现手续费--%><fmt:message key="fund.raiseCash.fee" /></th>
                                    <th><%--审核状态--%><fmt:message key='fund.raise.approvalStatus' /></th>
                                    <th><%--汇出状态--%><fmt:message key='fund.raise.transferStatus' /></th>
                                    <th><%--操作--%><fmt:message key='operation' /></th>
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
<%--添加/修改EUR地址--%>
<div class="modal fade" id="addrMgePop" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title text-success"><%--添加/修改提现信息--%><fmt:message key='fund.raiseCash.addModify' /></h4>
            </div>
            <div class="modal-body">
                <form:form class="form-horizontal" id="addAddr" method="post"  data-widget="validator">
                    <input autocomplete="off" name="stockinfoId" hidden value="${stockinfoId}" />
                    <%--<div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label">&lt;%&ndash;银行名称&ndash;%&gt;<fmt:message key="fund.raiseCash.bank" /></label>
                        <div class="col-xs-7 ui-form-item">
                            <input type="text" autocomplete="off" name="bankName" onKeyUp="value=value.replace(/[^a-zA-Z\s]/g,'')" class="form-control" data-display="Bank"/>
                        </div>
                    </div>--%>
                    <div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"><%--开户人--%><fmt:message key='fund.raiseCash.accountHolder' /></label>
                        <div class="col-xs-7 ui-form-item">
                            <c:choose>
                                <c:when test="${!certStatus}"><a color="text-danger" onclick="jumpUrl('${ctx}/account/certificationDoing')"><%--请先登记证件信息--%><fmt:message key='fund.raiseCash.tip' /></a></c:when>
                                <c:otherwise>
                                    ${realName}
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"><%--卡号--%><fmt:message key='fund.raiseCash.IBAN' /></label>
                        <div class="col-xs-7 ui-form-item">
                            <input type="text" autocomplete="off" name="cardNo" onKeyUp="value=value.replace(/[^a-zA-Z0-9]/g,'')" class="form-control" data-display="IBAN"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label">SWIFT/BIC</label>
                        <div class="col-xs-7 ui-form-item">
                            <input type="text" autocomplete="off" name="swiftBic" onKeyUp="value=value.replace(/[^a-zA-Z0-9]/g,'')" class="form-control" data-display="SWIFT/BIC"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"><%--资金密码--%><fmt:message key='fund.raise.fundPassword' /></label>
                        <div class="col-xs-7 ui-form-item">
                            <input type="password" autocomplete="new-password" name="fundPwd" class="form-control" style="background-color: rgba(38,40,46,0.25);" data-display="Payment password"/>
                            <small class="text-danger fundPwdTip" onclick="jumpUrl('${ctx}/account/setting')" style="display: none;cursor: pointer">Please set payment password first!</small>
                        </div>
                    </div>
                    <div class="securityPolicyDiv2">
                        <div class="form-group">
                            <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"><%--短信验证码--%><fmt:message key='account.setting.smsCode' /></label>
                            <div class="col-xs-7 ui-form-item">
                                <div class="input-group">
                                    <input autocomplete="off" type="text" class="form-control" name="sms" data-display="SMS verification">
                                    <span class="input-group-btn">
                                        <button class="btn btn-primary btn-block" type="button" id="authCodeAddrBtn"><%--发送验证码--%><fmt:message key='fund.raise.sendCode' /></button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"><%--谷歌验证码--%><fmt:message key='fund.raise.googleCode' /></label>
                            <div class="col-xs-7 ui-form-item">
                                <input autocomplete="off" type="text" class="form-control" name="ga" data-display="GA code">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"></label>
                        <div class="col-xs-7">
                            <button type="button" id="addAddrBtn" class="btn btn-primary"><%--确定--%><fmt:message key='determine' /></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<script id="list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDate $value.currentDate}}</td>
        <td>{{$value.withdrawCardNo}}</td>
        <td>{{$subFlag $value.occurAmt,$value.fee}}</td>
        <td>{{$formatFlag2 $value.fee}}</td>
        <td>{{$formatFlag $value.approveStatus}}</td>
        <td>{{$formatFlag $value.transferStatus}}</td>
        <td>{{$actionFlag $value.approveStatus,$value.id}}</td>
    </tr>
    {{/each}}
</script>
<script>
    var validator;
    seajs.use([ 'pagination', 'template', 'moment', 'validator','i18n'  ], function(Pagination, Template, moment,Validator,I18n){
        $('.pageLoader').hide();
        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });
        template.helper('$formatFlag', function(flag) {
            return getDictValueByCode(flag);
        });
        template.helper('$subFlag', function(occurAmt,fee) {
            return ((parseFloat(occurAmt)*10000-parseFloat(fee)*10000)/10000).toFixed(4);
        });
        template.helper('$formatFlag2', function(flag) {
            if (flag == null || flag == '') {
                flag=0;
                return flag.toFixed(4);
            } else {
                return flag.toFixed(4);
            }
        });
        template.helper('$actionFlag', function(flag,id) {
            if (flag == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING %>' || flag == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_WAITING_EMAIL_CONFIRM %>' ) {
                return '<div class="btn btn-primary btn-xs" onclick="cancel(\'id'+id+'\')"><fmt:message key='cancel' /><div>';
            } else {
                return '';
            }
        });

        renderPage = new Pagination({
            url : "${ctx}/fund/withdrawCash/withdrawCashList",
            elem : "#pagination",
            form : $("#withdrawForm"),
            rows : 5,
            method : "post",
            handleData : function(json) {
                var html = Template.render("list_tpl", json);
                $("#list_emement").html(html);
            }
        });

        <%--添加提币地址--%>
        validator = new Validator();
        $("#addAddrBtn").on("click",function () {
            validator.destroy();
            validator = new Validator({
                element: '#addAddr',
                autoSubmit: false,<%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url : '${ctx}/fund/withdrawCash/withdrawCashAddrAdd',
                            type : 'post',
                            data : $("#addAddr").serialize(),
                            success : function(data,textStatus, jqXHR) {
                                $('#addAddr').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                data = JSON.parse(data);
                                if (data.code == 200) {
                                    <%--location.reload();--%>
                                    $('#addrMgePop').modal('toggle');
                                    <%--清理表单--%>
                                    $("#addAddr input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                    remind(remindType.success,data.message,500);
                                    setTimeout("location.reload();",500);
                                }
                                else {
                                    remind(remindType.error,data.message,1000);
                                }
                            }
                        });
                    }
                }
            }).addItem({
                element: '#addAddr [name=cardNo]',
                required : true,
                rule:'maxlength{max:48} minlength{min:10} iban LetterNumber'
            }).addItem({
                element: '#addAddr [name=swiftBic]',
                required : true,
                rule:'maxlength{max:20} LetterNumber'
            }).addItem({
                element: '#addAddr [name=fundPwd]',
                required : true,
                rule:'maxlength{max:20}'
            }).addItem({
                element: '#addAddr [name=ga]',
                required: true
            }).addItem({
                element: '#addAddr [name=sms]',
                required: true
            });
            $("#addAddr").submit();
        });

        <%--提现申请--%>
        validator = new Validator();
        $("#addraisebutton").on("click",function () {
            validator.destroy();
            validator = new Validator({
                element: '#addRaise',
                autoSubmit: false,<%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url : '${ctx}/fund/withdrawCash/withdrawCash',
                            type : 'post',
                            data : $("#addRaise").serialize(),
                            success : function(data,textStatus, jqXHR) {
                                $('#addRaise').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                data = JSON.parse(data);
                                if (data.code == 200) {
                                    remind(remindType.success,data.message,500);
                                    setTimeout("location.reload();",500);
                                    $("#currAmt").text((parseFloat($("#currAmt").text())-parseFloat($("#occurAmt").val())*(${feeRate}+1)).toFixed(4));
                                    $("#addRaise input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                    renderPage.render(true);
                                }
                                else {
                                    remind(remindType.error,data.message,1000);
                                }
                            },
                            error : function(
                                XMLHttpRequest,
                                textStatus) {
                                console.log(textStatus);
                            }
                        });
                    }
                }
            }).addItem({
                element: '#addRaise [name=occurAmt]',
                required : true,
                rule:'number min{min:100} max{max:100000} numberOfDigits{maxLength:2}'
            }).addItem({
                element: '#addRaise [name=fundPwd]',
                required : true,
                rule:'maxlength{max:20}'
            }).addItem({
                element: '#addRaise [name=ga]',
                required: true
            });
            $("#addRaise").submit();
        });

        <%--获取短信验证码--%>
        $("#authCodeAddrBtn").on("click",function () {
            if('${account.walletPwd}' != null || '${account.walletPwd}' != '') {
                $.ajax({
                    url: '/common/bind/sendSms',
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == bitms.success) {
                            countDown($('#authCodeAddrBtn'), I18n.prop('generic.sendsms'));
                        }else if(data.code == bitms.smsSendError){
                            remind(remindType.error,  I18n.prop('generic.sendsmsRepeat'));
                        }else{
                            remind(remindType.error, data.message, 1000);
                        }
                    }
                });
            }
        });
    });

    <%--取消提现--%>
    function cancel(id){
        $.ajax({
            url : '${ctx}/fund/withdrawCash/withdrawCashCancel',
            data : {
                "id" : id.substr(2),
                "stockinfoId" : ${stockinfoId}
            },
            type : "POST",
            cache : false,
            async : false,<%--false=同步调用，锁定其它JS操作--%>
            dataType : "json",
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                alert('服务器连接失败，请稍候重试！');
            },
            success : function(data, textStatus, jqXHR) {
                $('#csrf-form').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                setCsrfToken("csrf-form");
                if (data.code == bitms.success) {
                    remind(remindType.success, data.message,500);
                    setTimeout("location.reload();",500);
                } else {
                    remind(remindType.error, data.message,1000);
                }
            }
        });
    }
    $(function(){
        <%--fundPwdTip--%>
        if('${account.walletPwd}' == null || '${account.walletPwd}' == ''){
            $('.fundPwdTip').show();
            $('#addAddrBtn').attr("disabled", true);
            $('#authCodeAddrBtn').attr("disabled",true);
            $('input[name=fundPwd]').attr("disabled",true);
        }else{
            $('.fundPwdTip').hide();
            $('#addAddrBtn').attr("disabled", false);
            $('#authCodeAddrBtn').attr("disabled", false);
            $('input[name=fundPwd]').attr("disabled",false);
        }
    });
</script>
</body>
</html>
