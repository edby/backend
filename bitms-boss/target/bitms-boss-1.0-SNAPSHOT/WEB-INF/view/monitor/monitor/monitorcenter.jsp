<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="detail.jsp" %>
<style>
    .monitor-panel {
        width: auto;
        margin: 20px;
        /*min-width: 960px;*/
        min-width: 1070px;
        border: solid 1px #eee;
        margin-top: 20px;
        padding: 20px;
        padding-right: 0;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        height: auto;
        overflow: hidden;
    }

    .monitor-company {
        /*width: 50%;*/
        width: 33.33%;
        padding-right: 20px;
        float: left;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
    }

    .monitor-con {
        border: solid 1px #eee;
        padding: 20px;
        line-height: 30px;
    }

    .monitor-tit {
        font-size: 18px;
        text-align: center;
        border-bottom: solid 1px #eee;
        background: #eee;
        line-height: 46px;
        margin-bottom: 30px;
    }

    .mt20 {
        margin-top: 20px;
    }

    .monitor-state {
        font-size: 20px;
        padding: 0 20px;
        min-width: 145px;
        display: inline-block;
        box-sizing: border-box;
        text-align: left;
        height: 33px;
    }

    .monitor-link {
        font-size: 15px;
        text-align: right;
        display: block;
        color: green;
    }

    .monitor-label {
        font-size: 16px;
        line-height: 30px;
        margin: 0 auto;
        text-align: center;
        margin-bottom: 10px;
    }

    .monitor-num {
        font-size: 14px;
        color: red;
    }

    .dn {
        display: none;
    }

    .cred {
        color: red;
    }

    .cgreen {
        color: green;
    }

    .monitor-icon {
        font-size: 25px;
        padding: 0 5px;
        vertical-align: middle;
    }

    .fs24 {
        font-size: 24px;
    }

    .fs20 {
        font-size: 20px;
    }

    .fs14 {
        font-size: 14px;
    }

    .hand {
        cursor: pointer;
    }

    .floatL {
        float: left;
    }

    .floatR {
        float: right;
    }

    .add-height {
        padding-top: 28px;
        padding-bottom: 15px;
    }

    .alarm {
        position: absolute;
        left: 50%;
        top: 31px;
        cursor: pointer;
        margin-left: -25px;
        display: none;
    }

    .alarm i {
        font-size: 38px;
    }

    /*shake*/
    @-webkit-keyframes shake {
        0% {
            -webkit-transform: translate(2px, 2px);
        }
        25% {
            -webkit-transform: translate(-2px, -2px);
        }
        50% {
            -webkit-transform: translate(0px, 0px);
        }
        75% {
            -webkit-transform: translate(2px, -2px);
        }
        100% {
            -webkit-transform: translate(-2px, 2px);
        }
    }

    @keyframes shake {
        0% {
            transform: translate(2px, 2px);
        }
        25% {
            transform: translate(-2px, -2px);
        }
        50% {
            transform: translate(0px, 0px);
        }
        75% {
            transform: translate(2px, -2px);
        }
        100% {
            transform: translate(-2px, 2px);
        }
    }

    .shake-on {
        -webkit-animation: shake 0.2s infinite;
        animation: shake 0.2s infinite;
        display: block;
    }

    .shake-on .fi-volume {
        color: red;
    }
</style>

