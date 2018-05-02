<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%-- 此段必须要引入 t为小时级别的时间戳 --%>
<link type="text/css" href="//g.alicdn.com/sd/ncpc/nc.css?t=1502956831425" rel="stylesheet"/>
<script type="text/javascript" src="//g.alicdn.com/sd/ncpc/nc.js?t=1502956831425"></script>
<%-- 引入结束 --%>
<%-- 此段必须要引入 --%>
<div id="_umfp" style="display:inline;width:1px;height:1px;overflow:hidden"></div>
<%-- 引入结束 --%>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon">
            <h2 class="loginTit">Reset Password</h2>
            <hr>
               <form:form class="form-horizontal" data-widget="validator" id="findpwdForm">
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-envelope" style="top:0;"></span>
                        <input type="text"  class="form-control" id="forgetEmail" name="email" required
                               data-rule="email"
                               data-display="Email"
                               placeholder="Your Email"/>
                    </div>
                </div>
               <div class="form-group slider-group">
                   <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                       <div class="ln">
                           <div id="captcha"></div>
                           <input type='hidden' id='csessionid' name='csessionid'/>
                           <input type='hidden' id='sig' name='sig'/>
                           <input type='hidden' id='token' name='token'/>
                           <input type='hidden' id='scene' name='scene'/>
                       </div>
                   </div>
               </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <button type="button" class="btn btn-primary btn-block" id="verifySubmit">Submit</button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
 <script>
     var validator;
     seajs.use(['validator', 'i18n'], function (Validator, I18n) {
         validator = new Validator();
         <%--滑块验证--%>
         $('#verifySubmit').attr("disabled",true);
         var nc = new noCaptcha();
         var nc_appkey = appKey;  <%--应用标识,不可更改--%>
         var nc_scene = 'other';  <%--场景,不可更改--%>
         var nc_token = [nc_appkey, (new Date()).getTime(), Math.random()].join(':');
         var nc_option = {
             renderTo: '#captcha',
             appkey: nc_appkey,
             scene: nc_scene,
             token: nc_token,
             callback: function (data) {  <%--校验成功回调--%>
                 $('#verifySubmit').attr("disabled",false);
                 $("#sig").val(data.sig);
                 $("#token").val(nc_token);
                 $("#scene").val(nc_scene);
                 $("#csessionid").val(data.csessionid);
             },
             language: locale || 'en_US',
             foreign: 0,
             isEnabled: true
         };
         nc.init(nc_option);
         $("#verifySubmit").on("click", function () {
             validator.destroy();
             validator = new Validator({
                 element: '#findpwdForm',
                 autoSubmit: false, <%--当验证通过后不自动提交--%>
                 onFormValidated: function (error, results, element) {
                     if (!error) {
                         $.ajax({
                             url: '${ctx}/forgetPass/submit',
                             type: 'post',
                             data: $('#findpwdForm').serialize(),
                             dataType: 'json',
                             beforeSend:function(){
                                 $('.reg-pop').fadeIn();
                                 $("#verifySubmit").addClass('btn-dis');
                             },
                             success: function (data) {
                                 $('#findpwdForm').find('input[name="csrf"]').val(data.csrf);
                                 nc.init(nc_option);
                                 $('#verifySubmit').attr("disabled",true);
                                 if (data.code == bitms.success) {
                                     remind(remindType.success,"Success",1000,function() {
                                         jumpUrl("${ctx}/forgetPass/confirm");
                                     });
                                 } else {
                                     if (data.code == bitms.noUser) {
                                         $('#forgetEmail').val('').focus();
                                         remind(remindType.error, data.message);
                                     } else if (data.code == bitms.captchaError) {
                                         remind(remindType.error, I18n.prop("error.captchaCode"));
                                     } else {
                                         remind(remindType.error, data.message);
                                     }
                                 }
                             },
                             complete:function(){
                                 $('.reg-pop').hide();
                                 $("#verifySubmit").removeClass('btn-dis');
                             }
                         });
                     }
                 }
             }).addItem({
                 element: '[name=email]',
                 required: true,
                 rule: 'email'
             });
             $("#findpwdForm").submit();
         });
     });
 </script>
</body>
</html>