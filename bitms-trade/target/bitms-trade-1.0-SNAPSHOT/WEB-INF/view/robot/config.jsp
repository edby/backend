<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%@ include file="/global/setup_ajax.jsp" %>
<style>
    .div-inline {
        width: 50%;
        float: left;
    }
</style>
<!DOCTYPE html>
<html>

<body style="padding:15px;">

<h2>网格机器人控制台</h2>

<ul id="myTab" class="nav nav-tabs bitms-tabs clearfix">
    <li class="active" id="li_c">
        <a href="#configList" data-toggle="tab">
            配置列表
        </a>
    </li>
    <li id="li_m"><a href="#multiInfo" data-toggle="tab">机器人运行信息</a></li>
</ul>


<div id="myTabContent" class="tab-content">
    <div class="tab-pane fade in active" id="configList">


        <button class="btn btn-primary btn-sm" data-toggle="modal" onclick="addParam()">
            添加配置
        </button>
        <form class="clearfix" data-widget="validator" name="paramForm" id="paramForm">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th style="display: none;">id</th>
                    <th>配置名称</th>
                    <th>买单/卖单最大委托数</th>
                    <th>网格价差</th>
                    <th>盘口初始价差</th>
                    <%--<th>下单量类型</th>--%>
                    <th>最小下单量</th>
                    <th>最大下单量</th>
                    <th>持仓量下限</th>
                    <th>持仓量上限</th>
                    <th>重启买单持仓量</th>
                    <th>重启卖单持仓量</th>
                    <th>基准价更新临界值</th>
                    <th>盘口撤单临界值</th>
                    <th>启用状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="param_list_emement">

                </tbody>
            </table>
        </form>
        <div class="mt10 m010">
            <%-- 通用分页 --%>
            <div id="entrustx_pagination" class="paginationContainer"></div>
        </div>

        <div class="modal fade" id="addUserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="myModalLabel">
                            参数列表
                        </h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" role="form" id="configForm">
                            <input type="hidden" name="id" id="id"/>
                            <input type="hidden" name="active" id="active"/>
                            <div class="form-group">
                                <label for="configName" class="col-sm-3 control-label">配置名称</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="configName" value="" id="configName"
                                           placeholder="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="maxOrderSize" class="col-sm-3 control-label">买单/卖单最大委托数</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="maxOrderSize" value=""
                                           id="maxOrderSize"
                                           placeholder="请输入0~25之间的整数">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="priceGrade" class="col-sm-3 control-label">网格价差</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="priceGrade" value="" id="priceGrade"
                                           placeholder="请输入0~10000之间的整数">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="beginPriceGrade" class="col-sm-3 control-label">盘口初始价差</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="beginPriceGrade" value=""
                                           id="beginPriceGrade"
                                           placeholder="请输入0~10000之间的整数">
                                </div>
                            </div>
                            <div class="form-group" id="div_minAmt">
                                <label for="minAmt" class="col-sm-3 control-label">最小下单量</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="minAmt" value="" id="minAmt"
                                           placeholder="请输入0~100之间的数字">
                                </div>
                            </div>
                            <div class="form-group" id="div_maxAmt">
                                <label for="maxAmt" class="col-sm-3 control-label">最大下单量</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="maxAmt" value="" id="maxAmt"
                                           placeholder="请输入0~100之间的数字">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="avgUpdateLimit" class="col-sm-3 control-label">基准价更新临界值</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="avgUpdateLimit" value=""
                                           id="avgUpdateLimit"
                                           placeholder="请输入0~10000之间的整数">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="cancelLimit" class="col-sm-3 control-label">盘口撤单临界值</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="cancelLimit" value=""
                                           id="cancelLimit"
                                           placeholder="请输入0~10000之间的整数">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="maxOpenInterest" class="col-sm-3 control-label">usd持仓上限</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="maxOpenInterest" value=""
                                           id="maxOpenInterest"
                                           placeholder="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="minOpenInterest" class="col-sm-3 control-label">usd持仓下限</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="minOpenInterest" value=""
                                           id="minOpenInterest"
                                           placeholder="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="reBuyOpenInterest" class="col-sm-3 control-label">重启买单持仓量</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="reBuyOpenInterest" value=""
                                           id="reBuyOpenInterest"
                                           placeholder="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="reSellOpenInterest" class="col-sm-3 control-label">重启卖单持仓量</label>
                                <div class="col-sm-6 ui-form-item">
                                    <input type="text" class="form-control" name="reSellOpenInterest" value=""
                                           id="reSellOpenInterest"
                                           placeholder="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="reSellOpenInterest" class="col-sm-3 control-label"></label>
                                <div class="col-sm-6 ui-form-item">
                                    <button type="button" class="btn btn-primary" id="configBtn">提交</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <!-- 综合信息-->
    <div class="tab-pane fade" id="multiInfo">

        <div class="panel clearfix" id="multiPanel" style="margin-top: 5px">
            <h4 class="text-success">状态信息</h4>
            <a class='text-success' onclick='getMultiInfo()'>刷新</a>
            <div id="toggle_div">
                <div class="div-inline" id="config_status">
                    <span>配置名称:</span>
                    <span class="text-success" id="m_configName"></span>
                    <br>
                    <span>开始时间:</span>
                    <span class="text-success" id="m_startTime"></span>
                    <br>
                    <span>买单开关:</span>
                    <span class="text-success" id="m_buySwitch"></span>
                    <br>
                    <span>卖单开关:</span>
                    <span class="text-success" id="m_sellSwitch"></span>
                </div>
                <div class="div-inline" id="asset_status">
                    <span>初始usd:</span>
                    <span class="text-success" id="org_cap"></span>
                    <br>
                    <span>剩余usd:</span>
                    <span class="text-success" id="cur_cap"></span>
                    <br>
                    <span>初始btc:</span>
                    <span class="text-success" id="org_tar"></span>
                    <br>
                    <span>剩余btc:</span>
                    <span class="text-success" id="cur_tar"></span>
                </div>

            </div>

        </div>
        <div class="panel clearfix" id="logPanel" hidden>
            <h4 class="text-success">日志信息</h4>
            <div>
                <span>尚未开放</span>
            </div>

        </div>
    </div>
