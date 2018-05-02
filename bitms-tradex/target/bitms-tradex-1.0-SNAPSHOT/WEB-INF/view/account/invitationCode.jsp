<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-sm-5 col-xs-12 padding-xs-none" style="margin-bottom:10px;">
            <div style="border:1px solid #ddd;">
                <h4 class="loginTit" style="display:block;background-color:#e8e8e8;padding:7px;font-size:12px;font-weight:bold;">
                    <span>Invitation Code</span>
                </h4>
                <div class="text-center clearfix" style="padding-bottom:5px;margin:20px auto;max-width:600px;">
                    <div class="form-group">
                        <div class="col-xs-10 col-xs-offset-1 input-group">
                            <span class="input-group-addon glyphicon glyphicon-send" style="top:0;"></span>
                            <textarea id="code" class="form-control" rows="1" readonly style="background-color:#eee;resize:none;">${inviteCode}</textarea>
                            <span onclick="myCopy()" class="input-group-addon" style="top:0;cursor:pointer;background-color:#337ab7;color:#fff;border:none;">Copy</span>
                        </div>
                        <p class="col-xs-10 col-xs-offset-1 text-danger text-left" style="margin-top:10px;padding:0;">When someone else enters your invitation code into the activation page, you will get <fmt:formatNumber type="number" value="${award}" pattern="#,##0" maxFractionDigits="0"/> BIEX when the token trading activation is successful.</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-7 col-xs-12 padding-xs-none">
            <div class="table-responsive">
                <table class="table table-striped table-condensed table-bordered text-center" style="font-size:12px;margin-bottom:0;">
                    <thead>
                    <tr style="border:1px solid #ddd;">
                        <th class="text-center" style="background-color:#e8e8e8;border-left:1px solid #ddd;width:25%">Date</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:25%">Activated Contract Address</th>
                        <th class="text-center" style="background-color:#e8e8e8;width:25%">Associated account</th>
                        <th class="text-center" style="background-color:#e8e8e8;border-right:1px solid #ddd;width:25%">Reward</th>
                    </tr>
                    </thead>
                    <tbody id="invitation">

                    </tbody>
                </table>
                <div id="pagination" class="paginationContainer"></div>
                <div class="text-center" id="listTip" style="margin:100px;">
                    <span class="glyphicon glyphicon-list-alt" style="font-size:100px;color:#ddd;margin-bottom:20px;"></span>
                    <p>You have no invitation History</p>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script id="invitation_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td class="text-center">{{$formatDate $value.createDate}}</td>
        <td class="text-center">{{$value.contractAddr}}</td>
        <td class="text-center">{{$value.inviter}}</td>
        <td class="text-center">+{{$value.awardAmount}}&nbsp;BIEX</td>
    </tr>
    {{/each}}
</script>
<script>
    var invitation = null;
    seajs.use([ 'pagination', 'template', 'moment'], function(Pagination, Template, moment) {

        template.helper('$formatDate', function(millsec) {
            return moment(millsec).format("YYYY-MM-DD HH:mm:ss");
        });

        if('${longStatus}' == 'true') {
            invitation = new Pagination({
                url: "${ctx}/exchange/currents/awardCurrentsList",
                elem : "#pagination",
                rows: 10,
                method: "get",
                handleData: function (json) {
                    var html = Template.render("invitation_list_tpl", json);
                    $("#invitation").html(html);

                    <%--判断图标是否显示--%>
                    if(json.rows.length > 0){
                        $("#listTip").hide();
                    }else {
                        $("#listTip").show();
                    }
                }
            });
        }
    });

    function myCopy(){
        var ele = document.getElementById("code");
        ele.select();
        document.execCommand("Copy");
    }
</script>
</body>
</html>