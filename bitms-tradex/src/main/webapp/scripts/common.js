//全局语言标识
window.locale="en_US";
//https协议
var agreement = location.protocol;
//滑动验证KEY
var appKey = "FFFF000000000177330E";
//放全网站的通用js
var bitms = {
    success: 200, // ajax 成功代码
    error: 1000, // 表单提交验证错误
    gaValid: 2001,//GOOGLE 认证
    codeError: 2002, //验证码错误,找回密码发送短信中代表手机号码输入错误
    captchaError: 2007,//滑块验证码错误
    emailExists: 2010,//邮箱已存在
    noUser: 2004,//用户不存在
    unbindGA: 2024,//GOOGLE验证码未绑定
    smsError:2014,//找回密码中 短信验证码错误
    gaError:2018,//解绑ga中，GA验证码错误
    smsSendError:2011, //重复发送短信请求
    pwdError:2005,//登录密码错误
    emailCodeError:1001 //注册时邮箱验证码错误

};

//ajax全局超时错误处理
$.ajaxSetup({
 /*   timeout:5000,*/
    beforeSend:function(){
        //处理中弹框
        $('<div class="loader reg-pop"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>').appendTo("body");
        /* var loadPop = '<div class="reg-pop"><div class="rp-inner"><img src="/images/bitms/reg-pop.gif"/><div class="rpi-txt fs14 c1">loading</div></div></div>';*/
    },
    error: function (jqXHR, textStatus, errorThrown) {
       switch(jqXHR.status){
           case(500):
               remind(remindType.error, 'service error',2000);
               break;
           case(403):
               remind(remindType.error, 'access forbidden', 2000);
               location.reload();
               break;
           case(404):
               remind(remindType.error, 'can not find the source file',2000);
               break;
           default:
               remind(remindType.error, 'unknown error', 2000);
       }
    },
    complete:function(XMLHttpRequest,textStatus){
        $('.reg-pop').hide();
    }
});

//提示类型
var remindType = {
    success: "success",
    error: "error",
    normal: 'normal'
}
/**
 * 切换语言
 * @param lang
 */
function changeLanguage(lang) {
    $.ajax({
        url: '/common/changeLang',
        type: 'get',
        data: "locale=" + lang,
        dataType: 'json',
        success: function (data) {
            window.locale = lang;
            if (data.code == bitms.success) {
                window.location.reload();
            }
        }
    });
}
function changeLanguageLogin(lang) {
    $.ajax({
        url: '/common/changeLang',
        type: 'get',
        data: "locale=" + lang,
        dataType: 'json',
        success: function (data) {
            window.locale = lang;
            jumpUrl('/login');
        }
    });
}
//底部导航固定
$(function(){
    lrFixFooter("footer");
    function lrFixFooter(obj) {
        var footer = $(obj), doc = $(document);
        function fixFooter() {
            if (doc.height() - 1 <= $(window).height()) {
                footer.css({
                    width: "100%",
                    position: "absolute",
                    left: 0,
                    bottom: 0
                });
            } else {
                footer.css({
                    position: "static"
                });
            }
        }
        fixFooter();
        $(window).on('resize.footer', function () {
            fixFooter();
        });
        $(window).on('scroll.footer', function () {
            fixFooter();
        });
    }
});
/**
 * 数字修正
 *
 * @param number 数值
 * @param digit 小数位数
 * （例：如果 digit=2 number范围[0.000000001,0.00499999] 则返回 0.01,否则 按四舍五入返回）
 */
function toFiexFloat(number,digit)
{
    var ret = parseFloat(number).toFixed(digit);
    if(ret == 0 && number > ret)
    {
        ret = parseFloat(number).toFixed(digit-1)+'1';
        return ret+'';
    }
    else
    {
        return ret+'';
    }
}

