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
                <li class="active">
                    <a href="#" data-toggle="tab"><%--账户资产--%><fmt:message key="sidebar.accoutAsset" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/charge')">
                    <a href="#" data-toggle="tab"><%--充值充币--%><fmt:message key="sidebar.charge" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/withdraw')">
                    <a href="#" data-toggle="tab"><%--提现提币--%><fmt:message key="sidebar.raise" /></a>
                </li>
                <%--<li onclick="jumpUrl('${ctx}/fund/chargeCash')">
                    <a href="#" data-toggle="tab">&lt;%&ndash;现金充值&ndash;%&gt;<fmt:message key="fund.chargeCash.title" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/withdrawCash')">
                    <a href="#" data-toggle="tab">&lt;%&ndash;现金提现&ndash;%&gt;<fmt:message key="fund.raiseCash.title" /></a>
                </li>--%>
                <%--<li class="pull-right">
                    <a href="#" style="background-color: transparent;color: #888;cursor: auto;">
                        <span>总资产估值&lt;%&ndash;Estimated Value&ndash;%&gt;：</span>
                        <span>0.00000000</span>
                        <span>BTC / $</span>
                        <span>0.00</span>
                    </a>
                </li>--%>
            </ul>
            <form data-widget="validator" name="walletAssetForm" id="walletAssetForm" method="post">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th style="width: 14%;"><%--账户类型--%><fmt:message key="fund.currents.accountType" /></th>
                                <th style="width: 9%;"><%--资产分类--%><fmt:message key="fund.currents.assetClassify" /></th>
                                <th style="width: 10%;"><%--全称--%><fmt:message key="fund.accountAsset.name" /></th>
                                <th style="width: 14%;"><%--可用--%><fmt:message key="spot.C2C.available" /></th>
                                <th style="width: 14%;"><%--冻结--%><fmt:message key="spot.C2C.freeze" /></th>
                                <th style="width: 14%;"><%--负债--%><fmt:message key="spot.C2C.debit" /></th>
                                <th style="width: 35%;"><%--操作--%><fmt:message key="operation" /></th>
                            </tr>
                        </thead>
                        <tbody id="list_emement_wallet">
                        </tbody>
                    </table>
                </div>
            </form>
            <form data-widget="validator" name="spotAssetForm" id="spotAssetForm" method="post">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <tbody id="list_emement_spot">

                        </tbody>
                    </table>
                </div>
            </form>
            <%--<form data-widget="validator" name="contractAssetForm" id="contractAssetForm" method="post">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <tbody id="list_emement_contract">

                        </tbody>
                    </table>
                </div>
            </form>--%>
            <%--<form data-widget="validator" name="wealthAssetForm" id="wealthAssetForm" method="post">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <tbody id="list_emement_wealth">

                        </tbody>
                    </table>
                </div>
            </form>--%>
        </div>
    </div>
