<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<html>
<body>
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp" %>
    <%--代码开始--%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <%--<div class="col-sm-2 column">
            <ul class="nav nav-tabs nav-stacked">
                <li>
                    <a href="${ctx}/account/setting">
                        <span class="glyphicon glyphicon-lock"></span>
                        <span>安全中心</span>
                    </a>
                </li>
                &lt;%&ndash;<li>
                    <a href="${ctx}/account/baseInfo">
                        <span class="glyphicon glyphicon-plus"></span>
                        <span>基本信息</span>
                    </a>
                </li>&ndash;%&gt;
                </li>
                <li>
                    <a href="${ctx}/account/certification">
                        <span class="glyphicon glyphicon-user"></span>
                        <span>身份认证</span>
                    </a>
                </li>
                <li>
                    <a href="${ctx}/common/message">
                        <span class="glyphicon glyphicon-envelope"></span>
                        <span>我的消息</span>
                    </a>
                </li>
                <li class="active">
                    <a href="${ctx}/common/feedback">
                        <span class="glyphicon glyphicon-question-sign"></span>
                        <span>问题反馈</span>
                    </a>
                </li>
            </ul>
        </div>--%>
        <div class="col-sm-12 column">
            <%--开始代码位置--%>
            <div class="panel">
                <div class="row">
                    <div class="col-lg-9 col-md-10">
                        <form:form id="feedbackForm" class="form-horizontal" role="form">
                            <div class="form-group ui-form-item">
                                <label class="col-sm-2 control-label"><%--问题描述--%><fmt:message key="feedback.description" />：</label>
                                <div class="col-sm-6">
                                    <textarea name="describe" style="resize: vertical" class="form-control" rows="6" data-display="<%--问题描述--%><fmt:message key="feedback.description" />"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 col-xs-12 control-label"><%--上传附件--%><fmt:message key="feedback.attachments" />：</label>
                                <div class="col-sm-2 col-xs-6">
                                    <a id="selectfiles" href="javascript:void(0);" class='btn btn-primary btn-block'><%--点击上传--%><fmt:message key="feedback.attachments" /></a>
                                </div>
                                <input type="hidden" name="attachments" />
                                <ul class="col-sm-3 col-xs-8" id="previewUl"></ul>
                            </div>
                            <div class="form-group ui-form-item">
                                <label class="col-sm-2 col-xs-12 control-label"><%--姓名--%><fmt:message key="feedback.name" />：</label>
                                <div class="col-sm-3 col-xs-8">
                                    <input type="text" class="form-control" name="trueName" placeholder="" data-display="<%--姓名--%><fmt:message key="feedback.name" />"></div>
                            </div>
                            <div class="form-group ui-form-item">
                                <label class="col-sm-2 col-xs-12 control-label"><%--联系电话--%><fmt:message key="feedback.phone" />：</label>
                                <div class="col-sm-3 col-xs-8">
                                    <input type="text" class="form-control" name="contactNumber" placeholder="" data-display="<%--联系电话--%><fmt:message key="feedback.phone" />"></div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-3 col-xs-8">
                                    <button id="feedbackSubmit" type="button" class="btn btn-primary btn-block"><%--提交--%><fmt:message key="submit" /></button>
                                </div>
                            </div>
                        </form:form>
                    </div>
                    <div class="col-md-4"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/global/setup_ajax.jsp"></jsp:include>
