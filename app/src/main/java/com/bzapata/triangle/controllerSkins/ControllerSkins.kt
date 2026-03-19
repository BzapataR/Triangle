//
// ControllerSkins.kt
//
// Created by Brian Zapata Resendiz on 03/18/26
// COPYRIGHT © 2026 Brian Zapata All rights reserved.
//
// I should really add this heading more. This marks the first time I really look at swift code from
// the Delta repo and this comes first due to me wanting to see if deltaskins can map on android
// which I think it will.

// To note: JSON format
/*
{
    "name" : "display name in app
    "identifier" : "unique id for skin in reverse dns fashion. i.e. com.brian.gba.standard
    "gameTypeIdentifier" : "unique id for what console goes with the skin as defined in https://noah978.gitbook.io/delta-docs/skins
    "debug" : Bool to enable debug layout
    "representation" : {  this tells which file goes to which screen type and orientation along with buttons and positions
        iphone or ipad { will come down to WindowSize width
            standard or edge-to-edge splitView(ipad only) { this will probably come down to custom logic based on aspect ratio on device
                portrait/landscape {
                    assets : { pdf or png. Why pdf like wtf i never understood this
                             resizeable or small,medium,large (png only) : "filename"
                             ... rest if definition mostly
                         Mapping size { w x h (in DP) of skin image
                         input screen what part of the screen from the emulator you want to capture // original resolution
                         outscreen where to placae said input in screen
 */
package com.bzapata.triangle.controllerSkins

import android.content.Context
import android.graphics.PointF
import android.graphics.RectF
import android.service.controls.DeviceTypes
import android.util.Log
import android.view.WindowInsets
import androidx.collection.LruCache
import androidx.core.view.WindowInsetsCompat
import androidx.window.layout.WindowMetricsCalculator
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.util.zip.ZipFile

class ControllerSkins(val file : File) {
    val tag = "Controller Skins"

    //metadata from the info.json file
    var name: String = ""
    var identifier : String = ""
    var gameConsole : String = ""
    var debug : Boolean = false

    private val representations = mutableMapOf<Traits, Representation>()
    private val imageCache = LruCache<String, android.graphics.Bitmap>(6)
    private val zipFile = ZipFile(file)

    enum class Placement { CONTROLLER, APP }

    enum class Device { IPHONE, IPAD, TV}
    enum class DisplayType { STANDARD, EDGE_TO_EDGE, SPLIT_VIEW }
    enum class Orientation { PORTRAIT, LANDSCAPE }
    enum class Size { SMALL, MEDIUM, LARGE }


