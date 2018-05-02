define("gallery/turn/0.0.1/turn", ["$"], function (r, e, m) {
    var $ = r("$");

    var Turn = function (option) {
        var me = this;

        me.element = $(option.element);
        me.panels = option.panels? $(option.panels):$(option.element).find(".tx-turn-item");
        me.count = option.count || 4;
        me.random = option.random || false;
        me.interval = option.interval || 3000;
        me.animates = option.animates || ["flipIn","flipOut"];
        me.much = option.much || 1;


        if (me.random) { me._randomArray(); }
        me.mainArray = [];
        me.tempArray = [];
        me.panels.each(function (i, o) {
            if (i < me.count) {
                me.mainArray.push($(o).html());
            }
            else {
                me.tempArray.push($(o).html())
                $(o).remove();
            };
        }).children().addClass("animated");

        if (me.random) {
            me.panels.each(function (i, o) {
                if (me.mainArray[i]) {
                    $(o).html(me.mainArray[i]);
                }
            });
        };

        var init =  me.init();

        return init
    }

    Turn.prototype = {
        init: function () {
            this._play();
        },
        _play: function () {

            var $me = this;

            setInterval(function () {
               var newRandom = $me._random();
               for(var i in newRandom)
               {
                   $me._toTurn(newRandom[i]);
               }

            }, $me.interval);
        },
        _toTurn: function (index) {
            var $me = this;
            
            $me.tempArray.push($($me.panels[index]).html());
            var old = $($me.panels[index]).children();
            var now = $($me.tempArray.shift());

            $me._turnOut(old, function () {
                $($me.panels[index]).html($(now).hide());
                $me._turnIn(now);
            });
        },
        _turnIn: function (obj, callback) {
            /*if (!this._isH5()) {
                $(obj).fadeIn(300, function () {
                    if (callback) callback(obj);
                });
            }
            else {*/
                $(obj).show().removeClass(this.animates[1]).addClass(this.animates[0]);
                setTimeout(function () {
                    if (callback) callback(obj);
                }, 500);
            //}
        },
        _turnOut: function (obj, callback) {
           /* if (!this._isH5()) {
                $(obj).fadeOut(300, function () {
                    if (callback) callback(obj);
                });
            }
            else {*/
                $(obj).removeClass(this.animates[0]).addClass(this.animates[1]);
                setTimeout(function () {
                    if (callback) callback(obj);
                },500);
            //}
        },

        _randomArray: function () {
            this.panels.sort(function () {
                return 0.5 - Math.random();
            });
        },
        _random: function () {
            var $me = this;
            var ret = [];
            for(var i = 0;i<$me.much;i++)
            {
                var r = (Math.random() * ($me.count - 1)).toFixed(0);
                ret.push(r);
            }
            ret = $me.Unique(ret);
            return ret;
        },
        _isH5: function () {
            return (typeof (Worker) !== "undefined") ? true : false;
        },
        Unique: function(arr){
            arr.sort();
            var re = [arr[0]];
            for(var i = 1;i<arr.length;i++)
            {
                if(arr[i]!==re[re.length-1])
                {
                    re.push(arr[i]);
                }
            }
            return re;
        }
    }

    m.exports = Turn;
});