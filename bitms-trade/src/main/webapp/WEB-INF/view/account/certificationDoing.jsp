<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<html>
<body>
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp"%>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <div class="col-sm-12 column">
            <%--开始代码位置--%>
            <div class="panel bitms-bg1">
                <div class="row">
                    <div class="col-sm-12">
                        <span><h3 class="text-success"><%--身份认证--%><fmt:message key="account.setting.identity"/></h3></span>
                        <hr />
                        <form:form id="certificationForm" class="form-horizontal" role="form">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><%--姓氏--%><fmt:message key="account.setting.surnames"/></label>
                                <div class="col-sm-4 ui-form-item">
                                    <input type="text" onKeyUp="value=value.replace(/[^a-zA-Z\s]/g,'')" class="form-control" name="surname" placeholder="Must be in English" data-display="Last Name">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><%--名字--%><fmt:message key="account.setting.name"/></label>
                                <div class="col-sm-4 ui-form-item">
                                    <input type="text" onKeyUp="value=value.replace(/[^a-zA-Z\s]/g,'')" class="form-control" name="realname" placeholder="Must be in English" data-display="First Name">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><%--性别--%><fmt:message key="account.setting.gender"/></label>
                                <div class="col-sm-6">
                                    <label class="radio-inline">
                                        <input type="radio" name="sex" value="" checked><%--男--%><fmt:message key="account.setting.male"/></label>
                                    <label class="radio-inline">
                                        <input type="radio" name="sex" value=""><%--女--%><fmt:message key="account.setting.female"/></label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><%--国家及地区--%><fmt:message key="account.setting.addr"/></label>
                                <div class="col-sm-4">
                                    <c:set var="regions" value="${fns:getRegions()}"/>
                                    <select name="regionId" class="form-control" >
                                        <option></option>
                                        <c:forEach var="item" items="${regions}">
                                            <option value="${item.LCode}" ${item.LCode == account.country?"selected":""}>${item.enName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><%--护照ID--%><fmt:message key="account.setting.passport"/>&nbsp;ID</label>
                                <div class="col-sm-4 ui-form-item">
                                    <input type="text" class="form-control" onKeyUp="value=value.replace(/[^a-zA-Z0-9]/g,'')" name="passportId" placeholder="" data-display="Passport ID"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><%--护照个人信息页--%><fmt:message key="account.setting.informationPage"/></label>
                                <div class="col-sm-9">
                                    <a id="selectfiles2" href="javascript:void(0);" class='btn btn-primary uploadImg'><%--点击上传--%><fmt:message key="account.setting.plsUpload"/></a>
                                    <br>
                                    <small class="text-info"><%--请确保照片的内容完整并清晰可用，身份证必须在有效期内，图片仅支持jpg、jpeg、bmp格式，建议图片大小在2M以内。--%><fmt:message key="account.setting.identityTip3"/></small>
                                    <br />
                                    <div class="row">
                                        <div class="col-sm-5 col-xs-12">
                                            <input type="hidden" name="attachment.frontage" />
                                            <div id="progressDiv2"></div>
                                            <img class="img-responsive" id="previewDiv2" src="${imagesPath}/bitms/passport.png"/>
                                            <span class="text-danger errorImg" id="previewDiv2Error"></span>
                                        </div>
                                        <div class="col-sm-2 col-xs-12">
                                            <span><fmt:message key="account.setting.instance"/>▸</span>
                                        </div>
                                        <div class="col-sm-5 col-xs-12">
                                            <img class="img-responsive" src="${imagesPath}/bitms/passport2.png"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <hr />
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><%--手持护照并签字--%><fmt:message key="account.setting.sign"/></label>
                                <div class="col-sm-9">
                                    <a id="selectfiles3" href="javascript:void(0);" class='btn btn-primary uploadImg'><%--点击上传--%><fmt:message key="account.setting.plsUpload"/></a>
                                    <br>
                                    <small class="text-info"><%--请您上传手持身份证正面照和个人签字的照片，个人签字的内容包含“BITMS”和当前日期。请确保照片和个人签字的内容清晰可见。--%><fmt:message key="account.setting.identityTip4"/></small>
                                    <br />
                                    <%--<ul>
                                        <li class="pull-left">
                                            <span class="glyphicon glyphicon-ok text-success" aria-hidden="true"></span>
                                            <small>&lt;%&ndash;头像清晰&ndash;%&gt;<fmt:message key="account.setting.identityImgTxt1"/></small>
                                        </li>
                                        <li class="pull-left">
                                            <span class="glyphicon glyphicon-ok text-success" aria-hidden="true"></span>
                                            <small>&lt;%&ndash;护照ID清晰&ndash;%&gt;<fmt:message key="account.setting.identityImgTxt2"/></small>
                                        </li>
                                        <li class="pull-left">
                                            <span class="glyphicon glyphicon-ok text-success" aria-hidden="true"></span>
                                            <small>&lt;%&ndash;包含当前日期&ndash;%&gt;<fmt:message key="account.setting.identityImgTxt3"/></small>
                                        </li>
                                        <li class="pull-left">
                                            <span class="glyphicon glyphicon-ok text-success" aria-hidden="true"></span>
                                            <small>&lt;%&ndash;包含"BITMS"&ndash;%&gt;<fmt:message key="account.setting.identityImgTxt4"/>"BITMS"</small>
                                        </li>
                                    </ul>
                                    <br />--%>
                                    <div class="row">
                                        <div class="col-sm-5 col-xs-12">
                                            <input type="hidden" name="attachment.opposite" />
                                            <div id="progressDiv3"></div>
                                            <img class="img-responsive" id="previewDiv3" src="${imagesPath}/bitms/passport.png"/>
                                            <span class="text-danger errorImg" id="previewDiv3Error"></span>
                                        </div>
                                        <div class="col-sm-2 col-xs-12">
                                            <span><fmt:message key="account.setting.instance"/>▸</span>
                                        </div>
                                        <div class="col-sm-5 col-xs-12">
                                            <img class="img-responsive" src="${imagesPath}/bitms/passport3.png"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <hr />
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-2">
                                    <button id="certificationSubmit" type="button" class="btn btn-primary btn-block"><%--提交--%><fmt:message key="submit"/></button>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/global/setup_ajax.jsp"></jsp:include>
<script>
    //身份认证-护照认证
    var validator;
    var accessid = '';
    var host = '';
    var policyBase64 = '';
    var signature = '';
    var callbackbody = '';
    var key = '';
    var expire = 0;
    var acl = 'public-read';
    var g_object_name = '';
    var timestamp = Date.parse(new Date()) / 1000;
    var now = timestamp;
    var imageState2 = true;
    var imageState3 = true;

    seajs.use(['validator', 'plupload'], function (Validator) {
        $('.pageLoader').hide();
        //提交身份认证
        validator = new Validator();
        $("#certificationSubmit").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#certificationForm',
                autoSubmit: false,  <%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    imgCheck2();
                    imgCheck3();
                    if(imageState2==false || imageState3==false){
                        return;
                    }
                    if (!error) {
                        $.ajax({
                            url: '${ctx}/account/certification/save',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                                $("#certificationSubmit").attr("disabled",true);
                            },
                            success: function (data, textStatus, jqXHR) {
                                $('#certificationForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                if (data.code == bitms.success) {
                                    remind(remindType.success, data.message,2000,function(){
                                        jumpUrl("${ctx}/account/certification");
                                    });
                                } else {
                                    remind(remindType.error, data.message);
                                }
                            },
                            complete:function(){
                                $('.reg-pop').hide();
                                $("#certificationSubmit").attr("disabled",false);
                            }
                        });
                    }
                }
            }).addItem({
                element: '#certificationForm [name=surname]',
                required: true,
                rule:'maxlength{max:64} Letterblank'
            }).addItem({
                element: '#certificationForm [name=realname]',
                required: true,
                rule:'maxlength{max:64} Letterblank'
            }).addItem({
                element: '#certificationForm [name=sex]',
                required: true
            }).addItem({
                element: '#certificationForm [name=regionId]',
                required: true
            }).addItem({
                element: '#certificationForm [name=passportId]',
                required: true,
                rule:'maxlength{max:32} LetterNumber'
            });
            $("#certificationForm").submit();
        });

        /*附件校验*/
        function imgCheck2() {
            var frontage = $("input[name='attachment.frontage']").attr("value");

            if(frontage == null){
                $("#previewDiv2Error").html("<small class='text-danger'>Please upload your picture!</small>");
                imageState2 = false;
            }else{
                $("#previewDiv2Error").html("");
                imageState2 = true;
            }
        }
        function imgCheck3() {
            var opposite = $("input[name='attachment.opposite']").attr("value");

            if(opposite == null){
                $("#previewDiv3Error").html("<small class='text-danger'>Please upload your picture!</small>");
                imageState3 = false;
            }else{
                $("#previewDiv3Error").html("");
                imageState3 = true;
            }
        }

        //获取安全策略
        function get_signature() {
            now = timestamp = Date.parse(new Date()) / 1000;
            if (expire < now + 3) {
                var body = $.ajax({url: "/common/upload/policy", async: false}).responseText;
                var obj = eval("(" + body + ")");
                host = obj['host'];
                acl = obj['acl'];
                policyBase64 = obj['policy'];
                accessid = obj['accessid'];
                signature = obj['signature'];
                expire = parseInt(obj['expire']);
                key = obj['dir'];
                return true;
            }
            return false;
        }

        //取上传文件的名称
        function get_uploaded_object_name(filename) {
            return g_object_name;
        }

        //生成随机文件名
        function random_string(len) {
            len = len || 32;
            var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
            var maxPos = chars.length;
            var pwd = '';
            for (i = 0; i < len; i++) {
                pwd += chars.charAt(Math.floor(Math.random() * maxPos));
            }
            return pwd;
        }

        //获取文件后缀
        function get_suffix(filename) {
            pos = filename.lastIndexOf('.');
            suffix = '';
            if (pos != -1) {
                suffix = filename.substring(pos)
            }
            return suffix;
        }

        function set_upload_param(up, filename, ret) {
            if (ret == false) {
                get_signature();
            }
            g_object_name = key;
            if (filename != '') {
                suffix = get_suffix(filename);
                g_object_name = key + random_string(10) + suffix;
            }
            new_multipart_params = {
                'key': g_object_name,
                'acl': acl,
                'policy': policyBase64,
                'AWSAccessKeyId': accessid,
                'success_action_status': '200', //让服务端返回200,不然，默认会返回204
                'callback': callbackbody,
                'signature': signature
            };

            up.setOption({
                'url': host,
                'multipart_params': new_multipart_params
            });

            up.start();
        }

        //上传对象
        var uploader2 = new plupload.Uploader({
            runtimes: 'html5,flash,silverlight,html4',
            browse_button: 'selectfiles2',
            multi_selection: false,
            /*container: document.getElementById('container'),*/
            flash_swf_url: window.SWF_UPLOAD_URL,
            silverlight_xap_url: window.XAP_UPLOAD_URL,
            url: agreement +'//s3.amazonaws.com',

            filters: {
                mime_types: [ //只允许上传图片和zip,rar文件
                    {title: "Image files", extensions: "jpg,jpeg,bmp,png"}
                ],
                max_file_size: '3mb', //最大只能上传1mb的文件
                prevent_duplicates: true //不允许选取重复文件
            },

            init: {
                PostInit: function () {

                },

                FilesAdded: function (up, files) {
                    if(files[0].name.indexOf(".exe") != -1){
                        remind(remindType.error,"Upload failed. The file name contains sensitive characters.");
                        return;
                    }
                    else{
                        set_upload_param(uploader2, '', false);

                        /*删除进度条*/
                        $("#progress2").remove();

                        $("#previewDiv2").attr("src","");
                        var html = '';
                        html = "<div id='progress2' class='mb5'><div class='progress' style='margin-bottom: 8px;'><div class='progress-bar'><span class='percent'>0%</span></div></div></div>";
                        $("#progressDiv2").append(html);
                        uploader2.start();
                    }
                },

                BeforeUpload: function (up, file) {
                    set_upload_param(up, file.name, true);
                },

                UploadProgress: function (up, file) {
                    var percent = file.percent;
                    $("#progress2").find('.progress-bar').css({"width": percent + "%"});
                    $("#progress2").find(".percent").text(percent + "%");
                },

                FileUploaded: function (up, file, info) {
                    if (info.status == 200)
                    {
                        /*附件预览*/
                        var imgAddr = host +"/"+ get_uploaded_object_name(file.name);
                        $("#previewDiv2").attr("src",imgAddr);

                        /*附件参数封装*/
                        $("input[name='attachment.frontage']").attr("value",get_uploaded_object_name(file.name));

                        /*附件校验*/
                        imgCheck2();
                    }
                    else
                    {
                        remind(remindType.error,info.response);
                        imgCheck2();
                    }
                },

                Error: function (up, err) {
                    if (err.code == -600) {
                        remind(remindType.error,"The picture you uploaded is too big. Please upload a picture within 3M size.");
                    }
                    else if (err.code == -601) {
                        remind(remindType.error,"The suffix of the file is incorrect, the picture only accept the following formats: jpg, png, jpeg, bmp.");
                    }
                    else if (err.code == -602) {
                        remind(remindType.error,"The file is uploaded repeatedly.");
                    }
                    else {
                        remind(remindType.error,err.response);
                    }
                }
            }
        });

        var uploader3 = new plupload.Uploader({
            runtimes: 'html5,flash,silverlight,html4',
            browse_button: 'selectfiles3',
            multi_selection: false,
            /*container: document.getElementById('container'),*/
            flash_swf_url: window.SWF_UPLOAD_URL,
            silverlight_xap_url: window.XAP_UPLOAD_URL,
            url: agreement + '//s3.amazonaws.com',

            filters: {
                mime_types: [ //只允许上传图片和zip,rar文件
                    {title: "Image files", extensions: "jpg,jpeg,bmp,png"}
                ],
                max_file_size: '3mb', //最大只能上传1mb的文件
                prevent_duplicates: true //不允许选取重复文件
            },

            init: {
                PostInit: function () {

                },

                FilesAdded: function (up, files) {
                    if(files[0].name.indexOf(".exe") != -1){
                        remind(remindType.error,"Upload failed. The file name contains sensitive characters.");
                        return;
                    }
                    else{
                        set_upload_param(uploader3, '', false);

                        /*删除进度条*/
                        $("#progress3").remove();

                        $("#previewDiv3").attr("src","");
                        var html = '';
                        html = "<div id='progress3' class='mb5'><div class='progress' style='margin-bottom: 8px;'><div class='progress-bar'><span class='percent'>0%</span></div></div></div>";
                        $("#progressDiv3").append(html);
                        uploader3.start();
                    }
                },

                BeforeUpload: function (up, file) {
                    set_upload_param(up, file.name, true);
                },

                UploadProgress: function (up, file) {
                    var percent = file.percent;
                    $("#progress3").find('.progress-bar').css({"width": percent + "%"});
                    $("#progress3").find(".percent").text(percent + "%");
                },

                FileUploaded: function (up, file, info) {
                    if (info.status == 200)
                    {
                        /*附件预览*/
                        var imgAddr = host +"/"+ get_uploaded_object_name(file.name);
                        $("#previewDiv3").attr("src",imgAddr);

                        /*附件参数封装*/
                        $("input[name='attachment.opposite']").attr("value",get_uploaded_object_name(file.name));

                        /*附件校验*/
                        imgCheck3();
                    }
                    else
                    {
                        remind(remindType.error,info.response);
                        imgCheck3();
                    }
                },

                Error: function (up, err) {
                    if (err.code == -600) {
                        remind(remindType.error,"The picture you uploaded is too big. Please upload a picture within 3M size.");
                    }
                    else if (err.code == -601) {
                        remind(remindType.error,"The suffix of the file is incorrect, the picture only accept the following formats: jpg, png, jpeg, bmp.");
                    }
                    else if (err.code == -602) {
                        remind(remindType.error,"The file is uploaded repeatedly.");
                    }
                    else {
                        remind(remindType.error,err.response);
                    }
                }
            }
        });

        uploader2.init();
        uploader3.init();
    });
</script>
</body>
</html>
