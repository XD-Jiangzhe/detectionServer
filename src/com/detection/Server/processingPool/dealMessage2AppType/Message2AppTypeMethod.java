package com.detection.Server.processingPool.dealMessage2AppType;

import java.util.ArrayList;

//这里定义了一个处理接口 abstract factory 模式
public interface Message2AppTypeMethod {

    //将传入的string 根据type 的类型转成各个形式
    ArrayList<String> dealwithTransferTxt(String transferTxt);

    //根据上面转成的list 再经过不同的分析函数得到相应的类型
    String detectionStrategy(ArrayList<String> contentList);
}


