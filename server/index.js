var express = require("express");
var app     = express();
var path    = require("path");

app.set('view engine', 'ejs');

app.get('/:directions', function(req, res){
  var directions_list = req.params.directions;
  res.render('index', {
    directions: directions_list
  });
});

app.listen(3000);
console.log("Running on port 3000");
