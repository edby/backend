<%@ page trimDirectiveWhitespaces="true" isELIgnored="false" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="/WEB-INF/tlds/form.tld" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form:form id="csrf-form" cssStyle="display: none"></form:form>
<script type="text/javascript">
    $(function(){
        setCsrfToken("csrf-form");
        window['G_PATH']="${ctx}";
    });
    
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
