<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-xs-12 padding-xs-none">
            <div class="table-responsive">
                <table class="table table-hover table-condensed table-bordered text-center" style="font-size: 12px;">
                    <thead>
                    <tr>
                        <th class="text-left" style="background-color:#e8e8e8;padding-left:20px;width:10%">Symbol</th>
                        <th class="text-center hidden-xs" style="background-color:#e8e8e8;width:15%">Name</th>
                        <th class="text-center hidden-xs" style="background-color:#e8e8e8;width:22%">ERC20 Contract Address</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:13%">Available</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:13%">Frozen</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:23%">Operation</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:4%">Status</th>
                    </tr>
                    </thead>
                    <tbody id="Asset">

                    </tbody>
                </table>
                <div id="pagination" class="paginationContainer"></div>
            </div>
        </div>
    </div>
</div>
<%--交易开关弹窗--%>
<div class="modal fade" id="activeTradeRule" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header text-center" style="padding:10px;">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title text-danger">
                    <span class="glyphicon glyphicon-warning-sign"></span>
                    <span>RISK WARNING</span>
                </h3>
            </div>
            <div class="modal-body table-responsive" style="padding-top:10px;">
                <form:form class="form-horizontal" data-widget="validator" id="activeTradeForm">
                    <input hidden id="addr" name="addr" />
                    <input hidden id="status" name="status" />
                    <input hidden id="stockinfoId" name="stockinfoId" />
                    <p style="margin-bottom:5px;">1. BIEX is a neutral Token trade service platform. It will not evaluate or endorse any Token. All Token trading is activated and carried out by users themselves. You should fully understand that Token trading is extremely risky and you should be able to bear any irreparable loss caused thereby.</p>
                    <p style="margin-bottom:5px;">2. Since the ERC20 Token Standard allows multiple use of the same symbol and name, a Token’s identity cannot be accurately determined based on them. You should determine the identity of a Token based on the Token's unique contract address. We marked Tokens’ contract addresses as clearly as possible, and we added a distinguishing mark to each Token's Symbol. The mark consists of two digits identical with the last two digits of the Token's contract address.</p>
                    <p style="margin-bottom:0;">3. You are obliged to fully understand and be familiar with the trading pair you are about to enable, and you should be aware of the risks involved. We strongly remind you that for the upcoming trading pair, the Token's contract address is: <span id="contractAddr" style="display:block;margin:5px auto;text-align:center" class="text-danger"></span> Please, verify personally with the official team of the Token that the contract address of the Token to enable the trading pair is correct, otherwise, please, do not enable it.</p>
                    <p style="margin-bottom:5px;">4. You should carefully understand and, if necessary, consult a lawyer to verify that your Token trading is legal and compliant with the regulations of your region or country. Otherwise, please do not enable it.</p>
                    <p style="margin-bottom:5px;">5. If the Token trading is forbidden due to your local policy or regulation, we have the right to unilaterally stop the service to you.</p>
                    <div class="form-group" style="margin-bottom:10px;">
                        <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                            <span class="input-group-addon text-left" style="top:0;width:130px;text-align:left;">GA code：</span>
                            <input class="form-control" id="confirmGa" name="ga" data-display="UID" placeholder="Please enter GA code"/>
                        </div>
                    </div>
                    <div class="form-group" style="margin-bottom:0;">
                        <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                            <button type="button" id="activeTradeComfirm" class="btn btn-primary btn-block" >I understand and comfirm to Enable</button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script id="Asset_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td onclick="jumpUrl('${ctx}/exchange?contractAddr={{$value.tokenContactAddr}}')" title="Go to Trade" style="padding-left:15px;cursor:pointer;" class="text-left"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/tokenLogo/{{$value.tokenContactAddr}}.svg" onerror="this.src='${imagesPath}/tokenLogo/token.svg'">{{$value.stockCode}}<sup>{{$supFlag $value.tokenContactAddr}}</sup></td>
        <td style="cursor:pointer;" class="hidden-xs">{{$value.stockName}}</td>
        <td class="hidden-xs">{{$addrFlag $value.tokenContactAddr $value.relatedStockinfoId}}</td>
        <td onclick="jumpUrl('${ctx}/exchange?contractAddr={{$value.tokenContactAddr}}')" title="Go to Trade" style="cursor:pointer;">{{$subDataFlag $value.amount $value.frozenAmt}}</td>
        <td onclick="jumpUrl('${ctx}/exchange?contractAddr={{$value.tokenContactAddr}}')" title="Go to Trade" style="cursor:pointer;">{{$fix4Flag $value.frozenAmt}}</td>
        <td>{{$operationFlag $value.tokenContactAddr $value.relatedStockinfoId $value.isActiveTrade $value.canRecharge $value.canWithdraw $value.canTrade}}</td>
        <td>{{$activeFlag $value.tokenContactAddr $value.relatedStockinfoId $value.isActiveTrade}}</td>
    </tr>
    {{/each}}
