package com.alizaidi.busroute.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.alizaidi.busroute.models.BusSearch
import android.widget.Filter
import android.widget.Filterable
import com.alizaidi.busroute.R
import java.util.*


class BusSearchAdapter(private val context: Context, private val arrayList: java.util.ArrayList<BusSearch>) : BaseAdapter(),Filterable {
    private lateinit var bno: TextView
    private var flist: List<BusSearch> = arrayList
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                flist = filterResults.values as List<BusSearch>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    arrayList
                else
                    arrayList.filter {
                        it.bno.lowercase(Locale.getDefault()).contains(queryString) ||
                                it.bd.lowercase(Locale.getDefault()).contains(queryString)
                    }
                return filterResults
            }
        }
    }

    private lateinit var bd: TextView
    override fun getCount(): Int {
        return flist.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.bus_search, parent, false)
        bno = convertView.findViewById(R.id.busno)
        bd = convertView.findViewById(R.id.busdir)
        bno.text = flist[position].bno
        bd.text = flist[position].bd
        return convertView
    }
}