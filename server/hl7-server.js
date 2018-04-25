var xml     = require("xml");
var body_parser = require("body-parser");
var hl7     = require('node-hl7');
var app     = hl7.Server();
require('body-parser-xml')(body_parser);

/*app.use(body_parser.xml({
  limit: '10MB',   // Reject payload bigger than 1 MB
  xmlParseOptions: {
    normalize: true,     // Trim whitespace inside text nodes
    normalizeTags: true, // Transform tags to lowercase
    explicitArray: false // Only put nodes in array if >1
  }
}));*/

app.on('hl7', function(msg){
    // Do whatever you wanna do.
});



app.listen(5000);
console.log("Running on port 5000");
