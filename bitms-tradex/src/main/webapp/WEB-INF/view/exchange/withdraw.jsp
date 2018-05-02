<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp" %>
<%
    request.setAttribute("eth", FundConsts.WALLET_ETH_TYPE);
%>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-sm-5 col-xs-12 padding-xs-none" style="margin-bottom:10px;">
            <div style="border:1px solid #ddd;">
                <h4 class="loginTit" style="display:block;background-color:#e8e8e8;padding:7px;font-size:12px;font-weight:bold;">
                    <span>Withdraw</span>
                    <a class="pull-right" href="${ctx}/exchange?contractAddr=${ERC20Contract}" style="cursor:pointer">Go to Trade</a>
                </h4>
                <div class="text-center" style="padding-bottom:5px;margin:20px auto;max-width:600px;">
                    <form:form class="form-horizontal" data-widget="validator" id="withdrawForm">
                        <input name="stockinfoId" type="hidden" value="${stockinfoId}"/>
                        <input id="limitAmount" type="hidden" value=""/>
                        <c:if test="${stockinfoId ne eth}">
                            <div class="form-group">
                                <div class="col-xs-10 col-xs-offset-1 input-group">
                                    <p class="alert alert-danger" style="background-color:#f2dede;border-color:#ebccd1;color:#a94442;padding:10px;background-image:none;margin-bottom:0px;">
                                        <span class="glyphicon glyphicon-exclamation-sign"></span>
                                        <span>Please check that you are withdrawing this token</span>
                                        <br>
                                        <span class="text-success text-left" style="font-weight:bold;">
                                            <span>Name:${symbol}(Symbol:${symbolName})</span>
                                            <br>
                                            <span>ERC20 Contract Address:${fn:substring(ERC20Contract,0,10)}...${fn:substring(ERC20Contract,34,42)}</span>
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </c:if>
                        <div class="form-group">
                            <small class="col-xs-10 col-xs-offset-1 text-danger text-right" style="padding:0;">Daily limited：<span id="usedCnt"></span>/<span id="totalCnt"></span>Times&nbsp;&nbsp;<span id="ethUpLine"></span>ETH/Per</small>
                            <div class="col-xs-10 col-xs-offset-1 input-group text-left">
                                <span class="input-group-addon glyphicon glyphicon-credit-card" style="top:0;"></span>
                                <span class="form-control" style="background-color:#eee;cursor:no-drop;">
                                    <span>Availible：</span>
                                    <input type="hidden" value="" id="enableAmount_check" />
                                    <span id="enableAmount"></span>
                                    <c:if test="${stockinfoId eq eth}">
                                        <span>ETH</span>
                                    </c:if>
                                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-10 col-xs-offset-1 input-group text-left">
                                <span class="input-group-addon glyphicon glyphicon-bookmark" style="top:0;"></span>
                                <span class="form-control" style="background-color:#eee;cursor:no-drop;">
                                    <span>Fee：</span>
                                    <span id="fee"></span>
                                </span>
                            </div>
                        </div>
                        <div id="confirmContext">
                            <div class="form-group ui-form-item">
                                <div class="col-xs-10 col-xs-offset-1 input-group">
                                    <span class="input-group-addon glyphicon glyphicon-barcode" style="top:0;"></span>
                                    <input type="email" class="form-control" name="address" data-display="Address" placeholder="Address"/>
                                </div>
                            </div>
                            <div class="form-group ui-form-item">
                                <div class="col-xs-10 col-xs-offset-1 input-group">
                                    <span class="input-group-addon glyphicon glyphicon-edit" style="top:0;"></span>
                                    <input type="text" class="form-control" name="amount" data-display="Amount" placeholder="Amount"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group ui-form-item">
                            <div class="col-xs-10 col-xs-offset-1 input-group">
                                <span class="input-group-addon glyphicon glyphicon-time" style="top:0;"></span>
                                <input type="text" class="form-control" name="ga" data-display="Google Auth" placeholder="Google Auth"/>
                            </div>
                        </div>
                        <div class="form-group" style="margin-bottom:0;">
                            <div class="col-xs-10 col-xs-offset-1 input-group">
                                <button type="button" id="withdrawSubmit" class="btn btn-primary btn-block"  <c:if test="${longStatus == false}" >disabled</c:if> >Submit</button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
        <div class="col-sm-7 col-xs-12 padding-xs-none">
            <div class="table-responsive">
                <table class="table table-striped table-condensed table-bordered text-center" style="font-size:12px;margin-bottom:0;">
                    <thead>
                    <tr style="border:1px solid #ddd;">
                        <th class="text-center" style="background-color:#e8e8e8;border-left:1px solid #ddd;width:20%">Date</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:45%">Withdraw address</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:15%">Amount</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:10%">Status</th>
                        <th class="text-center" style="background-color:#e8e8e8;border-right:1px solid #ddd;width:10%">Operation</th>
                    </tr>
                    </thead>
                    <tbody id="withdrawHistory">

                    </tbody>
                </table>
                <div id="pagination" class="paginationContainer"></div>
                <div class="text-center" id="listTip" style="margin:100px;">
                    <span class="glyphicon glyphicon-list-alt" style="font-size:100px;color:#ddd;margin-bottom:20px;"></span>
                    <p>You have no withdraw History</p>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script id="withdrawHistory_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDate $value.currentDate}}</td>
        <td>{{$formatFlag3 $value.withdrawAddr $value.transId}}</td>
        <td>{{$subFlag $value.occurAmt,$value.fee}}</td>
        <td>{{$formatFlag $value.approveStatus}}</td>
        <td>{{$actionFlag $value.approveStatus,$value.id}}</td>
    </tr>
    {{/each}}
