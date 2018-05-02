<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal fade bindPhone" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close pop-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-success"><fmt:message key="account.setting.safeCenter"/>><span><fmt:message key="account.setting.bindPhone"/></span></h5>
            </div>
            <div class="modal-body">
                <form:form data-widget="validator" class="form-horizontal" id="bindPhoneForm">
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.bpLocation"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <select name="location" class="form-control selectLocation">
                                <option></option>
                                <c:forEach var="item" items="${regions}">
                                    <option value="${item.LCode}" <%--${item.LCode==account.country? "selected": ""}--%>>${item.enName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.phone"/></label>
                        <div class="col-sm-9 ui-form-item">
                           <%-- <div class="input-group">
                                <span class="input-group-addon bg-success bitms-bg3 cf">+<span class="phoneLocation"></span></span>--%>
                                <input type="text" name="phone" class="form-control" placeholder='<fmt:message key="account.setting.phone"/>' data-display='Phone Number'>
                         <%--   </div>--%>
                        </div>
                    </div>
                    <div class="form-group slider-group">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.slider"/></label>
                        <div class="col-sm-9">
                            <div class="ln">
                                <div id="bindPhoneCaptcha"></div>
                                <input type='hidden' name='csessionid' />
                                <input type='hidden' name='sig' />
                                <input type='hidden' name='token' />
                                <input type='hidden' name='scene' />
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.smsCode"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <div class="input-group">
                                <input type="text" class="form-control" name="vlidCode" onkeydown="KeyDown($('#bindPhoneSub'));" placeholder='<fmt:message key="account.setting.smsCode"/>'  data-display='SMS verification'>
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" id="sendSMS" type="button"><fmt:message key="account.setting.getCode"/></button>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <button type="button" class="btn btn-primary bitms-width" id="bindPhoneSub"><fmt:message key="account.setting.confirm"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade changePhone" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close pop-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-success"><fmt:message key="account.setting.safeCenter"/>><span><fmt:message key="account.setting.changePhone"/></span></h5>
            </div>
            <div class="modal-body">
                <div><fmt:message key="account.setting.currAccount"/><span class="text-success">${account.accountName}</span><fmt:message key="account.setting.changePhone"/></div>
                <hr>
                <form:form data-widget="validator" class="form-horizontal" id="changePhoneForm">
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.bpLocation"/></label>
                        <div class="col-sm-9">
                            <select name="location" class="form-control selectLocation">
                                <option></option>
                                <c:forEach var="item" items="${regions}">
                                    <option value="${item.LCode}" <%--${item.LCode==account.country? "selected": ""}--%>>${item.enName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.newphone"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <div class="input-group">
                                <span class="input-group-addon bg-success bitms-bg3 cf">+<span class="phoneLocation"></span></span>
                                <input type="text" class="form-control newPhone" name="phone" placeholder='<fmt:message key="account.setting.newphone"/>'  data-display='New phone'>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.slider"/></label>
                        <div class="col-sm-9">
                            <div class="ln">
                                <div id="changePhoneCaptcha"></div>
                                <input type='hidden' name='csessionid' />
                                <input type='hidden' name='sig' />
                                <input type='hidden' name='token' />
                                <input type='hidden' name='scene' />
                            </div>
                        </div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.smsCode"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <div class="input-group">
                                <input type="text" class="form-control" name="vlidCode" placeholder='<fmt:message key="account.setting.smsCode"/>'
                                       data-display='SMS verification'>
                                <span class="input-group-btn">
                                            <button class="btn btn-primary" id="newSendSMS" type="button"><fmt:message key="account.setting.getCode"/></button>
                                        </span>
                            </div>
                        </div>
                    </div>
                    <hr>
                    <div class="form-group  safe0-con">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.login"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="password" name="pwd" class="form-control" autocomplete="new-password" data-display='Login password' placeholder='<fmt:message key="register.loginPwd"/>'>
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
                                <input type="text" name="sms" class="form-control" data-display='SMS verification' placeholder='<fmt:message key="account.setting.smsCode"/>'>
                                <span class="input-group-btn">
                                            <button class="btn btn-primary safeSendSms" type="button"><fmt:message key="account.setting.getCode"/></button>
                                        </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group safe2-con">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.bGAVcode"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="text" name="ga" class="form-control" data-display='GA code' placeholder='<fmt:message key="fund.raise.googleCode" />'>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <button type="button" class="btn btn-primary bitms-width" id="changePhoneSub"><fmt:message key="account.setting.confirm"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade bindGA" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog ga-modal-dialog" role="document">
        <div class="modal-content bingGa-con1">
            <div class="modal-header">
                <button type="button" class="close pop-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-success"><fmt:message key="account.setting.safeCenter"/>><span><fmt:message key="account.setting.bind"/>&nbsp;<fmt:message key="account.setting.ga"/></span></h5>
            </div>
            <div class="modal-body bitms-con">
                <form:form data-widget="validator" class="form-horizontal" id="bindGAForm">
                    <div><fmt:message key="account.setting.currAccount"/>&nbsp;<span class="text-success">${account.accountName}</span>&nbsp;<fmt:message key="account.setting.bind"/><fmt:message key="account.setting.ga"/></div>
                    <hr>
                    <div class="col-sm-4 px5">
                        <img src="${imagesPath}/bitms/ga-img1.png" class="img-responsive hidden-xs" alt="GAimage">
                    </div>
                    <div class="col-sm-8">
                        <div class="form-group">
                            <label class="col-sm-12 col-xs-4 db control-label">
                                1.<fmt:message key="account.setting.step1"/>
                            </label>
                            <div class="col-sm-6 mt10 col-xs-4">
                                <a class=" bitms-b db p15 bindGa-link1 text-center" href="https://itunes.apple.com/cn/app/google-authenticator/id388497605" target="_blank" title="downloadGA">
                                    <img src="${imagesPath}/bitms/ga-img2.png" class="img-responsive pull-left mt5 phone-gaimg1 ml5" alt="GAimage">
                                    <div class="text-success h6 ml35 phone-mt7">download <span class="hidden-xs">it from</span><br><span class="text-uppercase hidden-xs">app store</span></div>
                                </a>
                            </div>
                            <div class="col-sm-6 mt10 col-xs-4">
                                <a class=" bitms-b db bindGa-link2 p15 text-center" href="http://p.gdown.baidu.com/4563446310a95afd4a4a2c7d37e0d57e58db08b763e5f8f7912d74966866ba038876b51a51372ad22f9ac54b9bff14b5c15bfa6ab33bff6e52194130e95df46bbc5b07381ef7a2e323e41c6f06da6d5cda7044f4aab833f39b6978ba611f65ea1538926fee90cd0085d3ee085ee25966a27afdce5da76665403cb75a51829695405387778e76f7fc90856b9ce55fa05799abd20ca5845ebcd56e48918a53d44221260ce909a48c073e2e8cec8319f7cd342c3b5fabe04226b8420f96f1a4d67c
" target="_blank" title="downloadGA">
                                    <img src="${imagesPath}/bitms/ga-img3.png" class="img-responsive pull-left mt5 phone-gaimg2 ml5" alt="GAimage">
                                    <div class="text-success h6 ml35 phone-mt7">download <span class="hidden-xs">it from</span><br><span class="text-uppercase hidden-xs">google play</span></div>
                                </a>
                            </div>
                        </div>
                        <div class="form-group mt20">
                            <div class="col-sm-12">
                                <div class="col-sm-6 pl0">
                                    <label class=" control-label">2.<fmt:message key="account.setting.step2"/></label>
                                    <div class="px0 text-center mt5">
                                        <div id="qrcode_img" class="phone-gaCodeImg"></div>
                                    </div>
                                    <span class="gaKeyLabel"></span>
                                </div>
                                <label class="col-sm-6 control-label pr0 pl0">3.<fmt:message key="account.setting.phone"/>&nbsp;&nbsp; <label class="text-success mobNoVal"></label></label>
                                <div class="col-sm-6 ui-form-item  pr0 pl0 mt5 phone-mt0">
                                    <div class="input-group">
                                        <input type="text" class="form-control" name="validCode" placeholder='<fmt:message key="account.setting.smsCode"/>' data-display='SMS verification'>
                                        <span class="input-group-btn">
                                                <button class="btn btn-primary  safeSendSms " type="button"><fmt:message key="account.setting.getCode"/></button>
                                            </span>
                                    </div>
                                </div>
                                <label class="col-sm-6 control-label  pr0 pl0 mt30">4.<fmt:message key="account.setting.bGAVcode"/></label>
                                <div class="col-sm-6 ui-form-item  pr0 pl0 phone-mt0 mt10">
                                    <input type="text" name="gaCode" class="form-control" onkeydown="KeyDown($('.bingGANext'));" placeholder='<fmt:message key="fund.raise.googleCode"/>'  data-display='GA code'>
                                </div>
                                <div class="col-sm-6 control-label  pr0 pl0 mt10"><button type="button" class="btn btn-primary bitms-width bingGANext"><fmt:message key="submit"/><%--<fmt:message key="account.setting.next"/>--%></button></div>
                            </div>
                        </div>
                        <%--<div class="form-group">
                            <label class="col-sm-5 control-label">3.<fmt:message key="account.setting.phone"/></label>
                            <div class="col-sm-7 ui-form-item">
                                <label class="text-success mobNoVal"></label>
                                <div class="input-group">
                                    <input type="text" class="form-control" name="validCode" placeholder='<fmt:message key="account.setting.smsCode"/>' data-display='SMS verification'>
                                    <span class="input-group-btn">
                                                <button class="btn btn-primary  safeSendSms " type="button"><fmt:message key="account.setting.getCode"/></button>
                                            </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-5 control-label">4.<fmt:message key="account.setting.bGAVcode"/></label>
                            <div class="col-sm-7 ui-form-item">
                                <input type="text" name="gaCode" class="form-control" onkeydown="KeyDown($('.bingGANext'));" placeholder='<fmt:message key="fund.raise.googleCode"/>'  data-display='GA code'>
                            </div>
                        </div>--%>
                        <div class="form-group">
                            <div class="col-sm-6 ">
                               <%-- <span class="text-success "><fmt:message key="account.setting.bGAkey"/></span>--%>

                                <input type="text text-success"  id="GAkey" name="secretKey"  onpaste="return false" oncontextmenu="return false" oncopy="return false" oncut="return false" class="form-control px5 mt5 hidden">
                            </div>
                            <%--<div class="col-sm-6 mt20 pl0">
                                <button type="button" class="btn btn-primary bitms-width bingGANext mt5"><fmt:message key="submit"/>&lt;%&ndash;<fmt:message key="account.setting.next"/>&ndash;%&gt;</button>
                            </div>--%>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <%--<div class="modal-content bingGa-con2">
            <div class="modal-header">
                <button type="button" class="close saveClose " aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-success"><fmt:message key="account.setting.gaSave"/></h5>
            </div>
            <div class="modal-body bitms-con">
                <div class="form-group">
                    <div class="col-sm-3 text-right mt15">GA&nbsp;<fmt:message key="account.setting.bGAkey"/>：</div>
                    <div class="col-sm-7">
                        <input type="text text-success" id="GAkeySave" onpaste="return false" oncontextmenu="return false" oncopy="return false" oncut="return false" class="form-control px5">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-12 mt15">
                        <button type="button" class="btn btn-primary bitms-width" id="gaSaveBtn"><fmt:message key="account.setting.complete"/></button>
                    </div>
                </div>
            </div>
        </div>--%>
     <%--   <div class="modal-content bingGa-con3">
            <div class="modal-header">
                <button type="button" class="close  pop-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-success"><fmt:message key="account.setting.gaEnter"/></h5>
            </div>
            <div class="modal-body bitms-con">
                <form:form data-widget="validator" class="form-horizontal" id="bindGAFormConfirm">
                    <div class="form-group">
                        <div class="col-sm-12 ui-form-item">
                            <div class="gakey-enter" id="gakeyEnter">
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                                <input type="text" />
                            </div>
                            <input type="hidden" id="secretKey" name="secretKey" class="form-control" />
                            <input type="hidden" id="GAkeyVal" class="form-control px5">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-12">
                            <div class="col-sm-6 col-xs-6">
                                <button type="button" class="btn btn-primary bitms-width" id="bindGALast"><fmt:message key="account.setting.goLast"/></button>
                            </div>
                            <div class="col-sm-6 col-xs-6">
                                <button type="button" class="btn btn-primary bitms-width" id="bindGASub"><fmt:message key="submit"/></button>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>--%>
    </div>
</div>
<div class="modal fade unbindGA" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close pop-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-success"><fmt:message key="account.setting.safeCenter"/>><span><fmt:message key="account.setting.unbind"/><fmt:message key="account.setting.ga"/></span></h5>
            </div>
            <div class="modal-body">
                <div><fmt:message key="account.setting.currAccount"/><span class="text-success">${account.accountName}</span><fmt:message key="account.setting.unbind"/><fmt:message key="account.setting.ga"/></div>
                <hr>
                <form:form data-widget="validator" class="form-horizontal" id="unbindGAForm">
                    <div class="form-group bitms-hide">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.bGAVcode"/></label>
                        <div class="col-sm-9">
                            <input type="text" name="gaCode" class="form-control">
                        </div>
                    </div>
                    <hr class="bitms-hide">
                    <div class="form-group  safe0-con">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.login"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="password" name="pwd" class="form-control" autocomplete="new-password" data-display='Login password' placeholder='<fmt:message key="register.loginPwd"/>'>
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
                                <input type="text" name="sms" class="form-control" data-display='SMS verification' placeholder='<fmt:message key="account.setting.smsCode"/>'>
                                <span class="input-group-btn">
                                        <button class="btn btn-primary safeSendSms" type="button"><fmt:message key="account.setting.getCode"/></button>
                                    </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group safe2-con">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.bGAVcode"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="text" name="ga" class="form-control" data-display='GA code' placeholder='<fmt:message key="fund.raise.googleCode" />'>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <button type="button" class="btn btn-primary bitms-width" id="unbindGASub"><fmt:message key="account.setting.confirm"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade resetLoginPwd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close pop-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-success"><fmt:message key="account.setting.safeCenter"/>><span><fmt:message key="account.setting.reset"/><fmt:message key="account.setting.login"/></span></h5>
            </div>
            <div class="modal-body">
                <div><fmt:message key="account.setting.currAccount"/><span class="text-success">${account.accountName}</span>&nbsp;<fmt:message key="account.setting.reset"/><fmt:message key="account.setting.login"/></div>
                <hr>
                <form:form data-widget="validator" class="form-horizontal" id="resetLoginForm">
                    <div class="form-group ">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.old"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="password"   class="form-control" name="origPass" id="origPass" placeholder='<fmt:message key="account.setting.old"/>' data-display='The current'>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.new"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="password" data-rule="password" class="form-control" name="newPass" id="loginNewPass" placeholder='<fmt:message key="account.setting.new"/>'  data-display='The new'>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.confirm"/><fmt:message key="user.pass"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="password" data-rule="password" name="newrePass" onkeydown="KeyDown($('#resetLoginSub'));" class="form-control" placeholder='<fmt:message key="placeholder.confirmpwd"/>'  data-display='Confirm password'>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <button type="button" class="btn btn-primary bitms-width" id="resetLoginSub"><fmt:message key="account.setting.confirm"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade capitalPwd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close pop-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-success"><fmt:message key="account.setting.safeCenter"/>><span><fmt:message key="account.setting.set"/><fmt:message key="account.setting.fund"/></span></h5>
            </div>
            <div class="modal-body">
                <div><fmt:message key="account.setting.currAccount"/> <span class="text-success">${account.accountName}</span> <fmt:message key="account.setting.fund"/></div>
                <hr>
                <form:form data-widget="validator" class="form-horizontal" id="setCapticalForm">
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.fund"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="password" data-rule="fundPwd" class="form-control" name="fundPwd" id="fundPwd" placeholder='<fmt:message key="account.setting.fund"/>'  data-display='Payment Password'>
                        </div>
                        <div class="col-sm-9  col-sm-offset-3  fs12"><fmt:message key='fund.pwdTip' /></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><fmt:message key="placeholder.confirmpwd"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="password" data-rule="fundPwd" class="form-control" name="fondrePass" placeholder='<fmt:message key="account.setting.confirm"/><fmt:message key="fund.raise.fundPassword"/>'  data-display='Confirm Payment Password'>
                        </div>
                    </div>
                    <hr>
                    <div class="form-group  safe0-con">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.login"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="password" name="pwd" class="form-control" autocomplete="new-password" data-display='Login password' placeholder='<fmt:message key="register.loginPwd"/>'>
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
                                <input type="text" name="sms" class="form-control" onkeydown="KeyDown($('#setCapticalSub'));" data-display='SMS verification' placeholder='<fmt:message key="account.setting.smsCode"/>'>
                                <span class="input-group-btn">
                                            <button class="btn btn-primary safeSendSms" type="button"><fmt:message key="account.setting.getCode"/></button>
                                        </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group safe2-con">
                        <label class="col-sm-3 control-label"><fmt:message key="account.setting.bGAVcode"/></label>
                        <div class="col-sm-9 ui-form-item">
                            <input type="text" name="ga" class="form-control" onkeydown="KeyDown($('#setCapticalSub'));" data-display='GA code' placeholder='<fmt:message key="fund.raise.googleCode" />'>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <button type="button" class="btn btn-primary bitms-width" id="setCapticalSub"><fmt:message key="account.setting.confirm"/></button>
                        </div>
                    </div>
                    <div class="form-group resetCapticalTxt hidden">
                        <div class="col-sm-offset-3 col-sm-9 text-danger h6"><fmt:message key="account.setting.resetFondTip"/></div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>