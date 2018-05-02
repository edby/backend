<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript" src="${ctx}/static/js/monitor.js" charset="utf-8"></script>
<script type="text/javascript">

    if ("${monitorIndex.id}" != "") {
        //编辑模式
        $("input[id='id']").attr("value", "${monitorIndex.id}");
        $("input[id='idxName']").attr("value", "${monitorIndex.idxName}");
        $("input[id='idxLevel']").attr("value", getIdxLevelName(${monitorIndex.idxLevel}));
        $("input[id='actionValue']").attr("value", "${monitorIndex.actionValue}");
        $("#paramDesc").text("${monitorIndex.paramDesc}");

        //处理方式显示
        var actions = new Array(); //定义一数组
        actions = "${monitorIndex.actionType}".split(","); //字符分割
        for (var i = 0; i < actions.length; i++) {
            if (actions[i] == "doNothing") {
                $("#ac1").prop("checked", true);
                $("#one").hide();
                $("#two").hide();
                $("#three").hide();
            } else if (actions[i] == "FrozenAcct") {
                $("#ac2").prop("checked", true);
            } else if (actions[i] == "Message") {
                $("#ac3").prop("checked", true);
                $("#valTr").show();
            } else if (actions[i] == "CloseTradeAndWithdraw") {
                $("#ac4").prop("checked", true);
            }
        }
    } else {
        //添加模式
//        $("input[id='idxName']").attr("value", "");
    }


    //保存编辑信息
    $(function () {
        $('#editMonitorIndexForm').form({
            url: '${ctx}/monitor/monitorIndex/save',
            onSubmit: function () {
                var isValid = $(this).form('validate');
                if (isValid) {
                    progressLoad();
                }
                return isValid;
            },
            success: function (result) {
                progressClose();
                result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editMonitorIndexForm').find('input[name="csrf"]').val(result.csrf);

                }
            }
        });
    });

    //选中无操作则其他三个选项自动隐藏且重置为未选中状态，同时要确保参数值为空
    $("#ac1").click(function () {
        $('#actionValue').combobox('clear');//清空选中项
        if ($("#ac1").is(":checked")) {
            $("input[id='ac2']").attr("checked", false);
            $("input[id='ac3']").attr("checked", false);
            $("input[id='ac4']").attr("checked", false);

            $("#valTr").hide();
            $("#one").hide();
            $("#two").hide();
            $("#three").hide();
        } else {
            $("#one").show();
            $("#two").show();
            $("#three").show();
        }
        ;
    });

    //当选中消息通知时显示参数值，否则隐藏参数值并重置参数值为空
    $("#ac3").click(function () {
        if ($("#ac3").is(":checked")) {
            $("#valTr").show();
        } else {
            $('#actionValue').combobox('clear');//清空选中项
//            $("#valTr input").clear;
            $("#valTr").hide();

        }
        ;
    });

    //指标级别下拉菜单
    $("#idxLevelTd").html(printOptions(idxLevel,'idxLevel', 'idxLevel', '${monitorIndex.idxLevel}', 'required:true,', 'width:300px'));
    $("#idxLevel").combobox({
        valueField: 'code',
        textField: 'name'
    });


</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editMonitorIndexForm" method="post">
            <input name="id" type="hidden" value="${monitorIndex.id}">
            <table class="grid">
                <tr>
                    <td>指标名称</td>
                    <td><input id="idxName" name="idxName" data-options="required:true" style="width: 300px;"
                               placeholder="请输入指标名称" class="easyui-validatebox easyui-textbox">
                    </td>
                </tr>
                <tr>
                    <td>指标级别</td>
                    <td id="idxLevelTd">
                    </td>
                </tr>
                <tr>
                    <td>处理方式</td>
                    <td>
                        <label><input id="ac1" name="actionType" type="checkbox" value="doNothing"
                                      data-options="required:true"/>无操作
                        </label>
                        <label id="one"><input id="ac2" name="actionType" type="checkbox" value="FrozenAcct"
                                               data-options="required:true"/>冻结账户
                        </label>
                        <label id="two"><input id="ac3" name="actionType" type="checkbox"
                                               value="Message" data-options="required:true"/>消息通知</label>
                        <label id="three"><input id="ac4" name="actionType" type="checkbox"
                                                 value="CloseTradeAndWithdraw"
                                                 data-options="required:true"/>关闭交易和提现</label>
                    </td>
                </tr>
                <tr id="valTr" style="display:none">
                    <td>参数值</td>
                    <td>
                        <input id="actionValue" class="easyui-combobox" name="actionValue" style="width:305px;"

                               data-options="multiple:true,panelHeight:'auto',editable:false,
									url: '${ctx}/monitor/param/related', method: 'get', valueField:'id',
									textField:'paramName', groupField:'group'">
                        </input>
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td>
                        <textarea id="paramDesc" name="paramDesc"
                                  style="width: 374px;height: 50px;"></textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>