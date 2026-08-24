import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';

function ProductList() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const username = localStorage.getItem('username');

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await api.get('/api/products');
      setProducts(response.data);
    } catch (err) {
      if (err.response?.status === 401) {
        handleLogout();
      } else {
        setError('Failed to load products');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    navigate('/login');
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure?')) return;
    try {
      await api.delete(`/api/products/${id}`);
      setProducts(products.filter((p) => p.id !== id));
    } catch (err) {
      setError('Failed to delete product');
    }
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div style={{ maxWidth: '800px', margin: '2rem auto', padding: '1rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Products</h1>
        <div>
          <span style={{ marginRight: '1rem' }}>Hello, {username}</span>
          <button onClick={handleLogout} style={{ cursor: 'pointer' }}>Logout</button>
        </div>
      </div>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #ddd' }}>
            <th style={{ textAlign: 'left', padding: '0.75rem' }}>ID</th>
            <th style={{ textAlign: 'left', padding: '0.75rem' }}>Name</th>
            <th style={{ textAlign: 'left', padding: '0.75rem' }}>Price</th>
            <th style={{ textAlign: 'left', padding: '0.75rem' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id} style={{ borderBottom: '1px solid #eee' }}>
              <td style={{ padding: '0.75rem' }}>{product.id}</td>
              <td style={{ padding: '0.75rem' }}>{product.name}</td>
              <td style={{ padding: '0.75rem' }}>${product.price}</td>
              <td style={{ padding: '0.75rem' }}>
                <button onClick={() => handleDelete(product.id)} style={{ color: 'red', cursor: 'pointer' }}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {products.length === 0 && <p>No products found.</p>}
    </div>
  );
}

export default ProductList;
