<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<style>
    .panel-body {
        background-color: #ffffff;
        color: #000000;
        font-size: 16px;
    }

    div#code {
        font-size: 16px;
    }

    div#desc {
        font-size: 16px;
    }

    div#main table tr td {
        height: 30px;
        line-height: 30px;
        font-size: 16px;
        /*border: solid 1px #eee;*/
        /*padding: 0 5px;*/
    }

    .l-btn-text {
        display: inline-block;
        vertical-align: top;
        width: 100px;
        line-height: 24px;
        font-size: 15px;
        padding: 0;
        margin: 0 4px;
    }

</style>
<div id="main">
    <table cellspacing="10">
        <tr>
            <td>
                <%--<shiro:hasPermission name="monitor:setting:archive:operator">--%>
                    <a id="archiveBtn" class="easyui-linkbutton" data-options="iconCls:'icon-save'">归 档</a>
                <%--</shiro:hasPermission>--%>
            </td>
        </tr>
        <tr>
            <td>
                <div id="code"></div>
            </td>
        </tr>
        <tr>
            <td>
                <div id="desc"></div>
            </td>
        </tr>
    </table>
</div>
<script>
    $('#archiveBtn').click(
        function () {
            $.ajax({
                url: '${ctx}/monitor/archive/execute',
                type: 'post',
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        $('#code').text('归档结果代码：' + data.object.archiveCode);
                        $('#desc').text('归档结果描述：' + data.object.archiveDesc);
                    } else {
                        alert("归档出现异常！");
                    }
                }
            });

        });


</script>