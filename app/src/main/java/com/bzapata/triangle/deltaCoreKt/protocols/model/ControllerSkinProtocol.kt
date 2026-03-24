package com.bzapata.triangle.deltaCoreKt.protocols.model

import android.graphics.RectF
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.DpSize
import com.bzapata.triangle.deltaCoreKt.model.ControllerSkins

sealed interface ControllerSkinProtocol {
    var name : String
    var identifier : String
    var gameType : GameType
    var isDebugEnabled : Boolean
    fun supports(traits: ControllerSkins.Traits) : Boolean

    fun image(traits: ControllerSkins.Traits, preferredSize : ControllerSkins.Size) : ImageBitmap // maybe im unsure about this type
    fun thumbstick(item: ControllerSkins.Item, traits: ControllerSkins.Traits, preferredSize : ControllerSkins.Size) : Pair<ImageBitmap, DpSize> // unsure about this one too

    fun items(traits : ControllerSkins.Traits) : List<ControllerSkins.Item>?

    fun isTranslucent(traits: ControllerSkins.Traits) : Boolean

    fun gameScreenFrame(traits : ControllerSkins.Traits) : RectF? {
        return this.screens(traits)?.first()?.outputFrame
    }
    fun screens(traits : ControllerSkins.Traits) : List<ControllerSkins.Screen>?

    fun aspectRatio(traits : ControllerSkins.Traits) : DpSize?
    fun contentSize(traits : ControllerSkins.Traits) : DpSize?

    fun menuInsets(traits : ControllerSkins.Traits) : PaddingValues?

    fun supportedTraits(traits: ControllerSkins.Traits) : ControllerSkins.Traits? {
        var currentTraits: ControllerSkins.Traits = traits
        while (!this.supports(currentTraits)) {
            currentTraits = when (traits.device) {
                ControllerSkins.Device.IPHONE if traits.displayType == ControllerSkins.DisplayType.EDGE_TO_EDGE -> {
                    currentTraits.copy(
                        displayType = ControllerSkins.DisplayType.STANDARD,
                        )
                }
                ControllerSkins.Device.IPAD -> {
                    currentTraits.copy(
                    device = ControllerSkins.Device.IPHONE,
                    displayType = ControllerSkins.DisplayType.EDGE_TO_EDGE
                    )
                }
                else -> {
                    return null
                }
            }
        }
        return currentTraits
    }
    // NOTE kotlin replaces functions below with lhs?.identifier == rhs?.identifier etc.
    // public func ==(lhs: ControllerSkinProtocol?, rhs: ControllerSkinProtocol?) -> Bool
    // {
    // return lhs?.identifier == rhs?.identifier
    // }
    //
    // public func !=(lhs: ControllerSkinProtocol?, rhs: ControllerSkinProtocol?) -> Bool
    // {
    // return !(lhs == rhs)
    // }
    //
    // public func ~=(pattern: ControllerSkinProtocol?, value: ControllerSkinProtocol?) -> Bool
    // {
    // return pattern == value
    // }
}

