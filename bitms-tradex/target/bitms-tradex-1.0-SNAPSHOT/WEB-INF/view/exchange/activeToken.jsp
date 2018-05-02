<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon" id="activeTokenDiv">
            <h2 class="loginTit">Active Token Trading</h2>
            <hr style="margin:5px;">
            <form:form class="form-horizontal" data-widget="validator" id="openTokenForm">
                <input name="addr" value="${ERC20Contract}" type="hidden" />
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 input-group">
                        <p class="alert alert-danger" style="background-color:#f2dede;border-color:#ebccd1;color:#a94442;padding:5px;background-image:none;margin-bottom:0px;">
                            <span class="glyphicon glyphicon-warning-sign"></span>
                            <span>Warning：When the Token appears to be suspected of fraud, or when we have identified it for illegal acts and clearly imitating the Token of other projects, we have the right to permanently close its trading functions and will not return any related costs for the activation of the token. Please be aware of this point and agree with it, otherwise, please don't activate the token.</span>
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-tag" style="top:0;"></span>
                        <span class="form-control" style="background-color:#eee;cursor:no-drop;"><span>Symbol：${symbol}</span>&#12288;<span class="hidden-xs">Name：${symbolName}</span></span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-barcode" style="top:0;"></span>
                        <a onMouseOver="this.style.textDecoration='none'" target="_blank" href="https://etherscan.io/token/${ERC20Contract}"><span class="form-control hidden-xs" style="background-color:#eee;">Token：${ERC20Contract}</span><span class="form-control visible-xs" style="background-color:#eee;">Token：${fn:substring(ERC20Contract,0,10)}...${fn:substring(ERC20Contract,34,42)}</span></a>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon" style="top:0;"><span class="glyphicon glyphicon-time"></span></span>
                        <span class="form-control" style="background-color:#eee;cursor:no-drop;">Expiry date：365 Days</span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon" style="top:0;width:130px;text-align:left;">Activation Fee：</span>
                        <span class="form-control" id="actFee" style="background-color:#eee;cursor:no-drop;"><fmt:formatNumber type="number" value="${vinOpenFee}" pattern="0.0000" maxFractionDigits="4"/> BIEX</span>
                    </div>
                </div>
                <div class="form-group" style="margin-bottom:5px;">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon text-left" style="top:0;width:130px;text-align:left;">Your balance：</span>
                        <span class="form-control" style="background-color:#eee;cursor:no-drop;"><fmt:formatNumber type="number" value="${vinEnable.enableAmount}" pattern="0.0000" maxFractionDigits="4"/>  BIEX</span>
                    </div>
                </div>
                <%--<div class="form-group">
                    <div class="col-xs-8 col-xs-offset-2 input-group">
                        <span class="input-group-addon text-left" style="top:0;width:130px;text-align:left;">GA code：</span>
                        <input class="form-control" name="ga" data-display="UID" placeholder="Please enter GA code"/>
                    </div>
                </div>--%>
                <c:if test="${awardStatus eq 0}">
                <div class="form-group" style="margin-bottom:5px;">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group text-left" style="margin-bottom:5px;">
                        <a id="InvitationSwitch" herf="javascript:void(0)" class="text-primary" style="cursor:pointer;box-shadow:0 1px;text-decoration:none">Invitation Code</a>
                    </div>
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group" id="InvitationDiv" style="display:none">
                        <span class="input-group-addon text-left" style="top:0;width:130px;text-align:left;">Invitation Code：</span>
                        <input class="form-control" id="unid" name="unid" data-display="UID" placeholder="Please enter the invitation code(If you have)"/>
                    </div>
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 text-left" style="padding:0;font-weight:bold">
                    <span class="text-success">
                        <span class="glyphicon glyphicon-volume-up"></span>
                        <span>If you fill in the invitation code, activation fee can be 5% discount.</span>
                    </span>
                    </div>
                </div>
                </c:if>
                <div class="form-group" style="margin-bottom:5px;">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <button type="button" id="activeTokenButton" class="btn btn-primary btn-block"  <c:if test="${longStatus == false}" >disabled</c:if> >Submit</button>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <a class="pull-right" style="cursor:pointer;" href="${ctx}/exchange?contractAddr=0x806336c912762274bfc4d0f78b1be2c0119e86f0">Buy BIEX Token</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script>
    seajs.use(['validator', 'i18n', 'qrcode', 'plupload'], function (Validator, I18n, qrcode) {

        validator = new Validator();

        $('#unid').on("blur",function () {
            if($('#unid').val() == ''){
                $("#actFee").html('<fmt:formatNumber type="number" value="${vinOpenFee}" pattern="#,##0.0000" maxFractionDigits="4"/> BIEX');
            }
            else {
                $("#actFee").html('<del style=\'margin-right:5px;\'><fmt:formatNumber type="number" value="${vinOpenFee}" pattern="#,##0.0000" maxFractionDigits="4"/></del><fmt:formatNumber type="number" value="${vinOpenFee*0.95}" pattern="0.0000" maxFractionDigits="4"/> BIEX');
            }
        });

        <%-- 开通TOKEN交易 --%>
        $("#activeTokenButton").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#openTokenForm',
                autoSubmit: false, <%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        confirmDialog("Please comfirm your operation.",function () {
                            $.ajax({
                                url: '${ctx}/exchange/openToken',
                                type: 'post',
                                data: element.serialize(),
                                beforeSend: function () {
                                    $("#activeTokenButton").attr("disabled", true);
                                },
                                success: function (data, textStatus, jqXHR) {
                                    $('#openTokenForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                    data = JSON.parse(data);
                                    if (data.code == bitms.success) {
                                        remind(remindType.success, data.message, 1000);
                                        location.href="${ctx}/exchange?contractAddr=${ERC20Contract}"
                                    } else {
                                        remind(remindType.error, data.message, 1000);
                                    }
                                },
                                complete: function () {
                                    $("#activeTokenButton").attr("disabled", false);
                                }
                            });
                        });
                    }
                }
            })
              /*  .addItem({
                element: '#openTokenForm [name=ga]',
                required: true
            })*/
            <c:if test="${awardStatus eq 0}">
            // 邀请人unid 非必填
                .addItem({
                    element: '#openTokenForm [name=unid]',
                    rule:'minlength{min:6} maxlength{max:48}'
                });
            </c:if>
            $("#openTokenForm").submit();
        });
        
        $("#InvitationSwitch").on("click", function () {
            if($("#InvitationDiv").css("display") == "none"){
                $("#InvitationDiv").show();
            }
            else{
                $("#InvitationDiv").hide();
            }
        })
    });
</script>
</body>
</html>