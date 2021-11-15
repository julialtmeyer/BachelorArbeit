const express = require('express')
const path = require("path");
const app = express()
const port = 3000;
require('dotenv').config();
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname,'/index.html'))
})

app.get("/geturl", function(req, res){
    res.json({ url: process.env.BACKEND_URL });
});

app.use('/img',express.static(path.join(__dirname, '/assets/img')))
app.use('/css',express.static(path.join(__dirname, '/assets/css')))
app.use('/js',express.static(path.join(__dirname, '/scripts')))

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})