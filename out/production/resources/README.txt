Put resources needed by your application here, e.g. data files, images, FXML files, etc. Anything stored here
will be packaged at the top level of the jar file, so to access "hello.png", just use .getResource("hello.png");
If you organize your resources into folders, the same hierarchy applies; e.g. if you store "hello.png" here in
a folder called "images", you'd use .getResource("images/hello.png");

THESE RESOURCES ARE IMMUTABLE, *DON'T* TRY TO WRITE TO THEM IN JAVA CODE! YOU CAN'T PUT A DATABASE HERE!
