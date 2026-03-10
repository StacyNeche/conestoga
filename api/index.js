const express = require('express');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(express.static('../server/public'));

const products = [
  { id: 1, name: 'Product 1', price: 10.99, imageUrl: '/images/product1.svg' },
  { id: 2, name: 'Product 2', price: 20.99, imageUrl: '/images/product2.svg' },
  { id: 3, name: 'Product 3', price: 30.99, imageUrl: '/images/product3.svg' },
];

app.get('/api/products', (req, res) => {
  res.json(products);
});

module.exports = app;