<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<style>
    .queryCon *{
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
    }
    #queryForm table {
        width: 100%;
    }
    .queryCon{
        width: auto;
        margin: 20px;
        min-width: 960px;
        border: solid 1px #eee;
        padding: 10px 0;
        padding-right: 0;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        height: auto;
        overflow: hidden;
        color: #333;
        font-size:16px;
    }
    form#queryForm tr td:first-child,form#queryForm tr td:nth-child(3) {
        width:100px;
        font-size: 15px;
    }
    form#queryForm tr td:nth-child(2){
        width:200px;
    }
    form#queryForm input,form#queryForm button{
        width:150px;
        height: 25px;
        line-height: 25px;
        font-size: 14px;
        padding: 0 10px;
    }
    form#queryForm textarea {
        width:100%;
        height: 120px;
        font-size: 15px;
        padding: 5px 10px;
    }
    .jqueryData {
        width: 100%;
        border-top: solid 1px #eee;
        margin-top: 10px;
        padding: 10px 20px;
        overflow: auto;
        height: 500px;
    }
    .jqueryData tr{
        border:solid 1px red;
    }
    .jqueryData tr td {
        font-size: 15px;
        line-height: 25px;
    }
    .jqueryData table{
        border:solid 1px #eee;
    }
    .jqueryData caption {
        font-size: 18px;
        margin: 0 auto;
        text-align: left;
        line-height: 25px;
        margin-bottom: 10px;
    }
    .jqueryData tr td,.jqueryData th {
        font-size: 16px;
        line-height: 25px;
        border: solid 1px #eee;
        padding: 0 20px;
        white-space: nowrap;
    }
</style>
<div class="queryCon">
    <form id="queryForm">
        <table cellspacing="10">
            <tr>
                <td>SQL语句:</td>
                <td colspan="3"><textarea name="sql"></textarea></td>
            </tr>
            <tr>
                <td>offset:</td>
                <td><input name="offset" type="text"></input></td>
                <td>length:</td>
                <td><input name="length" type="text"></input></td>
            </tr>
            <tr>
                <td></td>
                <td><button id="subBtn" type="button" >提交</button></td>
            </tr>
        </table>
    </form>
    <div class="jqueryData">
    </div>
</div>
    <script type="text/javascript">
        $(function(){
            $('#subBtn').click(function(){
                $.ajax({
                url:'${ctx}/monitor/query/data',
                    data:$('#queryForm').serialize(),
                type:'post',
                dataType:"json",
                success:function(data){
                    if(data.code==200){
                        $('.jqueryData').html(data.object);
                    }else if(data.code == 2003) {
                        window.location.reload();
                    }else{
                        alert("操作失败");
                    }
                }
            })
        })
        })
</script>


