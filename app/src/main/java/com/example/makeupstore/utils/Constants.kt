package com.example.makeupstore.utils

import com.example.makeupstore.R
import com.example.makeupstore.models.ProductType

object Constants {
    const val BASE_URL = "https://makeup-api.herokuapp.com/api/v1/"
    const val RC_SIGN_IN = 123

    val PRODUCT_TYPE = listOf<ProductType>(
        ProductType("blush", R.drawable.blush, R.color.color1),
        ProductType("bronzer", R.drawable.bronzer, R.color.color2),
        ProductType("eyebrow", R.drawable.eyebrow, R.color.color3),
        ProductType("eyeliner", R.drawable.eyeliner, R.color.color4),
        ProductType("eyeshadow", R.drawable.eyeshadow, R.color.color5),
        ProductType("foundation", R.drawable.foundation, R.color.color6),
        ProductType("lip_liner", R.drawable.lip_liner, R.color.color7),
        ProductType("lipstick", R.drawable.lipstick, R.color.color8),
        ProductType("mascara", R.drawable.mascara, R.color.color9),
        ProductType("nail_polish", R.drawable.nail_polish, R.color.color10),

        )
    val typesNames = arrayOf(
        "all Product",
        "blush",
        "bronzer",
        "eyebrow",
        "eyeliner",
        "eyeshadow",
        "foundation",
        "lip_liner",
        "lipstick",
        "mascara",
        "nail_polish"
    )
    val typesImages = arrayOf(
        R.drawable.home,
        R.drawable.blush,
        R.drawable.bronzer,
        R.drawable.eyebrow,
        R.drawable.eyeliner,
        R.drawable.eyeshadow,
        R.drawable.foundation,
        R.drawable.lip_liner,
        R.drawable.lipstick,
        R.drawable.mascara,
        R.drawable.nail_polish,
    )
}