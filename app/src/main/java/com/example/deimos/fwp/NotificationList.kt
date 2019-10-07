package com.example.deimos.fwp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.notificationlist.*
import java.lang.Exception
import java.util.ArrayList


class NotificationList : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notificationlist,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backprees.setOnClickListener {

            fragmentManager?.popBackStack()

        }




        val notifications = ArrayList<NotificationModel>()
        notifications.add(NotificationModel("Notification 1","17/5/2019","Info1"))
        notifications.add(NotificationModel("Notification 2","15/9/2019","Info2"))
        notifications.add(NotificationModel("Notification 3","2/10/2019","Info3"))
        notifications.add(NotificationModel("Notification 4","5/5/2019","Info4"))
        notifications.add(NotificationModel("Notification 5","2/3/2019","Info5"))


        try {
            list_recycler_view_noti.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = NotificationAdapter(context,notifications)

            }  }catch (e: Exception){


        }




    }



}