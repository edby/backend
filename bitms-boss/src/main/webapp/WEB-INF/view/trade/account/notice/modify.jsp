<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editNoticeForm').form({
            url : '${ctx}/account/notice/save',
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
                    $('#editNoticeForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        }); 
        
        var longtime='${notice.publicDate}';
        if(longtime == ''){
        	longtime=new Date().getTime()+'';
        }
        var status='${notice.status}';
        if(status == null || status == ''){
        	status='unPublished';
        }
        $("#status").val(status);
       $("#publicDate2").val(getFormatDateByLong(longtime*1, "yyyy-MM-dd hh:mm:ss"));

	   	$("#langTypeEditTd").html(dictDropDownOptions('langTypeEdit','langType', 'langType', '${notice.langType}', 'required:true,', 'width:474px,'));
	   	$("#langTypeEdit").combobox({
	   	    valueField:'code',
	   	    textField:'name'
	   	});
    });
   try{UE.delEditor('_container');}catch(err){}
   $("#ueditor").html('<textare  id="_container" name="content" ex-id="content" style=" width:474px;height: 200px;"></textarea>');
   var ue = UE.getEditor('_container',{
        toolbars:[['FullScreen', 'Source', 'Undo', 'Redo','bold','test']],  
        autoClearinitialContent:true,  
        wordCount:false
    });
   ue.ready(function() {//编辑器初始化完成再赋值  
       ue.setContent(escape2Html("${notice.content}"));  //赋值给UEditor  
   }); 
   function escape2Html(str) {
		var arrEntities = {
			'lt' : '<','gt':'>',
			'nbsp' : ' ',
			'amp' : '&',
			'quot' : '"'
		};
		return str.replace(/&(lt|gt|nbsp|amp|quot);/ig, function(all, t) {
			return arrEntities[t];
		});
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editNoticeForm" method="post">
        	<input type="hidden" name="id" value="${notice.id}"/>
            <table class="grid">
                <tr>
                    <td>语言类型</td>
                    <td id="langTypeEditTd">
                    </td>
                </tr>
                <tr>
                    <td>发布时间</td>
                    <td>
                    	<input id="publicDate2"  name="publicDate"  type="text" placeholder="请输入发布时间" class="easyui-datetimebox" 
                               data-options="required:true" value=""  style="width:474px;"></td>
                </tr>
                <tr>
                    <td>标题</td>
                    <td ><input name="title" type="text" placeholder="请输入标题" style="width:474px;" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,64]']" value="${notice.title}"> 
                    </td>
                </tr>
                <tr>
                    <td>内容</td>
                    <td  id="ueditor">
                    </td>
                </tr>
            </table>
        </form:form >
    </div>
</div>