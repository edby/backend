<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%--策略设置--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--安全策略--%>
<div class="col-md-6 col-sm-12 col-xs-12 column">
    <div class="col-md-2 col-sm-2 hidden-xs mt-20"><i class="iconfont text-success fs60">&#xe61d;</i></div>
    <div class="col-md-7 col-sm-7 col-xs-12">
        <div class="col-md-12 col-xs-12">
            <div class="col-md-4 col-sm-4 col-xs-4 pl0">
                <div class="strength progress">
                    <div class="progress-bar  safe-strength1 strength0" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
            </div>
            <div class="col-md-4 col-sm-4 col-xs-4 pl0">
                <div class="strength progress">
                    <div class="progress-bar  safe-strength2 strength0" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
            </div>
            <div class="col-md-4 col-sm-4 col-xs-4 pl0">
                <div class="strength progress">
                    <div class="progress-bar  safe-strength3 strength0" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
            </div>
        </div>
        <p class="col-md-12 col-xs-12 text-left">
            <fmt:message key="account.setting.currSafe" /><span class="text-success safeTxt"><fmt:message key="account.setting.smsCode"/> + <fmt:message key="account.setting.bGAVcode"/></span></p>
    </div>
    <div class="text-primary col-md-3 col-sm-3 col-xs-12 pl30 pb20 bitms-pointer" id="setSafeBtn" data-toggle="modal" data-target=".safeStrategy">
        <fmt:message key="account.setting.setSafe" />
    </div>
    <div class="modal fade safeStrategy" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close pop-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title text-success"><fmt:message key="account.setting.safeCenter"/><fmt:message key="account.setting.safetxt"/></h5>
                </div>
                <div class="modal-body">
                    <form:form data-widget="validator" class="form-horizontal" id="safeForm" autocomplete="off">
                        <div class="radio">
                            <label>
                                <input type="radio" class="safe0" name="level"  value="0" ><fmt:message key="account.setting.login"/>
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input type="radio" class="safe1" name="level" value="1"> <fmt:message key="account.setting.smsCode"/>
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input type="radio" class="safe2" name="level" value="2"><fmt:message key="account.setting.bGAVcode"/>
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input type="radio" class="safe3" name="level" value="3"> <fmt:message key="account.setting.smsCode"/>&nbsp;<fmt:message key="account.setting.or"/>&nbsp;<fmt:message key="account.setting.bGAVcode"/>
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input type="radio" class="safe4" name="level" value="4"><fmt:message key="account.setting.smsCode"/>+<fmt:message key="account.setting.bGAVcode"/>
                            </label>
                        </div>
                        <hr>
                        <div class="form-group  safe0-con">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.login"/></label>
                            <div class="col-sm-9 ui-form-item">
                                <input type="password" name="pwd" class="form-control" autocomplete="new-password" data-display='<fmt:message key="register.loginPwd"/>' placeholder='<fmt:message key="register.loginPwd"/>'>
                            </div>
                        </div>
                        <div class="form-group safe-select">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.verityWay"/></label>
                            <div class="col-sm-9">
                                <select  class="form-control checkType" name="type">
                                    <option value="ga"><fmt:message key="account.setting.bGAVcode"/></option>
                                    <option value="sms"><fmt:message key="account.setting.smsCode"/></option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group safe1-con">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.phone"/></label>
                            <div class="col-sm-9 text-success mobNoVal"></div>
                        </div>
                        <div class="form-group safe1-con">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.smsCode"/></label>
                            <div class="col-sm-9 ui-form-item">
                                <div class="input-group">
                                    <input type="text" name="sms" class="form-control" data-display='<fmt:message key="account.setting.smsCode"/>' placeholder='<fmt:message key="account.setting.smsCode"/>'>
                                    <span class="input-group-btn">
        															<button class="btn btn-primary safeSendSms" type="button"><fmt:message key="account.setting.getCode"/></button>
      															</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group safe2-con">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.bGAVcode"/></label>
                            <div class="col-sm-9 ui-form-item">
                                <input type="text" name="ga" class="form-control" data-display='<fmt:message key="fund.raise.googleCode" />' placeholder='<fmt:message key="fund.raise.googleCode" />'>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-9">
                                <button type="button" class="btn btn-primary bitms-width" id="safeBtnSub"><fmt:message key="account.setting.confirm"/></button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
