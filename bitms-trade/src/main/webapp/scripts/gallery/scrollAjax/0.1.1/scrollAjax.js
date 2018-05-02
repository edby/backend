//滚动延迟加载区域
define(function(require, exports, module){
    var template = require("template");
    /* 静态结构
        <div class="js-scrollAjax-area" data-path="ajax-path" data-ajax="0" style="height: 400px;">
            <!--<div class="js-scrollAjax-loading">
                正在加载中...
            </div>
            -->
            <div class="test_box">

            </div>
        </div>
    *
    * 结构说明
    * data-path:请求路径
    * data-ajax:请求状态,0:为可以请求，1:为不可请求
    *
    * 参数说明
    * elem: 需要延迟加载的项
    * templateID:模版处理的ID
    * templateFn：模版处理函数，传出的参数（_self,json,template）,_self为当前对象，json为对应的数据,templat为js模版处理。
    * errorAjaxFn：出错处理函数，_self为当前对象，其他参照jquery.ajax。出错处理，默认隐藏出错区域。
    * ajaxType: true 为数据交互 false为load此区域的文件
    *
    * 使用说明
    * 结构一样的模块，可以通用一个 templateFn 方法。结构不同的，自己定义 templateFn。
    *
    */
    var scrollAjax = function(config){
        var default_config = {
            elem: ".js-scrollAjax-area",
            type:"post",
            datatype:"json",
            time:618,
            ajaxType:true,
            count:0
        };

        var configs = $.extend(default_config,config);

        for(var prop in configs){
            this[prop] = configs[prop];
        }

        var _view = $(window).height();//可视范围高度
        this._view = _view;

        this.init();
        return this;
    };

    scrollAjax.prototype = {
        init:function(){
            this.areaAjax();
            this._areaFn($(document).scrollTop());
        },
        areaAjax:function(){

            var _this = this;

            $(window).scroll(function(){
                //全部加载完成，取消window的scroll事件
                /*if($(_this.elem).length <= _this.count){
                    $(window).off("scroll");
                }*/

                var _scrollTop = $(document).scrollTop();//滚动条到顶部距离

                _this._areaFn(_scrollTop);

            });

        },
        _areaFn:function(_scrollTop){
            var _this = this;

            $(_this.elem).each(function(){

                var _self = $(this);
                if(_self.data("ajax") == false){
                    return;
                }
                var _top = _self.offset().top,
                _ajax_url = _self.data("path"),
                _ajax_state = _self.data("ajax"),
                _t_st = _top - _scrollTop;

                $(window).resize(function(){
                    //窗口变化，重新获取可视范围高度
                    _this._view = $(window).height();
                });

                if(_t_st <= _this._view && _ajax_state == true){ // _t_st <= _view  表示当前容器在可视区域 或者 滚动条已超过该容器
                    _self.data("ajax",false);
                    if(_this.ajaxType == false){
                        _this.count ++;
                        _this.loadUrl(_self,_ajax_url);
                        return;
                    }
                    if(_this.ajaxType == true){
                        _this.ajaxUrl(_ajax_url,_this.type,_this.datatype,_self);
                    }
                }

            });
        },
        loadUrl:function(a,b){
            setTimeout(function(){
                //a.css({opacity:0});
                a.load(b,function(){
                    a.children().fadeIn();
                });
            },this.time);
        },
        ajaxUrl:function(a,b,c,_self){
            var _this = this;
            setTimeout(function(){
                $.ajax({
                    url:a,
                    type:b,
                    dataType:c,
                    success:function(json){
                        if(_this.templateID){
                            var _html = template.render(_this.templateID,json);
                            $(_self).html(_html);
                            return;
                        }
                        if(_this.templateFn && $.isFunction(_this.templateFn)){
                            _this.templateFn(_self,json,template); //外部处理数据的函数
                        }
                    },
                    error:function(XMLHttpRequest, textStatus, errorThrown){
                        if(_this.errorAjaxFn && $.isFunction(_this.errorAjaxFn)){
                            _this.errorAjaxFn(_self,XMLHttpRequest, textStatus, errorThrown); //外部处理出错的函数
                        }else{
                            _self.hide(); //出错默认处理，隐藏当前容器
                        }
                    }
                });
            },_this.time);
        }
    };

    module.exports = scrollAjax;
});