//回车键绑定form提交按钮
function KeyDown(btn) {
    var theEvent = window.event || arguments.callee.caller.arguments[0];
    var code = theEvent.keyCode;
    if (code== 13) {
        theEvent.returnValue = false;
        theEvent.cancel = true;
        btn.click();
    }
}
/**
 * 根据字典编码生成下拉选项
 * @param element 元素
 * @param code 字典编码
 * @param value 选中的值
 * @param style 样式
 */
function dictDropDownOptions(element, dictCode, value, style, classStyle) {
    style = style || "";
    var locale = window.locale || "zh_CN";
    var html = "<select name='" + element + "' id='" + element + "' class='" + classStyle + "' style='" + style + "'>";
    $.each(dictionary, function (idx, obj) {
        if (dictCode == obj.code) {
            $.each(obj.children, function (cidx, cobj) {
                if (locale == cobj.lang) {
                    if (cobj.code == value) {
                        html += "<option value='" + cobj.code + "' selected>" + cobj.name + "</option>";
                    } else {
                        html += "<option value='" + cobj.code + "'>" + cobj.name + "</option>";
                    }
                }
            });
        }
    });
    html += "</select>";
    return html;
};

/**
 * 根据字典编码生成下拉选项
 * @param element 元素
 * @param code 字典编码
 * @param value 选中的值
 * @param style 样式
 */
function dictDropDownOptionsSelect(element, dictCode, value, style, classStyle,array) {
    style = style || "";
    var locale = window.locale || "zh_CN";
    var html = "<select name='" + element + "' id='" + element + "' class='" + classStyle + "' style='" + style + "'>";
    $.each(dictionary, function (idx, obj) {
        if (dictCode == obj.code) {
            $.each(obj.children, function (cidx, cobj) {
                if (locale == cobj.lang) {
                    if(array.indexOf(cobj.code)>-1)
                    {
                        if (cobj.code == value) {
                            html += "<option value='" + cobj.code + "' selected>" + cobj.name + "</option>";
                        } else {
                            html += "<option value='" + cobj.code + "'>" + cobj.name + "</option>";
                        }
                    }
                }
            });
        }
    });
    html += "</select>";
    return html;
};

/**
 * 根据字典编码取显示的名称
 * @param code
 */
function getDictValueByCode(dictCode) {
    var locale = window.locale || "en_US";
    var str = "";
    $.each(dictionary, function (idx, obj) {
        $.each(obj.children, function (cidx, cobj) {
            if (dictCode == cobj.code && locale == cobj.lang) {
                str = cobj.name;
            }
        });
    });
    return str;
}

/**
 * 安全的使用 console.log 和 console.error
 */
if (typeof window.console == 'undefined') {
    window.console = {};
    $.each(['log', 'error'], function (i, v) {
        if (typeof window.console[v] == 'undefined') {
            window.console[v] = function () {
            };
        }
    })
}

//解析
$.unparam = function (url) {
    var vars = {}, hash, i, urlParams = url.indexOf('?') > -1 ? url.split('?')[1] : url;
    var hashes = urlParams.split('&');
    for (i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars[hash[0]] = decodeURIComponent(hash[1]).replace(/\+/g, ' ');
    }
    return vars;
};

