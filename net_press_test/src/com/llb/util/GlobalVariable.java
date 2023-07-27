package com.llb.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建时间：2020/12/17
 * 创建供所有线程操作的全局变量类
 * @author wpf
 */

public class GlobalVariable {

    /*
    考虑到多线程操作，因此需要使用到原子类
     */
    public static AtomicInteger successThreadCount = new AtomicInteger(0); //表示成功线程的数量
    public static AtomicInteger failureThreadCount = new AtomicInteger(0);; //表示失败线程的数量

    public static String filePath = "F://net-press-test.txt";

    public static String urlRegx = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
    public static String numRegex = "^[\\d]*$";
}
