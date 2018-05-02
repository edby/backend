<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
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
             <div class="panel pb10 pt10">
                 <div class="fs16 fb"><a href="${ctx}/common/notice"><%--公告--%><fmt:message key='topNav.notice' /> > </a> <fmt:message key='notice.info' /></div>
             </div>
             <div class="panel pt10 pb0">
                 <div class="col-sm-12 bitms-bb pb5 text-center fs18">${notice.title}<span class="pull-right fs14" id="NoticeCreateDate"></span></div>
                 <div class="row px10">
                     <div class="col-sm-12 py10 text-center notice-content">
                        <p></p>
                     </div>
                 </div>
             </div>
         </div>
    </div>
</div>
<script>
    seajs.use( ['moment'], function (moment) {
        $('.pageLoader').hide();
        var timeStr = ${notice.createDate};
        var time= moment(timeStr*1).format("YYYY-MM-DD HH:mm:ss");
        $('#NoticeCreateDate').text(time);
        var contentStr ='${notice.content}'.replace(/<[^>]+>/g,"").replace(/&nbsp;/ig,'').replace(/&gt;/ig,'').replace(/&lt;/ig,'').replace('br/','').replace('/p','');
        $('.notice-content p').text(contentStr);
    })
</script>
</body>
</html>