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
        <%--<div class="col-sm-2 column">
            <ul class="nav nav-tabs nav-stacked">
                <li>
                    <a href="${ctx}/account/setting">
                        <span class="glyphicon glyphicon-lock"></span>
                        <span>安全中心</span>
                    </a>
                </li>
                &lt;%&ndash;<li>
                    <a href="${ctx}/account/baseInfo">
                        <span class="glyphicon glyphicon-plus"></span>
                        <span>基本信息</span>
                    </a>
                </li>&ndash;%&gt;
                <li>
                    <a href="${ctx}/account/certification">
                        <span class="glyphicon glyphicon-user"></span>
                        <span>身份认证</span>
                    </a>
                </li>
                <li class="active">
                    <a href="${ctx}/common/message">
                        <span class="glyphicon glyphicon-envelope"></span>
                        <span>我的消息</span>
                    </a>
                </li>
                <li>
                    <a href="${ctx}/common/feedback">
                        <span class="glyphicon glyphicon-question-sign"></span>
                        <span>问题反馈</span>
                    </a>
                </li>
            </ul>
        </div>--%>

        <div class="col-sm-12 column">
            <%--开始代码位置--%>
            <div class="panel">
                <div class="row">
                    <div class="col-sm-12">
                        <form data-widget="validator" name="messageForm" id="messageForm" method="post">
                            <div id="list_element"></div>
                            <div class="clearfix text-center">
                                <%-- 通用分页 --%>
                                <div id="pagination" class="paginationContainer"></div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
<jsp:include page="/global/setup_ajax.jsp"></jsp:include>
<script id="list_tpl" type="text/html">
    <div class="well well-sm clearfix message-well">
        <div class="pull-left">
            <span>
                <span id="messageTotal"></span>
                <span>
                    <span><%--您有--%><fmt:message key="notice.messageTxt" /></span>
                    <span class="text-success">{{total}}</span>
                    <span><%--条消息;--%><fmt:message key="notice.total" /></span>
                </span>
            </span>
        </div>
        <div class="pull-right">
            <a class="text-success" id="deleteMessage">
                <span class="glyphicon glyphicon-trash"></span>
                <span><%--删除所选消息--%><fmt:message key="notice.messageDelete" /></span>
            </a>
        </div>
    </div>
    {{each rows}}
    <div class="well well-md clearfix">
        <div class="row">
            <div class="col-md-2 col-sm-3 col-xs-12">
                <input type="checkbox" class="messageCheckbox" name="ids" data="{{$value.id}}">
                <span class="text-success">{{$formatDate $value.createDate}}</span>
            </div>
            <div class="col-md-9 col-xs-8">
                <span>{{$value.content}}</span>
            </div>
            <div class="col-sm-1 col-xs-4">
                <span class="messageClose glyphicon glyphicon-trash" aria-label="Close" data="{{$value.id}}" style="cursor: pointer"></span>
            </div>
        </div>
    </div>
    {{/each}}
</script>
<script>
    var renderPage;
    seajs.use(['pagination', 'template', 'moment', 'cookie','i18n'], function (Pagination, Template, moment, Cookie,I18n) {
        $('.pageLoader').hide();
        template.helper('$formatDate', function (millsec) {
            return moment(millsec).format("YYYY.MM.DD");
        });
        var langType = Cookie.get('locale');
        $("#langType").val(langType);
        renderPage = new Pagination({
            url: "${ctx}/common/message/data",
            elem: "#pagination",
            form: $("#messageForm"),
            rows: 5,
            method: "post",
            handleData: function (json) {
                var html = Template.render("list_tpl", json);
                $("#list_element").html(html);
                /*删除单条消息*/
                $(".messageClose").click(function () {
                    var data = $(this).attr("data");
                    confirmDialog("Are you sure to delete?", function () {
                        $.ajax({
                            cache: true,
                            type: 'POST',
                            url: "${ctx}/common/message/del",
                            data: "ids=" + data,
                            dataType: 'json',
                            async: false,
                            success: function (data) {
                                if (data.code == bitms.success) {
                                    remind(remindType.success,data.message,300);
                                    renderPage.render(true);
                                } else {
                                    remind(remindType.error,data.message,300);
                                }
                            }
                        });
                    })
                });
                /*删除所选消息*/
                $("#deleteMessage").click(function () {
                    var dataNum = [];
                    $(".messageCheckbox:checked").each(function () {
                        dataNum.push($(this).attr("data"));
                    });
                    if(dataNum.length==0){
                        alert("请选择要删除的消息！");
                        return;
                    }
                    else{
                        var data = dataNum.join(",");
                        confirmDialog("确定删除所选消息么？", function () {
                            $.ajax({
                                cache: true,
                                type: 'POST',
                                url: "${ctx}/common/message/del",
                                data: {ids: data},
                                dataType: 'json',
                                async: false,
                                success: function (data) {
                                    if (data.code == bitms.success) {
                                        remind(remindType.success,data.message,300);
                                        renderPage.render(true);
                                    } else {
                                        remind(remindType.error,data.message,300);
                                    }
                                }
                            });
                        });
                    }
                });
            }
        });
    })
</script>
</body>
</html>
