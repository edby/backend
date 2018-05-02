define(function(require, exports, module){

    var Widget = require("widget");
    var Validator = require("validator");

    var PCAS = require("pcas");
    var ZTTX = require("tool");

    /**
     * @param config
     *
     * isAjax: 是否是ajax提交
     * formSelector： 表单选择器
     * editor：编辑器对象，如：
     *          editor: {
                    id: "myEditor1",
                    width: 650,
                    height: 400,
                    display: "品牌简介",
                    isValidate: true
                },
     * pcas: 省市县级联对象，如：
              pcas: {
                province: "#p",
                city: "#c",
                area: "#a",
                currentP: "330000",
                currentC: "330200",
                currentA: "330212"
              }
     * chooseOne: 二选一的功能（传数组的selector，最多只支持到三个，即手机、电话区号及电话号码）
     * passwordStrength：密码强度，如：
        passwordStrength: {
                elem: "#password",
                dColor: "#D0D0D0",
                lColor: "#FF0000",
                mColor: "#FF9900",
                hColor: "#33CC00",
                tag: ".passwordStrength span"   //密码强度容器标签
            },
     * otherFn: 用来处理其他的功能，比如说预览功能，需要表单校验
     * addElemFn: 添加新的校验函数，第一个参数Core对象为Validator的实体对象，通过它可以.addItem，第二个参数Validator可用于新增一些规则
     * beforeSubmit: 注意此方法不能阻止表单提交，只是用来在表单提交前的赋值操作，适用于ajax或非ajax
     * ajaxURL: ajax的请求路径
     * timeout: 请求时间
     * ajaxData:　ajax的data
     * ajaxSuccessFn： 处理ajax的成功函数
     * ajaxErrorFn: 处理ajax的失败函数
     */
    function validateForm(config){

        var setting = {
            "isAjax": false
        }

        var config = $.extend(setting,config);

        for(var prop in config){
            this[prop] = config[prop];
        }

        this.isPCAS = !! this.pcas;

        this.isFiles = !! this.files;

        if(this.isFiles){
            $.each(this.files,function(i,obj){
                if(typeof obj.isValidate == "undefined"){
                    obj.isValidate = true;
                }
                if(typeof obj.isAjax == "undefined"){
                    obj.isAjax = true;
                }
            });
        }

        this.validate();

    }
    validateForm.prototype = {

        validate: function(){

            var _this = this;

            if(this.isPCAS){
                this.handlePCAS();    //处理省市县级联
            }

            if(this.isFiles){
                this.handleFiles();   //处理文件上传
            }

            if(!!this.passwordStrength){
                this.handlePasswordStrength();  //处理密码强度
            }

            Widget.autoRenderAll();

            var Core = Validator.query(this.formSelector);

            if (this.isAjax) {
                Core.set("autoSubmit", false);
            }

            //处理省市县
            if(this.isPCAS){
                Core.addItem({
                    element: _this.pcas.city,
                    required: true,
                    showMessage: function(message, element) {
                        var startErr = $.trim(this.getExplain(element).html());
                        if (!startErr) {
                            this.getExplain(element).html(message);
                            this.getItem(element).addClass(this.get('itemErrorClass'));
                        }
                    },
                    hideMessage: function(message, element) {
                        var startErr = $.trim(this.getExplain(element).html());
                        if (!startErr) {
                            this.getExplain(element).html(element.attr('data-explain') || ' ');
                            this.getItem(element).removeClass(this.get('itemErrorClass'));
                        }
                    }
                }).addItem({
                    element: _this.pcas.area,
                    required: true,
                    showMessage: function(message, element) {
                        var startErr = $.trim(this.getExplain(element).html());
                        if (!startErr) {
                            this.getExplain(element).html(message);
                            this.getItem(element).addClass(this.get('itemErrorClass'));
                        }
                    },
                    hideMessage: function(message, element) {
                        var startErr = $.trim(this.getExplain(element).html());
                        if (!startErr) {
                            this.getExplain(element).html(element.attr('data-explain') || ' ');
                            this.getItem(element).removeClass(this.get('itemErrorClass'));
                        }
                    }
                });

            }

            //处理二选一
            if($.type(this.chooseOne) == "array" && this.chooseOne.length >= 2){

                Core.addItem({
                    element: _this.chooseOne[0],
                    required: function(){
                        return $.trim($(_this.chooseOne[1]).val()) == "" ? true: false;
                    }
                }).addItem({
                    element: _this.chooseOne[1],
                    required: function(){
                        return $.trim($(_this.chooseOne[0]).val()) == "" ? true: false;
                    }
                })

                if(_this.chooseOne[2]){

                    Core.addItem({
                        element: _this.chooseOne[2],
                        required: function(){
                            return $.trim($(_this.chooseOne[0]).val()) == "" ? true: false;
                        },
                        showMessage: function(message, element) {
                            var startErr = $.trim(this.getExplain(element).html());
                            if (!startErr) {
                                this.getExplain(element).html(message);
                                this.getItem(element).addClass(this.get('itemErrorClass'));
                            }
                        },
                        hideMessage: function(message, element) {
                            var startErr = $.trim(this.getExplain(element).html());
                            if (!startErr) {
                                this.getExplain(element).html(element.attr('data-explain') || ' ');
                                this.getItem(element).removeClass(this.get('itemErrorClass'));
                            }
                        }
                    })

                }

            }

            //处理文件
            if(this.isFiles){
                $.each(_this.files,function(i,obj){
                    if(!obj.isAjax && obj.isValidate){
                        Core.addItem({
                            element: "#"+obj.id,
                            required: true,
                            errormessageRequired: "请选择文件"
                        });
                    }
                    if(obj.isAjax && obj.isValidate){
                        Core.addItem({
                            element: "#"+obj.id+"_hidden",
                            required: true,
                            errormessageRequired: "请选择文件"
                        })
                    }
                })
            }

            if (this.otherFn && $.isFunction(this.otherFn)) {
                this.otherFn(Core, Validator);
            }

            if (this.addElemFn && $.isFunction(this.addElemFn)) {
                this.addElemFn(Core, Validator);
            }

            Core.on('formValidated', function (error) {
                if (!error) {

                    _this.beforeSubmit && $.isFunction(_this.beforeSubmit) && _this.beforeSubmit();

                    if( _this.isAjax){

                        var form_str = $(_this.formSelector).serialize();

                        var data = $.extend(_this.ajaxData,ZTTX.unparam(form_str));

                        ZTTX.ajax({
                            url: _this.ajaxURL,
                            data: data,
                            timeout: _this.timeout,
                            type: "submit",
                            form: _this.formSelector,
                            successFn: _this.ajaxSuccessFn,
                            errorFn: _this.ajaxErrorFn
                        })

                    }
                }
            });

        },
        handleEditor: function(){

            var editor = UE.getEditor(this.editor.id, {
                initialFrameWidth: this.editor.width || 300,
                initialFrameHeight: this.editor.height || 300
            });

            //加上vertical-tip
            $("#" + this.editor.id).parents(".ui-form-item").addClass("vertical-tip").append("<input id='editor_hidden' type='hidden' value='123' />");

            Validator.addRule('editor', function () {
                return editor.hasContents();
            }, '请输入{{display}}');

        },
        handlePCAS: function(){

            var _this = this;

            new PCAS({
                province: _this.pcas.province,
                city: _this.pcas.city,
                area: _this.pcas.area,
                currentP: _this.pcas.currentP || "",
                currentC: _this.pcas.currentC || "",
                currentA: _this.pcas.currentA || ""
            })
        },
        handleFiles: function(){
            var _this = this;
            $.each(_this.files,function(i,obj){
                var op = $("#"+obj.id).parents(".ui-form-item");
                op.addClass("vertical-tip");
                if(obj.isAjax && obj.isValidate){
                    op.append('<input id="'+obj.id+'_hidden" type="hidden" value="" />');
                    ZTTX.uploadFiles({
                        ids: obj.id,
                        successFn: function(uploadId,data){
                            $("#"+obj.id+"_hidden").val("1");
                            setTimeout(function(){
                                obj.ajaxSuccess(uploadId,data);
                            },0)
                        }
                    })
                }
            })
        },
        handlePasswordStrength: function(){
            var _this = this;
            this.dColor = this.passwordStrength.dColor;
            this.lColor = this.passwordStrength.lColor;
            this.mColor = this.passwordStrength.mColor;
            this.hColor = this.passwordStrength.hColor;
            $(this.passwordStrength.elem).on("keyup blur",function(){
                _this._pwStrength($(this).val());
            })
        },
        _pwStrength: function(value){
            var O_color = this.dColor;
            var L_color = this.lColor;
            var M_color = this.mColor;
            var H_color = this.hColor;
            if (value==null||value==''){
                Lcolor = Mcolor = Hcolor = O_color;
            }else {
                var S_level = this._checkStrong(value);
                switch (S_level) {
                    case 0:
                        Lcolor = Mcolor = Hcolor = O_color;
                    case 1:
                        Lcolor = L_color;
                        Mcolor = Hcolor = O_color;
                        break;
                    case 2:
                        Lcolor = Mcolor = M_color;
                        Hcolor = O_color;
                        break;
                    default:
                        Lcolor = Mcolor = Hcolor = H_color;
                }
            }
            var ospan = $(this.formSelector).find(this.passwordStrength.tag);
            ospan.eq(0).css("backgroundColor",Lcolor);
            ospan.eq(1).css("backgroundColor",Mcolor);
            ospan.eq(2).css("backgroundColor",Hcolor);
        },
        _checkStrong: function(value){
            if (value.length<=4)
                return 0; //密码太短
            var Modes=0;
            for (var i=0;i<value.length;i++){
                //密码模式
                Modes|= this._CharMode(value.charCodeAt(i));
            }
            return this._bitTotal(Modes);
        },
        _CharMode: function(iN){
            if (iN>=48 && iN <=57) //数字
                return 1;
            if (iN>=65 && iN <=90) //大写
                return 2;
            if (iN>=97 && iN <=122) //小写
                return 4;
            else
                return 8;
        },
        _bitTotal: function(num){
            var modes=0;
            for (var i=0;i<4;i++){
                if (num & 1) modes++;
                num>>>=1;
            }
            return modes;
        }
    }

    module.exports = validateForm;

})