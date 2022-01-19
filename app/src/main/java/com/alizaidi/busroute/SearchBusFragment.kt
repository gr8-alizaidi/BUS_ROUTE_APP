package com.alizaidi.busroute

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.alizaidi.busroute.Repository.repo
import com.alizaidi.busroute.adapter.BusSearchAdapter
import com.alizaidi.busroute.models.BusSearch
import com.alizaidi.busroute.models.Route
import com.alizaidi.busroute.retrofit.instance
import kotlinx.android.synthetic.main.fragment_searchbus.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchBusFragment: Fragment(R.layout.fragment_searchbus) {
    lateinit var adapter:BusSearchAdapter
     var routes:ArrayList<Route> = ArrayList()
    val repo : repo =repo()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBus.requestFocus()

        searchBus.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBus.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        getRouteData()
    }

    private fun getRouteData() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.getRouteData()
            if(repo.status==1)
            {
                routes=repo.routes
                withContext(Dispatchers.Main) {
                    setLV()
                }
            }
        }
    }

    private fun setLV() {
        val arrayList= ArrayList<BusSearch>()
        for(i in routes)
            arrayList.add(BusSearch(i.route,"towards ${i.end}"))
         adapter = BusSearchAdapter(requireContext(), arrayList)
        pbar.visibility=GONE
        listview.visibility= VISIBLE
        listview.adapter = adapter
        listview.setOnItemClickListener(){adapterView, view, position, id ->
            Toast.makeText(requireContext(), routes.get(position).route, Toast.LENGTH_LONG).show()
        }
    }
}
