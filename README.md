# Holodeck

Holodeck is a simple POC Android application made for the Nuxeo VR Hackathon 2017.

## Features
- Browse digital assets in a [Nuxeo](https://www.nuxeo.com/) server in VR.
- Navigate thru assets using (bluetooth) game controller, or keyboard connected to the Android device.
- Text-to-speech.
- Speech recognition (just say "Yes" if the 'Assistant' asks if you want to change the "holodeck" ;)
- Uses [Google VR SDK for Android](https://developers.google.com/vr/android/) for a better native experience. 
- Simple to use connection to a Nuxeo server, using [Polymer](https://www.polymer-project.org) and [nuxeo-elements](https://github.com/nuxeo/nuxeo-elements).
- Hackathon code quality ;)

## Installation

#### 1. Clone project
```
git clone https://github.com/adilioaraujo/holodeck.git
```

#### 2. Import project in Android Studio (or IntelliJ Idea)
Gradle based Android project

#### 3. Configure Nuxeo server url

Edit file
```
holodeck/web/index.html
```
And set Nuxeo server url
```html
<body>
   <vr-app id="app" server="http://localhost:8080/nuxeo/"></vr-app>
</body>

```

#### 4. Build Polymer app
```
cd holodeck/web
bower install
polymer build
```
Then copy the contents of
```
holodeck/web/build/bundled/*
```
to
```
holodeck/app/src/main/assets/web/*
```

#### 5. Build the Android project and have fun !!
