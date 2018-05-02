define(function(require, exports, module){
    /**
     * 一个通过 “+”，“-”以及自身 input 框 编辑的 设置数量的插件
     * @param options
     * numMinus: “-”对应的节点
     * numAmount: 自身文本框节点
     * numPlus:  “+”对应的节点
     * maxerror:  超过 data-max 时的错误提示
     * delayTime: 回调延迟触发时间
     * callback： 回调， 在 “-”，“+”，编辑的时候 都会触发
     * reg:       验证规则，计划扩展，暂时没有应用场景
     * */
    var _ = require("underscore");

    var setNumber = function(options){
        var setting = {
            numMinus: ".num-minus",
            numAmount: ".num-amount",
            numPlus: ".num-plus",
            maxerror: "数量超过最大值",
            delayTime: 300,
            callback: null,
            reg: /[^\d]/g
        };
        var configs = $.extend({}, setting, options);
        for(var key in configs){
            this[key] = configs[key];
        }
        this.init();
    };

    setNumber.prototype.init = function(){
        this.events();
    };

    setNumber.prototype.events = function(){
        var that = this;

        function delayChange(){
            that.callback && that.callback();
        }
        var _query = _(delayChange).debounce(that.delayTime);

        $(document).on("click", this.numMinus, function(){
            var amount = parseInt($(this).next().val()) || 0;
            amount--;
            that.changeValue($(this).next(), amount);
            _query();
        });
        $(document).on("keyup change", this.numAmount, function(){
            var amount = parseInt($(this).val());
            that.amount($(this), amount);
            _query();
        });
        $(document).on("click", this.numPlus, function(){
            var amount = parseInt($(this).prev().val()) || 0;
            amount++;
            that.changeValue($(this).prev(), amount);
            _query();
        });
    };

    setNumber.prototype.changeValue = function($amount, amount){
        var _max = $amount.data("max");
        if(amount < 0){
            $amount.val("0");
        }
        if(amount > _max){
            $amount.val(_max);
            remind("error", this.maxerror);
        }
        if(amount <= _max && amount >= 0){
            $amount.val(amount);
        }
    };

    setNumber.prototype.amount = function($amount, amount){
        $amount.val($amount.val().replace(this.reg, ''));
        if($amount.val() == ""){
            amount = 0;
        }
        this.changeValue($amount, amount);
    };

    module.exports = setNumber;
});