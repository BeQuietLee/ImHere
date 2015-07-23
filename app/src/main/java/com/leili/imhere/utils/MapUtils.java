package com.leili.imhere.utils;

/**
 * Created by Lei.Li on 7/23/15 11:21 AM.
 */
public class MapUtils {
    /**
    * Krasovsky 1940<br>
    * a = 6378245.0 1/f = 298.3 <br>
    * b = a * (1 - f)<br>
    * ee = (a^2 - b^2) / a^2;
    */
    private static double a = 6378245.0;
    private static double ee = 0.00669342162296594323;
    private static double pi = 3.14159265358979324;



    /**
     * GCJ到WGS坐标转换（高德地图为GCJ），较慢，精度高，使用二分极限法
     * @author DingFengHua
     * @since 2014-12-9
     * @param gcjLon
     * @param gcjLat
     * @return
     */
    public static double[] GCJ2WGS_exact(double gcjLon, double gcjLat) {
        double initDelta = 0.01;
        double threshold = 0.000000001;
        double dLat = initDelta, dLon = initDelta;
        double mLat = gcjLat - dLat, mLon = gcjLon - dLon;
        double pLat = gcjLat + dLat, pLon = gcjLon + dLon;
        double wgsLat, wgsLon, i = 0;
        while (true) {
            wgsLat = (mLat + pLat) / 2;
            wgsLon = (mLon + pLon) / 2;
            double[] tmp = WGS2GCJ(wgsLon,wgsLat);
            dLat = tmp[1] - gcjLat;
            dLon = tmp[0] - gcjLon;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
                break;

            if (dLat > 0) pLat = wgsLat; else mLat = wgsLat;
            if (dLon > 0) pLon = wgsLon; else mLon = wgsLon;
            if (++i > 10000) break;
        }
        return new double[]{wgsLon,wgsLat};
    }

    /**
     * GCJ到WGS坐标转换（高德地图为GCJ），快速，精度不高
     * @author DingFengHua
     * @since 2014-12-9
     * @param wgLon
     * @param wgLat
     * @return
     */
    public static double[] GCJ2WGS(double wgLon, double wgLat) {
        if (outOfChina(wgLat, wgLon)) {
            return new double[] { wgLat, wgLon };
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.cbrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        return new double[] { wgLon - dLon, wgLat - dLat };
    }
    /**
     * 设备为WGS,高德地图为GCJ
     *
     * @param wgLat
     * @param wgLon
     * @return
     */
    public static double[] WGS2GCJ(double wgLon, double wgLat) {
        if (outOfChina(wgLat, wgLon)) {
            return new double[] { wgLat, wgLon };
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.cbrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        return new double[] { wgLon + dLon, wgLat + dLat };
    }

    /**
     * 坐标是否在中国范围内
     * @author DingFengHua
     * @since 2014-12-11
     * @param lat
     * @param lon
     * @return
     */
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.cbrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.cbrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }
}
