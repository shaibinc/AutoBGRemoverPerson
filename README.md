# AutoBGRemoverPerson

Version 1.0.0

We are excited to announce the release of our new Android library, Auto Background Remover. This library utilizes Google ML's Selfie Segmentation Kit to automatically remove backgrounds from images of people, making it easy to create professional-looking images and videos.

Some of the key features of the library include:

Automatic background removal for images of people
Easy integration with your existing Android app
Utilizes Google's state-of-the-art machine learning technology for accurate results
We have thoroughly tested the library and have included detailed documentation to help you get started quickly. We hope you find this library useful and we look forward to hearing your feedback!


Installation

To use this library, you can include it in your app by adding the following to your app's build.gradle file:

Copy code

`dependencies {
      implementation 'com.github.shaibinc:AutoBGRemoverPerson:1.0'
}`

Usage

To use the library, simply create an instance of the BackgroundRemover class and call the removeBackground method, passing in the image or video frame you want to process. You will also need to pass in the context of your app and your API key.

Copy code
```
new BgRemover().bitmapSegmentation(context,
            bitmap,new BgRemoverListener() {
            
                @Override
                public void onSuccess(Bitmap bitmap) {
                    // Handle successful bitmap segmentation here
                }

                @Override
                public void onFailed(Exception exception) {
                    Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });
```


We hope you enjoy using this library and we look forward to your feedback!
