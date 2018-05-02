<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript" src="${ctx}/static/js/monitor.js" charset="utf-8"></script>
<script>

    if ("${monitorIndex.id}" != "") {
//编辑模式
//        $('#actionValue').combobox('disable');
        $("input[id='actionValue']").attr("value", "${monitorIndex.actionValue}");
        $("#idxLevel").text(getIdxLevelName(${monitorIndex.idxLevel}));
//处理方式显示
        var actions = new Array(); //定义一数组
        actions = "${monitorIndex.actionType}".split(","); //字符分割
        for (var i = 0; i < actions.length; i++) {
            if (actions[i] == "Message") {
                $("#valTr").show();
            }
        }
    } else {
//添加模式
    }
</script>
<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
    <input name="id" type="hidden" value="${monitorIndex.id}">
    <table class="idxDtail">
        <tr>
            <td>指标名称:</td>
            <td style="width: 300px;">
                ${monitorIndex.idxName}
            </td>
        </tr>
        <tr>
            <td>指标级别:</td>
            <td id="idxLevel" style="width: 300px;">

            </td>
        </tr>
        <tr>
            <td>处理方式:</td>
            <td style="width: 300px;">
                ${monitorIndex.actionType}
            </td>
        </tr>
        <tr id="valTr" style="display:none">
            <td>参数值:</td>
            <td>
                <input id="actionValue" class="easyui-combobox" name="actionValue" style="width:305px;"

                       data-options="multiple:true,panelHeight:'auto',editable:false,
									url: '${ctx}/monitor/param/related', method: 'get', valueField:'id',
									textField:'paramName', groupField:'group'">
                </input>
            </td>
        </tr>
        <tr>
            <td>备注:</td>
            <td style="width: 300px;height: 50px;">
                ${monitorIndex.paramDesc}
            </td>
        </tr>
    </table>
</div>