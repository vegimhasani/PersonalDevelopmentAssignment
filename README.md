# BuxAssignment

This is an technical assignment for a job interview at Bux. It consist of two screens represented by two fragments: `MainFragment` for the list
and `DetailFragment` for the detail screen.

The project uses the MVVM architecture and the libraries listed below to faciliate the implementation.

In the first screen there is a list with the hardcoded products defined in the `Products` class and displayed in the RecyclerView.

Once the product item is clicked, the productID of the product is passed to the `DetailFragment`, which then we query the product details for that
product and display the data once it's available.

In the details screen we also initiate the connection to the BUX web socket defined in the assignment in order to receive the real time data of the
product price change.



Libraries used on the sample project
------------------------------------

* [Android Architecture components][1]
* [Kotlin Coroutines & Flow][2]
* [Hilt][3]
* [Retrofit][4]
* [Tinder Scarlet][5]

[1]: https://developer.android.com/topic/libraries/architecture

[2]: https://developer.android.com/kotlin/flow

[3]: https://developer.android.com/training/dependency-injection/hilt-android

[4]: https://square.github.io/retrofit/

[5]: https://github.com/Tinder/Scarlet

Developed By
------------

* Vegim Hasani - <vegimhasani@gmail.com>
