package com.farmer.data.network.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Images(
//    @SerialName("meta")
//    val meta: Meta = Meta(""),
    @SerialName("result")
    val result: ReceiptResult = ReceiptResult()
) {
    @kotlinx.serialization.Serializable
    data class Meta(
        @SerialName("meta")
        val estimatedLanguage: String
    )

    @kotlinx.serialization.Serializable
    data class ReceiptResult(
        @SerialName("storeInfo")
        val storeInfo: StoreInfo? = StoreInfo(),
        @SerialName("paymentInfo")
        val paymentInfo: PaymentInfo? = PaymentInfo(),
//        @SerialName("subResults")
//        val subResults: ArrayList<SubResults> = arrayListOf(),
        @SerialName("totalPrice")
        val totalPrice: TotalPrice? = TotalPrice()
    ) {
        @kotlinx.serialization.Serializable
        data class StoreInfo(
            @SerialName("name")
            val name: Name? = Name(),
            // todo 아래는 임시로 주석 처리 해둠. 필요하면 추가하고 사용
//            @SerialName("subName")
//            val subName: SubName? = SubName(),
//            @SerialName("bizNum")
//            val bizNum: BizNum? = BizNum(),
//            @SerialName("addresses")
//            val addresses: ArrayList<Addresses> = arrayListOf(),
//            @SerialName("tel")
//            val tel: ArrayList<Tel> = arrayListOf()
        ) {

            @kotlinx.serialization.Serializable
            data class Name(
                @SerialName("text") val text: String? = null,
                // todo 아래는 임시로 주석 처리 해둠. 필요하면 추가하고 사용
//                @SerialName("formatted") val formatted: Formatted? = Formatted(),
//                @SerialName("boundingPolys") val boundingPolys: ArrayList<BoundingPolys> = arrayListOf(),
//                @SerialName("maskingPolys") val maskingPolys: ArrayList<String> = arrayListOf()
            )
        }

        @kotlinx.serialization.Serializable
        data class PaymentInfo(
            @SerialName("date") val date: Date? = Date(),
            @SerialName("time") val time: Time? = Time(),
            @SerialName("cardInfo") val cardInfo: CardInfo? = CardInfo(),
//            @SerialName("confirmNum") val confirmNum: ConfirmNum? = ConfirmNum()
        ) {
            @kotlinx.serialization.Serializable
            data class Date(
                @SerialName("text") val text: String? = null,
                @SerialName("formatted") val formattedDate: FormattedDate? = FormattedDate(),
//                @SerialName("boundingPolys" ) val boundingPolys : ArrayList<BoundingPolys> = arrayListOf(),
//                @SerialName("maskingPolys"  ) val maskingPolys  : ArrayList<String>        = arrayListOf()
            ) {
                @kotlinx.serialization.Serializable
                data class FormattedDate(
                    @SerialName("year")
                    val year: String? = null,
                    @SerialName("month")
                    val month: String? = null,
                    @SerialName("day")
                    val day: String? = null
                )
            }

            @kotlinx.serialization.Serializable
            data class Time(
                @SerialName("text") val text: String? = null,
//                @SerialName("formatted"     ) val formatted     : Formatted?               = Formatted(),
//                @SerialName("boundingPolys" ) val boundingPolys : ArrayList<BoundingPolys> = arrayListOf()
            )


            @kotlinx.serialization.Serializable
            data class CardInfo(
                @SerialName("company") val company: Company? = Company(),
//                @SerialName("number") val number: Number? = Number()
            ) {
                @kotlinx.serialization.Serializable
                data class Company(
                    @SerialName("text") val text: String? = null,
//                    @SerialName("formatted"     ) val formatted     : Formatted?               = Formatted(),
//                    @SerialName("boundingPolys" ) val boundingPolys : ArrayList<BoundingPolys> = arrayListOf(),
//                    @SerialName("maskingPolys"  ) val maskingPolys  : ArrayList<String>        = arrayListOf()
                )
            }
        }

//        @kotlinx.serialization.Serializable
//        data class SubResults(
//            @SerialName("items") val items: ArrayList<Items> = arrayListOf()
//        ) {
//
//        }

        @kotlinx.serialization.Serializable
        data class TotalPrice(
            @SerialName("price") val price: Price? = Price()
        ) {
            @kotlinx.serialization.Serializable
            data class Price(
                @SerialName("text") val text: String? = null,
//                @SerialName("formatted"     ) val formatted     : Formatted?               = Formatted(),
//                @SerialName("boundingPolys" ) val boundingPolys : ArrayList<BoundingPolys> = arrayListOf()

            )
        }
    }
}
