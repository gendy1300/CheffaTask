package com.agendy.chefaa.main

import androidx.lifecycle.ViewModel
import com.agendy.chefaa.utils.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(
    val appNavigator: AppNavigator
) : ViewModel() {

    val navigationChannel = appNavigator.navigationChannel


}
