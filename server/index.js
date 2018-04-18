var express = require("express");
var app     = express();
var path    = require("path");
var xml     = require("xml");
var body_parser = require("body-parser");

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

var xml_list = {};

app.get('/:steps', function(req, res){
  var path = "";
  var steps_list = req.params.steps;

  var buf = Buffer.from(steps_list, 'base64').toString('utf8');

  path = buf.replace(/-/g, '<li class="list-group-item">').replace(/\./g, '</li>');

  path = "<ul class='list-group list-group-flush col-sm-12' id='path-list'>" + path + "</ul>";

  res.render('index', {
    path: path
  });
});

app.post('/twiml/:twiml_steps', function(req, res) {
  var twiml_steps = req.params.twiml_steps;
  var steps_xml = decodeURIComponent(twiml_steps);

  var final_steps = steps_xml.replace(/\+/g, ' ');
  console.log(final_steps);
  res.set('Content-Type', 'text/xml');
  res.send(final_steps.replace(".xml", ""));
});

app.post('/twiml/new_xml/:key', function(req, res, body) {
  xml_list[req.params.key] = body;
  console.log(xml_list[req.params.key]);
});



app.listen(3000);
console.log("Running on port 3000");
