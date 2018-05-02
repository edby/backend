<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
	$(function() {
		$('#editRegionForm').form({
			url : '${ctx}/common/region/save',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				var result = $.parseJSON(result);
				if (result.code == ajax_result_success_code) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.message, 'error');
					$('#editRegionForm').find('input[name="csrf"]').val(result.csrf);
					}
				}
		});

	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden; padding: 3px;">
		<form:form id="editRegionForm" method="post">
			<table class="grid">
				<tr>
					<td>国际简码</td>
					<td><input name="id" type="hidden" value="${region.id}">
						<input name="sCode" type="text" placeholder="请输入国际简码" style="width:374px"
						class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']"
						value="${region.SCode}"></td>
				</tr>
				<tr>
					<td>国际代码</td>
					<td><input name="lCode" type="text" placeholder="请输入国际代码" style="width:374px"
						class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']"
						value="${region.LCode}"></td>
				</tr>
				<tr>
					<td>英文名称</td>
					<td><input name="enName" type="text" placeholder="请输入英文名称" style="width:374px"
						class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']"
						value="${region.enName}"></td>
				</tr>
				<tr>
					<td>中文名称</td>
					<td><input name="cnName" type="text" placeholder="请输入中文名称" style="width:374px"
						class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']"
						value="${region.cnName}"></td>
				</tr>
				<tr>
					<td>区域(洲)</td>
					<td><input name="area" type="text" placeholder="请输入区域" style="width:374px"
						class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']"
						value="${region.area}"></td>
				</tr>
				<tr>
					<td>排序</td>
					<td><input name="sortNum" type="text" placeholder="请输入排序号" style="width:374px"
						class="easyui-numberbox"
						data-options="required:true,min:0,max:99"
						value="${region.sortNum}"></td>
				</tr>

			</table>
		</form:form>
	</div>
</div>