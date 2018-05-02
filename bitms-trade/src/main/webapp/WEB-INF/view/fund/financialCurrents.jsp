<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp"%>
<html>
<body>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp"%>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <div class="col-sm-12 column">
            <%--开始代码位置--%>
            <ul class="nav nav-tabs bitms-tabs" style="background-color:transparent;margin-right:0px;margin-top:-15px;">
                <li onclick="jumpUrl('${ctx}/spot/historyEntrust')">
                    <a data-toggle="tab"><%--历史委托--%><fmt:message key="spot.query.historyEntrust" /></a>
                </li>
                <%--<li onclick="jumpUrl('${ctx}/fund/accountWealthAsset')">
                    <a href="#" data-toggle="tab">&lt;%&ndash;出借记录&ndash;%&gt;<fmt:message key="fund.tab.weathflow" /></a>
                </li>--%>
                <li onclick="jumpUrl('${ctx}/fund/accountDebitAsset')">
                    <a href="#" data-toggle="tab"><%--借贷流水--%><fmt:message key="fund.tab.debitflow" /></a>
                </li>
                <li class="active">
                    <a href="#" data-toggle="tab"><%--财务流水--%><fmt:message key="fund.tab.financeflow" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/currents')">
                    <a href="#" data-toggle="tab"><%--交易流水--%><fmt:message key="fund.tab.Dealflow" /></a>
                </li>
                <%--<li onclick="jumpUrl('${ctx}/fund/candyRecord')">
                    <a href="#" data-toggle="tab">&lt;%&ndash;糖果记录&ndash;%&gt;<fmt:message key="fund.tab.candyRecords" /></a>
                </li>--%>
            </ul>
            <form class="clearfix" data-widget="validator" name="currentsForm" id="currentsForm" action="${ctx}/fund/currents/currentsExport">
                <div class="panel clearfix">
                    <%--<div class="col-sm-12 px0">
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span>&lt;%&ndash;资产分类&ndash;%&gt;<fmt:message key="fund.currents.assetClassify" /></span>
                        </div>
                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <select id="stockinfoId" name="stockinfoId" class="form-control">
                            </select>
                        </div>
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span>&lt;%&ndash;业务类别&ndash;%&gt;<fmt:message key="fund.conversion.businessCategory" /></span>
                        </div>
                        <div id="businessFlagDiv" class="form-group col-lg-2 col-sm-3  col-xs-12">
                        </div>
                    </div>--%>
                    <div class="col-sm-12 px0">
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--资产分类--%><fmt:message key="fund.currents.assetClassify" /></span>
                        </div>
                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <select id="stockinfoId" name="stockinfoId" class="form-control">
                            </select>
                        </div>
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--起始时间--%><fmt:message key="fund.currents.startingTime" /></span>
                        </div>
                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <input name="timeStart" id="timeStart" class="form-control timeOpen" />
                        </div>
                        <div class="form-group col-sm-1 col-xs-12 px0 text-center">
                            <span><%--结束时间--%><fmt:message key="fund.currents.terminalTime" /></span>
                        </div>
                        <div class="form-group col-lg-2 col-sm-3 col-xs-12">
                            <input name="timeEnd" id="timeEnd" class="form-control" />
                        </div>
                        <div class="form-group col-sm-3 col-xs-12">

                            <input name="isHis" id="isHis" type="hidden" value="no" />
                            <button type="button" class="btn btn-primary" id="searchBtn0" title="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />"><%--当前查询--%><fmt:message key="fund.curent.currentQuery" /></button>
                            <button type="button" class="btn btn-primary" title="2017-12-21~ <fmt:formatDate value="${nowDate}" pattern="yyyy-MM-dd" />" id="searchBtn"><%--历史查询--%><fmt:message key="fund.curent.historyQuery" /></button>

                        </div>
                    </div>
                </div>
                <div class="panel clearfix">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th><%--时间--%><fmt:message key="time" /></th>
                                <%--<th>&lt;%&ndash;交易对&ndash;%&gt;<fmt:message key="spot.query.transPair" /></th>--%>
                                <th><%--账户类型--%><fmt:message key="fund.currents.accountType" /></th>
                                <th><%--资产分类--%><fmt:message key="fund.currents.assetClassify" /></th>
                                <th><%--业务类别--%><fmt:message key="fund.conversion.businessCategory" /></th>
                                <th><%--原余额--%><fmt:message key="fund.conversion.orgAmt" /></th>
                                <th><%--发生数量--%><fmt:message key="fund.currents.occurrenceQuantity" /></th>
                                <th><%--手续费--%><fmt:message key="fee" /></th>
                                <th><%--最新余额--%><fmt:message key="fund.currents.latesBalance" /></th>
                               <%-- <th>&lt;%&ndash;原冻结余额&ndash;%&gt;<fmt:message key="fund.currents.forzenOrgAmt" /></th>
                                <th>&lt;%&ndash;冻结解冻发生数量&ndash;%&gt;<fmt:message key="fund.currents.occurForzenAmt" /></th>
                                <th>&lt;%&ndash;最新冻结余额&ndash;%&gt;<fmt:message key="fund.currents.forzenLastAmt" /></th>--%>
                            </tr>
                            </thead>
                            <tbody id="list_emement">
                            </tbody>
                        </table>
                        <div class="mt10 m010">
                            <%-- 通用分页 --%>
                            <div id="curr_pagination" class="paginationContainer"></div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script id="list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td>{{$formatDate $value.currentDate}}</td>
        <%--<td>{{$value.relatedStockName}}</td>--%>
        <td>{{$formatFlag2 $value.accountAssetType}}</td>
        <td>{{$value.stockCode}}</td>
        <td>{{$formatFlag $value.businessFlag}}</td>
        <td>{{$fix8Date $value.orgAmt}}</td>
        <td>{{$plusMinusFlag3 $value.occurAmt,$value.occurDirect}}</td>
        <td>{{$plusMinusFlag2 $value.netFee,$value.occurDirect}}</td>
        <td>{{$fix8Date $value.lastAmt}}</td>
        <%--<td>{{$fix8Date $value.forzenOrgAmt}}</td>
        <td>{{$plusMinusFlag $value.occurForzenAmt,$value.occurDirect}}</td>
        <td>{{$fix8Date $value.forzenLastAmt}}</td>--%>
        <!--<td>{{$value.remark}}</td>-->
    </tr>
    {{/each}}