</script>
<script>
    var withdrawHistory = null;
    var maxAmount = 0;
    seajs.use([ 'validator','pagination', 'template', 'moment'], function(Validator,Pagination, Template, moment) {

        validator = new Validator();

        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });
        template.helper('$formatFlag', function(flag) {
            return getDictValueByCode(flag);
        });
        template.helper('$subFlag', function(occurAmt,netFee) {
            return comdify(((parseFloat(occurAmt)*10000-parseFloat(netFee)*10000)/10000).toFixed(4));
        });
        template.helper('$formatFlag3', function(flag,transId) {
            if(transId == null){
                return flag;
            }
            else{
                return flag+"&nbsp;<a class='text-success' target='_blank' href='https://etherscan.io/tx/"+transId+"'>Check</a>";
            }
        });
        template.helper('$actionFlag', function(flag,id) {
            if (flag == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING %>' || flag == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_WAITING_EMAIL_CONFIRM %>' ) {
                return '<div class="btn btn-primary btn-xs" onclick="cancel(\'id'+id+'\')">Cancel<div>';
            } else {
                return '';
            }

        });

        if('${longStatus}' == 'true') {

            <%--提现记录--%>
            withdrawHistory = new Pagination({
                url: "${ctx}/exchange/withdrawEth/withdrawEthList",
                data: {
                    contractAddr: '${ERC20Contract}'
                },
                elem : "#pagination",
                rows: 10,
                method: "get",
                handleData: function (json) {
                    var html = Template.render("withdrawHistory_list_tpl", json);
                    $("#withdrawHistory").html(html);

                    <%--判断图标是否显示--%>
                    if(json.rows.length > 0){
                        $("#listTip").hide();
                    }else {
                        $("#listTip").show();
                    }
                }
            });

            getEnableTrade();

            <%--提现申请--%>
            $("#withdrawSubmit").on("click",function () {
                validator.destroy();
                validator = new Validator({
                    element: '#withdrawForm',
                    autoSubmit: false,<%--当验证通过后不自动提交--%>
                    onFormValidated: function (error, results, element) {
                        if (!error) {
                            var address = $("#withdrawForm input[name=address]").val();
                            var amount = $("#withdrawForm input[name=amount]").val();
                            confirmDialog("Address:"+address+"<br>Amount:"+amount,function () {
                                $.ajax({
                                    url : '${ctx}/exchange/withdrawEth/withdrawEth',
                                    type : 'post',
                                    data : $("#withdrawForm").serialize(),
                                    beforeSend:function(){
                                        $('#withdrawSubmit').attr("disabled",true);
                                    },
                                    success : function(data,textStatus, jqXHR) {
                                        $('#withdrawForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                        data = JSON.parse(data);
                                        if (data.code == 200) {
                                            remind(remindType.success,data.message,500);
                                            setTimeout("location.reload();",500);
                                        }
                                        else {
                                            remind(remindType.error,data.message,1000);
                                        }
                                    },
                                    complete:function(){
                                        $('#withdrawSubmit').attr("disabled",false);
                                    },
                                    error : function(
                                        XMLHttpRequest,
                                        textStatus) {
                                        console.log(textStatus);
                                    }
                                });
                            });
                        }
                    }
                }).addItem({
                    element: '#withdrawForm [name=address]',
                    required : true
                }).addItem({
                    element: '#withdrawForm [name=amount]',
                    required : true,
                    rule:'number min{min:0.0001} max{max:'+maxAmount+'} numberOfDigits{maxLength:4}'
                }).addItem({
                    element: '#withdrawForm [name=ga]',
                    required : true
                });
                $("#withdrawForm").submit();
            });

        }

    });

    <%--获取提现数据--%>
    function getEnableTrade() {
        $.ajax({
            url: '${ctx}/exchange/getEnable',
            data: {id:"${stockinfoId}"},
            type: 'get',
            success: function (data, textStatus, jqXHR) {
                data = JSON.parse(data);
                $("#usedCnt").text(data.object.usedCnt);
                $("#totalCnt").text(data.object.totalCnt);
                $("#ethUpLine").text(data.object.ethUpLine.toFixed(2));
                $("#enableAmount").text(data.object.enableModel.enableAmount.toFixed(4));
                $("#enableAmount_check").val(data.object.enableModel.enableAmount);
                $("#fee").text(data.object.fee);
                $("#limitAmount").val(data.object.limitAmount);

                <%--最大可提数量（比较可用和单笔额度)--%>
                if($("#enableAmount_check").val()<=$("#limitAmount").val()){
                    maxAmount = $("#enableAmount_check").val();
                }
                else{
                    maxAmount = $("#limitAmount").val();
                }
            }
        });
    }

    <%--取消提币--%>
    function cancel(id){
        $.ajax({
            url : '${ctx}/exchange/withdrawEth/withdrawEthCancel',
            data : {
                "id" : id.substr(2),
                "stockinfoId" : ${stockinfoId}
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

</script>
</body>
</html>