package me.yifeiyuan.flap;

import me.yifeiyuan.flap.extensions.ComponentFlowListener;
/**
 * IFlap is the core interface that defines what Flap can do and how Flap works.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 */
interface IFlap extends ComponentManager, AdapterDelegate {

    void registerFlowListener(ComponentFlowListener componentFlowListener);

    void unregisterFlowListener(ComponentFlowListener componentFlowListener);
}
