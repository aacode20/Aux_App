# Aux

Aux is an Android Application developed in AndroidStudio and integrated with Spotify that allows users in a group to share and add songs to a master queue hosted by any of the members. Aux leverages a Firebase database in order to send and receive songs information, namely Spotify URIs, between participants and the host in what is known as a "Party". Firebase allows for applications that use it to respond to changes to the database as they are notified once it does, in this case Aux will receive an update when a participant shares a song from Spotify to it. Finally, Aux parses a song when it is shared to and takes full advantage of the newly updated Spotify SDK for Android to add the song to the host's queue, ready to play.

## Getting Started

The application is still under development.

## Built With

* [Spotify SDK for Android](https://developer.spotify.com/documentation/android/)
* [Firebase](https://firebase.google.com/docs/) - Database System
* [AndroidStudio](https://developer.android.com/docs/) - Used to develop and deploy Aux

## Contributing

Please read [CONTRIBUTING.md](https://github.com/srendano/Aux_App/blob/master/CONTRIBUTING.MD) for details on our code of conduct, and the process for submitting pull requests to us.
 

## Authors

* **Spencer Rendano** - [srendano](https://github.com/srendano)
* **Harshal Shukla** - [HarshulShuk](https://github.com/HarshulShuk)
* **Armand Asnani** - [aacode20](http://github.com/aacode20)
* **Steve Omeis** - [steveo539](https://github.com/Steveo539)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