//select
$.fn.SelectBox = function () {
    //老版本
    return this.each(function () {

        if ($.browser.msie6() || $.browser.msie7()) {
            return;
        }

        var className = "";

        var oparent = $(this).parent();

        if (oparent.find("select").length > 1) {
            $(this).wrap('<div class="inline-block"></div>');
        }

        oparent = $(this).parent();

        if (!oparent.hasClass("pr")) {
            oparent.addClass("pr");
        }


        $(this).css({
            "opacity": 0,
            "position": "absolute",
            "left": 0,
            "top": 0,
            "filter": "alpha(opacity=0)"
        })

        var str = "";

        var name_val = $(this).attr("name");
        var id_val = $(this).attr("id");

        if (name_val) {
            str = name_val;
        } else {
            if (id_val) {
                str = id_val;
            } else {
                console.error("没找到select的name或者ID值");
                return;
            }
        }

        $('<div id="' + str + '_div" class="common_select_box"><span class="fn-text-overflow">' + $(this).find("option:selected").text() + '</span><i class="arrow"></i></div>').insertBefore($(this));

        //处理小箭头的位置，3为箭头的高度的一半，就相当于top:50%-(xx的一半)
        $("#" + str + "_div .arrow").css("top", $(this).outerHeight() / 2 - 3);


        $("#" + str + "_div").css({
            "width": $(this).outerWidth() - 2,
            "height": $(this).outerHeight() - 2,
            "line-height": $(this).outerHeight() - 2 + "px"
        });

        $("#" + str + "_div span").css("width", $(this).outerWidth() - 20);

        $(this).on("change", function () {
            $("#" + str + "_div span").html($(this).find("option:selected").text());
        })

    });
}

/**
 *
 * @param select css选择器
 */
function renderSelect(select) {
    if ($.type(select) == "string") {
        var name = $(select).attr("name");
        $("#" + name + "_div span").html($(select).find("option:selected").text());
    }

    if ($.type(select) == "array") {
        $.each(select, function (i, ele) {
            var name = $(ele).attr("name");
            $("#" + name + "_div span").html($(ele).find("option:selected").text());
        })
    }

    var name = $(select).attr("name");
    $("#" + name + "_div span").html($(select).find("option:selected").text());
}

//校验price
$.fn.isPrice = function (flag) {
    return this.each(function () {

        //$(this).on("keyup keydown input", function () {//会出现选中全部输入在后面追加，而不是覆盖的情况
        $(this).on("keyup change", function () {
            //如果只是数字
            if (flag == false) {
                $(this).val($(this).val().replace(/[^\d]/g, ''));
                return;
            }

            //干掉非数字
            $(this).val($(this).val().replace(/[^0-9+.?0-9]/g, ''));

            //处理折扣之类的
            var min = parseFloat($(this).data("min")),
                max = parseFloat($(this).data("max"));

            var value = parseFloat($(this).val());

            if (min && max) {
                min = Math.min(min, max);
                max = Math.max(min, max);
            }

            if (max) {
                value > max ? $(this).val('') : "";
            }
            if (min) {
                value < min ? $(this).val('') : "";
            }

        });
    });
}

//发送短信验证码倒计时
function countDown(obj, msg) {
    var time = 60;
    var validCode = true;
    var code = $(obj);
    if (validCode) {
        validCode = false;
        var t = setInterval(function () {
            time--;
            code.html(time + "s");
            code.attr('disabled', 'disabled');
            if (time == 0) {
                clearInterval(t);
                code.html(msg);
                validCode = true;
                code.removeAttr("disabled");
                $('.slider-group').show();
            }
        }, 1000)
    }
}

//注册发送邮箱验证5分钟内有效
function sendDown(obj, msg1, msg2) {
    var time = 300;
    var validCode = true;
    var code = $(obj);
    if (validCode) {
        validCode = false;
        var t = setInterval(function () {
            time--;
            code.html(msg2);
            code.attr('disabled', 'disabled').addClass('disBtn');
            if (time == 0) {
                clearInterval(t);
                code.html(msg1);
                validCode = true;
                code.removeAttr("disabled").removeClass('disBtn');
                $('.slider-group').show();
            }
        }, 1000)
    }
}

