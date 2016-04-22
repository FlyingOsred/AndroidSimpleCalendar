package com.flyingosred.app.android.simplecalendar.database;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public final class SolarTermDatabase {

    public static final String TIMEZONE = "GMT+8";

    public static final int START_YEAR = 2016;

    public static final int[][][] DATABASE = {
            // 数据来源自紫金山天文台节气数据
            // 由于算法一样，其他地区比如台湾的数据和紫金山天文台数据一致
            // 格式 [月, 日, 小时, 分钟]
            // 必须从小寒开, 按照顺序排列
            {
                    // 2016 年
                    // 小寒
                    {1, 6, 6, 8},
                    // 大寒
                    {1, 20, 23, 27},
                    // 立春
                    {2, 4, 17, 46},
                    // 雨水
                    {2, 19, 13, 34},
                    // 惊蛰
                    {3, 5, 11, 44},
                    // 春分
                    {3, 20, 12, 30},
                    // 清明
                    {4, 4, 16, 28},
                    // 谷雨
                    {4, 19, 23, 29},
                    // 立夏
                    {5, 5, 9, 42},
                    // 小满
                    {5, 20, 22, 36},
                    // 芒种
                    {6, 5, 13, 49},
                    // 夏至
                    {6, 21, 6, 34},
                    // 小暑
                    {7, 7, 0, 3},
                    // 大暑
                    {7, 22, 17, 30},
                    // 立秋
                    {8, 7, 9, 53},
                    // 处暑
                    {8, 23, 0, 38},
                    // 白露
                    {9, 7, 12, 51},
                    // 秋分
                    {9, 22, 22, 21},
                    // 寒露
                    {10, 8, 4, 33},
                    // 霜降
                    {10, 23, 7, 46},
                    // 立冬
                    {11, 7, 7, 48},
                    // 小雪
                    {11, 22, 5, 22},
                    // 大雪
                    {12, 7, 0, 41},
                    // 冬至
                    {12, 21, 18, 44}
            }
    };
}
