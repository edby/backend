<%@ page trimDirectiveWhitespaces="true" isELIgnored="false" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/bitms.tld" %>
<%@ taglib prefix="form" uri="/WEB-INF/tlds/form.tld" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="imagesPath" value="${ctx}/images"/>
<c:set var="stylesPath" value="${ctx}/styles"/>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>BITMS - BE INCREDIBLE EXCHANGE</title>
        <meta name="description" >
        <meta name="Keywords" >
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="shortcut icon" href="${imagesPath}/common/favicon2.ico"/>
        <link rel="stylesheet" type="text/css" href="${ctx}/scripts/bootstrap/3.3.7/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="${ctx}/styles/common/reset.css">
        <link rel="stylesheet" type="text/css" href="${ctx}/styles/common/common.css">
        <link rel="stylesheet" type="text/css" href="${ctx}/styles/common/media.css">
        <link rel="stylesheet" type="text/css" href="${ctx}/styles/bitms.css">
        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="${ctx}/scripts/bootstrap/3.3.7/html5shiv.min.js"></script>
        <script src="${ctx}/scripts/bootstrap/3.3.7/respond.min.js"></script>
        <![endif]-->
        <script src="${ctx}/scripts/jquery.min.js"></script>
        <script src="${ctx}/scripts/bootstrap/3.3.7/bootstrap.min.js"></script>
        <script src="${ctx}/scripts/seajs/seajs/2.1.1/sea.js"></script>
        <script src="${ctx}/scripts/seajs/seajs-style/1.0.2/seajs-style.js"></script>
        <script src="${ctx}/scripts/seajs_config.js"></script>
        <script src="${ctx}/scripts/dictionary.js"></script>
        <script src="${ctx}/scripts/common.js"></script>
        <script>seajs.use(["i18n"],function(I18n){I18n.properties({name:"Messages",path:"/scripts/i18n/",mode:"map"})});</script>
    </head>

