import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import ProductList from './components/ProductList';

function PrivateRoute({ children }) {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" />;
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/products"
          element={
            <PrivateRoute>
              <ProductList />
            </PrivateRoute>
          }
        />
        <Route path="/" element={<Navigate to="/products" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
