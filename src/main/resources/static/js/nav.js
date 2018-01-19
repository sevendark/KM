/**
 * Created by jiyuze on 2017/8/1.
 */
function activeNav(nav){
    if(nav){
        switch (nav){
            case '首页': $("li:contains('首页')").first().addClass('active'); break;
            case '文档系统': $("li:contains('文档系统')").first().addClass('active'); break;
            case '问题系统': $("li:contains('问题系统')").first().addClass('active'); break;
            case '登陆': $("li:contains('登陆')").first().addClass('active'); break;
        }
    }
}