package com.happs.ximand.ringcontrol.model.bl.exception

import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException

class DiscoveryModeNotStartedException : BaseException(CODE) {

    companion object {
        const val CODE = 0x21
    }

    override val descriptionResId: Int = com.happs.ximand.ringcontrol.R.string.discovery_mode_not_started
    override val showRestartLayout = true
    override val showSupportLayout = true
}