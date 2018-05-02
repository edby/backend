<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
</script>
<div id="approvalWin" class="easyui-layout" data-options="fit:true,border:false" style="">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="marginForm" method="post">
            <input type="hidden" id="ids" name="ids" value="${ids}" />
            <table class="grid">
                <tr>
                    <td colspan="6" align="center"><h1>亏损情况</h1></td>
                </tr>
                <tr>
                    <td align="right" width="200px">
                        超级用户数字货币借款数量：
                    </td>
                    <td width="200px">
                        <fmt:formatNumber value="${superBtcDebit}" pattern="### ##0.00000000"/>
                    </td>
                    <td align="right" width="200px">
                        超级用户法定货币借款数量：
                    </td>
                    <td width="200px">
                            <fmt:formatNumber value="${superUsdxDebit}" pattern="### ##0.00000000"/>
                    </td>
                    <td align="right" width="200px">
                        结算价：
                    </td>
                    <td width="200px">
                        <fmt:formatNumber value="${settlementPrice}" pattern="### ##0.00000000"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="200px">
                        超级用户数字货币资产：
                    </td>
                    <td width="200px">
                        <fmt:formatNumber value="${superBtcAsset}" pattern="### ##0.00000000"/>
                    </td>
                    <td align="right" width="200px">
                        超级用户法定货币资产：
                    </td>
                    <td width="200px">
                        <fmt:formatNumber value="${superUsdxAsset}" pattern="### ##0.00000000"/>
                    </td>
                    <td align="right" width="200px">
                        需分摊BTC数量：
                    </td>
                    <td width="200px">
                        <fmt:formatNumber value="${needBtc}" pattern="### ##0.00000000"/>
                    </td>
                </tr>

                <tr>
                    <td align="right" width="200px">
                        亏算结果：
                    </td>
                    <td width="200px">
                            ${result?"<font color='green'>不亏损</font>":"<font color='red'>亏损</font>"}
                    </td>
                    <td align="right" width="200px">
                        分摊比例%：
                    </td>
                    <td width="200px">
                        <fmt:formatNumber value="${superYingLi==0?0:needBtc/superYingLi*100}" pattern="### ##0.00"/>
                    </td>
                    <td align="right" width="200px">
                        全市场盈利数字货币数量：
                    </td>
                    <td width="200px">
                            ${superYingLi lt 0 ?'-':''}<fmt:formatNumber value="${superYingLi}" pattern="### ##0.00000000"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>