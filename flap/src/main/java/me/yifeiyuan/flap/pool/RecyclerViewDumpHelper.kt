package me.yifeiyuan.flap.pool

import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Field
import java.util.*

/**
 * Dump RecyclerView 的缓存情况
 *
 * Created by 程序亦非猿 on 2022/8/15.
 */

private const val TAG = "RecyclerViewDumpHelper"

class RecyclerViewDumpHelper(val recyclerView: RecyclerView) {

    private val recyclerMediator: RecyclerMediator = RecyclerMediator(recyclerView)

    fun dumpRecycler(allDetails: Boolean = true): String {
        return if (allDetails) recyclerMediator.dumpDetails() else recyclerMediator.dump()
    }

    fun dumpRecycledViewPool(allDetails: Boolean = true): String {
        return if (allDetails) recyclerMediator.poolMediator.dumpDetails() else recyclerMediator.poolMediator.dump()
    }
}

@Suppress("UNCHECKED_CAST")
class RecyclerMediator(val recyclerView: RecyclerView) {

    private val recycler: RecyclerView.Recycler

    var mAttachedScrap: ArrayList<RecyclerView.ViewHolder>? = null
        get() {
            return mAttachedScrapField.get(recycler) as? ArrayList<RecyclerView.ViewHolder>
        }

    var mChangedScrap: ArrayList<RecyclerView.ViewHolder>? = null
        get() {
            return mChangedScrapField.get(recycler) as? ArrayList<RecyclerView.ViewHolder>
        }

    var mCachedViews: ArrayList<RecyclerView.ViewHolder>? = null
        get() {
            return mCachedViewsField.get(recycler) as? ArrayList<RecyclerView.ViewHolder>
        }

    var mUnmodifiableAttachedScrap: List<RecyclerView.ViewHolder>? = null
        get() {
            return recycler.scrapList
        }

    var mViewCacheExtension: RecyclerView.ViewCacheExtension? = null
        get() {
            return mViewCacheExtensionField.get(recycler) as? RecyclerView.ViewCacheExtension
        }

    var mRecyclerPool: RecyclerView.RecycledViewPool? = null
        get() {
            return mRecyclerPoolField.get(recycler) as? RecyclerView.RecycledViewPool
        }

    var mRequestedCacheMax: Int? = null
        get() {
            return mRequestedCacheMaxField.get(recycler) as? Int
        }

    var mViewCacheMax: Int? = null
        get() {
            return mViewCacheMaxField.get(recycler) as? Int
        }

    private val mRecyclerPoolField: Field
    private val mAttachedScrapField: Field
    private val mChangedScrapField: Field
    private val mCachedViewsField: Field
    private val mUnmodifiableAttachedScrapField: Field
    private val mViewCacheExtensionField: Field
    private val mRequestedCacheMaxField: Field
    private val mViewCacheMaxField: Field

    val poolMediator: RecycledViewPoolMediator

    init {
        val mRecyclerField = RecyclerView::class.java.getDeclaredField("mRecycler").also {
            it.isAccessible = true
        }

        recycler = mRecyclerField.get(recyclerView) as RecyclerView.Recycler

        val recyclerClass = Class.forName(RecyclerView::class.java.name + "$" + "Recycler")

        recyclerClass.run {
            mRecyclerPoolField = recyclerClass.getDeclaredField("mRecyclerPool").also {
                it.isAccessible = true
            }
            mAttachedScrapField = getDeclaredField("mAttachedScrap").also {
                it.isAccessible = true
            }
            mChangedScrapField = getDeclaredField("mChangedScrap").also {
                it.isAccessible = true
            }
            mCachedViewsField = getDeclaredField("mCachedViews").also {
                it.isAccessible = true
            }
            mUnmodifiableAttachedScrapField = getDeclaredField("mUnmodifiableAttachedScrap").also {
                it.isAccessible = true
            } // recycler.scrapList
            mViewCacheExtensionField = getDeclaredField("mViewCacheExtension").also {
                it.isAccessible = true
            }
            mRequestedCacheMaxField = getDeclaredField("mRequestedCacheMax").also {
                it.isAccessible = true
            }
            mViewCacheMaxField = getDeclaredField("mViewCacheMax").also {
                it.isAccessible = true
            }
        }

        poolMediator = RecycledViewPoolMediator(mRecyclerPool!!)
    }

