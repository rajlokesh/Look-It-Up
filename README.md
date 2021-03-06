# Look-It-Up

Look It Up is an Android mobile application which reads out the objects from a image taken from the app or from the phone's storage. This object detection application has been developed with the idea of aiding the blind. When used with headphones the user will hear the object's description in same direction as the same as where the object is present.

# Install and run

Copy app-release.apk file onto an android mobile with android version higher than 4.4 (KitKat) and install it.  
Open the app info for the application by dragging the app icon to the top of the screen and provide all permissions listed there.  
Run the application, you should see a screen like this.  
	
![alt text](https://res.cloudinary.com/dgxykz1au/image/upload/v1526106022/Screenshot_20180511-215850.png)
	
Click a picture or select a picture from the storage. The picture will be processed and the result will shown as below with bounding boxes around the objects detected and description on the top of it.  
	
![alt text](https://res.cloudinary.com/dgxykz1au/image/upload/v1526106030/Screenshot_20180511-220048.png)

Once you get the above screen the application will start reading out the description of the objects detected will be heard in the direction panned towards the direction where the object is present in the frame.  

# External Libraries used:
•	Retrofit - type-safe HTTP client for Android and Java  
•	Cloudinary - media management platform for mobile.  
  


# Functional description of important files :
AndroidManifest.xml - Describes the nature of the application and each of its components  
build.gradle (module) - This defines the module-specific build configurations.  
build.gradle (project) - This defines the build configuration that apply to all modules.  
build/ - Contains build outputs.  
libs/ - Contains private libraries.  
src/ - Contains all code and resource files for the module  
java/ - Contains Java code sources.  
gen/ - Contains the Java files generated by Android Studio, such as R.java file and interfaces created from AIDL files.  
res/ - Contains application resources, such as drawable files, layout files, and UI string.  
test/ - Contains code for local tests that run on host JVM.  
Activity_main.xml -  Design of the front end.  
MainActivity.java -  Has all the methods and calls for all the functions of the app. Starting point of the application. Some important functions  
imageSelect() – selecting image from storage  
getRealPAthFromURI() – getting path for the image file  
Call deep Ai() – performs the http post call with deepai api  
manipulateResults () – to select the required data from the json data  
showInTextView()- to show text in front end  
showBoundingBox() – to draw the boxes and descriptions around the objects detected.  
Couldinary()- uploading to the cloud  
Tts ()- text to speech and directional panning.  
 

	