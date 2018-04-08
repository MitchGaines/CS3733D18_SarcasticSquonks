var express = require("express");
var app     = express();
var path    = require("path");

app.set('view engine', 'ejs');

app.get('/:lang/:steps', function(req, res){
  var path = "";
  var steps_list = req.params.steps;
  var language = req.params.lang;

  var buf = Buffer.from(steps_list, 'base64').toString('utf8')

  path = buf.replace(/\./g, '<br />');

  res.render('index', {
    path: path
  });
});

app.listen(3000);
console.log("Running on port 3000");
