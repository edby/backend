/* ========================================================================
 * ZUI: messager.js
 * http://zui.sexy
 * ========================================================================
 * Copyright (c) 2014 cnezsoft.com; Licensed MIT
 * ======================================================================== */
define("arale/toast/0.1.1/toast", ["$"], function (require, exports, module) {
    var jQuery = require("$");

    (function ($, window) {
        'use strict';
        if (!String.prototype.format) {
            String.prototype.format = function (args) {
                var result = this;
                if (arguments.length > 0) {
                    var reg;
                    if (arguments.length == 1 && typeof(args) == "object") {
                        for (var key in args) {
                            if (args[key] !== undefined) {
                                reg = new RegExp("({" + key + "})", "g");
                                result = result.replace(reg, args[key]);
                            }
                        }
                    }
                    else {
                        for (var i = 0; i < arguments.length; i++) {
                            if (arguments[i] !== undefined) {
                                reg = new RegExp("({[" + i + "]})", "g");
                                result = result.replace(reg, arguments[i]);
                            }
                        }
                    }
                }
                return result;
            };
        }

        var id = 0;
        var template = '<div class="messager messager-{type} {placement}" id="messager{id}" style="display:none"><div class="messager-content"></div><div class="messager-actions"><button type="button" class="close action">&times;</button></div></div>';
        var defaultOptions = {
            type: 'default',
            placement: 'top',
            time: 1500,
            parent: 'body',
            // clear: false,
            icon: null,
            close: true,
            fade: true,
            scale: true
        };

        var getOptions = function (options) {
            return (typeof options === 'string') ?
                {
                    placement: options
                } : options;
        };

        var lastMessager;


        var Messager = function (message, options) {
            var that = this;
            that.id = id++;
            //console.log(getOptions(options));
            //console.log($);
            options = that.options = $.extend({}, defaultOptions, getOptions(options));
            //console.log(options);
            that.message = (options.icon ? '<i class="icon-' + options.icon + ' icon"></i> ' : '') + message;

            that.$ = $(template.format(options)).toggleClass('fade', options.fade).toggleClass('scale', options.scale).attr('id', 'messager-' + that.id);
            if (!options.close) {
                that.$.find('.close').remove();
            }
            else {
                that.$.on('click', '.close', function () {
                    that.hide();
                });
            }

            that.$.find('.messager-content').html(that.message);


            that.$.data('zui.messager', that);
        };

        Messager.prototype.show = function (message) {
            var that = this,
                options = this.options;

            if (lastMessager) {
                if (lastMessager.id == that.id) {
                    that.$.removeClass('in');
                }
                else if (lastMessager.isShow) {
                    lastMessager.hide();
                }
            }

            if (that.hiding) {
                clearTimeout(that.hiding);
                that.hiding = null;
            }

            if (message) {
                that.message = (options.icon ? '<i class="icon-' + options.icon + ' icon"></i> ' : '') + message;
                that.$.find('.messager-content').html(that.message);
            }

            that.$.appendTo(options.parent).show();

            if (options.placement === 'top' || options.placement === 'bottom' || options.placement === 'center') {
                that.$.css('left', ($(window).width() - that.$.width() - 50) / 2);
            }

            if (options.placement === 'left' || options.placement === 'right' || options.placement === 'center') {
                that.$.css('top', ($(window).height() - that.$.height() - 50) / 2);
            }

            that.$.addClass('in');

            if (options.time) {
                that.hiding = setTimeout(function () {
                    that.hide();
                }, options.time);
            }

            that.isShow = true;
            lastMessager = that;
        };

        Messager.prototype.hide = function () {
            var that = this;
            if (that.$.hasClass('in')) {
                that.$.removeClass('in');
                setTimeout(function () {
                    that.$.remove();
                }, 200);
            }

            that.isShow = false;
        };

        var showMessage = function (message, options) {
            if (typeof options === 'string') {
                options = {
                    type: options
                };
            }
            var msg = new Messager(message, options);
            msg.show();
            return msg;
        };


//    var message = function(message,options){
//    	
//    }


        module.exports = {
            Messager: Messager,
            show: showMessage,
            primary: function (message, options) {
                return showMessage(message, $.extend(
                    {
                        type: 'primary'
                    }, getOptions(options)));
            },
            success: function (message, options) {
                return showMessage(message, $.extend(
                    {
                        type: 'success',
                        icon: 'ok-sign'
                    }, getOptions(options)));
            },
            info: function (message, options) {
                return showMessage(message, $.extend(
                    {
                        type: 'info',
                        icon: 'info-sign'
                    }, getOptions(options)));
            },
            warning: function (message, options) {
                return showMessage(message, $.extend(
                    {
                        type: 'warning',
                        icon: 'warning-sign'
                    }, getOptions(options)));
            },
            danger: function (message, options) {
                return showMessage(message, $.extend(
                    {
                        type: 'danger',
                        icon: 'exclamation-sign'
                    }, getOptions(options)));
            },
            important: function (message, options) {
                return showMessage(message, $.extend(
                    {
                        type: 'important'
                    }, getOptions(options)));
            },
            special: function (message, options) {
                return showMessage(message, $.extend(
                    {
                        type: 'special'
                    }, getOptions(options)));
            }
        };

    }(jQuery, window));

});