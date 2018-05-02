<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var jiaoge_tabs;
    var stepLogDataGrid;
    $(function () {
        jiaoge_tabs = $('#jiaoge_tabs').tabs({
            fit: true,
            border: false,
            onContextMenu: function (e, title) {
                e.preventDefault();
                indexTabsMenu.menu('show', {
                    left: e.pageX,
                    top: e.pageY
                }).data('tabTitle', title);
            }
        });
        $("#jiesuanjia_save_btn").click(function (){
           var val=$("#jiesuanjia").val();
           var exchangePairMoney =  $("*[name='vcoin_money']").val();
            if(isNaN(val)){alert('结算价错误：不是一个数值！');return ;}
            if(parseFloat(val)<=0){alert('结算价错误：不能小于0！');return ;}
            $.ajax({
                url: "${ctx}/settlement/settlement/updateSettlementPrice",
                data:{jiesuanjia:val,exchangePairMoney:exchangePairMoney},
                type:'post',
                async: false,
                dataType: 'json',
                success: function(result){
                    if (result.code == ajax_result_success_code) {
                        $.messager.alert('成功',result.message);
                        $("#newjiesuanjia").text(val);
                        var exchangePairMoney=$("[name='vcoin_money']").val();
                        var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
                        $.ajax({
                            url: "${ctx}/settlement/settlement/stockinfo/data",
                            data:{exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                if (result.code == ajax_result_success_code) {
                                    $("#newjiesuanjia").text(result.object.settlementPrice);
                                    var i = result.object.settlementStep;
                                    for(var k=1;k<20;k ++)
                                    {
                                        $('#btn_'+k).linkbutton('disable');
                                    }
                                    $('#btn_'+i).linkbutton('enable');
                                    $('#jiesuanjia').val(result.object.settlementPrice);
                                }
                            }
                        });
                    }else
                    {
                        $.messager.alert('错误',result.message);
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                }
            });
        });

        stepLogDataGrid = $('#stepLogDataGrid').datagrid({
            url: '${ctx}/settlement/settlement/steplog/data',
            striped: true,
            rownumbers: true,
            pagination: false,
            singleSelect: false,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 200,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                {
                    width: '100',
                    title: '父流程',
                    field: 'processId',
                    sortable: true
                },{
                    width: '100',
                    title: '流程名',
                    field: 'processName',
                    sortable: true
                },{
                    width: '150',
                    title: '状态',
                    field: 'status',
                    sortable: true,
                    formatter: function (value, row, index) {
                        switch(value)
                        {
                            case -1:return '执行失败';break;
                            case 0:return '未执行';break;
                            case 1:return '执行成功';break;
                            default : break;
                        }
                    }
                },{
                    width: '140',
                    title: '更新时间',
                    field: 'createDate',
                    formatter: function (value, row, index) {
                        if(value!='' && value!=null  && value!=0)
                        {
                            return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");

                        }else
                        {
                            return '';
                        }
                    }
                },{
                    width: '250',
                    title: '备注',
                    field: 'remark',
                    sortable: true
                }

            ]],
            columns: [[

            ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
            }

        });
    });
    function addJiaoGeTab(opts) {
        var t = $('#jiaoge_tabs');
        if (t.tabs('exists', opts.title)) {
            t.tabs('select', opts.title);
        } else {
            t.tabs('add', opts);
        }
    }

    function addUrlTab(url,title)
    {
        var opts = {
            title: title,//这里是标题
            border: false,
            closable: false,
            fit: true,
            iconCls: '' //这里是图标
        };
        opts.href = url;
        addJiaoGeTab(opts);
    }

    function reload(step)
    {
        var exchangePairMoney=$("[name='vcoin_money']").val();
        var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
        $.ajax({
            url: "${ctx}/settlement/settlement/stockinfo/data",
            data:{exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
            type:'post',
            async: false,
            dataType: 'json',
            success: function(result){
                if (result.code == ajax_result_success_code) {
                    step= result.object.settlementStep;
                }
            }
        });
        if(step == 0 || step =='0' || step == false)
        {
            step = 1;
        }
        $('#stepLogDataGrid').datagrid('load', {});
        step=step*1;
        var url = '';
        //循环打开标签
        if(step>=1)
        {
            url = '/settlement/settlement/switch?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'系统开关');
        }
        if(step>=2)
        {
            url = '/settlement/settlement/entrustXDoing?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'未撤销委托');
        }
        if(step>=3)
        {
            url = '/settlement/settlement/entrustXDone?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'已撤销委托');
            url = '/settlement/settlement/superentrust?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'系统挂单已成交');
        }
        if(step>=4)
        {
            url = '/settlement/settlement/contributionQuota?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'准备金分摊提取');
            url = '/settlement/settlement/debitDoing?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'未处理借款');
        }

        if(step>=5)
        {
            url = '/settlement/settlement/settleEntrust?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'交割挂单');
            url = '/settlement/settlement/debitDone?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'已处理借款');
        }

        if(step>=6)
        {
            url = '/settlement/settlement/settleEntrust?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'交割挂单');
            url = '/settlement/settlement/debitDone?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'已处理借款');
        }

        if(step>=7)
        {
            url = '/settlement/settlement/debitDone?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'已处理借款');
            url = '/settlement/settlement/usdxHave?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'用户法定货币持仓');
        }
        if(step>=8)
        {
            url = '/settlement/settlement/usdxHaveMove?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'购回用户法定货币持仓');
            url = '/settlement/settlement/superasset?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'超级用户资产情况');
        }
        if(step>=9)
        {
            url = '/settlement/settlement/platLoss?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'全市场盈利BTC数量');
        }
        if(step>=10)
        {
            url = '/settlement/settlement/platLossShareConfirm?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'亏损实际分摊');
        }
        if(step>=11)
        {
            url = '/settlement/settlement/platLossShareConfirm?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'亏损实际分摊');
            url = '/settlement/settlement/switch?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'系统开关');
        }
        if(step>=12)
        {
            url = '/settlement/settlement/switch?exchangePairMoney='+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
            addUrlTab(url,'系统开关');
        }
    }

    $(function(){
        var step=${step};
       $('#jiaoge_tabs').tabs({
            border:false,
            onSelect:function(title,index){
                refreshTab(index);
            }
        });
        reload(step);
    });

    function refreshTab(index) {
      var currentTab = jiaoge_tabs.tabs('getTab', index);
       var url = $(currentTab.panel('options')).attr('href');
        var exchangePairMoney=$("[name='vcoin_money']").val();
        var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
        url = url.substring(0,url.indexOf('?'))+"?exchangePairMoney="+exchangePairMoney+"&exchangePairVCoin="+exchangePairVCoin;
        $('#jiaoge_tabs').tabs('update', {
            tab: currentTab,
            options: {
                href: url
            }
        });
        currentTab.panel('refresh');
    }

    function todo(i)
    {
        var exchangePairMoney=$("[name='vcoin_money']").val();
        var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
        switch(i)
        {
            case 1:
                $.messager.prompt('确认','您确认要关闭交易开关吗？如果您确定要进行交割平仓，请在下输入框中输入结算价：',function(r){
                    if (r){
                        var val=r;
                        if(isNaN(val)){alert('结算价错误：不是一个数值！');return ;}
                        if(parseFloat(val)<=0){alert('结算价错误：不能小于0！');return ;}
                        $.ajax({
                            url: "${ctx}/settlement/settlement/updateSettlementPrice",
                            data:{jiesuanjia:val,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $("#newjiesuanjia").text(val);
                                    $('#jiesuanjia').numberbox('setValue', parseFloat(val));
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                    return;
                                }

                            }
                        });
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    $('#btn_'+(i+1)).linkbutton('enable');
                                    refreshTab(0);
                                    reload(2);
                                    contributionQuota();
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }
                            }
                        });
                    }
                });
                break;
            case 2:
                $.messager.confirm('确认','您确认要撤销全部委托交易吗？',function(r){
                    if (r){
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    $('#btn_'+(i+1)).linkbutton('enable');
                                    reload(i+1);
                                    refreshTab(1);//强制刷新 还在委托中的交易
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }

                            }
                        });
                    }
                });
                contributionQuota();
                break;
            case 3:
                $.messager.confirm('确认','您确认要准备金分摊提取吗？',function(r){
                    if (r){
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    $('#btn_'+(i+1)).linkbutton('enable');
                                    reload(i+1);
                                    refreshTab(3);//强制刷新 还在委托中的交易
                                    contributionQuota();
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }
                            }
                        });
                    }
                });
                break;
            case 4:
                $.messager.confirm('确认','您确认要交割挂单吗？',function(r){
                    if (r){
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    $('#btn_'+(i+1)).linkbutton('enable');
                                    reload(i+1);
                                    refreshTab(5);//强制刷新
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }

                            }
                        });
                    }
                });
                break;
            case 5:
                $.messager.confirm('确认','您确认要撤单并强制还款吗？',function(r){
                    if (r){
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    $('#btn_'+(i+1)).linkbutton('enable');
                                    reload(i+1);
                                    contributionQuota();
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }
                            }
                        });
                    }
                });
                break;
            case 6:
                $.messager.confirm('确认','您确认要债务转移吗？',function(r){
                    if (r){
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    $('#btn_'+(i+1)).linkbutton('enable');
                                    reload(i+1);
                                    contributionQuota();
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }
                            }
                        });
                    }
                });
                break;
            case 7:
                $.messager.confirm('确认','您确认要法定货币吗？',function(r){
                    if (r){
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    $('#btn_'+(i+1)).linkbutton('enable');
                                    reload(i+1);
                                    contributionQuota();
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }
                            }
                        });
                    }
                });
                break;
            case 8:
                $.messager.confirm('确认','您确认要多空超级用户数字资产转移吗？',function(r){
                    if (r){
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    $('#btn_'+(i+1)).linkbutton('enable');
                                    reload(i+1);
                                    refreshTab(9);//强制刷新
                                    contributionQuota();
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }
                            }
                        });
                    }
                });
                break;
            case 9:
                $.messager.confirm('确认','您确认要进行分摊操作吗？',function(r){
                    if (r){
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    $('#btn_'+(i+1)).linkbutton('enable');
                                    reload(i+1);
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }
                            }
                        });
                    }
                });
                contributionQuota();
                break;
            case 10:
                $.messager.confirm('确认','您确认本次交割已经完成了吗？',function(r){
                    if (r){
                        $.ajax({
                            url: "${ctx}/settlement/settlement/step/operator",
                            data:{step:i,exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                            type:'post',
                            async: false,
                            dataType: 'json',
                            success: function(result){
                                $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                                setCsrfToken("csrf-form");
                                if (result.code == ajax_result_success_code) {
                                    $.messager.alert('成功',result.message);
                                    $('#btn_'+i).linkbutton('disable');
                                    reload(i+1);
                                    refreshTab(0);
                                    $('#btn_1').linkbutton('enable');
                                }else
                                {
                                    $.messager.alert('错误',result.message);
                                }
                            }
                        });
                    }
                });
                break;
            default : break;

        }
    }
    function contributionQuota()
    {
        var exchangePairMoney=$("[name='vcoin_money']").val();
        var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
        $.ajax({
            url: "${ctx}/settlement/settlement/contributionQuota/admin",
            data:{exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
            type:'post',
            async: false,
            dataType: 'json',
            success: function(result){
                if (result.code == ajax_result_success_code) {
                    var btc = 0;
                    var usdx = 0;
                    for(var i = 0;i < result.object.length;i ++)
                    {
                        if(result.object[i].stockinfoId == exchangePairVCoin)
                        {
                            btc = result.object[i].amount;
                        }
                        if(result.object[i].stockinfoId == exchangePairMoney)
                        {
                            usdx = result.object[i].amount;
                        }
                    }
                    var txt = "数字货币："+btc.toFixed(8)+",法定货币："+usdx.toFixed(8)+"";
                    $("#contributionQuotaTxt").text(txt);
                }else
                {
                    console.log("取准备金错误");
                }

            }
        });
    }
    function getStockinfo()
    {
        var exchangePairMoney=$("[name='vcoin_money']").val();
        var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
        $.ajax({
            url: "${ctx}/settlement/settlement/stockinfo/data",
            data:{exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
            type:'post',
            async: false,
            dataType: 'json',
            success: function(result){
                if (result.code == ajax_result_success_code) {
                    $("#newjiesuanjia").text(result.object.settlementPrice);
                    var i = result.object.settlementStep;
                    $('#btn_'+(i)).removeClass("l-btn-disabled");
                    $('#btn_'+(i)).attr("data-options","iconCls:'icon-large-smartart'");
                    $("*[name='jiesuanjia']").val(result.object.settlementPrice);
                    reload(result.object.settlementStep);
                }
            }
        });
    }

    $(function () {
        contributionQuota();

    });
    $(document).ready(function(){
        getStockinfo();
        $("#money").combobox({
            onChange: function (n,o) {
                //alert(""+n);
                var exchangePairMoney=$("[name='vcoin_money']").val();
                var exchangePairVCoin=$("[name='vcoin_vcoin']").val();
                $.ajax({
                    url: "${ctx}/settlement/settlement/stockinfo/data",
                    data:{exchangePairMoney:exchangePairMoney,exchangePairVCoin:exchangePairVCoin},
                    type:'post',
                    async: false,
                    dataType: 'json',
                    success: function(result){
                        if (result.code == ajax_result_success_code) {
                            $("#newjiesuanjia").text(result.object.settlementPrice);
                            var i = result.object.settlementStep;
                            for(var k=1;k<20;k ++)
                            {
                                $('#btn_'+k).linkbutton('disable');
                            }
                            $('#btn_'+i).linkbutton('enable');
                            $('#jiesuanjia').textbox('setValue',result.object.settlementPrice);
                            reload(result.object.settlementStep);
                            refreshTab(0);
                        }
                    }
                });
                contributionQuota();
            }
        });
        $(function(){
            //$('#updateTradeMasterSwitch').switchbutton({  checked: ${switchStatus=="yes"?"true":"fales"} })
            $('#updateTradeMasterSwitch').switchbutton({
                checked: ${switchStatus=="yes"?"true":"false"},
                onChange: function(checked){
                    var status='no';
                    if(checked){status='yes';}
                    $.ajax({
                        url: "${ctx}/settlement/settlement/updateTradeMasterSwitch",
                        data:{status:status},
                        type:'post',
                        async: false,
                        dataType: 'json',
                        success: function(result){
                            $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                            setCsrfToken("csrf-form");
                            if (result.code == ajax_result_success_code) {
                                $.messager.alert('成功',result.message);
                            }else
                            {
                                $.messager.alert('失败',result.message);
                            }
                        }
                    });
                }
            })
        })

    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 300px; overflow: hidden;background-color: #fff;padding-top:5px; pading:auto auto;">
        <div class="easyui-layout" data-options="fit:true">
            <div data-options="region:'north',split:true,border:false" style="height: 110px; overflow: hidden;background-color: #fff;padding-top:5px; pading:auto auto;">
                &nbsp;&nbsp;&nbsp;&nbsp;交易总控开关：<input  id="updateTradeMasterSwitch" class="easyui-switchbutton" data-options="onText:'已打开',offText:'已关闭'" style="margin:7px;">
                <a id="btn_1" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart', ${step ne 1?"disabled:true":""}"  style="margin:7px;" onclick="todo(1);">关闭交易开关</a>
                <i class="fi-arrow-right"  style="margin:7px;"></i>
                <a id="btn_2" href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart', ${step ne 2?"disabled:true":""}"  style="margin:7px;" onclick="todo(2);">撤销全部委托</a>
                <i class="fi-arrow-right"  style="margin:7px;"></i>
                <a id="btn_3" href="#"  class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart', ${step ne 3?"disabled:true":""}"  style="margin:7px;" onclick="todo(3);">准备金分摊提取</a>
                <i class="fi-arrow-right"  style="margin:7px;"></i>
                <a id="btn_4" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart', ${step ne 4?"disabled:true":""}"  style="margin:7px;" onclick="todo(4);">交割挂单</a>
                <i class="fi-arrow-right"  style="margin:7px;"></i>
                <a id="btn_5" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart', ${step ne 5?"disabled:true":""}"  style="margin:7px;" onclick="todo(5);">撤单并强制还款</a>
                <i class="fi-arrow-right"  style="margin:7px;"></i>
                <a id="btn_6" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart', ${step ne 6?"disabled:true":""}"  style="margin:7px;" onclick="todo(6);">债务转移</a>
                <i class="fi-arrow-right"  style="margin:7px;"></i>
                <a id="btn_7" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart', ${step ne 7?"disabled:true":""}"  style="margin:7px;" onclick="todo(7);">法定货币购回</a>
                <i class="fi-arrow-right"  style="margin:7px;"></i>
                <a id="btn_8" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart', ${step ne 8?"disabled:true":""}"  style="margin:7px;" onclick="todo(8);">多空超级用户数字货币转移</a>
                <i class="fi-arrow-right" style="margin:7px;"></i>
                <a id="btn_9" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart', ${step ne 9?"disabled:true":""}"   style="margin:7px;" onclick="todo(9);">市场盈亏和分摊确认</a>
                <i class="fi-arrow-right"  style="margin:7px;"></i>
                <a id="btn_10" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart',${step ne 10?"disabled:true":""}"  style="margin:7px;" onclick="todo(10);">打开交易开关</a>
                <br />
                <center><strong>债务转移:包含强制法定货币还款、强制数字货币还款、还款后的债务转移。&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;法定货币购回：包含费用超级用户、多空超级用户。</strong></center>
            </div>
            <div data-options="region:'west',split:true,border:false" style="width:440px;">
                <div id="north_left_panel" class="easyui-panel" title="结算价"
                     style="width:99%;height:99%;padding:10px;"
                     data-options="closable:false">
                    <table class="grid">
                        <tr>
                            <td align="right" width="60px">结算价：</td>
                            <td width="330px">
                                <input  id="vcoin" name="vcoin_vcoin"  class="easyui-combobox" name="language" style="width: 70px;"
                                        value="<%=FundConsts.WALLET_BTC_TYPE%>" data-options="
									url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_PURESPOT%>', method: 'get', valueField:'id',
									textField:'stockCode', groupField:'group'"  />
                                <input  id="money" name="vcoin_money"  class="easyui-combobox" name="language" style="width: 90px;"
                                          value="<%=FundConsts.WALLET_BTC2USDX_TYPE%>" data-options="
									url: '${ctx}/stock/info/allInExchange', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'"  />
                                <input name="jiesuanjia" id="jiesuanjia" type="text" style="width: 70px;" placeholder="结算价" class="easyui-numberbox" data-options="required:true,min:0.01,precision:2" value="${jiesuanjia}" \>
                                <a id="jiesuanjia_save_btn"  class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
                                <center>
                                    <font color="red">当前结算价：<span id="newjiesuanjia">${jiesuanjia}</span><br />
                                        请确保该结算价在【法定货币购回】之前正确填入！
                                    </font>
                                </center>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="80px">当前准备金账户资产：</td>
                            <td align="center">
                                <font color="red">
                                    <span id="contributionQuotaTxt"></span>
                                </font>
                            </td>
                        </tr>

                    </table>
                </div>
            </div>
            <div data-options="region:'center',border:false">
                <div id="north_right_panel" class="easyui-panel" title="交割处理日志"
                     style="width:100%;height:185px;"
                     data-options="closable:false">
                    <table id="stepLogDataGrid" data-options="fit:false,border:false"></table>
                </div>
            </div>
        </div>
    </div>
	<div data-options="region:'center',border:true">
        <div id="jiaoge_tabs" style="overflow: hidden;">

        </div>
    </div>
</div>