</div>
</body>
<script id="param_list_tpl" type="text/html">
    {{each rows}}
    <tr class="test">
        <td class="c_id" style="display: none;">{{$value.id}}</td>
        <td class="c_configName">{{$value.configName}}</td>
        <td class="c_maxOrderSize">{{$value.maxOrderSize}}</td>
        <td class="c_priceGrade">{{$value.priceGrade}}</td>
        <td class="c_beginPriceGrade">{{$value.beginPriceGrade}}</td>
        <%--<td class="c_amtType">{{$amtTypeFlag $value.amtType}}</td>--%>
        <td class="c_minAmt">{{$value.minAmt}}</td>
        <td class="c_maxAmt">{{$value.maxAmt}}</td>
        <td class="c_minOpenInterest">{{$value.minOpenInterest}}</td>
        <td class="c_maxOpenInterest">{{$value.maxOpenInterest}}</td>
        <td class="c_reBuyOpenInterest">{{$value.reBuyOpenInterest}}</td>
        <td class="c_reSellOpenInterest">{{$value.reSellOpenInterest}}</td>
        <td class="c_avgUpdateLimit">{{$value.avgUpdateLimit}}</td>
        <td class="c_cancelLimit">{{$value.cancelLimit}}</td>
        <td class="c_active">{{$activeFlag $value.active}}</td>
        <td>
            {{$actionFlag $value.active}}
        </td>
    </tr>
    {{/each}}
