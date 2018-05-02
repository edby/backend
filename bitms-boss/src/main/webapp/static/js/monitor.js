

//监控指标级别
var idxLevel = {
    1:"低级别",
    2:"普通级别",
    3:"高级别"
};
function getIdxLevelName(target){
    var res = '';
    $.each(idxLevel,function(key,value){
        if(key == target){
            res = value;
            return false;
        }
    });
    return res;

}

//比较方向
var compDirect = {
    1:"绝对值",
    2:"资产-负债",
    3:"负债-资产",
    4:"内部-外部",
    5:"外部-内部"
};

function getCompDirectName(target){
    var res = '';
    $.each(compDirect,function(key,value){
        if(key == target){
            res = value;
            return false;
        }
    });
    return res;

}
//监控类型
var monitorType = {
    "ACCTFUNDCUR":"账户资产监控",
    "MARGIN":"保证金监控",
    "INTERNALPLATFUNDCUR":"内部资金总账监控",
    "MONITORDIGITALCOIN":"数字资产内外部总额监控",
    "MONITORCASHCOIN":"现金资产内外部总额监控",
    "MONITORERC20BAL":"ERC20内外部总额监控",
    "ERC20HOTWALLET":"ERC20热钱包余额监控",
    "ERC20COLDWALLET":"ERC20冷钱包余额监控",
    "ERC20COLLECTFEE":"ERC20归集费用监控",
    "MONITORBLOCKNUM":"区块高度内外部监控"
};
function getMonitorTypeName(target){
    var res = '';
    $.each(monitorType,function(key,value){
        if(key == target){
            res = value;
            return false;
        }
    });
    return res;

}


function printOptions(list,id,name, target, option,style) {
    style = style || "";
    var data=[];
    var html="<input id='"+id+"' name='"+name+"' style='"+style+"' class=\"easyui-combobox\" data-options=\" "+option+"valueField:'code',textField:'name',editable:false, panelHeight:'auto' , data: ";
            $.each(list, function (key, value) {
                    if (key == target) {
                        data.push({ "name": value, "code": key ,selected:true});
                    } else {
                        data.push({ "name": value, "code": key });
                    }

            });

    html+= JSON.stringify(data).replace(/"/g, "'");
    html += "\" />";
    return html;
};