    fun clear() {
        recycler.clear()
    }

    /**
     * dump 简化过的数据
     */
    fun dump(): String {
        val stringBuilder = StringBuilder()
                .appendLine()
                .appendLine(">>>>>>> Recycler Start <<<<<<<<")
                .appendLine("mRequestedCacheMax=$mRequestedCacheMax")
                .appendLine("mViewCacheMax=$mViewCacheMax")
                .appendLine("mAttachedScrap:")
                .appendLine("mAttachedScrap.size=${mAttachedScrap?.size}")
                .appendLine("mAttachedScrap=$mAttachedScrap")
                .appendLine("mChangedScrap:")
                .appendLine("mChangedScrap.size=${mChangedScrap?.size}")
                .appendLine("mChangedScrap=$mChangedScrap")
                .appendLine("mCachedViews:")
                .appendLine("mCachedViews.size=${mCachedViews?.size}")
                .appendLine("mCachedViews=$mCachedViews")
                .appendLine("${poolMediator.dump()}")
                .appendLine(">>>>>>> Recycler End <<<<<<<<")
        return stringBuilder.toString()
    }

    /**
     * dump 所有细节信息
     */
    fun dumpDetails(): String {
        val stringBuilder = StringBuilder()
                .appendLine()
                .appendLine(">>>>>>> Recycler Start <<<<<<<<")
                .appendLine("mAttachedScrap:")
                .appendLine("mAttachedScrap.size=${mAttachedScrap?.size}")
                .appendLine("mAttachedScrap=$mAttachedScrap")
                .appendLine("mUnmodifiableAttachedScrap:")
                .appendLine("mUnmodifiableAttachedScrap.size=${mUnmodifiableAttachedScrap?.size}")
                .appendLine("mUnmodifiableAttachedScrap=$mUnmodifiableAttachedScrap")
                .appendLine("mRequestedCacheMax=$mRequestedCacheMax")
                .appendLine("mViewCacheMax=$mViewCacheMax")
                .appendLine("mChangedScrap:")
                .appendLine("mChangedScrap.size=${mChangedScrap?.size}")
                .appendLine("mChangedScrap=$mChangedScrap")
                .appendLine("mCachedViews:")
                .appendLine("mCachedViews.size=${mCachedViews?.size}")
                .appendLine("mCachedViews=$mCachedViews")
                .appendLine("mViewCacheExtension:")
                .appendLine("mViewCacheExtension=$mViewCacheExtension")
                .appendLine("mRecyclerPool:")
                .appendLine("mRecyclerPool:$mRecyclerPool")
                .appendLine("mRecyclerPool.dump:${poolMediator.dumpDetails()}")
                .appendLine(">>>>>>> Recycler End <<<<<<<<")
        return stringBuilder.toString()
    }

    override fun toString(): String {
        return "RecyclerMediator(recyclerView=$recyclerView, recycler=$recycler, mAttachedScrap=$mAttachedScrap, mChangedScrap=$mChangedScrap, mCachedViews=$mCachedViews, mUnmodifiableAttachedScrap=$mUnmodifiableAttachedScrap, mViewCacheExtension=$mViewCacheExtension, mRecyclerPool=$mRecyclerPool, mRequestedCacheMax=$mRequestedCacheMax, mViewCacheMax=$mViewCacheMax)"
    }
}

class RecycledViewPoolMediator(private val pool: RecyclerView.RecycledViewPool) {

    val mScrap: SparseArray<Any>
        get() {
            return mScrapField.get(pool) as SparseArray<Any>
        }

    val mAttachCount: Int
        get() {
            return mAttachCountField.getInt(pool)
        }

