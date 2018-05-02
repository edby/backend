<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var feedbackDataGrid;
    $(function () {
        feedbackDataGrid = $('#feedbackDataGrid').datagrid({
            url: '${ctx}/common/feedback/list/data',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                {
                    width: '100',
                    title: '姓名',
                    field: 'trueName',
                    sortable: true
                },{
                    width: '100',
                    title: '联系电话',
                    field: 'contactNumber',
                    sortable: true
                }
            ]],
            columns: [[ {
                width: '200',
                title: '问题描述',
                field: 'describe',
                sortable: true
            },{
                width: '300',
                title: '附件',
                field: 'attachments',
                sortable: true,
                formatter: function (value) {
                    var url = "${baseUrl}/"+value;
                    var str = '';
                    str += $.formatString('<a href="javascript:void(0)" onclick="preview(\'{0}\');" >查看附件</a>', url);
                    return str;
                }
            }
            ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
            }
        });
    });

    function preview(url){
        $('#attachment').window({
            modal:true,
            title:'附件',
            content:"<img style='width: 100%;' src='"+url+"' />"
        });
    }

    function searchfeedbackFun() {
    	feedbackDataGrid.datagrid('load', $.serializeObject($('#searchfeedbackForm')));
    }
    function cleanfeedbackFun() {
        $('#searchfeedbackForm input').val('');
        feedbackDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchfeedbackForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="feedbackTrueName" class="easyui-textbox" name="trueName" placeholder="请输入姓名"/></td>
                    <th>联系电话:</th>
                    <td><input id="feedbackContactNumber" class="easyui-textbox" name="contactNumber" placeholder="请输入联系电话"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchfeedbackFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanfeedbackFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="feedbackDataGrid" data-options="fit:true,border:false"></table>
    </div>
    <div id="attachment" style="width: 500px;"></div>
</div>
