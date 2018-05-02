<%@ page import="com.blocain.bitms.security.OnLineUserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-xs-12 marginBottom-xs-none" style="background-color:#337ab7;margin-bottom:10px;">
    <nav class="navbar navbar-default" role="navigation" style="background-image:none;background-color:#337ab7;margin-bottom:0;margin-top:5px;border:none;box-shadow:none;">
        <div class="navbar-header" style="background-color:#337ab7;">
            <div class="pull-right clearfix visible-xs">
                <c:choose>
                    <c:when test="<%=OnLineUserUtils.getUnid()==null%>">
                        <span style="line-height:30px;font-size:16px;">
                            <a href="/login" style="cursor:pointer;color:#fff">Log in</a>
                            <span style="color:#fff">|</span>
                            <a href="/register" style="cursor:pointer;color:#fff">Sign up</a>
                        </span>
                    </c:when>
                    <c:otherwise>
                        <div class="dropdown" style="margin-top:3px;cursor:pointer;">
                            <button style="background-color:transparent;" type="button" class="navbar-toggle" id="dLabelMobile" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span class="sr-only">Toggle navigation</span>
                                <span class="icon-bar" style="background-color:#fff"></span>
                                <span class="icon-bar" style="background-color:#fff"></span>
                                <span class="icon-bar" style="background-color:#fff"></span>
                            </button>
                            <ul class="dropdown-menu" aria-labelledby="dLabelMobile" style="margin-top:10px;">
                                <li style="text-align:center">
                                    <span><%=OnLineUserUtils.getEmail() %></span>
                                </li>
                                <hr style="margin:5px;">
                                <li style="margin:5px;">
                                    <button onclick="window.location.href='/wallet'" class="btn btn-sm btn-block btn-warning" style="text-align:left;">
                                        <span class="glyphicon glyphicon-piggy-bank"></span>
                                        <span style="padding:4px;">|</span>
                                        <span>Funds</span>
                                    </button>
                                </li>
                                <li style="margin:5px;">
                                    <button onclick="window.location.href='/changeLoginPwd'" class="btn btn-sm btn-block btn-info" style="text-align:left;">
                                        <span class="glyphicon glyphicon-lock"></span>
                                        <span style="padding:4px;">|</span>
                                        <span>Change Password</span>
                                    </button>
                                </li>
                                <li style="margin:5px;">
                                    <button onclick="window.location.href='/invitationCode'" class="btn btn-sm btn-block btn-primary" style="text-align:left;">
                                        <span class="glyphicon glyphicon-tag"></span>
                                        <span style="padding:4px;">|</span>
                                        <span>My Invitation Code</span>
                                    </button>
                                </li>
                                <li style="margin:5px;">
                                    <button onclick="window.location.href='/getCandy'" class="btn btn-sm btn-block btn-primary" style="text-align:left;background-color:#ffb0b0;">
                                        <span class="glyphicon glyphicon-gift"></span>
                                        <span style="padding:4px;">|</span>
                                        <span>Candy</span>
                                    </button>
                                </li>
                                <c:choose>
                                    <c:when test="<%=OnLineUserUtils.getAuthKey()==null %>">
                                        <li style="margin:5px;">
                                            <button onclick="window.location.href='/bindGA'" class="btn btn-sm btn-block btn-success" style="text-align:left;">
                                                <span class="glyphicon glyphicon-phone"></span>
                                                <span style="padding:4px;">|</span>
                                                <span>Google Authenticator</span>
                                            </button>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li style="margin:5px;">
                                            <button onclick="window.location.href='/unbindGA'" class="btn btn-sm btn-block btn-success" style="text-align:left;">
                                                <span class="glyphicon glyphicon-phone"></span>
                                                <span style="padding:4px;">|</span>
                                                <span>Google Authenticator</span>
                                            </button>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                                <li style="margin:5px;">
                                    <button id="LogoutMobile" class="btn btn-sm btn-block btn-danger" style="text-align:left;">
                                        <span class="glyphicon glyphicon-off"></span>
                                        <span style="padding:4px;">|</span>
                                        <span>Logout</span>
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <a class="clearfix" style="cursor:pointer;line-height:30px;display:inline-block;" href="/market">
                <img id="Logo" style="width:230px;margin:5px auto;" class="img-responsive" src="${imagesPath}/common/logo.svg" alt="biex">
            </a>
        </div>
        <div class="clearfix collapse navbar-collapse in" style="background-color:#337ab7;padding:0;border:none;">
            <ul class="pull-left float-xs-none hidden-xs" style="padding:1px;margin-bottom:0;">
                <c:choose>
                    <c:when test="${label == true}">
                        <li class="list-group-item pull-left float-xs-none" style="border:none;padding:0;margin-top:15px;background-color:#337ab7;z-index:1;">
                            <a onMouseOver="this.style.textDecoration='none'" target="_blank" href="https://etherscan.io/token/${ERC20Contract}">
                                <span style="padding:2px;border-radius:2px;border:1px solid #fff;color:#fff;margin-right:2px;"><c:if test="${fn:length(symbolName)>15 }">${fn:substring(symbolName,0,15)}...</c:if><c:if test="${fn:length(symbolName)<=15 }">${symbolName}</c:if>(${symbol}):${fn:substring(ERC20Contract,0,10)}...${fn:substring(ERC20Contract,34,42)}</span>
                            </a>
                            <c:choose>
                                <c:when test="${activeDays > 0}">
                                    <span style="padding:2px;border-radius:2px;border:1px solid #fff;color:#fff;"><span class="glyphicon glyphicon-time"></span>&nbsp;<span id="date">${activeDays}</span><span>Day</span></span>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </ul>
            <ul class="list-group pull-right float-xs-none" style="margin-bottom:10px;box-shadow:none;">
                <c:choose>
                    <c:when test="<%=OnLineUserUtils.getUnid()==null%>">
                        <li class="list-group-item pull-left float-xs-none padding-xs-top text-center hidden-xs" style="border:none;padding:10px 4px 0;background-color:#337ab7;color:#fff;">
                        <span style="line-height:30px;">
                            <a href="/login" style="cursor:pointer;color:#fff">Log in</a>
                            <span style="color:#fff">or</span>
                            <a href="/register" style="cursor:pointer;color:#fff">Sign up</a>
                        </span>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="list-group-item pull-left float-xs-none padding-xs-top text-center hidden-xs" style="border:none;padding:10px 4px 0;background-color:#337ab7;color:#fff;">
                            <div class="dropdown" style="margin-top:5px;cursor:pointer;">
                            <span id="dLabel" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span><%=OnLineUserUtils.getEmail() %></span>
                                <span class="caret"></span>
                            </span>
                                <button onclick="window.location.href='/wallet'" class="btn btn-sm btn-warning" style="margin-left:5px;text-align:left;margin-top:-4px;outline:none;">
                                    <span class="glyphicon glyphicon-piggy-bank"></span>
                                    <span style="padding:2px;">|</span>
                                    <span>Funds</span>
                                </button>
                                <ul class="dropdown-menu" aria-labelledby="dLabel" style="margin-top:10px;">
                                    <li style="margin:5px;">
                                        <button onclick="window.location.href='/changeLoginPwd'" class="btn btn-sm btn-block btn-info" style="text-align:left;">
                                            <span class="glyphicon glyphicon-lock"></span>
                                            <span style="padding:4px;">|</span>
                                            <span>Change Password</span>
                                        </button>
                                    </li>
                                    <li style="margin:5px;">
                                        <button onclick="window.location.href='/invitationCode'" class="btn btn-sm btn-block btn-primary" style="text-align:left;">
                                            <span class="glyphicon glyphicon-gift"></span>
                                            <span style="padding:4px;">|</span>
                                            <span>My Invitation Code</span>
                                        </button>
                                    </li>
                                    <li style="margin:5px;">
                                        <button onclick="window.location.href='/getCandy'" class="btn btn-sm btn-block btn-primary" style="text-align:left;background-color:#ffb0b0;">
                                            <span class="glyphicon glyphicon-gift"></span>
                                            <span style="padding:4px;">|</span>
                                            <span>Candy</span>
                                        </button>
                                    </li>
                                    <c:choose>
                                        <c:when test="<%=OnLineUserUtils.getAuthKey()==null %>">
                                            <li style="margin:5px;">
                                                <button onclick="window.location.href='/bindGA'" class="btn btn-sm btn-block btn-success" style="text-align:left;">
                                                    <span class="glyphicon glyphicon-phone"></span>
                                                    <span style="padding:4px;">|</span>
                                                    <span>Google Authenticator</span>
                                                </button>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li style="margin:5px;">
                                                <button onclick="window.location.href='/unbindGA'" class="btn btn-sm btn-block btn-success" style="text-align:left;">
                                                    <span class="glyphicon glyphicon-phone"></span>
                                                    <span style="padding:4px;">|</span>
                                                    <span>Google Authenticator</span>
                                                </button>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                    <li style="margin:5px;">
                                        <button id="Logout" class="btn btn-sm btn-block btn-danger" style="text-align:left;">
                                            <span class="glyphicon glyphicon-off"></span>
                                            <span style="padding:4px;">|</span>
                                            <span>Logout</span>
                                        </button>
                                    </li>
                                </ul>
                            </div>
                        </li>
                    </c:otherwise>
                </c:choose>
                <li class="list-group-item pull-left float-xs-none padding-xs-top text-center" style="border:none;padding:10px 4px 0;background-color:#337ab7;">
                    <div class="input-group" style="display: inline;">
                        <input id="txtSearchInput" type="text" class="form-control-custom ui-autocomplete-input"
                               placeholder="Search by Contact Address,Token Symbol or Name" name="tokens" maxlength="80"
                               style="width:315px;height:32px;padding-left:5px;" autocomplete="off">
                        <span class="btn btn-primary" style="border-radius:0 4px 4px 0;cursor:default;background-color:#3498db;box-shadow:none;background-image:none;border:none;margin-left:-7px;margin-top:-2px;" type="submit">
                        <span class="glyphicon glyphicon-search"></span>
                    </span>
                    </div>
                </li>
            </ul>
        </div>
    </nav>
