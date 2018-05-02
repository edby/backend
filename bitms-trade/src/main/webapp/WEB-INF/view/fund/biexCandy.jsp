<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<body>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp" %>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <div class="col-sm-12 column">
            <%--开始代码位置--%>
            <div class="panel clearfix">
                <div class="col-sm-12">
                    <p class="fs25 text-center" style="font-weight: bold"><%--BITMS 糖果生态--%><fmt:message key="fund.candy.title" /></p>
                    <p class="text-center"><%--BITMS糖果是为建设良好的BITMS生态而推出，供应总量为恒定50亿枚，全部用于赠送用户。--%><fmt:message key="fund.candy.subTitle" /></p>
                    <p class="p10 bitms-bg7 text-center" style="width: 80%;margin: 0 auto;"><%--BITMS奖励获得途径：通过交易BTC/USD交易对获得，当前每产生1USD交易手续费将会奖励100BITMS，奖励的BITMS将统一在每日新加坡时间0点发放到您的账户。--%><fmt:message key="fund.candy.reward" /></p>
                    <div id="pie" style="height:500px;"></div>
                    <div class="text-center" style="word-wrap:break-word;">
                        <span><%--查询地址--%><fmt:message key="fund.candy.address" />：</span>
                        <a target="_blank" href="https://etherscan.io/token/0x806336c912762274bfc4d0f78b1be2c0119e86f0">https://etherscan.io/token/0x806336c912762274bfc4d0f78b1be2c0119e86f0</a>
                    </div>
                </div>
            </div>
            <%--<div class="panel clearfix">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>&lt;%&ndash;时间&ndash;%&gt;<fmt:message key="fund.candy.date" /></th>
                            <th>&lt;%&ndash;已赠送&ndash;%&gt;<fmt:message key="fund.candy.presented" /></th>
                            <th>&lt;%&ndash;待赠送&ndash;%&gt;<fmt:message key="fund.candy.pending" /></th>
                            <th>&lt;%&ndash;已销毁&ndash;%&gt;<fmt:message key="fund.candy.destoryed" /></th>
                            <th>&lt;%&ndash;供应总量&ndash;%&gt;<fmt:message key="fund.candy.total" /></th>
                        </tr>
                        </thead>
                        <tbody id="list_emement">
                            <td><jsp:useBean id="now" class="java.util.Date" scope="page"/><fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                            <td>${presentAmt}</td>
                            <td>${lastAmt}</td>
                            <td>${burnAmt}</td>
                            <td><fmt:formatNumber type="number" value="${totalSupply}" groupingUsed="false" maxFractionDigits="0"/> </td>
                        </tbody>
                    </table>
                    <div class="mt10 m010">
                        &lt;%&ndash; 通用分页 &ndash;%&gt;
                        <div id="curr_pagination" class="paginationContainer"></div>
                    </div>
                </div>
            </div>--%>
        </div>
    </div>
</div>
</body>
<script src="${ctx}/scripts/gallery/echarts/echarts.js"></script>
<script>

    var dom = document.getElementById("pie");
    var myChart = echarts.init(dom);
    option = null;

    option = {
        tooltip : {
            trigger: 'item',
            formatter: "{b} : {c} ({d}%)"
        },
        legend: {
            bottom: 10,
            left: 'center',
            data: ['<%--已赠送--%><fmt:message key="fund.candy.presented" />','<%--待赠送--%><fmt:message key="fund.candy.pending" />','<%--已销毁--%><fmt:message key="fund.candy.destoryed" />'],
            textStyle: {
                color:['#a1ddb0', '#4fada7','#57797e']
            }
        },
        color:['#a1ddb0', '#4fada7','#57797e'],
        series : [
            {
                type: 'pie',
                radius : '65%',
                center: ['50%', '45%'],
                top: '100%',
                selectedMode: 'single',
                label: {
                    normal: {
                        formatter: '{b|{b}：}{c}  {per|{d}%}',
                        backgroundColor: 'rgba(0,23,11,0.3)',
                        borderColor: '#22333f',
                        borderWidth: 0.5,
                        borderRadius: 2,
                        rich: {
                            a: {
                                color: '#999',
                                lineHeight: 25,
                                align: 'center',
                                padding: 10
                            },
                            hr: {
                                borderColor: '#aaa',
                                width: '100%',
                                borderWidth: 0.5,
                                height: 0
                            },
                            b: {
                                fontSize: 14,
                                lineHeight: 25,
                                padding: 10
                            },
                            per: {
                                color: '#eee',
                                backgroundColor: '#334455',
                                padding: [2, 4],
                                borderRadius: 2
                            },
                            fontSize: 14
                        }
                    }
                },
                data:[
                    {value:${presentAmt}, name: '<%--已赠送--%><fmt:message key="fund.candy.presented" />'},
                    {value:${lastAmt}, name: '<%--待赠送--%><fmt:message key="fund.candy.pending" />'},
                    {value:${burnAmt}, name: '<%--已销毁--%><fmt:message key="fund.candy.destoryed" />'}
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }

    window.onresize = function () {
        myChart.resize();
    };

</script>
</html>
