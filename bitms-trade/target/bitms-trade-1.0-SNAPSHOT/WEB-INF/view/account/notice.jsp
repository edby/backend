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
                <div class="fs16 fb"><%--公告--%><fmt:message key='topNav.notice' /> > <fmt:message key='notice.list' /></div>
            </div>
            <div class="panel pt0 pb5">
                <form data-widget="validator" name="noticeForm" id="noticeForm" method="post">
                    <input id="langType" name="langType" hidden type="text"/>
                    <div class="row px10"  id="list_element"></div>
                    <div class="mt10 m010">
                        <%-- 通用分页 --%>
                        <div id="pagination" class="paginationContainer"></div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script id="list_tpl" type="text/html">
    {{each rows }}
    <a  href="${ctx}/common/notice/getNotice?id={{$value.id}}" data="{{$value.id}}" class="col-sm-12 bitms-bb py10 noticeLine">
        <div class="col-sm-9">
            <span>{{$value.title}}</span>
        </div>
        <div class="col-sm-3 text-right fs14">{{$formatDate $value.createDate}}</div>
    </a>
    {{/each}}
</script>
<script>
    var renderPage;
    seajs.use(['pagination', 'template', 'moment', 'cookie','i18n'], function (Pagination, Template, moment, Cookie,I18n) {
        $('.pageLoader').hide();
        template.helper('$formatDate', function (millsec) {
            return moment(millsec*1).format("YYYY-MM-DD HH:mm:ss");
        });
        var langType = Cookie.get('locale');
        $("#langType").val(langType);
        renderPage = new Pagination({
            url: "${ctx}/common/notice/data",
            elem: "#pagination",
            form: $("#noticeForm"),
            rows: 10,
            method: "post",
            handleData: function (json) {
                var html = Template.render("list_tpl", json);
                $("#list_element").html(html);
            }
        });
    })
</script>
</body>
</html>