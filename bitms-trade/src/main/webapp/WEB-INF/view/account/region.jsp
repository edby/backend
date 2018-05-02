<!--推广中心 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%--<%@ include file="/global/header1.jsp" %>--%>
<link rel="stylesheet" type="text/css" href="${stylesPath}/account/extensionCenter.css">
<body class="indexPage">
<div class="main-wrapper">
    <div class="header clearfix">
    </div>
    <div class="content">
        <div class="main">
            <!--主要内容-->
            <div class="extensionCenter-wrap">
                <div class="well clearfix">
                </div>
                <form data-widget="validator" name="mainForm" id="mainForm" method="post">
                    <div class="extensionCenter-data">
                        <h3>区域代码列表</h3>
                        <table id="rowPage" class="table">
                            <thead>
                            <tr>
                                <th>国际简码</th>
                                <th>国际代码</th>
                                <th>中文名称</th>
                                <th>英文名称</th>
                                <th>区域</th>
                            </tr>
                            </thead>
                            <tbody id="list_emement">

                            </tbody>
                        </table>
                        <div class="mt10 m010">
                            <%-- 通用分页 --%>
                            <div id="pagination"></div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script id="list_tpl" type="text/html">
        {{each rows}}
        <tr class="test">
            <td>{{$value.sCode}}</td>
            <td>{{$value.lCode}}</td>
            <td>{{$value.cnName}}</td>
            <td>{{$value.enName}}</td>
            <td>{{$value.area}}</td>
        </tr>
        {{/each}}
    </script>

    <script>
        seajs.use(['pagination', 'template'], function (Pagination, Template) {
            var renderPage = new Pagination({
                url: "${ctx}/common/region/data",
                elem: "#pagination",
                form: $("#mainForm"),
                rows: 20,
                method: "post",
                handleData: function (json) {
                    var html = Template.render("list_tpl", json);
                    $("#list_emement").html(html);
                }
            });
        });
    </script>
</div>
</body>
</html>