</div>
<div class="modal fade" id="conversionModal" tabindex="-1" role="dialog">
    <input type="hidden" id="enableAmount" value='0'/>
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title text-success"><%--账户互转--%><fmt:message key="sidebar.conversion"/></h4>
            </div>
            <div class="modal-body clearfix" id="viewEntrustVCoinMoneyBody">
                <form:form id="conversionForm" method="post" data-widget="validator" >
                    <input type="hidden" id="businessFlag" name="businessFlag" value="">
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"><%--转移币种--%><fmt:message key="spot.pushTrade.transferCurrency"/></label>
                        <div class="col-xs-7 col-sm-9">
                            <select class="form-control" id="xianshibizhong">
                                <option>BTC</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"><%--从--%><fmt:message key="fund.conversion.from"/></label>
                        <div class="col-xs-7 col-sm-9">
                            <select class="form-control" id="stockinfoId2" name="stockinfoId" data-display="<%--转出钱包--%>" ></select>
                        </div>
                    </div>
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"><%--转至--%><fmt:message key="fund.conversion.to"/></label>
                        <div class="col-xs-7 col-sm-9">
                            <select class="form-control" id="stockinfoIdEx" name="stockinfoIdEx" data-display="<%--存入钱包--%>"></select>
                        </div>
                    </div>
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"><%--转账数量--%><fmt:message key="fund.conversion.conversionNum"/></label>
                        <div class="col-xs-7 col-sm-9 ui-form-item">
                            <input autocomplete="no" class="form-control" name="amount" placeholder="" data-display="Transfer amount">
                            <small>
                                <span><%--可转数量--%><fmt:message key="spot.currencyTrade.available"/>：</span>
                                <span id="maxAmount"></span>
                            </small>
                            <br>
                            <small id="maxAmountTxt" class="text-danger">You cannot transfer from margin account to wallet account because of existing debts or insufficient  balance.</small>
                        </div>
                    </div>
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"></label>
                        <div class="col-xs-7 col-sm-9">
                            <button type="button" id="conversionSubmit" class="btn btn-primary"><%--提交--%><fmt:message key="submit"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="wealthModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title text-success"><%--账户互转--%><fmt:message key="sidebar.conversion"/></h4>
            </div>
            <div class="modal-body clearfix" id="viewEntrustVCoinMoneyBody2">
                <form:form id="wealthForm" method="post" data-widget="validator" >
                    <input type="hidden" id="businessFlagSpot" name="businessFlag" value="">
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"><%--转移币种--%><fmt:message key="spot.pushTrade.transferCurrency"/></label>
                        <div class="col-xs-7 col-sm-9">
                            <select class="form-control" id="xianshibizhong2">
                                <option>BTC</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"><%--从--%><fmt:message key="fund.conversion.from"/></label>
                        <div class="col-xs-7 col-sm-9">
                            <select class="form-control" id="stockinfoId2Spot" name="stockinfoId" data-display="<%--转出钱包--%>" >
                            </select>
                        </div>
                    </div>
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"><%--转至--%><fmt:message key="fund.conversion.to"/></label>
                        <div class="col-xs-7 col-sm-9">
                            <select class="form-control" id="stockinfoIdExSpot" name="stockinfoIdEx" data-display="<%--存入钱包--%>">
                            </select>
                        </div>
                    </div>
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"><%--转账数量--%><fmt:message key="fund.conversion.conversionNum"/></label>
                        <div class="col-xs-7 col-sm-9 ui-form-item">
                            <input autocomplete="no" class="form-control" name="amount" placeholder="" data-display="Transfer amount">
                            <small>
                                <span><%--可转数量--%><fmt:message key="spot.currencyTrade.available"/>：</span>
                                <span id="maxAmountSpot"></span>
                            </small>
                        </div>
                    </div>
                    <div class="form-group clearfix">
                        <label class="col-xs-5 col-sm-3 control-label"></label>
                        <div class="col-xs-7 col-sm-9">
                            <button type="button" id="wealthSubmit" class="btn btn-primary"><%--提交--%><fmt:message key="submit"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<script id="list_tpl_wallet" type="text/html">
    {{each rows}}
    <tr class="test">
        <td style="width: 14%;"><%--现货账户--%><fmt:message key="spot.C2C.walletAccount"/></td>
        <td style="width: 9%;"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/coinLogo/{{$value.stockCode}}.svg">{{$value.stockCode}}</td>
        <td style="width: 10%;">{{$value.stockName}}</td>
        <td style="width: 14%;">{{$subDataFlag $value.amount $value.frozenAmt}}</td>
        <td style="width: 14%;">{{$doubleDateFlag $value.frozenAmt}}</td>
        <td style="width: 14%;">-</td>
        <td style="width: 35%;">{{$formatFlagCharge $value.canRecharge $value.stockCode $value.stockType}}{{$formatFlagWithdraw $value.canWithdraw $value.stockCode $value.stockType}}<%--{{$formatFlagTrans $value.stockinfoId}}--%></td>
    </tr>
    {{/each}}
