var xml     = require("xml");
var body_parser = require("body-parser");
var hl7     = require('simple-hl7');
var app     = hl7.tcp();
require('body-parser-xml')(body_parser);

app.use('/js', express.static(__dirname + '/node_modules/bootstrap/dist/js')); // redirect bootstrap JS
app.use('/js', express.static(__dirname + '/node_modules/jquery/dist')); // redirect JS jQuery
app.use('/css', express.static(__dirname + '/node_modules/bootstrap/dist/css')); // redirect CSS bootstrap
app.use(body_parser.xml({
  limit: '10MB',   // Reject payload bigger than 1 MB
  xmlParseOptions: {
    normalize: true,     // Trim whitespace inside text nodes
    normalizeTags: true, // Transform tags to lowercase
    explicitArray: false // Only put nodes in array if >1
  }
}));
app.set('view engine', 'ejs');



app.listen(8080);
console.log("Running hl7 server on port 8080");