//倒计时
$.fn.cutDown = function (format, onEnd, onBefore) {

    var getDHMS = function (s) {
        var d = parseInt(s / 86400);//秒数除以（3600*24） 则可以取到天数
        s %= 86400;//取余数
        var h = parseInt(s / 3600);
        s %= 3600;
        var m = parseInt(s / 60);
        s %= 60;
        var s = parseInt(s);
        return {d: d, h: h, m: m, s: s};
    };

    if (!format) {
        format = function (dhms) {
            return (dhms.d * 24 + dhms.h) + ':' + dhms.m + ':' + dhms.s;
        };
    }

    this.each(function () {
        var $that = $(this);
        var start = +new Date();
        var end = $that.data('endtime');

        if (end <= start) {

            if (typeof onBefore === 'function') {
                onBefore();
            }

            return;
        }

        var interval = setInterval(function () {

            var remain = end - new Date();

            if (remain <= 0) {
                remain = 0;
                clearInterval(interval);

                if (typeof onEnd === 'function') {
                    onEnd();
                }
            }

            $that.html(format(getDHMS(remain / 1000), remain));

        }, 900);
    });
    return this;
};
//判断浏览器
$.browser = {
    mozilla: function () {
        return /firefox/.test(navigator.userAgent.toLowerCase());
    },
    webkit: function () {
        return /webkit/.test(navigator.userAgent.toLowerCase());
    },
    opera: function () {
        return /opera/.test(navigator.userAgent.toLowerCase());
    },
    msie: function () {
        return /msie/.test(navigator.userAgent.toLowerCase());
    },
    msie6: function () {
        return /msie 6\.0/i.test(navigator.userAgent.toLowerCase());
    },
    msie7: function () {
        return /msie 7\.0/i.test(navigator.userAgent.toLowerCase());
    },
    //竺显斌 2014-04-22
    msie8: function () {
        return /msie 8\.0/i.test(navigator.userAgent.toLowerCase());
    }
    //竺显斌 2014-04-22
};
//如果是IE6就加载png24的js文件
if ($.browser.msie6()) {
    $.getScript("/scripts/DD_belatedPNG.js", function () {
        DD_belatedPNG.fix("*");
    })
}


//获取验证码的函数
function getCodeFn(obj, num) {

    $(obj).attr("disabled", "disabled");
    var num = typeof num == "undefined" ? 60 : parseInt(num);

    function cutDown() {
        if (num == 0) {
            clearInterval(getCodeFn.timer);
            $(obj).html("获取验证码");
            $(obj).removeAttr("disabled");
            return;
        }
        $(obj).html("倒计时（" + num + "）");
        num--;
    }

    cutDown();
    getCodeFn.timer = setInterval(function () {
        cutDown();
    }, 1000);

}

//弹出提示信息
function remind(type, message, timeout, fn) {
    if ($.isFunction(timeout)) {
        fn = timeout;
        timeout = 3;
    }

    timeout = timeout || 2000;

    remindfn(type, message, timeout, fn);
}

function remindfn(type, message, timeout, fn) {
    seajs.use(["underscore", "dialog", "template", "i18n"], function (_, Dialog, template, I18n) {
        //var types = 'simple,error,warning,success,tip,loading';
        var types = 'success,error,normal';
        if (_.indexOf(types.split(','), type) == -1) {
            return;
        }
        var format = '<div class="remind-border"><div class="ui-head"><h3>'
            + 'Message</h3></div>'
            + '<div class="remind remind-{{type}}">'
            + '<span class="remind-icon {{icon}}"></span>'
            + '<span class="remind-message">{{message}}</span></div></div>';

        var render = template.compile(format);
        var html = render({
            "type": type,
            "message": message,
            "icon": (function () {
                var str = "";
                if (type == "success") {
                    str = "glyphicon glyphicon-ok text-success";
                } else if (type == "error") {
                    str = "glyphicon glyphicon-remove text-danger";
                } else if (type == "normal") {
                    str = "glyphicon glyphicon-warning-sign text-warnning";
                }
                return str;
            }())
        });
        var d = new Dialog({
            "content": html,
            "hasMask": true,
            "classPrefix": "remind-dialog",
            "closeTpl": "",
            "zIndex": "2099",
            "width": "260",
            "align":0
        });
        d.after('show', function () {
            setTimeout(function () {
                d.hide();
                if ($.isFunction(fn)) {
                    fn()
                }
            }, timeout);
        });
        d.show();
    });
}

