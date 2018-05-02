var ctx = "";
window.DOMIN = "";
window.SWF_UPLOAD_URL = window.DOMIN + "/scripts/gallery/plupload/2.1.2/js/Moxie.swf";
window.XAP_UPLOAD_URL = window.DOMIN + "/scripts/gallery/plupload/2.1.2/js/Moxie.xap";
seajs.config({
    base: window.DOMIN + '/scripts',
    alias: {
        '$': 'jquery/1.7.2/jquery',
        'tip': 'arale/tip/1.2.1/tip',
        'dnd': 'arale/dnd/1.0.0/dnd',
        'tabs': 'arale/switchable/1.0.2/tabs',
        'slide': 'arale/switchable/1.0.2/slide',
        'cookie': 'arale/cookie/1.0.2/cookie',
        'popup': 'arale/popup/1.1.5/popup',
        'widget': 'arale/widget/1.1.1/widget',
        'sticky': "arale/sticky/1.3.1/sticky",
        'qrcode': "arale/qrcode/1.0.3/qrcode",
        'dialog': 'arale/dialog/1.2.5/dialog',
        'select': 'arale/select/0.9.9/select',
        'toast':  'arale/toast/0.1.1/toast',
        'overlay': 'arale/overlay/1.1.4/overlay',
        'carousel': 'arale/switchable/1.0.2/carousel',
        'calendar': 'arale/calendar/1.0.0/calendar',
        'validator': 'arale/validator/0.9.6/validator',
        'accordion': 'arale/switchable/1.0.2/accordion',
        'confirmbox': 'arale/dialog/1.2.5/confirmbox',
        'templatable': 'arale/templatable/0.9.2/templatable',
        'autocomplete': "arale/autocomplete/1.3.0/autocomplete",
        'dialog_style': 'arale/dialog/1.2.5/dialog.css',
        'calendar_style': 'arale/calendar/1.0.0/calendar.css',

        'turn': 'gallery/turn/0.0.1/turn',
        'i18n': 'gallery/i18n/1.2.7/i18n',
        'echarts': "gallery/echarts/echarts",
        'ztree': 'gallery/ztree/3.5.3/ztree',
        'sockjs':"gallery/scoket/sockjs.min",
        'Jcrop': "gallery/Jcrop/0.9.12/Jcrop",
        'moment': 'gallery/moment/2.0.0/moment',
        'morris': "gallery/morris/0.5.0/morris",
        'introjs': "gallery/introjs/0.9.0/intro",
        'masonry': "gallery/masonry/3.1.5/masonry",
        'glasses': 'gallery/glasses/0.1.1/glasses',
        'swiper': "gallery/swiper/3.4.2/swiper.min",
        'clipboard': "gallery/clipboard/clipboard.min",
        'jscroll': 'gallery/jsrollbar/1.0.2/jscrollbar',
        'template': 'gallery/template/1.0.1/template',
        'lightbox': 'gallery/lightbox/1.0.0/lightbox',
        'plupload': "gallery/plupload/2.1.2/js/plupload",
        'reconnection':"gallery/scoket/reconnecting.min",
        'pagination': 'gallery/pagination/0.1.2/pagination',
        'underscore': 'gallery/underscore/1.6.0/underscore',
        "validateForm": "gallery/validateForm/1.0.0/validateForm",
        'placeholders': 'gallery/placeholders/3.0.2/placeholders',
        'zeroclipboard': 'gallery/zeroclipboard/1.2.2/zeroclipboard',
        'my97DatePicker': "gallery/my97DatePicker/1.0.0/WdatePicker"
    }
    // , map: [
    //     ['.js', '.js?t=' + Math.random()]
    // ]
});