<div class="monitor-panel">
    <audio src="${res}/style/images/alarm.mp3" controls="controls" loop preload id="auto" hidden></audio>
    <div href="#" class="item alarm">
        <i class="fi-volume"></i>
    </div>
    <div class="monitor-company">
        <div class="monitor-con">
            <div class="monitor-tit">内部总账监控</div>
            <div class="monitor-label add-height">监控状态 ：
                <span class="monitor-state" id="matchfundNormal"><i
                        class="fi-social-foursquare monitor-icon cgreen"></i>正常</span>
                <span class="monitor-state dn cred" id="matchfundAbnormal"><i
                        class="fi-alert monitor-icon cred fs24"></i>异常<span class="monitor-num">【<span
                        id="matchfundAbnormalNum"></span>】</span></span></div>
            <div class="monitor-link hand" onclick="detailFun('matchfund')">查看详情</div>
        </div>
    </div>
    <div class="monitor-company">
        <div class="monitor-con">
            <div class="monitor-tit">账户资产监控</div>
            <div class="monitor-label add-height">监控状态 ：
                <span class="monitor-state" id="acctfundcurNormal"><i
                        class="fi-social-foursquare monitor-icon cgreen"></i>正常</span>
                <span class="monitor-state cred dn" id="acctfundcurAbnormal"><i
                        class="fi-alert monitor-icon cred fs24"></i>异常<span class="monitor-num">【<span
                        id="acctfundcurAbnormalNum"></span>】</span></span>
            </div>
            <div class="monitor-link hand" onclick="detailFun('acctfundcur')">查看详情</div>
        </div>
    </div>
    <div class="monitor-company">
        <div class="monitor-con">
            <div class="monitor-tit">保证金监控</div>
            <div class="monitor-label">多头账户状态 ：
                <span class="monitor-state" id="marginBullNormal"><i
                        class="fi-social-foursquare monitor-icon cgreen"></i>正常</span>
                <span class="monitor-state dn cred" id="marginBullAbnormal"><i
                        class="fi-alert monitor-icon cred fs24"></i>异常<span class="monitor-num">【<span
                        id="marginBullAbnormalNum"></span>】</span></span>
            </div>
            <div class="monitor-label">空头账户状态 ：
                <span class="monitor-state" id="marginBearNormal"><i
                        class="fi-social-foursquare monitor-icon cgreen"></i>正常</span>
                <span class="monitor-state dn cred" id="marginBearAbnormal"><i
                        class="fi-alert monitor-icon cred fs24"></i>异常<span class="monitor-num">【<span
                        id="marginBearAbnormalNum"></span>】</span></span>
            </div>
            <div class="monitor-link hand" onclick="detailFun('margin')">查看详情</div>
        </div>
    </div>
    <div class="monitor-company mt20">
        <div class="monitor-con">
            <div class="monitor-tit">内外部对账监控</div>
            <div class="monitor-label add-height">监控状态 ：
                <span class="monitor-state" id="platbalNormal"><i class="fi-social-foursquare monitor-icon cgreen"></i>正常</span>
                <span class="monitor-state cred dn" id="platbalAbnormal"><i class="fi-alert monitor-icon cred fs24"></i>异常<span
                        class="monitor-num">【<span id="platbalAbnormalNum"></span>】</span></span>
            </div>
            <div class="monitor-link hand" onclick="detailFun('platbal')">查看详情</div>
        </div>
    </div>
    <!--add-->
    <div class="monitor-company  mt20">
        <div class="monitor-con">
            <div class="monitor-tit">ERC20内外部总帐监控</div>
            <div class="monitor-label add-height">监控状态 ：
                <span class="monitor-state" id="ERC20Normal"><i class="fi-social-foursquare monitor-icon cgreen"></i>正常</span>
                <span class="monitor-state cred dn" id="ERC20Abnormal"><i class="fi-alert monitor-icon cred fs24"></i>异常<span
                        class="monitor-num">【<span id="ERC20AbnormalNum"></span>】</span></span>
            </div>
            <div class="monitor-link hand" onclick="detailFun('erc20Bal')">查看详情</div>
        </div>
    </div>
    <div class="monitor-company  mt20">
        <div class="monitor-con">
            <div class="monitor-tit">ETH区块高度监控</div>
            <div class="monitor-label add-height">监控状态 ：
                <span class="monitor-state" id="blockNormal"><i class="fi-social-foursquare monitor-icon cgreen"></i>正常</span>
                <span class="monitor-state cred dn" id="blockAbnormal"><i class="fi-alert monitor-icon cred fs24"></i>异常<span
                        class="monitor-num">【<span id=" blockAbnormalNum"></span>】</span></span>
            </div>
            <div class="monitor-link hand" onclick="detailFun('blockNum')">查看详情</div>
        </div>
    </div>
</div>
<div class="monitor-panel" style="padding:10px 20px; border:0;padding-right: 0; padding-top: 0;">
    <div class="floatR">
        <span class="fs14 ">刷新时间：</span>
        <select id="bzjjk_timer_c" class="easyui-combobox " name="bzjjk_timer_ac"
                style="width:150px; height:30px; font-size:14px; margin-top:20px; cursor:pointer;">
            <option value="0">不刷新</option>
            <option selected value="5">5秒</option>
            <option value="10">10秒</option>
            <option value="15">15秒</option>
            <option value="30">30秒</option>
            <option value="60">1分钟</option>
        </select>
    </div>