//确认弹出框
function confirmDialog(str, fn, isClose, beforeFn, isCancel) {
    var d = null;
    var isClose = isClose || "true";
    var title = "", content = "";
    if ($.type(str) == "string") {
        content = str;
    }
    if ($.type(str) == "object") {
        title = str.title;
        content = str.content;
    }
    if (fn == null && beforeFn == null) {
        return false;
    }

    seajs.use(["dialog", "i18n"], function (Dialog, I18n) {
        if ("" == title) title = "Confirm";
        var isCancelStyle = isCancel ? "display: none" : "";
        d = new Dialog({
            "content": '<div id="confirmDialog"><div class="ui-head"><h3>'
            + title + '</h3></div><div class="confirm_bd"><div class="ta-c confirm-txt fz14 text-center">'
            + content + '</div><div class="operate text-center"><button class="confirm_btn btn btn-primary btn-sm" style="margin-right:5px;">'
            + I18n.prop("generic.confirm") + '</button>'
            + '<button class="cancel_btn btn btn-danger btn-sm" style="' + isCancelStyle + '">'
            + I18n.prop("generic.cancel") + '</button></div></div></div>',
            "zIndex": "2099",
            "width": "auto"
        });
        d.before("show", function () {
            if ($.isFunction(beforeFn)) {
                beforeFn(d);
            }
        });
        d.after("show", function () {
            $("#confirmDialog .confirm_btn").on("click", function () {
                if (isClose == "true") {
                    d.hide();
                }
                if ($.isFunction(fn)) {
                    fn(d);
                }
            });
        });
        d.show();
    });

    $(document).on("click", "#confirmDialog .cancel_btn", function () {
        d.hide();
    });

}


/**
 * 表单验证
 * @param json
 */
//[selector,isAjax,beforeSubmitFn,addElemFn]
function baseFormValidator(json) {
    // selector,isAjax,fn,editor
    var setting = {
        "isAjax": false
    }
    var config = $.extend(setting, json);
    seajs.use(["validator", "widget"], function (Validator, Widget) {
        Widget.autoRenderAll();
        var Core = Validator.query(config.selector);
        if (config.isAjax) {
            Core.set("autoSubmit", false);
        }
        if ($.isFunction(config.addElemFn)) {
            config.addElemFn(Core, Validator);
        }
        Core.on('formValidated', function (error) {
            if (!error) {
                if ($.isFunction(config.beforeSubmitFn)) {
                    config.beforeSubmitFn();
                }
            }
        });
    });
}

/**
 * 日期控件
 * @param elem
 * @param json
 * @param flag
 * @param dateFmt
 * @param minDate
 */
function baseCalendar(elem, json, flag, dateFmt, minDate) {
    seajs.use(["my97DatePicker"], function () {
        $(elem).on("click", function () {
            if (typeof flag != "undefined") {
                if (minDate == undefined) {
                    minDate = '%y-%M-%d';
                }
                var newjson = $.extend(json || {}, {minDate: minDate, dateFmt: dateFmt});
                WdatePicker(newjson);
            } else {
                WdatePicker(json || {});
            }
        })
    });
}

/**
 * 应用场景：起始日期，截止日期
 * @param start 起始的input ID
 * @param end  截止的input ID
 * @param flag  是否从今天开始
 * @param addClass  类型object 需要给input加一个类
 */
function rangeCalendar(start, end, flag, addClass) {
    doubleCalendar({
        start: start,
        end: end,
        flag: flag,
        addClass: addClass
    });
}

/**
 * 应用场景：起始日期，截止日期
 * @param start 起始的input ID
 * @param end  截止的input ID
 * @param flag  是否从今天开始
 * @param addClass  类型object 需要给input加一个类
 * @param cb 空间
 */
