<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="nav navbar-nav lg-switch">
    <li class="dropdown" id="lg_switch">
        <a href="javascript:;" id="lg_curr" class="dropdown-toggle" data-toggle="dropdown"><img src="${imagesPath}/bitms/icon-state2.png" alt="">简体中文<strong class="caret"></strong></a>
        <ul class="dropdown-menu" aria-labelledby="dropdown">
            <li data="zh_CN" class="lg-item">
                <a href="javascript:;" onclick="changeLanguage('zh_CN')"><img src="${imagesPath}/bitms/icon-state2.png" alt="">简体中文</a>
            </li>
            <li class="divider">
            </li>
            <li data="zh_HK" class="lg-item">
                <a href="javascript:;" onclick="changeLanguage('zh_HK')"><img src="${imagesPath}/bitms/icon-state2.png" alt="">繁体中文</a>
            </li>
            <li class="divider">
            </li>
            <li data="en_US" class="lg-item">
                <a href="javascript:;" onclick="changeLanguage('en_US')"><img src="${imagesPath}/bitms/icon-state1.png" alt=""/>English</a>
            </li>
        </ul>
    </li>
</ul>
<script>
    seajs.use(['cookie'], function (Cookie) {
        var lang = Cookie.get('locale') ;
        if (!lang || lang.length < 2) {
            lang = (navigator.languages && navigator.languages.length > 0) ? navigator.languages[0]
                : (navigator.language || navigator.userLanguage);
            if ("zh" == lang) lang = "zh_CN";
            if ("en" == lang) lang = "en_US";
        }
        lang = lang.toLowerCase();
        lang = lang.replace(/-/, "_");
        if (lang.length > 3) {
            lang = lang.substring(0, 3) + lang.substring(3).toUpperCase();
        }
        window.locale= lang;//将语言投放到全局
       $('#lg_switch li.lg-item').each(function(i, item){
          if($(item).attr('data')==(lang)){
               var content = $(this).find('a').html();
               $('#lg_curr').html(content+'<strong class="caret"></strong>');
           }
       })
    });
</script>