</script>
<script>
    var Asset = null;
    seajs.use([ 'pagination', 'validator', 'template', 'moment', 'qrcode', 'my97DatePicker' ], function(Pagination, Validator, Template, moment, qrcode) {

        validator = new Validator();

        template.helper('$fix4Flag', function (flag) {
            if(flag == '' || flag == null){
                return '0.0000';
            }
            return comdify(parseFloat(flag).toFixed(4));

        });
        template.helper('$fix8Flag', function (flag) {
            if(flag == '' || flag == null){
                return '0.00000000';
            }
            return comdify(parseFloat(flag).toFixed(8));

        });
        template.helper('$addrFlag', function (flag,flag2) {
            if(flag2 != <%=FundConsts.WALLET_ETH_TYPE%>) {
                return "<a target='_blank' href='https://etherscan.io/token/" + flag + "'>" + flag + "</a>";
            }
        });
        template.helper('$subDataFlag', function (amount,frozenAmt) {
            if(amount == '' || amount == null){
                amount=0.0000;
            }
            if(frozenAmt == '' || frozenAmt == null){
                frozenAmt=0.0000;
            }
            amount = parseFloat(amount);
            frozenAmt = parseFloat(frozenAmt);
            return comdify(parseFloat(amount-frozenAmt).toFixed(4));

        });
        template.helper('$operationFlag', function (flag,flag2,flag3,canRecharge,canWithdraw,canTrade) {
            if(flag != null && flag != ''){
                if(flag2 != <%=FundConsts.WALLET_ETH_TYPE%>){
                    if(flag3 == 'no' && flag2 != <%=FundConsts.WALLET_BIEX_TYPE%>){
                        return (canRecharge=='yes'?'<button disabled type="button" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Dep.</span><span class="hidden-xs">Deposit</span></button>':'<button disabled type="button" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Sus.</span><span class="hidden-xs">Suspend</span></button>') +
                        (canWithdraw=='yes'?'<button disabled type="button" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Wi.</span><span class="hidden-xs">Withdraw</span></button>':'<button disabled type="button" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Sus.</span><span class="hidden-xs">Suspend</span></button>') +
                        (canTrade=='yes'?'<button disabled type="button" class="btn btn-primary btn-xs hidden-xs" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;">Trade</button>':'<button disabled type="button" class="btn btn-primary btn-xs hidden-xs" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Sus.</span><span class="hidden-xs">Suspend</span></button>');
                    }
                    else {
                        return (canRecharge=='yes'?'<button type="button" onclick="jumpUrl(\'${ctx}/exchange/deposit?contractAddr='+flag+'\')" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Dep.</span><span class="hidden-xs">Deposit</span></button>':'<button disabled type="button" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Sus.</span><span class="hidden-xs">Suspend</span></button>') +
                        (canWithdraw=='yes'?'<button type="button" onclick="jumpUrl(\'${ctx}/exchange/withdraw?contractAddr='+flag+'&trade=0\')" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Wi.</span><span class="hidden-xs">Withdraw</span></button>':'<button disabled type="button" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Sus.</span><span class="hidden-xs">Suspend</span></button>') +
                        (canTrade=='yes'?'<button type="button" onclick="jumpUrl(\'${ctx}/exchange?contractAddr='+flag+'\')" class="btn btn-primary btn-xs hidden-xs" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;">Trade</button>':'<button disabled type="button" class="btn btn-primary btn-xs hidden-xs" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Sus.</span><span class="hidden-xs">Suspend</span></button>');
                    }
                }
                else{
                    return (canRecharge=='yes'?'<button type="button" onclick="jumpUrl(\'${ctx}/exchange/deposit?contractAddr='+flag+'\')" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Dep.</span><span class="hidden-xs">Deposit</span></button>':'<button disabled type="button" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Sus.</span><span class="hidden-xs">Suspend</span></button>') +
                    (canWithdraw=='yes'?'<button type="button" onclick="jumpUrl(\'${ctx}/exchange/withdraw?contractAddr='+flag+'&trade=1\')" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Wi.</span><span class="hidden-xs">Withdraw</span></button>':'<button disabled type="button" class="btn btn-primary btn-xs width-xs-btn" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;"><span class="visible-xs">Sus.</span><span class="hidden-xs">Suspend</span></button>') +
                    '<button type="button" onclick="jumpUrl(\'${ctx}/exchange/billHistory?contractAddr='+flag+'\')" class="btn btn-primary btn-xs hidden-xs" style="box-shadow:none;background-image:none;border:none;text-shadow:none;width:60px;margin-left:5px;margin-right:5px;">History</button>';
                }
            }
        });
        template.helper('$supFlag', function (flag) {
            if(flag != null && flag != ''){
                return flag.substr(40,2);
            }
        });
        template.helper('$activeFlag', function (flag, flag2, flag3) {
            if(flag != null && flag != ''){
                if(flag2 != <%=FundConsts.WALLET_ETH_TYPE%> && flag2 != <%=FundConsts.WALLET_BIEX_TYPE%>){
                    if(flag3 == 'no')
                    {
                        return '<input type="checkbox" class="chooseBtn"><label onclick="activeTradeBtn(\''+flag+'\',\''+flag2+'\',\''+flag3+'\')" style="vertical-align:middle" class="choose-label" data-on="Enable" data-off="Disable"></label>';
                    }
                    else
                    {
                        return '<input type="checkbox" class="chooseBtn" checked><label onclick="activeTradeBtn(\''+flag+'\',\''+flag2+'\',\''+flag3+'\')" style="vertical-align:middle" class="choose-label" data-on="Enable" data-off="Disable"></label>';
                    }
                }
            }
        });

        if('${longStatus}' == 'true') {

            <%--获取资产列表--%>
            Asset = new Pagination({
                url: "${ctx}/accountAsset/walletAssetData",
                elem : "#pagination",
                rows: 50,
                method: "post",
                handleData: function (json) {
                    var html = Template.render("Asset_list_tpl", json);
                    $("#Asset").html(html);
                }
            });

            <%-- 交易开关 --%>
            function activeTrade() {
                validator.destroy();
                validator = new Validator({
                    element: '#activeTradeForm',
                    autoSubmit: false, <%--当验证通过后不自动提交--%>
                    onFormValidated: function (error, results, element) {
                        if (!error) {
                            $.ajax({
                                url: '${ctx}/wallet/doActiveTrade',
                                type: 'post',
                                data: element.serialize(),
                                beforeSend: function () {
                                    $("#activeTradeButton").attr("disabled", true);
                                },
                                success: function (data, textStatus, jqXHR) {
                                    $('#activeTradeForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                    data = JSON.parse(data);
                                    if (data.code == bitms.success) {
                                        remind(remindType.success, data.message, 1000);
                                        setTimeout("location.reload();",1000);
                                    } else {
                                        remind(remindType.error, data.message, 1000);
                                    }
                                },
                                complete: function () {
                                    $("#activeTradeButton").attr("disabled", false);
                                }
                            });
                        }
                    }
                }).addItem({
                    element: '#activeTradeForm [name=ga]',
                    required: true
                });
                $("#activeTradeForm").submit();
            }

            $("#activeTradeComfirm").click(function () {
                activeTrade();
            });
        }
    });

    <%--交易开关按钮--%>
    function activeTradeBtn(contractAddr,stockinfoId,activeStatus) {
        if (activeStatus == 'no') {
            $('#activeTradeRule').modal('show');
            $("#contractAddr").text(contractAddr);
            $("#stockinfoId").attr("value",stockinfoId);
            $("#addr").attr("value",contractAddr);
            $("#status").attr("value","yes");
        }
        else {
            $("#stockinfoId").attr("value",stockinfoId);
            $("#addr").attr("value",contractAddr);
            $("#status").attr("value","no");
            closeTrade();
        }
    }

    function closeTrade() {
        $.ajax({
            url: '${ctx}/wallet/doActiveTrade',
            type: 'post',
            data: $('#activeTradeForm').serialize(),
            beforeSend: function () {
                $("#activeTradeButton").attr("disabled", true);
            },
            success: function (data, textStatus, jqXHR) {
                $('#activeTradeForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                data = JSON.parse(data);
                if (data.code == bitms.success) {
                    remind(remindType.success, data.message, 1000);
                    setTimeout("location.reload();",1000);
                } else {
                    remind(remindType.error, data.message, 1000);
                }
            },
            complete: function () {
                $("#activeTradeButton").attr("disabled", false);
            }
        });
    }

</script>
</body>
</html>