///////////////////////////////////////////////////////////////////////////////
// 차트
var mdf = mdf || function($){};
mdf.Chart = function(chartSelector, chartTitle, yAxisTitle, option) {
    this.init(chartSelector, chartTitle, yAxisTitle, option);
};

$.extend(mdf.Chart.prototype, {
    /**
     * 생성자
     *
     * @param option 옵션
     */
    init : function(chartSelector, chartTitle, yAxisTitle, option) {
        this.option = {
            credits : {  // 우측 하단 Highcharts.com 제거
                text : ''
            },
            global : {
                useUTC : false
            },
            chart : {
                renderTo : chartSelector,
                borderRadius : 0
            },
            title : { // 차트 제목
                text : chartTitle,
                style : {
                    font : 'bold 1.45em 맑은 고딕,Malgun Gothic,굴림,gulim,dotum,돋움,sans-serif'
                }
            },
            xAxis : { // x축
                labels : {
                    //rotation : -35,
                    align : 'right',
                    style : {
                        font : 'normal 1.1em 맑은 고딕,Malgun Gothic,굴림,gulim,dotum,돋움,sans-serif',
                        color : '#6D869F'
                    }
                }
            },
            yAxis : { // y축
                title : {
                    text : yAxisTitle
                }
            },
            plotOptions : {
                area : {
                    dataLabels : {
                        enabled : true
                    }
                },
                bar : {
                    dataLabels : {
                        enabled : true
                    }
                },
                column : {
                    dataLabels : {
                        enabled : true
                    }
                },
                line : {
                    dataLabels : {
                        enabled : true
                    }
                },
                pie : {
                    dataLabels : {
                        enabled : true,
                        //color: Highcharts.theme.textColor || '#000000',
                        //connectorColor: Highcharts.theme.textColor || '#000000',
                        formatter: function() {
                           return this.point.name + ': ' + this.y;
                        }
                    }
                },
                series : {
                    dataLabels : {
                        enabled : true
                    },
                    cursor: 'pointer',
                    events: {
                        click: function(event) {

                        }
                    }
                },
                borderWidth : 1
            },
            legend : {
                borderRadius : 0,
                margin : 25
            },
            tooltip : {}
        };

        this.option = $.extend(true, this.option, option || {});
    },
    renderChart : function(chartType, categories, series) {
        var option = this.option;

        if(chartType == 'PIE') {
            option.tooltip.formatter = function() {
                return this.point.name +': '+ this.y;
            }
        } else {
            option.tooltip.formatter = function() {
                return this.series.name +'<br/>'+ this.x +': '+ this.y;
            }
        }

        switch(chartType) {
        case 'HOR_BAR' :
            option.chart.defaultSeriesType = "bar";
            option.chart.inverted = false;
            option.plotOptions.series.stacking = null;
            option.xAxis.labels.rotation = 0;
            break;
        case 'VER_BAR' :
            option.chart.defaultSeriesType = "column";
            option.chart.inverted = false;
            option.plotOptions.series.stacking = null;
            option.xAxis.labels.rotation = -35;
            break;
        case 'HOR_STACKED_BAR' :
            option.chart.defaultSeriesType = "bar";
            option.chart.inverted = false;
            option.plotOptions.series.stacking = "normal";
            option.xAxis.labels.rotation = 0;
            break;
        case 'VER_STACKED_BAR' :
            option.chart.defaultSeriesType = "column";
            option.chart.inverted = false;
            option.plotOptions.series.stacking = "normal";
            option.xAxis.labels.rotation = -35;
            break;
        case 'HOR_LINE' :
            option.chart.defaultSeriesType = "line";
            option.chart.inverted = true;
            option.plotOptions.series.stacking = null;
            option.xAxis.labels.rotation = 0;
            break;
        case 'VER_LINE' :
            option.chart.defaultSeriesType = "line";
            option.chart.inverted = false;
            option.plotOptions.series.stacking = null;
            option.xAxis.labels.rotation = -35;
            break;
        case 'HOR_AREA' :
            option.chart.defaultSeriesType = "area";
            option.chart.inverted = true;
            option.plotOptions.series.stacking = null;
            option.xAxis.labels.rotation = 0;
            break;
        case 'VER_AREA' :
            option.chart.defaultSeriesType = "area";
            option.chart.inverted = false;
            option.plotOptions.series.stacking = null;
            option.xAxis.labels.rotation = -35;
            break;
        case 'HOR_STACKED_AREA' :
            option.chart.defaultSeriesType = "area";
            option.chart.inverted = true;
            option.plotOptions.series.stacking = null;
            option.xAxis.labels.rotation = 0;
            break;
        case 'VER_STACKED_AREA' :
            option.chart.defaultSeriesType = "area";
            option.chart.inverted = false;
            option.plotOptions.series.stacking = null;
            option.xAxis.labels.rotation = -35;
            break;
        case 'PIE' :
            option.chart.defaultSeriesType = "pie";
            option.chart.inverted = false;
            option.plotOptions.series.stacking = null;
            option.xAxis.labels.rotation = 0;
            break;
        }

        option.xAxis.categories = categories;
        option.series = series;

        return new Highcharts.Chart(option);
    }
});