</script>
<script>
    var renderRecivePage;
    seajs.use(['pagination', 'template', 'validator'], function (Pagination, Template, Validator) {
        renderRecivePage = new Pagination({
            url: "${ctx}/robot/config/data",
            elem: "#entrustx_pagination",
            form: $("#paramForm"),
            rows: 10,
            method: "post",
            handleData: function (json) {
                if (json.code == 200) {
                    var html = Template.render("param_list_tpl", json);
                    $("#param_list_emement").html(html);
                } else {
                    alert(json.message);
                }
            }
        });
        template.helper('$activeFlag', function (flag, id) {

            if (flag == 0) {
                return "<span style=\"padding: 0px 2px; border-radius: 2px; color: rgb(21, 25, 34); background-color: rgb(169, 68, 66);font-weight: bold\">禁用</span>";
            } else {
                return "<span style=\"padding: 0px 2px; border-radius: 2px; color: rgb(21, 25, 34); background-color: rgb(100, 130, 65);font-weight: bold\">启用</span>";
            }
        });
        template.helper('$actionFlag', function (flag) {
            var str = '';
            if (flag == 0) {
                str += "<a style=\"color: #87CEEB;\" onclick='active()' >启用 </a>";
            } else {
                str += "<a style=\"color: #87CEEB;\" onclick='unActive()' >禁用 </a>";
            }
            str += "<a  style=\"color: #87CEEB;\" onclick='modifyParam()' >编辑 </a>";
            ;
            str += "<a  style=\"color: #87CEEB;\" onclick='deleteParam()' >删除</a>";
            ;
            return str;
        });
//        template.helper('$Fix8Flag', function (flag) {
//            return parseFloat((flag)).toFixed(8);
//        });
        validator = new Validator();
        $("#configBtn").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#configForm',
                autoSubmit: false,
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        var min = $('#minAmt').val();
                        var max = $('#maxAmt').val();
                        if (max < min) {
                            remind(remindType.error, "最小下单量不得大于最大下单量", 1000);
                            return;
                        }
                        var resellOpen = $('#reSellOpenInterest').val();
                        var maxOpen = $('#maxOpenInterest').val();
                        if (parseFloat(resellOpen) >= parseFloat(maxOpen)) {
                            remind(remindType.error, "重启卖单持仓量必须小于持仓量上限", 1000);
                            return;
                        }
                        var rebuyOpen = $('#reBuyOpenInterest').val();
                        var minOpen = $('#minOpenInterest').val();
                        if (parseFloat(rebuyOpen) <= parseFloat(minOpen)) {
                            remind(remindType.error, "重启买单持仓量必须大于持仓量下限", 1000);
                            return;
                        }

                        $.ajax({
                            url: '/robot/config/save',
                            type: 'post',
                            data: $('#configForm').serialize(),
                            dataType: 'json',
                            success: function (data, textStatus, jqXHR) {
                                $('#csrf-form').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                setCsrfToken("csrf-form");
                                if (data.code == 200) {
                                    remind(remindType.success, data.message, 500);
                                    renderRecivePage.render(true);
                                    $('#addUserModal input').val("");
                                    $('#addUserModal').modal('hide');
                                } else {
                                    remind(remindType.error, data.message, 1500);
                                }

                            }

                        });
                    }

                }
            }).addItem({
                element: '#configForm [name=maxOrderSize]',
                required: true,
                rule: 'number min{min:0} max{max:25} numberOfDigits{maxLength:0}'
            }).addItem({
                element: '#configForm [name=minAmt]',
                required: true,
                rule: 'number min{min:0} max{max:100} numberOfDigits{maxLength:2}'
            }).addItem({
                element: '#configForm [name=maxAmt]',
                required: true,
                rule: 'number min{min:0} max{max:100} numberOfDigits{maxLength:2}'
            }).addItem({
                element: '#configForm [name=priceGrade]',
                required: true,
                rule: 'number min{min:0} max{max:10000} numberOfDigits{maxLength:0}'
            }).addItem({
                element: '#configForm [name=beginPriceGrade]',
                required: true,
                rule: 'number min{min:0} max{max:10000} numberOfDigits{maxLength:0}'
            }).addItem({
                element: '#configForm [name=avgUpdateLimit]',
                required: true,
                rule: 'number min{min:0} max{max:10000} numberOfDigits{maxLength:0}'
            }).addItem({
                element: '#configForm [name=maxOpenInterest]',
                required: true,
                rule: 'number numberOfDigits{maxLength:2}'
            }).addItem({
                element: '#configForm [name=minOpenInterest]',
                required: true,
                rule: 'number numberOfDigits{maxLength:2}'
            }).addItem({
                element: '#configForm [name=reBuyOpenInterest]',
                required: true,
                rule: 'number numberOfDigits{maxLength:2}'
            }).addItem({
                element: '#configForm [name=reSellOpenInterest]',
                required: true,
                rule: 'number numberOfDigits{maxLength:2}'
            }).addItem({
                element: '#configForm [name=cancelLimit]',
                required: true,
                rule: 'number min{min:0} max{max:10000} numberOfDigits{maxLength:2}'
            }).addItem({
                element: '#configForm [name=configName]',
                required: true

            });
            $("#configForm").submit();
        });


    });


    //添加配置
    function addParam() {
        $('#addUserModal input').val("");
        $('#addUserModal').modal();

    }

    //修改配置
    function modifyParam() {
        $('#addUserModal').modal();
        var id = $(event.target).closest("tr").find('.c_id').text();
        var active = $(event.target).closest("tr").find('.c_active').text();
        var configName = $(event.target).closest("tr").find('.c_configName').text();
        var maxOrderSize = $(event.target).closest("tr").find('.c_maxOrderSize').text();
        var priceGrade = $(event.target).closest("tr").find('.c_priceGrade').text();
        var beginPriceGrade = $(event.target).closest("tr").find('.c_beginPriceGrade').text();
//        var amtType = $(event.target).closest("tr").find('.c_amtType').text();
        var minAmt = $(event.target).closest("tr").find('.c_minAmt').text();
        var maxAmt = $(event.target).closest("tr").find('.c_maxAmt').text();
        var minOpenInterest = $(event.target).closest("tr").find('.c_minOpenInterest').text();
        var maxOpenInterest = $(event.target).closest("tr").find('.c_maxOpenInterest').text();
        var reBuyOpenInterest = $(event.target).closest("tr").find('.c_reBuyOpenInterest').text();
        var reSellOpenInterest = $(event.target).closest("tr").find('.c_reSellOpenInterest').text();
        var avgUpdateLimit = $(event.target).closest("tr").find('.c_avgUpdateLimit').text();
        var cancelLimit = $(event.target).closest("tr").find('.c_cancelLimit').text();

        var ac = active.indexOf("启用");
        if (ac == 0) {
            $('#active').val(1);
        } else {
            $('#active').val(0);
        }
        $('#id').val(id);
        $('#configName').val(configName);
        $('#maxOrderSize').val(maxOrderSize);
        $('#priceGrade').val(priceGrade);
        $('#beginPriceGrade').val(beginPriceGrade);
        $('#minAmt').val(minAmt);
        $('#maxAmt').val(maxAmt);
        $('#minOpenInterest').val(minOpenInterest);
        $('#maxOpenInterest').val(maxOpenInterest);
        $('#reBuyOpenInterest').val(reBuyOpenInterest);
        $('#reSellOpenInterest').val(reSellOpenInterest);
        $('#avgUpdateLimit').val(avgUpdateLimit);
        $('#cancelLimit').val(cancelLimit);

    }

    //删除配置
    function deleteParam() {
        var id = $(event.target).closest("tr").find('.c_id').text();
        confirmDialog("确认删除当前配置？", function () {
            $.ajax({
                url: '/robot/config/del',
                data: "id=" + id,
                type: 'post',
                success: function (data, textStatus, jqXHR) {
                    $('#csrf-form').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                    setCsrfToken("csrf-form");
                    var json = JSON.parse(data);
                    if (json.code == 200) {
                        remind(remindType.success, json.message, 500);
                    } else {
                        remind(remindType.error, json.message, 1000);
                    }
                    renderRecivePage.render(true);

                }
            });
        });


    }

    //启用配置
    function active() {
        var id = $(event.target).closest("tr").find('.c_id').text();
        $.ajax({
            url: '/robot/config/active',
            data: "id=" + id,
            type: 'post',
            success: function (data, textStatus, jqXHR) {
                $('#csrf-form').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                setCsrfToken("csrf-form");
                var json = JSON.parse(data);
                if (json.code == 200) {
                    remind(remindType.success, json.message, 500);
                } else {
                    remind(remindType.error, json.message, 1000);
                }
                renderRecivePage.render(true);

            }

        });
    }

    //禁用配置
    function unActive() {
        var id = $(event.target).closest("tr").find('.c_id').text();
        $.ajax({
            url: '/robot/config/unActive',
            data: "id=" + id,
            type: 'post',
            success: function (data, textStatus, jqXHR) {
                $('#csrf-form').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                setCsrfToken("csrf-form");
                var json = JSON.parse(data);
                if (json.code == 200) {
                    remind(remindType.success, json.message, 500);
                } else {
                    remind(remindType.error, json.message, 1000);
                }
                renderRecivePage.render(true);
            }

        });
    }

    $(document).ready(function () {
        $('#li_c').click(function () {
            renderRecivePage.render(true);
        });

        $('#li_m').click(function () {
            getMultiInfo();

        });

    });

    function getMultiInfo() {
        $.ajax({
            url: '/robot/config/multiData',
            type: 'post',
            success: function (data) {
                var json = JSON.parse(data);
                if (json.code == 200) {
                    writeMultiInfo(json);
                } else {
                    remind(remindType.error, json.message, 1000);
                }
            }

        });
    }

    function writeMultiInfo(json) {
        $('#activeBot').text(json.object.robotAmt);
        if (!json.object.botStatus) {
            $('#toggle_div').hide();
        } else {
            $('#toggle_div').show();
            $('#m_configName').text(json.object.configName);
            $('#m_startTime').text(new Date(json.object.startTime).Format("yyyy-MM-dd hh:mm:ss"));
            if (json.object.buySwitch) {
                $('#m_buySwitch').text("开");
            } else {
                $('#m_buySwitch').text("关");
            }
            if (json.object.sellSwitch) {
                $('#m_sellSwitch').text("开");
            } else {
                $('#m_sellSwitch').text("关");
            }
            $('#org_cap').text(json.object.usdOrgBal);
            $('#cur_cap').text(json.object.usdCurBal);
            $('#org_tar').text(json.object.btcOrgBal);
            $('#cur_tar').text(json.object.btcCurBal);


        }


    }

</script>
</html>

