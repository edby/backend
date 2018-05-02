<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    if ("${monitorLimitParam.id}" != "") {
        $("input[id='id']").attr("value", "${monitorLimitParam.id}");
        $("input[id='idxName']").attr("value", "${monitorLimitParam.relatedId}");
        <%--$("#idxName").attr("value", "${monitorLimitParam.idxName}");--%>
        $("input[id='stockinfoId']").attr("value", "${monitorLimitParam.stockinfoId}");
        $("input[id='minValue']").attr("value", "${monitorLimitParam.minValue}");
        $("input[id='maxValue']").attr("value", "${monitorLimitParam.maxValue}");
        $("#paramDesc").text("${monitorLimitParam.paramDesc}");

    }else{
        //新增模式下需要将当前选中的指标带下来
        $("input[id='idxName']").attr("value", $("#relatedId").val());
    }


    $(function () {
        $('#editMonitorLimitParamForm').form({
            url: '${ctx}/monitor/index/limitParam/save',
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
                    $('#editMonitorLimitParamForm').find('input[name="csrf"]').val(result.csrf);

                }
            }
        });
    });

    //比较方向下拉菜单compDirectTd
    $("#compDirectTd").html(printOptions(compDirect, 'compDirect', 'compDirect', '${monitorLimitParam.compDirect}', 'required:true,', 'width:305px'));
    $("#compDirect").combobox({
        valueField: 'code',
        textField: 'name'
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editMonitorLimitParamForm" method="post">
            <input name="id" type="hidden" value="${monitorLimitParam.id}">
            <table class="grid">
                <tr>
                    <td>指标名称</td>

                    <td><input id="idxName" name="relatedId" style="width: 300px;" placeholder="请输入指标名称"
                               class="easyui-combobox"
                               data-options="required:true,url: '${ctx}/monitor/monitorIndex/all', method: 'get', valueField:'id',
								textField:'idxName', groupField:'group'"></td>
                </tr>
                <tr>
                    <td>证券代码</td>
                    <td>
                        <input id="stockinfoId" name="stockinfoId" class="easyui-combobox" name="language"
                               style="width: 305px;"
                               placeholder="请选择证券" value="${systemWalletERC20.stockinfoId}" data-options="
								editable:false,url: '${ctx}/stock/info/realAll', method: 'get', valueField:'id',
								textField:'stockName', groupField:'group',required:true">
                </tr>

                <tr>
                    <td>最小阈值</td>
                    <td>
                        <input id="minValue" class="easyui-validatebox" validType="number"
                               name="minValue"
                               style="width:305px;"
                               data-options="required:true">

                        </input>
                    </td>

                </tr>
                <tr>
                    <td>最大阈值</td>
                    <td>
                        <input id="maxValue" class="easyui-validatebox" validType="number"
                               name="maxValue"
                               style="width:305px;"
                        >

                        </input>
                    </td>

                </tr>
                <tr>
                    <td>比较方向</td>
                    <td id="compDirectTd">
                    <%--<td>--%>
                        <%--<select id="compDirect" class="easyui-combobox " name="compDirect" style="width:300px;" >--%>
                            <%--<option  value="1">绝对值</option>--%>
                            <%--<option value="2">资产-负债</option>--%>
                            <%--<option value="3">负债-资产</option>--%>
                            <%--<option value="4">内部-外部</option>--%>
                            <%--<option value="5">外部-内部</option>--%>
                        <%--</select>--%>
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