<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body style="background:url('${imagesPath}/bitms/home.jpg')">
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="row my0">
    <div class="col-xs-12 mt50">
        <div class="text-center"><img src="${imagesPath}/bitms/bitms.svg" alt="BitMS"></div>
        <div class="p10 bitms-con text-center mt50">
            <div class="bitms-con py20">
                <div class="col-xs-12 bitms-nofloat my0 bitms-max-input">
                   <form:form  data-widget="validator" id="changepwdForm">
                        <div class="h2 text-center mb20 cf">Reset password</div>
                       <input type="hidden" name="unid" value="${unid}">
                       <input type="hidden" name="oid" value="${oid}">
                        <div class="form-group">
                            <input type="password"  data-rule="password" class="form-control input-lg" id="setNewPwd" name="loginPwd"  placeholder="Set the login password" data-display="Password" >
                            <p class="fs12 mt5 text-left">At least 8 characters, must contain both capital letters and numbers, and cannot contain spaces</p>
                        </div>
                        <div class="form-group ui-form-item">
                            <input type="password" data-rule="password" class="form-control input-lg" onkeydown="KeyDown($('#changePwdSubmit'));" id="repeatPwd" placeholder="Confirm password" name="reloginpwd" data-display="Confirm password" />
                        </div>
                        <div class="form-group mt30">
                            <button  class="btn btn-primary bitms-width btn-lg" id="changePwdSubmit" name="changePwdSubmit">Confirm</button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="navbar-fixed-bottom bitms-con">
    <div class="fs12 text-center bitms-footer">© 2017  BITMS.com All Rights ReservedBitms Technology Group Co.,Ltd(Seychelles)</div>
</footer>
<script type="text/javascript">
	baseFormValidator({
	    selector: "#changepwdForm",
	    isAjax: true
	});
	var validator;
	seajs.use(['validator','i18n'], function (Validator,I18n) {
        $('.pageLoader').hide();
		validator = new Validator();
	    $("#changePwdSubmit").on("click", function () {
             validator.destroy();
             validator = new Validator({
        	  element: '#changepwdForm',
              autoSubmit: false,<%--当验证通过后不自动提交--%>
              onFormValidated: function (error, results, element) {
                  if (!error) {
                      $.ajax({
                          url: '${ctx}/resetPass/savePass',
                          type: 'post',
                          data: element.serialize(),
                          dataType: 'json',
                          beforeSend:function(){
                          	$('.reg-pop').fadeIn();
                          	$("#changePwdSubmit").attr("disabled",true);
                          },
                          success: function (data, textStatus, jqXHR) {
                              $('#changepwdForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                              if (data.code == bitms.success) {
                            	  remind(remindType.success,"Success",2000,function() {
                            		    var url = "${ctx}/login";
                            		    window.location.href = url;
                            	  });
                              } else {
                                  remind(remindType.error, data.message);
                              }
                          },
                          complete:function(){
                              $('.reg-pop').hide();
                              $("#changePwdSubmit").attr("disabled",false);
                          }
                      });
                  }
              }
         }).addItem({
             element: '[name=reloginpwd]',
             required: true,
             rule: 'confirmation{target: "#setNewPwd"}'
         });
	 })
 });
</script>
</body>
</html>