function rangeCalendarTime(start, end, flag, addClass, cb) {
    if (addClass) {
        $("#" + start).add("#" + end).addClass(addClass.name || "hasDatepicker");
    }

    seajs.use(["my97DatePicker"], function () {
        $("#" + start).on("focus", function () {
            if (typeof flag != "undefined") {
                WdatePicker({
                    maxDate: '#F{$dp.$D(\'' + end + '\')}',
                    minDate: '%y-%M-%d %H:%m',
                    lang: window.locale || 'en_US',
                    dateFmt: 'yyyy-MM-dd HH:mm',
                    onpicked: function (a) {
                        cb && cb(a)
                    }
                });
            } else {
                WdatePicker({
                    maxDate: '#F{$dp.$D(\'' + end + '\')}',lang: window.locale || 'en_US', dateFmt: 'yyyy-MM-dd HH:mm',
                    onpicked: function (a) {
                        cb && cb(a)
                    }
                });
            }
        })

        $("#" + end).on("focus", function () {
            WdatePicker({
                minDate: '#F{$dp.$D(\'' + start + '\')}',lang: window.locale || 'en_US', dateFmt: 'yyyy-MM-dd HH:mm', onpicked: function (a) {
                    cb && cb(a)
                }
            });
        })
    });
}

function doubleCalendar(_config) {
    var config = $.extend({
        start: "",
        end: "",
        flag: "",
        addClass: "hasDatepicker",
        format: 'yyyy-MM-dd'
    }, _config);
    if (config.addClass) {
        $("#" + config.start, "#" + config.end).addClass(config.addClass);
    }

    seajs.use(["my97DatePicker"], function () {
        $("#" + config.start).on("focus", function () {
            if (config.flag) {
                WdatePicker({
                    lang: window.locale || 'en_US',
                    maxDate: '#F{$dp.$D(\'' + config.end + '\')}',
                    minDate: '%y-%M-%d',
                    dateFmt: config.format
                });
            } else {
                WdatePicker({maxDate: '#F{$dp.$D(\'' + config.end + '\')}',lang: window.locale || 'en_US', dateFmt: config.format});
            }
        })

        $("#" + config.end).on("focus", function () {
            WdatePicker({minDate: '#F{$dp.$D(\'' + config.start + '\')}',lang: window.locale || 'en_US', dateFmt: config.format});
        })
    });
}


//重置错误信息（让错误信息隐藏）
function resetValidatorError(form) {
    var form = !form ? "" : form;
    $(form + " .ui-form-item").removeClass("ui-form-item-error");
    $(form + " .ui-form-explain").html("");
}

/**
 * 字符串截取
 * @param origText
 * @param length
 * @param ellipsisStr
 */
function trimLongString(origText, len, ellipsisStr) {
    var newText = "";
    var ellipsis = ellipsisStr || "...";
    if ($.type(origText) == "string") {
        newText = origText;
    }
    if (len > 0 && len < newText.length) {
        newText = newText.slice(0, len) + ellipsis;
    }

    return newText;
}

/**
 * 字符串截取
 * @param origText
 * @param length
 * @param ellipsisStr
 * 例YYYY年MM月DD日 HH:II:SS
 */
function form_unixtime(unixtime, format) {
    var unixtimestamp = new Date(unixtime);
    return format.replace("YYYY", unixtimestamp.getFullYear()).replace("MM", unixtimestamp.getMonth() + 1).replace("DD", unixtimestamp.getDate()).replace("HH", unixtimestamp.getHours()).replace("II", unixtimestamp.getMinutes()).replace("SS", unixtimestamp.getSeconds());
}

