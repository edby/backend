<%@ page import="lombok.experimental.var" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp"%>
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
                <li class="active">
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
                    <div class="tab-content clearfix">
                        <div class="tab-pane clearfix active bitms-bg1 col-xs-12 pt10">
                            <form:form id="addRaise" class="form-horizontal" method="post" data-widget="validator" >
                                <input type="hidden" name="stockinfoId" id="raiseStockinfoId" value='<%=FundConsts.WALLET_BTC_TYPE %>' autocomplete="off"/>
                                <input id="activeStatus" type="hidden" name="activeStatus" value="no"/>
                                <div class="form-group" style="overflow: visible">
                                    <div class="dropdown col-sm-4">
                                        <button class="btn btn-block dropdown-toggle text-left" type="button" id="dropdownMenuListERC" data-toggle="dropdown" style="color:#151922;border-bottom:1px solid #999;background-color:#eee;" aria-haspopup="true" aria-expanded="true">
                                            <span class="pull-left mr10"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/coinLogo/BTC.svg"></span>
                                            <span class="pull-left mr10">BTC</span>
                                            <span class="pull-left mr10">-</span>
                                            <span class="pull-left">Bitcoin</span>
                                        </button>
                                        <span class="caret pull-right" style="border-width: 8px;margin-top:-20px;margin-right:5px;color: #151922;"></span>
                                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuListERC" style="width:100%;border:none;padding:12px;padding-top:0;margin:0;background-color:transparent;box-shadow: none;">
                                            <c:forEach items="${listCoin}" var="data">
                                                <button class="btn btn-block dropdown-toggle text-left ListERC" onclick="JumpURList('${data.stockType}','${data.remark}')" ${data.canWithdraw eq 'no'?'disabled':''} type="button" style="background-color:#eee;border-radius:0;color:#151922;margin-top:0;border-bottom:1px solid #999;">
                                                    <input hidden value="${data.id}">
                                                    <span class="pull-left mr10"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/coinLogo/${data.stockCode}.svg"></span>
                                                    <span class="pull-left mr10 coinERC">${data.stockCode}</span>
                                                    <span class="pull-left mr10">-</span>
                                                    <span class="pull-left">${data.stockName}</span>
                                                    <c:choose>
                                                        <c:when test="${data.canWithdraw == 'no'}">
                                                            <span class="pull-right mr20"><%--暂停--%><fmt:message key="stop" /></span>
                                                        </c:when>
                                                    </c:choose>
                                                </button>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"><%--可用余额--%><fmt:message key='fund.raise.availableBalance' /></label>
                                    <div class="col-sm-4 mt5">
                                        <span id="currAmt">&nbsp;&nbsp;0.0000</span> BTC
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 col-xs-12 control-label"><%--提现地址--%><fmt:message key='fund.raise.withdrawalsAddr' /></label>
                                    <div class="col-sm-8 col-xs-12 ui-form-item">
                                        <div class="form-control pl0 disabled coinAddr" style="box-shadow: none;background-color: #1e222d;border: 0px;cursor:default;font-weight: bold;font-size: 20px;height: 45px;display: inline-block;">
                                            <span id="lockIcon" class="glyphicon glyphicon-lock" style="display:none;"></span>
                                            <span id="addr_pos"></span>
                                            <button type="button" id="addAddrPreBtn" data-toggle="modal" data-target="#addrMgePop" class="btn btn-primary"></button>
                                        </div>
                                        <input id="addressInput" type="hidden" name="address" />
                                    </div>
                                    <small class="text-danger col-sm-offset-2 col-sm-5 col-xs-12">
                                        <div id="displayText"></div>
                                    </small>
                                </div>
                                <div id="withdrawpanel" style="display: none;">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label"><%--提现数量--%><fmt:message key='fund.raise.withdrawalsNum' /></label>
                                        <div class="col-sm-2 text-left ui-form-item">
                                            <input autocomplete="off" type="text" class="form-control" name="amount" id="amount" data-display="Amount"/>
                                        </div>
                                        <small class="text-info col-xs-12 col-sm-7 mt10">
                                            <span><%--当日剩余提币额度：--%><fmt:message key='fund.raise.remainMoney' /></span>
                                            <span id="shengyuAmt"></span>
                                        </small>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label"><%--手续费--%><fmt:message key='fund.raise.netFees' /></label>
                                        <div class="col-sm-2">
                                            <select name="feeString" id="addr_posFee" class="form-control disabled"  readonly="readonly" style="color:#888;box-shadow: none;background-color:#1e222d;border: none;cursor: default;padding-left: 0px;">
                                                <option value="${feeRate }" selected ><fmt:formatNumber type="number" value="${feeRate}" pattern="0.0000" maxFractionDigits="4"/></option>
                                            </select>
                                        </div>
                                        <small class="text-info col-xs-12 col-sm-7 mt10"><fmt:message key='fund.raise.txt' /></small>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label"><%--资金密码--%><fmt:message key='fund.raise.fundPassword' /></label>
                                        <div class="col-sm-2 ui-form-item">
                                            <input autocomplete="new-password" type="password" name="fundPwd" class="form-control" style="background-color: rgba(38,40,46,0.25);" data-display="Payment password"/>
                                        </div>
                                        <small class="text-danger col-sm-3 fundPwdTip" onclick="jumpUrl('${ctx}/account/setting')" style="display: none;cursor: pointer">Please set payment password first!</small>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-sm-offset-2 col-sm-10">
                                            <button type="button" id="addraisebutton" class="btn btn-primary"><%--立即提现--%><fmt:message key='fund.raise.immediateWithdrawal' /></button>
                                        </div>
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <form data-widget="validator" name="withdrawForm" id="withdrawForm" method="post">
                    <%-- 证券ID隐藏域 --%>
                    <input type="hidden" id="stockinfoId" name="stockinfoId" value="<%=FundConsts.WALLET_BTC_TYPE%>" />

                    <h4 class="text-success"><%--提现记录--%><fmt:message key='fund.raise.withdrawalRecord' /></h4>
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th><%--提现时间--%><fmt:message key='fund.raise.withdrawalTime' /></th>
                                <th><%--提现地址--%><fmt:message key='fund.raise.withdrawalsAddr' /></th>
                                <th><%--提现数量--%><fmt:message key='fund.raise.withdrawalNum' /></th>
                                <th><%--网络手续费--%><fmt:message key='fund.raise.netFees' /></th>
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
<%--邮件发送确认弹窗--%>
<div class="modal fade" id="mailConfirm" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title text-success"><%--邮箱确认--%><fmt:message key='fund.raise.mailConfirm' /></h4>
            </div>
            <div class="modal-body">
                <span><%--已向您的绑定邮箱发送一份确认邮件，请点击邮件中的链接完成确认(该邮件可能有延迟，请耐心等待)。--%><fmt:message key='fund.raise.MailConfirmTxt' /></span>
            </div>
        </div>
    </div>
