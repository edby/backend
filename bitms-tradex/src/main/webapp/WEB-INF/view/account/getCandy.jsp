<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="col-xs-10 col-xs-offset-1 text-center">
            <div class="form-group" style="color:#f37777">
                <h1 style="font-size:200px;" class="glyphicon glyphicon-gift"></h1>
            </div>
            <div class="form-group clearfix">
                <div class="col-xs-10 col-xs-offset-1">
                    <c:choose>
                        <c:when test="${candyStatus == true}">
                            <button id="collect" type="button" class="btn btn-primary btn-block bingGASub" style="background-color:#f37777">Collect Candy</button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-primary btn-block bingGASub" disabled style="background-color:#f37777">Collect Candy</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <img class="img-responsive" src="${ctx}/images/common/candy.png" style="margin:50px auto;">
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script>
    $("#collect").on("click", function () {
        $.ajax({
            url: '${ctx}/colletCandy',
            type: 'get',
            dataType : "json",
            beforeSend: function () {
                $("#collect").attr("disabled", true);
            },
            success: function (data) {
                if (data.code == bitms.success) {
                    remind(remindType.success,1000,function(){
                        location.reload();
                    });
                } else {
                    remind(remindType.error, data.message);
                }
            },
            complete: function () {
                $("#collect").attr("disabled", false);
            }
        });
    });
</script>
</body>
</html>