    data class Traits(
        val device : Device,
        val displayType : DisplayType,
        val orientation : Orientation,

        val description : String = device.name + "-" + displayType.name + displayType.name
    )
    fun defaults(context : Context, windowInsets : WindowInsetsCompat) : Traits {
        val resources = context.resources
        val configuration = resources.configuration

        //Getting device type from DP
        val device = when {
            configuration.smallestScreenWidthDp <= 600 -> Device.IPHONE
            configuration.smallestScreenWidthDp > 600 -> Device.IPAD
            else -> Device.TV
        }
        val metrics = resources.displayMetrics
        val orientation = if (metrics.widthPixels > metrics.heightPixels) Orientation.LANDSCAPE else Orientation.PORTRAIT

        val maxWindowMetricsBounds = WindowMetricsCalculator.getOrCreate().computeMaximumWindowMetrics(context).bounds
        val windowBounds = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(context).bounds
        val isSplitView = windowBounds.width() < maxWindowMetricsBounds.width() ||
                windowBounds.height() < maxWindowMetricsBounds.height()
        val isEdgeToEdge = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom > 0
        val displayType = when {
            isSplitView -> DisplayType.SPLIT_VIEW
            isEdgeToEdge -> DisplayType.EDGE_TO_EDGE
            else -> DisplayType.STANDARD

        }

        return Traits(
            device = device,
            displayType = displayType,
            orientation = orientation
        )
    }
    data class Screen(
        val id:String,
        val inputFrame : RectF? = null,
        val outputFrame : RectF? = null,
        val placement : Placement = Placement.CONTROLLER,
        //val filters : List<CIFilter>? = null will not implement this as there is no real Android equivalent and not too important
        val isTouchScreen : Boolean = false
    )
    data class Item (
        val id : String,
        val kind : Kind,
        val frame : RectF,
        val extendedFrame : RectF,
        val placement : Placement,
        val inputs : List<String> = emptyList()
    ) {
        enum class Kind { BUTTON, DPAD, THUMBSTICKS, TOUCHSCREEN }
        sealed class Inputs {
            data class Standard(val inputs : List<Inputs>) : Inputs()
            data class Directional(val up : Inputs, val down : Inputs, val left : Inputs, val right : Inputs) : Inputs()
            data class Touch(val x : Inputs, val y : Inputs) : Inputs()
            val allInputs : List<Inputs> get() = when(this) {
                is Standard -> inputs
                is Directional -> listOf(up, down, left, right)
                is Touch -> listOf(x,y)
            }
        }
    }
    private data class Representation(
        val traits : Traits,

        val assets : Map<String,String>,
        val isTranslucent : Boolean,
        val screens : List<String>?,
        val mappingSize : PointF,

        val items : List<Item>,

        val menuInsets : WindowInsets,

        val description : String
    )
    private fun loadSkin() {
        try {
            val entry = zipFile.getEntry("info.json") ?: return
            val jsonString = zipFile.getInputStream(entry).bufferedReader().use { it.readText() }
            val json = Json.parseToJsonElement(jsonString).jsonObject

            //metadata extraction
            this.name = json["name"]?.jsonPrimitive?.content ?: ""
            this.identifier = json["identifier"]?.jsonPrimitive?.content ?: ""
            this.debug = json["debug"]?.jsonPrimitive?.boolean ?: false
            this.gameConsole = json["representation"]?.jsonPrimitive?.content ?: ""

            val repsJson = json["representations"]?.jsonObject ?: return
            parseRepresentations(repsJson)
        }
        catch (e : Exception) {
            Log.e(tag , "Error loading skin", e)
        }
    }
    // In the words of Riley, Sometime recursion is the best solution
    private fun parseRepresentations(json : JsonObject, device : String? = null, display : String? = null) {
        json.forEach { (key, value) ->
            when{
                device == null -> parseRepresentations(value.jsonObject, key, null)
                display == null -> parseRepresentations(value.jsonObject, device, key)
                else -> {
                    val traits = Traits(device, display, key)
                    val repContent = value.jsonObject

                    val mappingWidth = repContent["mappingSize"]?.jsonObject?.get("width")?.jsonPrimitive?.float ?: 1f
                    val mappingHeight  = repContent["mappingHeight"]?.jsonObject?.get("height")?.jsonPrimitive?.float ?: 1f
                    val mappingSize = PointF(mappingWidth, mappingHeight)

                    val items = parseItems(repContent["items"]?.jsonArray , traits, mappingSize)
                    val screens = parseScreens(repContent["screens"]?.jsonArray, traits, mappingSize)

                    representations[traits] = Representation(
                        traits = traits,
                        assets = repContent["assets"]?.jsonObject?.mapValues { it.value.jsonPrimitive.content }
                            ?: emptyMap(),
                        screens = screens,
                        mappingSize = mappingSize,
                        items = items
                    )
                }
            }
        }
    }

    private fun parseItems(array: JsonArray?, traits: Traits, mappingSize: PointF) : List<Item> {
        return array?.mapIndexedNotNull { index, element ->
            val obj = element.jsonObject
            val frameObj = obj["frame"]?.jsonObject ?: return@mapIndexedNotNull null

            // Convert absolute coordinates to relative (0.0 - 1.0)
            val rawFrame = RectF(
                frameObj["x"]?.jsonPrimitive?.float ?: 0f,
                frameObj["y"]?.jsonPrimitive?.float ?: 0f,
                (frameObj["x"]?.jsonPrimitive?.float ?: 0f) + (frameObj["width"]?.jsonPrimitive?.float ?: 0f),
                (frameObj["y"]?.jsonPrimitive?.float ?: 0f) + (frameObj["height"]?.jsonPrimitive?.float ?: 0f)
            )

            Item(
                id = "${identifier}_${traits}_$index",
                kind = Item.Kind.BUTTON, // Simplified for brevity
                frame = RectF(rawFrame.left / mappingSize.x, rawFrame.top / mappingSize.y, rawFrame.right / mappingSize.x, rawFrame.bottom / mappingSize.y),
                extendedFrame = rawFrame, // Logic for extended edges would go here
                placement = Placement.CONTROLLER
            )
        } ?: emptyList()
    }
    private fun parseScreens(array : JsonArray?, traits: Traits, mappingSize: PointF) : List<Screen>? {
        return array?.mapIndexed { index, element ->
            val obj = element.jsonObject
            val outFrame = obj["outputFrame"]?.jsonObject

            Screen(
                id = "${identifier}_${traits}_screen_$index",
                outputFrame = outFrame?.let {
                    RectF(
                        it["x"]!!.jsonPrimitive.float / mappingSize.x,
                        it["y"]!!.jsonPrimitive.float / mappingSize.y,
                        (it["x"]!!.jsonPrimitive.float + it["width"]!!.jsonPrimitive.float) / mappingSize.x,
                        (it["y"]!!.jsonPrimitive.float + it["height"]!!.jsonPrimitive.float) / mappingSize.y
                    )
                },
                placement = if (outFrame == null) Placement.APP else Placement.CONTROLLER
            )
        }
    }

    fun getItems(traits : Traits) : List<Item>? = representations[traits]?.items
    fun getScreens(traits: Traits): List<Screen>? = representations[traits]?.screens
}