</div>
<%--二次确认弹窗--%>
<div class="modal fade" id="confirmPop" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title text-success"><%--二次确认--%><fmt:message key='fund.raise.secondConfirm' /></h4>
            </div>
            <div class="modal-body">
                <form:form class="form-horizontal" onkeydown="if(event.keyCode==13)return false;" id="confirmForm" method="post" data-widget="validator">
                    <input id="confirmId" name="id" value="" type="hidden"/>
                    <input id="confirmAmt" name="amt" value="" type="hidden"/>
                    <div class="form-group">
                        <img id="image" class="col-xs-10 col-xs-offset-1" />
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"><%--验证码--%>Security Code</label>
                        <div class="col-xs-7 ui-form-item">
                            <input type="text" id="confirmCode" name="confirmCode" autocomplete="off" class="form-control" data-display="Verification number"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"></label>
                        <div class="col-xs-7">
                            <button type="button" id="confirmBtn" class="btn btn-primary"><%--确定--%><fmt:message key='determine' /></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<%--添加/修改提币地址--%>
<div class="modal fade" id="addrMgePop" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title text-success"><%--添加/修改提币绑定地址--%><fmt:message key='fund.raise.addUpdateAddress' /></h4>
            </div>
            <div class="modal-body">
                <form:form class="form-horizontal" id="addAddr" method="post"  data-widget="validator">
                    <div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"><%--提现--%><fmt:message key='withdrawals' /></label>
                        <div class="col-xs-7">
                            <select name="stockinfoId" id="stockinfoIdSelect" class="form-control" readonly>
                                <option value="<%=FundConsts.WALLET_BTC_TYPE%>">BTC&nbsp;<fmt:message key='address' /></option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-sm-3 col-sm-offset-1 control-label"><%--提现地址--%><fmt:message key='fund.raise.withdrawalsAddr' /></label>
                        <div class="col-xs-7 ui-form-item">
                            <input type="text" autocomplete="off" onKeyUp="value=value.replace(/[^a-zA-Z0-9]/g,'')" name="collectAddr" class="form-control" data-display="Address"/>
                        </div>
                    </div>
                    <%--<div class="form-group">
                        <label class="col-xs-5 col-sm-2 col-sm-offset-1 control-label">&lt;%&ndash;标签&ndash;%&gt;<fmt:message key='fund.raise.label' /></label>
                        <div class="col-xs-7">
                            <input type="text" autocomplete="off" name="remark" class="form-control" />
                        </div>
                    </div>--%>
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
                    <%--<div class="form-group">
                        <label class="col-xs-5 col-sm-2 col-sm-offset-1 control-label"></label>
                        <div class="col-xs-7">
                            <label>
                                <input autocomplete="off" name="certStatus" id="authCheck" type="checkbox" value="<%=FundConsts.WALLET_AUTH_STATUS_AUTH %>" />
                                <span>&lt;%&ndash;认证地址&ndash;%&gt;<fmt:message key='fund.raise.authenticationAddr' /></span>
                            </label>
                        </div>
                    </div>--%>
                    <input autocomplete="off" name="certStatus" id="authCheck" type="checkbox" checked value="<%=FundConsts.WALLET_AUTH_STATUS_AUTH %>" style="display: none" />
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
        <td>{{$formatFlag3 $value.withdrawAddr $value.transId}}</td>
        <td>{{$subFlag $value.occurAmt,$value.netFee}}</td>
        <td>{{$formatFlag2 $value.netFee}}</td>
        <td>{{$formatFlag $value.approveStatus}}</td>
        <td>{{$formatFlag $value.transferStatus}}</td>
        <td>{{$actionFlag $value.approveStatus,$value.id}}</td>
    </tr>
    {{/each}}
