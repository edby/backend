/**
 * Created by blocain03 on 2017/6/20.
 */
/*trade.jsp Div自适应*/
$(function(){
    function redraw(){
        var vh = $(window).height();
        var pos =  $('#bit_market').offset().top;
        $('#bit_market').height(vh -pos-20);
        $('.chart-box .table-box').height(vh -pos);
        $('.chart-table').height(Math.floor((vh -pos)/30)*30)
    }
    redraw();
    $(window).resize(function(){
        redraw()
    });
});