    private val mScrapField: Field
    private val mAttachCountField: Field

    init {
        mScrapField = RecyclerView.RecycledViewPool::class.java.getDeclaredField("mScrap").also {
            it.isAccessible = true
        }

        mAttachCountField = RecyclerView.RecycledViewPool::class.java.getDeclaredField("mAttachCount").also {
            it.isAccessible = true
        }
    }

    override fun toString(): String {
        return "RecycledViewPool(mAttachCount=$mAttachCount),mScrap=$mScrap)"
    }

    /**
     * dump RecycledViewPool 简化过的信息
     */
    fun dump(): String {
        val stringBuilder = StringBuilder()
                .appendLine()
                .appendLine("======= Recycler.RecycledViewPool Start =======")
                .appendLine("mAttachCount=$mAttachCount")
                .appendLine("mScrap.size=${mScrap.size()}")
                .appendLine("mScrap Details:")

        for (i in 0 until mScrap.size()) {
            val itemViewType = mScrap.keyAt(i)
            val scrapData = mScrap.valueAt(i)
            val scrapDataMediator = ScrapDataMediator(scrapData)
            stringBuilder.appendLine("index=$i,itemViewType=$itemViewType,scrapData.size=${scrapDataMediator.mScrapHeap.size},scrapData=$scrapDataMediator")
        }

        stringBuilder.appendLine("======= Recycler.RecycledViewPool End =======")
        return stringBuilder.toString()
    }

    /**
     * dump RecycledViewPool 所有细节信息
     */
    fun dumpDetails(): String {
        val stringBuilder = StringBuilder()
                .appendLine()
                .appendLine("======= Recycler.RecycledViewPool Start =======")
                .appendLine("mAttachCount=$mAttachCount")
                .appendLine("mScrap:")
                .appendLine("mScrap.size=${mScrap.size()}")
                .appendLine("mScrap=$mScrap")
                .appendLine()

        for (i in 0 until mScrap.size()) {
            val itemViewType = mScrap.keyAt(i)
            val scrapData = mScrap.valueAt(i)
            val scrapDataMediator = ScrapDataMediator(scrapData)
            stringBuilder.appendLine("index=$i,itemViewType=$itemViewType,scrapData.size=${scrapDataMediator.mScrapHeap.size},scrapData=$scrapDataMediator")
        }

        stringBuilder.appendLine("======= Recycler.RecycledViewPool End =======")
        return stringBuilder.toString()
    }
}

class ScrapDataMediator(private val scrapData: Any) {

    val mScrapHeap: ArrayList<RecyclerView.ViewHolder>
        get() {
            return mScrapHeapField.get(scrapData) as ArrayList<RecyclerView.ViewHolder>
        }
    val mMaxScrap: Int
        get() {
            return mMaxScrapField.getInt(scrapData)
        }

    private val mScrapHeapField: Field
    private val mMaxScrapField: Field
    private val mCreateRunningAverageNsField: Field
    private val mBindRunningAverageNsField: Field

    private val scrapDataClass: Class<*>

    init {

        val scrapDataClassName = RecyclerView::class.java.name + "$" + "RecycledViewPool" + "$" + "ScrapData"
        scrapDataClass = Class.forName(scrapDataClassName)

        mScrapHeapField = scrapDataClass.getDeclaredField("mScrapHeap").also {
            it.isAccessible = true
        }
        mMaxScrapField = scrapDataClass.getDeclaredField("mMaxScrap").also {
            it.isAccessible = true
        }
        mCreateRunningAverageNsField = scrapDataClass.getDeclaredField("mCreateRunningAverageNs").also {
            it.isAccessible = true
        }
        mBindRunningAverageNsField = scrapDataClass.getDeclaredField("mBindRunningAverageNs").also {
            it.isAccessible = true
        }
    }

    override fun toString(): String {
        return "ScrapData(mMaxScrap=$mMaxScrap,mScrapHeap=$mScrapHeap)"
    }

}