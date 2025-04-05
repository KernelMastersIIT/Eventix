package com.example.kernel.Components

data class EventData(val eventTitle : String? = "",
                     var eventId: String = "",
                     val hashtags : String? = "",
                     val description : String? = "",
                     val venue : String? = "",
                     val date :String? = "",
                     val time : String? = "",
                     val postLink :String? = "")
