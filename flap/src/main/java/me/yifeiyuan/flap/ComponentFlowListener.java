package me.yifeiyuan.flap;

/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2019-12-31 11:14
 * @since 1.5
 */
public interface ComponentFlowListener {

    void onBeforeCreate();

    void onAfterCreate();

    void onBeforeBind();

    void onAfterBind();

}
