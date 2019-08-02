package com.appsamurai.ads.rnbridge

import com.appsamurai.ads.data.AdNetwork

internal fun createAdUnitIdMap(adUnitId: String?, gadAdUnitId: String?): HashMap<AdNetwork, String> {
    return hashMapOf( AdNetwork.APPSAMURAI to adUnitId, AdNetwork.GOOGLE to gadAdUnitId)
            .filterValues { it != null }
            .mapValues { it.value as String } as HashMap<AdNetwork, String>
}
