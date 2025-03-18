--------------

"好的，我将为您生成一个随机的图表示例。

数据：
```json
{
  "categories": ["A", "B", "C", "D", "E"],
  "series": [
    {
      "name": "Series 1",
      "data": [120, 200, 150, 80, 70]
    },
    {
      "name": "Series 2",
      "data": [80, 90, 100, 120, 150]
    }
  ]
}
```

图表类型：柱状图

```echarts
{
  "tooltip": {},
  "legend": {
    "data": ["Series 1", "Series 2"]
  },
  "xAxis": {
    "data": ["A", "B", "C", "D", "E"]
  },
  "yAxis": {},
  "series": [
    {
      "name": "Series 1",
      "type": "bar",
      "data": [120, 200, 150, 80, 70]
    },
    {
      "name": "Series 2",
      "type": "bar",
      "data": [80, 90, 100, 120, 150]
    }
  ]
}
```


图表类型：柱状图2

```echarts
{
  "tooltip": {},
  "legend": {
    "data": ["Series 1", "Series 2"]
  },
  "xAxis": {
    "data": ["A", "B", "C", "D", "E"]
  },
  "yAxis": {},
  "series": [
    {
      "name": "Series 1",
      "type": "bar",
       "smooth": true,
      "data": [120, 200, 150, 80, 70]
    },
    {
      "name": "Series 2",
      "type": "bar",
       "smooth": true,
      "data": [80, 90, 100, 120, 150]
    }
  ]
}
```
图表类型3

```echarts
{
  "title": {
    "text": "支出金额统计",
    "subtext": "2022年1月-2023年9月",
    "textStyle": {
      "color": "#fff"
    }
  },
  "tooltip": {
    "trigger": "axis",
    "axisPointer": {
      "type": "shadow"
    },
    "formatter": "{b}<br/>{a0}: {c0}<br/>{a1}: {c1}%"
  },
  "legend": {
    "data": ["支出金额", "涨跌比例"],
    "orient": "horizontal",
    "bottom": "10",
    "textStyle": {
      "color": "#fff"
    }
  },
  "xAxis": {
    "type": "category",
    "data": ["2022年1月", "2022年2月", "2022年3月", "2022年4月", "2022年5月", "2022年6月", "2022年7月", "2022年8月", "2022年9月", "2022年10月", "2022年11月", "2022年12月", "2023年1月", "2023年2月", "2023年3月", "2023年4月", "2023年5月", "2023年6月", "2023年7月", "2023年8月", "2023年9月"],
    "axisLine": {
      "lineStyle": {
        "color": "#fff"
      }
    },
    "axisLabel": {
      "color": "#fff"
    }
  },
  "yAxis": [
    {
      "type": "value",
      "name": "支出金额",
      "position": "left",
      "axisLine": {
        "lineStyle": {
          "color": "#fff"
        }
      },
      "axisLabel": {
        "formatter": "{value}",
        "color": "#fff"
      },
      "splitLine": {
        "show": true,
        "lineStyle": {
          "color": ["#aaa"],
          "type": "dashed"
        }
      }
    },
    {
      "type": "value",
      "name": "涨跌比例",
      "position": "right",
      "axisLine": {
        "lineStyle": {
          "color": "#fff"
        }
      },
      "axisLabel": {
        "formatter": "{value}%",
        "color": "#fff"
      }
    }
  ],
  "series": [
    {
      "name": "支出金额",
      "type": "bar",
      "data": [548582, 484939, 769054, 643352, 669089, 749881, 624908, 950257, 740968, 530375, 582426, 549291, 525588, 513340, 539107, 498265, 477665, 505812, 488352, 483600, 515058],
      "itemStyle": {
        "color": function(params) {
          return params.data > 600000 ? '#fa541c' : '#1890ff';
        }
      },
      "label": {
        "show": true,
        "position": "top",
        "formatter": function(params) {
          return params.data > 600000 ? '{c}' : '';
        }
      }
    },
    {
      "name": "涨跌比例",
      "type": "line",
      "yAxisIndex": 1,
      "data": [null, -11.6, 58.4, -16.4, 4.0, 12.0, -16.7, 52.0, -22.1, -28.2, 9.7, -6.0, -4.4, -2.3, 5.0, -7.2, -4.1, 5.9, -3.3, -1.1, 6.5],
      "itemStyle": {
        "color": "#fadb14"
      },
      "lineStyle": {
        "width": 2
      },
      "label": {
        "show": true,
        "position": "right",
        "formatter": "{c}%"
      }
    }
  ],
  "backgroundColor": "#000"
}
``` "