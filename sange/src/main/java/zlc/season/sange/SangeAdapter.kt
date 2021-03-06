package zlc.season.sange

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class SangeAdapter<T : Any, VH : ViewHolder>(
    protected open val dataSource: DataSource<T>
) : Adapter<VH>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        dataSource.setAdapter(this)

        //To avoid memory leaks, clean up automatically
        registerAutoClear(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        dataSource.setAdapter(null)
    }

    open fun getItem(position: Int): T {
        return dataSource.getItemInner(position)
    }

    override fun getItemCount(): Int {
        return dataSource.getItemCount()
    }

    private fun registerAutoClear(recyclerView: RecyclerView) {
        recyclerView.addOnAttachStateChangeListener(
            object : View.OnAttachStateChangeListener {
                override fun onViewDetachedFromWindow(v: View?) {
                    recyclerView.adapter = null
                    dataSource.setAdapter(null)
                    recyclerView.removeOnAttachStateChangeListener(this)

                    //Clean up when the Adapter is not needed
                    if (!hasObservers()) {
                        dataSource.cleanUp()
                    }
                }

                override fun onViewAttachedToWindow(v: View?) {
                }
            })
    }
}