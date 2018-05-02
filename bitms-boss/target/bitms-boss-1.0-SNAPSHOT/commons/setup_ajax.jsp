<%@ page trimDirectiveWhitespaces="true" isELIgnored="false" session="false" %>
<form:form id="csrf-form" cssStyle="display: none"></form:form>
<script type="text/javascript">
    $(function(){
        setCsrfToken("csrf-form");
        window['G_PATH']="${ctx}";
    });
</script>
