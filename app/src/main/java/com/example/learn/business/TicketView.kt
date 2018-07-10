package com.example.learn.business

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.learn.R
import com.example.learn.text
import com.example.learn.view.AnimationUpdateListener
import com.example.learn.view.ViewState.stateRefresh
import com.example.learn.view.ViewState.*
import kotlinx.android.synthetic.main.ticket_view.view.*

class TicketView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) , AnimationUpdateListener {

    private var firstLayout: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.ticket_view, this)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!firstLayout) {
            firstLayout = true
            stateSave(vBorder1,R.id.vs1).a(1F)
            stateSave(vBorder1,R.id.vs2).ws(3.8F).hs(3.8F).a(0F)

            stateSave(vBorder2,R.id.vs1).a(0F)
            stateSave(vBorder2,R.id.vs2).ws(3.8F).hs(3.8F).a(1F)

            stateSave(vSimple,R.id.vs1)
            stateSave(vSimple,R.id.vs2).a(0f)

            stateSave(layDetail,R.id.vs1)
            stateSave(layDetail,R.id.vs2).sx(1F).sy(1F).a(1F)
        }
    }

    fun set(amount: Int, limit: Int, expireTime: String) {
        vSimple.text("领￥$amount")

        vDetail1.text("￥$amount")
        vDetail2.text("满$limit 可用")
        vDetail3.text("有效期至$expireTime")
    }

    override fun onAnimationUpdate(tag1: Int, tag2: Int, p: Float) {
        arrayOf(vBorder1, vBorder2, vSimple, layDetail).forEach {
            stateRefresh(it,tag1, tag2, p) }
    }
}