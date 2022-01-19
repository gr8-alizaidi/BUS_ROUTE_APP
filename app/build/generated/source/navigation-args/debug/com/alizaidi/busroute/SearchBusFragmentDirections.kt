package com.alizaidi.busroute

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

class SearchBusFragmentDirections private constructor() {
  companion object {
    fun actionSearchBusFragmentToMapsFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_searchBusFragment_to_mapsFragment)
  }
}