<%--交易策略--%>
<div class="col-md-6 col-sm-12 col-xs-12 column">
    <div class="col-md-2 col-sm-2 hidden-xs mt-20 center-block"><i class="iconfont text-success fs60">&#xe61d;</i></div>
    <div class="col-md-7 col-sm-7 col-xs-12">
        <div class="col-md-12">
            <div class="col-md-4 col-sm-4 col-xs-4 pl0">
                <div class="progress strength">
                    <div class="progress-bar trade-strength1 strength0" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
            </div>
            <div class="col-md-4 col-sm-4 col-xs-4 pl0">
                <div class="progress strength">
                    <div class="progress-bar trade-strength2 strength0" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
            </div>
            <div class="col-md-4 col-sm-4 col-xs-4 pl0">
                <div class="progress strength">
                    <div class="progress-bar trade-strength3 strength0" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
            </div>
        </div>
        <p class="col-md-12 text-left"><fmt:message key="account.setting.currTrade"/>：<span class="text-success tradeTxt"><fmt:message key="account.setting.smsCode"/> + <fmt:message key="account.setting.bGAVcode"/></span></p>
    </div>
    <div class="text-primary col-md-3 col-sm-3 col-xs-12 pl30 bitms-pointer" id="setTradeBtn" data-toggle="modal" data-target=".tradeStrategy"><fmt:message key="account.setting.set"/><fmt:message key="account.setting.tradetxt"/></div>
    <div class="modal fade tradeStrategy" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close pop-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title text-success"><fmt:message key="account.setting.safeCenter"/>><fmt:message key="account.setting.tradetxt"/></h5>
                </div>
                <div class="modal-body">
                    <form:form data-widget="validator" class="form-horizontal" id="tradeForm">
                        <div class="radio">
                            <label>
                                <input type="radio" name="level" class="trade0"  value="0"> <span class="text-success"><fmt:message key="account.setting.TradeStrategie1"/></span><fmt:message key="account.setting.fund"/>
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input type="radio" name="level" class="trade1"  value="1" > <span class="text-success"><fmt:message key="account.setting.TradeStrategie2"/></span><fmt:message key="account.setting.fund"/>
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input type="radio" name="level" class="trade2"  value="2" > <span class="text-success"><fmt:message key="account.setting.TradeStrategie3"/></span><fmt:message key="account.setting.fund"/>
                            </label>
                        </div>
                        <hr>
                        <div class="form-group  safe0-con">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.login"/></label>
                            <div class="col-sm-9 ui-form-item">
                                <input type="password" name="pwd" class="form-control" autocomplete="new-password" data-display='<fmt:message key="register.loginPwd"/>' placeholder='<fmt:message key="register.loginPwd"/>'>
                            </div>
                        </div>
                        <div class="form-group safe-select">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.verityWay"/></label>
                            <div class="col-sm-9">
                                <select  class="form-control checkType" name="type">
                                    <option value="ga"><fmt:message key="account.setting.bGAVcode"/></option>
                                    <option value="sms"><fmt:message key="account.setting.smsCode"/></option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group safe1-con">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.phone"/></label>
                            <div class="col-sm-9 text-success mobNoVal"></div>
                        </div>
                        <div class="form-group safe1-con">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.smsCode"/></label>
                            <div class="col-sm-9 ui-form-item">
                                <div class="input-group">
                                    <input type="text" name="sms" class="form-control" data-display='<fmt:message key="account.setting.smsCode"/>' placeholder='<fmt:message key="account.setting.smsCode"/>'>
                                    <span class="input-group-btn">
        															<button class="btn btn-primary safeSendSms" type="button"><fmt:message key="account.setting.getCode"/></button>
      															</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group safe2-con">
                            <label class="col-sm-3 control-label"><fmt:message key="account.setting.bGAVcode"/></label>
                            <div class="col-sm-9 ui-form-item">
                                <input type="text" name="ga" class="form-control" data-display='<fmt:message key="fund.raise.googleCode" />' placeholder='<fmt:message key="fund.raise.googleCode" />'>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-9">
                                <button type="button" class="btn btn-primary bitms-width" id="tradeBtnSub"><fmt:message key="account.setting.confirm"/></button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    seajs.use('',function () {
        var safe ="${account.securityPolicy}";
        var trade ="${account.tradePolicy}";
        //弹框关闭，表单清空
        $('.pop-close').click(function(){
            $(this).parent().parent().find('form').find('input[type="text"]').val('');
            $(this).parent().parent().find('form').find('input[type="password"]').val('');
        });
        $('.safeStrategy .pop-close,.tradeStrategy .pop-close').click(function(){
            location.reload();
        })

        //安全验证策略联动效果，默认为登录密码
        if(safe == 0){
            var  safe0Txt= $('.safe0').parent().text();
            $('.safeTxt').text(safe0Txt);
            $('.safe1,.safe2,.safe3,.safe4').removeAttr('checked');
            $('.safe0').prop('checked',true);
            $('.safe0-con').removeClass('bitms-hide');
            $('.safe1-con,.safe2-con,.safe-select').addClass('bitms-hide');
            //安全等级
            $('.safe-strength1').removeClass('strength0').addClass('strength2');
        }else if(safe == 1){
            var  safe1Txt= $('.safe1').parent().text();
            $('.safeTxt').text(safe1Txt);
            $('.safe0,.safe2,.safe3,.safe4').removeAttr('checked');
            $('.safe1').prop('checked',true);
            $('.safe1-con').removeClass('bitms-hide');
            $('.safe0-con,.safe2-con,.safe-select').addClass('bitms-hide');
            //安全等级
            $('.safe-strength1').removeClass('strength0').addClass('strength2');
        }else if(safe == 2) {
            var  safe2Txt= $('.safe2').parent().text();
            $('.safeTxt').text(safe2Txt);
            $('.safe0,.safe1,.safe3,.safe4').removeAttr('checked');
            $('.safe2').prop('checked',true);
            $('.safe2-con').removeClass('bitms-hide');
            $('.safe0-con,.safe1-con,.safe-select').addClass('bitms-hide');
            //安全等级
            $('.safe-strength1').removeClass('strength0').addClass('strength2');
        }else if(safe == 4){
            var  safe4Txt= $('.safe4').parent().text();
            $('.safeTxt').text(safe4Txt);
            $('.safe0,.safe1,safe2,.safe3').removeAttr('checked');
            $('.safe4').prop('checked',true);
            $('.safe0-con,.safe-select').addClass('bitms-hide');
            $('.safe1-con,.safe2-con').removeClass('bitms-hide');
            //安全等级
            $('.safe-strength1').removeClass('strength0').addClass('strength2');
            $('.safe-strength2').removeClass('strength0').addClass('strength2');
            $('.safe-strength3').removeClass('strength0').addClass('strength2');
        }else{
            var  safe3Txt= $('.safe3').parent().text();
            $('.safeTxt').text(safe3Txt);
            $('.safe0,.safe1,safe2,.safe4').removeAttr('checked');
            $('.safe3').prop('checked',true);
            $('.safe0-con,.safe1-con').addClass('bitms-hide');
            $('.safe-select,.safe2-con').removeClass('bitms-hide');
            //安全等级
            $('.safe-strength1').removeClass('strength0').addClass('strength2');
            $('.safe-strength2').removeClass('strength0').addClass('strength2');
            //下拉框默认值:GA绑定的时候为ga,反之为手机
            if("${account.authKey}"== ""  || "${account.authKey}"== null){
                $(".checkType").val('sms');
                $('.safe1-con').removeClass('bitms-hide');
                $('.safe2-con').addClass('bitms-hide');
            }else{
                $(".checkType").val('ga');
                $('.safe1-con').addClass('bitms-hide');
                $('.safe2-con').removeClass('bitms-hide');
            }
            $(".checkType").on("click", function () {
                if ("sms" == $(this).val()) {
                    $('.safe1-con').removeClass('bitms-hide');
                    $('.safe2-con').addClass('bitms-hide');
                }else {
                    //如果ga没绑定提示
                    if("${account.authKey}"== ""  || "${account.authKey}"== null){
                        $(this).val('sms');
                        remind(remindType.error,I18n.prop("setting.unbindGa"));
                    }else{
                        $('.safe1-con').addClass('bitms-hide');
                        $('.safe2-con').removeClass('bitms-hide');
                    }
                }
            });
        }

        //交易验证策略动态显示

        if(trade == 0) {
            var trade0Txt = $('.trade0').parent().text();
            $('.tradeTxt').text(trade0Txt);
            $('.trade1,.trade2').removeAttr('checked');
            $('.trade0').prop('checked',true);
            //安全等级
            $('.trade-strength1').removeClass('strength0').addClass('strength2');
        }else if(trade == 1){
            var trade1Txt = $('.trade1').parent().text();
            $('.tradeTxt').text(trade1Txt);
            $('.trade0,.trade2').removeAttr('checked');
            $('.trade1').prop('checked',true);
            //安全等级
            $('.trade-strength1').removeClass('strength0').addClass('strength2');
            $('.trade-strength2').removeClass('strength0').addClass('strength2');
        }else{
            var trade2Txt = $('.trade2').parent().text();
            $('.tradeTxt').text(trade2Txt);
            $('.trade1,.trade0').removeAttr('checked');
            $('.trade2').prop('checked',true);
            //安全等级
            $('.trade-strength1').removeClass('strength0').addClass('strength2');
            $('.trade-strength2').removeClass('strength0').addClass('strength2');
            $('.trade-strength3').removeClass('strength0').addClass('strength2');
        }
    });
</script>