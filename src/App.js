import React, { useState } from 'react';
import './App.css';

function App() {
  const [products] = useState([
    { id: 1, name: 'Product 1', price: 10.99, imageUrl: '/images/product1.svg' },
    { id: 2, name: 'Product 2', price: 20.99, imageUrl: '/images/product2.svg' },
    { id: 3, name: 'Product 3', price: 30.99, imageUrl: '/images/product3.svg' },
  ]);

  return (
    <div className="App">
      <h1>Products App</h1>
      <div className="products-container">
        {products.map(product => (
          <div key={product.id} className="product-card">
            <img src={product.imageUrl} alt={product.name} className="product-image" />
            <h3>{product.name}</h3>
            <p>${product.price}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default App;