</script>
<script>
    var pageNo=1;
    seajs.use([ 'pagination', 'template', 'moment','my97DatePicker' ], function(Pagination, Template, moment) {
        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });
        template.helper('$moneyFlag', function(direct) {
            return ""+getDictValueByCode(direct)+"";
        });
        template.helper('$fix8Date', function (flag) {
            if(flag == '' || flag == null){
                return '0';
            }
            return parseFloat(flag).toFixed(8);
        });
        template.helper('$fix8xDate', function (flag) {
            if(flag == '' || flag == null){
                return '0';
            }
            return "<font color='#DC605A'>-"+parseFloat(flag).toFixed(8)+"</font>";

        });
        template.helper('$plusMinusFlag', function(amt,direct) {
            var tag="+";
            var clr='#DC605A';
            if (direct == "increase") {
                if(amt>0){tag="+";clr='#5D7B3C';}else{clr='#FFFFFF';tag="";}
            }
            else if (direct == "decrease") {
                if(amt>0){tag="-";clr='#DC605A';}else{clr='#FFFFFF';tag="";}
            }

            else if (direct == "unfrozen") {
                if(amt>0){clr='#00FFFF';  tag="🔑";}else{clr='#FFFFFF';tag="";}
            }
            else{
                if (direct == "frozen") {
                    if(amt>0){clr='yellow'; tag="🔒";}else{clr='#FFFFFF';tag="";}
                }else if (direct == "unfrozenDecrease") {
                    if(amt>0){tag="🔑";clr='#00FFFF';}else{tag="🔑";clr='#FFFFFF';tag="";}
                }else{
                    clr='';
                    tag="";
                }
            }
            return "<font color='"+clr+"' >"+tag+''+parseFloat(amt).toFixed(8)+"</font>";
        });
        template.helper('$plusMinusFlag3', function(amt,direct) {
            var tag="+";
            var clr='#DC605A';
            if (direct=="increase") {
                if(amt>0){tag="+";clr='#5D7B3C';}else{clr='#FFFFFF';tag="";}
            }
            else if (direct=="decrease") {
                if(amt>0){tag="-";clr='#DC605A';}else{clr='#FFFFFF';tag="";}
            }

            else if (direct=="unfrozen") {
                if(amt>0){clr='#00FFFF';  tag="";}else{clr='#FFFFFF';tag="";}
            }
            else{
                if (direct == "frozen") {
                    if(amt>0){clr='yellow'; tag="";}else{clr='#FFFFFF';tag="";}
                }else if (direct == "unfrozenDecrease") {
                    if(amt>0){tag="-";clr='#DC605A';}else{tag="";clr='#FFFFFF';tag="";}
                }else{
                    clr='';
                    tag="";
                }
            }
            return "<font color='"+clr+"' >"+tag+''+parseFloat(amt).toFixed(8)+"</font>";
        });
        template.helper('$plusMinusFlag2', function(amt,direct) {
            if(amt == '' || amt == null){
                return '0';
            }
            var tag="";
            var clr='#DC605A';
            if (direct == 'decrease') {
                clr='#DC605A';
                tag="-";
            }else{
                clr='';
                tag="+";
            }
            return "<font color='"+clr+"' >"+tag+''+parseFloat(amt).toFixed(8)+"</font>";
        });


        template.helper('$formatFlag', function(flag) {
            return getDictValueByCode(flag);
        });
        template.helper('$formatFlag2', function(flag) {
            return getDictValueByCode(flag);
        });

        var timestamp=new Date().getTime();
        $('#timeStart').val(moment(timestamp).format('YYYY-MM-DD'));
        $('#timeEnd').val(moment(timestamp).format('YYYY-MM-DD'));
        $('#timeStart').on('click', function(){
            WdatePicker(
                {
                    minDate:"2017-01-01",
                    maxDate:""+moment(timestamp).format("YYYY-MM-DD"),
                    lang:'en'
                }
            );
        });
        $('#timeEnd').on('click', function(){
            WdatePicker(
                {
                    minDate:"2017-01-01",
                    maxDate:""+moment(timestamp).format("YYYY-MM-DD"),
                    lang:'en'
                }
            );
        });

        renderPage = new Pagination({
            beforeAjax:function(){
                $('#searchBtn').attr("disabled", true);
                $('.reg-pop').show();
            },
            url : "${ctx}/fund/currents/financialCurrentsList",
            elem : "#curr_pagination",
            form : $("#currentsForm"),
            rows : 10,
            method : "get",
            handleData : function(json) {
                var html = Template.render("list_tpl", json);
                $("#list_emement").html(html);
                $('#searchBtn').attr("disabled", false);
                $('.reg-pop').hide();
            }
        });
    });
    $("#searchBtn").click(function() {
        $("#isHis").val('yes');
        renderPage.render(true);
    });

    $("#searchBtn0").on('click',function() {
        var timestamp=new Date().getTime();
        $('#timeStart').val('');
        $('#timeEnd').val('');
        $("#isHis").val('no');
        renderPage.render(true);
    });

    $(function(){
        $.ajax({
            url : '${ctx}/common/stockinfo/allCoin',
            type : "get",
            cache : false,
            async : false,// false=同步调用，锁定其它JS操作
            dataType : "json",
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                alert('服务器连接失败，请稍候重试！');
            },
            success : function(data, textStatus) {
                for (var i = 0; i < data.length; i++) {
                    $("#stockinfoId").append(
                        "<option value='"+data[i].id+"'>"
                        + data[i].stockCode + "</option>");
                }
                $("#stockinfoId").val('<%=FundConsts.WALLET_BTC_TYPE%>');
            }
        });
        selectType();
    });

    function selectType()
    {
        var array=["wallet2Contract","contract2Wallet","walletRecharge","walletWithdraw","walletWithdrawCancel","walletWithdrawReject","platformAdjustAssetAdd","platformAdjustAssetSub","platformAdjustForzenAssetAdd","platformAdjustForzenAssetSub"];
        $("#businessFlagDiv").html(dictDropDownOptionsSelect('businessFlag', 'businessFlag', '','', 'form-control',array));
        $("#businessFlagDiv select").find("option:first").before('<option selected value="">-<fmt:message key="fund.currents.pleaseSelect" />-</option>');
    }
</script>
</body>
</html>