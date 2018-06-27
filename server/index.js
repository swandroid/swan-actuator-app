const express = require('express');
const bodyParser = require("body-parser");

const app = express();

app.use(express.json());       // to support JSON-encoded bodies
app.use(express.urlencoded()); // to support URL-encoded bodies

app.post('/', function (req, res) {
    console.log(req.body.value);

    // res.setHeader('Content-Type', 'application/json');
    // res.send('{}');

    res.sendStatus(200);
});

app.listen(4445);
