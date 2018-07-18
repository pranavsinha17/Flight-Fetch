# Flight-Fetch
An android app which will fetch the flight data from the web services and display them for our need.
This is an app which is connected to the web services and will fetch the data from these services.
Blaze Flight do the same thing in the following manner :
<br><p align="center">![37339638_1392611087550306_6199930878282432512_n](https://user-images.githubusercontent.com/29634475/42865197-83b19788-8a86-11e8-87ff-7d2e9c43da9d.jpg)<p></br>


 
First of all we need to give storage permission to our app(as the output has to be saved in the csv format).The fantastic part of our app is that it runs on a marshmellow platform and we have designed it so that if you click the floating action button below it will give you an alert box by which you can give the permission directly without being redirected to the permission page.
<br><p align="center">![37488821_1392611034216978_2453686546730057728_n](https://user-images.githubusercontent.com/29634475/42868760-c6f4b994-8a90-11e8-9443-5601da48a856.jpg)<p></br> 
Now after we got the permission,we will enter the origin ,destination and the date of travelling(optional as the person wants to search for different choices).It also includes our data validation checker.

<br><b>Note: The Api we have used allows you to find the prices of one-way flights between two airports over a large number of dates, and for a large variety of stay durations. The search doesn't return exact itineraries, but rather tells you the best price for a flight on a given day, for a stay of a given duration.The search is based on its Extreme Search platform, which continually caches a large number of flight search results from a list of origin cities to a variety of destinations. Since it's a cached search, the response time is fast, but not all origin airports are available.</b>
 

Now we will get our required result and that result will be exported and saved as csv file in the local storage.To make it look good,our result page will be displayed in the parallax view.
<br><p align="center">![37298500_1392626730882075_66487854678671360_n](https://user-images.githubusercontent.com/29634475/42866290-c7963b40-8a89-11e8-87b5-a1bb0c3ef78d.jpg)<p></br>
 
This is a sample of the csv file which will be generated from our results.
<br><p align="center">![37292419_1392611117550303_8082917924660051968_n](https://user-images.githubusercontent.com/29634475/42866241-ab15ca80-8a89-11e8-9b34-14e57c0e8568.jpg)<p></br>

<br>We have used :
<br><b>AsyncTask</b>
<br><b>HttpUrlConnection</b>
<br><b>CSV Writer</b>