</script>
<script id="list_tpl_spot" type="text/html">
    {{each rows}}
    <tr class="test">
        <td style="width: 14%;"><%--杠杆账户--%><fmt:message key="spot.C2C.contractAccount"/></td>
        <td style="width: 9%;"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/coinLogo/{{$value.digitalStockCode}}.svg">{{$value.digitalStockCode}}</td>
        <td style="width: 10%;">{{$value.stockName}}</td>
        <td style="width: 14%;">{{$subDataFlag $value.amount $value.frozenamt}}</td>
        <td style="width: 14%;">{{$doubleDateFlag $value.frozenamt}}</td>
        <td style="width: 14%;">{{$doubleDateFlag $value.debitAmt}}</td>
        <td style="width: 35%;">
            {{$formatFlagWealth $value.canWealth,$value.stockInfoId,$value.digitalStockCode}}
            {{$formatFlagConversion $value.canConversion,$value.stockInfoId,$value.digitalStockCode}}
            <button type="button" onclick="jumpUrl('/spot/leveragedSpotTrade?exchangePair=btc2usd')" class="btn btn-success btn-xs" style="width:70px;"><%--交易--%><fmt:message key="trade" /></button>
        </td>
    </tr>
    {{/each}}
</script>
<%--<script id="list_tpl_contract" type="text/html">
    {{each rows}}
    <tr class="test">
        <td class="rowspanTd" style="width: 16%;" {{$value.index%2==0?'rowspan=2':'hidden=hidden'}}>&lt;%&ndash;现货合约&ndash;%&gt;<fmt:message key="spot.C2C.spot"/>[{{$value.legalStockCode}}]</td>
        <td style="width: 16%;"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/coinLogo/{{$value.digitalStockCode}}.svg">{{$value.digitalStockCode}}</td>
        <td style="width: 10%;">{{$subDataFlag $value.amount $value.frozenamt}}</td>
        <td style="width: 16%;">{{$doubleDateFlag $value.frozenamt}}</td>
        <td style="width: 16%;">{{$doubleDateFlag $value.debitAmt}}</td>
        <td style="width: 26%;">{{$formatFlagCharge $value.canRecharge}}{{$formatFlagWithdraw $value.canWithdraw}}</td>
    </tr>
    {{/each}}
</script>
<script id="list_tpl_wealth" type="text/html">
    {{each rows}}
    <tr class="test">
        <td style="width: 16%;">&lt;%&ndash;出借账户&ndash;%&gt;<fmt:message key="spot.C2C.lendAccount"/></td>
        <td style="width: 10%;"><img style="margin-right:3px;vertical-align:bottom;width:18px;height:18px;" src="${imagesPath}/coinLogo/{{$value.stockCode}}.svg">{{$value.stockCode}}</td>
        <td style="width: 16%;">{{$formatFlag2 $value.wealthAmt}}</td>
        <td style="width: 16%;">-</td>
        <td style="width: 16%;">-</td>
        <td style="width: 26%;">{{$formatFlagWealth $value.canWealth,$value.stockinfoId,$value.stockCode}}</td>
    </tr>
    {{/each}}
</script>--%>
<%--<script id="list_tpl_conversion" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDate $value.currentDate}}</td>
        <td>{{$value.stockName}}</td>
        <td>{{$formatFlag $value.businessFlag}}</td>
        <td>{{$formatFlag2 $value.occurAmt}}</td>
        <td>{{$formatFlag2 $value.walletLastAmt}}</td>
        <td>{{$formatFlag2 $value.lastAmt}}</td>
    </tr>
    {{/each}}
