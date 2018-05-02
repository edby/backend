<%--标签 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="edge" />
<link rel="shortcut icon" href="${res}/style/images/favicon.ico" />
<%-- [my97日期时间控件] --%>
<script type="text/javascript" src="${res}/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<%-- [jQuery] --%>
<script type="text/javascript" src="${res}/easyui/jquery.min.js" charset="utf-8"></script>
<%-- [EasyUI] --%>
<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${res}/easyui/themes/gray/easyui.css" />
<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${res}/easyui/themes/icon.css" />
<script type="text/javascript" src="${res}/easyui/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${res}/easyui/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<%-- [扩展JS] --%>
<script type="text/javascript" src="${res}/extJs.js" charset="utf-8"></script>
<%-- [ztree] --%>
<link rel="stylesheet" type="text/css" href="${res}/ztree/css/zTreeStyle.css" />
<script type="text/javascript" src="${res}/ztree/js/jquery.ztree.core.js" charset="utf-8"></script>
<%-- [扩展样式] --%>
<link rel="stylesheet" type="text/css" href="${res}/style/css/dreamlu.css?v=10" />
<link rel="stylesheet" type="text/css" href="${res}/foundation-icons/foundation-icons.css" />
<%-- [ueditor] --%>
<script type="text/javascript" charset="utf-8" src="${res}/ueditor/ueditor.parse.js"> </script>
<script type="text/javascript" charset="utf-8" src="${res}/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${res}/ueditor/ueditor.all.min.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="${res}/ueditor/lang/zh-cn/zh-cn.js"></script>

<script type="text/javascript" src="${res}/js/datetimeformat.js" charset="utf-8"></script>
<script type="text/javascript" src="${res}/js/publicparams.js" charset="utf-8"></script>
<script type="text/javascript" src="${res}/js/common.js" charset="utf-8"></script>
<script type="text/javascript" src="${res}/js/dictionary.js" charset="utf-8"></script>

<script type="text/javascript" src="${res}/js/jquery.qrcode.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${res}/js/md5.min.js" charset="utf-8"></script>
<script type="text/javascript">
    var basePath = "${ctx}";
    window.UEDITOR_HOME_URL = "${res}/ueditor/";
	window.PROJECT_CONTEXT = "${ctx}";
    function setCsrfToken(formId) {
        var csrfToken = $('#' + formId).find('input[name="csrf"]').val();
        $(document).on('ajaxSend', function (elem, xhr, s) {
            if (s.type.toUpperCase() == 'POST') {
                if (xhr.setRequestHeader) {
                    xhr.setRequestHeader('csrf', csrfToken);
                } else {
                    s.url += (s.url.indexOf("?") == -1) ? "?" : "&";
                    s.url += "csrf=" + csrfToken;
                }
            }
        });
    }
</script>