</script>
<script>
    <%--接收系统参数--%>
    var params = '${paramlist}';
    params=JSON.parse(params);
    var renderPage;
    var validator;
    var safe ="${account.securityPolicy}";
    seajs.use([ 'pagination', 'template', 'moment', 'validator','i18n'  ], function(Pagination, Template, moment,Validator,I18n) {
        $('.pageLoader').hide();
        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });
        template.helper('$formatFlag', function(flag) {
            return getDictValueByCode(flag);
        });
        template.helper('$subFlag', function(occurAmt,netFee) {
            return ((parseFloat(occurAmt)*10000-parseFloat(netFee)*10000)/10000).toFixed(4);
        });
        template.helper('$formatFlag2', function(flag) {
            if (flag == null || flag == '') {
                flag=0;
                return flag.toFixed(4);
            } else {
                return flag.toFixed(4);
            }
        });
        template.helper('$formatFlag3', function(flag,transId) {
            if(transId == null){
                return flag;
            }
            else{
                return flag+"&nbsp;<a class='text-success' target='_blank' href='https://tradeblock.com/bitcoin/tx/"+transId+"'><%--查看状态--%><fmt:message key="fund.charge.check" /></a>";
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
            url : "${ctx}/fund/withdraw/withdrawList",
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
                        $("#stockinfoIdSelect").attr("disabled", false);
                        $.ajax({
                            url : '${ctx}/fund/withdraw/withdrawAddrAdd',
                            type : 'post',
                            data : $("#addAddr").serialize(),
                            success : function(data,textStatus, jqXHR) {
                                $('#addAddr').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                data = JSON.parse(data);
                                if (data.code == 200) {
                                    <%--location.reload();--%>
                                    <%--认证的提币地址需要邮件激活--%>
                                    if( $("input[type='checkbox']:checked").val()=='<%=FundConsts.WALLET_AUTH_STATUS_AUTH %>' )
                                    {
                                        <%--已向您的绑定邮箱发送一封确认邮件，请点击邮件中的链接完成地址认证。--%>
                                        <%--$('#mailConfirm').modal('toggle');--%>
                                        remind(remindType.success,data.message,300);
                                        $('#addrMgePop').modal('toggle');
                                    }
                                    else
                                    {
                                        <%--未认证的提币地址--%>
                                        remind(remindType.success,data.message,300);
                                        $('#addrMgePop').modal('toggle');
                                    }
                                    <%--清理表单--%>
                                    $("#addAddr input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                    if($("#addrId").val()!='')
                                    {
                                        deleteAddr('id'+$("#addrId").val());
                                    }
                                    getDate();<%--重新加载地址列表--%>
                                }
                                else {
                                    var code = data.code;
                                    if(code == 2020 || code == 2024 ){
                                        remind(remindType.error,data.message,1000);
                                    }else{
                                        remind(remindType.error,data.message,1000);
                                    }
                                }
                            }
                        });
                    }
                }
            }).addItem({
                element: '#addAddr [name=collectAddr]',
                required : true,
                rule:'maxlength{max:64} LetterNumber'
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
        <%--提币确认--%>
        validator = new Validator();
        $("#confirmBtn").on("click",function () {
            validator.destroy();
            validator = new Validator({
                element: '#confirmForm',
                autoSubmit: false,<%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url : '${ctx}/fund/withdraw/withdraw/confirm',
                            type : 'post',
                            data : $("#confirmForm").serialize(),
                            success : function(data,textStatus, jqXHR) {
                                $('#confirmForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                data = JSON.parse(data);
                                if (data.code == 200) {
                                    $('#confirmPop').modal('toggle');
                                    $("#shengyuAmt").text((parseFloat($("#shengyuAmt").text())-parseFloat($("#confirmAmt").val())).toFixed(4));
                                    <%--未认证地址提币 需要邮件激活--%>
                                    if( data.object.withdrawAddrAuth=='<%=FundConsts.WALLET_AUTH_STATUS_UNAUTH %>' )
                                    {
                                        <%--已向您的绑定邮箱发送一封确认邮件，请点击邮件中的链接完成地址认证。--%>
                                        $('#mailConfirm').modal('toggle');
                                    }
                                    else
                                    {
                                        remind(remindType.success,data.message,300);
                                        location.reload();
                                    }
                                }
                                else {
                                    var code = data.code;
                                    if(code == 2020 || code == 2024 ){
                                        remind(remindType.error,data.message,1000);
                                    }else{
                                        remind(remindType.error,data.message,1000);
                                    }
                                }
                                renderPage.render(true);
                            }
                        });
                    }
                }
            }).addItem({
                element: '#confirmForm [name=confirmCode]',
                required : true
            });
            $("#confirmForm").submit();
        });
        <%--获取短信验证码--%>
        $("#authCodeBtn").on("click",function () {
            $.ajax({
                url: '/common/bind/sendSms',
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    if (data.code == bitms.success) {
                        countDown($('#authCodeBtn'), I18n.prop('generic.sendsms'));
                    } else {
                        remind(remindType.error, data.message,1000);
                    }
                }
            });
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
        if(index == 0){
            <c:choose>
            <c:when test="${certStatus}">
                var shengyuAmt=(params.AuthUserWithdrawBTCDayQuotaUpper-params.btcUsedAmt).toFixed(4);
                $("#shengyuAmt").text(shengyuAmt>0?shengyuAmt:0);

            </c:when>
            <c:otherwise>
                var shengyuAmt=(params.WithdrawTansferBTCDayQuotaUpper-params.btcUsedAmt).toFixed(4);
                $("#shengyuAmt").text(shengyuAmt>0?shengyuAmt:0);
            </c:otherwise>
            </c:choose>
        }
        <%--提币申请--%>
        $("#addraisebutton").on("click",function () {
            var max=0;
            if(index == 0){
                <c:choose>
                    <c:when test="${certStatus}">var up = parseFloat(params.AuthUserWithdrawBTCDayQuotaUpper);</c:when>
                    <c:otherwise>var up = parseFloat(params.WithdrawTansferBTCDayQuotaUpper);</c:otherwise>
                </c:choose>
                var btc = parseFloat($("#shengyuAmt").text());<%--可用余额--%>
                if(btc<=up){
                    if(btc<0.0001){
                        remind(remindType.error,'<fmt:message key="account.wallet.asset.insufficient" />',1000);
                        return ;
                    }
                    max=btc;
                }else{
                    max=up;
                }
            }
            <%--提币申请--%>
            validator.destroy();
            validator = new Validator({
                element: '#addRaise',
                autoSubmit: false,<%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url : '${ctx}/fund/withdraw/withdraw',
                            type : 'post',
                            data : $("#addRaise").serialize(),
                            success : function(data,textStatus, jqXHR) {
                                $('#addRaise').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                data = JSON.parse(data);
                                if (data.code == 200) {
                                    <%--location.reload();--%>
                                    $("#addRaise input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                    <%--初始化二次确认表单--%>
                                    $("#image").attr("src",data.object.image);
                                    $("#confirmId").val(data.object.id);
                                    $("#confirmAmt").val((data.object.withdrawAmt-data.object.netFee).toFixed(4));
                                    $("#confirmCode").val('');

                                    $('#confirmPop').modal('toggle');
                                }
                                else {
                                    var code = data.code;
                                    if(code == 2020 || code == 2024 || code == 30004){
                                        remind(remindType.error,data.message,1000);
                                    }else{
                                        remind(remindType.error,data.message,1000);
                                    }
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
                element: '#addRaise [name=address]',
                required : true
            }).addItem({
                element: '#addRaise [name=amount]',
                required : true,
                rule:'number min{min:0.0001} max{max:'+max+'}  numberOfDigits{maxLength:4}'
            }).addItem({
                element: '#addRaise [name=fundPwd]',
                required : true,
                rule:'maxlength{max:20}'
            }).addItem({
                element: '#addRaise [name=feeString]',
                required : true
            });


            <%--非认证地址动态添加验证
            if($("#activeStatus").val() == 'no'){
                validator.addItem({
                    element: '#addRaise [name=type]',
                    required : true
                });
                validator.addItem({
                    element: '#addRaise [name=checkCode]',
                    required : true
                });
            }else{
            }--%>
            $("#addRaise").submit();
        });
    });
    var index = 0;
    <%--取消提币--%>
    function cancel(id){
        $.ajax({
            url : '${ctx}/fund/withdraw/withdrawCancel',
            data : {
                "id" : id.substr(2)
            },
            type : "POST",
            cache : false,
            async : false,// false=同步调用，锁定其它JS操作
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
    $("#minvalue").text(params.WithdrawTansferBTCCostLow);
    $("#maxvalue").text(params.WithdrawTansferBTCCostUpper);
    $("#avervalue").text(params.WithdrawTansferBTCCostAver);
    $("#upLine").text(params.WithdrawTansferBTCQuotaUpper);
    $("#currAmt").text(params.btcEnableAmount.toFixed(4));
    $("#addr_posFee option").each(function(){
        if($(this).text() == ""+parseFloat(params.WithdrawTansferBTCCostAver).toFixed(4)){
            $(this).attr("selected","selected");
        }
    });
    <%--根据参数异步更新界面元素和地址列表--%>
    function getDate(){
        var stockinfoId=$("#stockinfoId").val();
        $("#raiseStockinfoId").val(stockinfoId);
        <%--获取地址列表--%>
        $.ajax({
            url : '${ctx}/fund/withdraw/withdrawAddr',
            data : {
                "stockinfoId" : stockinfoId
            },
            type : "POST",
            cache : false,
            async : false,<%--false=同步调用，锁定其它JS操作--%>
            dataType : "json",
            success : function(data, textStatus) {
                if (data.code == bitms.success) {
                    var list=data.rows;
                    $("#addrlist").html('');
                    $("#addr_pos").html('');
                    $("#addAddrPreBtn").text('<%--绑定--%><fmt:message key='account.setting.add' />');

                    $("#addr_pos").html('');<%--先清空--%>

                    if(list.length == 0){
                        $("#lockIcon").hide();
                        $("#addr_pos").hide();
                    }
                    else {
                        if(list[0].status=='yes')
                        {
                            $("#withdrawpanel").show();
                        }
                        $("#lockIcon").show();
                        $("#addr_pos").show();
                        $("#addr_pos").html(list[0].collectAddr);
                        $("#addressInput").val(list[0].collectAddr);
                        $("#addAddrPreBtn").text('<%--修改--%><fmt:message key='account.setting.modify' />');
                        var html='<span style="display:inline-block;width:450px;border:1px dashed;padding:3px;">'+(list[0].isActivate=='yes' && list[0].status=='no'?'<span><%--等待激活审核中：《提现地址激活协议》已发至您的邮箱，请按协议提示进行邮件回复，我们会及时审核并激活您的提现地址。--%><fmt:message key="fund.raise.humanReview" /></span><span class="timers" name="'+list[0].id+'" id="'+list[0].createDate+'"></span><div id="'+list[0].id+'"></div>':'')+'</span>';
                        if(list[0].isActivate=='yes' && list[0].status=='no'){
                            $("#displayText").html(html);
                        }
                        else{
                            $("#displayText").html('');
                        }
                    }
                    $(".timers").each(function (){
                        var curr=( new Date()).valueOf();
                        var old=$(this).attr("id");
                        var id=$(this).attr("name");
                        if(curr-old>30*60*1000)
                        {
                            $("#"+old).html('<span class="text-success" style="cursor: pointer" onclick="activeAddr(\''+id+'\')" id="addr_icon2"><%--重新发送--%><fmt:message key='fund.raise.resend' /></span>');
                        }
                        else
                        {
                            var timer1=window.setInterval(function(){
                                var curr=( new Date()).valueOf();
                                if(curr-old>30*60*1000){
                                    $("#"+old).html('<span class="glyphicon glyphicon-log-out text-success" style="cursor: pointer" onclick="activeAddr(\''+id+'\')" id="addr_icon2" title="重新发送激活邮件">');
                                    window.clearInterval(timer1);
                                }else{
                                    $("#"+old).html("<font class='text-danger'>You can resend email in "+formatDuring(30*60*1000-(curr-old))+"</font>");
                                }
                            },1000);

                        }

                    });
                } else {
                    remind(remindType.error,data.message,300);
                }
            }
        });
        if(index == 0){
            $("#minvalue").text(params.WithdrawTansferBTCCostLow);
            $("#maxvalue").text(params.WithdrawTansferBTCCostUpper);
            $("#avervalue").text(params.WithdrawTansferBTCCostAver);
            $("#upLine").text(params.WithdrawTansferBTCQuotaUpper);
            $("#currAmt").text(params.btcEnableAmount.toFixed(4));
            $("#addr_posFee option").each(function(){
                if($(this).text() == ""+parseFloat(params.WithdrawTansferBTCCostAver).toFixed(4)){
                    $(this).attr("selected","selected");
                }
            });
            $(".addon").each(function (){
                $(this).text('BTC');
            });
        }
    }
    function formatDuring(mss) {
        var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = ((mss % (1000 * 60)) / 1000).toFixed(0);
        return  minutes + " minutes " + seconds + " seconds ";
    }
    $(function() {
        <%--提现地址/账户管理邮箱确认关闭--%>
        $('.mailConfirmBtn').on('click',function(){
            $('#mailConfirm').hide();
        });
        <%--tab切换--%>
        $('.tabnav li').on('click', function() {
            index = $(this).index();
            $("#stockinfoId").val($(this).attr("id"));
            $("#title").html($(this).text() + "<fmt:message key='fund.raise.withdrawalRecord' />");
            $("#stockinfoIdSelect").val($(this).attr("id"));
            $("#stockinfoIdSelect").attr("disabled", true);
            getDate();
            $("#amount").change();
            renderPage.render(true);
        });
        index=0;
        if(index == 0){
            <c:choose>
            <c:when test="${certStatus}">
                var shengyuAmt=(params.AuthUserWithdrawBTCDayQuotaUpper-params.btcUsedAmt).toFixed(4);
                $("#shengyuAmt").text(shengyuAmt>0?shengyuAmt:0);
            </c:when>
            <c:otherwise>
                var shengyuAmt=(params.WithdrawTansferBTCDayQuotaUpper-params.btcUsedAmt).toFixed(4);
                $("#shengyuAmt").text(shengyuAmt>0?shengyuAmt:0);
            </c:otherwise>
            </c:choose>
        }
        getDate();
        $("#newAddrPop").on("hidden.bs.modal",function(){
            $(document.body).addClass("modal-open");
        });

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

    $("#stockinfoIdSelect").attr("disabled", true);
    $("#amount").change();

    $('.select-content').click(function(){
        $(this).next('.select-label').toggle();
    });
    <%--删除提币地址--%>
    function deleteAddr(id){
        $.ajax({
            url : '${ctx}/fund/withdraw/withdrawAddrDelete',
            data : {
                id : id.substr(2)+''
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
                    remind(remindType.success,data.message,300);
                    $("#addr"+id.substr(2)).remove();
                    getDate();
                } else {
                    remind(remindType.error,data.message,1000);
                }
            }
        });
    }
    <%--重新发送提币地址 邮件激活--%>
    function activeAddr(id){
        $.ajax({
            url : '${ctx}/fund/withdraw/withdraw/sendActiveEmail',
            data : {
                id : id
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
                    remind(remindType.success,data.message,300);
                    getDate();
                } else {
                    remind(remindType.error,data.message,1000);
                }
            }
        });
    }

    function JumpURList(flagStockType,symbol) {
        if(flagStockType == "<%=FundConsts.STOCKTYPE_DIGITALCOIN %>")
        {
            jumpUrl('${ctx}/fund/withdraw?symbol='+symbol);
        }
        else if(flagStockType == "<%=FundConsts.STOCKTYPE_ERC20_TOKEN %>"){
            jumpUrl('${ctx}/fund/withdrawERC20?symbol='+symbol);
        }
    }
</script>
</body>
</html>
