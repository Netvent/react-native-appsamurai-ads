package com.appsamurai.ads.rnbridge

import com.appsamurai.ads.data.AdNetwork
import com.facebook.react.bridge.ReadableMap
import java.util.HashMap

object Utils {
    val LOGTAG = "ASRNBridge"

    public fun convertAdUnitIdMap(adUnitIDs: ReadableMap): HashMap<AdNetwork, String> {
        val map = HashMap<AdNetwork, String>()
        if (adUnitIDs.hasKey(AdNetwork.APPSAMURAI.value)) {
            map[AdNetwork.APPSAMURAI] = adUnitIDs.getString(AdNetwork.APPSAMURAI.value)!!
        }

        if (adUnitIDs.hasKey(AdNetwork.GOOGLE.value)) {
            map[AdNetwork.GOOGLE] = adUnitIDs.getString(AdNetwork.GOOGLE.value)!!
        }

        return map
    }

}