function form_unixtimeFull(unixtime, format) {
    function fill_zero(i) {
        if (i < 10) i = "0" + i;
        return i;
    }

    var unixtimestamp = new Date(unixtime);
    return format.replace("YYYY", unixtimestamp.getFullYear())
        .replace("MM", fill_zero(unixtimestamp.getMonth() + 1)).replace("DD", fill_zero(unixtimestamp.getDate()))
        .replace("HH", fill_zero(unixtimestamp.getHours())).replace("II", fill_zero(unixtimestamp.getMinutes()))
        .replace("SS", fill_zero(unixtimestamp.getSeconds()));
}

/**
 * 判断是否是金额
 * @param str 具体的金额值
 * @returns {boolean}
 */
function isAmount(str) {
    return /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/.test(str);
}


// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2014-05-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2014-5-2 8:9:4.18
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 跳转到指定的地址
 * @param url
 */
function jumpUrl(url) {
    window.location.href = url;
}

function getZero(d)
{
    var ret = "";
    for(var i = 0;i < d;i ++)
    {
        ret +="0";
    }
    return ret;
}
/**
 *  科学计数法转字符串
 *  3e-8  = 0.00000003
 * @param num
 * @returns {*}
 */
function scientificToNumber(num) {
    var str = num.toString().toLowerCase();
    var key = str.split("e");
    if (key[1] == null || key[1] == undefined) {
        return key[0];
    }
    if(key[1]*1 > 0)
    { // 1.2e8
        var p = key[0].split(".");
        if (p[1] == null || p[1] == undefined) {
            p[1]="";
        }
        return (key[0]*1).toString().replace(".","")+getZero(key[1]*1-p[1].length);
    }
    else
    { // 1.2e-8
        var p = key[0].split(".");
        if (p[1] == null || p[1] == undefined) {
            p[1]="";
        }
        return "0."+getZero(key[1]*-1-key[0].replace(".","").length)+key[0].replace(".","");
    }
}
function comdify(n){
    var re=/\d{1,3}(?=(\d{3})+$)/g;
    var n1=n.replace(/^(\d+)((\.\d+)?)$/,function(s,s1,s2){return s1.replace(re,"$&,")+s2;});
    return n1;
}
/**
 * 精度处理
 * @param d
 * @returns {string}
 */
Number.prototype.toFixed = function(d) {

    var num = this;
    if(isNaN(this)){num = 0;}
    var times = Math.pow(10, d)
    if (num.toString().indexOf("e") > -1 || num.toString().indexOf("E") > -1) {
        num = scientificToNumber(num);
    }
    var des = num * times + 0.5
    des = parseInt(des, 10) / times
    var ret = scientificToNumber(des)+"";
    var key = (scientificToNumber(des)+"").split(".");
    if (key[1] == null || key[1] == undefined) {
        if(d == 0) return des +'';
        return ret + '.' +  getZero(d)

    }
    else {
        return ret + ''+  getZero(d-key[1].length)
    }



    /* var value = this + "";
     if(isNaN(this)){value = "0";}
     var newstr = "";
     if (value.indexOf("e") > -1 || value.indexOf("E") > -1) {
     newstr = scientificToNumber(value);
     }
     else {
     newstr = value;
     }
     var key = newstr.split(".");
     if (key[1] == null || key[1] == undefined) {
     key[1] = '';
     }
     var oldkey1=key[1];
     d = d * 1;
     if ((key[1] + "").length > d+1) {
     key[1] = key[1].substring(0, d+1);
     }
     else {
     key[1] = key[1] + getZero(d+1 - key[1].length);
     }
     if(key[1] == "")
     {
     return key[0].substring(0,d);
     }
     else
     {
     key[1]=Math.round((key[1]*1/10)).toString();
     if(key[1].substring(0,1) == '1' && key[1].length>d)
     {
     key[1]= key[1].substring(1,d+1);
     key[0]= (key[0]*1+1).toString();
     }
     else
     {
     key[1] = oldkey1+getZero(d - oldkey1.length) ;
     }
     return key[0] + "." + key[1];
     }*/
};