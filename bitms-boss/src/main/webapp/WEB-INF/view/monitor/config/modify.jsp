<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    //对编辑模式下的输入框赋值（添加模式则不会执行条件体内代码）
    if ("${monitorConfig.id}" != "") {
        $("input[id='id']").attr("value", "${monitorConfig.id}");
        $("input[id='monitorCode']").attr("value", "${monitorConfig.monitorCode}");
        $("input[id='monitorName']").attr("value", "${monitorConfig.monitorName}");
        $("input[id='monitorCategorys']").attr("value", "${monitorConfig.monitorCategorys}");
        $("input[id='idxid1']").attr("value", "${monitorConfig.idxid1}");
        $("input[id='idxid2']").attr("value", "${monitorConfig.idxid2}");
        $("input[id='idxid3']").attr("value", "${monitorConfig.idxid3}");
        $("input[id='idxid4']").attr("value", "${monitorConfig.idxid4}");
        $("input[id='pollingTime']").attr("value", "${monitorConfig.pollingTime}");
        $("#monitorDesc").text("${monitorConfig.monitorDesc}");

        //启用状态
        if ("${monitorConfig.active}" == "true") {
            $("#active").prop("checked", true);
        }
        if ("${monitorConfig.active}" == "false") {
            $("#unActive").prop("checked", true);
        }

    } else {
        //如果是添加模式则移除监控代码输入框的只读属性
        $('input[name=monitorCode]').removeAttr("readonly");

    }

    $(function () {
        $('#editMonitorConfigForm').form({
            url: '${ctx}/monitor/config/save',
            onSubmit: function () {
                var limitList = new Array();
                $("#list tr td input").each(function (i, v) {
                    limitList.push($(this).val());
                });
                $('#limit').val(limitList);
                progressLoad();
                var isValid = $(this).form('validate');
                if (!isValid) {
                    alert('参数验证失败');
                    progressClose();
                }
                return isValid;


            },
            success: function (result) {
                progressClose();
                var result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editMonitorConfigForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });

        //自定义输入框验证规则：数值限制，只能输入数字或小数
        var reg = new RegExp("^\\d+(\\.\\d+)?$");
        $.extend($.fn.validatebox.defaults.rules, {//添加自定义表单验证规则：只能输入数值（可带小数点）
            number: {//value值为文本框中的值
                validator: function (value) {
                    var reg = /^\d+(\.\d+)?$/;
                    return reg.test(value);
                },
                message: '请输入正确的数值'
            }
        });

    });

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: scroll;padding: 3px;">
        <form:form id="editMonitorConfigForm" method="post">
            <input id="id" name="id" type="hidden">
            <table class="grid">
                <tr>
                    <td>监控代码</td>
                    <td>
                        <input id="monitorCode" name="monitorCode" readonly style="width:305px;"
                               data-options="required:true">
                        </input>
                    </td>
                </tr>
                <tr>
                    <td>监控名称</td>
                    <td>
                        <input id="monitorName" name="monitorName" style="width:305px;"
                               data-options="required:true">
                        </input>

                    </td>
                </tr>
                <tr>
                    <td>监控业务列表</td>
                    <td>
                        <input id="monitorCategorys" class="easyui-combobox" name="monitorCategorys"
                               style="width:305px;"
                               data-options="
                                    required:true,multiple:true,editable:false,
									url: '${ctx}/stock/info/realAll', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'">

                        </input>
                    </td>
                <tr>
                <tr>
                    <td>监控指标1</td>
                    <td>
                        <input id="idxid1" class="easyui-combobox" name="idxid1"
                               style="width:305px;"
                               data-options="

									url: '${ctx}/monitor/monitorIndex/all', method: 'get', valueField:'id',
									textField:'idxName', groupField:'group'">

                        </input>
                            <%--<input id="btn_limit" type="button"  value="阈值设置" onclick="setLimitsFun();"/>--%>
                    </td>
                <tr>
                <tr>
                    <td>监控指标2</td>
                    <td>
                        <input id="idxid2" class="easyui-combobox" name="idxid2"
                               style="width:305px;"
                               data-options="

									url: '${ctx}/monitor/monitorIndex/all', method: 'get', valueField:'id',
									textField:'idxName', groupField:'group'">

                        </input>
                    </td>
                <tr>
                <tr>
                    <td>监控指标3</td>
                    <td>
                        <input id="idxid3" class="easyui-combobox" name="idxid3"
                               style="width:305px;"
                               data-options="

									url: '${ctx}/monitor/monitorIndex/all', method: 'get', valueField:'id',
									textField:'idxName', groupField:'group'">

                        </input>

                    </td>
                <tr>
                <tr>
                    <td>监控指标4</td>
                    <td>
                        <input id="idxid4" class="easyui-combobox" name="idxid4"
                               style="width:305px;"
                               data-options="

									url: '${ctx}/monitor/monitorIndex/all', method: 'get', valueField:'id',
									textField:'idxName', groupField:'group'">

                        </input>

                    </td>
                <tr>

                    <td>轮询时间</td>
                    <td>
                        <input id="pollingTime" class="easyui-validatebox" validType="number" name="pollingTime"
                               style="width:305px;"
                               data-options="required:true">

                        </input>
                    </td>

                </tr>
                <tr>
                    <td>是否启用</td>
                    <td>
                        <label>启用<input id="active" name="active" type="radio" value="1" data-options="required:true"/>
                        </label>
                        <label>停用<input id="unActive" name="active" type="radio" value="-1"
                                        data-options="required:true"/>
                        </label>
                    </td>

                </tr>

                <tr>
                    <td>备注</td>
                    <td>
                        <textarea id="monitorDesc" name="monitorDesc"
                                  style="width: 374px;height: 50px;"></textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>