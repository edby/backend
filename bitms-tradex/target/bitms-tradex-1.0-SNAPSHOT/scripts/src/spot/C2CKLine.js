var rawData = null;
var dates = null;
var data = [];
var dataS = [];

function calculateMA(dayCount, data) {
    var result = [];
    for (var i = 0, len = data.length; i < len; i++) {
        if (i < dayCount) {
            result.push('-');
            continue;
        }
        var sum = 0;
        for (var j = 0; j < dayCount; j++) {
            sum += data[i - j][1];
        }
        if(isNaN(sum/dayCount))
        {
            result.push((0).toFixed(8));
        }else
        {
            result.push(parseFloat(sum/dayCount).toFixed(8));
        }
    }
    return result;
}

function renderKline(message) {
    rawData = eval(message.msgContent).reverse();
    dates = rawData.map(function (item) {
        return item[0];
    });

    dataS = rawData.map(function (item) {
        return [item[1], item[2], item[3], item[4]];
    });

    data = rawData.map(function (item) {
        return [+item[1], +item[2], +item[3], +item[4]];
    });

    myChart.setOption({
        xAxis: {
            type: 'category',
            data: dates,
            axisLine: {lineStyle: {color: '#8392A5'}}
        },
        series: [
            {
                type: 'candlestick', name: lineName, data: dataS,
                itemStyle: {normal: {color: '#0CF49B', color0: '#FD1050', borderColor: '#0CF49B', borderColor0: '#FD1050'}}
            }/*,
            {
                name: 'MA5', type: 'line', data: calculateMA(5, data), smooth: true, showSymbol: false, lineStyle: {normal: {width: 1}}
            },
            {
                name: 'MA15', type: 'line', data: calculateMA(15, data), smooth: true, showSymbol: false, lineStyle: {normal: {width: 1}}
            },
            {
                name: 'MA30', type: 'line', data: calculateMA(30, data), smooth: true, showSymbol: false, lineStyle: {normal: {width: 1}}
            },
            {
                name: 'MA60', type: 'line', data: calculateMA(60, data), smooth: true, showSymbol: false, lineStyle: {normal: {width: 1}}
            }*/
        ]
    });
}