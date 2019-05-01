package hu.aut.bme.androidchatter.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

class EmptyRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    var emptyView: TextView? = null
    var emptyMessage: String? = null

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = this.adapter
        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)

        checkIfEmpty()
    }

    fun checkIfEmpty() {
        val adapter = adapter
        val emptyView = emptyView

        if (adapter != null && emptyView != null) {
            if (adapter.itemCount == 0) {
                visibility = View.GONE
                emptyView.visibility = View.VISIBLE
                emptyView.text = emptyMessage
            } else {
                visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            }
        }
    }
}