</script>--%>
<script>
    function trim(str) {
        return str.replace(/(^\s+)|(\s+$)/g, "");
    }
    var renderPageWallet;
    var renderPageWealth;
    var renderPageSpot;
    <%--var renderPageContract;--%>
    <%--var renderPageConversion;--%>
    seajs.use(['pagination', 'template', 'moment', 'validator','i18n'], function (Pagination, Template, moment, Validator,I18n) {
        $('.pageLoader').hide();
        template.helper('$formatDate', function (millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm");
        });
        template.helper('$doubleDateFlag', function (flag) {
            if(flag == '' || flag == null){
                return '0.00000000';
            }
            return parseFloat(flag).toFixed(8);

        });
        template.helper('$subDataFlag', function (amount,frozenAmt) {
            if(amount == '' || amount == null){
                amount=0;
            }
            if(frozenAmt == '' || frozenAmt == null){
                frozenAmt=0;
            }
            amount = parseFloat(amount);
            frozenAmt = parseFloat(frozenAmt);
            return parseFloat(amount-frozenAmt).toFixed(8);

        });
        template.helper('$formatFlagCharge', function (flag,flagCode,flagType) {
            var symbol = trim(flagCode.toLowerCase());
            if(flag == "yes"){
                if(flagType == "<%=FundConsts.STOCKTYPE_DIGITALCOIN %>"){
                    return '<button type="button" onclick="jumpUrl(\'${ctx}/fund/charge\')" class="btn btn-success btn-xs" style="width:70px;"><%--充币--%><fmt:message key="fund.accountAsset.charge" /></button>&nbsp;&nbsp;';
                }else if(flagType == "<%=FundConsts.STOCKTYPE_CASHCOIN %>"){
                    return '<button type="button" onclick="jumpUrl(\'${ctx}/fund/chargeCash?symbol='+symbol+'\')" class="btn btn-success btn-xs" style="width:70px;"><%--充币--%><fmt:message key="fund.accountAsset.charge" /></button>&nbsp;&nbsp;';
                }else if(flagType == "<%=FundConsts.STOCKTYPE_ERC20_TOKEN %>"){
                    return '<button type="button" onclick="jumpUrl(\'${ctx}/fund/chargeEth?symbol='+symbol+'\')" class="btn btn-success btn-xs" style="width:70px;"><%--充币--%><fmt:message key="fund.accountAsset.charge" /></button>&nbsp;&nbsp;';
                }
            }
            else {
                return '<button type="button" class="btn btn-success btn-xs" disabled style="width:70px;"><%--暂停--%><fmt:message key="stop" /></button>&nbsp;&nbsp;';
            }
        });
        template.helper('$formatFlagWithdraw', function (flag,flagCode,flagType) {
            var symbol = trim(flagCode.toLowerCase());
            if(flag == "yes"){
                if(flagType == "<%=FundConsts.STOCKTYPE_DIGITALCOIN %>"){
                    return '<button type="button" onclick="jumpUrl(\'${ctx}/fund/withdraw\')" class="btn btn-success btn-xs" style="width:70px;"><%--提币--%><fmt:message key="fund.accountAsset.raise" /></button>&nbsp;&nbsp;';
                }else if(flagType == "<%=FundConsts.STOCKTYPE_CASHCOIN %>"){
                    return '<button type="button" onclick="jumpUrl(\'${ctx}/fund/withdrawCash?symbol='+symbol+'\')" class="btn btn-success btn-xs" style="width:70px;"><%--提币--%><fmt:message key="fund.accountAsset.raise" /></button>&nbsp;&nbsp;';
                }else if(flagType == "<%=FundConsts.STOCKTYPE_ERC20_TOKEN %>"){
                    return '<button type="button" onclick="jumpUrl(\'${ctx}/fund/withdrawERC20?symbol='+symbol+'\')" class="btn btn-success btn-xs" style="width:70px;"><%--提币--%><fmt:message key="fund.accountAsset.raise" /></button>&nbsp;&nbsp;';
                }
            }
            else {
                return '<button type="button" class="btn btn-success btn-xs" disabled style="width:70px;"><%--暂停--%><fmt:message key="stop" /></button>&nbsp;&nbsp;';
            }
        });
        template.helper('$formatFlagConversion', function (flag,id,code) {
            if(flag == "yes"){
                return '<button type="button" data-toggle="modal"  onclick="chgOption(\'xianshibizhong\',\''+id+'\',\''+code+'\')" data-target="#conversionModal" class="btn btn-success btn-xs" style="width:70px;"><%--账户互转--%><fmt:message key="sidebar.conversion" /></button>';
            }
        });
        template.helper('$formatFlagWealth', function (flag,id,code) {
            if(flag == "yes")
            {
                return '<button type="button" data-toggle="modal" onclick="chgOption(\'xianshibizhong2\',\''+id+'\',\''+code+'\')" data-target="#wealthModal" class="btn btn-success btn-xs" style="width:70px;"><%--账户互转--%><fmt:message key="sidebar.conversion" /></button>';
            }
        });
        template.helper('$formatFlagTrans', function (flag) {
            if(getTradeCount(flag) >= 1){
                return '<button type="button" onclick="jumpTradeUrl(\''+flag+'\')" class="btn btn-success btn-xs" style="width:70px;"><%--交易--%><fmt:message key="trade" /></button>';
            }else {
                return '<button type="button" disabled class="btn btn-success btn-xs" style="width:70px;"><%--暂停--%><fmt:message key="stop" /></button>';
            }
        });
        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });
        template.helper('$formatFlag', function(flag) {
            return  getDictValueByCode(flag);
        });
        template.helper('$formatFlag2', function(flag) {
            if(flag == null || flag == ''){
                return '0.0000';
            }else{
                return flag.toFixed(4);
            }
        });
        renderPageWallet = new Pagination({
            url: "${ctx}/fund/accountAsset/walletAssetData",
            form: $("#walletAssetForm"),
            method: "post",
            handleData: function (json) {
                var html = Template.render("list_tpl_wallet", json);
                $("#list_emement_wallet").html(html);
            }
        });
        renderPageSpot = new Pagination({
            url: "${ctx}/fund/accountAsset/spotAssetData",
            form: $("#spotAssetForm"),
            method: "post",
            handleData: function (json) {
                if(json.rows.length)
                {
                    for(var i=0;i<json.rows.length;i++)
                    {
                        json.rows[i].index = i;
                    }
                }
                var html = Template.render("list_tpl_spot", json);
                $("#list_emement_spot").html(html);
            }
        });
        <%--renderPageWealth = new Pagination({
            url: "${ctx}/fund/accountAsset/wealthAssetData",
            form: $("#wealthAssetForm"),
            method: "post",
            handleData: function (json) {
                var html = Template.render("list_tpl_wealth", json);
                $("#list_emement_wealth").html(html);
            }
        });
        renderPageContract = new Pagination({
            url: "${ctx}/fund/accountAsset/contractAssetData",
            form: $("#contractAssetForm"),
            method: "post",
            handleData: function (json) {
                if(json.rows.length>0)
                {
                    for(var j=0;j<json.rows.length;j++)
                    {
                        json.rows[j].index = j;
                    }
                }
                var html = Template.render("list_tpl_contract", json);
                $("#list_emement_contract").html(html);
            }
        });
        renderPageConversion = new Pagination({
            url : "${ctx}/fund/conversion/conversionList",
            elem : "#paginationConversion",
            form : $("#conversionRecordForm"),
            rows : 10,
            method : "post",
            handleData : function(json) {
                var html = Template.render("list_tpl_conversion", json);
                $("#list_emement_conversion").html(html);
            }
        }); --%>

        validator = new Validator();
        $("#conversionSubmit").on("click",function () {
            validator.destroy();
            <%--查询转成账户的余额--%>
            enableAmount();
            validator = new Validator({
                element: '#conversionForm',
                autoSubmit: false,  <%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url : '${ctx}/fund/conversion/conversion',
                            type : 'post',
                            cache:false,
                            async:false,  <%--false=同步调用，锁定其它JS操作--%>
                            data : $("#conversionForm").serialize(),
                            success : function(data,textStatus, jqXHR) {
                                data = JSON.parse(data);
                                data.message=data.message.replace('{0}', data.object);
                                if (data.code == 200) {
                                    renderPageWallet.render(true);
                                    renderPageSpot.render(true);
                                    <%--renderPageContract.render(true);--%>
                                    <%--renderPageConversion.render(true);--%>
                                    remind(remindType.success,data.message,300);
                                    $('#conversionModal').modal('toggle');
                                    $("#conversionForm input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                }
                                else {
                                    remind(remindType.error,data.message,1000);
                                }
                                enableAmount();
                                $('#conversionForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                setCsrfToken("conversionForm");
                            },
                            error : function(XMLHttpRequest, textStatus, errorThrown) {
                                console.log(textStatus);
                            }
                        });
                    }
                }
            }).addItem({
                element: '#conversionForm [name=stockinfoId]',
                required : true,
                rule:'maxlength{max:32}'
            }).addItem({
                element: '#conversionForm [name=stockinfoIdEx]',
                required : true,
                rule:'maxlength{max:32}'
            }).addItem({
                element: '#conversionForm [name=amount]',
                required : true,
                rule:'number min{min:0.0001} max{max:99999999} numberOfDigits{maxLength:4}'
            });
            $("#conversionForm").submit();
        });

        validator = new Validator();
        $("#wealthSubmit").on("click",function () {
            validator.destroy();
            <%--查询转成账户的余额--%>
            enableAmountSpot();
            validator = new Validator({
                element: '#wealthForm',
                autoSubmit: false,  <%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url : '${ctx}/fund/conversion/wealth',
                            type : 'post',
                            cache:false,
                            async:false, <%--false=同步调用，锁定其它JS操作--%>
                            data : $("#wealthForm").serialize(),
                            success : function(data,textStatus, jqXHR) {
                                data = JSON.parse(data);
                                data.message=data.message.replace('{0}', data.object);
                                if (data.code == 200) {
                                    renderPageWealth.render(true);
                                    renderPageSpot.render(true);
                                    <%--renderPageConversion.render(true);--%>
                                    remind(remindType.success,data.message,300);
                                    $('#wealthModal').modal('toggle');
                                    $("#wealthForm input").not(':button, :submit, :reset, :hidden').each(function(){
                                        $(this).val('');
                                    });
                                }
                                else {
                                    remind(remindType.error,data.message,1000);
                                }
                                enableAmountSpot();
                                $('#wealthForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                setCsrfToken("wealthForm");
                            },
                            error : function(XMLHttpRequest, textStatus, errorThrown) {
                                console.log(textStatus);
                            }
                        });
                    }
                }
            }).addItem({
                element: '#wealthForm [name=stockinfoId]',
                required : true,
                rule:'maxlength{max:32}'
            }).addItem({
                element: '#wealthForm [name=stockinfoIdEx]',
                required : true,
                rule:'maxlength{max:32}'
            }).addItem({
                element: '#wealthForm [name=amount]',
                required : true,
                rule:'number min{min:0.0001} max{max:99999999} numberOfDigits{maxLength:4}'
            });
            $("#wealthForm").submit();
        });

    });
    function conversionDialog(currencyTypeId,container,searchType,isenable ){
        $.ajax({
            url : '${ctx}/fund/conversion/conversionDialog',
            type : 'post',
            async:false,
            data : {"currencyTypeId": currencyTypeId},
            success : function(data) {
                var isWallet =  ($("#businessFlag").val() == 'wallet2Contract')
                data = JSON.parse(data);
                if (data.code == 200) {
                    for(var i=0;i<data.object.length;i++)
                    {
                        if(searchType == 'all'){
                            if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_CONTRACTSPOT%>"){
                                $("#"+container).append("<option value='"+data.object[i].id+"'><%--现货合约--%><fmt:message key="spot.C2C.spot" /></option>");
                            }
                            else if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_LEVERAGEDSPOT%>"){
                                $("#"+container).append("<option value='"+data.object[i].id+"'><%--交易账户--%><fmt:message key="spot.C2C.contractAccount" /></option>");
                            }else if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_PURESPOT%>"){
                                <%--纯现货和钱包不显示--%>
                            }else {
                                $("#"+container).append("<option selected id='btcAccount' value='"+data.object[i].id+"'><%--钱包账户--%><fmt:message key="spot.C2C.walletAccount" /></option>");
                            }
                        }
                        else
                        {
                            if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_CONTRACTSPOT%>"){
                                if(isWallet)
                                {
                                    $("#"+container).append("<option selected value='"+data.object[i].id+"'><%--现货合约--%><fmt:message key="spot.C2C.spot" /></option>");
                                }
                            }
                            else if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_LEVERAGEDSPOT%>"){
                                if(isWallet)
                                {
                                    $("#" + container).append("<option selected value='" + data.object[i].id + "'><%--交易账户--%><fmt:message key="spot.C2C.contractAccount" /></option>");
                                }
                            }else if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_PURESPOT%>"){
                                <%--纯现货和钱包不显示--%>
                            }else {
                                if(!isWallet)
                                {
                                    $("#"+container).append("<option  id='btcAccount' value='"+data.object[i].id+"'><%--钱包账户--%><fmt:message key="spot.C2C.walletAccount" /></option>");
                                }
                              }
                        }
                        <%--
                         if(searchType == 'all'){
                            if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_CONTRACTSPOT%>"){
                                $("#"+container).append("<option value='"+data.object[i].id+"'>现货合约["+data.object[i].stockName+"]</option>");
                            }
                            else if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_LEVERAGEDSPOT%>"){
                                $("#"+container).append("<option value='"+data.object[i].id+"'>交易账户["+data.object[i].stockName+"]</option>");
                            }else {
                                $("#"+container).append("<option selected id='btcAccount' value='"+data.object[i].id+"'>钱包账户["+data.object[i].stockName+"]</option>");
                            }
                        }
                         else
                         {
                             if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_CONTRACTSPOT%>"){
                                if(isWallet)
                                {
                                    $("#"+container).append("<option selected value='"+data.object[i].id+"'>现货合约["+data.object[i].stockName+"]</option>");
                                }
                            }
                            else if(data.object[i].stockType == "<%=FundConsts.STOCKTYPE_LEVERAGEDSPOT%>"){
                                if(isWallet)
                                {
                                    $("#" + container).append("<option selected value='" + data.object[i].id + "'>交易账户[" + data.object[i].stockName + "]</option>");
                                }
                            }else {
                                if(!isWallet)
                                {
                                    $("#"+container).append("<option  id='btcAccount' value='"+data.object[i].id+"'>钱包账户["+data.object[i].stockName+"]</option>");
                                }
                              }
                        }
                         --%>
                    }
                    if(isenable)
                    {
                        enableAmount();
                    }
                }
            }
        });
    }

    function chgOption(domId,id,code)
    {
        $("#"+domId).empty();
        $("#"+domId).append("<option id='"+id+"'>"+code+"</option>");
    }
    function enableAmount(){
        $.ajax({
            url : '${ctx}/risk/enableAmount',
            type : 'post',
            data : $("#conversionForm").serialize(),
            success : function(data) {
                data = JSON.parse(data);
                if (data.code == 200) {
                    if(data.object.enableAmount!=null && data.object.enableAmount!='')
                    {
                        $("#maxAmount").text(data.object.enableAmount.toFixed(4));
                        $("#maxAmountTxt").hide();
                    }
                    else {
                        $("#maxAmount").text(0);
                        $("#maxAmountTxt").show();
                    }
                }
            }
        });
    }
    function enableAmountSpot(){
        $.ajax({
            url : '${ctx}/risk/enableAmountSpot',
            type : 'post',
            data : $("#wealthForm").serialize(),
            success : function(data) {
                data = JSON.parse(data);
                if (data.code == 200) {
                    if(data.object.enableAmount!=null && data.object.enableAmount!='')
                    {
                        $("#maxAmountSpot").text(data.object.enableAmount.toFixed(4));
                    }
                    else {
                        $("#maxAmountSpot").text(0);
                    }
                }
            }
        });
    }

    $(function() {
        $("#businessFlag").val('wallet2Contract');<%--默认钱包账户转合约账户--%>
        conversionDialog(<%=FundConsts.WALLET_BTC_TYPE %>, 'stockinfoId2', 'all',false);
        conversionDialog(<%=FundConsts.WALLET_BTC_TYPE %>, 'stockinfoIdEx', '',true);
        $("#stockinfoId2").change(function () {
            if ($(this).val() == $("#btcAccount").val()) {
                $("#businessFlag").val('wallet2Contract');<%--钱包账户转合约账户--%>
                $("#stockinfoIdEx").empty();
                conversionDialog(<%=FundConsts.WALLET_BTC_TYPE %>, 'stockinfoIdEx', '',true);
            } else {
                $("#businessFlag").val('contract2Wallet');<%--合约账户转钱包账户--%>
                $("#stockinfoIdEx").empty();
                conversionDialog(<%=FundConsts.WALLET_BTC_TYPE %>, 'stockinfoIdEx', '',true);
            }
        });
        $("#stockinfoIdEx").change(function () {
            enableAmount();
        });

        $("#businessFlagSpot").val('spot2Wealth');<%--默认钱包账户转合约账户--%>
        $("#stockinfoId2Spot").append("<option   value='<%=FundConsts.WALLET_USD_TYPE%>'><%--出借账户--%><fmt:message key="spot.C2C.lendAccount" /></option>");
        $("#stockinfoId2Spot").append("<option id='btcAccountSpot' selected  value='<%=FundConsts.WALLET_BTC2USD_TYPE%>'><%--交易账户--%><fmt:message key="spot.C2C.contractAccount" /></option>");
        $("#stockinfoIdExSpot").append("<option selected value='<%=FundConsts.WALLET_USD_TYPE%>'><%--出借账户--%><fmt:message key="spot.C2C.lendAccount" /></option>");
        $("#stockinfoId2Spot").change(function () {
            if ($(this).val() == $("#btcAccountSpot").val()) {
                $("#businessFlagSpot").val('spot2Wealth');<%--钱包账户转合约账户--%>
                $("#stockinfoIdExSpot").empty();
                $("#stockinfoIdExSpot").append("<option selected value='<%=FundConsts.WALLET_USD_TYPE%>'><%--出借账户--%><fmt:message key="spot.C2C.lendAccount" /></option>");
                enableAmountSpot();
            } else {
                $("#businessFlagSpot").val('wealth2Spot');<%--合约账户转钱包账户--%>
                $("#stockinfoIdExSpot").empty();
                $("#stockinfoIdExSpot").append("<option selected value='<%=FundConsts.WALLET_BTC2USD_TYPE%>'><%--交易账户--%><fmt:message key="spot.C2C.contractAccount" /></option>");
                enableAmountSpot();
            }
        });
        $("#stockinfoIdExSpot").change(function () {
            enableAmountSpot();
        });
        enableAmountSpot();
    });

    function jumpTradeUrl(stockinfoId) {
        var stockinfolist ='${stockinfos}';
        var list = eval('(' + stockinfolist + ')');
        for(var i=0;i<list.length;i++) {
            if(list[i].stockType == '<%=FundConsts.STOCKTYPE_LEVERAGEDSPOT %>' && list[i].canTrade == 'yes'){
                if(list[i].tradeStockinfoId == stockinfoId || list[i].capitalStockinfoId == stockinfoId) {
                    jumpUrl("${ctx}/spot/leveragedSpotTrade?exchangePair="+list[i].remark);
                }
            }
            else if(list[i].stockType == '<%=FundConsts.STOCKTYPE_PURESPOT %>' && list[i].canTrade == 'yes'){
                if(list[i].tradeStockinfoId == stockinfoId || list[i].capitalStockinfoId == stockinfoId) {
                    jumpUrl("${ctx}/spot/pureSpotTrade?exchangePair="+list[i].remark);
                }
            }
        }
        console.log(stockinfoId);
    }

    function getTradeCount(stockinfoId) {
        var cnt = 0;
        var stockinfolist ='${stockinfos}';
        var list = eval('(' + stockinfolist + ')');
        for(var i=0;i<list.length;i++) {
            if(list[i].tradeStockinfoId == stockinfoId || list[i].capitalStockinfoId == stockinfoId) {
                if (list[i].canTrade == 'yes') {
                    cnt++;
                }
            }
        }
        return cnt;
    }

</script>
</body>
</html>