</div>
<script>
    function detailFun(title) {
        var titleName;
        var redirectUrl;
        if (title == 'matchfund') {
            titleName = '内部总账监控';
            redirectUrl = '/monitor/matchfund';
        } else if (title == "acctfundcur") {
            titleName = '账户资产监控';
            redirectUrl = '/monitor/acctfundcur';
        } else if (title == "margin") {
            titleName = '保证金监控';
            redirectUrl = '/monitor/margin';
        } else if (title == "platbal") {
            titleName = '内外部对账';
            redirectUrl = '/monitor/platbal';
        } else if (title == "erc20Bal") {
            titleName = 'erc20内外部总账监控';
            redirectUrl = '/monitor/erc20Bal';
        } else if (title == "blockNum") {
            titleName = 'eth区块高度监控';
            redirectUrl = '/monitor/blockNum';
        } else {
        }

        var opts = {
            title: titleName,//这里是标题
            border: false,
            closable: true,
            fit: true
        };
        var url = redirectUrl;
        opts.href = url;
        var t = $('#index_tabs');
        if (t.tabs('exists', opts.title)) {
            t.tabs('select', opts.title);
        } else {
            t.tabs('add', opts);
        }
    }

    $(function () {
        $('.monitor-panel').eq(0).parent().parent().parent().parent().find('.tabs-close').click(function(){
            window.clearInterval(bzj_timer_c);
        })
            var bzj_timer_c;
            bzj_timer_c = window.setInterval(monitor, 5 * 1000);
            $("#bzjjk_timer_c").combobox({
                onChange: function (n, o) {
                    window.clearInterval(bzj_timer_c);
                    if ($("#bzjjk_timer_c").combobox('getValue') != 0) {
                        bzj_timer_c = null;
                        bzj_timer_c = window.setInterval(monitor, $("#bzjjk_timer_c").combobox('getValue') * 1000);
                    }
                }
            });
        monitor();
        function monitor() {
            $.ajax({
                url: "${ctx}/monitor/monitorcenter/data",
                type: "post",
                dataType: 'json',
              /*  beforeSend: function () {
                    load();
                },*/
                success: function (data) {
                   /* disLoad();*/
                    if (data.object.matchFund.chkStatus == 1) {
                        $('#matchfundNormal').removeClass('dn');
                        $('#matchfundAbnormal').addClass('dn');
                    } else if (data.object.matchFund.chkStatus == -1) {
                        $('#matchfundNormal').addClass('dn');
                        $('#matchfundAbnormal').removeClass('dn');
                        $('#matchfundAbnormalNum').text(data.object.matchFund.abNormalCount);
                    }
                    if (data.object.acctFundCur.chkStatus == 1) {
                        $('#acctfundcurNormal').removeClass('dn');
                        $('#acctfundcurAbnormal').addClass('dn');
                    } else if (data.object.acctFundCur.chkStatus == -1) {
                        $('#acctfundcurNormal').addClass('dn');
                        $('#acctfundcurAbnormal').removeClass('dn');
                        $('#acctfundcurAbnormalNum').text(data.object.acctFundCur.abNormalCount);
                    }
                    if (data.object.margin.bullStatus == 1) {
                        $('#marginBullNormal').removeClass('dn');
                        $('#marginBullAbnormal').addClass('dn');
                    } else if (data.object.margin.bullStatus == -1) {
                        $('#marginBullNormal').addClass('dn');
                        $('#marginBullAbnormal').removeClass('dn');
                        $('#marginBullAbnormalNum').text(data.object.margin.bullAbNormalCount);
                    }
                    if (data.object.margin.bearStatus == 1) {
                        $('#marginBearNormal').removeClass('dn');
                        $('#marginBearAbnormal').addClass('dn');
                    } else if (data.object.margin.bearStatus == -1) {
                        $('#marginBearNormal').addClass('dn');
                        $('#marginBearAbnormal').removeClass('dn');
                        $('#marginBearAbnormalNum').text(data.object.margin.bearAbNormalCount);
                    }
                    if (data.object.platBal.chkStatus == 1) {
                        $('#platbalNormal').removeClass('dn');
                        $('#platbalAbnormal').addClass('dn');
                    } else if (data.object.platBal.chkStatus == -1) {
                        $('#platbalNormal').addClass('dn');
                        $('#platbalAbnormal').removeClass('dn');
                        $('#platbalAbnormalNum').text(data.object.platBal.abNormalCount);
                    }
                    if (data.object.ERC20Bal.chkStatus == 1) {
                        $('#ERC20Normal').removeClass('dn');
                        $('#ERC20Abnormal').addClass('dn');
                    } else if (data.object.ERC20Bal.chkStatus == -1) {
                        $('#ERC20Normal').addClass('dn');
                        $('#ERC20Abnormal').removeClass('dn');
                        $('#ERC20AbnormalNum').text(data.object.ERC20Bal.abNormalCount);
                    }
                    if (data.object.blockNum.chkStatus == 1) {
                        $('#blockNormal').removeClass('dn');
                        $('#blockAbnormal').addClass('dn');
                    } else if (data.object.blockNum.chkStatus == -1) {
                        $('#blockNormal').addClass('dn');
                        $('#blockAbnormal').removeClass('dn');
                        $('#blockAbnormalNum').text(data.object.blockNum.abNormalCount);
                    }
                    //异常警报
                    if (data.object.matchFund.chkStatus == -1 || data.object.acctFundCur.chkStatus == -1 || data.object.margin.bullStatus == -1 || data.object.margin.bearStatus == -1 || data.object.platBal.chkStatus == -1 || data.object.ERC20Bal.chkStatus == -1 || data.object.blockNum.chkStatus == -1) {
                        playSound();
                    } else {
                        stopSound();
                    }
                }
            });
        }

       /* //弹出加载层
        function load() {
            $("<div class=\"datagrid-mask\"></div>").css({
                display: "block",
                width: "100%",
                height: $(window).height()
            }).appendTo(".monitor-panel");
            $("<div class=\"datagrid-mask-msg\"></div>").html("处理中，请稍候。。。").appendTo(".monitor-panel").css({
                display: "block",
                left: ($(document.body).outerWidth(true) - 190) / 2,
                top: ($(window).height() - 45) / 2
            });
        }

        //取消加载层
        function disLoad() {
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }*/

        $('.alarm').click(function () {
            stopSound();
        })
    })

    //警报
    function playSound() {
        $('.alarm').addClass('shake-on');
        var audio = document.getElementById('auto');
        audio.play();
    }
    function stopSound() {
        $('.alarm').removeClass('shake-on');
        var audio = document.getElementById('auto');
        audio.pause();
    }
</script>