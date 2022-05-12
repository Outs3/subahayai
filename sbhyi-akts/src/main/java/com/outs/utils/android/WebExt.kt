package com.outs.utils.android

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/5/12 9:37
 * desc:
 */
fun fixImgFillMaxWidthJS() = "javascript:(function(){" +
        "var objs = document.getElementsByTagName('img');" +
        "for(var i=0;i<objs.length;i++)" +
        "{" +
        "var img=objs[i];" +
        "img.style.maxWidth = '100%';img.style.height = 'auto';" +
        "}" +
        "})()"