var express = require("express");
var app     = express();
var path    = require("path");

app.use('/js', express.static(__dirname + '/node_modules/bootstrap/dist/js')); // redirect bootstrap JS
app.use('/js', express.static(__dirname + '/node_modules/jquery/dist')); // redirect JS jQuery
app.use('/css', express.static(__dirname + '/node_modules/bootstrap/dist/css')); // redirect CSS bootstrap
app.use('/js', express.static(__dirname + '/node_modules/swiped/dist'));
app.set('view engine', 'ejs');

app.get('/:steps', function(req, res){
  var path = "";
  var steps_list = req.params.steps;

  var buf = Buffer.from(steps_list, 'base64').toString('utf8');

  path = buf.replace(/-/g, '<li>').replace(/\./g, '</li>');

  path = "<ul class='list1'>" + path + "</ul>";

  res.render('index', {
    path: path
  });
});

app.listen(3000);
console.log("Running on port 3000");
