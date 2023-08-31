package com.example.momo_interview.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Color(
    val name: String,
    val code: String
) : Parcelable


@Parcelize
data class Variant(
    @Json(name = "color_code") val colorCode: String,
    val size: String,
    val stock: Int
) : Parcelable


//@TypeConverters(StylishConverters::class)
@Parcelize
data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: Int,
    val texture: String,
    val wash: String,
    val place: String,
    val note: String,
    val story: String,
    val colors: List<Color>,
    val sizes: List<String>,
    val variants: List<Variant>,
    @Json(name = "main_image") val mainImage: String,
    val images: List<String>
) : Parcelable

@Parcelize
data class Item(val number: String, var isFavorite: Boolean): Parcelable {
    companion object {
        fun getDefaultItems(size: Int): List<Item> {
            return List(size) { index ->
                Item(number = "Goods ${index + 1}", isFavorite = false)
            }
        }
    }
}

@Parcelize
data class Promo(val title: String): Parcelable {
    companion object {
        fun getDefaultPromo(size: Int): List<Promo> {
            return List(size) {
                Promo(title = "promo")
            }
        }
    }
}
@Parcelize
data class ProductListResult(
    val error: String? = null,
    @Json(name = "data") val products: List<Product>? = null,
    // mock data
    val item: List<Item> = Item.getDefaultItems(50),
    val promo: List<Promo> = Promo.getDefaultPromo(9)
) : Parcelable