</div>
<script>
    ///开始定义全局内容
    var fouce_li_num = -1;///默认没有选择任何下拉内容
    var width_ = 330;//这里设置的是搜索框的宽度，目的为了与下面的列表宽度相同
    var li_color = "#fff";//默认的下拉背景颜色
    var li_color_ = "#CCC";//当下拉选项获取焦点后背景颜色
    var list;
    String.prototype.endWith = function (str) {
        if (str == null || str == "" || this.length == 0 || str.length > this.length) {
            return false;
        }
        if (this.substring(this.length - str.length)) {
            return true;
        } else {
            return false;
        }
        return true;
    };

    String.prototype.startWith = function (str) {
        if (str == null || str == "" || this.length == 0 || str.length > this.length) {
            return false;
        }
        if (this.substr(0, str.length) == str) {
            return true;
        } else {
            return false;
        }
        return true;
    };
    $(function () {

       /* $.ajax({
            cache: true,
            type: 'POST',
            url: "${ctx}/exchange/getTokens",
            data: {},
            success: function (data, textStatus, jqXHR) {
                list = JSON.parse(data);
            }
        });*/

        $("input[name=tokens]").keyup(function (event) {
            var keycode = event.keyCode;
            if (delkeycode(keycode))return;
            var key_ = $(this).val();//获取搜索值
            var top_ = $(this).offset().top;//获搜索框的顶部位移
            var left_ = $(this).offset().left;//获取搜索框的左边位移
            if (keycode == 13) {//enter search
                if (fouce_li_num >= 0) {
                    location.href = "${ctx}/exchange?contractAddr=" + $("#foraspcn >li:eq(" + fouce_li_num + ")").attr("contractAddr");
                } else {
                    /////当没有选中下拉表内容时 则提交form 这里可以自定义提交你的搜索
                }
                $("#foraspcn").hide();
            } else if (keycode == 40) {//单击键盘向下按键
                fouce_li_num++;
                var li_allnum = $("#foraspcn >li").css("background-color", li_color).size();
                if (fouce_li_num >= li_allnum && li_allnum != 0) {//当下拉选择不为空时
                    fouce_li_num = 0;
                } else if (li_allnum == 0) {
                    fouce_li_num--;
                    return;
                }
                $("#foraspcn >li:eq(" + fouce_li_num + ")").css("background-color", li_color_);
            } else if (keycode == 38) {//点击键盘向上按键
                fouce_li_num--;
                var li_allnum = $("#foraspcn >li").css("background-color", li_color).size();
                if (fouce_li_num < 0 && li_allnum != 0) {//当下拉选择不为空时
                    fouce_li_num = li_allnum - 1;
                } else if (li_allnum == 0) {
                    fouce_li_num++;
                    return;
                }
                $("#foraspcn >li:eq(" + fouce_li_num + ")").css("background-color", li_color_);
            } else {//进行数据查询，显示查询结果
                fouce_li_num = -1;
                $("#foraspcn").empty();
                ajax_getdata(key_);//如果使用ajax去前面的demo和//
                //赋值完毕后进行显示
                $("#foraspcn").show().css({"top": top_ + 38, "left": left_});
            }
        });
        //当焦点从搜索框内离开则，隐藏层
        $("body").click(function () {
            $("#foraspcn").hide();
        });
        ///创建隐藏的div，用来显示搜索下的内容
        $("body").append("<div id='foraspcn'></div>");
        $("#foraspcn").css({
            "width": "" + width_ + "px",
            "position": "absolute",
            "z-index": "999",
            "list-style": "none",
            "border": "solid #E4E4E4 1px",
            "display": "none",
            "top": "56.5px",
            "background-color": "#fff",
            "max-height": "500px",
            "overflow": "auto"
        });//这里设置列下拉层的样式，默认为隐藏的
    });
    //定义非开始运行函数
    function delkeycode(keycode) {//去除了不必要的按键反应，当比如删除，f1 f2等按键时，则返回
        var array = new Array();
        array = [16, 19, 20, 27, 33, 34, 35, 36, 45, 46, 91, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 145, 192];
        for (i = 0; i < array.length; i++) {
            if (keycode == array[i]) {
                return true;
                break;
            }
        }
        return false;
    }
    function myTrim(x) {
        return x.replace(/^\s+|\s+$/gm, '');
    }
    function dropMenu(key,data)
    {
        if (data.code == bitms.success) {
            var data_array = data.object;
            var keyvalue;
            if(key.length<2)return;
            var cnt = 0;
            for (i = 0; i < data_array.length; i++)//这里进行数据附加 返回数据格式为 关键词数组
            {
                if (key.startWith("0x")) {
                    keyvalue = data_array[i].contractAddr;
                }
                else {
                    keyvalue = data_array[i].symbol+"-"+data_array[i].symbolName;
                }
                if ((keyvalue.toLocaleLowerCase() + "").indexOf((key.toLocaleString()) + "") != -1) {
                    cnt +=1;
                    var sty = " glyphicon-lock text-danger ";
                    if (data_array[i].isActive == "yes") {
                        sty = " glyphicon-transfer text-success ";
                    }
                    $("#foraspcn").append("<li style='width:328px;padding:5px;cursor:pointer;font-size:12px;' contractAddr = '" + data_array[i].contractAddr + "'><span style='color:#3498db;display:block;font-weight:bold;'>" + data_array[i].symbolName + "（" + data_array[i].symbol + "）<span class='glyphicon " + sty + " pull-right' style='margin-right:20px;'></span></span><span>TOKEN:" + data_array[i].contractAddr + "</span></li>");
                    $("#foraspcn >li").mouseover(function () {
                        $(this).css("background-color", li_color_);
                    });
                    $("#foraspcn >li").mouseout(function () {
                        $(this).css("background-color", li_color);
                    });
                    $("#foraspcn >li").click(function () {
                        $("input[name=tokens]").val($.trim($(this).text()));
                        $(this).parent().hide();
                        location.href = "${ctx}/exchange?contractAddr=" + $(this).attr("contractAddr");
                    });
                }
                if(cnt == 100){
                    break;
                }
            }
        }
        else {}
    }

    ////////////////这里是正式的ajax调用
    function ajax_getdata(key) {
        $("#foraspcn").html("");
        key = myTrim(key).toLowerCase();
        if (key.startWith("0x")) {
            $.ajax({
                cache: true,
                type: 'POST',
                async: false,//使用同步的方式,true为异步方式
                url: "${ctx}/exchange/getTokens",
                data: {contractAddr:key},
                success: function (data, textStatus, jqXHR) {
                    list = JSON.parse(data);
                    dropMenu(key,list);
                }
            });
        }
        else {
            if(key.length>=2)
            {
                $.ajax({
                    cache: true,
                    type: 'POST',
                    async: false,//使用同步的方式,true为异步方式
                    url: "${ctx}/exchange/getTokens",
                    data: {symbol:key},
                    success: function (data, textStatus, jqXHR) {
                        list = JSON.parse(data);
                        dropMenu(key,list);
                    }
                });
            }
        }

    }

    $('#Logout').on('click', function () {
        $.ajax({
            url: '/common/logout',
            type: 'post',
            dataType: 'json',
            success: function (json) {
                if (json.code == bitms.success) {
                    window.location.href = "/exchange";
                }
            }
        });
    });
    $('#LogoutMobile').on('click', function () {
        $.ajax({
            url: '/common/logout',
            type: 'post',
            dataType: 'json',
            success: function (json) {
                if (json.code == bitms.success) {
                    window.location.href = "/exchange";
                }
            }
        });
    });
</script>