<script>
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
    var attachmentArrow = [];

    seajs.use(['validator', 'plupload'], function (Validator) {
        $('.pageLoader').hide();
        //提交问题反馈
        validator = new Validator();
        $("#feedbackSubmit").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#feedbackForm',
                autoSubmit: false,  <%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '${ctx}/common/feedback/submit',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend:function(){
                                $('.reg-pop').fadeIn();
                                $("#feedbackSubmit").attr("disabled", true);
                            },
                            success: function (data, textStatus, jqXHR) {
                                if (data.code == bitms.success) {
                                    remind(remindType.success, data.message,300,function(){
                                        location.reload();
                                    });
                                }
                                else {
                                    remind(remindType.error, data.message,300);
                                }
                                $('#feedbackForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                setCsrfToken("csrf-form");
                            },
                            complete:function(){
                                $('.reg-pop').fadeOut();
                                $("#feedbackSubmit").attr("disabled", false);
                            }
                        });
                    }
                }
            }).addItem({
                element: '#feedbackForm [name=describe]',
                required: true
            }).addItem({
                element: '#feedbackForm [name=trueName]',
                required: true
            }).addItem({
                element: '#feedbackForm [name=contactNumber]',
                required: true
            });
            $("#feedbackForm").submit();
        });

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
        };

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
        var uploader = new plupload.Uploader({
            runtimes: 'html5,flash,silverlight,html4',
            browse_button: 'selectfiles',
            /*multi_selection: false,*/
            flash_swf_url: window.SWF_UPLOAD_URL,
            silverlight_xap_url: window.XAP_UPLOAD_URL,
            url: agreement +'//s3.amazonaws.com',

            filters: {
                mime_types: [ //只允许上传图片和zip,rar文件
                    {title: "Image files", extensions: "jpg,jpeg,bmp,png"}
                ],
                max_file_size: '2mb', //最大只能上传10mb的文件
                prevent_duplicates: true //不允许选取重复文件
            },

            init: {
                PostInit: function () {

                },

                FilesAdded: function (up, files) {
                    set_upload_param(uploader, '', false);

                    if ($("#previewUl").children("li").length > 5) {
                        alert("您上传的图片太多了！");
                        uploader.destroy();
                    } else {
                        var li = '';
                        plupload.each(files, function(file) { //遍历文件
                            li += "<li id='" + file['id'] + "' class='mb5'><div class='progress' style='margin-bottom: 8px;'><div class='progress-bar'><span class='percent'>0%</span></div></div></li>";
                        });
                        $("#previewUl").append(li);
                        uploader.start();
                    }

                },

                BeforeUpload: function (up, file) {
                    set_upload_param(up, file.name, true);
                },

                UploadProgress: function (up, file) {
                    var percent = file.percent;
                    $("#" + file.id).find('.progress-bar').css({"width": percent + "%"});
                    $("#" + file.id).find(".percent").text(percent + "%");
                },

                FileUploaded: function (up, file, info) {
                    if (info.status == 200)
                    {
                        //console.log(get_uploaded_object_name(file.name));

                        /*附件预览*/
                        var imgAddr = host +"/"+ get_uploaded_object_name(file.name);
                        $("#" + file.id).append("<img class='img-responsive' src='"+imgAddr+"'/><div class='clearfix'><span style='cursor: pointer' class='glyphicon glyphicon-trash mt5 mb5 imgRemove pull-right'></span><span class='text-success pull-left'>Success!</span></div>");
                        $('.imgRemove').on("click",function () {
                            $(this).parent().parent().remove();
                        });

                        /*附件参数封装*/
                        attachmentArrow.push(get_uploaded_object_name(file.name));
                        $("input[name='attachments']").attr("value",attachmentArrow);
                    }
                    else
                    {
                        console.log(info.response);
                    }
                },

                Error: function (up, err) {
                    if (err.code == -600) {
                        console.log("选择的文件太大了,可以根据应用情况，在upload.js 设置一下上传的最大大小");
                    }
                    else if (err.code == -601) {
                        console.log("选择的文件后缀不对,可以根据应用情况，在upload.js进行设置可允许的上传文件类型");
                    }
                    else if (err.code == -602) {
                        console.log("这个文件已经上传过一遍了");
                    }
                    else {
                        console.log(err.response);
                    }
                }
            }
        });

        uploader.init();

    });
</script>
</body>
</html>
