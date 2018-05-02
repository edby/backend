<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- [jQuery] --%>
<style media="print">
    @page {
        size: auto;  /* auto is the initial value */
        margin: 0mm 15mm 0mm 15mm; /* this affects the margin in the printer settings */
    }
    table tr td{
        font-size: 11px;
    }
    table,table td,table th{border:1px solid #000000;border-collapse:collapse;}
    .tdnoborder td{border:0px solid #000000;border-collapse:collapse;}
    .tdnoborder tr td{ border-bottom:1px solid; }
</style>
<script type="text/javascript" src="${ctx}/static/easyui/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/static/js/datetimeformat.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/static/js/publicparams.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/static/js/common.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/static/js/dictionary.js" charset="utf-8"></script>
<jsp:useBean id="Timestamp" class="java.util.Date"/>
<script type="text/javascript">
    $(document).ready(function(){
        $(".stateClass").each(function(index,obj){
            $(obj).html(getDictValueByCode($(obj).text()));
        });
        if(getExplorer() == "IE"){
            pagesetup_null();
        }
        window.print();
        function pagesetup_null(){
            var hkey_root,hkey_path,hkey_key;
            hkey_root="HKEY_CURRENT_USER";
            hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
            try{
                var RegWsh = new ActiveXObject("WScript.Shell");
                hkey_key="header";
                RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
                hkey_key="footer";
                RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
            }catch(e){}
        }

        function getExplorer() {
            var explorer = window.navigator.userAgent ;
            //ie
            if (explorer.indexOf("MSIE") >= 0) {
                return "IE";
            }
            //firefox
            else if (explorer.indexOf("Firefox") >= 0) {
                return "Firefox";
            }
            //Chrome
            else if(explorer.indexOf("Chrome") >= 0){
                return "Chrome";
            }
            //Opera
            else if(explorer.indexOf("Opera") >= 0){
                return "Opera";
            }
            //Safari
            else if(explorer.indexOf("Safari") >= 0){
                return "Safari";
            }
        }
    });
</script>
<div style="max-width:800px;margin:20px auto;" >
    <div style="overflow: hidden;width:100%;">
        <h5 style="float: left;display: inline-block;margin-top:0px;">
            <span>TRANS ID:</span>
            <span>${accountFundCurrent.id}</span>
        </h5>
        <img src="${ctx}/static/style/images/biex.svg" style="display: inline-block;float: right; " />
    </div>
    <br />
    <div style="overflow: hidden;">
        <h1 style="margin: 0px auto;width: 500px;text-align: center;margin-bottom: 10px;">Application for Withdrawal</h1>
        <span style="float: right;display: block;">
			<span style="font-weight:bold;">UID:</span>
			<span>${account.unid}</span>
		</span>
        <br>
        <span style="float: right;display: block;">
			<span style="font-weight:bold;">Email:</span>
			<span>${account.email}</span>
		</span>
        <br>
        <span style="float: right;display: block;">
			<span style="font-weight:bold;">Name:</span>
			<span>${certification.realname}&nbsp;${certification.surname}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		</span>
        <br>
        <span style="float: right;display: block;">
			<span style="font-weight:bold;">Registration Time:</span>
			<span> <c:set target="${Timestamp}" property="time" value="${account.createDate}"/>
                        <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${Timestamp}" type="both"/></span>
		</span>
        <br>
        <span style="float: right;display: block;">
			<span style="font-weight:bold;">KYC status:</span>
            <span><c:if test="${!empty certification.passportId}">yes</c:if><c:if test="${empty certification.passportId}">no</c:if></span>
		</span>
        <br>
        <span style="float: right;display: block;">
			<span style="font-weight:bold;">Total Deposit:</span>
			<span><fmt:formatNumber value="${chargeAmt}" pattern="#,##0.000000"  />&nbsp;${coin}</span>
		</span>
        <br>
        <span style="float: right;display: block;">
			<span style="font-weight:bold;">Total Withdraw:</span>
			<span><fmt:formatNumber value="${usedAmt}" pattern="#,##0.000000"  />&nbsp;${coin}</span>
		</span>
    </div>
    <div style="overflow: hidden;margin-top:20px;">
        <h3 style="margin-bottom: 5px;">HISTORY OF WITHDRAW RECORDS</h3>
        <table style="width: 100%;" border="1" cellspacing="0" class="tdnoborder">
            <tr>
                <td style="width: 30%;">Date</td>
                <td style="width: 10%;">Bank Name</td>
                <td style="width: 10%;">Card No.</td>
                <td style="width: 10%;">SWIFT/BTC</td>
                <td style="width: 15%;">Amount</td>
                <td style="width: 15%;">Status</td>
            </tr>
            <c:if test="${!empty currList}">
                <c:forEach var="item" items="${currList }"  varStatus="status" >
                    <c:choose>
                        <c:when test="${item.id != accountFundCurrent.id }">
                            <tr>
                                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.currentDate}"/></td>
                                <td>${item.withdrawBankName}</td>
                                <td>${item.withdrawCardNo}</td>
                                <td>${item.swiftBic}</td>
                                <td><fmt:formatNumber value="${item.occurAmt-item.fee}" pattern="#,##0.000000"  />${coin}</td>
                                <td class="stateClass">${item.approveStatus}</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
            <c:if test="${empty currList}">
                <tr class="test">
                    <td colspan="4" align="center">
                        no record
                    </td>
                </tr>
            </c:if>
        </table>
    </div>
    <div style="overflow: hidden;margin-top:20px;">
        <h3 style="margin-bottom: 5px;">HISTORY OF DEPOSIT RECORDS</h3>
        <table style="width: 100%;" border="1" cellspacing="0" class="tdnoborder">
            <tr>
                <td style="width: 30%;">Date</td>
                <td style="width: 40%;">transId</td>
                <td style="width: 15%;">Amount</td>
                <td style="width: 15%;">Status</td>
            </tr>
            <c:if test="${!empty chargeList}">
                <c:forEach var="item" items="${chargeList }"  varStatus="status" >
                    <tr class="test">
                        <td>
                            <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createDate}"/>
                        </td>
                        <td>${item.transId}</td>
                        <td>${item.amount}${coin}</td>
                        <td  class="stateClass">${item.status}</td>
                    </tr>
                </c:forEach>
            </c:if>
            <c:if test="${empty chargeList}">
                <tr class="test">
                    <td colspan="4" align="center">
                        no record
                    </td>
                </tr>
            </c:if>
        </table>
    </div>
    <div style="overflow: hidden;margin-top:20px;">
        <h3 style="margin-bottom: 5px;">APPLICATION FOR WITHDRAWAL</h3>
        <table style="width: 100%;" border="1" cellspacing="0">
            <tr>
                <td style="width: 30%;">Date</td>
                <td style="width: 10%;">Bank Name</td>
                <td style="width: 10%;">Card No.</td>
                <td style="width: 10%;">SWIFT/BTC</td>
                <td style="width: 15%;">Amount</td>
                <td style="width: 15%;">Status</td>
            </tr>
            <tr>
                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${accountFundCurrent.currentDate}"/></td>
                <td>${accountFundCurrent.withdrawBankName}</td>
                <td>${accountFundCurrent.withdrawCardNo}</td>
                <td>${accountFundCurrent.swiftBic}</td>
                <td><fmt:formatNumber value="${accountFundCurrent.occurAmt-accountFundCurrent.fee}" pattern="#,##0.000000"  />${coin}</td>
                <td class="stateClass">${accountFundCurrent.approveStatus}</td>
            </tr>
        </table>
    </div>
</div>