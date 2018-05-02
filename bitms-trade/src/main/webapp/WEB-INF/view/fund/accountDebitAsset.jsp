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
                <li class="active">
                    <a href="#" data-toggle="tab"><%--借贷流水--%><fmt:message key="fund.tab.debitflow" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/financialCurrents')">
                    <a href="#" data-toggle="tab"><%--财务流水--%><fmt:message key="fund.tab.financeflow" /></a>
                </li>
                <li onclick="jumpUrl('${ctx}/fund/currents')">
                    <a href="#" data-toggle="tab"><%--交易流水--%><fmt:message key="fund.tab.Dealflow" /></a>
                </li>
                <%--<li onclick="jumpUrl('${ctx}/fund/candyRecord')">
                    <a href="#" data-toggle="tab">&lt;%&ndash;糖果记录&ndash;%&gt;<fmt:message key="fund.tab.candyRecords" /></a>
                </li>--%>
            </ul>
            <form class="clearfix" data-widget="validator" name="currentsForm" id="currentsForm">
                <div class="panel clearfix">
                    <div class="col-sm-12 px0">
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
                            <button type="button" class="btn btn-primary" id="searchBtn"><%--查询--%><fmt:message key="search" /></button>
                        </div>
                    </div>
                    <div class="ml20 clearfix text-danger">
                        <small style="margin-bottom: 5px;">1、<%--溢价费按每天0时溢价为准进行调节：--%><fmt:message key='spot.C2C.premiumTxt1'/></small>
                        <small style="margin-bottom: 5px;"><%--当溢价<-3%时，空头向多头支付；当溢价[-3%~3%]时，无溢价费；当溢价>3%时，多头向空头支付；--%><fmt:message key='spot.C2C.premiumTxt2'/></small>
                        <small style="margin-bottom: 0;"><%--溢价费=USD持仓量*0.1%--%><fmt:message key='spot.C2C.premiumTxt3'/></small>
                    </div>
                    <div class="ml20 clearfix text-danger">
                        <small style="margin-bottom: 5px;">2、<%--（±）持仓价值=可用USD+冻结USD-负债USD，当持仓价值为“+”时为空头仓位，当持仓价值为“﹣”时为多头仓位。--%><fmt:message key='spot.C2C.premiumTxt4'/></small>
                    </div>
                </div>
                <div class="panel clearfix">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th><%--时间--%><fmt:message key="time" /></th>
                                <th><%--方向--%><fmt:message key='spot.C2C.positionDirection'/></th>
                                <th><%--持有仓位--%><fmt:message key='spot.C2C.usdPosition'/></th>
                                <th><%--平台价格--%><fmt:message key='spot.C2C.innerPrice'/></th>
                                <th><%--风控基价--%><fmt:message key='spot.C2C.RiskPrice'/></th>
                                <th><%--溢价率--%><fmt:message key='spot.C2C.premiumRate'/></th>
                                <th><%--溢价费率--%><fmt:message key='spot.C2C.premiumFeeRate'/></th>
                                <th><%--溢价费--%><fmt:message key='spot.C2C.premiumFee'/></th>
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
        <td>{{$formatDate $value.updateDate}}</td>
        <td>{{$formatFlag $value.positionAmt}}</td>
        <td>{{$format2Flag $value.positionAmt}}</td>
        <td>{{$fix2Date $value.paltformPrice}}</td>
        <td>{{$fix2Date $value.indexPrice}}</td>
        <td>{{$percentDate $value.premiumRate}}%</td>
        <td>{{$percentDate $value.premiumFeeRate}}%</td>
        <td>{{$format3Flag $value.premiumFee}}</td>
    </tr>
    {{/each}}
</script>
<script>
    seajs.use([ 'pagination', 'template', 'moment','my97DatePicker' ], function(Pagination, Template, moment) {
        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD 00:00");
        });
        template.helper('$formatFlag', function(flag) {
            if(flag < 0)
                return '<span style="display:inline-block;padding: 0px 2px;border-radius:2px;color:rgb(21, 25, 34);background-color:#648241;">Long</span>';
            else
                return '<span style="display:inline-block;padding: 0px 2px;border-radius:2px;color:rgb(21, 25, 34);background-color:#a94442;">Short</span>';
        });
        template.helper('$format2Flag', function (flag) {
            if(flag == '' || flag == null){
                return '0';
            }
            return '$'+parseFloat(Math.abs(flag)).toFixed(2);
        });
        template.helper('$fix2Date', function (flag) {
            if(flag == '' || flag == null){
                return '0';
            }
            return parseFloat(flag).toFixed(2);
        });
        template.helper('$format3Flag', function (flag) {
            if(flag == '' || flag == null){
                return '0';
            }
            else if(flag < 0){
                return '<span class="text-danger">'+parseFloat(flag).toFixed(4)+'</span>';
            }
            else{
                return '<span class="text-success">+'+parseFloat(flag).toFixed(4)+'</span>';
            }

        });
        template.helper('$fix8Date', function (flag) {
            if(flag == '' || flag == null){
                return '0';
            }
            return parseFloat(flag).toFixed(8);
        });
        template.helper('$percentDate', function (flag) {
            if(flag == '' || flag == null){
                return '0';
            }
            return (100*parseFloat(flag)).toFixed(2);
        });
        var timestamp=new Date().getTime();
        <%--$('#timeStart').val(moment(timestamp).format('YYYY-MM-DD'));--%>
        <%--$('#timeEnd').val(moment(timestamp).format('YYYY-MM-DD'));--%>
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
            url : "${ctx}/fund/premiumData",
            elem : "#curr_pagination",
            form : $("#currentsForm"),
            rows : 10,
            method : "post",
            handleData : function(json) {
                var html = Template.render("list_tpl", json);
                $("#list_emement").html(html);
                $('#searchBtn').attr("disabled", false);
                $('.reg-pop').hide();
            }
        });
    });

    $("#searchBtn").click(function() {
        renderPage.render(true);
    });

</script>
</body>
</html>