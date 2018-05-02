<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon">
            <h2 class="loginTit">Active Coin Trading</h2>
            <hr>
            <form:form class="form-horizontal" data-widget="validator" id="openTradeForm">
                <input name="addr" value="${ERC20Contract}" type="hidden" />
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-tag" style="top:0;"></span>
                        <span class="form-control" style="background-color:#eee;cursor:no-drop;">Symbol：${stockinfo.stockCode}</span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-list-alt" style="top:0;"></span>
                        <span class="form-control" style="background-color:#eee;cursor:no-drop;">Name：${stockinfo.stockName}</span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-barcode" style="top:0;"></span>
                        <a onMouseOver="this.style.textDecoration='none'" target="_blank" href="https://etherscan.io/token/${stockinfo.tokenContactAddr}"><span class="form-control" style="background-color:#eee;">Token：${stockinfo.tokenContactAddr}</span></a>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon" style="top:0;width:130px;text-align:left;">Amount：</span>
                        <span class="form-control" id="actFee" style="background-color:#eee;cursor:no-drop;"><fmt:formatNumber type="number" value="${accountWalletAsset.amount}" pattern="#,##0.0000" maxFractionDigits="4"/>&nbsp;&nbsp;${stockinfo.stockCode}</span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon text-left" style="top:0;width:130px;text-align:left;">Frozen Amount：</span>
                        <span class="form-control" style="background-color:#eee;cursor:no-drop;"><fmt:formatNumber type="number" value="${accountWalletAsset.frozenAmt}" pattern="#,##0.0000" maxFractionDigits="4"/>&nbsp;&nbsp;${stockinfo.stockCode}</span>
                    </div>
                </div>
                <c:if test="${accountWalletAsset.isActiveTrade eq 'no'}">
                    <div class="form-group">
                        <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                            <span class="input-group-addon text-left" style="top:0;width:130px;text-align:left;">GA code：</span>
                            <input class="form-control" name="ga" data-display="UID" placeholder="Please enter GA code"/>
                        </div>
                    </div>
                </c:if>
                <div class="form-group" style="margin-bottom:5px;">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <input type="hidden" name="stockinfoId" value="${stockinfo.id}" />
                        <c:if test="${accountWalletAsset.isActiveTrade eq 'yes'}">
                            <input type="hidden" name="status" value="no" />
                            <button type="button" id="activeTradeButton" class="btn btn-danger btn-block" >Close Trade</button>
                        </c:if>
                        <c:if test="${accountWalletAsset.isActiveTrade eq 'no'}">
                            <input type="hidden" name="status" value="yes" />
                            <button type="button" id="activeTradeButton" class="btn btn-primary btn-block" >Active Trade</button>
                        </c:if>
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

        <%-- 开通TOKEN交易 --%>
        $("#activeTradeButton").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#openTradeForm',
                autoSubmit: false, <%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        confirmDialog("Please comfirm your operation.",function () {
                            $.ajax({
                                url: '${ctx}/wallet/doActiveTrade',
                                type: 'post',
                                data: element.serialize(),
                                beforeSend: function () {
                                    $("#activeTradeButton").attr("disabled", true);
                                },
                                success: function (data, textStatus, jqXHR) {
                                    $('#openTradeForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                    data = JSON.parse(data);
                                    if (data.code == bitms.success) {
                                        remind(remindType.success, data.message, 1000);
                                        location.reload();
                                    } else {
                                        remind(remindType.error, data.message, 1000);
                                    }
                                },
                                complete: function () {
                                    $("#activeTradeButton").attr("disabled", false);
                                }
                            });
                        });
                    }
                }
            })
            <c:if test="${accountWalletAsset.isActiveTrade eq 'no'}">
            // ga
                    .addItem({
                        element: '#openTradeForm [name=ga]',
                        required: true
                    });
            </c:if>
            $("#openTradeForm").submit();
        });
    });
</script>
</body>
</html>