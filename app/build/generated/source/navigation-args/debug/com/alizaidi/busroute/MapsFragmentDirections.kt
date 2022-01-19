package com.alizaidi.busroute

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

class MapsFragmentDirections private constructor() {
  companion object {
    fun actionMapsFragmentToSearchBusFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_mapsFragment_to_searchBusFragment)
  }
}
