<%--历史信息查询--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="tabbable" id="save-history">
    <ul class="nav nav-tabs bitms-tabs">
        <li class="save-history1 active ">
            <a href="#save-history1" data-toggle="tab"><fmt:message key="account.setting.LoginHistory"/></a>
        </li>
        <li class="save-history2">
            <a href="#save-history2" data-toggle="tab"><fmt:message key="account.setting.safeHistory"/></a>
        </li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="save-history1">
            <form id="loginHistoryForm">
                <p class="text-success  fs12  mt10" id="p1"><fmt:message key="account.setting.loginTime"/>：<span class="time"></span>，<fmt:message key="account.setting.loginIP"/>IP：<span class="ip"></span></p>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th style="width: 40%;"><fmt:message key="time"/></th>
                            <th style="width: 60%;">IP</th>
                            <%--<th><fmt:message key="state"/></th>--%>
                        </tr>
                        </thead>
                        <tbody id="list_element1">
                        </tbody>
                    </table>
                </div>
            </form>
        </div>
        <div class="tab-pane" id="save-history2">
            <p class="text-success fs12 mt10" id="p2"><fmt:message key="account.setting.loginTime"/>：<span class="time"></span>，<fmt:message key="account.setting.loginIP"/>IP:<span class="ip"></span></p>
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th style="width: 30%;"><fmt:message key="time"/></th>
                        <th style="width: 50%;"><fmt:message key="account.setting.safeHistory"/></th>
                        <th style="width: 20%;">IP</th>
                    </tr>
                    </thead>
                    <tbody id="list_element2">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script id="list_tpl1" type="text/html">
    {{each}}
    <tr>
        <td style="width: 40%;">{{$formatDate $value.createDate}}</td>
        <td style="width: 60%;">{{$value.ipAddr}}({{$value.rigonName}})</td>
        <%--<td>{{$value.opType}}</td>--%>
    </tr>
    {{/each}}
</script>
<script id="list_tpl2" type="text/html">
    {{each}}
    <tr>
        <td style="width: 30%;">{{$formatDate $value.createDate}}</td>
        <td style="width: 50%;">{{$value.content}}</td>
        <td style="width: 20%;">{{$value.ipAddr}}({{$value.rigonName}})</td>
    </tr>
    {{/each}}
</script>
<script>
    //最近登录历史，安全设置历史
    seajs.use(['template','moment'],function(Template,moment){
        template.helper('$formatDate', function(millsec) {
            return moment(millsec*1).format("YYYY-MM-DD HH:mm:ss");
        });
        $.ajax({
            url:"${ctx}/common/logs/login",
            type:"get",
            dataType:'json',
            success:function(json){
                var html = Template.render("list_tpl1",json.object);
                $("#list_element1").html(html);

                var timeStr=json.object[0].createDate;
                var time= moment(timeStr*1).format("YYYY-MM-DD HH:mm:ss");
                var ip=json.object[0].ipAddr;
                var ipAddr = json.object[0].rigonName;
                $('#p1 .time,#p2 .time').text(time);
                $('#p1 .ip,#p2 .ip').text(ip+'('+ipAddr+')');

            }
        });

        $('.save-history2').click(function(){
        $.ajax({
            url:"${ctx}/common/logs/setting",
            type:"get",
            dataType:'json',
            beforeSend: function () {
                $('.reg-pop').fadeIn();
            },
            success:function(json){
                if(json.object.length != 0){
                    var html = Template.render("list_tpl2",json.object);
                    $("#list_element2").html(html);
                }
            }
        });
        })
    })
</script>
